import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { FluxTypesDataService } from './flux-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { FluxType } from '../models';

describe('Flux Types Data Service', () => {

    let fluxTypesDataService: FluxTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [FluxTypesDataService, MessageService],
        });

        fluxTypesDataService = TestBed.inject(FluxTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    describe('createFluxType', () => {
        it('should create and adds an instance of a new Flux Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, FluxTypesDataService],
                (httpMock: HttpTestingController, fluxTypesDataService: FluxTypesDataService) => {

                    // Define the mock fluxType
                    const mockFluxType = new FluxType({ id: 4, name: "FluxType 4", description: "FluxType 4 Description", version: 1 });

                    // Call & subscribe to the createFluxType() method
                    fluxTypesDataService.createFluxType(new FluxType({ name: "FluxType 4", description: "FluxType 4 Description" }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked FluxType - i.e with the id / version upated
                            expect(response).toEqual(mockFluxType);
                        });


                    // Subscribe to the Flux Types observable
                    fluxTypesDataService.fluxTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked FluxType - i.e with the id / version upated
                            expect(response[1]).toEqual([mockFluxType]);
                        });


                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types`);

                    // Expect that the request method is of type POST
                    expect(mockReq.request.method).toEqual('POST');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked FluxType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockFluxType);


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

                    // Define the mock fluxType
                    const mockFluxType = new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 });

                    // Call the getFluxType() method
                    fluxTypesDataService.getFluxType(2)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked FluxType
                            expect(response).toEqual(mockFluxType);
                        });

                    // Subscribe to the Flux Types observable
                    fluxTypesDataService.fluxTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked FluxType
                            expect(response[1]).toEqual([mockFluxType]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/ids/2`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked FluxType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockFluxType);



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
                    const mockFluxTypes: FluxType[] = <Array<FluxType>>[
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
                    ];

                    // Call the getAllFluxTypes() method
                    fluxTypesDataService.getAllFluxTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked FluxType
                            expect(response).toEqual(mockFluxTypes);
                        });

                    // Subscribe to the Flux Types observable
                    fluxTypesDataService.fluxTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked FluxTypes
                            expect(response[1]).toEqual(mockFluxTypes);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/all`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked FluxTypes as an argument to the flush method so that they are returned as the response
                    mockReq.flush(mockFluxTypes);

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

                    // Define the mock fluxType
                    const mockFluxType1 = new FluxType({ id: 4, name: "FluxType Four", description: "FluxType Four Description", version: 1 });
                    const mockFluxType2 = new FluxType({ id: 4, name: "Updated FluxType Four", description: "Updated FluxType Four Description", version: 2 });

                    // Call createFluxType() method to create the mock fluxType 1 and add it to the list of fluxTypes
                    fluxTypesDataService.createFluxType(mockFluxType1)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked FluxType 1
                            expect(response).toEqual(mockFluxType1);
                        });

                    // Mock the createFluxType()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types`).flush(mockFluxType1);

                    // Call the updateFluxType() method
                    fluxTypesDataService.updateFluxType(new FluxType({ id: 4, name: "Updated FluxType Four", description: "Updated FluxType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked FluxType 2
                            expect(response).toEqual(mockFluxType2);
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

                            // Expect that the second response is equal to the first (created) FluxType
                            expect(response[1]).toEqual([mockFluxType1]);

                            // Expect that the second response is equal to the second (updated) FluxType
                            expect(response[2]).toEqual([mockFluxType2]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types`);

                    // Expect that the request method is of type PUT
                    expect(mockReq.request.method).toEqual('PUT');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked FluxType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockFluxType2);

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

                    // Define a mock fluxType
                    const mockFluxType = new FluxType({ id: 4, name: "FluxType 4", description: "FluxType 4 Description", version: 1 });

                    // Call createFluxType() method to create the mock fluxType and add it to the list of fluxTypes
                    fluxTypesDataService.createFluxType(mockFluxType)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked FluxType - i.e with the id / version upated
                            expect(response).toEqual(mockFluxType);
                        });

                    // Mock the createFluxType()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types`).flush(mockFluxType);

                    // Call deleteFluxType() method to delete the mock fluxType from the list of fluxTypes
                    fluxTypesDataService.deleteFluxType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted FluxType
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

                            // Expect that the second response is equal to the created FluxType
                            expect(response[1]).toEqual([mockFluxType]);

                            // Expect that the second response is equal to an empty array - following the deletion of the created fluxType
                            expect(response[2]).toEqual([]);
                        });

                    // Mock the deleteFluxType()'s corresponding HTTP DELETE Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/ids/4`).flush(1);

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
                    const mockFluxTypes = [
                        new FluxType({ id: 1, name: "FluxType 1", description: "FluxType 1 Description", version: 1 }),
                        new FluxType({ id: 2, name: "FluxType 2", description: "FluxType 2 Description", version: 1 }),
                        new FluxType({ id: 3, name: "FluxType 3", description: "FluxType 3 Description", version: 1 })
                    ];

                    // Call the getAllFluxTypes() method
                    fluxTypesDataService.getAllFluxTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked FluxTypes
                            expect(response).toEqual(mockFluxTypes);
                        });

                    // Expect that a single request has been made, which matches the provided URL
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/flux_types/all`).flush(mockFluxTypes);

                    // Expect that the response is equal to an array containing the mocked FluxTypes
                    expect(fluxTypesDataService.records).toEqual(mockFluxTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });
});
