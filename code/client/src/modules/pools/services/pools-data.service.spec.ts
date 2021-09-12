import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { PoolsDataService } from './pools-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { Pool } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('Pools Data Service', () => {

    let poolsDataService: PoolsDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [PoolsDataService, MessageService, ConnectivityStatusService],
        });

    
        poolsDataService = TestBed.inject(PoolsDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createPool', () => {
        it('should create and adds an instance of a new Pool record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

          
                    // Define a couple of mock Pools
                    const initialPools: Pool[] = <Array<Pool>>[
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
                    ];

                    const newPool = new Pool({ id: 4, name: "Pool 4", description: "Pool 4 Description", version: 1 });
                    
                    const finalPools: Pool[] = <Array<Pool>>[
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 }),
                        new Pool({ id: 4, name: "Pool 4", description: "Pool 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createPool() method
                    poolsDataService.createPool(new Pool({ name: "Pool 4", description: "Pool 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Pool - i.e with the id / version upated
                            expect(response).toEqual(newPool);
                        });


                    // Subscribe to the Pools observable
                    poolsDataService.pools$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Pools
                            expect(response[1]).toEqual(initialPools);                            

                            // Expect that the third response is equal to the final Pools
                            expect(response[2]).toEqual(finalPools);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools/all`).flush(initialPools);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newPool);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });


    describe('getAllPools', () => {

        it('should retrieve and add all or a subset of all Pools Records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

                    // Define a couple of mock Pools
                    const allPools: Pool[] = <Array<Pool>>[
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
                    ];

                    // Call the getAllPools() method
                    poolsDataService.getAllPools()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Pool
                            expect(response).toEqual(allPools);
                        });

                    // Subscribe to the Pools observable
                    poolsDataService.pools$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Pools
                            expect(response[1]).toEqual(allPools);                            

                            // Expect that the third response is equal to all the Pools
                            expect(response[2]).toEqual(allPools);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.poolsBaseUrl}/api/v1/pools/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allPools);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allPools);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updatePool', () => {
        it('should update a single Pool record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

                    // Define a couple of mock Pools
                    const initialPools: Pool[] = <Array<Pool>>[
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 }),
                        new Pool({ id: 4, name: "Pool 4", description: "Pool 4 Description", version: 1 })
                    ];  

                    const updatedPool = new Pool({ id: 4, name: "Updated Pool Four", description: "Updated Pool Four Description", version: 2 });
                    
                    const finalPools: Pool[] = <Array<Pool>>[
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 }),
                        new Pool({ id: 4, name: "Updated Pool Four", description: "Updated Pool Four Description", version: 2 })
                    ];                     


                    // Call the updatePool() method
                    poolsDataService.updatePool(new Pool({ id: 4, name: "Updated Pool Four", description: "Updated Pool Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Pool
                            expect(response).toEqual(updatedPool);
                        });

                    // Subscribe to the Pools observable
                    poolsDataService.pools$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Pools
                            expect(response[1]).toEqual(initialPools);                            

                            // Expect that the third response is equal to the final Pools
                            expect(response[2]).toEqual(finalPools);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools/all`).flush(initialPools);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedPool);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deletePool', () => {
        it('should delete a single Pool record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

                    // Define a couple of mock Pools
                    const initialPools: Pool[] = <Array<Pool>>[
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 }),
                        new Pool({ id: 4, name: "Pool 4", description: "Pool 4 Description", version: 1 })
                    ];  

                    const targetPool = new Pool({ id: 4, name: "Pool 4", description: "Pool 4 Description", version: 1 });

                    const finalPools: Pool[] = <Array<Pool>>[
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
                    ];

                    // Call deletePool() method to delete the target Pool from the list of Pools
                    poolsDataService.deletePool(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Pools
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Pools observable
                    poolsDataService.pools$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Pools
                            expect(response[1]).toEqual(initialPools);                            

                            // Expect that the third response is equal to the final Pools
                            expect(response[2]).toEqual(finalPools);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools/all`).flush(initialPools);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools/ids/4`);

                    // Expect that the deletion request method was of type DELETE
                    expect(mockReq2.request.method).toEqual('DELETE');

                    // Expect that the deletion request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Resolve the deletion request
                    mockReq2.flush(1);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });


    describe('records', () => {

        it('should retrieve the most recent Pools Records from the local cache',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

                    // Define a couple of mock Pools
                    const allPools = [
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.poolsBaseUrl}/api/v1/pools/all`).flush(allPools);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Pools
                    expect(poolsDataService.records).toEqual(allPools);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

