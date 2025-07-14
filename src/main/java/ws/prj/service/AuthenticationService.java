package ws.prj.service;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ws.prj.dto.request.AuthenticationRequest;
import ws.prj.dto.request.IntrospectRequest;
import ws.prj.dto.request.LogoutRequest;
import ws.prj.dto.response.AuthenticationResponse;
import ws.prj.dto.response.IntrospectResponse;
import ws.prj.entity.InvalidatedToken;
import ws.prj.entity.User;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.repository.InvalidatedTokenRepository;
import ws.prj.repository.UserResponseDAO;
import ws.prj.service.impl.UserServiceImpl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserServiceImpl userService;
    InvalidatedTokenRepository invalidatedTokenRepository;
    private final UserResponseDAO userResponseDAO;

//    @NonFinal // không inject vào constructor
//    protected static final String SIGNER_KEY = "756dpVcRhjfE9GySMJmhN7A+zZ27tx5MoRqwX3CScB2j3fs8tfQgU+ft6+6rh3P6";

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY; //đọc biến từ file properties  hoặc yaml.

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
            var token = request.getToken();
            boolean isValid = true;
            try {
                verifyToken(token);
            }catch(AppException e){
                isValid = false;
            }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JsonEOFException, ParseException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        try{
            var user = userResponseDAO.findByUsername(request.getUsername()).orElseThrow(() ->
                    new AppException(ErrorCode.USER_NOT_EXISTED));
            boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

            if(!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

            var token = generateToken(user);
            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();

        }catch(AppException e){
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }
    
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signJWTToken = verifyToken(request.getToken());

        String jwtId = signJWTToken.getJWTClaimsSet().getJWTID();
        Date expireTime = signJWTToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jwtId)
                .expiryTime(expireTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiry = signedJWT.getJWTClaimsSet().getExpirationTime(); // lay duoc time cua token

        var verify = signedJWT.verify(verifier);
        if(!verify && expiry.after(new Date()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    private String generateToken(User user){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //các data trong body gọi là claimset
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // đại diện cho user đăng nhập
                .issuer("tien") // thường  là domain service
                .issueTime(new Date()) //Lấy thời điểm hịiện tại
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) //Thời  hạn token
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); //thuật toán ký và giải mã trùng nhau https://generate-random.org/
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

}
