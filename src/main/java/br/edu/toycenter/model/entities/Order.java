package br.edu.toycenter.model.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order")
public class Order {
	@Id
	private String id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant moment;
	private String clientId;
	private List<OrderItem> orderItens = new ArrayList<>();
	
	public Double getTotal() {
		Double sum = 0.0;
		for (OrderItem item : getOrderItens()) {
			sum += item.getSubTotal();
		}
		return sum;
	}
}
