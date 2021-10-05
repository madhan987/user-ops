package com.benz.user.ops.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.benz.user.ops.config.KafkaProducer;
import com.benz.user.ops.dto.UserDataDTO;
import com.benz.user.ops.dto.UserDataResponse;
import com.benz.user.ops.entity.UserEntity;
import com.benz.user.ops.exception.UserOpsException;
import com.benz.user.ops.proxy.DataHandlerProxy;
import com.benz.user.ops.repo.UserDataRepo;
import com.benz.user.ops.service.UserOpsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class UserOpsServiceImplTest {

	private static final String emailId = "sam@gmail.com";

	@Autowired
	private UserOpsService userOpsService;

//	@MockBean
//	private DataEncryptionDecryption dataEncryptionDecryption;

	@MockBean
	private KafkaProducer kafkaProducer;

	@MockBean
	private UserDataRepo userDataRepo;

	@MockBean
	private DataHandlerProxy dataHandlerProxy;

	@Autowired
	private ObjectMapper objectMapper;

	private JsonNode jsonNode;

	@Test
	void storeUserDataSuccessTest() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Sam\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790123341\",\r\n"
				+ "  \"email\":\"sam@gmail.com\",\r\n" + "  \"ext\":123\r\n" + "}");

		when(userDataRepo.findByUserEmail(emailId)).thenReturn(null);

		assertEquals("Success", userOpsService.storeUserData(new UserDataDTO(jsonNode, "XML")));
	}

	@Test
	void storeUserDataFailureTest() throws JsonMappingException, JsonProcessingException {
		// Test case where file type format is wrong
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Cat\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790969341\",\r\n" + "  \"ext\":123\r\n" + "}");

		UserOpsException userOpsException = assertThrows(UserOpsException.class, () -> {
			userOpsService.storeUserData(new UserDataDTO(jsonNode, "XMlL"));
		});
		assertEquals("Please provide a valid file type, valid file types are [XML, CSV]",
				userOpsException.getMessage());

		// Test case where email is not given
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Cat\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790969341\",\r\n" + "  \"ext\":123\r\n" + "}");

		userOpsException = assertThrows(UserOpsException.class, () -> {
			userOpsService.storeUserData(new UserDataDTO(jsonNode, "XML"));
		});
		assertEquals("Please provide a email address", userOpsException.getMessage());

		// Test case where email is not in valid format
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Sam\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790123341\",\r\n"
				+ "  \"email\":\"samgmail.com\",\r\n" + "  \"ext\":123\r\n" + "}");
		userOpsException = assertThrows(UserOpsException.class, () -> {
			userOpsService.storeUserData(new UserDataDTO(jsonNode, "XML"));
		});
		assertEquals("Please provide a valid email address, in the format abc@gmail.com",
				userOpsException.getMessage());

		// Test case where email id is already present in db
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Sam\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790123341\",\r\n"
				+ "  \"email\":\"sam@gmail.com\",\r\n" + "  \"ext\":123\r\n" + "}");
		when(userDataRepo.findByUserEmail(emailId)).thenReturn(new UserEntity());
		userOpsException = assertThrows(UserOpsException.class, () -> {
			userOpsService.storeUserData(new UserDataDTO(jsonNode, "XML"));
		});

		assertEquals("Given Email id is already present in the sysytem", userOpsException.getMessage());

	}

	@Test
	void updateUserDataSuccessTest() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Sam\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790123341\",\r\n"
				+ "  \"email\":\"sam@gmail.com\",\r\n" + "  \"ext\":123\r\n" + "}");

		when(userDataRepo.findByUserEmail(emailId)).thenReturn(new UserEntity());

		assertEquals("Success", userOpsService.updateUserData(new UserDataDTO(jsonNode, "XML")));
	}

	@Test
	void updateUserDataFailureTest() throws JsonMappingException, JsonProcessingException {
		jsonNode = objectMapper.readTree("{\r\n" + "  \"id\": 1,\r\n" + "  \"name\": \"Sam\",\r\n"
				+ "  \"place\": \"Chennai\",\r\n" + "  \"phone\": \"9790123341\",\r\n"
				+ "  \"email\":\"sam@gmail.com\",\r\n" + "  \"ext\":123\r\n" + "}");

		when(userDataRepo.findByUserEmail(emailId)).thenReturn(null);

		UserOpsException userOpsException = assertThrows(UserOpsException.class, () -> {
			userOpsService.updateUserData(new UserDataDTO(jsonNode, "XML"));
		});

		assertEquals("No records found for a given Email id to update", userOpsException.getMessage());
	}

	@Test
	void retrieveUserDataSuccessTest() throws JsonMappingException, JsonProcessingException {
		UserDataResponse userDataResponse = new UserDataResponse(
				"Ne79EengRc+psNseQpv+oNgdDITRJqrTpXWZA1XbjX+lcv/ZqPA+dNknl27AVx3/R/2HtbpxoyNnoDOCPMFBPOQs81LqVSty6hcMDvXvItIXX6vLcCRcm9BogYAwcSqhrlW4erFUyXfqnhOc8xBfGqt0WT2AVl3/fcx3IeSV12NOwILgEn1nOAlV7QJDOgKI",
				"clQjD");
		jsonNode = objectMapper.readTree(
				"{\"jsonNode\":{\"id\":1,\"name\":\"Kiran\",\"place\":\"Bangalore\",\"phone\":\"9790969341\",\"email\":\"kirand@gmail.com\",\"ext\":123},\"fileType\":\"csv\"}");
		when(userDataRepo.findByUserEmail(emailId)).thenReturn(new UserEntity());
		when(dataHandlerProxy.retrieveUserData(emailId)).thenReturn(userDataResponse);

		assertEquals(jsonNode, userOpsService.retrieveUserData(emailId));
	}

	@Test
	void retrieveUserDataFailureTest() {
		when(userDataRepo.findByUserEmail(emailId)).thenReturn(null);

		UserOpsException userOpsException = assertThrows(UserOpsException.class, () -> {
			userOpsService.retrieveUserData(emailId);
		});

		assertEquals("No records found for a given Email id", userOpsException.getMessage());

	}
}
