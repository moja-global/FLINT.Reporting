/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.configurations;

import global.moja.pools.handlers.PoolsHandler;
import global.moja.pools.models.Pool;
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
    RouterFunction<ServerResponse> routeRequests(PoolsHandler handler) {

        return
                route()
                        .POST("/api/v1/pools",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createPool,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createPool")
                                        .beanClass(PoolsHandler.class)
                                        .beanMethod("createPool")
                                        .description("Inserts a single Pool Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Pool.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Pool Record was successfully created")
                                                        .implementation(Pool.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Pool Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/pools/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createPools,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createPools")
                                        .beanClass(PoolsHandler.class)
                                        .beanMethod("createPools")
                                        .description("Inserts several Pools Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Pool.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Pools Records were successfully created")
                                                        .implementationArray(Pool.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Pools Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/pools/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrievePool,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrievePool")
                                        .beanClass(PoolsHandler.class)
                                        .beanMethod("retrievePool")
                                        .description("Retrieves a single Pool Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Pool Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Pool Record was successfully retrieved")
                                                        .implementation(Pool.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Pool Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/pools/all",
                                accept(APPLICATION_JSON),
                                handler::retrievePools,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrievePools")
                                        .beanClass(PoolsHandler.class)
                                        .beanMethod("retrievePools")
                                        .description("Retrieves all or some of the Pools Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Pools Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("poolTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Pool type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("poolDataSourceId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Pool data source to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("unitId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Pool data source to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Pool name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Pools Records were successfully retrieved")
                                                        .implementationArray(Pool.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Pools Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/pools",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updatePool,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updatePool")
                                        .beanClass(PoolsHandler.class)
                                        .beanMethod("updatePool")
                                        .description("Updates a single Pool Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Pool.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Pool Record was successfully updated")
                                                        .implementation(Pool.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Pool Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/pools/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updatePools,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updatePool")
                                        .beanClass(PoolsHandler.class)
                                        .beanMethod("updatePools")
                                        .description("Updates several Pools Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Pool.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Pools Records were successfully updated")
                                                        .implementation(Pool.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Pools Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/pools/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deletePool,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deletePool")
                                        .beanClass(PoolsHandler.class)
                                        .beanMethod("deletePool")
                                        .description("Deletes a single Pool Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Pool Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Pool Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Pools Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Pool Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/pools/all",
                                accept(APPLICATION_JSON),
                                handler::deletePools,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deletePools")
                                        .beanClass(PoolsHandler.class)
                                        .beanMethod("deletePools")
                                        .description("Deleted all or some of the Pools Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Pools Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("poolTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Pool type to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("poolDataSourceId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Pool data source to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("poolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Pool data source to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Pool name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Pools Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Pools Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Pools Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}
