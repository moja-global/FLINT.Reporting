/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.util.builders;

import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class FluxesToReportingVariableBuilder {

	private Long id;
	private Long startPoolId;
	private Long endPoolId;
	private Long reportingVariableId;
	private String rule;
	private Integer version;

	public FluxesToReportingVariableBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public FluxesToReportingVariableBuilder startPoolId(Long startPoolId) {
		this.startPoolId = startPoolId;
		return this;
	}

	public FluxesToReportingVariableBuilder endPoolId(Long endPoolId) {
		this.endPoolId = endPoolId;
		return this;
	}

	public FluxesToReportingVariableBuilder reportingVariableId(Long reportingVariableId) {
		this.reportingVariableId = reportingVariableId;
		return this;
	}

	public FluxesToReportingVariableBuilder rule(String rule) {
		this.rule = rule;
		return this;
	}

	public FluxesToReportingVariableBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public FluxToReportingVariable build() {

		return new FluxToReportingVariable(id, startPoolId, endPoolId, reportingVariableId, rule, version);
	}
}
