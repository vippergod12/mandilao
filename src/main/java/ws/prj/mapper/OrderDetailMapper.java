package ws.prj.mapper;

import org.mapstruct.Mapper;
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.entity.OrderDetail;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
    List<OrderDetailResponse> toOrderDetailResponses(List<OrderDetail> orderDetails);

}
