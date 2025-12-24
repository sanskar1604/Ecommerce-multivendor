package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {

}
