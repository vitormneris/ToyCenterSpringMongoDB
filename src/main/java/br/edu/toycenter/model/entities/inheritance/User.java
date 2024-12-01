package br.edu.toycenter.model.entities.inheritance;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public abstract class User {
	
	@Id
	protected String id;
	protected String name;
	protected String email;
	protected String password;

	public User() {
	}

	public User(String id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

    public void setId(String id) {
		this.id = id;
	}

    public void setName(String name) {
		this.name = name;
	}

    public void setEmail(String email) {
		this.email = email;
	}

    public void setPassword(String password) {
		this.password = password;
	}
}
