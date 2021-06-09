/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.util.webclient.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
public class

WebClientUtil {

    @Autowired
    ConversionAndRemainingPeriodsWebClientUtil conversionAndRemainingPeriodsWebClientUtil;

    @Autowired
    CoverTypesWebClientUtil coverTypesWebClientUtil;

    @Autowired
    DatabasesWebClientUtil databasesWebClientUtil;

    @Autowired
    DatesWebClientUtil datesWebClientUtil;

    @Autowired
    EmissionTypesWebClientUtil emissionTypesWebClientUtil;

    @Autowired
    FluxReportingResultsWebClientUtil fluxReportingResultsWebClientUtil;

    @Autowired
    FluxesToReportingVariablesWebClientUtil fluxesToReportingVariablesWebClientUtil;

    @Autowired
    LandUseCategoriesWebClientUtil landUseCategoriesWebClientUtil;

    @Autowired
    LandUsesFluxTypesWebClientUtil landUsesFluxTypesWebClientUtil;

    @Autowired
    LandUsesFluxTypesToReportingTablesWebClientUtil landUsesFluxTypesToReportingTablesWebClientUtil;

    @Autowired
    LocationsWebClientUtil locationsWebClientUtil;

    @Autowired
    QuantityObservationsWebClientUtil quantityObservationsWebClientUtil;

    @Autowired
    ReportingTablesWebClientUtil reportingTablesWebClientUtil;

    @Autowired
    VegetationHistoryVegetationTypesWebClientUtil vegetationHistoryVegetationTypesWebClientUtil;

    @Autowired
    VegetationTypesWebClientUtil vegetationTypesWebClientUtil;


    public WebClient getConversionAndRemainingPeriodsWebClient() {
        return conversionAndRemainingPeriodsWebClientUtil.getConversionAndRemainingPeriodsWebClient();
    }

    public WebClient getCoverTypesWebClient() {
        return coverTypesWebClientUtil.getCoverTypesWebClient();
    }

    public WebClient getDatabasesWebClient() {
        return databasesWebClientUtil.getDatabasesWebClient();
    }

    public WebClient getDatesWebClient() {
        return datesWebClientUtil.getDatesWebClient();
    }

    public WebClient getEmissionTypesWebClient() {
        return emissionTypesWebClientUtil.getEmissionTypesWebClient();
    }


    public WebClient getFluxReportingResultsWebClient() {
        return fluxReportingResultsWebClientUtil.getFluxReportingResultsWebClient();
    }

    public WebClient getFluxesToReportingVariablesWebClient() {
        return fluxesToReportingVariablesWebClientUtil.getFluxesToReportingVariablesWebClient();
    }

    public WebClient getLandUseCategoriesWebClient() {
        return landUseCategoriesWebClientUtil.getLandUseCategoriesWebClient();
    }

    public WebClient getLandUsesFluxTypesWebClient() {
        return landUsesFluxTypesWebClientUtil.getLandUsesFluxTypesWebClient();
    }

    public WebClient getLandUsesFluxTypesToReportingTablesWebClient() {
        return landUsesFluxTypesToReportingTablesWebClientUtil.getLandUsesFluxTypesToReportingTablesWebClient();
    }

    public WebClient getLocationsWebClient() {
        return locationsWebClientUtil.getLocationsWebClient();
    }

    public WebClient getQuantityObservationsWebClient() {
        return quantityObservationsWebClientUtil.getQuantityObservationsWebClient();
    }

    public WebClient getReportingTablesWebClient() {
        return reportingTablesWebClientUtil.getReportingTablesWebClient();
    }

    public WebClient getVegetationHistoryVegetationTypesWebClient() {
        return vegetationHistoryVegetationTypesWebClientUtil.getVegetationHistoryVegetationTypesWebClient();
    }

    public WebClient getVegetationTypesWebClient() {
        return vegetationTypesWebClientUtil.getVegetationTypesWebClient();
    }




}
