package com.labflow.orders.domain.model;

import com.labflow.orders.domain.exception.InvalidOrderException;
import com.labflow.orders.domain.exception.InvalidOrderStateException;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/** A request of lab tests for a patient. Its status only changes through start(), complete() and cancel(). */
public class Order {

	private final OrderId id;
	private final PatientId patientId;
	private final List<LabTestCode> tests;
	private final Instant createdAt;
	private OrderStatus status;

	private Order(OrderId id, PatientId patientId, List<LabTestCode> tests, Instant createdAt, OrderStatus status) {
		this.id = id;
		this.patientId = patientId;
		this.tests = tests;
		this.createdAt = createdAt;
		this.status = status;
	}

	public static Order create(PatientId patientId, List<LabTestCode> tests) {
		Objects.requireNonNull(patientId, "patientId must not be null");
		Objects.requireNonNull(tests, "tests must not be null");
		if (tests.isEmpty()) {
			throw new InvalidOrderException("An order needs at least one lab test");
		}
		List<LabTestCode> copy = List.copyOf(tests);
		// A set drops duplicates, so a smaller size means a test was repeated.
		if (new HashSet<>(copy).size() != copy.size()) {
			throw new InvalidOrderException("An order cannot request the same lab test twice");
		}
		return new Order(OrderId.newId(), patientId, copy, Instant.now(), OrderStatus.CREATED);
	}

	public void start() {
		transitionTo(OrderStatus.IN_PROGRESS);
	}

	public void complete() {
		transitionTo(OrderStatus.COMPLETED);
	}

	public void cancel() {
		transitionTo(OrderStatus.CANCELLED);
	}

	private void transitionTo(OrderStatus next) {
		if (!status.canTransitionTo(next)) {
			throw new InvalidOrderStateException(
					"Order %s cannot go from %s to %s".formatted(id.value(), status, next));
		}
		status = next;
	}

	public OrderId id() {
		return id;
	}

	public PatientId patientId() {
		return patientId;
	}

	public List<LabTestCode> tests() {
		return tests;
	}

	public Instant createdAt() {
		return createdAt;
	}

	public OrderStatus status() {
		return status;
	}

	// Two orders are the same if they have the same id.
	@Override
	public boolean equals(Object o) {
		return o instanceof Order other && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
