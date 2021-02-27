/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframework.util.builders;

import global.moja.reportingframework.models.ReportingFramework;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class ReportingFrameworkBuilder {

	private Long id;
	private String name;
	private String description;
	private Integer version;

	public ReportingFrameworkBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public ReportingFrameworkBuilder name(String name) {
		this.name = name;
		return this;
	}

	public ReportingFrameworkBuilder description(String description) {
		this.description = description;
		return this;
	}

	public ReportingFrameworkBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public ReportingFramework build(){
		return new ReportingFramework(id,name,description,version);
	}
}
