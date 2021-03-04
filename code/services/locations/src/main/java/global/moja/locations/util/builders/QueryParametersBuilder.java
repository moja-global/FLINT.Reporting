/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.util.builders;

import global.moja.locations.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private Long partyId;
    private Long tileId;
    private Long vegetationHistoryId;
    



    public QueryParametersBuilder ids(ServerRequest request) {

        this.ids =
                request.queryParams().get("ids") == null ? null :
                        request.queryParams()
                                .get("ids")
                                .stream()
                                .map(Long::parseLong)
                                .sorted()
                                .toArray(Long[]::new);
        return this;
    }

    public QueryParametersBuilder partyId(ServerRequest request) {
        this.partyId =
                request.queryParam("partyId").isPresent() ?
                        Long.parseLong(request.queryParam("partyId").get()) : null;
        return this;
    }

    public QueryParametersBuilder tileId(ServerRequest request) {
        this.tileId =
                request.queryParam("tileId").isPresent() ?
                        Long.parseLong(request.queryParam("tileId").get()) : null;
        return this;
    }

    public QueryParametersBuilder vegetationHistoryId(ServerRequest request) {
        this.vegetationHistoryId =
                request.queryParam("vegetationHistoryId").isPresent() ?
                        Long.parseLong(request.queryParam("vegetationHistoryId").get()) : null;
        return this;
    }



    public QueryParameters build() {
        return new QueryParameters(ids, partyId, tileId, vegetationHistoryId);
    }

}
