package com.labflow.orders.domain.model;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/** Lab test code, e.g. GLU (glucose). Always stored in upper case. */
public record LabTestCode(String value) {

	private static final Pattern FORMAT = Pattern.compile("[A-Z0-9]{2,10}");

	public LabTestCode {
		Objects.requireNonNull(value, "LabTestCode value must not be null");
		value = value.strip().toUpperCase(Locale.ROOT);
		if (!FORMAT.matcher(value).matches()) {
			throw new IllegalArgumentException("Invalid lab test code: '" + value + "'");
		}
	}

}
