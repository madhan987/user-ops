package com.benz.user.ops.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataResponse {
	private String jsonString;

	private String secretKey;
}
