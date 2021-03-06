/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.util.builders;

import global.moja.quantityobservations.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private Long taskId;
    private Long partyId;
    private Long databaseId;
    private Long reportingTableId;
    private Long reportingVariableId;
    private Integer year;

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

    public QueryParametersBuilder taskId(ServerRequest request) {
        this.taskId =
                request.queryParam("taskId").isPresent() ?
                        Long.parseLong(request.queryParam("taskId").get()) : null;
        return this;
    }

    public QueryParametersBuilder partyId(ServerRequest request) {
        this.partyId =
                request.queryParam("partyId").isPresent() ?
                        Long.parseLong(request.queryParam("partyId").get()) : null;
        return this;
    }

    public QueryParametersBuilder databaseId(ServerRequest request) {
        this.databaseId =
                request.queryParam("databaseId").isPresent() ?
                        Long.parseLong(request.queryParam("databaseId").get()) : null;
        return this;
    }

    public QueryParametersBuilder reportingTableId(ServerRequest request) {
        this.reportingTableId =
                request.queryParam("reportingTableId").isPresent() ?
                        Long.parseLong(request.queryParam("reportingTableId").get()) : null;
        return this;
    }

    public QueryParametersBuilder reportingVariableId(ServerRequest request) {
        this.reportingVariableId =
                request.queryParam("reportingVariableId").isPresent() ?
                        Long.parseLong(request.queryParam("reportingVariableId").get()) : null;
        return this;
    }

    public QueryParametersBuilder year(ServerRequest request) {
        this.year =
                request.queryParam("year").isPresent() ?
                        Integer.parseInt(request.queryParam("year").get()) : null;
        return this;
    }


    public QueryParameters build() {
        return new QueryParameters(ids, taskId, partyId,databaseId,reportingTableId,reportingVariableId, year);
    }

}
