package com.benz.user.ops.proxy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.benz.user.ops.dto.UserDataResponse;
import com.benz.user.ops.exception.UserOpsException;
import com.benz.user.ops.proxy.DataHandlerProxy;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataHandlerProxyImpl implements DataHandlerProxy {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${data.handler.service}")
	private String dataHandlerServiceUrl;

	@Override
	public UserDataResponse retrieveUserData(String emailId) {
		UriComponents builder = UriComponentsBuilder.fromUriString(dataHandlerServiceUrl + "/read")
				.queryParam("emailId", emailId).build();
		ResponseEntity<UserDataResponse> response;
		try {
			response = restTemplate.exchange(builder.encode().toUri(), HttpMethod.GET,
					new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<UserDataResponse>() {
					});
		} catch (Exception exception) {
			log.error("Failed to retrieve the  user data from data handler Service" + exception.getMessage(),
					exception);
			throw new UserOpsException("Unable to process please try after some time");
		}
		if (response.getStatusCode() == HttpStatus.OK) {
			log.info("Successfully retrieved the user data from data handler Service");
			UserDataResponse userData = response.getBody();
			if (userData != null) {
				return userData;
			}
		}
		throw new UserOpsException("Unable to process your request please try after some time");
	}

}
