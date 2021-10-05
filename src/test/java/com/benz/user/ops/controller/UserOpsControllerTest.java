package com.benz.user.ops.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import com.benz.user.ops.service.UserOpsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class UserOpsControllerTest {

	@Autowired
	private UserOpsController userOpsController;

	@MockBean
	private UserOpsService userOpsService;

	@Autowired
	private ObjectMapper objectMapper;

	private JsonNode jsonNode;

	@Test
	void storeUserDataTest() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Sam\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790123341\",\r\n"
				+ "  \"email\":\"sam@gmail.com\",\r\n" + "  \"ext\":123\r\n" + "}");
		when(userOpsService.storeUserData(Mockito.any())).thenReturn("success");
		assertEquals(HttpStatus.OK, userOpsController.storeUserData(jsonNode, "xml").getStatusCode());
	}

	@Test
	void retrieveUserDataTest() throws JsonMappingException, JsonProcessingException {
		when(userOpsService.retrieveUserData(Mockito.anyString())).thenReturn(jsonNode);
		assertEquals(HttpStatus.OK, userOpsController.retrieveUserData("sam@gmail").getStatusCode());
	}

	@Test
	void updateUserDataTest() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Sam\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790123341\",\r\n"
				+ "  \"email\":\"sam@gmail.com\",\r\n" + "  \"ext\":123\r\n" + "}");
		when(userOpsService.updateUserData(Mockito.any())).thenReturn("success");
		assertEquals(HttpStatus.OK, userOpsController.updateUserData(jsonNode, "xml").getStatusCode());
	}

}
