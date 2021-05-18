import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { EmissionTypesDataService } from './emission-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { EmissionType } from '../models';

describe('Emission Types Data Service', () => {

    let emissionTypesDataService: EmissionTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [EmissionTypesDataService, MessageService],
        });

        emissionTypesDataService = TestBed.inject(EmissionTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    describe('createEmissionType', () => {
        it('should create and adds an instance of a new Emission Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, EmissionTypesDataService],
                (httpMock: HttpTestingController, emissionTypesDataService: EmissionTypesDataService) => {

                    // Define the mock emissionType
                    const mockEmissionType = new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 });

                    // Call & subscribe to the createEmissionType() method
                    emissionTypesDataService.createEmissionType(new EmissionType({ name: "EmissionType 4", description: "EmissionType 4 Description" }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionType - i.e with the id / version upated
                            expect(response).toEqual(mockEmissionType);
                        });


                    // Subscribe to the Emission Types observable
                    emissionTypesDataService.emissionTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked EmissionType - i.e with the id / version upated
                            expect(response[1]).toEqual([mockEmissionType]);
                        });


                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types`);

                    // Expect that the request method is of type POST
                    expect(mockReq.request.method).toEqual('POST');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked EmissionType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockEmissionType);


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

                    // Define the mock emissionType
                    const mockEmissionType = new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 });

                    // Call the getEmissionType() method
                    emissionTypesDataService.getEmissionType(2)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionType
                            expect(response).toEqual(mockEmissionType);
                        });

                    // Subscribe to the Emission Types observable
                    emissionTypesDataService.emissionTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked EmissionType
                            expect(response[1]).toEqual([mockEmissionType]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/ids/2`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked EmissionType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockEmissionType);



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
                    const mockEmissionTypes: EmissionType[] = <Array<EmissionType>>[
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    // Call the getAllEmissionTypes() method
                    emissionTypesDataService.getAllEmissionTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionType
                            expect(response).toEqual(mockEmissionTypes);
                        });

                    // Subscribe to the Emission Types observable
                    emissionTypesDataService.emissionTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked EmissionTypes
                            expect(response[1]).toEqual(mockEmissionTypes);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/all`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked EmissionTypes as an argument to the flush method so that they are returned as the response
                    mockReq.flush(mockEmissionTypes);

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

                    // Define the mock emissionType
                    const mockEmissionType1 = new EmissionType({ id: 4, name: "EmissionType Four", description: "EmissionType Four Description", version: 1 });
                    const mockEmissionType2 = new EmissionType({ id: 4, name: "Updated EmissionType Four", description: "Updated EmissionType Four Description", version: 2 });

                    // Call createEmissionType() method to create the mock emissionType 1 and add it to the list of emissionTypes
                    emissionTypesDataService.createEmissionType(mockEmissionType1)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionType 1
                            expect(response).toEqual(mockEmissionType1);
                        });

                    // Mock the createEmissionType()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types`).flush(mockEmissionType1);

                    // Call the updateEmissionType() method
                    emissionTypesDataService.updateEmissionType(new EmissionType({ id: 4, name: "Updated EmissionType Four", description: "Updated EmissionType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionType 2
                            expect(response).toEqual(mockEmissionType2);
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

                            // Expect that the second response is equal to the first (created) EmissionType
                            expect(response[1]).toEqual([mockEmissionType1]);

                            // Expect that the second response is equal to the second (updated) EmissionType
                            expect(response[2]).toEqual([mockEmissionType2]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types`);

                    // Expect that the request method is of type PUT
                    expect(mockReq.request.method).toEqual('PUT');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked EmissionType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockEmissionType2);

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

                    // Define a mock emissionType
                    const mockEmissionType = new EmissionType({ id: 4, name: "EmissionType 4", description: "EmissionType 4 Description", version: 1 });

                    // Call createEmissionType() method to create the mock emissionType and add it to the list of emissionTypes
                    emissionTypesDataService.createEmissionType(mockEmissionType)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionType - i.e with the id / version upated
                            expect(response).toEqual(mockEmissionType);
                        });

                    // Mock the createEmissionType()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types`).flush(mockEmissionType);

                    // Call deleteEmissionType() method to delete the mock emissionType from the list of emissionTypes
                    emissionTypesDataService.deleteEmissionType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted EmissionType
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

                            // Expect that the second response is equal to the created EmissionType
                            expect(response[1]).toEqual([mockEmissionType]);

                            // Expect that the second response is equal to an empty array - following the deletion of the created emissionType
                            expect(response[2]).toEqual([]);
                        });

                    // Mock the deleteEmissionType()'s corresponding HTTP DELETE Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/ids/4`).flush(1);

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
                    const mockEmissionTypes = [
                        new EmissionType({ id: 1, name: "EmissionType 1", description: "EmissionType 1 Description", version: 1 }),
                        new EmissionType({ id: 2, name: "EmissionType 2", description: "EmissionType 2 Description", version: 1 }),
                        new EmissionType({ id: 3, name: "EmissionType 3", description: "EmissionType 3 Description", version: 1 })
                    ];

                    // Call the getAllEmissionTypes() method
                    emissionTypesDataService.getAllEmissionTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked EmissionTypes
                            expect(response).toEqual(mockEmissionTypes);
                        });

                    // Expect that a single request has been made, which matches the provided URL
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/emission_types/all`).flush(mockEmissionTypes);

                    // Expect that the response is equal to an array containing the mocked EmissionTypes
                    expect(emissionTypesDataService.records).toEqual(mockEmissionTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });
});
