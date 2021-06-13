
/*
 * Copyright (C) 2020 The Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.configurations;

import global.moja.taskmanager.exceptions.ServerException;
import global.moja.taskmanager.models.AccountabilityRule;
import global.moja.taskmanager.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Configuration
@PropertySource(value = {"classpath:hosts.properties"})
@Slf4j
public class ConfigurationDataProvider {

    @Value("${administrative.hierarchy.accountability.type.id}")
    Long ADMINISTRATIVE_HIERARCHY_ACCOUNTABILITY_TYPE_ID;

    @Value("${data.processing.entry.level.party.type.id}")
    Long DATA_PROCESSING_ENTRY_LEVEL_PARTY_TYPE_ID;

    private final LinkedList<AccountabilityRule> administrativeUnitsAccountabilityRules;
    private final List<Long> dataProcessingEntryLevelPartiesIds;

    @Autowired
    public ConfigurationDataProvider(EndpointsUtil endpointsUtil) throws ServerException {

        // Retrieve and sort the Administrative Units Accountability Rules
        log.trace("Retrieving and sorting the Administrative Units Accountability Rules");
        this.administrativeUnitsAccountabilityRules =
                endpointsUtil
                        .retrieveAccountabilityRules(ADMINISTRATIVE_HIERARCHY_ACCOUNTABILITY_TYPE_ID)
                        .collectSortedList()
                        .map(list -> new LinkedList<AccountabilityRule>(list))
                        .block();
        log.debug("Administrative Units Accountability Rules = {}", this.administrativeUnitsAccountabilityRules);

        // Retrieve the Data Processing Level Accountability Rule specification
        log.trace("Retrieving the Data Processing Level Accountability Rule");
        AccountabilityRule dataProcessingLevelAccountabilityRule =
                this.administrativeUnitsAccountabilityRules
                        .stream()
                        .filter(rule -> rule.getSubsidiaryPartyTypeId() == DATA_PROCESSING_ENTRY_LEVEL_PARTY_TYPE_ID)
                        .findAny()
                        .orElse(null);

        log.debug("Data Processing Level Accountability Rule = {}", dataProcessingLevelAccountabilityRule);

        // Ascertain that the Data Processing Level Accountability Rule is non null
        log.trace("Ascertaining that the Data Processing Level Accountability Rule is non null");
        if(dataProcessingLevelAccountabilityRule == null) {
            throw new ServerException("The Data Processing Level Accountability Rule is null");
        }

        // Retrieve the ids of the administrative units that form the entry level data processing task group
        log.trace("Retrieving the ids of the administrative units that form the entry level data processing task group");
        dataProcessingEntryLevelPartiesIds =
                endpointsUtil
                        .retrieveAccountabilities(
                                ADMINISTRATIVE_HIERARCHY_ACCOUNTABILITY_TYPE_ID,
                                administrativeUnitsAccountabilityRules
                                        .stream()
                                        .filter(accountabilityRule ->
                                                accountabilityRule.getSubsidiaryPartyTypeId() ==
                                                        DATA_PROCESSING_ENTRY_LEVEL_PARTY_TYPE_ID)
                                        .findAny()
                                        .map(accountabilityRule -> accountabilityRule.getParentPartyTypeId())
                                        .orElse(0L))
                        .map(accountability -> accountability.getSubsidiaryPartyId())
                        .collectList()
                        .block();

        // Retrieve the ids of the administrative units that form the data aggregation task group
        log.trace("Retrieving the ids of the administrative units that form the data aggregation task group");
        this.administrativeUnitsAccountabilityRules
                .subList(0, this.administrativeUnitsAccountabilityRules.indexOf(dataProcessingLevelAccountabilityRule))
                .stream();

    }


    //<editor-fold desc="Cover Types">



    //</editor-fold>

}
