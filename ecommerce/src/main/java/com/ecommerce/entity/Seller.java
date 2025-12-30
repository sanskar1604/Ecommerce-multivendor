package com.ecommerce.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.domain.AccountStatus;
import com.ecommerce.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seller {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String sellerName;
	
	private String mobile;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	private String password;
	
	@Embedded
	private BusinessDetails businessDetails = new BusinessDetails();
	
	@Embedded
	private BankDetails bankDetails = new BankDetails();
	
	@OneToOne(cascade = CascadeType.ALL)
	private Address pickupAddress = new Address();
	
	private String GSTIN;
	
	private UserRole role = UserRole.ROLE_SELLER;
	
	private Boolean isEmailVerified = false;
	
	private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
	
}
