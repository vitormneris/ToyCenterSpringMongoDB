package br.edu.toycenter.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import br.edu.toycenter.entities.User;
import br.edu.toycenter.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
		
	@Autowired
	private UserRepository userRepository;
	
    @Autowired
    private MongoTemplate mongoTemplate;

	
	@Override
	public void run(String... args) throws Exception {
        mongoTemplate.getDb().drop();
		
		User u1 = new User("951.954.148-88", "Maria Brown", "maria@gmail.com", "988888888", "123456");
		User u2 = new User("152.361.141-64", "Alex Green", "alex@gmail.com", "977777777", "123456");

		userRepository.saveAll(Arrays.asList(u1, u2));
	}
}