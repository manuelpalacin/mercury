package edu.upf.nets.mercury.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import edu.upf.nets.mercury.pojo.User;
import edu.upf.nets.mercury.pojo.UserMongo;

@Repository(value="helloDao")
public class HelloDaoImpl extends HibernateDaoSupport implements HelloDao {

	@Autowired
	MongoTemplate mongoTemplate;

	public void helloWorld(User user){
		getHibernateTemplate().saveOrUpdate(user);
	}
	
	@SuppressWarnings("unchecked")
	public List<User> helloWorldList(){
		return getHibernateTemplate().find(
		        "from User");
	}

	@Override
	public void helloWorldMongo(UserMongo user) {
		mongoTemplate.save(user);
		
	}

	@Override
	public List<UserMongo> helloWorldMongoList() {
		return mongoTemplate.findAll(UserMongo.class);
	}
}
