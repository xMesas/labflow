package com.labflow.orders.domain.model;

/** Order lifecycle: CREATED → IN_PROGRESS → COMPLETED, or CANCELLED before completion. */
public enum OrderStatus {

	CREATED,
	IN_PROGRESS,
	COMPLETED,
	CANCELLED;

	public boolean canTransitionTo(OrderStatus next) {
		return switch (this) {
			case CREATED -> next == IN_PROGRESS || next == CANCELLED;
			case IN_PROGRESS -> next == COMPLETED || next == CANCELLED;
			case COMPLETED, CANCELLED -> false;
		};
	}

}
