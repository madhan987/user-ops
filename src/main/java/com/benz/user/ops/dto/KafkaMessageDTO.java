package com.benz.user.ops.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaMessageDTO {

	private String jsonString;

	private String requestType;

	private String secretKey;
}