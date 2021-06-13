/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.models;

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
@EqualsAndHashCode
public class Task implements Comparable<Task> {

    private Long id;

    @EqualsAndHashCode.Exclude
    private Long taskTypeId;

    @EqualsAndHashCode.Exclude
    private Long taskStatusId;

    @EqualsAndHashCode.Exclude
    private Long databaseId;

    @EqualsAndHashCode.Exclude
    private Integer issues;

    @EqualsAndHashCode.Exclude
    private Integer resolved;

    @EqualsAndHashCode.Exclude
    private Integer rejected;

    @EqualsAndHashCode.Exclude
    private String note;

    @Override
    public int compareTo(Task task) {

        if(this.id != null && task.getId() != null){
            return this.id.compareTo(task.getId());
        } else {
            return 0;
        }

    }
}
