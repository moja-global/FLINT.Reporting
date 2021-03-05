/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults.util.builders;

import global.moja.fluxreportingresults.daos.QueryParameters;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class QueryWhereClauseBuilder {

    private QueryParameters queryParameters;

    public QueryWhereClauseBuilder queryParameters(QueryParameters queryParameters){
        this.queryParameters = queryParameters;
        return this;
    }

    public String build() {

        StringBuilder query = null;

        // Date Id
        if(queryParameters.getDateId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("date_dimension_id_fk = ").append(queryParameters.getDateId());

        }


        // Location Id
        if(queryParameters.getLocationId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("location_dimension_id_fk = ").append(queryParameters.getLocationId());

        }

        //  Flux Type Id
        if(queryParameters.getFluxTypeId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("fluxtypeinfo_dimension_id_fk = ").append(queryParameters.getFluxTypeId());

        }

        //  Source Pool Id
        if(queryParameters.getSourcePoolId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("source_poolinfo_dimension_id_fk = ").append(queryParameters.getSourcePoolId());

        }

        //  Sink Pool Id
        if(queryParameters.getSinkPoolId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("sink_poolinfo_dimension_id_fk = ").append(queryParameters.getSinkPoolId());

        }

        return query == null ? "" : " WHERE " + query.toString();
    }
}
