package com.restaurant.spring.mapper;

import com.restaurant.spring.dto.OrderDto;
import com.restaurant.spring.mapper.security.AccountMapper;
import com.restaurant.spring.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses = {AccountMapper.class})
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto orderDto);

    List<OrderDto> toOrderDtoList(List<Order> order);

    List<Order> toOrderList(List<OrderDto> orderDto);
}
