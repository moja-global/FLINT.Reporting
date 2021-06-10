/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.util.builders;

import global.moja.tasks.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private Long taskTypeId;
    private Long taskStatusId;
    private Long databaseId;

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

    public QueryParametersBuilder taskTypeId(ServerRequest request) {
        this.taskTypeId =
                request.queryParam("taskTypeId").isPresent() ?
                        Long.parseLong(request.queryParam("taskTypeId").get()) : null;
        return this;
    }

    public QueryParametersBuilder taskStatusId(ServerRequest request) {
        this.taskStatusId =
                request.queryParam("taskStatusId").isPresent() ?
                        Long.parseLong(request.queryParam("taskStatusId").get()) : null;
        return this;
    }

    public QueryParametersBuilder databaseId(ServerRequest request) {
        this.databaseId =
                request.queryParam("databaseId").isPresent() ?
                        Long.parseLong(request.queryParam("databaseId").get()) : null;
        return this;
    }

    public QueryParameters build() {
        return new QueryParameters(ids,taskTypeId,taskStatusId,databaseId);
    }

}
