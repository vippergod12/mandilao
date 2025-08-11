package ws.prj.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ws.prj.dto.response.PaymentResponse;
import ws.prj.entity.Payment;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface PaymentMapper {

    @Mapping(source = "tables.name", target = "name_table")
//    @Mapping(source = "orders.orderDetails", target = "orderDetailResponseList")
    @Mapping(source = "orders.id", target = "id_order")
    PaymentResponse toPaymentResponse(Payment payment);
}
