package com.benz.user.ops.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserOpsException extends RuntimeException {

	private static final long serialVersionUID = -8208008761293140807L;
	/** error message for the exception */
	private final String message;

	public UserOpsException(String message) {
		this.message = message;
	}
}
