package br.edu.toycenter.model.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
public class Product {
	
	@Id
	private String id;
	private String name;
    private String image;
	private String brand;
	private Double price;
	private String description;
	private String details;
	
	private List<String> categoriesId = new ArrayList<>();
}
