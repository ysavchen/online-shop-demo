package com.mycompany.online_shop_backend.controller;

import com.mycompany.online_shop_backend.controller.model.request.OrderRequest;
import com.mycompany.online_shop_backend.controller.model.response.CreatedOrderResponse;
import com.mycompany.online_shop_backend.controller.model.response.UserOrderDto;
import com.mycompany.online_shop_backend.service.OrderService;
import com.mycompany.online_shop_backend.service.SecurityService;
import com.mycompany.online_shop_backend.service.model.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;

    @PostMapping(path = "/api/v1/orders", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedOrderResponse createOrder(@RequestBody OrderRequest request) {
        OrderDto orderDto = orderService.save(request);
        return CreatedOrderResponse.toDto(orderDto);
    }

    @GetMapping(path = "/api/v1/users/{id}/orders")
    public List<UserOrderDto> getUserOrders(HttpServletRequest request) {
        String email = securityService.getUsernameFromRequest(request);
        List<OrderDto> orderDtos = orderService.getOrdersByEmail(email);
        return orderDtos.stream().map(UserOrderDto::toDto).toList();
    }
}
