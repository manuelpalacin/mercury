package edu.upf.nets.mercury.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.upf.nets.mercury.dao.HelloDao;
import edu.upf.nets.mercury.pojo.User;
import edu.upf.nets.mercury.pojo.UserMongo;

@Service(value="helloManager")
public class HelloManagerImpl implements HelloManager {

	@Autowired
	private HelloDao helloDao;

	public HelloDao getHelloDao() {
		return helloDao;
	}
	public void setHelloDao(HelloDao helloDao) {
		this.helloDao = helloDao;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void helloWorld(User user){
		helloDao.helloWorld(user);
	}
	
	public List<User> helloWorldList(){
		return helloDao.helloWorldList();
	}
	
	@Override
	public void helloWorldMongo(UserMongo user) {
		helloDao.helloWorldMongo(user);
		
	}
	@Override
	public List<UserMongo> helloWorldMongoList() {
		return helloDao.helloWorldMongoList();
	}
}
