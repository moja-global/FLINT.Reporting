/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.util.webclient.impl;


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
    LocationsWebClientUtil locationsWebClientUtil;

    @Autowired
    LandUseCategoriesWebClientUtil landUseCategoriesWebClientUtil;

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

    public WebClient getLandUseCategoriesWebClient() {
        return landUseCategoriesWebClientUtil.getLandUseCategoriesWebClient();
    }

    public WebClient getLocationsWebClient() {
        return locationsWebClientUtil.getLocationsWebClient();
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
