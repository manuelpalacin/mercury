package edu.upf.nets.mercury.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;

/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name = "users", catalog="mercury")
public class User implements Serializable {
 
	private static final long serialVersionUID = -4973700524714260100L;
    private Long id;
    private String password;
    private String username;
 

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
