package com.labflow.orders.domain.exception;

/** An order cannot move from its current status to the requested one. */
public class InvalidOrderStateException extends RuntimeException {

	public InvalidOrderStateException(String message) {
		super(message);
	}

}
