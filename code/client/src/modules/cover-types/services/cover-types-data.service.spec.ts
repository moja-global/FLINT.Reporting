import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { CoverTypesDataService } from './cover-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { CoverType } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('Cover Types Data Service', () => {

    let coverTypesDataService: CoverTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [CoverTypesDataService, MessageService, ConnectivityStatusService],
        });

    
        coverTypesDataService = TestBed.inject(CoverTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createCoverType', () => {
        it('should create and adds an instance of a new Cover Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoverTypesDataService],
                (httpMock: HttpTestingController, coverTypesDataService: CoverTypesDataService) => {

          
                    // Define a couple of mock Cover Types
                    const initialCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    const newCoverType = new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 });
                    
                    const finalCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                        new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createCoverType() method
                    coverTypesDataService.createCoverType(new CoverType({ name: "CoverType 4", description: "CoverType 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Cover Type - i.e with the id / version upated
                            expect(response).toEqual(newCoverType);
                        });


                    // Subscribe to the Cover Types observable
                    coverTypesDataService.coverTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Cover Types
                            expect(response[1]).toEqual(initialCoverTypes);                            

                            // Expect that the third response is equal to the final Cover Types
                            expect(response[2]).toEqual(finalCoverTypes);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/all`).flush(initialCoverTypes);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newCoverType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getCoverType', () => {
        it('should retrieve and add a single Cover Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoverTypesDataService],
                (httpMock: HttpTestingController, coverTypesDataService: CoverTypesDataService) => {

                    // Define a couple of mock Cover Types
                    const allCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    const targetCoverType = new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 });

                    // Call the getCoverType() method
                    coverTypesDataService.getCoverType(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Cover Type
                            expect(response).toEqual(targetCoverType);
                        });

                    // Subscribe to the Cover Types observable
                    coverTypesDataService.coverTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Cover Types
                            expect(response[1]).toEqual(allCoverTypes);                            

                            // Expect that the third response is equal to all the Cover Types
                            expect(response[2]).toEqual(allCoverTypes);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/all`).flush(allCoverTypes);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetCoverType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllCoverTypes', () => {

        it('should retrieve and add all or a subset of all Cover Types records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoverTypesDataService],
                (httpMock: HttpTestingController, coverTypesDataService: CoverTypesDataService) => {

                    // Define a couple of mock Cover Types
                    const allCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    // Call the getAllCoverTypes() method
                    coverTypesDataService.getAllCoverTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverType
                            expect(response).toEqual(allCoverTypes);
                        });

                    // Subscribe to the Cover Types observable
                    coverTypesDataService.coverTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Cover Types
                            expect(response[1]).toEqual(allCoverTypes);                            

                            // Expect that the third response is equal to all the Cover Types
                            expect(response[2]).toEqual(allCoverTypes);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/cover_types/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allCoverTypes);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allCoverTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateCoverType', () => {
        it('should update a single Cover Type record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoverTypesDataService],
                (httpMock: HttpTestingController, coverTypesDataService: CoverTypesDataService) => {

                    // Define a couple of mock Cover Types
                    const initialCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                        new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 })
                    ];  

                    const updatedCoverType = new CoverType({ id: 4, name: "Updated CoverType Four", description: "Updated CoverType Four Description", version: 2 });
                    
                    const finalCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                        new CoverType({ id: 4, name: "Updated CoverType Four", description: "Updated CoverType Four Description", version: 2 })
                    ];                     


                    // Call the updateCoverType() method
                    coverTypesDataService.updateCoverType(new CoverType({ id: 4, name: "Updated CoverType Four", description: "Updated CoverType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Cover Type
                            expect(response).toEqual(updatedCoverType);
                        });

                    // Subscribe to the Cover Types observable
                    coverTypesDataService.coverTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Cover Types
                            expect(response[1]).toEqual(initialCoverTypes);                            

                            // Expect that the third response is equal to the final Cover Types
                            expect(response[2]).toEqual(finalCoverTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/all`).flush(initialCoverTypes);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedCoverType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteCoverType', () => {
        it('should delete a single Cover Type record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoverTypesDataService],
                (httpMock: HttpTestingController, coverTypesDataService: CoverTypesDataService) => {

                    // Define a couple of mock Cover Types
                    const initialCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                        new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 })
                    ];  

                    const targetCoverType = new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 });

                    const finalCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    // Call deleteCoverType() method to delete the target Cover Type from the list of Cover Types
                    coverTypesDataService.deleteCoverType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Cover Types
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Cover Types observable
                    coverTypesDataService.coverTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Cover Types
                            expect(response[1]).toEqual(initialCoverTypes);                            

                            // Expect that the third response is equal to the final Cover Types
                            expect(response[2]).toEqual(finalCoverTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/all`).flush(initialCoverTypes);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/ids/4`);

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

        it('should retrieve the most recent Cover Types records from the local cache',
            inject(
                [HttpTestingController, CoverTypesDataService],
                (httpMock: HttpTestingController, coverTypesDataService: CoverTypesDataService) => {

                    // Define a couple of mock Cover Types
                    const allCoverTypes = [
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/all`).flush(allCoverTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Cover Types
                    expect(coverTypesDataService.records).toEqual(allCoverTypes);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

