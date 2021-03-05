/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.util.builders;

import global.moja.dates.models.Date;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class DateBuilder {

	private Long id;
	private Integer year;

	public DateBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public DateBuilder year(Integer year) {
		this.year = year;
		return this;
	}


	public Date build(){
		return new Date(id,year);
	}
}
