
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { Accountability } from '@modules/accountabilities/models';
import { AccountabilitiesDataService } from '@modules/accountabilities/services';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { AdministrativeUnitsFilterTabulationService } from './administrative-units-filter-tabulation.service';

describe('AdministrativeUnitsFilterTabulationService', () => {

    let accountabilitiesDataService: AccountabilitiesDataService;
    let accountabilitiesTableDataService: AdministrativeUnitsFilterTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, AccountabilitiesDataService, MessageService],
        });

        accountabilitiesDataService = TestBed.inject(AccountabilitiesDataService);
        accountabilitiesTableDataService = TestBed.inject(AdministrativeUnitsFilterTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('accountabilities$', () => {

        it('should return transformed Accountabilities', () => {

            // Define a couple of mock Accountabilities
            const mockAccountabilities = [
                new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
            ];


            accountabilitiesTableDataService.accountabilities$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockAccountabilities);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/accountabilities/all`).flush(mockAccountabilities);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(accountabilitiesTableDataService.compare("Accountability 1", "Accountability 2")).toEqual(-1);
            expect(accountabilitiesTableDataService.compare("Accountability 1", "Accountability 1")).toEqual(0);
            expect(accountabilitiesTableDataService.compare("Accountability 2", "Accountability 1")).toEqual(1);

            // numbers
            expect(accountabilitiesTableDataService.compare(1, 2)).toEqual(-1);
            expect(accountabilitiesTableDataService.compare(1, 1)).toEqual(0);
            expect(accountabilitiesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Accountabilities records by table column in ascending or descending order', () => {

            // Define a couple of mock Accountabilities

            const unsortedMockAccountabilities = [
                new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 }),
                new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),                
            ];

            const sortedMockAccountabilities1 = [
                new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
            ];  
            
            const sortedMockAccountabilities2 = [
                new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 }),                
                new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),                
            ];            


            expect(accountabilitiesTableDataService.sort(unsortedMockAccountabilities,"id", "asc")).toEqual(sortedMockAccountabilities1);
            expect(accountabilitiesTableDataService.sort(unsortedMockAccountabilities,"id", "desc")).toEqual(sortedMockAccountabilities2);

            expect(accountabilitiesTableDataService.sort(unsortedMockAccountabilities,"name", "asc")).toEqual(sortedMockAccountabilities1);
            expect(accountabilitiesTableDataService.sort(unsortedMockAccountabilities,"name", "desc")).toEqual(sortedMockAccountabilities2);
            
            expect(accountabilitiesTableDataService.sort(unsortedMockAccountabilities,"description", "asc")).toEqual(sortedMockAccountabilities1);
            expect(accountabilitiesTableDataService.sort(unsortedMockAccountabilities,"description", "desc")).toEqual(sortedMockAccountabilities2);            
        }); 
        
        
        it('should return true if a Accountability record matches a search string or false otherwise', () => {

            // Define a couple of mock Accountabilities
            let accountability1: Accountability = new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 });
            let accountability2: Accountability = new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 });
            let accountability3: Accountability = new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 });


            expect(accountabilitiesTableDataService.matches(accountability1, "3")).toBeFalse(); 
            expect(accountabilitiesTableDataService.matches(accountability2, "3")).toBeFalse(); 
            expect(accountabilitiesTableDataService.matches(accountability3, "3")).toBeTrue();
        }); 

        it('should index Accountabilities records', () => {

            // Define a couple of mock Accountabilities

            const unIndexedMockAccountabilities = [
                new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
            ]; 
            
            const indexedMockAccountabilities = [
                new Accountability({ pos: 1, id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                new Accountability({ pos: 2, id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                new Accountability({ pos: 3, id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
            ];             


            expect(accountabilitiesTableDataService.index(unIndexedMockAccountabilities)).toEqual(indexedMockAccountabilities);            
        }); 


        it('should paginate Accountabilities records by page and page sizes', () => {

            // Define a couple of mock Accountabilities

            const unpaginatedMockAccountabilities = [
                new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
            ]; 
            
            const paginatedMockAccountabilities = [
                new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 })
            ];             


            expect(accountabilitiesTableDataService.paginate(unpaginatedMockAccountabilities, 1, 2)).toEqual(paginatedMockAccountabilities);            
        });         

    });
});
