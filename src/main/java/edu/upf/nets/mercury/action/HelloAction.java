package edu.upf.nets.mercury.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import edu.upf.nets.mercury.manager.HelloManager;
import edu.upf.nets.mercury.pojo.User;

@Controller("helloAction")

public class HelloAction extends ActionSupport{

	private static final long serialVersionUID = -7311282190844486370L;
	private static final Logger log = Logger.getLogger(HelloAction.class.getName());

	private String username;
	private String password;
	private File photo;
	private String photoContentType;
	private String photoFileName;
	private InputStream photoStream;
	private List<User> userList;
	
	@Autowired
	private HelloManager helloManager;

	public HelloManager getHelloManager() {
		return helloManager;
	}

	public void setHelloManager(HelloManager helloManager) {
		this.helloManager = helloManager;
	}

	
	
	public String helloWorld() throws IOException{
		
		log.info("I am in the helloworld: "+username);
		if(username.equalsIgnoreCase("manu")){
			

			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			helloManager.helloWorld(user);
			
			if(photoFileName != null){

				photoStream = new FileInputStream(photo);
				Scanner scanner = new Scanner(photoStream);
				while(scanner.hasNextLine()){
		            String line = scanner.nextLine();
		            log.info(line);
				}
			}
			
			userList = helloManager.helloWorldList();
			
			return SUCCESS;
		} else{
			return ERROR;
		}
	}

	public String getHelloUsers(){
		userList = helloManager.helloWorldList();
		
		
		return SUCCESS;
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public File getPhoto() {
		return photo;
	}

	public void setPhoto(File photo) {
		this.photo = photo;
	}

	public String getPhotoContentType() {
		return photoContentType;
	}

	public void setPhotoContentType(String photoContentType) {
		this.photoContentType = photoContentType;
	}

	public String getPhotoFileName() {
		return photoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}

	public InputStream getPhotoStream() {
		return photoStream;
	}

	public void setPhotoStream(InputStream photoStream) {
		this.photoStream = photoStream;
	}
	
	public List<User> getUserList() {
		if(userList == null){
			userList = new ArrayList<User>();
		}
		return userList;
	}
	
}
