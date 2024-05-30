package br.edu.toycenter.infrastructure.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(collection = "order")
public class Order {
	
	@Id
	private String id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant moment;
	private String clientId;
	private List<OrderItem> orderItens = new ArrayList<>();
	
	public Order() {
		
	}
	
    public Order(String id, Instant moment, String clientId, List<OrderItem> orderItens) {
		this.id = id;
		this.moment = moment;
		this.clientId = clientId;
		this.orderItens = orderItens;
	}

	public Order(Builder builder) {
    	id = builder.id;
    	moment = builder.moment;
    	clientId = builder.clientId;
    	orderItens = builder.orderItens;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Instant getMoment() {
		return moment;
	}

	public void setMoment(Instant moment) {
		this.moment = moment;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public List<OrderItem> getOrderItens() {
		return orderItens;
	}

	public void setOrderItens(List<OrderItem> orderItens) {
		this.orderItens = orderItens;
	}
	
	public Double getTotal() {
		Double sum = 0.0;
		for (OrderItem item : getOrderItens()) {
			sum += item.getSubTotal();
		}
		return sum;
	}
	

	@Override
	public String toString() {
		return "Order [id=" + id + ", moment=" + moment + ", clientId=" + clientId + ", orderItens=" + orderItens + "]";
	}

	public static class Builder {

        private String id;
        private Instant moment;
        private String clientId;
        private List<OrderItem> orderItens;

        public Builder id(String value) {
        	id = value;
            return this;
        }

        public Builder moment(Instant value) {
        	moment = value;
            return this;
        }


        public Builder clientId(String value) {
        	clientId = value;
            return this;
        }
        
        public Builder orderItens(List<OrderItem> value) {
        	orderItens = value;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
