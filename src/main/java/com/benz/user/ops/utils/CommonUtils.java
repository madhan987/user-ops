package com.benz.user.ops.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.benz.user.ops.constant.FileType;
import com.benz.user.ops.dto.UserDataDTO;
import com.benz.user.ops.exception.UserOpsException;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CommonUtils {

	public static String getCurrentDate() {
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
		return dateTimeFormat.format(LocalDateTime.now());
	}

	public static final String SUCCESS = "Success";

	public static final String FAILURE = "Failure";

	private static final List<String> fileTypes = Arrays.asList("XML", "CSV");

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+.[a-zA-Z]{2,6}$");

	public static final String EMAIL = "email";

	public static boolean validateUserDataRequest(UserDataDTO userDataDTO) {

		validateFileType(userDataDTO.getFileType());
		validateUserData(userDataDTO.getJsonNode());
		return true;

	}

	private static boolean validateUserData(JsonNode jsonData) {
		JsonNode email = jsonData.get(CommonUtils.EMAIL);
		if (email == null || email.textValue() == null) {
			throw new UserOpsException("Please provide a email address");
		} else if (!EMAIL_PATTERN.matcher(email.textValue()).matches()) {
			throw new UserOpsException("Please provide a valid email address, in the format abc@gmail.com");
		}
		return true;
	}

	private static boolean validateFileType(String fileType) {
		if (Arrays.stream(FileType.values()).anyMatch(file -> file.name().equalsIgnoreCase(fileType))) {
			return true;
		}
		throw new UserOpsException("Please provide a valid file type, valid file types are " + fileTypes.toString());
	}
}
