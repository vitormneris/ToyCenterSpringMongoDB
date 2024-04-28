package br.edu.toycenter.infrastructure.entities;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "category")
public class Category {
	
	@Id
	private String id;
	private String name;
	
	public Category() {
	}

	public Category(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
    public Category(Builder builder) {
    	id = builder.id;
    	name = builder.name;
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

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}
	
	public static class Builder {

        private String id;
        private String name;

        public Builder id(String value) {
        	id = value;
            return this;
        }

        public Builder name(String value) {
        	name = value;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}
