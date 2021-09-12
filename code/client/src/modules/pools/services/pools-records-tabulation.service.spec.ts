
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Pool } from '../models/pool.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { PoolsDataService } from './pools-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';

import { PoolsRecordsTabulationService } from './pools-records-tabulation.service';
import { environment } from 'environments/environment';

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


    describe('get pools', () => {

 
        const pools = [
            new Pool({ id: 3, name: "Pool 3", version: 1 }),
            new Pool({ id: 1, name: "Pool 1", version: 1 }),
            new Pool({ id: 2, name: "Pool 2", version: 1 })
        ];        

        it('should return an observable containing Pools Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedPools = [
                new Pool({ id: 1, name: "Pool 1", version: 1 }),
                new Pool({ id: 2, name: "Pool 2", version: 1 }),
                new Pool({ id: 3, name: "Pool 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools/all`).flush(pools);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to Pools updates and verify the results
            poolsTableDataService.pools$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(sortedPools);
                });



                

        });


    });



    describe('get total', () => {

        const pools = [
            new Pool({ id: 3, name: "Pool 3", version: 1 }),
            new Pool({ id: 1, name: "Pool 1", version: 1 }),
            new Pool({ id: 2, name: "Pool 2", version: 1 })
        ];

        it('should return an observable containing the total number of Pools Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools/all`).flush(pools);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            poolsTableDataService.total$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(3);
                });


        });


    });    


    describe('get loading', () => {

        it('should return an observable indicating whether or not a data operation exercise (sorting, searching etc.) is currently underway', () => { 

            // Subscribe to loading updates and verify the results
            poolsTableDataService.loading$
                .pipe(
                    take(4),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(4)
                    expect(response[0]).toEqual(false);
                    expect(response[1]).toEqual(true);
                    expect(response[2]).toEqual(false);
                });
        });


    });   
    
    
    describe('get page', () => {

        it('should return the currently active page', () => { 
            expect(poolsTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(poolsTableDataService, 'set').and.callThrough();
            poolsTableDataService.page = 2;
            expect(poolsTableDataService.set).toHaveBeenCalled();
            expect(poolsTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(poolsTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(poolsTableDataService, 'set').and.callThrough();
            poolsTableDataService.page = 10;
            expect(poolsTableDataService.set).toHaveBeenCalled();
            expect(poolsTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(poolsTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(poolsTableDataService, 'set').and.callThrough();
            poolsTableDataService.searchTerm = "test";
            expect(poolsTableDataService.set).toHaveBeenCalled();
            expect(poolsTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(poolsTableDataService, 'set');
            poolsTableDataService.sortColumn = "name";
            expect(poolsTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 


    describe('compare', () => {

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


    }); 
    
    

    describe('sort', () => {

        const unsortedMockPools = [
            new Pool({ id: 2, name: "Pool 2", version: 1 }),
            new Pool({ id: 3, name: "Pool 3", version: 1 }),
            new Pool({ id: 1, name: "Pool 1", version: 1 }),                
        ];

        const sortedMockPools1 = [
            new Pool({ id: 1, name: "Pool 1", version: 1 }),
            new Pool({ id: 2, name: "Pool 2", version: 1 }),
            new Pool({ id: 3, name: "Pool 3", version: 1 })
        ];  
        
        const sortedMockPools2 = [
            new Pool({ id: 3, name: "Pool 3", version: 1 }),                
            new Pool({ id: 2, name: "Pool 2", version: 1 }),
            new Pool({ id: 1, name: "Pool 1", version: 1 }),                
        ];   

        it('should sort Pools Records by table column in ascending or descending order', () => {

            expect(poolsTableDataService.sort(unsortedMockPools,"name", "asc")).toEqual(sortedMockPools1);
            expect(poolsTableDataService.sort(unsortedMockPools,"name", "desc")).toEqual(sortedMockPools2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a Pool record matches a search string or false otherwise', () => {

            // Define a couple of Mock Pools
            let pool1: Pool = new Pool({ id: 1, name: "Pool 1", version: 1 });
            let pool2: Pool = new Pool({ id: 2, name: "Pool 2", version: 1 });
            let pool3: Pool = new Pool({ id: 3, name: "Pool 3", version: 1 });


            expect(poolsTableDataService.matches(pool1, "3")).toBeFalse(); 
            expect(poolsTableDataService.matches(pool2, "3")).toBeFalse(); 
            expect(poolsTableDataService.matches(pool3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockPools = [
            new Pool({ id: 1, name: "Pool 1", version: 1 }),
            new Pool({ id: 2, name: "Pool 2", version: 1 }),
            new Pool({ id: 3, name: "Pool 3", version: 1 })
        ]; 
        
        const indexedMockPools = [
            new Pool({ pos: 1, id: 1, name: "Pool 1", version: 1 }),
            new Pool({ pos: 2, id: 2, name: "Pool 2", version: 1 }),
            new Pool({ pos: 3, id: 3, name: "Pool 3", version: 1 })
        ];             


        it('should index Pools Records', () => {
            expect(poolsTableDataService.index(unIndexedMockPools)).toEqual(indexedMockPools);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockPools = [
            new Pool({ id: 1, name: "Pool 1", version: 1 }),
            new Pool({ id: 2, name: "Pool 2", version: 1 }),
            new Pool({ id: 3, name: "Pool 3", version: 1 })
        ]; 
        
        const paginatedMockPools = [
            new Pool({ id: 1, name: "Pool 1", version: 1 }),
            new Pool({ id: 2, name: "Pool 2", version: 1 })
        ];         

        it('should paginate Pools Records by page and page sizes', () => {
            expect(poolsTableDataService.paginate(unpaginatedMockPools, 1, 2)).toEqual(paginatedMockPools);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(poolsTableDataService, 'transform');
            poolsTableDataService.set({})
            expect(poolsTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const pools = [
            new Pool({ id: 3, name: "Pool 3", version: 1 }),
            new Pool({ id: 1, name: "Pool 1", version: 1 }),
            new Pool({ id: 2, name: "Pool 2", version: 1 }) 
        ];        

        it('should sorts, filter and paginate Pools Records', () => {

            spyOn(poolsTableDataService, 'sort').and.callThrough();;
            spyOn(poolsTableDataService, 'matches').and.callThrough();
            spyOn(poolsTableDataService, 'index').and.callThrough();
            spyOn(poolsTableDataService, 'paginate').and.callThrough();

            poolsTableDataService.transform(pools);

            expect(poolsTableDataService.sort).toHaveBeenCalled();
            expect(poolsTableDataService.matches).toHaveBeenCalled();
            expect(poolsTableDataService.index).toHaveBeenCalled();
            expect(poolsTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
