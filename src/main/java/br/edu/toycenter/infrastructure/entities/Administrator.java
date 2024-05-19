package br.edu.toycenter.infrastructure.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import br.edu.toycenter.infrastructure.entities.inheritance.User;

@Document(collection = "administrator")
public class Administrator extends User {

	public Administrator() {
	}

	public Administrator(String id, String name, String email, String password) {
		super(id, name, email, password);
	}
	
    public Administrator(Builder builder) {
    	id = builder.id;
    	name = builder.name;
    	email = builder.email;
    	password = builder.password;
    }
	
	public static class Builder {

        private String id;
        private String name;
        private String email;
        private String password;

        public Builder id(String value) {
        	id = value;
            return this;
        }

        public Builder name(String value) {
        	name = value;
            return this;
        }

        public Builder email(String value) {
        	email = value;
            return this;
        }

        public Builder password(String value) {
        	password = value;
            return this;
        }

        public Administrator build() {
            return new Administrator(this);
        }
    }
}
