
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { CoverType } from '../models/cover-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { CoverTypesDataService } from './cover-types-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { CoverTypesRecordsTabulationService } from './cover-types-records-tabulation.service';

describe('CoverTypesRecordsTabulationService', () => {

    let coverTypesDataService: CoverTypesDataService;
    let coverTypesTableDataService: CoverTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, CoverTypesDataService, MessageService],
        });

        coverTypesDataService = TestBed.inject(CoverTypesDataService);
        coverTypesTableDataService = TestBed.inject(CoverTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('coverTypes$', () => {

        it('should return transformed Cover Types', () => {

            // Define a couple of mock Cover Types
            const mockCoverTypes = [
                new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
            ];


            coverTypesTableDataService.coverTypes$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockCoverTypes);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/all`).flush(mockCoverTypes);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(coverTypesTableDataService.compare("CoverType 1", "CoverType 2")).toEqual(-1);
            expect(coverTypesTableDataService.compare("CoverType 1", "CoverType 1")).toEqual(0);
            expect(coverTypesTableDataService.compare("CoverType 2", "CoverType 1")).toEqual(1);

            // numbers
            expect(coverTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(coverTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(coverTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Cover Types records by table column in ascending or descending order', () => {

            // Define a couple of mock Cover Types

            const unsortedMockCoverTypes = [
                new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),                
            ];

            const sortedMockCoverTypes1 = [
                new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
            ];  
            
            const sortedMockCoverTypes2 = [
                new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),                
                new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),                
            ];            


            expect(coverTypesTableDataService.sort(unsortedMockCoverTypes,"id", "asc")).toEqual(sortedMockCoverTypes1);
            expect(coverTypesTableDataService.sort(unsortedMockCoverTypes,"id", "desc")).toEqual(sortedMockCoverTypes2);

            expect(coverTypesTableDataService.sort(unsortedMockCoverTypes,"name", "asc")).toEqual(sortedMockCoverTypes1);
            expect(coverTypesTableDataService.sort(unsortedMockCoverTypes,"name", "desc")).toEqual(sortedMockCoverTypes2);
            
            expect(coverTypesTableDataService.sort(unsortedMockCoverTypes,"description", "asc")).toEqual(sortedMockCoverTypes1);
            expect(coverTypesTableDataService.sort(unsortedMockCoverTypes,"description", "desc")).toEqual(sortedMockCoverTypes2);            
        }); 
        
        
        it('should return true if a Cover Type record matches a search string or false otherwise', () => {

            // Define a couple of mock Cover Types
            let coverType1: CoverType = new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 });
            let coverType2: CoverType = new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 });
            let coverType3: CoverType = new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 });


            expect(coverTypesTableDataService.matches(coverType1, "3")).toBeFalse(); 
            expect(coverTypesTableDataService.matches(coverType2, "3")).toBeFalse(); 
            expect(coverTypesTableDataService.matches(coverType3, "3")).toBeTrue();
        }); 

        it('should index Cover Types records', () => {

            // Define a couple of mock Cover Types

            const unIndexedMockCoverTypes = [
                new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
            ]; 
            
            const indexedMockCoverTypes = [
                new CoverType({ pos: 1, id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                new CoverType({ pos: 2, id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                new CoverType({ pos: 3, id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
            ];             


            expect(coverTypesTableDataService.index(unIndexedMockCoverTypes)).toEqual(indexedMockCoverTypes);            
        }); 


        it('should paginate Cover Types records by page and page sizes', () => {

            // Define a couple of mock Cover Types

            const unpaginatedMockCoverTypes = [
                new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
            ]; 
            
            const paginatedMockCoverTypes = [
                new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 })
            ];             


            expect(coverTypesTableDataService.paginate(unpaginatedMockCoverTypes, 1, 2)).toEqual(paginatedMockCoverTypes);            
        });         

    });
});
