package com.ecommerce.entity;

import java.util.HashSet;
import java.util.Set;

import com.ecommerce.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(name = "User", description = "Represents a registered user in the system")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Schema(description = "Unique user ID", examples = "1")
	private Long id;
	
	@Email
	@Schema(description = "User email address", examples = "user@example.com")
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	 @Schema(
		        description = "Encrypted password (never returned in response)",
		        accessMode = Schema.AccessMode.WRITE_ONLY
		    )
	private String password;
	
	@Schema(description = "User full name")
	private String fullName;
	
	@Schema(description = "User mobile")
	private String mobile;
	
	@Schema(description = "User role")
	private UserRole role = UserRole.ROLE_CUSTOMER;
	
	@OneToMany
	@Schema(description = "User set of address")
	private Set<Address> addresses = new HashSet<>();
	
	@ManyToMany
	@JsonIgnore
	@Schema(description = "User used coupans")
	private Set<Coupan> usedCoupans = new HashSet<>();
}
