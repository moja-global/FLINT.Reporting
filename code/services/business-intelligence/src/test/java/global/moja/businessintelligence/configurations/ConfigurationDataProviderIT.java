package global.moja.businessintelligence.configurations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.businessintelligence.models.*;
import global.moja.businessintelligence.util.LandUseChangeAction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ConfigurationDataProviderIT {

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    static List<ConversionAndRemainingPeriod> conversionAndRemainingPeriods;
    static List<CoverType> coverTypes;
    static List<Database> databases;
    static List<LandUseCategory> landUseCategories;
    static List<ReportingTable> reportingTables;
    static List<VegetationType> vegetationTypes;

    @BeforeAll
    public static void setUp() {

        try {

            //<editor-fold desc="conversionAndRemainingPeriods">
            conversionAndRemainingPeriods =
                    new ObjectMapper()
                            .readValue("[\n" +
                                            " {\n" +
                                            "   \"id\": 1,\n" +
                                            "   \"previousLandCoverId\": 1,\n" +
                                            "   \"currentLandCoverId\": 2,\n" +
                                            "   \"conversionPeriod\": 3,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 2,\n" +
                                            "   \"previousLandCoverId\": 1,\n" +
                                            "   \"currentLandCoverId\": 3,\n" +
                                            "   \"conversionPeriod\": 3,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 3,\n" +
                                            "   \"previousLandCoverId\": 1,\n" +
                                            "   \"currentLandCoverId\": 4,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 4,\n" +
                                            "   \"previousLandCoverId\": 1,\n" +
                                            "   \"currentLandCoverId\": 5,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 5,\n" +
                                            "   \"previousLandCoverId\": 2,\n" +
                                            "   \"currentLandCoverId\": 1,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 6,\n" +
                                            "   \"previousLandCoverId\": 2,\n" +
                                            "   \"currentLandCoverId\": 3,\n" +
                                            "   \"conversionPeriod\": 10,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 7,\n" +
                                            "   \"previousLandCoverId\": 2,\n" +
                                            "   \"currentLandCoverId\": 4,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 8,\n" +
                                            "   \"previousLandCoverId\": 2,\n" +
                                            "   \"currentLandCoverId\": 5,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 9,\n" +
                                            "   \"previousLandCoverId\": 3,\n" +
                                            "   \"currentLandCoverId\": 1,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 10,\n" +
                                            "   \"previousLandCoverId\": 3,\n" +
                                            "   \"currentLandCoverId\": 2,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 11,\n" +
                                            "   \"previousLandCoverId\": 3,\n" +
                                            "   \"currentLandCoverId\": 4,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 12,\n" +
                                            "   \"previousLandCoverId\": 3,\n" +
                                            "   \"currentLandCoverId\": 5,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 13,\n" +
                                            "   \"previousLandCoverId\": 4,\n" +
                                            "   \"currentLandCoverId\": 1,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 14,\n" +
                                            "   \"previousLandCoverId\": 4,\n" +
                                            "   \"currentLandCoverId\": 2,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 15,\n" +
                                            "   \"previousLandCoverId\": 4,\n" +
                                            "   \"currentLandCoverId\": 3,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 16,\n" +
                                            "   \"previousLandCoverId\": 4,\n" +
                                            "   \"currentLandCoverId\": 5,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 17,\n" +
                                            "   \"previousLandCoverId\": 5,\n" +
                                            "   \"currentLandCoverId\": 1,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 18,\n" +
                                            "   \"previousLandCoverId\": 5,\n" +
                                            "   \"currentLandCoverId\": 2,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 19,\n" +
                                            "   \"previousLandCoverId\": 5,\n" +
                                            "   \"currentLandCoverId\": 3,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 20,\n" +
                                            "   \"previousLandCoverId\": 5,\n" +
                                            "   \"currentLandCoverId\": 4,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 21,\n" +
                                            "   \"previousLandCoverId\": 6,\n" +
                                            "   \"currentLandCoverId\": 1,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 22,\n" +
                                            "   \"previousLandCoverId\": 6,\n" +
                                            "   \"currentLandCoverId\": 2,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 23,\n" +
                                            "   \"previousLandCoverId\": 6,\n" +
                                            "   \"currentLandCoverId\": 3,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 24,\n" +
                                            "   \"previousLandCoverId\": 6,\n" +
                                            "   \"currentLandCoverId\": 4,\n" +
                                            "   \"conversionPeriod\": 0,\n" +
                                            "   \"remainingPeriod\": 20,\n" +
                                            "   \"version\": 1\n" +
                                            " }\n" +
                                            "]",
                                    new TypeReference<>() {
                                    });
            //</editor-fold>

            //<editor-fold desc="coverTypes">
            coverTypes =
                    new ObjectMapper()
                            .readValue("[\n" +
                                            " {\n" +
                                            "   \"id\": 1,\n" +
                                            "   \"code\": \"F\",\n" +
                                            "   \"description\": \"Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 2,\n" +
                                            "   \"code\": \"C\",\n" +
                                            "   \"description\": \"Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 3,\n" +
                                            "   \"code\": \"G\",\n" +
                                            "   \"description\": \"Grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 4,\n" +
                                            "   \"code\": \"W\",\n" +
                                            "   \"description\": \"Wetland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 5,\n" +
                                            "   \"code\": \"S\",\n" +
                                            "   \"description\": \"Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 6,\n" +
                                            "   \"code\": \"O\",\n" +
                                            "   \"description\": \"Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " }\n" +
                                            "]",
                                    new TypeReference<>() {
                                    });

            //</editor-fold>

            //<editor-fold desc="databases">

            databases =
                    new ObjectMapper()
                            .readValue("[\n" +
                                            " {\n" +
                                            "   \"id\": 1,\n" +
                                            "   \"label\": \"SLEEK FSR TESTRUN 1\",\n" +
                                            "   \"description\": \"\",\n" +
                                            "   \"url\": \"jdbc:postgresql://reporter.miles.co.ke:31392/sleek_fsr_testrun_1\",\n" +
                                            "   \"startYear\": 1984,\n" +
                                            "   \"endYear\": 2014,\n" +
                                            "   \"processed\": false,\n" +
                                            "   \"published\": false,\n" +
                                            "   \"archived\": false,\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 2,\n" +
                                            "   \"label\": \"SLEEK FSR TESTRUN 2\",\n" +
                                            "   \"description\": \"\",\n" +
                                            "   \"url\": \"jdbc:postgresql://reporter.miles.co.ke:31392/sleek_fsr_testrun_2\",\n" +
                                            "   \"startYear\": 1984,\n" +
                                            "   \"endYear\": 2005,\n" +
                                            "   \"processed\": false,\n" +
                                            "   \"published\": false,\n" +
                                            "   \"archived\": false,\n" +
                                            "   \"version\": 1\n" +
                                            " }\n" +
                                            "]",
                                    new TypeReference<>() {
                                    });

            //</editor-fold>

            //<editor-fold desc="landUseCategories">

            landUseCategories =
                    new ObjectMapper()
                            .readValue("[\n" +
                                            " {\n" +
                                            "   \"id\": 1,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": null,\n" +
                                            "   \"coverTypeId\": 1,\n" +
                                            "   \"name\": \"Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 2,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 1,\n" +
                                            "   \"coverTypeId\": 1,\n" +
                                            "   \"name\": \"Forest land Remaining Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 3,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 1,\n" +
                                            "   \"coverTypeId\": 1,\n" +
                                            "   \"name\": \"Land Converted To Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 4,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 3,\n" +
                                            "   \"coverTypeId\": 1,\n" +
                                            "   \"name\": \"Cropland Converted To Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 5,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 3,\n" +
                                            "   \"coverTypeId\": 1,\n" +
                                            "   \"name\": \"Grassland Converted To Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 6,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 3,\n" +
                                            "   \"coverTypeId\": 1,\n" +
                                            "   \"name\": \"Wetlands Converted To Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 7,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 3,\n" +
                                            "   \"coverTypeId\": 1,\n" +
                                            "   \"name\": \"Settlements Converted To Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 8,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 3,\n" +
                                            "   \"coverTypeId\": 1,\n" +
                                            "   \"name\": \"Other land Converted To Forest land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 9,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": null,\n" +
                                            "   \"coverTypeId\": 2,\n" +
                                            "   \"name\": \"Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 10,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 9,\n" +
                                            "   \"coverTypeId\": 2,\n" +
                                            "   \"name\": \"Cropland Remaining Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 11,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 9,\n" +
                                            "   \"coverTypeId\": 2,\n" +
                                            "   \"name\": \"Land Converted To Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 12,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 11,\n" +
                                            "   \"coverTypeId\": 2,\n" +
                                            "   \"name\": \"Forest land Converted To Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 13,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 11,\n" +
                                            "   \"coverTypeId\": 2,\n" +
                                            "   \"name\": \"Grassland Converted To Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 14,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 11,\n" +
                                            "   \"coverTypeId\": 2,\n" +
                                            "   \"name\": \"Wetlands Converted To Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 15,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 11,\n" +
                                            "   \"coverTypeId\": 2,\n" +
                                            "   \"name\": \"Settlements Converted To Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 16,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 11,\n" +
                                            "   \"coverTypeId\": 2,\n" +
                                            "   \"name\": \"Other land Converted To Cropland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 17,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": null,\n" +
                                            "   \"coverTypeId\": 3,\n" +
                                            "   \"name\": \"Grassland \",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 18,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 17,\n" +
                                            "   \"coverTypeId\": 3,\n" +
                                            "   \"name\": \"Grassland Remaining Grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 19,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 17,\n" +
                                            "   \"coverTypeId\": 3,\n" +
                                            "   \"name\": \"Land Converted To Grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 20,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 19,\n" +
                                            "   \"coverTypeId\": 3,\n" +
                                            "   \"name\": \"Cropland Converted To Grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 21,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 19,\n" +
                                            "   \"coverTypeId\": 3,\n" +
                                            "   \"name\": \"Forest land Converted To Grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 22,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 19,\n" +
                                            "   \"coverTypeId\": 3,\n" +
                                            "   \"name\": \"Wetlands Converted To Grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 23,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 19,\n" +
                                            "   \"coverTypeId\": 3,\n" +
                                            "   \"name\": \"Settlements Converted To Grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 24,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 19,\n" +
                                            "   \"coverTypeId\": 3,\n" +
                                            "   \"name\": \"Other land Converted To Grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 25,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": null,\n" +
                                            "   \"coverTypeId\": 4,\n" +
                                            "   \"name\": \"Wetlands\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 26,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 25,\n" +
                                            "   \"coverTypeId\": 4,\n" +
                                            "   \"name\": \"Wetlands Remaining Wetlands\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 27,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 25,\n" +
                                            "   \"coverTypeId\": 4,\n" +
                                            "   \"name\": \"Land Converted To Wetlands\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 28,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 27,\n" +
                                            "   \"coverTypeId\": 4,\n" +
                                            "   \"name\": \"Cropland Converted To Wetlands\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 29,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 27,\n" +
                                            "   \"coverTypeId\": 4,\n" +
                                            "   \"name\": \"Grassland Converted To Wetlands\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 30,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 27,\n" +
                                            "   \"coverTypeId\": 4,\n" +
                                            "   \"name\": \"Forest land Converted To Wetlands\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 31,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 27,\n" +
                                            "   \"coverTypeId\": 4,\n" +
                                            "   \"name\": \"Settlements Converted To Wetlands\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 32,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 27,\n" +
                                            "   \"coverTypeId\": 4,\n" +
                                            "   \"name\": \"Other land Converted To Wetlands\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 33,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": null,\n" +
                                            "   \"coverTypeId\": 5,\n" +
                                            "   \"name\": \"Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 34,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 33,\n" +
                                            "   \"coverTypeId\": 5,\n" +
                                            "   \"name\": \"Settlements Remaining Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 35,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 33,\n" +
                                            "   \"coverTypeId\": 5,\n" +
                                            "   \"name\": \"Land Converted To Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 36,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 35,\n" +
                                            "   \"coverTypeId\": 5,\n" +
                                            "   \"name\": \"Cropland Converted To Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 37,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 35,\n" +
                                            "   \"coverTypeId\": 5,\n" +
                                            "   \"name\": \"Grassland Converted To Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 38,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 35,\n" +
                                            "   \"coverTypeId\": 5,\n" +
                                            "   \"name\": \"Wetlands Converted To Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 39,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 35,\n" +
                                            "   \"coverTypeId\": 5,\n" +
                                            "   \"name\": \"Forest land Converted To Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 40,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 35,\n" +
                                            "   \"coverTypeId\": 5,\n" +
                                            "   \"name\": \"Other land Converted To Settlements\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 41,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": null,\n" +
                                            "   \"coverTypeId\": 6,\n" +
                                            "   \"name\": \"Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 42,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 41,\n" +
                                            "   \"coverTypeId\": 6,\n" +
                                            "   \"name\": \"Other land Remaining Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 43,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 41,\n" +
                                            "   \"coverTypeId\": 6,\n" +
                                            "   \"name\": \"Land Converted To Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 44,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 43,\n" +
                                            "   \"coverTypeId\": 6,\n" +
                                            "   \"name\": \"Cropland Converted To Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 45,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 43,\n" +
                                            "   \"coverTypeId\": 6,\n" +
                                            "   \"name\": \"Grassland Converted To Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 46,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 43,\n" +
                                            "   \"coverTypeId\": 6,\n" +
                                            "   \"name\": \"Wetlands Converted To Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 47,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 43,\n" +
                                            "   \"coverTypeId\": 6,\n" +
                                            "   \"name\": \"Settlements Converted To Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 48,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"parentLandUseCategoryId\": 43,\n" +
                                            "   \"coverTypeId\": 6,\n" +
                                            "   \"name\": \"Forest land Converted To Other land\",\n" +
                                            "   \"version\": 1\n" +
                                            " }\n" +
                                            "]",
                                    new TypeReference<>() {
                                    });

            //</editor-fold>

            //<editor-fold desc="reportingTables">

            reportingTables =
                    new ObjectMapper()
                            .readValue("[\n" +
                                            " {\n" +
                                            "   \"id\": 1,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4\",\n" +
                                            "   \"name\": \"SECTORAL REPORT FOR LAND USE, LAND-USE CHANGE AND FORESTRY\",\n" +
                                            "   \"description\": \"Sectoral/land use category level summary of CO2, CH4 and N2O emissions. This summary is taken from the individual tables for each land use category\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 2,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.1\",\n" +
                                            "   \"name\": \"Land Transition Matrix\",\n" +
                                            "   \"description\": \"This table captures the area in each land use category for the year and the area of change in land use from the previous year.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 3,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.A\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Forest Land\",\n" +
                                            "   \"description\": \"Emissions and area data for the Forest land category including forest land remaining forest land and land converted to forest land.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 4,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.B\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Cropland\",\n" +
                                            "   \"description\": \"Emissions and area data for the Cropland category including cropland remaining cropland and land converted to cropland.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 5,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.C\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Grassland\",\n" +
                                            "   \"description\": \"Emissions and area data for the Grassland category including grassland remaining grassland and land converted to grassland.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 6,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.D\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Wetlands\",\n" +
                                            "   \"description\": \"Emissions and area data for the Wetland category including wetlands remaining wetlands and land converted to wetlands.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 7,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.E\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Settlements\",\n" +
                                            "   \"description\": \"Emissions and area data for the Settlements category including settlements remaining settlements and land converted to settlements.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 8,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.F\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Other land\",\n" +
                                            "   \"description\": \"Emissions and area data for the Other land category. Only area data is reported for other land remaining other land and emissions and area are reported for land converted to other land.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 9,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4(I)\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Direct nitrous oxide (N2O) emissions from nitrogen (N) inputs(1) to managed soils\",\n" +
                                            "   \"description\": \"Direct N2O emissions from N fertiliser application on forest land, wetlands, settlements and other. N2O emission from N fertiliser application is reported in the Agriculture sector.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 10,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4(II)\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Emissions and removals from drainage and rewetting and other management of organic and mineral soils\",\n" +
                                            "   \"description\": \"This table captures the CO2 and CH4 emissions from the draining and rewetting of soils on all land use categories. N2O emissions are also captured for all land categories except for Cropland and Grassland which are reported under the Agriculture sector.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 11,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4(III)\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Direct nitrous oxide (N2O) emissions from nitrogen (N) mineralization/immobilization associated with loss/gain of soil organic matter resulting from change of land use or management of mineral soils.\",\n" +
                                            "   \"description\": \"Covers all land-use categories except for cropland remaining cropland which is included in the Agriculture sector.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 12,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4 (IV)\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Indirect nitrous oxide (N2O) emissions from managed soils\",\n" +
                                            "   \"description\": \"Atmospheric deposition and leaching and runoff of N from the following sources of N inputs: synthetic and organic N fertilizer from land use categories, other than cropland and grassland (these emissions are reported in the agriculture sector)\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 13,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4 (V)\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Biomass Burning\",\n" +
                                            "   \"description\": \"This table captures emissions from Biomass Burning from Wildfires and Controlled burning. CO2 emissions from biomass burning should be reported here if they are not already included under the carbon stock change for the land use category. CH4 and N2O emissions associated with biomass burning on forest land and grassland classified as savannah should be reported in the Agriculture sector. \",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 14,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.G.s1\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Harvested Wood Products\",\n" +
                                            "   \"description\": \"This table captures the emissions from Harvested Wood Products and depends upon the method selected by the Party.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 15,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 4.G.s2\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR LAND USE, LAND-USE CHANGE AND FORESTRY – Harvested Wood Products\",\n" +
                                            "   \"description\": \"This table is for the historical activity data for Harvested Wood Products.\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 16,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3s1 and 3s2\",\n" +
                                            "   \"name\": \"SECTORAL REPORT AGRICULTURE\",\n" +
                                            "   \"description\": \"Sectoral level summary for Agriculture. Summary information drawn from the sectoral background tables\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 17,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3.A\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR AGRICULTURE – Enteric Fermentation\",\n" +
                                            "   \"description\": \"Captures the emissions and activity data for enteric fermentation CH4\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 18,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3.B(a)\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR AGRICULTURE – CH4 Emissions from Manure Management\",\n" +
                                            "   \"description\": \"Captures the CH4 Emissions from Manure Management\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 19,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3.B(b)\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR AGRICULTURE – N2O Emissions from Manure Management\",\n" +
                                            "   \"description\": \"Captures the CH4 Emissions from Manure Management\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 20,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3.C\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR AGRICULTURE – Rice Cultivation\",\n" +
                                            "   \"description\": \"Captures the activity data and CH4 emissions from Rice Cultivation\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 21,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3.D\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR AGRICULTURE – Direct and indirect N2O emissions from agricultural soils\",\n" +
                                            "   \"description\": \"Captures the N2O emissions from agricultural soils including the application of fertilisers to cropland and grassland\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 22,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3.E\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR AGRICULTURE – Prescribed burning of savannahs\",\n" +
                                            "   \"description\": \"Captures the activity data and CH4 and N2O emissions from savannah burning on forest land and grassland defined as savannah\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 23,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3.F\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR AGRICULTURE – Field burning of agricultural residues\",\n" +
                                            "   \"description\": \"Captures the activity data, CH4 and N2O emissions associated with the burning of agricultural residues\",\n" +
                                            "   \"version\": 1\n" +
                                            " },\n" +
                                            " {\n" +
                                            "   \"id\": 24,\n" +
                                            "   \"reportingFrameworkId\": 1,\n" +
                                            "   \"number\": \"Table 3.G-I\",\n" +
                                            "   \"name\": \"SECTORAL BACKGROUND DATA FOR AGRICULTURE – CO2 emissions from liming, urea application and other carbon-containing fertilizers\",\n" +
                                            "   \"description\": \"CO2 emissions associated with fertiliser applications\",\n" +
                                            "   \"version\": 1\n" +
                                            " }\n" +
                                            "]",
                                    new TypeReference<>() {
                                    });

            //</editor-fold>

            //<editor-fold desc="vegetationTypes">
            vegetationTypes = new ObjectMapper()
                    .readValue("[\n" +
                                    " {\n" +
                                    "   \"id\": 1,\n" +
                                    "   \"name\": \"Maize\",\n" +
                                    "   \"coverTypeId\": 2,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 2,\n" +
                                    "   \"name\": \"Wheat\",\n" +
                                    "   \"coverTypeId\": 2,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 3,\n" +
                                    "   \"name\": \"Native Pasture\",\n" +
                                    "   \"coverTypeId\": 3,\n" +
                                    "   \"woodyType\": true,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 4,\n" +
                                    "   \"name\": \"Indigenous Forest\",\n" +
                                    "   \"coverTypeId\": 1,\n" +
                                    "   \"woodyType\": true,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 5,\n" +
                                    "   \"name\": \"Eucalypt Plantation\",\n" +
                                    "   \"coverTypeId\": 1,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 6,\n" +
                                    "   \"name\": \"Acacia Plantation\",\n" +
                                    "   \"coverTypeId\": 1,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 7,\n" +
                                    "   \"name\": \"Cypress Plantation\",\n" +
                                    "   \"coverTypeId\": 1,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 8,\n" +
                                    "   \"name\": \"Pinus Plantation\",\n" +
                                    "   \"coverTypeId\": 1,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 9,\n" +
                                    "   \"name\": \"Casuarina Plantation\",\n" +
                                    "   \"coverTypeId\": 1,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 10,\n" +
                                    "   \"name\": \"Vitex Plantation\",\n" +
                                    "   \"coverTypeId\": 1,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 11,\n" +
                                    "   \"name\": \"Douglas Fir\",\n" +
                                    "   \"coverTypeId\": 1,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 12,\n" +
                                    "   \"name\": \"Improved Pasture\",\n" +
                                    "   \"coverTypeId\": 3,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 13,\n" +
                                    "   \"name\": \"Oil Palm\",\n" +
                                    "   \"coverTypeId\": 2,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 14,\n" +
                                    "   \"name\": \"Apple Orchard\",\n" +
                                    "   \"coverTypeId\": 2,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 15,\n" +
                                    "   \"name\": \"Dam\",\n" +
                                    "   \"coverTypeId\": 4,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 16,\n" +
                                    "   \"name\": \"Rice\",\n" +
                                    "   \"coverTypeId\": 2,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 17,\n" +
                                    "   \"name\": \"Natural Shrubs\",\n" +
                                    "   \"coverTypeId\": 3,\n" +
                                    "   \"woodyType\": true,\n" +
                                    "   \"naturalSystem\": true\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 18,\n" +
                                    "   \"name\": \"Bare Rock\",\n" +
                                    "   \"coverTypeId\": 6,\n" +
                                    "   \"woodyType\": true,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " },\n" +
                                    " {\n" +
                                    "   \"id\": 19,\n" +
                                    "   \"name\": \"Roading\",\n" +
                                    "   \"coverTypeId\": 5,\n" +
                                    "   \"woodyType\": false,\n" +
                                    "   \"naturalSystem\": false\n" +
                                    " }\n" +
                                    "]",
                            new TypeReference<>() {
                            });

            //</editor-fold>

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @AfterAll
    public static void tearDown() {
        conversionAndRemainingPeriods = null;
        coverTypes = null;
        databases = null;
        landUseCategories = null;
        reportingTables = null;
        vegetationTypes = null;
    }

    @Test
    public void Given_PreviousAndCurrentCoverTypeIds_When_GetConversionAndRemainingPeriod_Then_TheConversionAndRemainingPeriodWithThoseIdsRespectivelyWillBeReturned() {

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod(1L, 2L))
                .isEqualTo(conversionAndRemainingPeriods.get(0));

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod(2L, 1L))
                .isEqualTo(conversionAndRemainingPeriods.get(4));

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod(3L, 1L))
                .isEqualTo(conversionAndRemainingPeriods.get(8));

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod(4L, 1L))
                .isEqualTo(conversionAndRemainingPeriods.get(12));

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod(5L, 1L))
                .isEqualTo(conversionAndRemainingPeriods.get(16));

        /*assertThat(configurationDataProvider.getConversionAndRemainingPeriod(6L, 1L))
                .isEqualTo(conversionAndRemainingPeriods.get(20));*/
    }

    @Test
    public void Given_PreviousAndCurrentCoverTypeDescriptions_When_GetConversionAndRemainingPeriod_Then_TheConversionAndRemainingPeriodWithThoseDescriptionsRespectivelyWillBeReturned() {

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod("Forest land", "Cropland"))
                .isEqualTo(conversionAndRemainingPeriods.get(0));

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod("Cropland", "Forest land"))
                .isEqualTo(conversionAndRemainingPeriods.get(4));

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod("Grassland", "Forest land"))
                .isEqualTo(conversionAndRemainingPeriods.get(8));

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod("Wetland", "Forest land"))
                .isEqualTo(conversionAndRemainingPeriods.get(12));

        assertThat(configurationDataProvider.getConversionAndRemainingPeriod("Settlements", "Forest land"))
                .isEqualTo(conversionAndRemainingPeriods.get(16));

        /*assertThat(configurationDataProvider.getConversionAndRemainingPeriod("Other land", "Forest land"))
                .isEqualTo(conversionAndRemainingPeriods.get(20));*/

    }

    @Test
    public void Given_CoverTypeId_When_GetCoverType_Then_TheCoverTypeWithThatIdWillBeReturned() {

        assertThat(configurationDataProvider.getCoverType(1L))
                .isEqualTo(coverTypes.get(0));

        assertThat(configurationDataProvider.getCoverType(2L))
                .isEqualTo(coverTypes.get(1));

        assertThat(configurationDataProvider.getCoverType(3L))
                .isEqualTo(coverTypes.get(2));

        assertThat(configurationDataProvider.getCoverType(4L))
                .isEqualTo(coverTypes.get(3));

        assertThat(configurationDataProvider.getCoverType(5L))
                .isEqualTo(coverTypes.get(4));

        /*assertThat(configurationDataProvider.getCoverType(6L))
                .isEqualTo(coverTypes.get(5));*/

    }

    @Test
    public void Given_CoverTypeDescription_When_GetCoverType_Then_TheCoverTypeWithThatDescriptionWillBeReturned() {

        assertThat(configurationDataProvider.getCoverType("Forest land"))
                .isEqualTo(coverTypes.get(0));

        assertThat(configurationDataProvider.getCoverType("Cropland"))
                .isEqualTo(coverTypes.get(1));

        assertThat(configurationDataProvider.getCoverType("Grassland"))
                .isEqualTo(coverTypes.get(2));

        assertThat(configurationDataProvider.getCoverType("Wetland"))
                .isEqualTo(coverTypes.get(3));

        assertThat(configurationDataProvider.getCoverType("Settlements"))
                .isEqualTo(coverTypes.get(4));

        /*assertThat(configurationDataProvider.getCoverType("Other land"))
                .isEqualTo(coverTypes.get(5));*/

    }


    @Test
    public void Given_LandUseCategoryName_When_GetLandUseCategory_Then_TheLandUseCategory_WithThatNameWillBeReturned() {

        assertThat(configurationDataProvider.getLandUseCategory("Forest land Remaining Forest land"))
                .isEqualTo(landUseCategories.get(1));

        assertThat(configurationDataProvider.getLandUseCategory("Land Converted To Cropland"))
                .isEqualTo(landUseCategories.get(10));

        assertThat(configurationDataProvider.getLandUseCategory("Grassland Remaining Grassland"))
                .isEqualTo(landUseCategories.get(17));

        assertThat(configurationDataProvider.getLandUseCategory("Land Converted To Wetlands"))
                .isEqualTo(landUseCategories.get(26));

        assertThat(configurationDataProvider.getLandUseCategory("Settlements Remaining Settlements"))
                .isEqualTo(landUseCategories.get(33));

        /*assertThat(configurationDataProvider.getLandUseCategory("Other land Remaining Other land"))
                .isEqualTo(landUseCategories.get(41));*/

    }

    @Test
    public void Given_PreviousAndCurrentCoverTypeIdsAndLandUseChangeAction_When_GetLandUseCategory_Then_TheLandUseCategory_WithTheConcatenatedNameWillBeReturned() {

        assertThat(configurationDataProvider.getLandUseCategory(1L,1L, LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.get(1));

        assertThat(configurationDataProvider.getLandUseCategory(3L, 3L, LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.get(17));

        assertThat(configurationDataProvider.getLandUseCategory(5L,5L, LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.get(33));

        /*assertThat(configurationDataProvider.getLandUseCategory(6L, 6L, LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.get(41));*/

    }


    @Test
    public void Given_PreviousAndCurrentCoverTypeDescriptionsAndLandUseChangeAction_When_GetLandUseCategory_Then_TheLandUseCategory_WithTheConcatenatedNameWillBeReturned() {

        assertThat(configurationDataProvider.getLandUseCategory("Forest land","Forest land", LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.get(1));

        assertThat(configurationDataProvider.getLandUseCategory("Land","Cropland", LandUseChangeAction.CONVERSION))
                .isEqualTo(landUseCategories.get(10));

        assertThat(configurationDataProvider.getLandUseCategory("Grassland", "Grassland", LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.get(17));

        assertThat(configurationDataProvider.getLandUseCategory("Land","Wetlands", LandUseChangeAction.CONVERSION))
                .isEqualTo(landUseCategories.get(26));

        assertThat(configurationDataProvider.getLandUseCategory("Settlements","Settlements", LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.get(33));

        /*assertThat(configurationDataProvider.getLandUseCategory("Other land","Other land", LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.get(41));*/

    }


    @Test
    public void Given_ReportingTableId_When_GetReportingTable_Then_TheReportingTableWithThatIdWillBeReturned() {

        assertThat(configurationDataProvider.getReportingTable(1L))
                .isEqualTo(reportingTables.get(0));

        assertThat(configurationDataProvider.getReportingTable(2L))
                .isEqualTo(reportingTables.get(1));

        assertThat(configurationDataProvider.getReportingTable(3L))
                .isEqualTo(reportingTables.get(2));

        assertThat(configurationDataProvider.getReportingTable(4L))
                .isEqualTo(reportingTables.get(3));

        assertThat(configurationDataProvider.getReportingTable(5L))
                .isEqualTo(reportingTables.get(4));

    }


    @Test
    public void Given_VegetationTypeId_When_GetVegetationType_Then_TheVegetationTypeWithThatIdWillBeReturned() {

        assertThat(configurationDataProvider.getVegetationType(1L, 1L))
                .isEqualTo(vegetationTypes.get(0));

        assertThat(configurationDataProvider.getVegetationType(1L, 2L))
                .isEqualTo(vegetationTypes.get(1));

        assertThat(configurationDataProvider.getVegetationType(1L, 3L))
                .isEqualTo(vegetationTypes.get(2));

        assertThat(configurationDataProvider.getVegetationType(1L, 4L))
                .isEqualTo(vegetationTypes.get(3));

        assertThat(configurationDataProvider.getVegetationType(1L, 5L))
                .isEqualTo(vegetationTypes.get(4));

    }
}