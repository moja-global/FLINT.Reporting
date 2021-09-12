import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { EmissionsTypesDataService } from './emissions-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { EmissionType } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('EmissionsTypes Data Service', () => {

    let emissionsTypesDataService: EmissionsTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [EmissionsTypesDataService, MessageService, ConnectivityStatusService],
        });

    
        emissionsTypesDataService = TestBed.inject(EmissionsTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createEmissionType', () => {
        it('should create and adds an instance of a new Emission Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionsTypesDataService],
                (httpMock: HttpTestingController, emissionsTypesDataService: EmissionsTypesDataService) => {

          
                    // Define a couple of mock EmissionsTypes
                    const initialEmissionsTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    const newEmissionType = new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 });
                    
                    const finalEmissionsTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                        new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createEmissionType() method
                    emissionsTypesDataService.createEmissionType(new EmissionType({ name: "EmissionType 4", description: "EmissionType 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new EmissionType - i.e with the id / version upated
                            expect(response).toEqual(newEmissionType);
                        });


                    // Subscribe to the EmissionsTypes observable
                    emissionsTypesDataService.emissionsTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial EmissionsTypes
                            expect(response[1]).toEqual(initialEmissionsTypes);                            

                            // Expect that the third response is equal to the final EmissionsTypes
                            expect(response[2]).toEqual(finalEmissionsTypes);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emission_types/all`).flush(initialEmissionsTypes);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emission_types`);

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


    describe('getAllEmissionsTypes', () => {

        it('should retrieve and add all or a subset of all Emissions Types Records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionsTypesDataService],
                (httpMock: HttpTestingController, emissionsTypesDataService: EmissionsTypesDataService) => {

                    // Define a couple of mock EmissionsTypes
                    const allEmissionsTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    // Call the getAllEmissionsTypes() method
                    emissionsTypesDataService.getAllEmissionsTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionType
                            expect(response).toEqual(allEmissionsTypes);
                        });

                    // Subscribe to the EmissionsTypes observable
                    emissionsTypesDataService.emissionsTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the EmissionsTypes
                            expect(response[1]).toEqual(allEmissionsTypes);                            

                            // Expect that the third response is equal to all the EmissionsTypes
                            expect(response[2]).toEqual(allEmissionsTypes);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.emissionsTypesBaseUrl}/api/v1/emission_types/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allEmissionsTypes);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allEmissionsTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateEmissionType', () => {
        it('should update a single Emission Type record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionsTypesDataService],
                (httpMock: HttpTestingController, emissionsTypesDataService: EmissionsTypesDataService) => {

                    // Define a couple of mock EmissionsTypes
                    const initialEmissionsTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                        new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 })
                    ];  

                    const updatedEmissionType = new EmissionType({ id: 4, name: "Updated EmissionType Four", description: "Updated EmissionType Four Description", version: 2 });
                    
                    const finalEmissionsTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                        new EmissionType({ id: 4, name: "Updated EmissionType Four", description: "Updated EmissionType Four Description", version: 2 })
                    ];                     


                    // Call the updateEmissionType() method
                    emissionsTypesDataService.updateEmissionType(new EmissionType({ id: 4, name: "Updated EmissionType Four", description: "Updated EmissionType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated EmissionType
                            expect(response).toEqual(updatedEmissionType);
                        });

                    // Subscribe to the EmissionsTypes observable
                    emissionsTypesDataService.emissionsTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial EmissionsTypes
                            expect(response[1]).toEqual(initialEmissionsTypes);                            

                            // Expect that the third response is equal to the final EmissionsTypes
                            expect(response[2]).toEqual(finalEmissionsTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emission_types/all`).flush(initialEmissionsTypes);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emission_types`);

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
                [HttpTestingController, EmissionsTypesDataService],
                (httpMock: HttpTestingController, emissionsTypesDataService: EmissionsTypesDataService) => {

                    // Define a couple of mock EmissionsTypes
                    const initialEmissionsTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 }),
                        new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 })
                    ];  

                    const targetEmissionType = new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 });

                    const finalEmissionsTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    // Call deleteEmissionType() method to delete the target EmissionType from the list of EmissionsTypes
                    emissionsTypesDataService.deleteEmissionType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted EmissionsTypes
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the EmissionsTypes observable
                    emissionsTypesDataService.emissionsTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial EmissionsTypes
                            expect(response[1]).toEqual(initialEmissionsTypes);                            

                            // Expect that the third response is equal to the final EmissionsTypes
                            expect(response[2]).toEqual(finalEmissionsTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emission_types/all`).flush(initialEmissionsTypes);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emission_types/ids/4`);

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

        it('should retrieve the most recent Emissions Types Records from the local cache',
            inject(
                [HttpTestingController, EmissionsTypesDataService],
                (httpMock: HttpTestingController, emissionsTypesDataService: EmissionsTypesDataService) => {

                    // Define a couple of mock EmissionsTypes
                    const allEmissionsTypes = [
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emission_types/all`).flush(allEmissionsTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all EmissionsTypes
                    expect(emissionsTypesDataService.records).toEqual(allEmissionsTypes);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

