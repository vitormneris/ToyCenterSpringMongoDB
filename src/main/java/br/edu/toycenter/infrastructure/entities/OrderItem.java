package br.edu.toycenter.infrastructure.entities;

import lombok.ToString;

@ToString
public class OrderItem {
	
	private Integer quantity;
	private Double price;
	private Product product;
	
	public OrderItem() {
		
	}
	
    public OrderItem(Integer quantity, Double price, Product product) {
		this.quantity = quantity;
		this.price = price;
		this.product = product;
	}

	public OrderItem(Builder builder) {
    	quantity = builder.quantity;
    	price = builder.price;
    	product = builder.product;
    }

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public Double getSubTotal() {
		return price * quantity;
	}

	@Override
	public String toString() {
		return "OrderItem [quantity=" + quantity + ", price=" + price + ", product=" + product + "]";
	}

	public static class Builder {

        private Integer quantity;
        private Double price;
        private Product product;

        public Builder quantity(Integer value) {
        	quantity = value;
            return this;
        }
        
        public Builder price(Double value) {
        	price = value;
        	return this;
        }

        public Builder product(Product value) {
        	product = value;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }

}
