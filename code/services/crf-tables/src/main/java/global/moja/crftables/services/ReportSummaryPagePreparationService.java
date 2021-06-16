/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http:// mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.services;

import global.moja.crftables.util.styling.CellStyleUtil;
import global.moja.crftables.daos.Report;
import global.moja.crftables.daos.ReportingVariableFormattingRule;
import global.moja.crftables.exceptions.ServerException;
import global.moja.crftables.models.QuantityObservation;
import global.moja.crftables.util.styling.rules.SummaryReportFormattingRules;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class ReportSummaryPagePreparationService {

    @Value("${summary.report.template.index}")
    private Integer SUMMARY_REPORT_TEMPLATE_INDEX;

    // Forest Land

    @Value("${forest.land.lu.id}")
    private Long FOREST_LAND_LU_ID;

    @Value("${forest.land.id.remaining.forest.land.lu.id}")
    private Long FOREST_LAND_REMAINING_FOREST_LAND_LU_ID;

    @Value("${land.converted.to.forest.land.lu.id}")
    private Long LAND_CONVERTED_TO_FOREST_LAND_LU_ID;

    @Value("${cropland.converted.to.forest.land.lu.id}")
    private Long CROPLAND_CONVERTED_TO_FOREST_LAND_LU_ID;

    @Value("${grassland.converted.to.forest.land.lu.id}")
    private Long GRASSLAND_CONVERTED_TO_FOREST_LAND_LU_ID;

    @Value("${wetlands.converted.to.forest.land.lu.id}")
    private Long WETLANDS_CONVERTED_TO_FOREST_LAND_LU_ID;

    @Value("${settlements.converted.to.forest.land.lu.id}")
    private Long SETTLEMENTS_CONVERTED_TO_FOREST_LAND_LU_ID;

    @Value("${other.land.converted.to.forest.land.lu.id}")
    private Long OTHER_LAND_CONVERTED_TO_FOREST_LAND_LU_ID;

    // Cropland

    @Value("${cropland.lu.id}")
    private Long CROPLAND_LU_ID;

    @Value("${cropland.remaining.cropland.lu.id}")
    private Long CROPLAND_REMAINING_CROPLAND_LU_ID;

    @Value("${land.converted.to.cropland.lu.id}")
    private Long LAND_CONVERTED_TO_CROPLAND_LU_ID;

    @Value("${forest.land.id.converted.to.cropland.lu.id}")
    private Long FOREST_LAND_CONVERTED_TO_CROPLAND_LU_ID;

    @Value("${grassland.converted.to.cropland.lu.id}")
    private Long GRASSLAND_CONVERTED_TO_CROPLAND_LU_ID;

    @Value("${wetlands.converted.to.cropland.lu.id}")
    private Long WETLANDS_CONVERTED_TO_CROPLAND_LU_ID;

    @Value("${settlements.converted.to.cropland.lu.id}")
    private Long SETTLEMENTS_CONVERTED_TO_CROPLAND_LU_ID;

    @Value("${other.land.converted.to.cropland.lu.id}")
    private Long OTHER_LAND_CONVERTED_TO_CROPLAND_LU_ID;

    // Grassland

    @Value("${grassland.lu.id}")
    private Long GRASSLAND_LU_ID;

    @Value("${grassland.remaining.grassland.lu.id}")
    private Long GRASSLAND_REMAINING_GRASSLAND_LU_ID;

    @Value("${land.converted.to.grassland.lu.id}")
    private Long LAND_CONVERTED_TO_GRASSLAND_LU_ID;

    @Value("${cropland.converted.to.grassland.lu.id}")
    private Long CROPLAND_CONVERTED_TO_GRASSLAND_LU_ID;

    @Value("${forest.land.id.converted.to.grassland.lu.id}")
    private Long FOREST_LAND_CONVERTED_TO_GRASSLAND_LU_ID;

    @Value("${wetlands.converted.to.grassland.lu.id}")
    private Long WETLANDS_CONVERTED_TO_GRASSLAND_LU_ID;

    @Value("${settlements.converted.to.grassland.lu.id}")
    private Long SETTLEMENTS_CONVERTED_TO_GRASSLAND_LU_ID;

    @Value("${other.land.converted.to.grassland.lu.id}")
    private Long OTHER_LAND_CONVERTED_TO_GRASSLAND_LU_ID;

    // Wetlands

    @Value("${wetlands.lu.id}")
    private Long WETLANDS_LU_ID;

    @Value("${wetlands.remaining.wetlands.lu.id}")
    private Long WETLANDS_REMAINING_WETLANDS_LU_ID;

    @Value("${land.converted.to.wetlands.lu.id}")
    private Long LAND_CONVERTED_TO_WETLANDS_LU_ID;

    @Value("${cropland.converted.to.wetlands.lu.id}")
    private Long CROPLAND_CONVERTED_TO_WETLANDS_LU_ID;

    @Value("${grassland.converted.to.wetlands.lu.id}")
    private Long GRASSLAND_CONVERTED_TO_WETLANDS_LU_ID;

    @Value("${forest.land.id.converted.to.wetlands.lu.id}")
    private Long FOREST_LAND_CONVERTED_TO_WETLANDS_LU_ID;

    @Value("${settlements.converted.to.wetlands.lu.id}")
    private Long SETTLEMENTS_CONVERTED_TO_WETLANDS_LU_ID;

    @Value("${other.land.converted.to.wetlands.lu.id}")
    private Long OTHER_LAND_CONVERTED_TO_WETLANDS_LU_ID;

    @Value("${net.carbon.dioxide.emissions.removals.reporting.variable.id}")
    private Long NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID;

    @Value("${methane.reporting.variable.id}")
    private Long METHANE_REPORTING_VARIABLE_ID;

    @Value("${nitrous.oxide.reporting.variable.id}")
    private Long NITROUS_OXIDE_REPORTING_VARIABLE_ID;

    @Autowired
    private SummaryReportFormattingRules summaryReportFormattingRules;

    @Autowired
    private CellStyleUtil cellStyleUtil;

    public Mono<Report> prepareSummaryReport(Report report) {

        log.trace("Entering prepareSummaryReport()");

        // Create the summary report worksheet from the template sectoral report
        log.trace("Creating the summary report worksheet from the template sectoral report");
        Sheet sheet = report.getWorkbook().cloneSheet(SUMMARY_REPORT_TEMPLATE_INDEX);

        // Name the summary report appropriately
        log.trace("Naming the summary report appropriately");
        sheet.getWorkbook().setSheetName(
                sheet.getWorkbook().getSheetIndex(sheet), 
                getSummaryReportTableTitle());

        // (Re) Initialize the last row index
        // The last row index is a 0 based index of the last row in the table.
        // New data should be inserted from last row + 1 to avoid overwriting previously written data
        log.trace("(Re) Initializing the last row index");
        report.setLastRowIndex(1);

        // Fill in the report data
        log.trace("Filling in the report data");
        try {
            fillYearsSegment(report);

            fillUnitsSegment(report);

            fillTotalLULUCFSegment(report);

            for (Long landUseCategoryId : new Long[]{
                    FOREST_LAND_LU_ID,
                    CROPLAND_LU_ID,
                    GRASSLAND_LU_ID,
                    WETLANDS_LU_ID}) {

                if (report.getQuantityObservations()
                        .stream()
                        .anyMatch(quantityObservation ->
                                getLandUsesSubcategoriesIds(landUseCategoryId)
                                        .contains(quantityObservation.getLandUseCategoryId()))) {
                    fillLandUseCategorySegment(report, landUseCategoryId);
                }
            }

            // Auto-size the report columns
            autoSizeSheetColumns(report);

        } catch (Exception e) {
            return Mono.error(new ServerException("Report Generation Failed"));
        }


        // Return the updated Report Details
        log.trace("Return the updated Report Details");
        return Mono.just(report);

    }

    private void fillYearsSegment(Report report) throws Exception {

        log.trace("Filling in the Years Segment");

        // Read in the summary report worksheet
        log.trace("Reading in the summary report worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSummaryReportTableTitle());

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle valueCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                summaryReportFormattingRules.getSummaryReportYearValueFormattingRule());

        // Set and style the year header rows
        log.trace("Setting and styling the year header rows");
        int columnIndex = 2;
        Row row = sheet.getRow(0);
        for (int year = report.getMinYear(); year <= report.getMaxYear(); year++) {

            Cell cell = row.createCell(columnIndex);
            cell.setCellValue(year);
            cell.setCellStyle(valueCellStyle);

            columnIndex += 1;
        }

    }

    private void fillUnitsSegment(Report report) throws Exception {

        log.trace("Filling in the Units Segment");

        // Read in the summary report worksheet
        log.trace("Reading in the summary report worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSummaryReportTableTitle());

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                summaryReportFormattingRules.getSummaryReportUnitsTitleFormattingRule());

        // Declare / initialize processing variables
        log.trace("Declaring / initializing local variables");
        int columnIndex = 2;
        Row row = sheet.getRow(1);

        // Set the unit of measure data and style its cell
        log.trace("Setting the unit of measure data and styling its cell");
        Cell units = row.createCell(columnIndex);
        units.setCellValue("Kt");
        units.setCellStyle(titleCellStyle);

        // Increment the column index by 1
        log.trace("Incrementing the column index by 1");
        columnIndex += 1;

        // Create additional empty header rows and style them based on the total number of years minus 1
        log.trace("Creating additional empty header rows and styling them based on the total number of years minus 1");
        for (int year = report.getMinYear(); year < report.getMaxYear(); year++) {

            Cell empty = row.createCell(columnIndex);
            empty.setCellStyle(titleCellStyle);

            columnIndex += 1;
        }

        // Merge the data / empty cells
        // Syntax: sheet.addMergedRegion(new CellRangeAddress(rowFrom,rowTo,colFrom,colTo));
        log.trace("Merging the data / empty cells");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, columnIndex - 1));

    }

    private void fillTotalLULUCFSegment(Report report) throws Exception {

        log.trace("Filling in the LULUCF Segment");

        // Read in the summary report worksheet
        log.trace("Reading in the summary report worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSummaryReportTableTitle());

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                summaryReportFormattingRules.getSummaryReportTotalLULUCFTitleFormattingRule());

        // Set the column index to 0
        log.trace("Setting the column index to 0");
        int columnIndex = 0;

        // Insert 3 new rows into the worksheet
        log.trace("Inserting 3 new rows into the worksheet");
        Row row1 = sheet.createRow(report.getLastRowIndex() + 1);
        Row row2 = sheet.createRow(report.getLastRowIndex() + 2);
        Row row3 = sheet.createRow(report.getLastRowIndex() + 3);

        // Set the title in the first row
        log.trace("Setting the title in the first row");
        Cell title = row1.createCell(columnIndex);
        title.setCellValue("4. Total LULUCF");
        title.setCellStyle(titleCellStyle);

        // Create two additional empty cells under the title cell
        log.trace("Creating two additional empty cells under the title cell");
        Cell cell2 = row2.createCell(columnIndex);
        cell2.setCellStyle(titleCellStyle);

        Cell cell3 = row3.createCell(columnIndex);
        cell3.setCellStyle(titleCellStyle);

        // Merge the 3 cells
        log.trace("Merging the 3 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 3,
                        columnIndex,
                        columnIndex));

        // Fill the Net emissions/removals Data
        log.trace("Filling the Net emissions/removals Data");
        fillTotalLULUCFDataRow(report, NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID, row1);

        // Fill the CH4 Data
        log.trace("Filling the CH4 Data");
        fillTotalLULUCFDataRow(report, METHANE_REPORTING_VARIABLE_ID, row2);

        // Fill the N2O Data
        log.trace("Filling the N2O Data");
        fillTotalLULUCFDataRow(report, NITROUS_OXIDE_REPORTING_VARIABLE_ID, row3);

        // Update the last data row index
        log.trace("Updating the last data row index");
        report.setLastRowIndex(report.getLastRowIndex() + 3);

    }

    private void fillTotalLULUCFDataRow(
            Report report,
            final Long reportingVariableId,
            final Row row) throws Exception {

        // Initialize the cell styles
        log.trace("Initializing the cell styles");

        ReportingVariableFormattingRule reportingVariableFormattingRule =
                summaryReportFormattingRules
                        .getSummaryReportTotalLULUCFReportingVariableFormattingRule(reportingVariableId);

        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(report.getWorkbook(),
                                reportingVariableFormattingRule
                                        .getReportingVariableTitleFormattingRule());

        CellStyle valueCellStyle =
                cellStyleUtil
                        .getCellStyle(report.getWorkbook(),
                                reportingVariableFormattingRule
                                        .getReportingVariableValueFormattingRule());

        // Set the column index to 1
        log.trace("Setting the column index to 1");

        int columnIndex = 1;

        // Set and style the title
        log.trace("Setting and styling the title");

        Cell title = row.createCell(columnIndex);
        if (reportingVariableId.equals(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID)) {
            title.setCellValue("Net emissions/removals (Kt CO2)");
        } else if (reportingVariableId.equals(METHANE_REPORTING_VARIABLE_ID)) {
            title.setCellValue("CH4 (Kt)");
        } else if (reportingVariableId.equals(NITROUS_OXIDE_REPORTING_VARIABLE_ID)) {
            title.setCellValue("N2O (Kt)");
        }
        title.setCellStyle(titleCellStyle);

        // Increment the column index by 1
        log.trace("Incrementing the column index by 1");

        columnIndex += 1;

        // Fill the emissions/removals data
        log.trace("Filling the emissions/removals data");
        for (int year = report.getMinYear(); year <= report.getMaxYear(); year++) {

            int finalYear = year;
            Cell data = row.createCell(columnIndex);

            try {
                data.setCellValue(
                        report.getQuantityObservations()
                                .stream()
                                .filter(quantityObservation -> quantityObservation.getYear() == finalYear)
                                .filter(quantityObservation -> quantityObservation.getReportingVariableId()
                                        .equals(reportingVariableId))
                                .map(QuantityObservation::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .doubleValue());
            } catch (Exception e) {
                log.warn("Write attempt for Summary Report, Total LULUCF, Reporting Variable {}, Year {} Data failed", report, year);
                data.setCellValue(BigDecimal.ZERO.doubleValue());
            }

            data.setCellStyle(valueCellStyle);

            columnIndex += 1;
        }

    }

    private void fillLandUseCategorySegment(Report report, final Long landUseCategoryId) throws Exception {

        log.trace("Filling in Land Use Category Segment");
        fillTotalLandSegment(report, landUseCategoryId);
        fillLandRemainingLandSegment(report, landUseCategoryId);
        fillTotalLandConvertedToLandSegment(report, landUseCategoryId);
    }

    private void fillTotalLandSegment(Report report, final Long landUseCategoryId) throws Exception {

        // Read in the summary report worksheet
        log.trace("Reading in the summary report worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSummaryReportTableTitle());

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                summaryReportFormattingRules.getSummaryReportTotalLandTitleFormattingRule());

        // Set the column index to 0
        log.trace("Setting the column index to 0");
        int columnIndex = 0;

        // Insert 3 new rows into the worksheet
        log.trace("Inserting 3 new rows into the worksheet");
        Row row1 = sheet.createRow(report.getLastRowIndex() + 1);
        Row row2 = sheet.createRow(report.getLastRowIndex() + 2);
        Row row3 = sheet.createRow(report.getLastRowIndex() + 3);

        // Set the title in the first row
        log.trace("Setting the title in the first row");
        Cell title = row1.createCell(columnIndex);
        if (landUseCategoryId.equals(FOREST_LAND_LU_ID)) {
            title.setCellValue("A. Forest land");
        } else if (landUseCategoryId.equals(CROPLAND_LU_ID)) {
            title.setCellValue("B. Cropland");
        } else if (landUseCategoryId.equals(GRASSLAND_LU_ID)) {
            title.setCellValue("C. Grassland");
        } else if (landUseCategoryId.equals(WETLANDS_LU_ID)) {
            title.setCellValue("D. Wetlands");
        }
        title.setCellStyle(titleCellStyle);

        // Create two additional empty cells under the title cell
        log.trace("Creating two additional empty cells under the title cell");
        Cell cell2 = row2.createCell(columnIndex);
        cell2.setCellStyle(titleCellStyle);

        Cell cell3 = row3.createCell(columnIndex);
        cell3.setCellStyle(titleCellStyle);

        // Merge the 3 cells
        log.trace("Merging the 3 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 3,
                        columnIndex,
                        columnIndex));

        // Fill the Net emissions/removals Data
        log.trace("Filling the Net emissions/removals Data");
        fillTotalLandDataRow(report, landUseCategoryId, NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID, row1);

        // Fill the CH4 Data
        log.trace("Filling the CH4 Data");
        fillTotalLandDataRow(report, landUseCategoryId, METHANE_REPORTING_VARIABLE_ID, row2);

        // Fill the N2O Data
        log.trace("Filling the N2O Data");
        fillTotalLandDataRow(report, landUseCategoryId, NITROUS_OXIDE_REPORTING_VARIABLE_ID, row3);

        // Update the last data row index
        log.trace("Updating the last data row index");
        report.setLastRowIndex(report.getLastRowIndex() + 3);

    }

    private void fillLandRemainingLandSegment(Report report, final Long landUseCategoryId) throws Exception {

        // Get land remaining land subcategory
        log.trace("Getting land remaining land subcategory");
        Long landUseSubcategoryId;

        if (landUseCategoryId.equals(FOREST_LAND_LU_ID)) {
            landUseSubcategoryId = FOREST_LAND_REMAINING_FOREST_LAND_LU_ID;
        } else if (landUseCategoryId.equals(CROPLAND_LU_ID)) {
            landUseSubcategoryId = CROPLAND_REMAINING_CROPLAND_LU_ID;
        } else if (landUseCategoryId.equals(GRASSLAND_LU_ID)) {
            landUseSubcategoryId = GRASSLAND_REMAINING_GRASSLAND_LU_ID;
        } else if (landUseCategoryId.equals(WETLANDS_LU_ID)) {
            landUseSubcategoryId = WETLANDS_REMAINING_WETLANDS_LU_ID;
        } else {
            throw new ServerException("Land Use Category is unsupported");
        }

        // Read in the summary report worksheet
        log.trace("Reading in the summary report worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSummaryReportTableTitle());

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                summaryReportFormattingRules.getSummaryReportLandRemainingLandTitleFormattingRule());

        // Set the column index to 0
        log.trace("Setting the column index to 0");
        int columnIndex = 0;

        // Insert 3 new rows into the worksheet
        log.trace("Inserting 3 new rows into the worksheet");
        Row row1 = sheet.createRow(report.getLastRowIndex() + 1);
        Row row2 = sheet.createRow(report.getLastRowIndex() + 2);
        Row row3 = sheet.createRow(report.getLastRowIndex() + 3);

        // Set the title in the first row
        log.trace("Setting the title in the first row");
        Cell title = row1.createCell(columnIndex);
        if (landUseCategoryId.equals(FOREST_LAND_LU_ID)) {
            title.setCellValue("1. Forest land remaining forest land");
        } else if (landUseCategoryId.equals(CROPLAND_LU_ID)) {
            title.setCellValue("1. Cropland remaining cropland");
        } else if (landUseCategoryId.equals(GRASSLAND_LU_ID)) {
            title.setCellValue("1. Grassland remaining grassland");
        } else if (landUseCategoryId.equals(WETLANDS_LU_ID)) {
            title.setCellValue("D. Wetlands");
        }
        title.setCellStyle(titleCellStyle);

        // Create two additional empty cells under the title cell
        log.trace("Creating two additional empty cells under the title cell");
        Cell cell2 = row2.createCell(columnIndex);
        cell2.setCellStyle(titleCellStyle);

        Cell cell3 = row3.createCell(columnIndex);
        cell3.setCellStyle(titleCellStyle);

        // Merge the 3 cells
        log.trace("Merging the 3 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 3,
                        columnIndex,
                        columnIndex));

        // Fill the Net emissions/removals Data
        log.trace("Filling the Net emissions/removals Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID, row1);

        // Fill the CH4 Data
        log.trace("Filling the CH4 Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, METHANE_REPORTING_VARIABLE_ID, row2);

        // Fill the N2O Data
        log.trace("Filling the N2O Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, NITROUS_OXIDE_REPORTING_VARIABLE_ID, row3);

        // Update the last data row index
        log.trace("Updating the last data row index");
        report.setLastRowIndex(report.getLastRowIndex() + 3);

    }

    private void fillTotalLandConvertedToLandSegment(Report report, final Long landUseCategoryId) throws Exception {

        // Read in the summary report worksheet
        log.trace("Reading in the summary report worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSummaryReportTableTitle());

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                summaryReportFormattingRules
                                        .getSummaryReportTotalLandConvertedToLandTitleFormattingRule());

        // Set the column index to 0
        log.trace("Setting the column index to 0");
        int columnIndex = 0;

        // Insert 3 new rows into the worksheet
        log.trace("Inserting 3 new rows into the worksheet");
        Row row1 = sheet.createRow(report.getLastRowIndex() + 1);
        Row row2 = sheet.createRow(report.getLastRowIndex() + 2);
        Row row3 = sheet.createRow(report.getLastRowIndex() + 3);

        // Set the title in the first row
        log.trace("Setting the title in the first row");
        Cell title = row1.createCell(columnIndex);
        if (landUseCategoryId.equals(FOREST_LAND_LU_ID)) {
            title.setCellValue("2. Land converted to forest land");
        } else if (landUseCategoryId.equals(CROPLAND_LU_ID)) {
            title.setCellValue("2. Land converted to cropland");
        } else if (landUseCategoryId.equals(GRASSLAND_LU_ID)) {
            title.setCellValue("2. Land converted to grassland");
        } else if (landUseCategoryId.equals(WETLANDS_LU_ID)) {
            title.setCellValue("2. Land converted to wetlands");
        }
        title.setCellStyle(titleCellStyle);

        // Create two additional empty cells under the title cell
        log.trace("Creating two additional empty cells under the title cell");
        Cell cell2 = row2.createCell(columnIndex);
        cell2.setCellStyle(titleCellStyle);

        Cell cell3 = row3.createCell(columnIndex);
        cell3.setCellStyle(titleCellStyle);

        // Merge the 3 cells
        log.trace("Merging the 3 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 3,
                        columnIndex,
                        columnIndex));

        // Fill the Net emissions/removals Data
        log.trace("Filling the Net emissions/removals Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID, row1);

        // Fill the CH4 Data
        log.trace("Filling the CH4 Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, METHANE_REPORTING_VARIABLE_ID, row2);

        // Fill the N2O Data
        log.trace("Filling the N2O Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, NITROUS_OXIDE_REPORTING_VARIABLE_ID, row3);

        // Update the last data row index
        log.trace("Updating the last data row index");
        report.setLastRowIndex(report.getLastRowIndex() + 3);

    }

    private void fillTotalLandDataRow(
            Report report,
            final Long landUseCategoryId,
            final Long reportingVariableId,
            final Row row) throws Exception {

        // Initialize the cell styles
        log.trace("Initializing the cell styles");

        ReportingVariableFormattingRule reportingVariableFormattingRule =
                summaryReportFormattingRules
                        .getSummaryReportTotalLandReportingVariableFormattingRule(reportingVariableId);

        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(report.getWorkbook(),
                                reportingVariableFormattingRule
                                        .getReportingVariableTitleFormattingRule());

        CellStyle valueCellStyle =
                cellStyleUtil
                        .getCellStyle(report.getWorkbook(),
                                reportingVariableFormattingRule
                                        .getReportingVariableValueFormattingRule());

        // Set the column index to 1
        log.trace("Setting the column index to 1");

        int columnIndex = 1;

        // Set and style the title
        log.trace("Setting and styling the title");

        Cell title = row.createCell(columnIndex);
        if (reportingVariableId.equals(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID)) {
            title.setCellValue("Net emissions/removals (Kt CO2)");
        } else if (reportingVariableId.equals(METHANE_REPORTING_VARIABLE_ID)) {
            title.setCellValue("CH4 (Kt)");
        } else if (reportingVariableId.equals(NITROUS_OXIDE_REPORTING_VARIABLE_ID)) {
            title.setCellValue("N2O (Kt)");
        }
        title.setCellStyle(titleCellStyle);

        // Increment the column index by 1
        log.trace("Incrementing the column index by 1");

        columnIndex += 1;

        // Fill the emissions/removals data
        log.trace("Filling the emissions/removals data");
        for (int year = report.getMinYear(); year <= report.getMaxYear(); year++) {

            int finalYear = year;
            Cell data = row.createCell(columnIndex);

            try {
                data.setCellValue(
                        report.getQuantityObservations()
                                .stream()
                                .filter(quantityObservation -> quantityObservation.getYear() == finalYear)
                                .filter(quantityObservation -> getLandUsesSubcategoriesIds(landUseCategoryId)
                                        .contains(quantityObservation.getLandUseCategoryId()))
                                .filter(quantityObservation -> quantityObservation.getReportingVariableId()
                                        .equals(reportingVariableId))
                                .map(QuantityObservation::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .doubleValue());
            } catch (Exception e) {
                log.warn("Write attempt for Summary Report, Total Land, Reporting Variable {}, Year {} Data failed", report, year);
                data.setCellValue(BigDecimal.ZERO.doubleValue());
            }

            data.setCellStyle(valueCellStyle);

            columnIndex += 1;
        }

    }

    private void fillLandRemainingLandDataRow(
            Report report,
            final Long landUseCategoryId,
            final Long reportingVariableId,
            final Row row) throws Exception {

        // Initialize the cell styles
        log.trace("Initializing the cell styles");

        ReportingVariableFormattingRule reportingVariableFormattingRule =
                summaryReportFormattingRules
                        .getSummaryReportLandRemainingLandReportingVariableFormattingRule(reportingVariableId);

        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(report.getWorkbook(),
                                reportingVariableFormattingRule
                                        .getReportingVariableTitleFormattingRule());

        CellStyle valueCellStyle =
                cellStyleUtil
                        .getCellStyle(report.getWorkbook(),
                                reportingVariableFormattingRule
                                        .getReportingVariableValueFormattingRule());

        // Set the column index to 1
        log.trace("Setting the column index to 1");

        int columnIndex = 1;

        // Set and style the title
        log.trace("Setting and styling the title");

        Cell title = row.createCell(columnIndex);
        if (reportingVariableId.equals(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID)) {
            title.setCellValue("Net emissions/removals (Kt CO2)");
        } else if (reportingVariableId.equals(METHANE_REPORTING_VARIABLE_ID)) {
            title.setCellValue("CH4 (Kt)");
        } else if (reportingVariableId.equals(NITROUS_OXIDE_REPORTING_VARIABLE_ID)) {
            title.setCellValue("N2O (Kt)");
        }
        title.setCellStyle(titleCellStyle);

        // Increment the column index by 1
        log.trace("Incrementing the column index by 1");

        columnIndex += 1;

        // Fill the emissions/removals data
        log.trace("Filling the emissions/removals data");
        for (int year = report.getMinYear(); year <= report.getMaxYear(); year++) {

            int finalYear = year;
            Cell data = row.createCell(columnIndex);

            try {
                data.setCellValue(
                        Objects.requireNonNull(
                                report.getQuantityObservations()
                                        .stream()
                                        .filter(quantityObservation -> quantityObservation.getYear() == finalYear)
                                        .filter(quantityObservation -> quantityObservation.getLandUseCategoryId()
                                                .equals(landUseCategoryId))
                                        .filter(quantityObservation -> quantityObservation.getReportingVariableId()
                                                .equals(reportingVariableId))
                                        .findAny()
                                        .map(QuantityObservation::getAmount)
                                        .orElse(null))
                                .doubleValue());
            } catch (Exception e) {

                log.warn("Write attempt for Summary Report, Land Remaining Land, " +
                                "Land Use Category {}, Reporting Variable {}, Year {} Data failed",
                        landUseCategoryId, report, year);
                data.setCellValue(BigDecimal.ZERO.doubleValue());
            }

            data.setCellStyle(valueCellStyle);

            columnIndex += 1;
        }

    }

    private void fillTotalLandConvertedToLandDataRow(
            Report report,
            final Long landUseCategoryId,
            final Long reportingVariableId,
            final Row row) throws Exception {

        // Initialize the cell styles
        log.trace("Initializing the cell styles");

        ReportingVariableFormattingRule reportingVariableFormattingRule =
                summaryReportFormattingRules
                        .getSummaryReportTotalLandConvertedToLandReportingVariableFormattingRule(reportingVariableId);

        CellStyle titleCellStyle =
                cellStyleUtil
                        .getCellStyle(report.getWorkbook(),
                                reportingVariableFormattingRule
                                        .getReportingVariableTitleFormattingRule());

        CellStyle valueCellStyle =
                cellStyleUtil
                        .getCellStyle(report.getWorkbook(),
                                reportingVariableFormattingRule
                                        .getReportingVariableValueFormattingRule());

        // Set the column index to 1
        log.trace("Setting the column index to 1");

        int columnIndex = 1;

        // Set and style the title
        log.trace("Setting and styling the title");

        Cell title = row.createCell(columnIndex);
        if (reportingVariableId.equals(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE_ID)) {
            title.setCellValue("Net emissions/removals (Kt CO2)");
        } else if (reportingVariableId.equals(METHANE_REPORTING_VARIABLE_ID)) {
            title.setCellValue("CH4 (Kt)");
        } else if (reportingVariableId.equals(NITROUS_OXIDE_REPORTING_VARIABLE_ID)) {
            title.setCellValue("N2O (Kt)");
        }
        title.setCellStyle(titleCellStyle);

        // Increment the column index by 1
        log.trace("Incrementing the column index by 1");

        columnIndex += 1;

        // Fill the emissions/removals data
        log.trace("Filling the emissions/removals data");
        for (int year = report.getMinYear(); year <= report.getMaxYear(); year++) {

            int finalYear = year;
            Cell data = row.createCell(columnIndex);

            try {
                data.setCellValue(
                        report.getQuantityObservations()
                                .stream()
                                .filter(quantityObservation -> quantityObservation.getYear() == finalYear)
                                .filter(quantityObservation -> getConvertedLandUsesSubcategoriesIds(landUseCategoryId)
                                        .contains(quantityObservation.getLandUseCategoryId()))
                                .filter(quantityObservation -> quantityObservation.getReportingVariableId()
                                        .equals(reportingVariableId))
                                .map(QuantityObservation::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                                .doubleValue());
            } catch (Exception e) {

                log.warn("Write attempt for Summary Report, Total Land Converted To Land, " +
                                "Land Use Category {}, Reporting Variable {}, Year {} Data failed",
                        landUseCategoryId, report, year);
                data.setCellValue(BigDecimal.ZERO.doubleValue());
            }

            data.setCellStyle(valueCellStyle);

            columnIndex += 1;
        }

    }



    private void autoSizeSheetColumns(Report report) throws Exception {

        // Read in the summary report worksheet
        log.trace("Reading in the summary report worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSummaryReportTableTitle());

        // Auto Sizing the Summary Report Sheet Columns
        log.trace("Auto Sizing the Summary Report Sheet Columns");
        int columnIndex = 2;
        for (int year = report.getMinYear(); year <= report.getMaxYear(); year++) {

            sheet.autoSizeColumn(columnIndex);

            columnIndex += 1;
        }


    }
    

    private List<Long> getLandUsesSubcategoriesIds(Long landUseCategoryId) {

        if (landUseCategoryId.equals(FOREST_LAND_LU_ID)) {
            return Arrays.asList(
                    FOREST_LAND_REMAINING_FOREST_LAND_LU_ID,
                    CROPLAND_CONVERTED_TO_FOREST_LAND_LU_ID,
                    GRASSLAND_CONVERTED_TO_FOREST_LAND_LU_ID,
                    WETLANDS_CONVERTED_TO_FOREST_LAND_LU_ID,
                    SETTLEMENTS_CONVERTED_TO_FOREST_LAND_LU_ID,
                    OTHER_LAND_CONVERTED_TO_FOREST_LAND_LU_ID
            );
        } else if (landUseCategoryId.equals(CROPLAND_LU_ID)) {
            return Arrays.asList(
                    CROPLAND_REMAINING_CROPLAND_LU_ID,
                    FOREST_LAND_CONVERTED_TO_CROPLAND_LU_ID,
                    GRASSLAND_CONVERTED_TO_CROPLAND_LU_ID,
                    WETLANDS_CONVERTED_TO_CROPLAND_LU_ID,
                    SETTLEMENTS_CONVERTED_TO_CROPLAND_LU_ID,
                    OTHER_LAND_CONVERTED_TO_CROPLAND_LU_ID
            );
        } else if (landUseCategoryId.equals(GRASSLAND_LU_ID)) {
            return Arrays.asList(
                    GRASSLAND_REMAINING_GRASSLAND_LU_ID,
                    CROPLAND_CONVERTED_TO_GRASSLAND_LU_ID,
                    FOREST_LAND_CONVERTED_TO_GRASSLAND_LU_ID,
                    WETLANDS_CONVERTED_TO_GRASSLAND_LU_ID,
                    SETTLEMENTS_CONVERTED_TO_GRASSLAND_LU_ID,
                    OTHER_LAND_CONVERTED_TO_GRASSLAND_LU_ID
            );
        } else if (landUseCategoryId.equals(WETLANDS_LU_ID)) {
            return Arrays.asList(
                    WETLANDS_REMAINING_WETLANDS_LU_ID,
                    CROPLAND_CONVERTED_TO_WETLANDS_LU_ID,
                    GRASSLAND_CONVERTED_TO_WETLANDS_LU_ID,
                    FOREST_LAND_CONVERTED_TO_WETLANDS_LU_ID,
                    SETTLEMENTS_CONVERTED_TO_WETLANDS_LU_ID,
                    OTHER_LAND_CONVERTED_TO_WETLANDS_LU_ID
            );
        } else {
            return new ArrayList<>();
        }
    }

    private List<Long> getConvertedLandUsesSubcategoriesIds(Long landUseCategoryId) {

        if (landUseCategoryId.equals(FOREST_LAND_LU_ID)) {
            return Arrays.asList(
                    CROPLAND_CONVERTED_TO_FOREST_LAND_LU_ID,
                    GRASSLAND_CONVERTED_TO_FOREST_LAND_LU_ID,
                    WETLANDS_CONVERTED_TO_FOREST_LAND_LU_ID,
                    SETTLEMENTS_CONVERTED_TO_FOREST_LAND_LU_ID,
                    OTHER_LAND_CONVERTED_TO_FOREST_LAND_LU_ID
            );
        } else if (landUseCategoryId.equals(CROPLAND_LU_ID)) {
            return Arrays.asList(
                    FOREST_LAND_CONVERTED_TO_CROPLAND_LU_ID,
                    GRASSLAND_CONVERTED_TO_CROPLAND_LU_ID,
                    WETLANDS_CONVERTED_TO_CROPLAND_LU_ID,
                    SETTLEMENTS_CONVERTED_TO_CROPLAND_LU_ID,
                    OTHER_LAND_CONVERTED_TO_CROPLAND_LU_ID
            );
        } else if (landUseCategoryId.equals(GRASSLAND_LU_ID)) {
            return Arrays.asList(
                    CROPLAND_CONVERTED_TO_GRASSLAND_LU_ID,
                    FOREST_LAND_CONVERTED_TO_GRASSLAND_LU_ID,
                    WETLANDS_CONVERTED_TO_GRASSLAND_LU_ID,
                    SETTLEMENTS_CONVERTED_TO_GRASSLAND_LU_ID,
                    OTHER_LAND_CONVERTED_TO_GRASSLAND_LU_ID
            );
        } else if (landUseCategoryId.equals(WETLANDS_LU_ID)) {
            return Arrays.asList(
                    CROPLAND_CONVERTED_TO_WETLANDS_LU_ID,
                    GRASSLAND_CONVERTED_TO_WETLANDS_LU_ID,
                    FOREST_LAND_CONVERTED_TO_WETLANDS_LU_ID,
                    SETTLEMENTS_CONVERTED_TO_WETLANDS_LU_ID,
                    OTHER_LAND_CONVERTED_TO_WETLANDS_LU_ID
            );
        } else {
            return new ArrayList<>();
        }
    }


    private String getSummaryReportTableTitle() {
        return "Table 4";
    }
}
