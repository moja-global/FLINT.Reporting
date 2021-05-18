import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { PoolsDataService } from './pools-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { Pool } from '../models';

describe('Pools Data Service', () => {

    let poolsDataService: PoolsDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [PoolsDataService, MessageService],
        });

        poolsDataService = TestBed.inject(PoolsDataService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    describe('createPool', () => {
        it('should create and adds an instance of a new Pool record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

                    // Define the mock pool
                    const mockPool = new Pool({ id: 4, name: "Pool 4", description: "Pool 4 Description", version: 1 });

                    // Call & subscribe to the createPool() method
                    poolsDataService.createPool(new Pool({ name: "Pool 4", description: "Pool 4 Description" }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Pool - i.e with the id / version upated
                            expect(response).toEqual(mockPool);
                        });


                    // Subscribe to the Pools observable
                    poolsDataService.pools$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked Pool - i.e with the id / version upated
                            expect(response[1]).toEqual([mockPool]);
                        });


                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/pools`);

                    // Expect that the request method is of type POST
                    expect(mockReq.request.method).toEqual('POST');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked Pool as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockPool);


                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('getPool', () => {
        it('should retrieve and add a single Pool record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

                    // Define the mock pool
                    const mockPool = new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 });

                    // Call the getPool() method
                    poolsDataService.getPool(2)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Pool
                            expect(response).toEqual(mockPool);
                        });

                    // Subscribe to the Pools observable
                    poolsDataService.pools$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked Pool
                            expect(response[1]).toEqual([mockPool]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/pools/ids/2`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked Pool as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockPool);



                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllPools', () => {

        it('should retrieve and add all or a subset of all Pools records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

                    // Define a couple of mock Pools
                    const mockPools: Pool[] = <Array<Pool>>[
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
                    ];

                    // Call the getAllPools() method
                    poolsDataService.getAllPools()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Pool
                            expect(response).toEqual(mockPools);
                        });

                    // Subscribe to the Pools observable
                    poolsDataService.pools$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked Pools
                            expect(response[1]).toEqual(mockPools);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/pools/all`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked Pools as an argument to the flush method so that they are returned as the response
                    mockReq.flush(mockPools);

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

                    // Define the mock pool
                    const mockPool1 = new Pool({ id: 4, name: "Pool Four", description: "Pool Four Description", version: 1 });
                    const mockPool2 = new Pool({ id: 4, name: "Updated Pool Four", description: "Updated Pool Four Description", version: 2 });

                    // Call createPool() method to create the mock pool 1 and add it to the list of pools
                    poolsDataService.createPool(mockPool1)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Pool 1
                            expect(response).toEqual(mockPool1);
                        });

                    // Mock the createPool()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/pools`).flush(mockPool1);

                    // Call the updatePool() method
                    poolsDataService.updatePool(new Pool({ id: 4, name: "Updated Pool Four", description: "Updated Pool Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Pool 2
                            expect(response).toEqual(mockPool2);
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

                            // Expect that the second response is equal to the first (created) Pool
                            expect(response[1]).toEqual([mockPool1]);

                            // Expect that the second response is equal to the second (updated) Pool
                            expect(response[2]).toEqual([mockPool2]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/pools`);

                    // Expect that the request method is of type PUT
                    expect(mockReq.request.method).toEqual('PUT');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked Pool as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockPool2);

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

                    // Define a mock pool
                    const mockPool = new Pool({ id: 4, name: "Pool 4", description: "Pool 4 Description", version: 1 });

                    // Call createPool() method to create the mock pool and add it to the list of pools
                    poolsDataService.createPool(mockPool)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Pool - i.e with the id / version upated
                            expect(response).toEqual(mockPool);
                        });

                    // Mock the createPool()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/pools`).flush(mockPool);

                    // Call deletePool() method to delete the mock pool from the list of pools
                    poolsDataService.deletePool(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Pool
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

                            // Expect that the second response is equal to the created Pool
                            expect(response[1]).toEqual([mockPool]);

                            // Expect that the second response is equal to an empty array - following the deletion of the created pool
                            expect(response[2]).toEqual([]);
                        });

                    // Mock the deletePool()'s corresponding HTTP DELETE Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/pools/ids/4`).flush(1);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });


    describe('records', () => {

        it('should retrieve the most recent Pools records from the local cache',
            inject(
                [HttpTestingController, PoolsDataService],
                (httpMock: HttpTestingController, poolsDataService: PoolsDataService) => {

                    // Define a couple of mock Pools
                    const mockPools = [
                        new Pool({ id: 1, name: "Pool 1", description: "Pool 1 Description", version: 1 }),
                        new Pool({ id: 2, name: "Pool 2", description: "Pool 2 Description", version: 1 }),
                        new Pool({ id: 3, name: "Pool 3", description: "Pool 3 Description", version: 1 })
                    ];

                    // Call the getAllPools() method
                    poolsDataService.getAllPools()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Pools
                            expect(response).toEqual(mockPools);
                        });

                    // Expect that a single request has been made, which matches the provided URL
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/pools/all`).flush(mockPools);

                    // Expect that the response is equal to an array containing the mocked Pools
                    expect(poolsDataService.records).toEqual(mockPools);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });
});
