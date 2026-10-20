package com.labflow.orders.domain.model;

import java.util.Objects;
import java.util.UUID;

/** Unique id of an order. */
public record OrderId(UUID value) {

	public OrderId {
		Objects.requireNonNull(value, "OrderId value must not be null");
	}

	public static OrderId newId() {
		return new OrderId(UUID.randomUUID());
	}

}
