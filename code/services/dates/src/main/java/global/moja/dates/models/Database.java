/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Database implements Comparable<Database> {

    private Long id;
    private String label;
    private String description;
    private String url;
    private Integer startYear;
    private Integer endYear;
    private Boolean processed;
    private Boolean published;
    private Boolean archived;
    private Integer version;

    @Override
    public int compareTo(Database database) {

        if(this.id != null && database.getId() != null){
            return this.id.compareTo(database.getId());
        } else {
            return 0;
        }

    }
}