/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.util.builders;

import global.moja.reportingtable.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private Long reportingFrameworkId;
    private String number;
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

    public QueryParametersBuilder reportingFrameworkId(ServerRequest request) {
        this.reportingFrameworkId =
                request.queryParam("reportingFrameworkId").isPresent() ?
                        Long.parseLong(request.queryParam("reportingFrameworkId").get()) : null;
        return this;
    }

    public QueryParametersBuilder number(ServerRequest request) {
        this.number =
                request.queryParam("number").isPresent() ?
                        request.queryParam("number").get() : null;
        return this;
    }

    public QueryParametersBuilder name(ServerRequest request) {
        this.name =
                request.queryParam("name").isPresent() ?
                        request.queryParam("name").get() : null;
        return this;
    }    


    public QueryParameters build() {
        return new QueryParameters(ids, reportingFrameworkId, number, name);
    }

}
