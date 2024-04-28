package br.edu.toycenter.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import br.edu.toycenter.infrastructure.entities.Category;
import br.edu.toycenter.infrastructure.entities.Product;
import br.edu.toycenter.infrastructure.entities.User;
import br.edu.toycenter.infrastructure.repositories.CategoryRepository;
import br.edu.toycenter.infrastructure.repositories.ProductRepository;
import br.edu.toycenter.infrastructure.repositories.UserRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
		
    @Autowired
    private MongoTemplate mongoTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public void run(String... args) throws Exception {
        mongoTemplate.getDb().drop();
		
		User u1 = new User(null, "951.954.148-88", "Maria Brown", "maria@gmail.com", "988888888", "654123");
		User u2 = new User(null, "152.361.141-64", "Alex Green", "alex@gmail.com", "977777777", "123456");
		User u3 = new User(null, "142.141.391-51", "James Red", "james@gmail.com", "966666666", "654321");
		
		userRepository.saveAll(Arrays.asList(u1, u2, u3));
		
		Product p1 = new Product(null, "Carrinho de controle remoto", "Estrela", 99.90, "Carrinho de controle remoto de qualidade"
				, "Carrinho a pilha de 40 cm" );
		Product p2 = new Product(null, "Boneca da Barbie", "Funtoy", 120.99, "Boneca da Barbie do seus sonhos"
				, "Boneca de plástico de 16 cm");
		Product p3 = new Product(null, "PlayStation 3", "Sony", 1299.80, "Console de antepenúltima geração maravilhoso"
				, "Console com 256 GB de memória");
		
		productRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		Category c1 = new Category(null, "Carrinhos");
		Category c2 = new Category(null, "Bonecas");
		Category c3 = new Category(null, "Consoles");
		
		categoryRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		p1.getCategoriesId().add(c1.getId());
		p2.getCategoriesId().add(c2.getId());
		p3.getCategoriesId().add(c3.getId());
		
		c1.getProductsId().add(p1.getId());
		c2.getProductsId().add(p2.getId());
		c3.getProductsId().add(p3.getId());
		
		productRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		categoryRepository.saveAll(Arrays.asList(c1, c2, c3));

	}
}