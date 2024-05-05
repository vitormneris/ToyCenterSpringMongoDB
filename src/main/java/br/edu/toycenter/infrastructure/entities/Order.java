package br.edu.toycenter.infrastructure.entities;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order")
public class Order {
	
	@Id
	private String id;
	private Instant moment;
	private String userId;
	private List<OrderItem> orderItens;
	
	public Order() {
		
	}
	
    public Order(String id, Instant moment, String userId, List<OrderItem> orderItens) {
		super();
		this.id = id;
		this.moment = moment;
		this.userId = userId;
		this.orderItens = orderItens;
	}

	public Order(Builder builder) {
    	id = builder.id;
    	moment = builder.moment;
    	userId = builder.userId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<OrderItem> getOrderItens() {
		return orderItens;
	}

	public void setOrderItens(List<OrderItem> orderItens) {
		this.orderItens = orderItens;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		return "Order [id=" + id + ", moment=" + moment + ", userId=" + userId + ", orderItens=" + orderItens + "]";
	}

	public static class Builder {

        private String id;
        private Instant moment;
        private String userId;
        private List<OrderItem> orderItens;

        public Builder id(String value) {
        	id = value;
            return this;
        }

        public Builder moment(Instant value) {
        	moment = value;
            return this;
        }


        public Builder userId(String value) {
        	userId = value;
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
