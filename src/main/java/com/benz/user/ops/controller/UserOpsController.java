package com.benz.user.ops.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.benz.user.ops.dto.UserDataDTO;
import com.benz.user.ops.service.UserOpsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserOpsController {

	@Autowired
	private UserOpsService userOpsService;

	@PostMapping("/store")
	public ResponseEntity<String> storeUserData(@RequestBody JsonNode jsonNode, @RequestParam String fileType)
			throws JsonProcessingException {
		log.info("Request received to store user data in the format = {} ", fileType);
		return new ResponseEntity<>(
				userOpsService.storeUserData(UserDataDTO.builder().jsonNode(jsonNode).fileType(fileType).build()),
				HttpStatus.OK);
	}

	@GetMapping("/read")
	public ResponseEntity<JsonNode> retrieveUserData(@RequestParam String emailId) throws JsonProcessingException {
		log.info("Request received to retrieve user data for a email = {} ", emailId);
		return new ResponseEntity<>(userOpsService.retrieveUserData(emailId), HttpStatus.OK);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updateUserData(@RequestBody JsonNode jsonNode, @RequestParam String fileType)
			throws JsonProcessingException {
		log.info("Request received to update user data in the format = {} ", fileType);
		return new ResponseEntity<>(
				userOpsService.updateUserData(UserDataDTO.builder().jsonNode(jsonNode).fileType(fileType).build()),
				HttpStatus.OK);
	}
}
