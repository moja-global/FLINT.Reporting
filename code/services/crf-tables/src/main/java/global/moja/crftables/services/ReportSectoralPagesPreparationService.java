/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http:// mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.services;

import global.moja.crftables.daos.Report;
import global.moja.crftables.daos.ReportingVariableFormattingRule;
import global.moja.crftables.exceptions.ServerException;
import global.moja.crftables.models.QuantityObservation;
import global.moja.crftables.util.styling.CellStyleUtil;
import global.moja.crftables.util.styling.rules.SectoralReportFormattingRules;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
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
public class ReportSectoralPagesPreparationService {

    @Value("${sectoral.report.template.index}")
    private Integer SECTORAL_REPORT_TEMPLATE_INDEX;

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

    @Value("${area.reporting.variable.id}")
    private Long AREA_REPORTING_VARIABLE_ID;

    @Value("${net.carbon.stock.change.in.living.biomass.reporting.variable.id}")
    private Long NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.dead.organic.matter.reporting.variable.id}")
    private Long NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.mineral.soils.reporting.variable.id}")
    private Long NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.organic.soils.reporting.variable.id}")
    private Long NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE;

    @Value("${net.carbon.dioxide.emissions.removals.reporting.variable.id}")
    private Long NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE;

    @Autowired
    private SectoralReportFormattingRules sectoralReportFormattingRules;

    @Autowired
    private CellStyleUtil cellStyleUtil;

    public Mono<Report> prepareSectoralReports(Report report) {

        log.trace("Entering prepareSectoralReports()");

        // Fill in the report data
        log.trace("Filling in the report data");
        try {
            for (Long landUseCategoryId : new Long[]{FOREST_LAND_LU_ID, CROPLAND_LU_ID, GRASSLAND_LU_ID, WETLANDS_LU_ID}) {
                if (report.getQuantityObservations()
                        .stream()
                        .anyMatch(quantityObservation ->
                                getLandUsesSubcategoriesIds(landUseCategoryId)
                                        .contains(quantityObservation.getLandUseCategoryId()))) {

                    // Create the sectoral report worksheet from the template sectoral report
                    log.trace("Creating the sectoral report worksheet from the template sectoral report");
                    Sheet sheet = report.getWorkbook().cloneSheet(SECTORAL_REPORT_TEMPLATE_INDEX);

                    // Name the sectoral report appropriately
                    log.trace("Naming the sectoral report appropriately");
                    sheet.getWorkbook()
                            .setSheetName(
                                    sheet.getWorkbook().getSheetIndex(sheet),
                                    getSectoralReportTableTitle(landUseCategoryId));


                    // (Re) Initialize the last row index
                    // The last row index is a 0 based index of the last row in the table.
                    // New data should be inserted from last row + 1 to avoid overwriting previously written data
                    log.trace("(Re) Initializing the last row index");
                    report.setLastRowIndex(1);

                    // Fill the sectoral report
                    log.trace("Filling the sectoral report");
                    fillSectoralReport(report, landUseCategoryId);

                    // Auto-size sheet columns
                    autoSizeSheetColumns(report, landUseCategoryId);
                }
            }
        } catch (Exception e) {
            return Mono.error(new ServerException("Report Generation Failed", e));
        }

        // Return the updated Report Details
        log.trace("Return the updated Report Details");
        return Mono.just(report);

    }

    private void fillSectoralReport(Report report, final Long landUseCategoryId) throws Exception {

        log.trace("Filling in Land Use Category Segment");
        fillYearsSegment(report, landUseCategoryId);
        fillTotalLandSegment(report, landUseCategoryId);
        fillLandRemainingLandSegment(report, landUseCategoryId);
        fillTotalLandConvertedToLandSegment(report, landUseCategoryId);
        fillLandConvertedToLandSegment(report, landUseCategoryId);
    }

    private void fillYearsSegment(Report report, final Long landUseCategoryId) throws Exception {

        log.trace("Filling in the Years Segment");

        // Read in the sectoral worksheet
        log.trace("Reading in the sectoral worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSectoralReportTableTitle(landUseCategoryId));

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle valueCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules.getSectoralReportYearValueFormattingRule());


        //Set and style the year header rows
        log.trace("Setting and styling the year header rows");

        int columnIndex = 3;
        Row yearRow = sheet.getRow(0);
        Row emptyRow = sheet.getRow(1);
        for (int year = report.getMinYear(); year <= report.getMaxYear(); year++) {

            Cell cell = yearRow.createCell(columnIndex);
            cell.setCellValue(year);
            cell.setCellStyle(valueCellStyle);

            //Add an empty cell below the year cell
            Cell empty = emptyRow.createCell(columnIndex);
            empty.setCellStyle(valueCellStyle);

            //Merge the two cells
            sheet.addMergedRegion(
                    new CellRangeAddress(0, 1, columnIndex, columnIndex));

            columnIndex += 1;
        }

    }

    private void fillTotalLandSegment(Report report, final Long landUseCategoryId) throws Exception {

        // Read in the sectoral worksheet
        log.trace("Reading in the sectoral worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSectoralReportTableTitle(landUseCategoryId));

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle categoryTitleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules
                                        .getSectoralReportTotalLandCategoryTitleFormattingRule());

        CellStyle subdivisionTitleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules
                                        .getSectoralReportTotalLandSubdivisionTitleFormattingRule());

        // Set the column index to 0
        log.trace("Setting the column index to 0");
        int columnIndex = 0;

        // Insert 3 new rows into the worksheet
        log.trace("Inserting 3 new rows into the worksheet");
        Row row1 = sheet.createRow(report.getLastRowIndex() + 1);
        Row row2 = sheet.createRow(report.getLastRowIndex() + 2);
        Row row3 = sheet.createRow(report.getLastRowIndex() + 3);
        Row row4 = sheet.createRow(report.getLastRowIndex() + 4);
        Row row5 = sheet.createRow(report.getLastRowIndex() + 5);
        Row row6 = sheet.createRow(report.getLastRowIndex() + 6);

        // Set the title in the first row
        log.trace("Setting the title in the first row");
        Cell title = row1.createCell(columnIndex);
        title.setCellValue(getSectoralReportCategoryTitle(landUseCategoryId));
        title.setCellStyle(categoryTitleCellStyle);

        // Create five additional empty cells under the title cell
        log.trace("Creating five additional empty cells under the title cell");
        Cell cell2 = row2.createCell(columnIndex);
        cell2.setCellStyle(categoryTitleCellStyle);

        Cell cell3 = row3.createCell(columnIndex);
        cell3.setCellStyle(categoryTitleCellStyle);

        Cell cell4 = row4.createCell(columnIndex);
        cell4.setCellStyle(categoryTitleCellStyle);

        Cell cell5 = row5.createCell(columnIndex);
        cell5.setCellStyle(categoryTitleCellStyle);

        Cell cell6 = row6.createCell(columnIndex);
        cell6.setCellStyle(categoryTitleCellStyle);

        // Merge the 6 cells
        log.trace("Merging the 6 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 6,
                        columnIndex,
                        columnIndex));


        // Set the column index to 1
        log.trace("Setting the column index to 1");
        columnIndex = 1;

        // Create six empty cells to serve as placeholders for subdivision data
        log.trace("Creating six empty cells to serve as placeholders for subdivision data");

        Cell cell7 = row1.createCell(columnIndex);
        cell7.setCellStyle(subdivisionTitleCellStyle);

        Cell cell8 = row2.createCell(columnIndex);
        cell8.setCellStyle(subdivisionTitleCellStyle);

        Cell cell9 = row3.createCell(columnIndex);
        cell9.setCellStyle(subdivisionTitleCellStyle);

        Cell cell10 = row4.createCell(columnIndex);
        cell10.setCellStyle(subdivisionTitleCellStyle);

        Cell cell11 = row5.createCell(columnIndex);
        cell11.setCellStyle(subdivisionTitleCellStyle);

        Cell cell12 = row6.createCell(columnIndex);
        cell12.setCellStyle(subdivisionTitleCellStyle);

        // Merge the 6 cells
        log.trace("Merging the 6 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 6,
                        columnIndex,
                        columnIndex));

        // Fill the Area (kha) Data
        log.trace("Filling the Area (kha)Data");
        fillTotalLandDataRow(report, landUseCategoryId, AREA_REPORTING_VARIABLE_ID, row1);

        // Fill the Net carbon stock change in living biomass (kt C) Data
        log.trace("Filling the Net carbon stock change in living biomass (kt C) Data");
        fillTotalLandDataRow(report, landUseCategoryId, NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE, row2);

        // Fill the Net carbon stock change in dead organic matter (kt C) Data
        log.trace("Filling the Net carbon stock change in dead organic matter (kt C) Data");
        fillTotalLandDataRow(report, landUseCategoryId, NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE, row3);

        // Fill the Net carbon stock change in mineral soils (kt C) Data
        log.trace("Filling the Net carbon stock change in mineral soils (kt C) Data");
        fillTotalLandDataRow(report, landUseCategoryId, NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE, row4);

        // Fill the Net carbon stock change in organic soils (kt C) Data
        log.trace("Filling the Net carbon stock change in organic soils (kt C) Data");
        fillTotalLandDataRow(report, landUseCategoryId, NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE, row5);

        // Fill the Net emissions/removals (kt CO2) Data
        log.trace("Filling the Net emissions/removals (kt CO2) Data");
        fillTotalLandDataRow(report, landUseCategoryId, NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE, row6);

        // Update the last data row index
        log.trace("Updating the last data row index");
        report.setLastRowIndex(report.getLastRowIndex() + 6);

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

        // Read in the sectoral worksheet
        log.trace("Reading in the sectoral worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSectoralReportTableTitle(landUseCategoryId));

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle categoryTitleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules
                                        .getSectoralReportLandRemainingLandCategoryTitleFormattingRule());

        CellStyle subdivisionTitleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules
                                        .getSectoralReportLandRemainingLandSubdivisionTitleFormattingRule());


        // Set the column index to 0
        log.trace("Setting the column index to 0");
        int columnIndex = 0;

        // Insert 3 new rows into the worksheet
        log.trace("Inserting 3 new rows into the worksheet");
        Row row1 = sheet.createRow(report.getLastRowIndex() + 1);
        Row row2 = sheet.createRow(report.getLastRowIndex() + 2);
        Row row3 = sheet.createRow(report.getLastRowIndex() + 3);
        Row row4 = sheet.createRow(report.getLastRowIndex() + 4);
        Row row5 = sheet.createRow(report.getLastRowIndex() + 5);
        Row row6 = sheet.createRow(report.getLastRowIndex() + 6);

        // Set the title in the first row
        log.trace("Setting the title in the first row");
        Cell title = row1.createCell(columnIndex);
        title.setCellValue(getSectoralReportCategoryTitle(landUseSubcategoryId));
        title.setCellStyle(categoryTitleCellStyle);

        // Create five additional empty cells under the title cell
        log.trace("Creating five additional empty cells under the title cell");
        Cell cell2 = row2.createCell(columnIndex);
        cell2.setCellStyle(categoryTitleCellStyle);

        Cell cell3 = row3.createCell(columnIndex);
        cell3.setCellStyle(categoryTitleCellStyle);

        Cell cell4 = row4.createCell(columnIndex);
        cell4.setCellStyle(categoryTitleCellStyle);

        Cell cell5 = row5.createCell(columnIndex);
        cell5.setCellStyle(categoryTitleCellStyle);

        Cell cell6 = row6.createCell(columnIndex);
        cell6.setCellStyle(categoryTitleCellStyle);

        // Merge the 6 cells
        log.trace("Merging the 6 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 6,
                        columnIndex,
                        columnIndex));


        // Set the column index to 1
        log.trace("Setting the column index to 1");
        columnIndex = 1;

        // Create six empty cells to serve as placeholders for subdivision data
        log.trace("Creating six empty cells to serve as placeholders for subdivision data");

        Cell cell7 = row1.createCell(columnIndex);
        cell7.setCellStyle(subdivisionTitleCellStyle);

        Cell cell8 = row2.createCell(columnIndex);
        cell8.setCellStyle(subdivisionTitleCellStyle);

        Cell cell9 = row3.createCell(columnIndex);
        cell9.setCellStyle(subdivisionTitleCellStyle);

        Cell cell10 = row4.createCell(columnIndex);
        cell10.setCellStyle(subdivisionTitleCellStyle);

        Cell cell11 = row5.createCell(columnIndex);
        cell11.setCellStyle(subdivisionTitleCellStyle);

        Cell cell12 = row6.createCell(columnIndex);
        cell12.setCellStyle(subdivisionTitleCellStyle);

        // Merge the 6 cells
        log.trace("Merging the 6 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 6,
                        columnIndex,
                        columnIndex));


        // Fill the Area (kha) Data
        log.trace("Filling the Area (kha)Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, AREA_REPORTING_VARIABLE_ID, row1);

        // Fill the Net carbon stock change in living biomass (kt C) Data
        log.trace("Filling the Net carbon stock change in living biomass (kt C) Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE, row2);

        // Fill the Net carbon stock change in dead organic matter (kt C) Data
        log.trace("Filling the Net carbon stock change in dead organic matter (kt C) Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE, row3);

        // Fill the Net carbon stock change in mineral soils (kt C) Data
        log.trace("Filling the Net carbon stock change in mineral soils (kt C) Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE, row4);

        // Fill the Net carbon stock change in organic soils (kt C) Data
        log.trace("Filling the Net carbon stock change in organic soils (kt C) Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE, row5);

        // Fill the Net emissions/removals (kt CO2) Data
        log.trace("Filling the Net emissions/removals (kt CO2) Data");
        fillLandRemainingLandDataRow(report, landUseSubcategoryId, NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE, row6);

        // Update the last data row index
        log.trace("Updating the last data row index");
        report.setLastRowIndex(report.getLastRowIndex() + 6);

    }

    private void fillTotalLandConvertedToLandSegment(Report report, final Long landUseCategoryId) throws Exception {

        // Get land remaining land subcategory
        log.trace("Getting land remaining land subcategory");
        Long landUseSubcategoryId;

        if (landUseCategoryId.equals(FOREST_LAND_LU_ID)) {
            landUseSubcategoryId = LAND_CONVERTED_TO_FOREST_LAND_LU_ID;
        } else if (landUseCategoryId.equals(CROPLAND_LU_ID)) {
            landUseSubcategoryId = LAND_CONVERTED_TO_CROPLAND_LU_ID;
        } else if (landUseCategoryId.equals(GRASSLAND_LU_ID)) {
            landUseSubcategoryId = LAND_CONVERTED_TO_GRASSLAND_LU_ID;
        } else if (landUseCategoryId.equals(WETLANDS_LU_ID)) {
            landUseSubcategoryId = LAND_CONVERTED_TO_WETLANDS_LU_ID;
        } else {
            throw new ServerException("Land Use Category is unsupported");
        }

        // Read in the sectoral worksheet
        log.trace("Reading in the sectoral worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSectoralReportTableTitle(landUseCategoryId));

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle categoryTitleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules
                                        .getSectoralReportLandConvertedToLandCategoryTitleFormattingRule());

        CellStyle subdivisionTitleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules
                                        .getSectoralReportLandConvertedToLandSubdivisionTitleFormattingRule());


        // Set the column index to 0
        log.trace("Setting the column index to 0");
        int columnIndex = 0;

        // Insert 3 new rows into the worksheet
        log.trace("Inserting 3 new rows into the worksheet");
        Row row1 = sheet.createRow(report.getLastRowIndex() + 1);
        Row row2 = sheet.createRow(report.getLastRowIndex() + 2);
        Row row3 = sheet.createRow(report.getLastRowIndex() + 3);
        Row row4 = sheet.createRow(report.getLastRowIndex() + 4);
        Row row5 = sheet.createRow(report.getLastRowIndex() + 5);
        Row row6 = sheet.createRow(report.getLastRowIndex() + 6);

        // Set the title in the first row
        log.trace("Setting the title in the first row");
        Cell title = row1.createCell(columnIndex);
        title.setCellValue(getSectoralReportCategoryTitle(landUseSubcategoryId));
        title.setCellStyle(categoryTitleCellStyle);

        // Create five additional empty cells under the title cell
        log.trace("Creating five additional empty cells under the title cell");
        Cell cell2 = row2.createCell(columnIndex);
        cell2.setCellStyle(categoryTitleCellStyle);

        Cell cell3 = row3.createCell(columnIndex);
        cell3.setCellStyle(categoryTitleCellStyle);

        Cell cell4 = row4.createCell(columnIndex);
        cell4.setCellStyle(categoryTitleCellStyle);

        Cell cell5 = row5.createCell(columnIndex);
        cell5.setCellStyle(categoryTitleCellStyle);

        Cell cell6 = row6.createCell(columnIndex);
        cell6.setCellStyle(categoryTitleCellStyle);

        // Merge the 6 cells
        log.trace("Merging the 6 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 6,
                        columnIndex,
                        columnIndex));


        // Set the column index to 1
        log.trace("Setting the column index to 1");
        columnIndex = 1;

        // Create six empty cells to serve as placeholders for subdivision data
        log.trace("Creating six empty cells to serve as placeholders for subdivision data");

        Cell cell7 = row1.createCell(columnIndex);
        cell7.setCellStyle(subdivisionTitleCellStyle);

        Cell cell8 = row2.createCell(columnIndex);
        cell8.setCellStyle(subdivisionTitleCellStyle);

        Cell cell9 = row3.createCell(columnIndex);
        cell9.setCellStyle(subdivisionTitleCellStyle);

        Cell cell10 = row4.createCell(columnIndex);
        cell10.setCellStyle(subdivisionTitleCellStyle);

        Cell cell11 = row5.createCell(columnIndex);
        cell11.setCellStyle(subdivisionTitleCellStyle);

        Cell cell12 = row6.createCell(columnIndex);
        cell12.setCellStyle(subdivisionTitleCellStyle);

        // Merge the 6 cells
        log.trace("Merging the 6 cells");
        sheet.addMergedRegion(
                new CellRangeAddress(
                        report.getLastRowIndex() + 1,
                        report.getLastRowIndex() + 6,
                        columnIndex,
                        columnIndex));


        // Fill the Area (kha) Data
        log.trace("Filling the Area (kha)Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, AREA_REPORTING_VARIABLE_ID, row1);

        // Fill the Net carbon stock change in living biomass (kt C) Data
        log.trace("Filling the Net carbon stock change in living biomass (kt C) Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE, row2);

        // Fill the Net carbon stock change in dead organic matter (kt C) Data
        log.trace("Filling the Net carbon stock change in dead organic matter (kt C) Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE, row3);

        // Fill the Net carbon stock change in mineral soils (kt C) Data
        log.trace("Filling the Net carbon stock change in mineral soils (kt C) Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE, row4);

        // Fill the Net carbon stock change in organic soils (kt C) Data
        log.trace("Filling the Net carbon stock change in organic soils (kt C) Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE, row5);

        // Fill the Net emissions/removals (kt CO2) Data
        log.trace("Filling the Net emissions/removals (kt CO2) Data");
        fillTotalLandConvertedToLandDataRow(report, landUseCategoryId, NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE, row6);

        // Update the last data row index
        log.trace("Updating the last data row index");
        report.setLastRowIndex(report.getLastRowIndex() + 6);

    }

    private void fillLandConvertedToLandSegment(Report report, final Long landUseCategoryId) throws Exception {

        // Get land converted to land subcategories
        log.trace("Getting land converted to land subcategories");
        List<Long> convertedLandUsesSubcategoriesIds = getConvertedLandUsesSubcategoriesIds(landUseCategoryId);
        if (convertedLandUsesSubcategoriesIds.isEmpty()) {
            throw new ServerException("Land Use Category is unsupported");
        }

        // Read in the sectoral worksheet
        log.trace("Reading in the sectoral worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSectoralReportTableTitle(landUseCategoryId));

        // Initialize the cell styles
        log.trace("Initializing the cell styles");
        CellStyle categoryTitleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules
                                        .getSectoralReportLandConvertedToLandCategoryTitleFormattingRule());

        CellStyle subdivisionTitleCellStyle =
                cellStyleUtil
                        .getCellStyle(
                                report.getWorkbook(),
                                sectoralReportFormattingRules
                                        .getSectoralReportLandConvertedToLandSubdivisionTitleFormattingRule());


        for (Long landUseSubcategoryId : convertedLandUsesSubcategoriesIds) {

            // Set the column index to 0
            log.trace("Setting the column index to 0");
            int columnIndex = 0;

            // Insert 3 new rows into the worksheet
            log.trace("Inserting 3 new rows into the worksheet");
            Row row1 = sheet.createRow(report.getLastRowIndex() + 1);
            Row row2 = sheet.createRow(report.getLastRowIndex() + 2);
            Row row3 = sheet.createRow(report.getLastRowIndex() + 3);
            Row row4 = sheet.createRow(report.getLastRowIndex() + 4);
            Row row5 = sheet.createRow(report.getLastRowIndex() + 5);
            Row row6 = sheet.createRow(report.getLastRowIndex() + 6);

            // Set the title in the first row
            log.trace("Setting the title in the first row");
            Cell title = row1.createCell(columnIndex);
            title.setCellValue(getSectoralReportCategoryTitle(landUseSubcategoryId));
            title.setCellStyle(categoryTitleCellStyle);

            // Create five additional empty cells under the title cell
            log.trace("Creating five additional empty cells under the title cell");
            Cell cell2 = row2.createCell(columnIndex);
            cell2.setCellStyle(categoryTitleCellStyle);

            Cell cell3 = row3.createCell(columnIndex);
            cell3.setCellStyle(categoryTitleCellStyle);

            Cell cell4 = row4.createCell(columnIndex);
            cell4.setCellStyle(categoryTitleCellStyle);

            Cell cell5 = row5.createCell(columnIndex);
            cell5.setCellStyle(categoryTitleCellStyle);

            Cell cell6 = row6.createCell(columnIndex);
            cell6.setCellStyle(categoryTitleCellStyle);

            // Merge the 6 cells
            log.trace("Merging the 6 cells");
            sheet.addMergedRegion(
                    new CellRangeAddress(
                            report.getLastRowIndex() + 1,
                            report.getLastRowIndex() + 6,
                            columnIndex,
                            columnIndex));


            // Set the column index to 1
            log.trace("Setting the column index to 1");
            columnIndex = 1;

            // Create six empty cells to serve as placeholders for subdivision data
            log.trace("Creating six empty cells to serve as placeholders for subdivision data");

            Cell cell7 = row1.createCell(columnIndex);
            cell7.setCellStyle(subdivisionTitleCellStyle);

            Cell cell8 = row2.createCell(columnIndex);
            cell8.setCellStyle(subdivisionTitleCellStyle);

            Cell cell9 = row3.createCell(columnIndex);
            cell9.setCellStyle(subdivisionTitleCellStyle);

            Cell cell10 = row4.createCell(columnIndex);
            cell10.setCellStyle(subdivisionTitleCellStyle);

            Cell cell11 = row5.createCell(columnIndex);
            cell11.setCellStyle(subdivisionTitleCellStyle);

            Cell cell12 = row6.createCell(columnIndex);
            cell12.setCellStyle(subdivisionTitleCellStyle);

            // Merge the 6 cells
            log.trace("Merging the 6 cells");
            sheet.addMergedRegion(
                    new CellRangeAddress(
                            report.getLastRowIndex() + 1,
                            report.getLastRowIndex() + 6,
                            columnIndex,
                            columnIndex));


            // Fill the Area (kha) Data
            log.trace("Filling the Area (kha)Data");
            fillLandConvertedToLandDataRow(report, landUseSubcategoryId, AREA_REPORTING_VARIABLE_ID, row1);

            // Fill the Net carbon stock change in living biomass (kt C) Data
            log.trace("Filling the Net carbon stock change in living biomass (kt C) Data");
            fillLandConvertedToLandDataRow(report, landUseSubcategoryId, NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE, row2);

            // Fill the Net carbon stock change in dead organic matter (kt C) Data
            log.trace("Filling the Net carbon stock change in dead organic matter (kt C) Data");
            fillLandConvertedToLandDataRow(report, landUseSubcategoryId, NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE, row3);

            // Fill the Net carbon stock change in mineral soils (kt C) Data
            log.trace("Filling the Net carbon stock change in mineral soils (kt C) Data");
            fillLandConvertedToLandDataRow(report, landUseSubcategoryId, NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE, row4);

            // Fill the Net carbon stock change in organic soils (kt C) Data
            log.trace("Filling the Net carbon stock change in organic soils (kt C) Data");
            fillLandConvertedToLandDataRow(report, landUseSubcategoryId, NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE, row5);

            // Fill the Net emissions/removals (kt CO2) Data
            log.trace("Filling the Net emissions/removals (kt CO2) Data");
            fillLandConvertedToLandDataRow(report, landUseSubcategoryId, NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE, row6);

            // Update the last data row index
            log.trace("Updating the last data row index");
            report.setLastRowIndex(report.getLastRowIndex() + 6);

        }

    }

    private void fillTotalLandDataRow(
            Report report,
            final Long landUseCategoryId,
            final Long reportingVariableId,
            final Row row) throws Exception {

        // Initialize the cell styles
        log.trace("Initializing the cell styles");

        ReportingVariableFormattingRule reportingVariableFormattingRule =
                sectoralReportFormattingRules
                        .getSectoralReportTotalLandReportingVariableFormattingRule(reportingVariableId);

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

        int columnIndex = 2;

        // Set and style the title
        log.trace("Setting and styling the title");

        Cell title = row.createCell(columnIndex);
        title.setCellValue(getSectoralReportReportingVariableTitle(reportingVariableId));
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
                log.warn("Write attempt for Sectoral Report, Total Land, Reporting Variable {}, " +
                        "Year {} Data failed", report, year);
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
                sectoralReportFormattingRules
                        .getSectoralReportLandRemainingLandReportingVariableFormattingRule(reportingVariableId);

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

        int columnIndex = 2;

        // Set and style the title
        log.trace("Setting and styling the title");

        Cell title = row.createCell(columnIndex);
        title.setCellValue(getSectoralReportReportingVariableTitle(reportingVariableId));
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

                log.warn("Write attempt for Sectoral Report, Land Remaining Land, " +
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
                sectoralReportFormattingRules
                        .getSectoralReportTotalLandConvertedToLandReportingVariableFormattingRule(reportingVariableId);

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

        int columnIndex = 2;

        // Set and style the title
        log.trace("Setting and styling the title");

        Cell title = row.createCell(columnIndex);
        title.setCellValue(getSectoralReportReportingVariableTitle(reportingVariableId));
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

                log.warn("Write attempt for Sectoral Report, Total Land Converted To Land, " +
                                "Land Use Category {}, Reporting Variable {}, Year {} Data failed",
                        landUseCategoryId, report, year);
                data.setCellValue(BigDecimal.ZERO.doubleValue());
            }

            data.setCellStyle(valueCellStyle);

            columnIndex += 1;
        }

    }

    private void fillLandConvertedToLandDataRow(
            Report report,
            final Long landUseCategoryId,
            final Long reportingVariableId,
            final Row row) throws Exception {

        // Initialize the cell styles
        log.trace("Initializing the cell styles");

        ReportingVariableFormattingRule reportingVariableFormattingRule =
                sectoralReportFormattingRules
                        .getSectoralReportLandConvertedToLandReportingVariableFormattingRule(reportingVariableId);

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

        int columnIndex = 2;

        // Set and style the title
        log.trace("Setting and styling the title");

        Cell title = row.createCell(columnIndex);
        title.setCellValue(getSectoralReportReportingVariableTitle(reportingVariableId));
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

                log.warn("Write attempt for Sectoral Report, Land Remaining Land, " +
                                "Land Use Category {}, Reporting Variable {}, Year {} Data failed",
                        landUseCategoryId, report, year);
                data.setCellValue(BigDecimal.ZERO.doubleValue());
            }

            data.setCellStyle(valueCellStyle);

            columnIndex += 1;
        }

    }

    private void autoSizeSheetColumns(Report report, Long landUseCategoryId) throws IOException {

        // Read in the summary report worksheet
        log.trace("Reading in the summary report worksheet");
        Sheet sheet = report.getWorkbook().getSheet(getSectoralReportTableTitle(landUseCategoryId));

        // Auto Sizing the Summary Report Sheet Columns
        log.trace("Auto Sizing the Summary Report Sheet Columns");
        int columnIndex = 3;
        for (int year = report.getMinYear(); year <= report.getMaxYear(); year++) {

            sheet.autoSizeColumn(columnIndex);

            columnIndex += 1;
        }


    }


    private String getSectoralReportTableTitle(Long landUseCategoryId) {

        if (Arrays.asList(
                FOREST_LAND_LU_ID,
                FOREST_LAND_REMAINING_FOREST_LAND_LU_ID,
                LAND_CONVERTED_TO_FOREST_LAND_LU_ID,
                CROPLAND_CONVERTED_TO_FOREST_LAND_LU_ID,
                GRASSLAND_CONVERTED_TO_FOREST_LAND_LU_ID,
                WETLANDS_CONVERTED_TO_FOREST_LAND_LU_ID,
                SETTLEMENTS_CONVERTED_TO_FOREST_LAND_LU_ID,
                OTHER_LAND_CONVERTED_TO_FOREST_LAND_LU_ID)
                .contains(landUseCategoryId)) {

            return "Table 4.A";

        } else if (Arrays.asList(
                CROPLAND_LU_ID,
                CROPLAND_REMAINING_CROPLAND_LU_ID,
                LAND_CONVERTED_TO_CROPLAND_LU_ID,
                FOREST_LAND_CONVERTED_TO_CROPLAND_LU_ID,
                GRASSLAND_CONVERTED_TO_CROPLAND_LU_ID,
                WETLANDS_CONVERTED_TO_CROPLAND_LU_ID,
                SETTLEMENTS_CONVERTED_TO_CROPLAND_LU_ID,
                OTHER_LAND_CONVERTED_TO_CROPLAND_LU_ID)
                .contains(landUseCategoryId)) {

            return "Table 4.B";

        } else if (Arrays.asList(
                GRASSLAND_LU_ID,
                GRASSLAND_REMAINING_GRASSLAND_LU_ID,
                LAND_CONVERTED_TO_GRASSLAND_LU_ID,
                CROPLAND_CONVERTED_TO_GRASSLAND_LU_ID,
                FOREST_LAND_CONVERTED_TO_GRASSLAND_LU_ID,
                WETLANDS_CONVERTED_TO_GRASSLAND_LU_ID,
                SETTLEMENTS_CONVERTED_TO_GRASSLAND_LU_ID,
                OTHER_LAND_CONVERTED_TO_GRASSLAND_LU_ID)
                .contains(landUseCategoryId)) {

            return "Table 4.C";

        } else if (Arrays.asList(
                WETLANDS_LU_ID,
                WETLANDS_REMAINING_WETLANDS_LU_ID,
                LAND_CONVERTED_TO_WETLANDS_LU_ID,
                CROPLAND_CONVERTED_TO_WETLANDS_LU_ID,
                GRASSLAND_CONVERTED_TO_WETLANDS_LU_ID,
                FOREST_LAND_CONVERTED_TO_WETLANDS_LU_ID,
                SETTLEMENTS_CONVERTED_TO_WETLANDS_LU_ID,
                OTHER_LAND_CONVERTED_TO_WETLANDS_LU_ID)
                .contains(landUseCategoryId)) {

            return "Table 4.D";

        } else {
            return "";
        }

    }

    private String getSectoralReportCategoryTitle(Long landUseCategoryId) {

        // Forest Land

        if (landUseCategoryId.equals(FOREST_LAND_LU_ID)) {
            return "A. Total forest land";
        } else if (landUseCategoryId.equals(FOREST_LAND_REMAINING_FOREST_LAND_LU_ID)) {
            return "1. Forest land remaining forest land";
        } else if (landUseCategoryId.equals(LAND_CONVERTED_TO_FOREST_LAND_LU_ID)) {
            return "2. Land converted to forest land";
        } else if (landUseCategoryId.equals(CROPLAND_CONVERTED_TO_FOREST_LAND_LU_ID)) {
            return "2.1 Cropland converted to forest land";
        } else if (landUseCategoryId.equals(GRASSLAND_CONVERTED_TO_FOREST_LAND_LU_ID)) {
            return "2.2 Grassland converted to forest land";
        } else if (landUseCategoryId.equals(WETLANDS_CONVERTED_TO_FOREST_LAND_LU_ID)) {
            return "2.3 Wetlands converted to forest land";
        } else if (landUseCategoryId.equals(SETTLEMENTS_CONVERTED_TO_FOREST_LAND_LU_ID)) {
            return "2.4 Settlements converted to forest land";
        } else if (landUseCategoryId.equals(OTHER_LAND_CONVERTED_TO_FOREST_LAND_LU_ID)) {
            return "2.5 Other land converted to forest land";
        }

        // Cropland

        else if (landUseCategoryId.equals(CROPLAND_LU_ID)) {
            return "B. Total cropland";
        } else if (landUseCategoryId.equals(CROPLAND_REMAINING_CROPLAND_LU_ID)) {
            return "1. Cropland remaining cropland";
        } else if (landUseCategoryId.equals(LAND_CONVERTED_TO_CROPLAND_LU_ID)) {
            return "2. Land converted to cropland";
        } else if (landUseCategoryId.equals(FOREST_LAND_CONVERTED_TO_CROPLAND_LU_ID)) {
            return "2.1 Forest land converted to cropland";
        } else if (landUseCategoryId.equals(GRASSLAND_CONVERTED_TO_CROPLAND_LU_ID)) {
            return "2.2 Grassland converted to cropland";
        } else if (landUseCategoryId.equals(WETLANDS_CONVERTED_TO_CROPLAND_LU_ID)) {
            return "2.3 Wetlands converted to cropland";
        } else if (landUseCategoryId.equals(SETTLEMENTS_CONVERTED_TO_CROPLAND_LU_ID)) {
            return "2.4 Settlements converted to cropland";
        } else if (landUseCategoryId.equals(OTHER_LAND_CONVERTED_TO_CROPLAND_LU_ID)) {
            return "2.5 Other land converted to cropland";
        }

        // Grassland

        else if (landUseCategoryId.equals(GRASSLAND_LU_ID)) {
            return "C. Total grassland";
        } else if (landUseCategoryId.equals(GRASSLAND_REMAINING_GRASSLAND_LU_ID)) {
            return "1. Grassland remaining grassland";
        } else if (landUseCategoryId.equals(LAND_CONVERTED_TO_GRASSLAND_LU_ID)) {
            return "2. Land converted to grassland";
        } else if (landUseCategoryId.equals(FOREST_LAND_CONVERTED_TO_GRASSLAND_LU_ID)) {
            return "2.1 Forest land converted to grassland";
        } else if (landUseCategoryId.equals(CROPLAND_CONVERTED_TO_GRASSLAND_LU_ID)) {
            return "2.2 Cropland converted to grassland";
        } else if (landUseCategoryId.equals(WETLANDS_CONVERTED_TO_GRASSLAND_LU_ID)) {
            return "2.3 Wetlands converted to grassland";
        } else if (landUseCategoryId.equals(SETTLEMENTS_CONVERTED_TO_GRASSLAND_LU_ID)) {
            return "2.4 Settlements converted to grassland";
        } else if (landUseCategoryId.equals(OTHER_LAND_CONVERTED_TO_GRASSLAND_LU_ID)) {
            return "2.5 Other land converted to grassland";
        }

        // Wetlands

        else if (landUseCategoryId.equals(WETLANDS_LU_ID)) {
            return "D. Total wetlands";
        } else if (landUseCategoryId.equals(WETLANDS_REMAINING_WETLANDS_LU_ID)) {
            return "1. Wetlands remaining wetlands";
        } else if (landUseCategoryId.equals(LAND_CONVERTED_TO_WETLANDS_LU_ID)) {
            return "2. Land converted to wetlands";
        } else if (landUseCategoryId.equals(FOREST_LAND_CONVERTED_TO_WETLANDS_LU_ID)) {
            return "2.1 Forest land converted to wetlands";
        } else if (landUseCategoryId.equals(CROPLAND_CONVERTED_TO_WETLANDS_LU_ID)) {
            return "2.2 Cropland converted to wetlands";
        } else if (landUseCategoryId.equals(GRASSLAND_CONVERTED_TO_WETLANDS_LU_ID)) {
            return "2.3 Grassland converted to wetlands";
        } else if (landUseCategoryId.equals(SETTLEMENTS_CONVERTED_TO_WETLANDS_LU_ID)) {
            return "2.4 Settlements converted to wetlands";
        } else if (landUseCategoryId.equals(OTHER_LAND_CONVERTED_TO_WETLANDS_LU_ID)) {
            return "2.5 Other land converted to wetlands";
        }

        // Other

        else {
            return "";
        }

    }

    private String getSectoralReportReportingVariableTitle(Long reportingVariableId) {

        if (reportingVariableId.equals(AREA_REPORTING_VARIABLE_ID)) {
            return "Area (kha)";
        } else if (reportingVariableId.equals(NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE)) {
            return "Net carbon stock change in living biomass (kt C)";
        } else if (reportingVariableId.equals(NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE)) {
            return "Net carbon stock change in dead organic matter (kt C)";
        } else if (reportingVariableId.equals(NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE)) {
            return "Net carbon stock change in mineral soils (kt C)";
        } else if (reportingVariableId.equals(NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE)) {
            return "Net carbon stock change in organic soils (kt C)";
        } else if (reportingVariableId.equals(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE)) {
            return "Net emissions/removals (kt CO2)";
        } else {
            return "";
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

}
