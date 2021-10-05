package com.benz.user.ops.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.benz.user.ops.config.KafkaProducer;
import com.benz.user.ops.constant.RequestType;
import com.benz.user.ops.dto.KafkaMessageDTO;
import com.benz.user.ops.dto.UserDataDTO;
import com.benz.user.ops.dto.UserDataResponse;
import com.benz.user.ops.exception.UserOpsException;
import com.benz.user.ops.proxy.DataHandlerProxy;
import com.benz.user.ops.repo.UserDataRepo;
import com.benz.user.ops.service.UserOpsService;
import com.benz.user.ops.utils.CommonUtils;
import com.benz.user.ops.utils.DataEncryptionDecryption;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Service
@Slf4j
public class UserOpsServiceImpl implements UserOpsService {

	@Autowired
	private DataEncryptionDecryption dataEncryptionDecryption;

	@Autowired
	private KafkaProducer kafkaProducer;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${private.key}")
	private String privateKey;

	@Autowired
	private UserDataRepo userDataRepo;

	@Autowired
	private DataHandlerProxy dataHandlerProxy;

	private String secret = null;

	@Override
	public String storeUserData(UserDataDTO userDataDTO) throws JsonProcessingException {
		log.info("Data received for processing  and storing {}  in the  file format {}", userDataDTO.getJsonNode(),
				userDataDTO.getFileType());

		CommonUtils.validateUserDataRequest(userDataDTO);
		validateUser(userDataDTO.getJsonNode());
		secret = RandomString.make(5);
		String userDataJSONString = objectMapper.writeValueAsString(userDataDTO);
		KafkaMessageDTO kafkaMessageDTO = new KafkaMessageDTO(
				dataEncryptionDecryption.encrypt(userDataJSONString, secret + privateKey), RequestType.INSERT.name(),
				secret);

		String message = objectMapper.writeValueAsString(kafkaMessageDTO);
		kafkaProducer.send(message);
		return CommonUtils.SUCCESS;
	}

	private boolean validateUser(JsonNode jsonNode) {
		if (userDataRepo.findByUserEmail(jsonNode.get(CommonUtils.EMAIL).textValue()) != null) {
			log.error("Given Email id is already present in the sysytem");
			throw new UserOpsException("Given Email id is already present in the sysytem");
		}
		return true;
	}

	@Override
	public JsonNode retrieveUserData(String emailId) throws JsonProcessingException {
		log.info("Retrieving the data for emailId = {}", emailId);
		if (userDataRepo.findByUserEmail(emailId) != null) {
			UserDataResponse userDataResponse = dataHandlerProxy.retrieveUserData(emailId);
			String userDataJSONString = dataEncryptionDecryption.decrypt(userDataResponse.getJsonString(),
					userDataResponse.getSecretKey() + privateKey);
			return objectMapper.readTree(userDataJSONString);
		}
		log.error("No records found for a given Email id", emailId);
		throw new UserOpsException("No records found for a given Email id");
	}

	@Override
	public String updateUserData(UserDataDTO userDataDTO) throws JsonProcessingException {
		log.info("Data received for processing  and updating {}  and file type mentioned to store is {}",
				userDataDTO.getJsonNode(), userDataDTO.getFileType());
		CommonUtils.validateUserDataRequest(userDataDTO);
		String emailId = userDataDTO.getJsonNode().get(CommonUtils.EMAIL).textValue();
		if (userDataRepo.findByUserEmail(emailId) != null) {
			secret = RandomString.make(5);
			String userDataJSONString = objectMapper.writeValueAsString(userDataDTO);
			KafkaMessageDTO kafkaMessageDTO = new KafkaMessageDTO(
					dataEncryptionDecryption.encrypt(userDataJSONString, secret + privateKey),
					RequestType.UPDATE.name(), secret);

			String message = objectMapper.writeValueAsString(kafkaMessageDTO);
			kafkaProducer.send(message);
			return CommonUtils.SUCCESS;
		}
		log.error("No records found for a given Email id to update", emailId);
		throw new UserOpsException("No records found for a given Email id to update");
	}

}
