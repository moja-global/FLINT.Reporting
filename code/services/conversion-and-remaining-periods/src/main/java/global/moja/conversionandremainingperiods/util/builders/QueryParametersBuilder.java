/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.util.builders;

import global.moja.conversionandremainingperiods.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private Long previousLandCoverId;
    private Long currentLandCoverId;


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

    public QueryParametersBuilder previousLandCoverId(ServerRequest request) {
        this.previousLandCoverId =
                request.queryParam("previousLandCoverId").isPresent() ?
                        Long.parseLong(request.queryParam("previousLandCoverId").get()) : null;
        return this;
    }

    public QueryParametersBuilder currentLandCoverId(ServerRequest request) {
        this.currentLandCoverId =
                request.queryParam("currentLandCoverId").isPresent() ?
                        Long.parseLong(request.queryParam("currentLandCoverId").get()) : null;
        return this;
    }


    public QueryParameters build() {
        return new QueryParameters(ids, previousLandCoverId, currentLandCoverId);
    }

}
