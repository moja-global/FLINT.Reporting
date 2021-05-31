
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { PartyType } from '../models/party-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { PartyTypesDataService } from './party-types-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { PartyTypesRecordsTabulationService } from './party-types-records-tabulation.service';

describe('PartyTypesRecordsTabulationService', () => {

    let partyTypesDataService: PartyTypesDataService;
    let partyTypesTableDataService: PartyTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, PartyTypesDataService, MessageService],
        });

        partyTypesDataService = TestBed.inject(PartyTypesDataService);
        partyTypesTableDataService = TestBed.inject(PartyTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('partyTypes$', () => {

        it('should return transformed Party Types', () => {

            // Define a couple of mock Party Types
            const mockPartyTypes = [
                new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
            ];


            partyTypesTableDataService.partyTypes$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockPartyTypes);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/party_types/all`).flush(mockPartyTypes);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });

        it('should filter PartyTypes records by parent party type id', () => {

            // Define a couple of mock PartyTypes

            const unfilteredMockPartyTypes = [
                new PartyType({ id: 1, parentPartyTypeId: 1, name: "PartyType 1", version: 1 }),
                new PartyType({ id: 2, parentPartyTypeId: 1, name: "PartyType 2", version: 1 }),
                new PartyType({ id: 3, parentPartyTypeId: 2, name: "PartyType 3", version: 1 })
            ]; 
            
            const filteredMockPartyTypes = [
                new PartyType({ id: 1, parentPartyTypeId: 1, name: "PartyType 1", version: 1 }),
                new PartyType({ id: 2, parentPartyTypeId: 1, name: "PartyType 2", version: 1 })
            ];             


            expect(partyTypesTableDataService.filterByParentPartyType(unfilteredMockPartyTypes, 1)).toEqual(filteredMockPartyTypes);            
        });          


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(partyTypesTableDataService.compare("PartyType 1", "PartyType 2")).toEqual(-1);
            expect(partyTypesTableDataService.compare("PartyType 1", "PartyType 1")).toEqual(0);
            expect(partyTypesTableDataService.compare("PartyType 2", "PartyType 1")).toEqual(1);

            // numbers
            expect(partyTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(partyTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(partyTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Party Types records by table column in ascending or descending order', () => {

            // Define a couple of mock Party Types

            const unsortedMockPartyTypes = [
                new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 }),
                new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),                
            ];

            const sortedMockPartyTypes1 = [
                new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
            ];  
            
            const sortedMockPartyTypes2 = [
                new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 }),                
                new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),                
            ];            


            expect(partyTypesTableDataService.sort(unsortedMockPartyTypes,"id", "asc")).toEqual(sortedMockPartyTypes1);
            expect(partyTypesTableDataService.sort(unsortedMockPartyTypes,"id", "desc")).toEqual(sortedMockPartyTypes2);

            expect(partyTypesTableDataService.sort(unsortedMockPartyTypes,"name", "asc")).toEqual(sortedMockPartyTypes1);
            expect(partyTypesTableDataService.sort(unsortedMockPartyTypes,"name", "desc")).toEqual(sortedMockPartyTypes2);
            
            expect(partyTypesTableDataService.sort(unsortedMockPartyTypes,"description", "asc")).toEqual(sortedMockPartyTypes1);
            expect(partyTypesTableDataService.sort(unsortedMockPartyTypes,"description", "desc")).toEqual(sortedMockPartyTypes2);            
        }); 
        
        
        it('should return true if a Party Type record matches a search string or false otherwise', () => {

            // Define a couple of mock Party Types
            let partyType1: PartyType = new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 });
            let partyType2: PartyType = new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 });
            let partyType3: PartyType = new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 });


            expect(partyTypesTableDataService.matches(partyType1, "3")).toBeFalse(); 
            expect(partyTypesTableDataService.matches(partyType2, "3")).toBeFalse(); 
            expect(partyTypesTableDataService.matches(partyType3, "3")).toBeTrue();
        }); 

        it('should index Party Types records', () => {

            // Define a couple of mock Party Types

            const unIndexedMockPartyTypes = [
                new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
            ]; 
            
            const indexedMockPartyTypes = [
                new PartyType({ pos: 1, id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                new PartyType({ pos: 2, id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                new PartyType({ pos: 3, id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
            ];             


            expect(partyTypesTableDataService.index(unIndexedMockPartyTypes)).toEqual(indexedMockPartyTypes);            
        }); 


        it('should paginate Party Types records by page and page sizes', () => {

            // Define a couple of mock Party Types

            const unpaginatedMockPartyTypes = [
                new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
            ]; 
            
            const paginatedMockPartyTypes = [
                new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 })
            ];             


            expect(partyTypesTableDataService.paginate(unpaginatedMockPartyTypes, 1, 2)).toEqual(paginatedMockPartyTypes);            
        });         

    });
});
