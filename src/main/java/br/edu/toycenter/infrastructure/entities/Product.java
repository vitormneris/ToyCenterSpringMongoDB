package br.edu.toycenter.infrastructure.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product")
public class Product {
	
	@Id
	private String id;
	private String name;
    private String image;
	private String brand;
	private Double price;
	private String description;
	private String details;
	
	private List<String> categoriesId = new ArrayList<>();
 	
	public Product() {
	}

	public Product(String id, String name, String image, String brand, Double price, String description, String details) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.brand = brand;
		this.price = price;
		this.description = description;
		this.details = details;
	}
	
    public Product(Builder builder) {
    	id = builder.id;
    	name = builder.name;
    	image = builder.image;
    	brand = builder.brand;
    	price = builder.price;
    	description = builder.description;
    	details = builder.details;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public List<String> getCategoriesId() {
		return categoriesId;
	}
 
	public void setCategoriesId(List<String> categoriesId) {
		this.categoriesId = categoriesId;
	}
	
	public static class Builder {

        private String id;
        private String name;
        private String image;
        private String brand;
        private Double price;
        private String description;
        private String details;

        public Builder id(String value) {
        	id = value;
            return this;
        }

        public Builder name(String value) {
        	name = value;
            return this;
        }
        
        public Builder image(String value) {
        	image = value;
            return this;
        }

		public Builder brand(String value) {
        	brand = value;
            return this;
        }

        public Builder price(Double value) {
        	price = value;
            return this;
        }

        public Builder description(String value) {
        	description = value;
            return this;
        }

        public Builder details(String value) {
        	details = value;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
