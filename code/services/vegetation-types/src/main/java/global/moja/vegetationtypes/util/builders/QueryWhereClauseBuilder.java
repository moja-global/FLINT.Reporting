/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes.util.builders;

import global.moja.vegetationtypes.daos.QueryParameters;

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
                query = new StringBuilder("vegtypeinfo_dimension_id_pk = " + queryParameters.getIds()[0]);
            } else {
                query = new StringBuilder("vegtypeinfo_dimension_id_pk IN (");

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


        // Cover Type Id
        if(queryParameters.getCoverTypeId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("ipcccovertypeinfo_dimension_id_fk = ").append(queryParameters.getCoverTypeId());

        }


        // Is Woody Type
        if(queryParameters.getWoodyType() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("woodtype = ").append(queryParameters.getWoodyType());

        }

        //  Is Natural System
        if(queryParameters.getNaturalSystem() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("naturalsystem = ").append(queryParameters.getNaturalSystem());

        }

        //  Name
        if(queryParameters.getName()!= null && !queryParameters.getName().isBlank()){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            // Finds any values that have the name in any position
            query.append("vegtypename LIKE '%").append(queryParameters.getName()).append("%'");

        }

        return query == null ? "" : " WHERE " + query.toString();
    }
}
