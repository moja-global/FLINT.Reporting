/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.util.builders;

import moja.global.reportingtables.models.ReportingTable;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class ReportingTableBuilder {
	
	private Long id;
	private String number;
	private String name;
	private String description;
	private Integer version;

	public ReportingTableBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public ReportingTableBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ReportingTableBuilder number(String number) {
		this.number = number;
		return this;
	}

	public ReportingTableBuilder description(String description) {
		this.description = description;
		return this;
	}

	public ReportingTableBuilder version(Integer version) {
		this.version = version;
		return this;
	}
	
	
	public ReportingTable build() {

		return new ReportingTable(id, number, name, description, version);
	}

}
