package com.labflow.orders.domain.model;

import com.labflow.orders.domain.exception.InvalidOrderException;
import com.labflow.orders.domain.exception.InvalidOrderStateException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Business rules of Order, one test per rule. No Spring, so they run in milliseconds. */
class OrderTest {

	private static final PatientId PATIENT = new PatientId("P-123");
	private static final List<LabTestCode> TESTS = List.of(new LabTestCode("GLU"), new LabTestCode("HBA1C"));

	@Test
	void newOrderStartsInCreatedStatus() {
		Order order = Order.create(PATIENT, TESTS);

		assertThat(order.id()).isNotNull();
		assertThat(order.patientId()).isEqualTo(PATIENT);
		assertThat(order.tests()).containsExactlyElementsOf(TESTS);
		assertThat(order.status()).isEqualTo(OrderStatus.CREATED);
	}

	@Test
	void orderWithoutTestsIsRejected() {
		assertThatThrownBy(() -> Order.create(PATIENT, List.of()))
				.isInstanceOf(InvalidOrderException.class);
	}

	@Test
	void sameTestTwiceIsRejected() {
		List<LabTestCode> tests = List.of(new LabTestCode("GLU"), new LabTestCode("glu"));

		assertThatThrownBy(() -> Order.create(PATIENT, tests))
				.isInstanceOf(InvalidOrderException.class);
	}

	@Test
	void testsCannotBeChangedFromOutside() {
		List<LabTestCode> tests = new ArrayList<>(TESTS);
		Order order = Order.create(PATIENT, tests);

		tests.add(new LabTestCode("CHOL"));

		assertThat(order.tests()).hasSize(2);
		assertThatThrownBy(() -> order.tests().add(new LabTestCode("CHOL")))
				.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void startedOrderCanBeCompleted() {
		Order order = Order.create(PATIENT, TESTS);

		order.start();
		order.complete();

		assertThat(order.status()).isEqualTo(OrderStatus.COMPLETED);
	}

	@Test
	void orderCanBeCancelledBeforeCompletion() {
		Order created = Order.create(PATIENT, TESTS);
		Order inProgress = Order.create(PATIENT, TESTS);
		inProgress.start();

		created.cancel();
		inProgress.cancel();

		assertThat(created.status()).isEqualTo(OrderStatus.CANCELLED);
		assertThat(inProgress.status()).isEqualTo(OrderStatus.CANCELLED);
	}

	@Test
	void orderThatWasNotStartedCannotBeCompleted() {
		Order order = Order.create(PATIENT, TESTS);

		assertThatThrownBy(order::complete)
				.isInstanceOf(InvalidOrderStateException.class);
		assertThat(order.status()).isEqualTo(OrderStatus.CREATED);
	}

	@Test
	void completedOrderCannotBeCancelled() {
		Order order = Order.create(PATIENT, TESTS);
		order.start();
		order.complete();

		assertThatThrownBy(order::cancel)
				.isInstanceOf(InvalidOrderStateException.class);
		assertThat(order.status()).isEqualTo(OrderStatus.COMPLETED);
	}

}
