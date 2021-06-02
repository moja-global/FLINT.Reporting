/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults.util.builders;

import global.moja.fluxreportingresults.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long id;
    private Long dateId;
    private Long locationId;
    private Long fluxTypeId;
    private Long sourcePoolId;
    private Long sinkPoolId;
    private Long itemCount;

    public QueryParametersBuilder id(ServerRequest request) {
        this.id =
                request.queryParam("id").isPresent() ?
                        Long.parseLong(request.queryParam("id").get()) : null;
        return this;
    }

    public QueryParametersBuilder dateId(ServerRequest request) {
        this.dateId =
                request.queryParam("dateId").isPresent() ?
                        Long.parseLong(request.queryParam("dateId").get()) : null;
        return this;
    }

    public QueryParametersBuilder locationId(ServerRequest request) {
        this.locationId =
                request.queryParam("locationId").isPresent() ?
                        Long.parseLong(request.queryParam("locationId").get()) : null;
        return this;
    }

    public QueryParametersBuilder fluxTypeId(ServerRequest request) {
        this.fluxTypeId =
                request.queryParam("fluxTypeId").isPresent() ?
                        Long.parseLong(request.queryParam("fluxTypeId").get()) : null;
        return this;
    }

    public QueryParametersBuilder sourcePoolId(ServerRequest request) {
        this.sourcePoolId =
                request.queryParam("sourcePoolId").isPresent() ?
                        Long.parseLong(request.queryParam("sourcePoolId").get()) : null;
        return this;
    }

    public QueryParametersBuilder sinkPoolId(ServerRequest request) {
        this.sinkPoolId =
                request.queryParam("sinkPoolId").isPresent() ?
                        Long.parseLong(request.queryParam("sinkPoolId").get()) : null;
        return this;
    }

    public QueryParametersBuilder itemCount(ServerRequest request) {
        this.itemCount =
                request.queryParam("itemCount").isPresent() ?
                        Long.parseLong(request.queryParam("itemCount").get()) : null;
        return this;
    }

    public QueryParameters build() {
        return new QueryParameters(id, dateId, locationId, fluxTypeId, sourcePoolId, sinkPoolId, itemCount);
    }

}
