/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.util.builders;

import global.moja.reportingvariables.models.ReportingVariable;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class ReportingVariableBuilder {

	private Long id;
	private Long reportingFrameworkId;
	private String name;
	private String description;
	private Integer version;

	public ReportingVariableBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public ReportingVariableBuilder reportingFrameworkId(Long reportingFrameworkId) {
		this.reportingFrameworkId = reportingFrameworkId;
		return this;
	}

	public ReportingVariableBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ReportingVariableBuilder description(String description) {
		this.description = description;
		return this;
	}

	public ReportingVariableBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public ReportingVariable build(){
		return new ReportingVariable(id,reportingFrameworkId,name,description,version);
	}


}
