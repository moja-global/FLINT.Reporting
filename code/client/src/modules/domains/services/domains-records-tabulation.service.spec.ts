
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Domain } from '../models/domain.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { DomainsDataService } from './domains-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { DomainsRecordsTabulationService } from './domains-records-tabulation.service';

describe('DomainsRecordsTabulationService', () => {

    let domainsDataService: DomainsDataService;
    let domainsTableDataService: DomainsRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, DomainsDataService, MessageService],
        });

        domainsDataService = TestBed.inject(DomainsDataService);
        domainsTableDataService = TestBed.inject(DomainsRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('domains$', () => {

        it('should return transformed domains', () => {

            // Define a couple of mock domains
            const mockDomains = [
                new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),
                new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 })
            ];


            domainsTableDataService.domains$
                .pipe(
                    take(3),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockDomains);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/domains/all`).flush(mockDomains);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(domainsTableDataService.compare("Domain 1", "Domain 2")).toEqual(-1);
            expect(domainsTableDataService.compare("Domain 1", "Domain 1")).toEqual(0);
            expect(domainsTableDataService.compare("Domain 2", "Domain 1")).toEqual(1);

            // numbers
            expect(domainsTableDataService.compare(1, 2)).toEqual(-1);
            expect(domainsTableDataService.compare(1, 1)).toEqual(0);
            expect(domainsTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort domains records by table column in ascending or descending order', () => {

            // Define a couple of mock domains

            const unsortedMockDomains = [
                new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 }),
                new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),                
            ];

            const sortedMockDomains1 = [
                new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),
                new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 })
            ];  
            
            const sortedMockDomains2 = [
                new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 }),                
                new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),                
            ];            


            expect(domainsTableDataService.sort(unsortedMockDomains,"id", "asc")).toEqual(sortedMockDomains1);
            expect(domainsTableDataService.sort(unsortedMockDomains,"id", "desc")).toEqual(sortedMockDomains2);

            expect(domainsTableDataService.sort(unsortedMockDomains,"name", "asc")).toEqual(sortedMockDomains1);
            expect(domainsTableDataService.sort(unsortedMockDomains,"name", "desc")).toEqual(sortedMockDomains2);
            
            expect(domainsTableDataService.sort(unsortedMockDomains,"description", "asc")).toEqual(sortedMockDomains1);
            expect(domainsTableDataService.sort(unsortedMockDomains,"description", "desc")).toEqual(sortedMockDomains2);            
        }); 
        
        
        it('should return true if a domain record matches a search string or false otherwise', () => {

            // Define a couple of mock domains
            let domain1: Domain = new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 });
            let domain2: Domain = new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 });
            let domain3: Domain = new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 });


            expect(domainsTableDataService.matches(domain1, "3")).toBeFalse(); 
            expect(domainsTableDataService.matches(domain2, "3")).toBeFalse(); 
            expect(domainsTableDataService.matches(domain3, "3")).toBeTrue();
        }); 

        it('should index domains records', () => {

            // Define a couple of mock domains

            const unIndexedMockDomains = [
                new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),
                new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 })
            ]; 
            
            const indexedMockDomains = [
                new Domain({ pos: 1, id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),
                new Domain({ pos: 2, id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                new Domain({ pos: 3, id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 })
            ];             


            expect(domainsTableDataService.index(unIndexedMockDomains)).toEqual(indexedMockDomains);            
        }); 


        it('should paginate domains records by page and page sizes', () => {

            // Define a couple of mock domains

            const unpaginatedMockDomains = [
                new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),
                new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 })
            ]; 
            
            const paginatedMockDomains = [
                new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),
                new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 })
            ];             


            expect(domainsTableDataService.paginate(unpaginatedMockDomains, 1, 2)).toEqual(paginatedMockDomains);            
        });         

    });
});
