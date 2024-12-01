package br.edu.toycenter.model.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	
	private Integer quantity;
	private Double price;
	private Product product;


	public Double getSubTotal() {
		return price * quantity;
	}
}
