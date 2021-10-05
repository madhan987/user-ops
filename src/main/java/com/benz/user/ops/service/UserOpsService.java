package com.benz.user.ops.service;

import com.benz.user.ops.dto.UserDataDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface UserOpsService {

	String storeUserData(UserDataDTO userDataDTO) throws JsonProcessingException;

	JsonNode retrieveUserData(String emailId) throws JsonProcessingException;

	String updateUserData(UserDataDTO userDataDTO) throws JsonProcessingException;
}
