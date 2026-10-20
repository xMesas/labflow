package com.labflow.orders.domain.exception;

/** An order breaks a business rule when it is created. */
public class InvalidOrderException extends RuntimeException {

	public InvalidOrderException(String message) {
		super(message);
	}

}
