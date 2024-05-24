package br.edu.toycenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import br.edu.toycenter.api.request.ProductRequestDTO;

@Configuration
public class WebConfig {

	@Bean 
	HiddenHttpMethodFilter hiddenHttpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}
	
    @Bean
    ProductRequestDTO productRequestDTO() {
        return new ProductRequestDTO(null, null, null, null, null, null);
    }
}
