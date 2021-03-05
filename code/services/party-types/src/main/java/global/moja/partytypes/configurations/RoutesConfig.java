/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.partytypes.handlers.PartyTypesHandler;
import global.moja.partytypes.models.PartyType;
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
    RouterFunction<ServerResponse> routeRequests(PartyTypesHandler handler) {

        return
                route()
                        .POST("/api/v1/party_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createPartyType,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createPartyType")
                                        .beanClass(PartyTypesHandler.class)
                                        .beanMethod("createPartyType")
                                        .description("Inserts a single Party Type Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(PartyType.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Party Type Record was successfully created")
                                                        .implementation(PartyType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Party Type Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/party_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createPartyTypes,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createPartyTypes")
                                        .beanClass(PartyTypesHandler.class)
                                        .beanMethod("createPartyTypes")
                                        .description("Inserts several Party Types Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(PartyType.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Party Types Records were successfully created")
                                                        .implementationArray(PartyType.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Party Types Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/party_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrievePartyType,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrievePartyType")
                                        .beanClass(PartyTypesHandler.class)
                                        .beanMethod("retrievePartyType")
                                        .description("Retrieves a single Party Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Party Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Type Record was successfully retrieved")
                                                        .implementation(PartyType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Party Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/party_types/all",
                                accept(APPLICATION_JSON),
                                handler::retrievePartyTypes,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrievePartyTypes")
                                        .beanClass(PartyTypesHandler.class)
                                        .beanMethod("retrievePartyTypes")
                                        .description("Retrieves all or some of the Party Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Party Types Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("parentPartyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Parent Party Type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Party Type name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Types Records were successfully retrieved")
                                                        .implementationArray(PartyType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Party Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/party_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updatePartyType,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updatePartyType")
                                        .beanClass(PartyTypesHandler.class)
                                        .beanMethod("updatePartyType")
                                        .description("Updates a single Party Type Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Type Record was successfully updated")
                                                        .implementation(PartyType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Party Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/party_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updatePartyTypes,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updatePartyType")
                                        .beanClass(PartyTypesHandler.class)
                                        .beanMethod("updatePartyTypes")
                                        .description("Updates several Party Types Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(PartyType.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Types Records were successfully updated")
                                                        .implementation(PartyType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Party Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/party_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deletePartyType,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deletePartyType")
                                        .beanClass(PartyTypesHandler.class)
                                        .beanMethod("deletePartyType")
                                        .description("Deletes a single Party Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Party Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Type Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Party Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Party Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/party_types/all",
                                accept(APPLICATION_JSON),
                                handler::deletePartyTypes,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deletePartyTypes")
                                        .beanClass(PartyTypesHandler.class)
                                        .beanMethod("deletePartyTypes")
                                        .description("Deleted all or some of the Party Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Party Types Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("parentPartyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Parent Party Type  to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("partyTypeDataSourceId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Party Type data source to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("partyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Party Type data source to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Party Type name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Party Types Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Party Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Party Types Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}
