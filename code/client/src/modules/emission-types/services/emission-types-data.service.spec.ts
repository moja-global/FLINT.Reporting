import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { EmissionTypesDataService } from './emission-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { EmissionType } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('Emission Types Data Service', () => {

    let emissionTypesDataService: EmissionTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [EmissionTypesDataService, MessageService, ConnectivityStatusService],
        });

    
        emissionTypesDataService = TestBed.inject(EmissionTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createEmissionType', () => {
        it('should create and adds an instance of a new Emission Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionTypesDataService],
                (httpMock: HttpTestingController, emissionTypesDataService: EmissionTypesDataService) => {

          
                    // Define a couple of mock Emission Types
                    const initialEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    const newEmissionType = new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 });
                    
                    const finalEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                        new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createEmissionType() method
                    emissionTypesDataService.createEmissionType(new EmissionType({ name: "EmissionType 4", description: "EmissionType 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Emission Type - i.e with the id / version upated
                            expect(response).toEqual(newEmissionType);
                        });


                    // Subscribe to the Emission Types observable
                    emissionTypesDataService.emissionTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Emission Types
                            expect(response[1]).toEqual(initialEmissionTypes);                            

                            // Expect that the third response is equal to the final Emission Types
                            expect(response[2]).toEqual(finalEmissionTypes);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/all`).flush(initialEmissionTypes);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newEmissionType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getEmissionType', () => {
        it('should retrieve and add a single Emission Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionTypesDataService],
                (httpMock: HttpTestingController, emissionTypesDataService: EmissionTypesDataService) => {

                    // Define a couple of mock Emission Types
                    const allEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    const targetEmissionType = new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 });

                    // Call the getEmissionType() method
                    emissionTypesDataService.getEmissionType(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Emission Type
                            expect(response).toEqual(targetEmissionType);
                        });

                    // Subscribe to the Emission Types observable
                    emissionTypesDataService.emissionTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Emission Types
                            expect(response[1]).toEqual(allEmissionTypes);                            

                            // Expect that the third response is equal to all the Emission Types
                            expect(response[2]).toEqual(allEmissionTypes);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/all`).flush(allEmissionTypes);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetEmissionType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllEmissionTypes', () => {

        it('should retrieve and add all or a subset of all Emission Types records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionTypesDataService],
                (httpMock: HttpTestingController, emissionTypesDataService: EmissionTypesDataService) => {

                    // Define a couple of mock Emission Types
                    const allEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    // Call the getAllEmissionTypes() method
                    emissionTypesDataService.getAllEmissionTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionType
                            expect(response).toEqual(allEmissionTypes);
                        });

                    // Subscribe to the Emission Types observable
                    emissionTypesDataService.emissionTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Emission Types
                            expect(response[1]).toEqual(allEmissionTypes);                            

                            // Expect that the third response is equal to all the Emission Types
                            expect(response[2]).toEqual(allEmissionTypes);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/emission_types/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allEmissionTypes);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allEmissionTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateEmissionType', () => {
        it('should update a single Emission Type record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionTypesDataService],
                (httpMock: HttpTestingController, emissionTypesDataService: EmissionTypesDataService) => {

                    // Define a couple of mock Emission Types
                    const initialEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                        new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 })
                    ];  

                    const updatedEmissionType = new EmissionType({ id: 4, name: "Updated EmissionType Four", description: "Updated EmissionType Four Description", version: 2 });
                    
                    const finalEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                        new EmissionType({ id: 4, name: "Updated EmissionType Four", description: "Updated EmissionType Four Description", version: 2 })
                    ];                     


                    // Call the updateEmissionType() method
                    emissionTypesDataService.updateEmissionType(new EmissionType({ id: 4, name: "Updated EmissionType Four", description: "Updated EmissionType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Emission Type
                            expect(response).toEqual(updatedEmissionType);
                        });

                    // Subscribe to the Emission Types observable
                    emissionTypesDataService.emissionTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Emission Types
                            expect(response[1]).toEqual(initialEmissionTypes);                            

                            // Expect that the third response is equal to the final Emission Types
                            expect(response[2]).toEqual(finalEmissionTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/all`).flush(initialEmissionTypes);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedEmissionType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteEmissionType', () => {
        it('should delete a single Emission Type record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionTypesDataService],
                (httpMock: HttpTestingController, emissionTypesDataService: EmissionTypesDataService) => {

                    // Define a couple of mock Emission Types
                    const initialEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                        new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 })
                    ];  

                    const targetEmissionType = new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 });

                    const finalEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    // Call deleteEmissionType() method to delete the target Emission Type from the list of Emission Types
                    emissionTypesDataService.deleteEmissionType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Emission Types
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Emission Types observable
                    emissionTypesDataService.emissionTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Emission Types
                            expect(response[1]).toEqual(initialEmissionTypes);                            

                            // Expect that the third response is equal to the final Emission Types
                            expect(response[2]).toEqual(finalEmissionTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/all`).flush(initialEmissionTypes);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/ids/4`);

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

        it('should retrieve the most recent Emission Types records from the local cache',
            inject(
                [HttpTestingController, EmissionTypesDataService],
                (httpMock: HttpTestingController, emissionTypesDataService: EmissionTypesDataService) => {

                    // Define a couple of mock Emission Types
                    const allEmissionTypes = [
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/all`).flush(allEmissionTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Emission Types
                    expect(emissionTypesDataService.records).toEqual(allEmissionTypes);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

