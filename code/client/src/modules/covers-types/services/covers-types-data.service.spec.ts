import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { CoversTypesDataService } from './covers-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { CoverType } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('CoversTypes Data Service', () => {

    let coversTypesDataService: CoversTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [CoversTypesDataService, MessageService, ConnectivityStatusService],
        });

    
        coversTypesDataService = TestBed.inject(CoversTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createCoverType', () => {
        it('should create and adds an instance of a new Cover Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoversTypesDataService],
                (httpMock: HttpTestingController, coversTypesDataService: CoversTypesDataService) => {

          
                    // Define a couple of mock CoversTypes
                    const initialCoversTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    const newCoverType = new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 });
                    
                    const finalCoversTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                        new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createCoverType() method
                    coversTypesDataService.createCoverType(new CoverType({ name: "CoverType 4", description: "CoverType 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new CoverType - i.e with the id / version upated
                            expect(response).toEqual(newCoverType);
                        });


                    // Subscribe to the CoversTypes observable
                    coversTypesDataService.coversTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial CoversTypes
                            expect(response[1]).toEqual(initialCoversTypes);                            

                            // Expect that the third response is equal to the final CoversTypes
                            expect(response[2]).toEqual(finalCoversTypes);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/cover_types/all`).flush(initialCoversTypes);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/cover_types`);

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


    describe('getAllCoversTypes', () => {

        it('should retrieve and add all or a subset of all Covers Types Records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoversTypesDataService],
                (httpMock: HttpTestingController, coversTypesDataService: CoversTypesDataService) => {

                    // Define a couple of mock CoversTypes
                    const allCoversTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    // Call the getAllCoversTypes() method
                    coversTypesDataService.getAllCoversTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverType
                            expect(response).toEqual(allCoversTypes);
                        });

                    // Subscribe to the CoversTypes observable
                    coversTypesDataService.coversTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the CoversTypes
                            expect(response[1]).toEqual(allCoversTypes);                            

                            // Expect that the third response is equal to all the CoversTypes
                            expect(response[2]).toEqual(allCoversTypes);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.coversTypesBaseUrl}/api/v1/cover_types/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allCoversTypes);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allCoversTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateCoverType', () => {
        it('should update a single Cover Type record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoversTypesDataService],
                (httpMock: HttpTestingController, coversTypesDataService: CoversTypesDataService) => {

                    // Define a couple of mock CoversTypes
                    const initialCoversTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                        new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 })
                    ];  

                    const updatedCoverType = new CoverType({ id: 4, name: "Updated CoverType Four", description: "Updated CoverType Four Description", version: 2 });
                    
                    const finalCoversTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                        new CoverType({ id: 4, name: "Updated CoverType Four", description: "Updated CoverType Four Description", version: 2 })
                    ];                     


                    // Call the updateCoverType() method
                    coversTypesDataService.updateCoverType(new CoverType({ id: 4, name: "Updated CoverType Four", description: "Updated CoverType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated CoverType
                            expect(response).toEqual(updatedCoverType);
                        });

                    // Subscribe to the CoversTypes observable
                    coversTypesDataService.coversTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial CoversTypes
                            expect(response[1]).toEqual(initialCoversTypes);                            

                            // Expect that the third response is equal to the final CoversTypes
                            expect(response[2]).toEqual(finalCoversTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/cover_types/all`).flush(initialCoversTypes);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/cover_types`);

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
                [HttpTestingController, CoversTypesDataService],
                (httpMock: HttpTestingController, coversTypesDataService: CoversTypesDataService) => {

                    // Define a couple of mock CoversTypes
                    const initialCoversTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 }),
                        new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 })
                    ];  

                    const targetCoverType = new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 });

                    const finalCoversTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    // Call deleteCoverType() method to delete the target CoverType from the list of CoversTypes
                    coversTypesDataService.deleteCoverType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted CoversTypes
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the CoversTypes observable
                    coversTypesDataService.coversTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial CoversTypes
                            expect(response[1]).toEqual(initialCoversTypes);                            

                            // Expect that the third response is equal to the final CoversTypes
                            expect(response[2]).toEqual(finalCoversTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/cover_types/all`).flush(initialCoversTypes);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/cover_types/ids/4`);

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

        it('should retrieve the most recent Covers Types Records from the local cache',
            inject(
                [HttpTestingController, CoversTypesDataService],
                (httpMock: HttpTestingController, coversTypesDataService: CoversTypesDataService) => {

                    // Define a couple of mock CoversTypes
                    const allCoversTypes = [
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/cover_types/all`).flush(allCoversTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all CoversTypes
                    expect(coversTypesDataService.records).toEqual(allCoversTypes);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

