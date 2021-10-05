package com.benz.user.ops.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int userId;

	@Column(name = "EMAIL", unique = true)
	private String userEmail;

	@Column(name = "DOC_LOCATION")
	private String docLocation;

	@Column(name = "DOC_TYPE")
	private String docType;
}
