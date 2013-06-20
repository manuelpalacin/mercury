package edu.upf.nets.mercury.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "users")
public class UserMongo implements Serializable {
 
	private static final long serialVersionUID = -4973700524714260100L;
    private String id;
    private String password;
    private String username;
 

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="password")
	public String getPassword() {
        return this.password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    @Column(name="username")
    public String getUsername() {
        return this.username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
}
