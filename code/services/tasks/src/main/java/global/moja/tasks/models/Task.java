/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.models;

import lombok.*;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class Task implements Comparable<Task> {

    private Long id;
    private Long taskTypeId;
    private Long taskStatusId;
    private Long databaseId;
    private Integer issues;
    private Integer resolved;
    private Integer rejected;
    private String note;
    private Long lastUpdated;

    @Override
    public int compareTo(Task task) {

        if(this.id != null && task.getId() != null){
            return this.id.compareTo(task.getId());
        } else {
            return 0;
        }

    }
}
