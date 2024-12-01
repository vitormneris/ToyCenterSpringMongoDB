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
@Document(collection = "category")
public class Category {
	@Id
	private String id;
	private String name;
	private String image;
	private List<String> productsId = new ArrayList<>();
}
