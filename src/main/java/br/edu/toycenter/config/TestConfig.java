package br.edu.toycenter.config;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import br.edu.toycenter.model.entities.Category;
import br.edu.toycenter.model.entities.Product;
import br.edu.toycenter.model.repositories.CategoryRepository;
import br.edu.toycenter.model.repositories.ProductRepository;

@Configuration
@Profile("test")
@RequiredArgsConstructor
public class TestConfig implements CommandLineRunner {
    private final MongoTemplate mongoTemplate;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public void run(String... args) {
        mongoTemplate.getDb().drop();

		Product p1 = new Product(null, "Carrinho de controle remoto", "/images/product/pinguim1.webp", "Estrela", 99.90, "Carrinho de controle remoto de qualidade"
				, "Carrinho a pilha de 40 cm", new ArrayList<>());
		Product p2 = new Product(null, "Boneca da Barbie", "/images/product/pinguim1.webp", "Funtoy", 120.99, "Boneca da Barbie do seus sonhos"
				, "Boneca de plástico de 16 cm", new ArrayList<>());
		Product p3 = new Product(null, "PlayStation 3", "/images/product/pinguim1.webp", "Sony", 1299.80, "Console de antepenúltima geração maravilhoso"
				, "Console com 256 GB de memória", new ArrayList<>());
		
		productRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		Category c1 = new Category(null, "Carrinhos", "/images/category/pinguim1.webp", new ArrayList<>());
		Category c2 = new Category(null, "Bonecas", "/images/category/pinguim1.webp", new ArrayList<>());
		Category c3 = new Category(null, "Consoles", "/images/category/pinguim1.webp", new ArrayList<>());
		
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