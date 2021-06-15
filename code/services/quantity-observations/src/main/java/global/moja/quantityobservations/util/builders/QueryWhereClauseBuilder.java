/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.util.builders;

import global.moja.quantityobservations.daos.QueryParameters;

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

        // Observation Type Id
        if(queryParameters.getObservationTypeId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("observation_type_id = ").append(queryParameters.getObservationTypeId());

        }

        // Task Id
        if(queryParameters.getTaskId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("task_id = ").append(queryParameters.getTaskId());

        }

        // Parties Ids
        if(queryParameters.getPartiesIds() != null && queryParameters.getPartiesIds().length != 0) {

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            if(queryParameters.getPartiesIds().length == 1) {

                query = new StringBuilder("party_id = " + queryParameters.getPartiesIds()[0]);

            } else {

                query = new StringBuilder("party_id IN (");

                int i = 0;
                while (i < queryParameters.getPartiesIds().length) {
                    query.append(queryParameters.getPartiesIds()[i]);
                    if(i < queryParameters.getPartiesIds().length - 1){
                        query.append(",");
                    }
                    i++;
                }

                query.append(")");
            }
        }

        // Database Id
        if(queryParameters.getDatabaseId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("database_id = ").append(queryParameters.getDatabaseId());

        }

        // Land Use Category Id
        if(queryParameters.getLandUseCategoryId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("land_use_category_id = ").append(queryParameters.getLandUseCategoryId());

        }

        // Table Id
        if(queryParameters.getReportingTableId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("reporting_table_id = ").append(queryParameters.getReportingTableId());

        }

        //  Reporting Variable Id
        if(queryParameters.getReportingVariableId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("reporting_variable_id = ").append(queryParameters.getReportingVariableId());

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

        //  Min Year
        if(queryParameters.getMinYear() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("year >= ").append(queryParameters.getMinYear());

        }


        //  Max Year
        if(queryParameters.getMaxYear() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("year <= ").append(queryParameters.getMaxYear());

        }

        return query == null ? "" : " WHERE " + query.toString();
    }
}
