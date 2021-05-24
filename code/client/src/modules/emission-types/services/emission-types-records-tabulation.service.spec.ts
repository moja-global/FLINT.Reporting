
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { EmissionType } from '../models/emission-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { EmissionTypesDataService } from './emission-types-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { EmissionTypesRecordsTabulationService } from './emission-types-records-tabulation.service';

describe('EmissionTypesRecordsTabulationService', () => {

    let emissionTypesDataService: EmissionTypesDataService;
    let emissionTypesTableDataService: EmissionTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, EmissionTypesDataService, MessageService],
        });

        emissionTypesDataService = TestBed.inject(EmissionTypesDataService);
        emissionTypesTableDataService = TestBed.inject(EmissionTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('emissionTypes$', () => {

        it('should return transformed Emission Types', () => {

            // Define a couple of mock Emission Types
            const mockEmissionTypes = [
                new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
            ];


            emissionTypesTableDataService.emissionTypes$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockEmissionTypes);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/all`).flush(mockEmissionTypes);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(emissionTypesTableDataService.compare("EmissionType 1", "EmissionType 2")).toEqual(-1);
            expect(emissionTypesTableDataService.compare("EmissionType 1", "EmissionType 1")).toEqual(0);
            expect(emissionTypesTableDataService.compare("EmissionType 2", "EmissionType 1")).toEqual(1);

            // numbers
            expect(emissionTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(emissionTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(emissionTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Emission Types records by table column in ascending or descending order', () => {

            // Define a couple of mock Emission Types

            const unsortedMockEmissionTypes = [
                new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),                
            ];

            const sortedMockEmissionTypes1 = [
                new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
            ];  
            
            const sortedMockEmissionTypes2 = [
                new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),                
                new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),                
            ];            


            expect(emissionTypesTableDataService.sort(unsortedMockEmissionTypes,"id", "asc")).toEqual(sortedMockEmissionTypes1);
            expect(emissionTypesTableDataService.sort(unsortedMockEmissionTypes,"id", "desc")).toEqual(sortedMockEmissionTypes2);

            expect(emissionTypesTableDataService.sort(unsortedMockEmissionTypes,"name", "asc")).toEqual(sortedMockEmissionTypes1);
            expect(emissionTypesTableDataService.sort(unsortedMockEmissionTypes,"name", "desc")).toEqual(sortedMockEmissionTypes2);
            
            expect(emissionTypesTableDataService.sort(unsortedMockEmissionTypes,"description", "asc")).toEqual(sortedMockEmissionTypes1);
            expect(emissionTypesTableDataService.sort(unsortedMockEmissionTypes,"description", "desc")).toEqual(sortedMockEmissionTypes2);            
        }); 
        
        
        it('should return true if a Emission Type record matches a search string or false otherwise', () => {

            // Define a couple of mock Emission Types
            let emissionType1: EmissionType = new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 });
            let emissionType2: EmissionType = new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 });
            let emissionType3: EmissionType = new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 });


            expect(emissionTypesTableDataService.matches(emissionType1, "3")).toBeFalse(); 
            expect(emissionTypesTableDataService.matches(emissionType2, "3")).toBeFalse(); 
            expect(emissionTypesTableDataService.matches(emissionType3, "3")).toBeTrue();
        }); 

        it('should index Emission Types records', () => {

            // Define a couple of mock Emission Types

            const unIndexedMockEmissionTypes = [
                new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
            ]; 
            
            const indexedMockEmissionTypes = [
                new EmissionType({ pos: 1, id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                new EmissionType({ pos: 2, id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                new EmissionType({ pos: 3, id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
            ];             


            expect(emissionTypesTableDataService.index(unIndexedMockEmissionTypes)).toEqual(indexedMockEmissionTypes);            
        }); 


        it('should paginate Emission Types records by page and page sizes', () => {

            // Define a couple of mock Emission Types

            const unpaginatedMockEmissionTypes = [
                new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
            ]; 
            
            const paginatedMockEmissionTypes = [
                new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 })
            ];             


            expect(emissionTypesTableDataService.paginate(unpaginatedMockEmissionTypes, 1, 2)).toEqual(paginatedMockEmissionTypes);            
        });         

    });
});
