package ws.prj.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ws.prj.dto.response.OrderDetailResponse;
import ws.prj.entity.OrderDetail;
import ws.prj.entity.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(source = "product.name", target = "name")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    List<OrderDetailResponse> toOrderDetailResponseList(List<OrderDetail> list);


}
