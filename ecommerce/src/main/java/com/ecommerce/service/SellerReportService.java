package com.ecommerce.service;

import com.ecommerce.entity.Seller;
import com.ecommerce.entity.SellerReport;

public interface SellerReportService {

	SellerReport getSellerReport(Seller seller);
	
	SellerReport updateSellerReport(SellerReport sellerReport);
}
