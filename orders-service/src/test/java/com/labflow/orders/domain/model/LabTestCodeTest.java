package com.labflow.orders.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LabTestCodeTest {

	@Test
	void codeIsTrimmedAndUppercased() {
		assertThat(new LabTestCode(" hba1c ").value()).isEqualTo("HBA1C");
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "   ", "G", "GLU-1", "TOOLONGCODE1" })
	void invalidCodesAreRejected(String code) {
		assertThatThrownBy(() -> new LabTestCode(code))
				.isInstanceOf(IllegalArgumentException.class);
	}

}
