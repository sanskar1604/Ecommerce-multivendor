package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Coupan;

public interface CoupanRepository extends JpaRepository<Coupan, Long> {

	Coupan findByCode(String code);
}
