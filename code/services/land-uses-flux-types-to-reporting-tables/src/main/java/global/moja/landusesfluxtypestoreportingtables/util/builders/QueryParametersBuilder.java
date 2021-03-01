/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.util.builders;

import global.moja.landusesfluxtypestoreportingtables.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private Long landUseFluxTypeId;
    private Long emissionTypeId;
    private Long reportingTableId;
    private String name;    
    



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

    public QueryParametersBuilder landUseFluxTypeId(ServerRequest request) {
        this.landUseFluxTypeId =
                request.queryParam("landUseFluxTypeId").isPresent() ?
                        Long.parseLong(request.queryParam("landUseFluxTypeId").get()) : null;
        return this;
    }

    public QueryParametersBuilder emissionTypeId(ServerRequest request) {
        this.emissionTypeId =
                request.queryParam("emissionTypeId").isPresent() ?
                        Long.parseLong(request.queryParam("emissionTypeId").get()) : null;
        return this;
    }

    public QueryParametersBuilder reportingTableId(ServerRequest request) {
        this.reportingTableId =
                request.queryParam("reportingTableId").isPresent() ?
                        Long.parseLong(request.queryParam("reportingTableId").get()) : null;
        return this;
    }

    public QueryParameters build() {
        return new QueryParameters(ids, landUseFluxTypeId, emissionTypeId, reportingTableId);
    }

}
