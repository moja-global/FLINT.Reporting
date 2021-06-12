/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.util.builders;

import global.moja.accountabilities.daos.QueryParameters;
import org.springframework.web.reactive.function.server.ServerRequest;

// TODO Exception Checks

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
public class QueryParametersBuilder {

    private Long[] ids;
    private Long accountabilityTypeId;
    private Long accountabilityRuleId;
    private Long parentPartyId;
    private Long subsidiaryPartyId;
    



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

    public QueryParametersBuilder accountabilityTypeId(ServerRequest request) {
        this.accountabilityTypeId =
                request.queryParam("accountabilityTypeId").isPresent() ?
                        Long.parseLong(request.queryParam("accountabilityTypeId").get()) : null;
        return this;
    }

    public QueryParametersBuilder accountabilityRuleId(ServerRequest request) {
        this.accountabilityRuleId =
                request.queryParam("accountabilityRuleId").isPresent() ?
                        Long.parseLong(request.queryParam("accountabilityRuleId").get()) : null;
        return this;
    }

    public QueryParametersBuilder parentPartyId(ServerRequest request) {
        this.parentPartyId =
                request.queryParam("parentPartyId").isPresent() ?
                        Long.parseLong(request.queryParam("parentPartyId").get()) : null;
        return this;
    }

    public QueryParametersBuilder subsidiaryPartyId(ServerRequest request) {
        this.subsidiaryPartyId =
                request.queryParam("subsidiaryPartyId").isPresent() ?
                        Long.parseLong(request.queryParam("subsidiaryPartyId").get()) : null;
        return this;
    }



    public QueryParameters build() {
        return new QueryParameters(ids, accountabilityTypeId, accountabilityRuleId, parentPartyId, subsidiaryPartyId);
    }

}
