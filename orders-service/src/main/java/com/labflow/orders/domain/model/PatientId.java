package com.labflow.orders.domain.model;

import java.util.Objects;

/** Id of the patient the lab tests are ordered for. */
public record PatientId(String value) {

	public PatientId {
		Objects.requireNonNull(value, "PatientId value must not be null");
		value = value.strip();
		if (value.isEmpty()) {
			throw new IllegalArgumentException("PatientId must not be blank");
		}
	}

}
