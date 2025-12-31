package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//	List<Product> findBySellerid(Long sellerId);
	
	@Query("""
		    SELECT p FROM Product p
		    WHERE (:query IS NULL
		           OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%'))
		           OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :query, '%'))
		    )
		""")
		List<Product> searchProduct(@Param("query") String query);
}
