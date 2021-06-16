/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.services;

import global.moja.crftables.daos.Report;
import global.moja.crftables.exceptions.ServerException;
import global.moja.crftables.util.NameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class ReportSavingService {

    public Mono<Report> saveReport(Report report) {

        File output;
        try{
            output = createTempFile();

            try (FileOutputStream fos = new FileOutputStream(output)) {

                report.getWorkbook().write(fos);
                report.getWorkbook().close();
                report.setOutput(output);

            }
        } catch (Exception e) {
            return Mono.error(new ServerException("Could not successfully save  the generated report", e));
        }

        return Mono.just(report);
    }

    private File createTempFile() throws IOException {

        Path path = Paths.get("/tmp/" + NameUtil.generateUniqueName() + ".xlsx");

        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();


        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_EXECUTE);

        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);

        Files.setPosixFilePermissions(path, perms);

        return path.toFile();
    }
}
