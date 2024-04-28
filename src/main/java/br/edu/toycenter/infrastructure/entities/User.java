package br.edu.toycenter.infrastructure.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {
	
	@Id
	private String id;
	private String cpf;
	private String name;
	private String email;
	private String phone;
	private String password;
	
	List<String> ordersId = new ArrayList<>();
	
	public User() {
		
	}
	
	public User(String id, String cpf, String name, String email, String phone, String password) {
		super();
		this.id = id;
		this.cpf = cpf;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.password = password;
	}
	
    public User(Builder builder) {
    	id = builder.id;
    	cpf = builder.cpf;
    	name = builder.name;
    	email = builder.email;
    	phone = builder.phone;
    	password = builder.password;
    }
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<String> getOrdersId() {
		return ordersId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(cpf, other.cpf);
	}

	@Override
	public String toString() {
		return "User [cpf=" + cpf + ", name=" + name + ", email=" + email + ", phone=" + phone + ", password="
				+ password + "]";
	}
	
	public static class Builder {

        private String id;
        private String cpf;
        private String name;
        private String email;
        private String phone;
        private String password;

        public Builder id(String value) {
        	id = value;
            return this;
        }

        public Builder cpf(String value) {
        	cpf = value;
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

        public Builder phone(String value) {
        	phone = value;
            return this;
        }

        public Builder password(String value) {
        	password = value;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
