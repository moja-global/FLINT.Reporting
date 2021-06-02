package global.moja.businessintelligence.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.businessintelligence.daos.LocationCoverTypesHistory;
import global.moja.businessintelligence.daos.LocationVegetationTypesHistory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class LocationCoverTypesHistoryProcessorIT {

    @Autowired
    LocationCoverTypesHistoryProcessor locationCoverTypesHistoryProcessor;

    static LocationVegetationTypesHistory locationVegetationTypesHistory;
    static LocationCoverTypesHistory expected;

    @BeforeAll
    public static void setUp() {

        // Initialize the expected Location Vegetation Types History
        try {
            locationVegetationTypesHistory =
                    new ObjectMapper()
                            .readValue(
                                    "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"partyId\": 34,\n" +
                                            "  \"tileId\": 1,\n" +
                                            "  \"vegetationHistoryId\": 1,\n" +
                                            "  \"unitCount\": 508981,\n" +
                                            "  \"unitAreaSum\": 39329.575347296675,\n" +
                                            "  \"history\": [\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 0,\n" +
                                            "      \"year\": 0,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 1,\n" +
                                            "      \"year\": 1985,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 2,\n" +
                                            "      \"year\": 1986,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 3,\n" +
                                            "      \"year\": 1987,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 4,\n" +
                                            "      \"year\": 1988,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 5,\n" +
                                            "      \"year\": 1989,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 6,\n" +
                                            "      \"year\": 1990,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 7,\n" +
                                            "      \"year\": 1991,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 8,\n" +
                                            "      \"year\": 1992,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 9,\n" +
                                            "      \"year\": 1993,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 10,\n" +
                                            "      \"year\": 1994,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 11,\n" +
                                            "      \"year\": 1995,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 12,\n" +
                                            "      \"year\": 1996,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 13,\n" +
                                            "      \"year\": 1997,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 14,\n" +
                                            "      \"year\": 1998,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 15,\n" +
                                            "      \"year\": 1999,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 16,\n" +
                                            "      \"year\": 2000,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 17,\n" +
                                            "      \"year\": 2001,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 18,\n" +
                                            "      \"year\": 2002,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 19,\n" +
                                            "      \"year\": 2003,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 20,\n" +
                                            "      \"year\": 2004,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 21,\n" +
                                            "      \"year\": 2005,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 22,\n" +
                                            "      \"year\": 2006,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 23,\n" +
                                            "      \"year\": 2007,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 24,\n" +
                                            "      \"year\": 2008,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 25,\n" +
                                            "      \"year\": 2009,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 26,\n" +
                                            "      \"year\": 2010,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 27,\n" +
                                            "      \"year\": 2011,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 28,\n" +
                                            "      \"year\": 2012,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 29,\n" +
                                            "      \"year\": 2013,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 30,\n" +
                                            "      \"year\": 2014,\n" +
                                            "      \"vegetationType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"coverTypeId\": 3,\n" +
                                            "        \"name\": \"Native Pasture\",\n" +
                                            "        \"woodyType\": true,\n" +
                                            "        \"naturalSystem\": false\n" +
                                            "      }\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}", LocationVegetationTypesHistory.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        // Initialize the expected Location Cover Types History
        try {
            expected =
                    new ObjectMapper()
                            .readValue(
                                    "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"partyId\": 34,\n" +
                                            "  \"tileId\": 1,\n" +
                                            "  \"vegetationHistoryId\": 1,\n" +
                                            "  \"unitCount\": 508981,\n" +
                                            "  \"unitAreaSum\": 39329.575347296675,\n" +
                                            "  \"history\": [\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 0,\n" +
                                            "      \"year\": 0,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 1,\n" +
                                            "      \"year\": 1985,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 2,\n" +
                                            "      \"year\": 1986,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 3,\n" +
                                            "      \"year\": 1987,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 4,\n" +
                                            "      \"year\": 1988,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 5,\n" +
                                            "      \"year\": 1989,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 6,\n" +
                                            "      \"year\": 1990,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 7,\n" +
                                            "      \"year\": 1991,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 8,\n" +
                                            "      \"year\": 1992,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 9,\n" +
                                            "      \"year\": 1993,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 10,\n" +
                                            "      \"year\": 1994,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 11,\n" +
                                            "      \"year\": 1995,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 12,\n" +
                                            "      \"year\": 1996,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 13,\n" +
                                            "      \"year\": 1997,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 14,\n" +
                                            "      \"year\": 1998,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 15,\n" +
                                            "      \"year\": 1999,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 16,\n" +
                                            "      \"year\": 2000,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 17,\n" +
                                            "      \"year\": 2001,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 18,\n" +
                                            "      \"year\": 2002,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 19,\n" +
                                            "      \"year\": 2003,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 20,\n" +
                                            "      \"year\": 2004,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 21,\n" +
                                            "      \"year\": 2005,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 22,\n" +
                                            "      \"year\": 2006,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 23,\n" +
                                            "      \"year\": 2007,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 24,\n" +
                                            "      \"year\": 2008,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 25,\n" +
                                            "      \"year\": 2009,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 26,\n" +
                                            "      \"year\": 2010,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 27,\n" +
                                            "      \"year\": 2011,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 28,\n" +
                                            "      \"year\": 2012,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 29,\n" +
                                            "      \"year\": 2013,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    },\n" +
                                            "    {\n" +
                                            "      \"itemNumber\": 30,\n" +
                                            "      \"year\": 2014,\n" +
                                            "      \"coverType\": {\n" +
                                            "        \"id\": 3,\n" +
                                            "        \"code\": \"G\",\n" +
                                            "        \"description\": \"Grassland\",\n" +
                                            "        \"version\": 1\n" +
                                            "      }\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}", LocationCoverTypesHistory.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @AfterAll
    public static void tearDown() {
        locationVegetationTypesHistory = null;
        expected = null;
    }

    @Test
    public void Given_DatabaseIdAndLocation_When_Process_Then_TheCorrespondingLocationCoverTypesHistoryWillBeReturned() {

        assertThat(locationCoverTypesHistoryProcessor
                .processLocationCoverTypesHistory(locationVegetationTypesHistory)
                .block())
                .isEqualTo(expected);

    }

}