/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes.util.builders;

import global.moja.vegetationhistoryvegetationtypes.daos.QueryParameters;

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
                query = new StringBuilder("veghistory_vegtypeinfo_mapping_id_pk = " + queryParameters.getIds()[0]);
            } else {
                query = new StringBuilder("veghistory_vegtypeinfo_mapping_id_pk IN (");

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


        // Vegetation History Id
        if(queryParameters.getVegetationHistoryId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("veghistory_dimension_id_fk = ").append(queryParameters.getVegetationHistoryId());

        }


        // Vegetation Info Id
        if(queryParameters.getVegetationTypeId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("vegtypeinfo_dimension_id_fk = ").append(queryParameters.getVegetationTypeId());

        }

        //  Year
        if(queryParameters.getYear() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("year = ").append(queryParameters.getYear());

        }

        //  Party Id
        if(queryParameters.getPartyId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("veghistory_dimension_id_fk IN (SELECT veghistory_dimension_id_fk FROM location_dimension WHERE countyinfo_dimension_id_fk = ")
                    .append(queryParameters.getPartyId())
                    .append(")");

        }

        return query == null ? "" : " WHERE " + query.toString();
    }
}
