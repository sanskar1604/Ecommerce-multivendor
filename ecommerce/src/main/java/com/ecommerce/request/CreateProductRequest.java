package com.ecommerce.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {

	private String title;
	private String description;
	private int mrpPrice;
	private int sellingPrice;
	private String color;
	private List<String> images;
	private String category;
	private String category2;
	private String cateory3;
	private String size;
}
