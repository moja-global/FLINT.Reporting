/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.configurations;

import global.moja.covertypes.handlers.CoverTypesHandler;
import global.moja.covertypes.models.CoverType;
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
    RouterFunction<ServerResponse> routeRequests(CoverTypesHandler handler) {

        return
                route()
                        .POST("/api/v1/cover_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createCoverType,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createCoverType")
                                        .beanClass(CoverTypesHandler.class)
                                        .beanMethod("createCoverType")
                                        .description("Inserts a single Cover Type Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(CoverType.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Cover Type Record was successfully created")
                                                        .implementation(CoverType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Cover Type Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/cover_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createCoverTypes,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createCoverTypes")
                                        .beanClass(CoverTypesHandler.class)
                                        .beanMethod("createCoverTypes")
                                        .description("Inserts several Cover Types Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(CoverType.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Cover Types Records were successfully created")
                                                        .implementationArray(CoverType.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Cover Types Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/cover_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveCoverType,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveCoverType")
                                        .beanClass(CoverTypesHandler.class)
                                        .beanMethod("retrieveCoverType")
                                        .description("Retrieves a single Cover Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Cover Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Cover Type Record was successfully retrieved")
                                                        .implementation(CoverType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Cover Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/cover_types/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveCoverTypes,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveCoverTypes")
                                        .beanClass(CoverTypesHandler.class)
                                        .beanMethod("retrieveCoverTypes")
                                        .description("Retrieves all or some of the Cover Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Cover Types Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("code").in(ParameterIn.QUERY)
                                                        .description("The Cover Type code (or code fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Cover Types Records were successfully retrieved")
                                                        .implementationArray(CoverType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Cover Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/cover_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateCoverType,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateCoverType")
                                        .beanClass(CoverTypesHandler.class)
                                        .beanMethod("updateCoverType")
                                        .description("Updates a single Cover Type Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Cover Type Record was successfully updated")
                                                        .implementation(CoverType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Cover Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/cover_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateCoverTypes,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateCoverType")
                                        .beanClass(CoverTypesHandler.class)
                                        .beanMethod("updateCoverTypes")
                                        .description("Updates several Cover Types Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(CoverType.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Cover Types Records were successfully updated")
                                                        .implementation(CoverType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Cover Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/cover_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteCoverType,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteCoverType")
                                        .beanClass(CoverTypesHandler.class)
                                        .beanMethod("deleteCoverType")
                                        .description("Deletes a single Cover Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Cover Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Cover Type Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Cover Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Cover Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/cover_types/all",
                                accept(APPLICATION_JSON),
                                handler::deleteCoverTypes,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteCoverTypes")
                                        .beanClass(CoverTypesHandler.class)
                                        .beanMethod("deleteCoverTypes")
                                        .description("Deleted all or some of the Cover Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Cover Types Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("code").in(ParameterIn.QUERY)
                                                        .description("The Cover Type code (or code fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Cover Types Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Cover Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Cover Types Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}
