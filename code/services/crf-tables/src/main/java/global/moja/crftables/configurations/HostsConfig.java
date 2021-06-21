/*
 * Copyright (C) 2018 The System for Land-based Emissions Estimation in Kenya (SLEEK)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author Kwaje Anthony <kwajeanthony@gmail.com>
 */
@Configuration
@Getter
public class HostsConfig {

    @Value("${databases.service.host}/api/v1/databases")
    String databasesServiceHost;

    @Value("${parties.service.host}/api/v1/parties")
    String partiesServiceHost;

    @Value("${quantity.observations.service.host}/api/v1/quantity_observations")
    String quantityObservationsServiceHost;
}
