package ibf2022.batch2.ssf.frontcontroller.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationRepository {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	// TODO Task 5
	// Use this class to implement CRUD operations on Redis
	public void disableUser(String username) {
		redisTemplate.opsForValue().set(username, "locked", 30);
	}

	public boolean isLocked(String username) {
		if(redisTemplate.opsForValue().get(username) != null) {
			return redisTemplate.opsForValue().get(username).equals("locked");
		}
		return false;
	}
}
