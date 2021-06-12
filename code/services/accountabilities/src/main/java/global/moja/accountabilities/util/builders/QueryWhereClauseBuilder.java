/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.util.builders;

import global.moja.accountabilities.daos.QueryParameters;

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


        // Accountability Type Id
        if(queryParameters.getAccountabilityTypeId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("accountability_type_id = ").append(queryParameters.getAccountabilityTypeId());

        }


        // Parent Party Id
        if(queryParameters.getParentPartyId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            if(queryParameters.getParentPartyId().equals(0L)) {
                query.append("parent_party_id IS NULL");
            } else {
                query.append("parent_party_id = ").append(queryParameters.getParentPartyId());
            }

        }

        //  Subsidiary Party Id
        if(queryParameters.getSubsidiaryPartyId() != null){

            if(query != null){
                query.append(" AND ");
            } else {
                query = new StringBuilder();
            }

            query.append("subsidiary_party_id = ").append(queryParameters.getSubsidiaryPartyId());

        }



        return query == null ? "" : " WHERE " + query.toString();
    }
}
