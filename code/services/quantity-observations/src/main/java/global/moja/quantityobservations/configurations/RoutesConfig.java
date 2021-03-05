/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.configurations;

import global.moja.quantityobservations.handlers.QuantityObservationsHandler;
import global.moja.quantityobservations.models.QuantityObservation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    RouterFunction<ServerResponse> routeRequests(QuantityObservationsHandler handler) {

        return
                route()
                        .POST("/api/v1/quantity_observations",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createQuantityObservation,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createQuantityObservation")
                                        .beanClass(QuantityObservationsHandler.class)
                                        .beanMethod("createQuantityObservation")
                                        .description("Inserts a single Quantity Observation Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(QuantityObservation.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Quantity Observation Record was successfully created")
                                                        .implementation(QuantityObservation.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Quantity Observation Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/quantity_observations/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createQuantityObservations,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createQuantityObservations")
                                        .beanClass(QuantityObservationsHandler.class)
                                        .beanMethod("createQuantityObservations")
                                        .description("Inserts several Quantity Observations Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(QuantityObservation.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Quantity Observations Records were successfully created")
                                                        .implementationArray(QuantityObservation.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Quantity Observations Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/quantity_observations/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveQuantityObservation,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveQuantityObservation")
                                        .beanClass(QuantityObservationsHandler.class)
                                        .beanMethod("retrieveQuantityObservation")
                                        .description("Retrieves a single Quantity Observation Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Quantity Observation Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Quantity Observation Record was successfully retrieved")
                                                        .implementation(QuantityObservation.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Quantity Observation Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/quantity_observations/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveQuantityObservations,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveQuantityObservations")
                                        .beanClass(QuantityObservationsHandler.class)
                                        .beanMethod("retrieveQuantityObservations")
                                        .description("Retrieves all or some of the Quantity Observations Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Quantity Observations Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("taskId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Task to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("partyId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Party (Party being an organization or place e.g. County) to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingVariableId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Reporting Variable to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("year").in(ParameterIn.QUERY)
                                                        .description("The Year to filter the returned values by ")
                                                        .implementation(Integer.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Quantity Observations Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/quantity_observations",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateQuantityObservation,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateQuantityObservation")
                                        .beanClass(QuantityObservationsHandler.class)
                                        .beanMethod("updateQuantityObservation")
                                        .description("Updates a single Quantity Observation Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Quantity Observation Record was successfully updated")
                                                        .implementation(QuantityObservation.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Quantity Observation Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/quantity_observations/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateQuantityObservations,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateQuantityObservation")
                                        .beanClass(QuantityObservationsHandler.class)
                                        .beanMethod("updateQuantityObservations")
                                        .description("Updates several Quantity Observations Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(QuantityObservation.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Quantity Observations Records were successfully updated")
                                                        .implementation(QuantityObservation.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Quantity Observations Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/quantity_observations/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteQuantityObservation,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteQuantityObservation")
                                        .beanClass(QuantityObservationsHandler.class)
                                        .beanMethod("deleteQuantityObservation")
                                        .description("Deletes a single Quantity Observation Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Quantity Observation Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Quantity Observation Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Quantity Observations Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Quantity Observation Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/quantity_observations/all",
                                accept(APPLICATION_JSON),
                                handler::deleteQuantityObservations,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteQuantityObservations")
                                        .beanClass(QuantityObservationsHandler.class)
                                        .beanMethod("deleteQuantityObservations")
                                        .description("Deleted all or some of the Quantity Observations Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Quantity Observations Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("taskId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Task to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("partyId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Party (Party being an organization or place e.g. County) to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingVariableId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Reporting Variable to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("year").in(ParameterIn.QUERY)
                                                        .description("The Year to filter the deleted values by ")
                                                        .implementation(Integer.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Quantity Observations Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Quantity Observations Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Quantity Observations Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}
