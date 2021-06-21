/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.configurations;

import global.moja.accountabilitytypes.handlers.AccountabilityTypesHandler;
import global.moja.accountabilitytypes.models.AccountabilityType;
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
    RouterFunction<ServerResponse> routeRequests(AccountabilityTypesHandler handler) {

        return
                route()
                        .POST("/api/v1/accountability_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createAccountabilityType,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createAccountabilityType")
                                        .beanClass(AccountabilityTypesHandler.class)
                                        .beanMethod("createAccountabilityType")
                                        .description("Inserts a single Accountability Type Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(AccountabilityType.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Accountability Type Record was successfully created")
                                                        .implementation(AccountabilityType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Accountability Type Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/accountability_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createAccountabilityTypes,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createAccountabilityTypes")
                                        .beanClass(AccountabilityTypesHandler.class)
                                        .beanMethod("createAccountabilityTypes")
                                        .description("Inserts several Accountability Types Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(AccountabilityType.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Accountability Types Records were successfully created")
                                                        .implementationArray(AccountabilityType.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Accountability Types Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/accountability_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveAccountabilityType,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveAccountabilityType")
                                        .beanClass(AccountabilityTypesHandler.class)
                                        .beanMethod("retrieveAccountabilityType")
                                        .description("Retrieves a single Accountability Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Accountability Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Type Record was successfully retrieved")
                                                        .implementation(AccountabilityType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Accountability Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/accountability_types/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveAccountabilityTypes,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveAccountabilityTypes")
                                        .beanClass(AccountabilityTypesHandler.class)
                                        .beanMethod("retrieveAccountabilityTypes")
                                        .description("Retrieves all or some of the Accountability Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Accountability Types Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Accountability Type name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Types Records were successfully retrieved")
                                                        .implementationArray(AccountabilityType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Accountability Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/accountability_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateAccountabilityType,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateAccountabilityType")
                                        .beanClass(AccountabilityTypesHandler.class)
                                        .beanMethod("updateAccountabilityType")
                                        .description("Updates a single Accountability Type Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(AccountabilityType.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Type Record was successfully updated")
                                                        .implementation(AccountabilityType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Accountability Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/accountability_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateAccountabilityTypes,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateAccountabilityType")
                                        .beanClass(AccountabilityTypesHandler.class)
                                        .beanMethod("updateAccountabilityTypes")
                                        .description("Updates several Accountability Types Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(AccountabilityType.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Types Records were successfully updated")
                                                        .implementation(AccountabilityType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Accountability Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/accountability_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteAccountabilityType,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteAccountabilityType")
                                        .beanClass(AccountabilityTypesHandler.class)
                                        .beanMethod("deleteAccountabilityType")
                                        .description("Deletes a single Accountability Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Accountability Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Type Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Accountability Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Accountability Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/accountability_types/all",
                                accept(APPLICATION_JSON),
                                handler::deleteAccountabilityTypes,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteAccountabilityTypes")
                                        .beanClass(AccountabilityTypesHandler.class)
                                        .beanMethod("deleteAccountabilityTypes")
                                        .description("Deleted all or some of the Accountability Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Accountability Types Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Accountability Type name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Types Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Accountability Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Accountability Types Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}
