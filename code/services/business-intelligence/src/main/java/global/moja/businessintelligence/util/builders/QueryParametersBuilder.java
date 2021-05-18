/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.businessintelligence.util.builders;

import ke.co.miles.businessintelligence.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private Long businessIntelligenceTypeId;
    private Long businessIntelligenceDataSourceId;
    private Long unitId;
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

    public QueryParametersBuilder businessIntelligenceTypeId(ServerRequest request) {
        this.businessIntelligenceTypeId =
                request.queryParam("businessIntelligenceTypeId").isPresent() ?
                        Long.parseLong(request.queryParam("businessIntelligenceTypeId").get()) : null;
        return this;
    }

    public QueryParametersBuilder businessIntelligenceDataSourceId(ServerRequest request) {
        this.businessIntelligenceDataSourceId =
                request.queryParam("businessIntelligenceDataSourceId").isPresent() ?
                        Long.parseLong(request.queryParam("businessIntelligenceDataSourceId").get()) : null;
        return this;
    }

    public QueryParametersBuilder unitId(ServerRequest request) {
        this.unitId =
                request.queryParam("unitId").isPresent() ?
                        Long.parseLong(request.queryParam("unitId").get()) : null;
        return this;
    }

    public QueryParametersBuilder name(ServerRequest request) {
        this.name =
                request.queryParam("name").isPresent() ?
                        request.queryParam("name").get() : null;
        return this;
    }    


    public QueryParameters build() {
        return new QueryParameters(ids, businessIntelligenceTypeId, businessIntelligenceDataSourceId, unitId, name);
    }

}
