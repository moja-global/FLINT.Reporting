/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.util.builders;

import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class FluxesToUnfcccVariableBuilder {

	private Long id;
	private Long startPoolId;
	private Long endPoolId;
	private Long unfcccVariableId;
	private String rule;
	private Integer version;

	public FluxesToUnfcccVariableBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public FluxesToUnfcccVariableBuilder startPoolId(Long startPoolId) {
		this.startPoolId = startPoolId;
		return this;
	}

	public FluxesToUnfcccVariableBuilder endPoolId(Long endPoolId) {
		this.endPoolId = endPoolId;
		return this;
	}

	public FluxesToUnfcccVariableBuilder unfcccVariableId(Long unfcccVariableId) {
		this.unfcccVariableId = unfcccVariableId;
		return this;
	}

	public FluxesToUnfcccVariableBuilder rule(String rule) {
		this.rule = rule;
		return this;
	}

	public FluxesToUnfcccVariableBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public FluxToUnfcccVariable build() {

		return new FluxToUnfcccVariable(id, startPoolId, endPoolId, unfcccVariableId, rule, version);
	}
}
