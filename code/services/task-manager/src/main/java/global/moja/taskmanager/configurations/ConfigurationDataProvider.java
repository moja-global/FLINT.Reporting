
/*
 * Copyright (C) 2020 The Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.configurations;

import global.moja.taskmanager.exceptions.ServerException;
import global.moja.taskmanager.models.Accountability;
import global.moja.taskmanager.models.AccountabilityRule;
import global.moja.taskmanager.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final EndpointsUtil endpointsUtil;

    private List<Long> dataProcessingLevelPartiesIds;
    private Map<Integer, LinkedList<Long>> dataAggregationLevelsPartiesIds;
    
    @Autowired
    public ConfigurationDataProvider(EndpointsUtil endpointsUtil){
        this.endpointsUtil = endpointsUtil;
    }

    @PostConstruct
    void init() throws ServerException {

        // Retrieve and sort the Administrative Units Accountability Rules in ascending order
        log.trace("Retrieving and sorting the Administrative Units Accountability Rules in ascending order");
        LinkedList<AccountabilityRule> administrativeUnitsAccountabilityRules =
                endpointsUtil
                        .retrieveAccountabilityRules(ADMINISTRATIVE_HIERARCHY_ACCOUNTABILITY_TYPE_ID)
                        .collectSortedList()
                        .map(LinkedList::new)
                        .block();

        log.debug("Administrative Units Accountability Rules = {}", administrativeUnitsAccountabilityRules);

        // Retrieve the Data Processing Level Accountability Rule specification
        log.trace("Retrieving the Data Processing Level Accountability Rule");
        AccountabilityRule dataProcessingLevelAccountabilityRule =
                administrativeUnitsAccountabilityRules
                        .stream()
                        .filter(rule -> rule.getSubsidiaryPartyTypeId().equals(DATA_PROCESSING_ENTRY_LEVEL_PARTY_TYPE_ID))
                        .findAny()
                        .orElse(null);

        log.debug("Data Processing Level Accountability Rule = {}", dataProcessingLevelAccountabilityRule);

        // Ascertain that the Data Processing Level Accountability Rule is non null
        log.trace("Ascertaining that the Data Processing Level Accountability Rule is non null");
        if (dataProcessingLevelAccountabilityRule == null) {
            throw new ServerException("The Data Processing Level Accountability Rule is null");
        }

        // Retrieve the ids of the Administrative Units that are at the entry data processing level
        log.trace("Retrieving the ids of the Administrative Units that are at the entry data processing level");
        dataProcessingLevelPartiesIds =
                endpointsUtil
                        .retrieveAccountabilities(dataProcessingLevelAccountabilityRule.getId())
                        .sort()
                        .map(Accountability::getSubsidiaryPartyId)
                        .collectList()
                        .map(LinkedList::new)
                        .block();

        log.debug("Administrative Units at the entry data processing level = {}", dataProcessingLevelPartiesIds);

        // Retrieve the ids of the administrative units that are at the different data aggregation levels
        log.trace("Retrieving the ids of the administrative units that are at the different data aggregation levels");
        this.dataAggregationLevelsPartiesIds = new LinkedHashMap<>();
        AtomicInteger aggregationLevel = new AtomicInteger(0);
        administrativeUnitsAccountabilityRules
                .subList(0, administrativeUnitsAccountabilityRules.indexOf(dataProcessingLevelAccountabilityRule))
                .stream()
                .sorted(Collections.reverseOrder()) // This is important because aggregation is a bottom up affair
                .forEach(accountabilityRule ->
                        dataAggregationLevelsPartiesIds.put(
                                aggregationLevel.incrementAndGet(),
                                endpointsUtil
                                        .retrieveAccountabilities(accountabilityRule.getId())
                                        .sort()
                                        .map(Accountability::getSubsidiaryPartyId)
                                        .collectList()
                                        .map(LinkedList::new)
                                        .block()
                        ));

        log.debug("Administrative Units at the different data aggregation levels = {}", dataAggregationLevelsPartiesIds);

    }

    public List<Long> getDataProcessingLevelPartiesIds() {
        return dataProcessingLevelPartiesIds;
    }

    public int getTotalDataProcessingIssues() {
        return dataProcessingLevelPartiesIds.size();
    }

    public Map<Integer, LinkedList<Long>> getDataAggregationLevelsPartiesIds() {
        return dataAggregationLevelsPartiesIds;
    }

    public int getTotalDataAggregationIssues() {
        return dataAggregationLevelsPartiesIds
                .keySet()
                .stream()
                .map(level -> dataAggregationLevelsPartiesIds.get(level).size())
                .reduce(0, Integer::sum);
    }

}
