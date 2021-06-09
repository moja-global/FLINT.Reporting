package global.moja.dataprocessing.configurations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.dataprocessing.models.*;
import global.moja.dataprocessing.util.LandUseChangeAction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ConfigurationDataProviderIT {

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static List<ConversionAndRemainingPeriod> conversionAndRemainingPeriods;
    static List<CoverType> coverTypes;
    static List<EmissionType> emissionTypes;
    static List<LandUseCategory> landUseCategories;
    static List<ReportingTable> reportingTables;
    static List<LandUseFluxType> landUsesFluxTypes;
    static List<LandUseFluxTypeToReportingTable> landUsesFluxTypesToReportingTables;
    static List<FluxToReportingVariable> fluxesToReportingVariables;
    static List<Database> databases;
    static List<VegetationType> vegetationTypes;
    static List<Date> dates;

    @BeforeAll
    public static void setUp() {

        try {
            conversionAndRemainingPeriods =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "conversion_and_remaining_periods.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            coverTypes =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "cover_types.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            emissionTypes =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "emission_types.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            landUseCategories =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "land_use_categories.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            reportingTables =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "reporting_tables.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        try {
            landUsesFluxTypes =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "land_uses_flux_types.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            landUsesFluxTypesToReportingTables =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "land_uses_flux_types_to_reporting_tables.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            fluxesToReportingVariables =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "fluxes_to_reporting_variables.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            databases =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "databases.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            vegetationTypes =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "database_vegetation_types.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            dates =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(absolutePath +
                                            FileSystems.getDefault().getSeparator() +
                                            "database_dates.json").toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    @AfterAll
    public static void tearDown() {
        conversionAndRemainingPeriods = null;
        coverTypes = null;
        landUseCategories = null;
        reportingTables = null;
        landUsesFluxTypes = null;
        landUsesFluxTypesToReportingTables = null;
        fluxesToReportingVariables = null;
        databases = null;
        vegetationTypes = null;
        dates = null;
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

    }


    @Test
    public void Given_EmissionTypeAbbreviation_When_GetEmissionType_Then_TheEmissionTypeWithThatAbbreviationWillBeReturned() {

        assertThat(configurationDataProvider.getEmissionType("CO2"))
                .isEqualTo(emissionTypes.get(0));

        assertThat(configurationDataProvider.getEmissionType("CH4"))
                .isEqualTo(emissionTypes.get(1));

        assertThat(configurationDataProvider.getEmissionType("N2O"))
                .isEqualTo(emissionTypes.get(2));


    }


    @Test
    public void Given_LandUseCategoryName_When_GetLandUseCategory_Then_TheLandUseCategoryWithThatNameWillBeReturned() {

        assertThat(configurationDataProvider.getLandUseCategory("Forest land Remaining Forest land"))
                .isEqualTo(landUseCategories.stream().filter(l -> l.getId() == 2).findAny().orElse(null));

        assertThat(configurationDataProvider.getLandUseCategory("Land Converted To Cropland"))
                .isEqualTo(landUseCategories.stream().filter(l -> l.getId() == 11).findAny().orElse(null));

        assertThat(configurationDataProvider.getLandUseCategory("Grassland Remaining Grassland"))
                .isEqualTo(landUseCategories.stream().filter(l -> l.getId() == 18).findAny().orElse(null));

        assertThat(configurationDataProvider.getLandUseCategory("Land Converted To Wetlands"))
                .isEqualTo(landUseCategories.stream().filter(l -> l.getId() == 27).findAny().orElse(null));

        assertThat(configurationDataProvider.getLandUseCategory("Settlements Remaining Settlements"))
                .isEqualTo(landUseCategories.stream().filter(l -> l.getId() == 34).findAny().orElse(null));


    }

    @Test
    public void Given_PreviousAndCurrentCoverTypeIdsAndLandUseChangeAction_When_GetLandUseCategory_Then_TheLandUseCategoryWithTheConcatenatedNameWillBeReturned() {

        assertThat(configurationDataProvider.getLandUseCategory(1L,1L, LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.stream().filter(l -> l.getId() == 2).findAny().orElse(null));

        assertThat(configurationDataProvider.getLandUseCategory(3L, 3L, LandUseChangeAction.REMAINING))
                .isEqualTo(landUseCategories.stream().filter(l -> l.getId() == 18).findAny().orElse(null));

        assertThat(configurationDataProvider.getLandUseCategory(5L,3L, LandUseChangeAction.CONVERSION))
                .isEqualTo(landUseCategories.stream().filter(l -> l.getId() == 23).findAny().orElse(null));

    }

    @Test
    public void Given_LandUseCategoryId_When_GetParentLandUseCategoryId_Then_TheParentLandUseCategoryIdWillBeReturned() {

        assertThat(configurationDataProvider.getParentLandUseCategoryId(2L))
                .isEqualTo(1L);

        assertThat(configurationDataProvider.getParentLandUseCategoryId(11L))
                .isEqualTo(9L);

        assertThat(configurationDataProvider.getParentLandUseCategoryId(18L))
                .isEqualTo(17L);

        assertThat(configurationDataProvider.getParentLandUseCategoryId(27L))
                .isEqualTo(25L);

        assertThat(configurationDataProvider.getParentLandUseCategoryId(34L))
                .isEqualTo(33L);


    }

    @Test
    public void Given_LandUseCategoryIdAndFluxTypeId_When_GetLandUsesFluxTypes_Then_TheLandUseFluxTypeWithTheSpecifiedLandUseCategoryIdAndFluxTypeIdWillBeReturned() {

        assertThat(configurationDataProvider.getLandUseFluxType(1L,1L))
                .isEqualTo(landUsesFluxTypes.get(0));

        assertThat(configurationDataProvider.getLandUseFluxType(2L,1L))
                .isEqualTo(landUsesFluxTypes.get(0));

        assertThat(configurationDataProvider.getLandUseFluxType(3L,1L))
                .isEqualTo(landUsesFluxTypes.get(0));

        assertThat(configurationDataProvider.getLandUseFluxType(4L,1L))
                .isEqualTo(landUsesFluxTypes.get(0));

        assertThat(configurationDataProvider.getLandUseFluxType(5L,1L))
                .isEqualTo(landUsesFluxTypes.get(0));


    }

    @Test
    public void Given_LandUseCategoryIdAndFluxTypeId_When_GetLandUsesFluxTypesToReportingTables_Then_TheLandUsesFluxTypesToReportingTablesRecordsWithTheUnderlyingLandUseFluxTypeWillBeReturned() {

        assertThat(configurationDataProvider.getLandUsesFluxTypesToReportingTables(1L,1L))
                .isEqualTo(landUsesFluxTypesToReportingTables
                        .stream()
                        .filter(l -> l.getLandUseFluxTypeId() == 1)
                        .collect(Collectors.toList()));

        assertThat(configurationDataProvider.getLandUsesFluxTypesToReportingTables(1L, 9L))
                .isEqualTo(landUsesFluxTypesToReportingTables
                        .stream()
                        .filter(l -> l.getLandUseFluxTypeId() == 41)
                        .collect(Collectors.toList()));

        assertThat(configurationDataProvider.getLandUsesFluxTypesToReportingTables(1L,17L))
                .isEqualTo(landUsesFluxTypesToReportingTables
                        .stream()
                        .filter(l -> l.getLandUseFluxTypeId() == 81)
                        .collect(Collectors.toList()));

    }


    @Test
    public void Given_StartPoolIdAndEndPoolId_When_GetFluxesToReportingVariables_Then_TheFluxesToReportingVariablesRecordsWithTheStartAndEndPoolIdsWillBeReturned() {

        assertThat(configurationDataProvider.getFluxesToReportingVariables(1L,5L))
                .isEqualTo(fluxesToReportingVariables
                        .stream()
                        .filter(l -> l.getStartPoolId() == 1 && l.getEndPoolId() == 5)
                        .collect(Collectors.toList()));

        assertThat(configurationDataProvider.getFluxesToReportingVariables(1L, 8L))
                .isEqualTo(fluxesToReportingVariables
                        .stream()
                        .filter(l -> l.getStartPoolId() == 1 && l.getEndPoolId() == 8)
                        .collect(Collectors.toList()));

        assertThat(configurationDataProvider.getFluxesToReportingVariables(1L,15L))
                .isEqualTo(fluxesToReportingVariables
                        .stream()
                        .filter(l -> l.getStartPoolId() == 1 && l.getEndPoolId() == 15)
                        .collect(Collectors.toList()));

    }


    @Test
    public void Given_DatabaseIdAndVegetationTypeId_When_GetVegetationType_Then_TheVegetationTypeWithTheSpecifiedIdInTheDatabaseWithTheSpecifiedIdWillBeReturned() {

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

    @Test
    public void Given_DatabaseIdAndDateId_When_GetDateDimension_Then_TheDateWithTheSpecifiedIdInTheDatabaseWithTheSpecifiedIdWillBeReturned() {

        assertThat(configurationDataProvider.getDateDimensionId(1L, 1984))
                .isEqualTo(dates.get(0).getId());

        assertThat(configurationDataProvider.getDateDimensionId(1L, 1985))
                .isEqualTo(dates.get(1).getId());

        assertThat(configurationDataProvider.getDateDimensionId(1L, 1986))
                .isEqualTo(dates.get(2).getId());

        assertThat(configurationDataProvider.getDateDimensionId(1L, 1987))
                .isEqualTo(dates.get(3).getId());

        assertThat(configurationDataProvider.getDateDimensionId(1L, 1988))
                .isEqualTo(dates.get(4).getId());

    }

}