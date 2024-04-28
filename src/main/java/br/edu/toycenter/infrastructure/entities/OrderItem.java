package br.edu.toycenter.infrastructure.entities;

public class OrderItem {
	
	private Integer quantity;
	private Double price;
	private String productId;
	
	public OrderItem() {
		
	}
	
    public OrderItem(Integer quantity, Double price, String productId) {
		super();
		this.quantity = quantity;
		this.price = price;
		this.productId = productId;
	}

	public OrderItem(Builder builder) {
    	quantity = builder.quantity;
    	price = builder.price;
    	productId = builder.productId;
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

	public String getProductId() {
		return productId;
	}

	public void setProductId(String product) {
		this.productId = product;
	}
	
	@Override
	public String toString() {
		return "OrderItem [quantity=" + quantity + ", price=" + price + ", productId=" + productId + "]";
	}

	public static class Builder {

        private Integer quantity;
        private Double price;
        private String productId;

        public Builder quantity(Integer value) {
        	quantity = value;
            return this;
        }
        
        public Builder price(Double value) {
        	price = value;
        	return this;
        }

        public Builder productId(String value) {
        	productId = value;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }

}
