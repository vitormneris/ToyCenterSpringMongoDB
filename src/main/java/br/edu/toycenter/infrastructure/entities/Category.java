package br.edu.toycenter.infrastructure.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "category")
public class Category {
	
	@Id
	private String id;
	private String name;
	private String image;
	
	List<String> productsId = new ArrayList<>();
	
	public Category() {
	}

	public Category(String id, String name, String image) {
		this.id = id;
		this.name = name;
		this.image = image;
	}
	
    public Category(Builder builder) {
    	id = builder.id;
    	name = builder.name;
		image = builder.image;
		productsId = builder.productsId;
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

	public List<String> getProductsId() {
		return productsId;
	}

	public void setProductsId(List<String> productsId) {
		this.productsId = productsId;
	}

	public static class Builder {

        private String id;
        private String name;
		private String image;
		private List<String> productsId;

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

		public Builder productsId(List<String> value) {
			productsId = value;
			return this;
		}

        public Category build() {
            return new Category(this);
        }
    }
}
