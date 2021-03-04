/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.util.builders;

import global.moja.locations.daos.QueryParameters;

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

        // Ids
        if(queryParameters.getIds() != null && queryParameters.getIds().length != 0) {
            if(queryParameters.getIds().length == 1) {
                query = new StringBuilder("location_dimension_id_pk = " + queryParameters.getIds()[0]);
            } else {
                query = new StringBuilder("location_dimension_id_pk IN (");

                int i = 0;
                while (i < queryParameters.getIds().length) {
                    query.append(queryParameters.getIds()[i]);
                    if(i < queryParameters.getIds().length - 1){
                        query.append(",");
                    }
                    i++;
                }

                query.append(")");
            }
        }


        // Party Id
        if(queryParameters.getPartyId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("countyinfo_dimension_id_fk = ").append(queryParameters.getPartyId());

        }


        // Tile Id
        if(queryParameters.getTileId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("tileinfo_dimension_id_fk = ").append(queryParameters.getTileId());

        }

        //  Vegetation History Id
        if(queryParameters.getVegetationHistoryId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("veghistory_dimension_id_fk = ").append(queryParameters.getVegetationHistoryId());

        }

        return query == null ? "" : " WHERE " + query.toString();
    }
}
