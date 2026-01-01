package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {

	SellerReport findBySellerId(Long sellerId);
}
