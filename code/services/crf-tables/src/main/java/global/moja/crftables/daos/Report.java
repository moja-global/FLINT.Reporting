/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.daos;

import global.moja.crftables.models.Database;
import global.moja.crftables.models.Party;
import global.moja.crftables.models.QuantityObservation;
import lombok.*;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.List;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class Report {

    private Party party;
    private Database database;
    private Integer minYear;
    private Integer maxYear;
    private List<QuantityObservation> quantityObservations;
    private Workbook workbook;
    private Integer lastRowIndex;
    private File output; //Final output file

}
