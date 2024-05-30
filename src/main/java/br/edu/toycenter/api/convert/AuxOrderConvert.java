package br.edu.toycenter.api.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.toycenter.api.request.AuxOrderRequestDTO;
import br.edu.toycenter.api.request.OrderItemRequestDTO;
import br.edu.toycenter.api.request.OrderRequestDTO;
import br.edu.toycenter.infrastructure.entities.Order;
import br.edu.toycenter.infrastructure.repositories.OrderRepository;

@Component
public class AuxOrderConvert {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderConvert orderConvert;

    public OrderRequestDTO forOrderRequestDTO(AuxOrderRequestDTO auxOrderRequestDTO) {
        Order order = new Order();
        order.setId(auxOrderRequestDTO.id());
        order.setClientId(auxOrderRequestDTO.clientId());
        OrderItemRequestDTO orderItemDTO = new OrderItemRequestDTO(auxOrderRequestDTO.quantity(), auxOrderRequestDTO.productId());
        OrderRequestDTO orderRequestDTO = orderConvert.forOrderRequestDTO(order);
        orderRequestDTO.orderItems().add(orderItemDTO);
        return orderRequestDTO;
    }
}
