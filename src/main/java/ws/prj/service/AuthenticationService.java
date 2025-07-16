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
import ws.prj.dto.response.AuthenticationResponse;
import ws.prj.dto.response.IntrospectResponse;
import ws.prj.entity.User;
import ws.prj.exception.AppException;
import ws.prj.exception.ErrorCode;
import ws.prj.repository.InvalidatedTokenRepository;
import ws.prj.repository.UserRepository;
import ws.prj.service.impl.UserServiceImpl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserServiceImpl userService;
    InvalidatedTokenRepository invalidatedTokenRepository;
    private final UserRepository userRepository;

//    @NonFinal // không inject vào constructor
//    protected static final String SIGNER_KEY = "756dpVcRhjfE9GySMJmhN7A+zZ27tx5MoRqwX3CScB2j3fs8tfQgU+ft6+6rh3P6";

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY; //đọc biến từ file properties  hoặc yaml.

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
            var token = request.getToken();

            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            Date expiry = signedJWT.getJWTClaimsSet().getExpirationTime(); // lay duoc time cua token

            var verify = signedJWT.verify(verifier);

            return IntrospectResponse.builder()
                    .valid(verify && expiry.after(new Date()))
                    .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JsonEOFException, ParseException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        try{
            var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->
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

    private String generateToken(User user){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //các data trong body gọi là claimset
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // đại diện cho user đăng nhập
                .issuer("tien") // thường  là domain service
                .issueTime(new Date()) //Lấy thời điểm hịiện tại
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) //Thời  hạn token
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
            user.getRoles().forEach(s -> stringJoiner.add(s.getName()));

        return stringJoiner.toString();
    }
}
