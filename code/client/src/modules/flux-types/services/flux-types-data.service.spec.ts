import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { FluxTypesDataService } from './flux-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { FluxType } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('Flux Types Data Service', () => {

    let fluxTypesDataService: FluxTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [FluxTypesDataService, MessageService, ConnectivityStatusService],
        });

    
        fluxTypesDataService = TestBed.inject(FluxTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createFluxType', () => {
        it('should create and adds an instance of a new Flux Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, FluxTypesDataService],
                (httpMock: HttpTestingController, fluxTypesDataService: FluxTypesDataService) => {

          
                    // Define a couple of mock Flux Types
                    const initialFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
                    ];

                    const newFluxType = new FluxType({ id: 4, name: "FluxType 4", description: "FluxType 4 Description", version: 1 });
                    
                    const finalFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 }),
                        new FluxType({ id: 4, name: "FluxType 4", description: "FluxType 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createFluxType() method
                    fluxTypesDataService.createFluxType(new FluxType({ name: "FluxType 4", description: "FluxType 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Flux Type - i.e with the id / version upated
                            expect(response).toEqual(newFluxType);
                        });


                    // Subscribe to the Flux Types observable
                    fluxTypesDataService.fluxTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Flux Types
                            expect(response[1]).toEqual(initialFluxTypes);                            

                            // Expect that the third response is equal to the final Flux Types
                            expect(response[2]).toEqual(finalFluxTypes);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/all`).flush(initialFluxTypes);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newFluxType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getFluxType', () => {
        it('should retrieve and add a single Flux Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, FluxTypesDataService],
                (httpMock: HttpTestingController, fluxTypesDataService: FluxTypesDataService) => {

                    // Define a couple of mock Flux Types
                    const allFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
                    ];

                    const targetFluxType = new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 });

                    // Call the getFluxType() method
                    fluxTypesDataService.getFluxType(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Flux Type
                            expect(response).toEqual(targetFluxType);
                        });

                    // Subscribe to the Flux Types observable
                    fluxTypesDataService.fluxTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Flux Types
                            expect(response[1]).toEqual(allFluxTypes);                            

                            // Expect that the third response is equal to all the Flux Types
                            expect(response[2]).toEqual(allFluxTypes);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/all`).flush(allFluxTypes);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetFluxType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllFluxTypes', () => {

        it('should retrieve and add all or a subset of all Flux Types records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, FluxTypesDataService],
                (httpMock: HttpTestingController, fluxTypesDataService: FluxTypesDataService) => {

                    // Define a couple of mock Flux Types
                    const allFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
                    ];

                    // Call the getAllFluxTypes() method
                    fluxTypesDataService.getAllFluxTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked FluxType
                            expect(response).toEqual(allFluxTypes);
                        });

                    // Subscribe to the Flux Types observable
                    fluxTypesDataService.fluxTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Flux Types
                            expect(response[1]).toEqual(allFluxTypes);                            

                            // Expect that the third response is equal to all the Flux Types
                            expect(response[2]).toEqual(allFluxTypes);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/flux_types/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allFluxTypes);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allFluxTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateFluxType', () => {
        it('should update a single Flux Type record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, FluxTypesDataService],
                (httpMock: HttpTestingController, fluxTypesDataService: FluxTypesDataService) => {

                    // Define a couple of mock Flux Types
                    const initialFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 }),
                        new FluxType({ id: 4, name: "FluxType 4", description: "FluxType 4 Description", version: 1 })
                    ];  

                    const updatedFluxType = new FluxType({ id: 4, name: "Updated FluxType Four", description: "Updated FluxType Four Description", version: 2 });
                    
                    const finalFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 }),
                        new FluxType({ id: 4, name: "Updated FluxType Four", description: "Updated FluxType Four Description", version: 2 })
                    ];                     


                    // Call the updateFluxType() method
                    fluxTypesDataService.updateFluxType(new FluxType({ id: 4, name: "Updated FluxType Four", description: "Updated FluxType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Flux Type
                            expect(response).toEqual(updatedFluxType);
                        });

                    // Subscribe to the Flux Types observable
                    fluxTypesDataService.fluxTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Flux Types
                            expect(response[1]).toEqual(initialFluxTypes);                            

                            // Expect that the third response is equal to the final Flux Types
                            expect(response[2]).toEqual(finalFluxTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/all`).flush(initialFluxTypes);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedFluxType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteFluxType', () => {
        it('should delete a single Flux Type record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, FluxTypesDataService],
                (httpMock: HttpTestingController, fluxTypesDataService: FluxTypesDataService) => {

                    // Define a couple of mock Flux Types
                    const initialFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 }),
                        new FluxType({ id: 4, name: "FluxType 4", description: "FluxType 4 Description", version: 1 })
                    ];  

                    const targetFluxType = new FluxType({ id: 4, name: "FluxType 4", description: "FluxType 4 Description", version: 1 });

                    const finalFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
                    ];

                    // Call deleteFluxType() method to delete the target Flux Type from the list of Flux Types
                    fluxTypesDataService.deleteFluxType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Flux Types
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Flux Types observable
                    fluxTypesDataService.fluxTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Flux Types
                            expect(response[1]).toEqual(initialFluxTypes);                            

                            // Expect that the third response is equal to the final Flux Types
                            expect(response[2]).toEqual(finalFluxTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/all`).flush(initialFluxTypes);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/ids/4`);

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

        it('should retrieve the most recent Flux Types records from the local cache',
            inject(
                [HttpTestingController, FluxTypesDataService],
                (httpMock: HttpTestingController, fluxTypesDataService: FluxTypesDataService) => {

                    // Define a couple of mock Flux Types
                    const allFluxTypes = [
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/all`).flush(allFluxTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Flux Types
                    expect(fluxTypesDataService.records).toEqual(allFluxTypes);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

