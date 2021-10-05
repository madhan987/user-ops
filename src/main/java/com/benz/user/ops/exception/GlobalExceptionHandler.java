package com.benz.user.ops.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.benz.user.ops.model.ErrorResponse;
import com.benz.user.ops.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice(basePackages = "com.benz.user.ops")
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(UserOpsException.class)
	public ResponseEntity<ErrorResponse> handleError(UserOpsException userOpsException) {
		log.error("Exception occured due to =" + userOpsException.getMessage(), userOpsException);
		ErrorResponse errorResponse = new ErrorResponse(CommonUtils.FAILURE, userOpsException.getMessage(),
				HttpStatus.BAD_REQUEST, CommonUtils.getCurrentDate());
		log.info("time " + CommonUtils.getCurrentDate());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleError(Exception exception) {
		log.error("Unhandled Exception = " + exception.getMessage(), exception);
		ErrorResponse errorResponse = new ErrorResponse(CommonUtils.FAILURE,
				"Exception occured due to = " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR,
				CommonUtils.getCurrentDate());

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleError(HttpMessageNotReadableException exception) {
		log.error("Exception occured due to = " + exception.getMessage(), exception);
		ErrorResponse errorResponse = new ErrorResponse(CommonUtils.FAILURE, "Please provide valid json",
				HttpStatus.BAD_REQUEST, CommonUtils.getCurrentDate());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
