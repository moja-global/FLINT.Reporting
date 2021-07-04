
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.databases.services;


import global.moja.databases.daos.DatabaseValidationStatus;
import global.moja.databases.models.*;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.pool.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class DatabaseValidityTestService {

    @Value("${spring.datasource.username}")
    String username;

    @Value("${spring.datasource.password}")
    String password;

    /**
     * Checks whether all the required database components can be reached for processing
     *
     * @param url the url of the database
     * @return the database's validation result
     */
    public Flux<DatabaseValidationStatus> validateDatabase(String url) {

        log.trace("Entering validateDatabase()");

        // Get a database connection if possible
        final Database database = isConnectable(url)? getDatabase(url) : null;

        // Validate
        return Flux.<DatabaseValidationStatus>create(fluxSink -> isDatabaseConnectionOk(database)
                .doOnNext(status -> {
                    if (!fluxSink.isCancelled()) {
                        fluxSink.next(status);
                    }
                })
                .flatMap(status -> isDateDimensionOk(database))
                .doOnNext(status -> {
                    if (!fluxSink.isCancelled()) {
                        fluxSink.next(status);
                    }
                })
                .flatMap(status -> isLocationDimensionOk(database))
                .doOnNext(status -> {
                    if (!fluxSink.isCancelled()) {
                        fluxSink.next(status);
                    }
                })
                .flatMap(status -> isVegetationTypeInfoMappingOk(database))
                .doOnNext(status -> {
                    if (!fluxSink.isCancelled()) {
                        fluxSink.next(status);
                    }
                })
                .flatMap(status -> isVegetationHistoryVegetationTypeInfoMappingOk(database))
                .doOnNext(status -> {
                    if (!fluxSink.isCancelled()) {
                        fluxSink.next(status);
                    }
                })
                .flatMap(status -> isFluxReportingResultsOk(database))
                .doOnNext(status -> {
                    if (!fluxSink.isCancelled()) {
                        fluxSink.next(status);
                        fluxSink.complete();
                        if(database != null) {
                            database.close();
                        }
                    }
                })
                .subscribe()).share();

    }

    private boolean isConnectable(String url) {

        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
            connection.close();
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    private Database getDatabase(String url) {

        return Database
                .nonBlocking()
                .url(url)
                .user(this.username)
                .password(this.password)
                .healthCheck(DatabaseType.POSTGRES)
                .idleTimeBeforeHealthCheck(5, TimeUnit.SECONDS)
                .maxPoolSize(Runtime.getRuntime().availableProcessors() * 5)
                .build();
    }

    private Mono<DatabaseValidationStatus> isDatabaseConnectionOk(Database database) {

        // Test the validity of the Database Connection
        log.info("Testing the validity of the Database Connection");

        if (database == null) {
            return
                    Mono.just(DatabaseValidationStatus
                            .builder()
                            .aspect("Database Connection")
                            .valid(false)
                            .message("Database Connection cannot be established")
                            .build());
        } else {


            String query = "SELECT now() as time";

            return Mono.from(database.select(query)
                    .get(rs -> rs.getString("time")))
                    .map(time -> {
                        if (!time.equals("")) {
                            return
                                    DatabaseValidationStatus
                                            .builder()
                                            .aspect("Database Connection")
                                            .valid(true)
                                            .message("Database Connection established")
                                            .build();
                        } else {
                            return
                                    DatabaseValidationStatus
                                            .builder()
                                            .aspect("Database Connection")
                                            .valid(false)
                                            .message("Database Connection cannot be established")
                                            .build();

                        }
                    });
        }

    }

    private Mono<DatabaseValidationStatus> isLocationDimensionOk(Database database) {


        // Test the validity of the Location Dimension
        log.info("Testing the validity of the Location Dimension");

        // Return a default false result if the database connection is null
        if (database == null) {
            return
                    Mono.just(DatabaseValidationStatus
                            .builder()
                            .aspect("Location Dimension")
                            .valid(false)
                            .message("Validity could not be established due to Database Connection Failure")
                            .build());
        }

        String query = "SELECT * FROM location_dimension LIMIT 1";
        return
                Mono.from(database
                        .select(query)
                        .get(rs ->
                                Location.builder()
                                        .id(rs.getLong("location_dimension_id_pk"))
                                        .partyId(rs.getLong("countyinfo_dimension_id_fk"))
                                        .tileId(rs.getLong("tileinfo_dimension_id_fk"))
                                        .vegetationHistoryId(rs.getLong("veghistory_dimension_id_fk"))
                                        .unitCount(rs.getLong("unitcount"))
                                        .unitAreaSum(rs.getDouble("unitareasum"))
                                        .build())
                        .onErrorResumeNext(Flowable.just(new Location())))
                        .map(location -> {
                            if (location.getId() != null) {
                                return
                                        DatabaseValidationStatus
                                                .builder()
                                                .aspect("Location Dimension")
                                                .valid(true)
                                                .message("Validity Check Succeeded")
                                                .build();
                            } else {
                                return
                                        DatabaseValidationStatus
                                                .builder()
                                                .aspect("Location Dimension")
                                                .valid(false)
                                                .message("Validity Check Failed")
                                                .build();

                            }
                        });


    }

    private Mono<DatabaseValidationStatus> isDateDimensionOk(Database database) {

        // Test the validity of the Date Dimension
        log.info("Testing the validity of the Date Dimension");

        // Return a default false result if the database connection is null
        if (database == null) {
            return
                    Mono.just(DatabaseValidationStatus
                            .builder()
                            .aspect("Date Dimension")
                            .valid(false)
                            .message("Validity could not be established due to Database Connection Failure")
                            .build());
        }

        String query = "SELECT * FROM date_dimension LIMIT 1";
        return Mono.from(database
                .select(query)
                .get(rs ->
                        Date.builder()
                                .id(rs.getLong("date_dimension_id_pk"))
                                .year(rs.getInt("year"))
                                .build())
                .onErrorResumeNext(Flowable.just(new Date())))
                .map(date -> {
                    if (date.getId() != null) {
                        return
                                DatabaseValidationStatus
                                        .builder()
                                        .aspect("Date Dimension")
                                        .valid(true)
                                        .message("Validity Check Succeeded")
                                        .build();
                    } else {
                        return
                                DatabaseValidationStatus
                                        .builder()
                                        .aspect("Date Dimension")
                                        .valid(false)
                                        .message("Validity Check Failed")
                                        .build();

                    }
                });


    }

    private Mono<DatabaseValidationStatus> isVegetationHistoryVegetationTypeInfoMappingOk(Database database) {

        // Test the validity of the Vegetation History Vegetation Type Info Mapping
        log.info("Testing the validity of the Vegetation History Vegetation Type Info Mapping ");

        // Return a default false result if the database connection is null
        if (database == null) {
            return
                    Mono.just(DatabaseValidationStatus
                            .builder()
                            .aspect("Vegetation History Vegetation Type Information Mapping")
                            .valid(false)
                            .message("Validity could not be established due to Database Connection Failure")
                            .build());
        }

        String query = "SELECT * FROM veghistory_vegtypeinfo_mapping LIMIT 1";
        return Mono.from(database
                .select(query)
                .get(rs ->
                        VegetationHistoryVegetationType.builder()
                                .id(rs.getLong("veghistory_vegtypeinfo_mapping_id_pk"))
                                .vegetationHistoryId(rs.getLong("veghistory_dimension_id_fk"))
                                .vegetationTypeId(rs.getLong("vegtypeinfo_dimension_id_fk"))
                                .itemNumber(rs.getLong("itemnumber"))
                                .year(rs.getInt("year"))
                                .build())
                .onErrorResumeNext(Flowable.just(new VegetationHistoryVegetationType())))
                .map(vegetationHistoryVegetationType -> {
                    if (vegetationHistoryVegetationType.getId() != null) {
                        return
                                DatabaseValidationStatus
                                        .builder()
                                        .aspect("Vegetation History Vegetation Type Information Mapping")
                                        .valid(true)
                                        .message("Validity Check Succeeded")
                                        .build();

                    } else {
                        return
                                DatabaseValidationStatus
                                        .builder()
                                        .aspect("Vegetation History Vegetation Type Information Mapping")
                                        .valid(false)
                                        .message("Validity Check Failed")
                                        .build();

                    }
                });


    }

    private Mono<DatabaseValidationStatus> isVegetationTypeInfoMappingOk(Database database) {

        // Test the validity of the Vegetation Type Info Mapping
        log.info("Testing the validity of the Vegetation Type Info Mapping ");

        // Return a default false result if the database connection is null
        if (database == null) {
            return
                    Mono.just(DatabaseValidationStatus
                            .builder()
                            .aspect("Vegetation Types Information Mapping")
                            .valid(false)
                            .message("Validity could not be established due to Database Connection Failure")
                            .build());
        }

        String query = "SELECT * FROM vegtypeinfo_dimension LIMIT 1";
        return Mono.from(database
                .select(query)
                .get(rs ->
                        VegetationType.builder()
                                .id(rs.getLong("vegtypeinfo_dimension_id_pk"))
                                .coverTypeId(rs.getLong("ipcccovertypeinfo_dimension_id_fk"))
                                .name(rs.getString("vegtypename"))
                                .woodyType(rs.getBoolean("woodtype"))
                                .naturalSystem(rs.getBoolean("naturalsystem"))
                                .build())
                .onErrorResumeNext(Flowable.just(new VegetationType())))
                .map(vegetationType -> {
                    if (vegetationType.getId() != null) {
                        return
                                DatabaseValidationStatus
                                        .builder()
                                        .aspect("Vegetation Types Information Mapping")
                                        .valid(true)
                                        .message("Validity Check Succeeded")
                                        .build();

                    } else {
                        return
                                DatabaseValidationStatus
                                        .builder()
                                        .aspect("Vegetation Types Information Mapping")
                                        .valid(false)
                                        .message("Validity Check Failed")
                                        .build();

                    }
                });


    }

    private Mono<DatabaseValidationStatus> isFluxReportingResultsOk(Database database) {

        // Test the validity of the Flux Reporting Results
        log.info("Testing the validity of the Flux Reporting Results");

        // Return a default false result if the database connection is null
        if (database == null) {
            return
                    Mono.just(DatabaseValidationStatus
                            .builder()
                            .aspect("Flux Reporting Results")
                            .valid(false)
                            .message("Validity could not be established due to Database Connection Failure")
                            .build());
        }

        String query =
                "SELECT " +
                        "flux_reporting_results_id_pk, " +
                        "date_dimension_id_fk, " +
                        "location_dimension_id_fk, " +
                        "fluxtypeinfo_dimension_id_fk, " +
                        "source_poolinfo_dimension_id_fk, " +
                        "sink_poolinfo_dimension_id_fk, " +
                        "round( flux::numeric, 12 ) flux, " +
                        "itemcount " +
                        "FROM materialized_flux_reporting_results " +
                        "LIMIT 1";

        return Mono.from(database
                .select(query)
                .get(rs ->
                        FluxReportingResult.builder()
                                .id(rs.getLong("flux_reporting_results_id_pk"))
                                .dateId(rs.getLong("date_dimension_id_fk"))
                                .locationId(rs.getLong("location_dimension_id_fk"))
                                .fluxTypeId(rs.getLong("fluxtypeinfo_dimension_id_fk"))
                                .sourcePoolId(rs.getLong("source_poolinfo_dimension_id_fk"))
                                .sinkPoolId(rs.getLong("sink_poolinfo_dimension_id_fk"))
                                .flux(rs.getDouble("flux"))
                                .itemCount(rs.getLong("itemcount"))
                                .build())
                .onErrorResumeNext(Flowable.just(new FluxReportingResult())))
                .map(fluxReportingResult -> {
                    if (fluxReportingResult.getId() != null) {
                        return
                                DatabaseValidationStatus
                                        .builder()
                                        .aspect("Flux Reporting Results")
                                        .valid(true)
                                        .message("Validity Check Succeeded")
                                        .build();

                    } else {
                        return
                                DatabaseValidationStatus
                                        .builder()
                                        .aspect("Flux Reporting Results")
                                        .valid(false)
                                        .message("Validity Check Failed")
                                        .build();

                    }
                });

    }
}
