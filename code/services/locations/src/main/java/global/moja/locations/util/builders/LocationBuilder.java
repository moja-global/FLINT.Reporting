/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.util.builders;

import global.moja.locations.models.Location;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class LocationBuilder {

	private Long id;
	private Long partyId;
	private Long tileId;
	private Long vegetationHistoryId;
	private Long unitCount;
	private Double unitAreaSum;

	public LocationBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public LocationBuilder partyId(Long partyId) {
		this.partyId = partyId;
		return this;
	}

	public LocationBuilder tileId(Long tileId) {
		this.tileId = tileId;
		return this;
	}

	public LocationBuilder vegetationHistoryId(Long vegetationHistoryId) {
		this.vegetationHistoryId = vegetationHistoryId;
		return this;
	}

	public LocationBuilder unitCount(Long unitCount) {
		this.unitCount = unitCount;
		return this;
	}

	public LocationBuilder unitAreaSum(Double unitAreaSum) {
		this.unitAreaSum = unitAreaSum;
		return this;
	}

	public Location build(){
		return new Location(id,partyId,tileId,vegetationHistoryId,unitCount,unitAreaSum);
	}
}
