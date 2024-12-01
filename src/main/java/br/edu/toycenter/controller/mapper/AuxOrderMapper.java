package br.edu.toycenter.controller.mapper;

import br.edu.toycenter.controller.dto.response.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import br.edu.toycenter.controller.dto.request.AuxOrderRequestDTO;
import br.edu.toycenter.controller.dto.request.OrderItemRequestDTO;
import br.edu.toycenter.controller.dto.request.OrderRequestDTO;
import br.edu.toycenter.model.entities.Order;

@Component
@RequiredArgsConstructor
public class AuxOrderMapper {
    private final OrderMapper orderMapper;

    public OrderRequestDTO forOrderRequestDTO(AuxOrderRequestDTO auxOrderRequestDTO) {
        Order order = new Order();
        order.setId(auxOrderRequestDTO.id());
        order.setClientId(auxOrderRequestDTO.clientId());
        OrderItemRequestDTO orderItemDTO = new OrderItemRequestDTO(auxOrderRequestDTO.quantity(), auxOrderRequestDTO.productId());
        OrderRequestDTO orderRequestDTO = orderMapper.forOrderRequestDTO(order);
        orderRequestDTO.orderItems().add(orderItemDTO);
        return orderRequestDTO;
    }

    public AuxOrderRequestDTO forAuxOrderRequestDTO(OrderResponseDTO orderRequestDTO, int index) {
        return new AuxOrderRequestDTO(
                orderRequestDTO.id(),
                orderRequestDTO.client().getId(),
                orderRequestDTO.orderItens().get(index).quantity(),
                orderRequestDTO.orderItens().get(index).product().getId());
    }
}
