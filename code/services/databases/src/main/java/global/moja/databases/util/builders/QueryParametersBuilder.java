/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.util.builders;

import global.moja.databases.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private String label;
    private Integer startYear;
    private Integer endYear;
    private Boolean processed;
    private Boolean published;
    private Boolean archived;

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

    public QueryParametersBuilder label(ServerRequest request) {
        this.label =
                request.queryParam("label").isPresent() ?
                        request.queryParam("label").get() : null;
        return this;
    }


    public QueryParametersBuilder startYear(ServerRequest request) {
        this.startYear =
                request.queryParam("startYear").isPresent() ?
                        Integer.parseInt(request.queryParam("startYear").get()) : null;
        return this;
    }

    public QueryParametersBuilder endYear(ServerRequest request) {
        this.endYear =
                request.queryParam("endYear").isPresent() ?
                        Integer.parseInt(request.queryParam("endYear").get()) : null;
        return this;
    }

    public QueryParametersBuilder processed(ServerRequest request) {
        this.processed =
                request.queryParam("processed").isPresent() ?
                        Boolean.parseBoolean(request.queryParam("processed").get()) : null;
        return this;
    }

    public QueryParametersBuilder published(ServerRequest request) {
        this.published =
                request.queryParam("published").isPresent() ?
                        Boolean.parseBoolean(request.queryParam("published").get()) : null;
        return this;
    }

    public QueryParametersBuilder archived(ServerRequest request) {
        this.archived =
                request.queryParam("archived").isPresent() ?
                        Boolean.parseBoolean(request.queryParam("archived").get()) : null;
        return this;
    }

    public QueryParameters build() {
        return new QueryParameters(ids, label, startYear, endYear, processed,published,archived);
    }

}
