package com.mycompany.online_shop_backend.controllers;

import com.mycompany.online_shop_backend.dto.request.OrderRequest;
import com.mycompany.online_shop_backend.dto.response.CreatedOrderResponse;
import com.mycompany.online_shop_backend.dto.response.UserOrderDto;
import com.mycompany.online_shop_backend.dto.services.OrderDto;
import com.mycompany.online_shop_backend.services.OrderService;
import com.mycompany.online_shop_backend.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;

    @PostMapping(
            path = "/api/v1/orders",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedOrderResponse createOrder(@RequestBody OrderRequest request) {
        OrderDto orderDto = orderService.save(request);
        return CreatedOrderResponse.toDto(orderDto);
    }

    @GetMapping(
            path = "/api/v1/users/{id}/orders",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<UserOrderDto> getUserOrders(HttpServletRequest request) {
        String email = securityService.getUsernameFromRequest(request);
        List<OrderDto> orderDtos = orderService.getOrdersByEmail(email);
        return orderDtos.stream().map(UserOrderDto::toDto).toList();
    }
}
