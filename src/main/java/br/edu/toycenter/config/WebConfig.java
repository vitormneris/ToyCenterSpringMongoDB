package br.edu.toycenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.toycenter.api.request.AdministratorRequestDTO;
import br.edu.toycenter.api.request.CategoryRequestDTO;
import br.edu.toycenter.api.request.ClientRequestDTO;
import br.edu.toycenter.api.request.OrderItemRequestDTO;
import br.edu.toycenter.api.request.OrderRequestDTO;
import br.edu.toycenter.api.request.ProductRequestDTO;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Bean 
	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
	
    @Bean
    ProductRequestDTO productRequestDTO() {
        return new ProductRequestDTO(null, null, null, null, null, null, null, null);
    }

    @Bean
    ClientRequestDTO clientRequestDTO() {
        return new ClientRequestDTO(null, null, null, null, null, null);
    }
    
    @Bean
    AdministratorRequestDTO administratorRequestDTO() {
        return new AdministratorRequestDTO(null, null, null, null);
    }

    @Bean
    CategoryRequestDTO categoryRequestDTO() {
        return new CategoryRequestDTO(null, null, null, null);
    }

    @Bean
    OrderRequestDTO orderRequestDTO() {
        return new OrderRequestDTO(null, null, null);
    }

    @Bean
    OrderItemRequestDTO orderItemRequestDTO() {
        return new OrderItemRequestDTO(null, null);
    }
}
