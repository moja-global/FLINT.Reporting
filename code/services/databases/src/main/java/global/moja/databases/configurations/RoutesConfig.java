/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.databases.handlers.DatabasesHandler;
import global.moja.databases.models.Database;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Configuration
public class RoutesConfig {


    @Bean
    RouterFunction<ServerResponse> routeRequests(DatabasesHandler handler) {

        return
                route()
                        .POST("/api/v1/databases",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createDatabase,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createDatabase")
                                        .beanClass(DatabasesHandler.class)
                                        .beanMethod("createDatabase")
                                        .description("Inserts a single Database Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Database.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Database Record was successfully created")
                                                        .implementation(Database.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Database Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/databases/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createDatabases,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createDatabases")
                                        .beanClass(DatabasesHandler.class)
                                        .beanMethod("createDatabases")
                                        .description("Inserts several Databases Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Database.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Databases Records were successfully created")
                                                        .implementationArray(Database.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Databases Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/databases/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveDatabase,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveDatabase")
                                        .beanClass(DatabasesHandler.class)
                                        .beanMethod("retrieveDatabase")
                                        .description("Retrieves a single Database Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Database Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Database Record was successfully retrieved")
                                                        .implementation(Database.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Database Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/databases/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveDatabases,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveDatabases")
                                        .beanClass(DatabasesHandler.class)
                                        .beanMethod("retrieveDatabases")
                                        .description("Retrieves all or some of the Databases Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Databases Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("label").in(ParameterIn.QUERY)
                                                        .description("The Database label to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("startYear").in(ParameterIn.QUERY)
                                                        .description("The start year to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("endYear").in(ParameterIn.QUERY)
                                                        .description("The end year to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("processed").in(ParameterIn.QUERY)
                                                        .description("The Processed Status (True or False)  to filter the returned values by ")
                                                        .implementation(Boolean.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("published").in(ParameterIn.QUERY)
                                                        .description("The Published Status (True or False)  to filter the returned values by ")
                                                        .implementation(Boolean.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("archived").in(ParameterIn.QUERY)
                                                        .description("The Archived Status (True or False)  to filter the returned values by ")
                                                        .implementation(Boolean.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Databases Records were successfully retrieved")
                                                        .implementationArray(Database.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Databases Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/databases",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateDatabase,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateDatabase")
                                        .beanClass(DatabasesHandler.class)
                                        .beanMethod("updateDatabase")
                                        .description("Updates a single Database Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Database.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Database Record was successfully updated")
                                                        .implementation(Database.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Database Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/databases/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateDatabases,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateDatabase")
                                        .beanClass(DatabasesHandler.class)
                                        .beanMethod("updateDatabases")
                                        .description("Updates several Databases Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Database.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Databases Records were successfully updated")
                                                        .implementation(Database.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Databases Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/databases/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteDatabase,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteDatabase")
                                        .beanClass(DatabasesHandler.class)
                                        .beanMethod("deleteDatabase")
                                        .description("Deletes a single Database Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Database Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Database Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Databases Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Database Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/databases/all",
                                accept(APPLICATION_JSON),
                                handler::deleteDatabases,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteDatabases")
                                        .beanClass(DatabasesHandler.class)
                                        .beanMethod("deleteDatabases")
                                        .description("Deleted all or some of the Databases Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Databases Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("label").in(ParameterIn.QUERY)
                                                        .description("The Database label to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("startYear").in(ParameterIn.QUERY)
                                                        .description("The start year to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("endYear").in(ParameterIn.QUERY)
                                                        .description("The end year to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("processed").in(ParameterIn.QUERY)
                                                        .description("The Processed Status (True or False)  to filter the deleted values by ")
                                                        .implementation(Boolean.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("published").in(ParameterIn.QUERY)
                                                        .description("The Published Status (True or False)  to filter the deleted values by ")
                                                        .implementation(Boolean.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("archived").in(ParameterIn.QUERY)
                                                        .description("The Archived Status (True or False)  to filter the deleted values by ")
                                                        .implementation(Boolean.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Databases Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Databases Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Databases Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}
