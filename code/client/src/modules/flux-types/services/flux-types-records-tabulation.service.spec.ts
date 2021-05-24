
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { FluxType } from '../models/flux-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { FluxTypesDataService } from './flux-types-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { FluxTypesRecordsTabulationService } from './flux-types-records-tabulation.service';

describe('FluxTypesRecordsTabulationService', () => {

    let fluxTypesDataService: FluxTypesDataService;
    let fluxTypesTableDataService: FluxTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, FluxTypesDataService, MessageService],
        });

        fluxTypesDataService = TestBed.inject(FluxTypesDataService);
        fluxTypesTableDataService = TestBed.inject(FluxTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('fluxTypes$', () => {

        it('should return transformed Flux Types', () => {

            // Define a couple of mock Flux Types
            const mockFluxTypes = [
                new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
            ];


            fluxTypesTableDataService.fluxTypes$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockFluxTypes);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/all`).flush(mockFluxTypes);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(fluxTypesTableDataService.compare("FluxType 1", "FluxType 2")).toEqual(-1);
            expect(fluxTypesTableDataService.compare("FluxType 1", "FluxType 1")).toEqual(0);
            expect(fluxTypesTableDataService.compare("FluxType 2", "FluxType 1")).toEqual(1);

            // numbers
            expect(fluxTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(fluxTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(fluxTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Flux Types records by table column in ascending or descending order', () => {

            // Define a couple of mock Flux Types

            const unsortedMockFluxTypes = [
                new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 }),
                new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),                
            ];

            const sortedMockFluxTypes1 = [
                new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
            ];  
            
            const sortedMockFluxTypes2 = [
                new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 }),                
                new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),                
            ];            


            expect(fluxTypesTableDataService.sort(unsortedMockFluxTypes,"id", "asc")).toEqual(sortedMockFluxTypes1);
            expect(fluxTypesTableDataService.sort(unsortedMockFluxTypes,"id", "desc")).toEqual(sortedMockFluxTypes2);

            expect(fluxTypesTableDataService.sort(unsortedMockFluxTypes,"name", "asc")).toEqual(sortedMockFluxTypes1);
            expect(fluxTypesTableDataService.sort(unsortedMockFluxTypes,"name", "desc")).toEqual(sortedMockFluxTypes2);
            
            expect(fluxTypesTableDataService.sort(unsortedMockFluxTypes,"description", "asc")).toEqual(sortedMockFluxTypes1);
            expect(fluxTypesTableDataService.sort(unsortedMockFluxTypes,"description", "desc")).toEqual(sortedMockFluxTypes2);            
        }); 
        
        
        it('should return true if a Flux Type record matches a search string or false otherwise', () => {

            // Define a couple of mock Flux Types
            let fluxType1: FluxType = new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 });
            let fluxType2: FluxType = new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 });
            let fluxType3: FluxType = new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 });


            expect(fluxTypesTableDataService.matches(fluxType1, "3")).toBeFalse(); 
            expect(fluxTypesTableDataService.matches(fluxType2, "3")).toBeFalse(); 
            expect(fluxTypesTableDataService.matches(fluxType3, "3")).toBeTrue();
        }); 

        it('should index Flux Types records', () => {

            // Define a couple of mock Flux Types

            const unIndexedMockFluxTypes = [
                new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
            ]; 
            
            const indexedMockFluxTypes = [
                new FluxType({ pos: 1, id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                new FluxType({ pos: 2, id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                new FluxType({ pos: 3, id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
            ];             


            expect(fluxTypesTableDataService.index(unIndexedMockFluxTypes)).toEqual(indexedMockFluxTypes);            
        }); 


        it('should paginate Flux Types records by page and page sizes', () => {

            // Define a couple of mock Flux Types

            const unpaginatedMockFluxTypes = [
                new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
            ]; 
            
            const paginatedMockFluxTypes = [
                new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 })
            ];             


            expect(fluxTypesTableDataService.paginate(unpaginatedMockFluxTypes, 1, 2)).toEqual(paginatedMockFluxTypes);            
        });         

    });
});
