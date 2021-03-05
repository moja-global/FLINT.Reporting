/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.util.builders;


import global.moja.dates.models.Database;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class DatabaseBuilder {

	private Long id;
	private String label;
	private String description;
	private String url;
	private Integer startYear;
	private Integer endYear;
	private Boolean processed;
	private Boolean published;
	private Boolean archived;
	private Integer version;

	public DatabaseBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public DatabaseBuilder label(String label) {
		this.label = label;
		return this;
	}

	public DatabaseBuilder description(String description) {
		this.description = description;
		return this;
	}

	public DatabaseBuilder url(String url) {
		this.url = url;
		return this;
	}

	public DatabaseBuilder startYear(Integer startYear) {
		this.startYear = startYear;
		return this;
	}

	public DatabaseBuilder endYear(Integer endYear) {
		this.endYear = endYear;
		return this;
	}

	public DatabaseBuilder processed(Boolean processed) {
		this.processed = processed;
		return this;
	}

	public DatabaseBuilder published(Boolean published) {
		this.published = published;
		return this;
	}

	public DatabaseBuilder archived(Boolean archived) {
		this.archived = archived;
		return this;
	}

	public Database build(){

		return new Database(id,label,description,url,startYear,endYear,processed,published,archived,version);
	}


}
