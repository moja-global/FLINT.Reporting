
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Pool } from '../models/pool.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { PoolsDataService } from './pools-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { PoolsRecordsTabulationService } from './pools-records-tabulation.service';

describe('PoolsRecordsTabulationService', () => {

    let poolsDataService: PoolsDataService;
    let poolsTableDataService: PoolsRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, PoolsDataService, MessageService],
        });

        poolsDataService = TestBed.inject(PoolsDataService);
        poolsTableDataService = TestBed.inject(PoolsRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('pools$', () => {

        it('should return transformed Pools', () => {

            // Define a couple of mock Pools
            const mockPools = [
                new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
            ];


            poolsTableDataService.pools$
                .pipe(
                    take(3),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockPools);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/pools/all`).flush(mockPools);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(poolsTableDataService.compare("Pool 1", "Pool 2")).toEqual(-1);
            expect(poolsTableDataService.compare("Pool 1", "Pool 1")).toEqual(0);
            expect(poolsTableDataService.compare("Pool 2", "Pool 1")).toEqual(1);

            // numbers
            expect(poolsTableDataService.compare(1, 2)).toEqual(-1);
            expect(poolsTableDataService.compare(1, 1)).toEqual(0);
            expect(poolsTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Pools records by table column in ascending or descending order', () => {

            // Define a couple of mock Pools

            const unsortedMockPools = [
                new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 }),
                new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),                
            ];

            const sortedMockPools1 = [
                new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
            ];  
            
            const sortedMockPools2 = [
                new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 }),                
                new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),                
            ];            


            expect(poolsTableDataService.sort(unsortedMockPools,"id", "asc")).toEqual(sortedMockPools1);
            expect(poolsTableDataService.sort(unsortedMockPools,"id", "desc")).toEqual(sortedMockPools2);

            expect(poolsTableDataService.sort(unsortedMockPools,"name", "asc")).toEqual(sortedMockPools1);
            expect(poolsTableDataService.sort(unsortedMockPools,"name", "desc")).toEqual(sortedMockPools2);
            
            expect(poolsTableDataService.sort(unsortedMockPools,"description", "asc")).toEqual(sortedMockPools1);
            expect(poolsTableDataService.sort(unsortedMockPools,"description", "desc")).toEqual(sortedMockPools2);            
        }); 
        
        
        it('should return true if a Pool record matches a search string or false otherwise', () => {

            // Define a couple of mock Pools
            let pool1: Pool = new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 });
            let pool2: Pool = new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 });
            let pool3: Pool = new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 });


            expect(poolsTableDataService.matches(pool1, "3")).toBeFalse(); 
            expect(poolsTableDataService.matches(pool2, "3")).toBeFalse(); 
            expect(poolsTableDataService.matches(pool3, "3")).toBeTrue();
        }); 

        it('should index Pools records', () => {

            // Define a couple of mock Pools

            const unIndexedMockPools = [
                new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
            ]; 
            
            const indexedMockPools = [
                new Pool({ pos: 1, id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                new Pool({ pos: 2, id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                new Pool({ pos: 3, id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
            ];             


            expect(poolsTableDataService.index(unIndexedMockPools)).toEqual(indexedMockPools);            
        }); 


        it('should paginate Pools records by page and page sizes', () => {

            // Define a couple of mock Pools

            const unpaginatedMockPools = [
                new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
            ]; 
            
            const paginatedMockPools = [
                new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 })
            ];             


            expect(poolsTableDataService.paginate(unpaginatedMockPools, 1, 2)).toEqual(paginatedMockPools);            
        });         

    });
});
