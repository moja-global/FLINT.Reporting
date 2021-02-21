/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.units.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import moja.global.units.handlers.UnitsHandler;
import moja.global.units.models.Unit;
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
 * @author Kwaje Anthony <tony@miles.co.ke>
 */
@Configuration
public class RoutesConfig {


    @Bean
    RouterFunction<ServerResponse> routeRequests(UnitsHandler handler) {

        return
                route()
                        .POST("/api/v1/units",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createUnit,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createUnit")
                                        .beanClass(UnitsHandler.class)
                                        .beanMethod("createUnit")
                                        .description("Inserts a single Unit Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Unit.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Unit Record was successfully created")
                                                        .implementation(Unit.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Unit Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/units/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createUnits,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createUnits")
                                        .beanClass(UnitsHandler.class)
                                        .beanMethod("createUnits")
                                        .description("Inserts several Unit Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Unit.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Unit Records were successfully created")
                                                        .implementationArray(Unit.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Unit Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/units/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveUnit,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveUnit")
                                        .beanClass(UnitsHandler.class)
                                        .beanMethod("retrieveUnit")
                                        .description("Retrieves a single Unit Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Unit Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Record was successfully retrieved")
                                                        .implementation(Unit.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Unit Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/units/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveUnits,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveUnits")
                                        .beanClass(UnitsHandler.class)
                                        .beanMethod("retrieveUnits")
                                        .description("Retrieves all or some of the Unit Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the Unit Records to restrict the retrieval to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Records were successfully retrieved")
                                                        .implementationArray(Unit.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Unit Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/units",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateUnit,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateUnit")
                                        .beanClass(UnitsHandler.class)
                                        .beanMethod("updateUnit")
                                        .description("Updates a single Unit Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Record was successfully updated")
                                                        .implementation(Unit.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Unit Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/units/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateUnits,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateUnit")
                                        .beanClass(UnitsHandler.class)
                                        .beanMethod("updateUnits")
                                        .description("Updates several Unit Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Unit.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Records were successfully updated")
                                                        .implementation(Unit.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Unit Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/units/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteUnit,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteUnit")
                                        .beanClass(UnitsHandler.class)
                                        .beanMethod("deleteUnit")
                                        .description("Deletes a single Unit Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Unit Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Unit Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Unit Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/units/all",
                                accept(APPLICATION_JSON),
                                handler::deleteUnits,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteUnits")
                                        .beanClass(UnitsHandler.class)
                                        .beanMethod("deleteUnits")
                                        .description("Deleted all or some of the Unit Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the Unit Records to restrict the deletion to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Unit Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Unit Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}
