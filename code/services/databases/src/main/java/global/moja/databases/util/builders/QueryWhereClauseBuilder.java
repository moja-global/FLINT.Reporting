/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.util.builders;

import global.moja.databases.daos.QueryParameters;

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
                query = new StringBuilder("id = " + queryParameters.getIds()[0]);
            } else {
                query = new StringBuilder("id IN (");

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

        //  Label
        if(queryParameters.getLabel() != null && !queryParameters.getLabel().isBlank()){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            // Finds any values that have the label in any position
            query.append("label LIKE '%").append(queryParameters.getLabel()).append("%'");

        }


        // Start Year
        if(queryParameters.getStartYear() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("start_year = ").append(queryParameters.getStartYear());

        }


        // End Year
        if(queryParameters.getEndYear() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("end_year = ").append(queryParameters.getEndYear());

        }

        // Processed
        if(queryParameters.getProcessed() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("processed = ").append(queryParameters.getProcessed());

        }

        // Published
        if(queryParameters.getPublished() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("published = ").append(queryParameters.getPublished());

        }

        // Archived
        if(queryParameters.getArchived() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("archived = ").append(queryParameters.getArchived());

        }

        return query == null ? "" : " WHERE " + query.toString();
    }
}
