/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.util.builders;

import global.moja.quantityobservations.models.QuantityObservation;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class QuantityObservationBuilder {

	private Long id;
	private Long taskId;
	private Long partyId;
	private Long databaseId;
	private Long reportingTableId;
	private Long reportingVariableId;
	private Integer year;
	private Double amount;
	private Long unitId;
	private Integer version;

	public QuantityObservationBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public QuantityObservationBuilder taskId(Long taskId) {
		this.taskId = taskId;
		return this;
	}

	public QuantityObservationBuilder partyId(Long partyId) {
		this.partyId = partyId;
		return this;
	}

	public QuantityObservationBuilder databaseId(Long databaseId) {
		this.databaseId = databaseId;
		return this;
	}

	public QuantityObservationBuilder reportingTableId(Long reportingTableId) {
		this.reportingTableId = reportingTableId;
		return this;
	}

	public QuantityObservationBuilder reportingVariableId(Long reportingVariableId) {
		this.reportingVariableId = reportingVariableId;
		return this;
	}

	public QuantityObservationBuilder year(Integer year) {
		this.year = year;
		return this;
	}

	public QuantityObservationBuilder amount(Double amount) {
		this.amount = amount;
		return this;
	}

	public QuantityObservationBuilder unitId(Long unitId) {
		this.unitId = unitId;
		return this;
	}

	public QuantityObservationBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public QuantityObservation build(){
		return new QuantityObservation(id,taskId,partyId,databaseId,reportingTableId,reportingVariableId,year,amount,unitId,version);
	}
}
