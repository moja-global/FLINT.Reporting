
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Party } from '../models/party.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { PartiesDataService } from './parties-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { PartiesRecordsTabulationService } from './parties-records-tabulation.service';

describe('PartiesRecordsTabulationService', () => {

    let partiesDataService: PartiesDataService;
    let partiesTableDataService: PartiesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, PartiesDataService, MessageService],
        });

        partiesDataService = TestBed.inject(PartiesDataService);
        partiesTableDataService = TestBed.inject(PartiesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('parties$', () => {

        it('should return transformed Parties', () => {

            // Define a couple of mock Parties
            const mockParties = [
                new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
            ];


            partiesTableDataService.parties$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockParties);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/parties/all`).flush(mockParties);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });

        it('should filter Parties records by parent party id', () => {

            // Define a couple of mock Parties

            const unfilteredMockParties = [
                new Party({ id: 1, partyTypeId: 1, name: "Party 1", version: 1 }),
                new Party({ id: 2, partyTypeId: 1, name: "Party 2", version: 1 }),
                new Party({ id: 3, partyTypeId: 2, name: "Party 3", version: 1 })
            ]; 
            
            const filteredMockParties = [
                new Party({ id: 1, partyTypeId: 1, name: "Party 1", version: 1 }),
                new Party({ id: 2, partyTypeId: 1, name: "Party 2", version: 1 })
            ];             


            expect(partiesTableDataService.filterByPartyType(unfilteredMockParties, 1)).toEqual(filteredMockParties);            
        });          


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(partiesTableDataService.compare("Party 1", "Party 2")).toEqual(-1);
            expect(partiesTableDataService.compare("Party 1", "Party 1")).toEqual(0);
            expect(partiesTableDataService.compare("Party 2", "Party 1")).toEqual(1);

            // numbers
            expect(partiesTableDataService.compare(1, 2)).toEqual(-1);
            expect(partiesTableDataService.compare(1, 1)).toEqual(0);
            expect(partiesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Parties records by table column in ascending or descending order', () => {

            // Define a couple of mock Parties

            const unsortedMockParties = [
                new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 }),
                new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),                
            ];

            const sortedMockParties1 = [
                new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
            ];  
            
            const sortedMockParties2 = [
                new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 }),                
                new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),                
            ];            


            expect(partiesTableDataService.sort(unsortedMockParties,"id", "asc")).toEqual(sortedMockParties1);
            expect(partiesTableDataService.sort(unsortedMockParties,"id", "desc")).toEqual(sortedMockParties2);

            expect(partiesTableDataService.sort(unsortedMockParties,"name", "asc")).toEqual(sortedMockParties1);
            expect(partiesTableDataService.sort(unsortedMockParties,"name", "desc")).toEqual(sortedMockParties2);
            
            expect(partiesTableDataService.sort(unsortedMockParties,"description", "asc")).toEqual(sortedMockParties1);
            expect(partiesTableDataService.sort(unsortedMockParties,"description", "desc")).toEqual(sortedMockParties2);            
        }); 
        
        
        it('should return true if a Party record matches a search string or false otherwise', () => {

            // Define a couple of mock Parties
            let party1: Party = new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 });
            let party2: Party = new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 });
            let party3: Party = new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 });


            expect(partiesTableDataService.matches(party1, "3")).toBeFalse(); 
            expect(partiesTableDataService.matches(party2, "3")).toBeFalse(); 
            expect(partiesTableDataService.matches(party3, "3")).toBeTrue();
        }); 

        it('should index Parties records', () => {

            // Define a couple of mock Parties

            const unIndexedMockParties = [
                new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
            ]; 
            
            const indexedMockParties = [
                new Party({ pos: 1, id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                new Party({ pos: 2, id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                new Party({ pos: 3, id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
            ];             


            expect(partiesTableDataService.index(unIndexedMockParties)).toEqual(indexedMockParties);            
        }); 


        it('should paginate Parties records by page and page sizes', () => {

            // Define a couple of mock Parties

            const unpaginatedMockParties = [
                new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
            ]; 
            
            const paginatedMockParties = [
                new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 })
            ];             


            expect(partiesTableDataService.paginate(unpaginatedMockParties, 1, 2)).toEqual(paginatedMockParties);            
        });         

    });
});
