/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.configurations;

import global.moja.parties.models.Party;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.parties.handlers.PartiesHandler;
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
    RouterFunction<ServerResponse> routeRequests(PartiesHandler handler) {

        return
                route()
                        .POST("/api/v1/parties",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createParty,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createParty")
                                        .beanClass(PartiesHandler.class)
                                        .beanMethod("createParty")
                                        .description("Inserts a single Party Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Party.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Party Record was successfully created")
                                                        .implementation(Party.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Party Record")
                                                        .implementation(String.class)))
                        .build()


                .and(route()
                        .POST("/api/v1/parties/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createParties,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createParties")
                                        .beanClass(PartiesHandler.class)
                                        .beanMethod("createParties")
                                        .description("Inserts several Parties Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Party.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Parties Records were successfully created")
                                                        .implementationArray(Party.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Parties Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/parties/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveParty,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveParty")
                                        .beanClass(PartiesHandler.class)
                                        .beanMethod("retrieveParty")
                                        .description("Retrieves a single Party Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Party Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Record was successfully retrieved")
                                                        .implementation(Party.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Party Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/parties/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveParties,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveParties")
                                        .beanClass(PartiesHandler.class)
                                        .beanMethod("retrieveParties")
                                        .description("Retrieves all or some of the Parties Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Parties Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("partyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Party type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Party name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Parties Records were successfully retrieved")
                                                        .implementationArray(Party.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Parties Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/parties",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateParty,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateParty")
                                        .beanClass(PartiesHandler.class)
                                        .beanMethod("updateParty")
                                        .description("Updates a single Party Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Party.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Record was successfully updated")
                                                        .implementation(Party.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Party Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/parties/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateParties,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateParty")
                                        .beanClass(PartiesHandler.class)
                                        .beanMethod("updateParties")
                                        .description("Updates several Parties Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Party.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Parties Records were successfully updated")
                                                        .implementation(Party.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Parties Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/parties/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteParty,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteParty")
                                        .beanClass(PartiesHandler.class)
                                        .beanMethod("deleteParty")
                                        .description("Deletes a single Party Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Party Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Parties Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Party Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/parties/all",
                                accept(APPLICATION_JSON),
                                handler::deleteParties,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteParties")
                                        .beanClass(PartiesHandler.class)
                                        .beanMethod("deleteParties")
                                        .description("Deleted all or some of the Parties Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Parties Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("partyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Party type to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Party name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Parties Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Parties Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Parties Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}
