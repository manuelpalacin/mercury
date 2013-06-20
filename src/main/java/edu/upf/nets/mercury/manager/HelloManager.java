package edu.upf.nets.mercury.manager;

import java.util.List;

import edu.upf.nets.mercury.pojo.User;
import edu.upf.nets.mercury.pojo.UserMongo;

public interface HelloManager {
	
	public void helloWorld(User user);
	
	public List<User> helloWorldList();

	public void helloWorldMongo(UserMongo user);
	
	public List<UserMongo> helloWorldMongoList();

}
