package com.benz.user.ops.model;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {

	private static final long serialVersionUID = 2630823980373660062L;

	/** The status. */
	private String status;

	/** The error message. */
	private String errorMessage;

	/** status code. */
	private HttpStatus statusCode;

	/** The timestamp. */
	private String timestamp;

}
