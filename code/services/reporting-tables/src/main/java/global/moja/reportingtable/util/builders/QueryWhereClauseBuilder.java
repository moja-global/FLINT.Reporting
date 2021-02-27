/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.util.builders;

import global.moja.reportingtable.daos.QueryParameters;

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


        // Reporting Framework Id
        if(queryParameters.getReportingFrameworkId()!= null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("reporting_framework_id = ").append(queryParameters.getReportingFrameworkId());

        }

        // Table Number
        if(queryParameters.getNumber() != null && !queryParameters.getNumber().isBlank()){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            // Finds any values that have the name in any position
            query.append("number LIKE '%").append(queryParameters.getNumber()).append("%'");

        }

        //  Name
        if(queryParameters.getName()!= null && !queryParameters.getName().isBlank()){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            // Finds any values that have the name in any position
            query.append("name LIKE '%").append(queryParameters.getName()).append("%'");

        }

        return query == null ? "" : " WHERE " + query.toString();
    }
}
