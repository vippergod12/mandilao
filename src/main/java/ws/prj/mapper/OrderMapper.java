package ws.prj.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ws.prj.dto.request.OrderRequest;
import ws.prj.dto.response.OrderReponse;
import ws.prj.entity.Orders;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "id_table", target = "tables.id")
    Orders toEntity(OrderRequest request);
    @Mapping(source = "orderDetails", target = "orderDetails")
    OrderReponse toOrderResponse(Orders order);


}
