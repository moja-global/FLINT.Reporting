import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { CoverTypesDataService } from './cover-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { CoverType } from '../models';

describe('Cover Types Data Service', () => {

    let coverTypesDataService: CoverTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [CoverTypesDataService, MessageService],
        });

        coverTypesDataService = TestBed.inject(CoverTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    describe('createCoverType', () => {
        it('should create and adds an instance of a new Cover Type record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, CoverTypesDataService],
                (httpMock: HttpTestingController, coverTypesDataService: CoverTypesDataService) => {

                    // Define the mock coverType
                    const mockCoverType = new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 });

                    // Call & subscribe to the createCoverType() method
                    coverTypesDataService.createCoverType(new CoverType({ name: "CoverType 4", description: "CoverType 4 Description" }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverType - i.e with the id / version upated
                            expect(response).toEqual(mockCoverType);
                        });


                    // Subscribe to the Cover Types observable
                    coverTypesDataService.coverTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked CoverType - i.e with the id / version upated
                            expect(response[1]).toEqual([mockCoverType]);
                        });


                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types`);

                    // Expect that the request method is of type POST
                    expect(mockReq.request.method).toEqual('POST');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked CoverType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockCoverType);


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

                    // Define the mock coverType
                    const mockCoverType = new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 });

                    // Call the getCoverType() method
                    coverTypesDataService.getCoverType(2)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverType
                            expect(response).toEqual(mockCoverType);
                        });

                    // Subscribe to the Cover Types observable
                    coverTypesDataService.coverTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked CoverType
                            expect(response[1]).toEqual([mockCoverType]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/ids/2`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked CoverType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockCoverType);



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
                    const mockCoverTypes: CoverType[] = <Array<CoverType>>[
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    // Call the getAllCoverTypes() method
                    coverTypesDataService.getAllCoverTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverType
                            expect(response).toEqual(mockCoverTypes);
                        });

                    // Subscribe to the Cover Types observable
                    coverTypesDataService.coverTypes$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked CoverTypes
                            expect(response[1]).toEqual(mockCoverTypes);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/all`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked CoverTypes as an argument to the flush method so that they are returned as the response
                    mockReq.flush(mockCoverTypes);

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

                    // Define the mock coverType
                    const mockCoverType1 = new CoverType({ id: 4, name: "CoverType Four", description: "CoverType Four Description", version: 1 });
                    const mockCoverType2 = new CoverType({ id: 4, name: "Updated CoverType Four", description: "Updated CoverType Four Description", version: 2 });

                    // Call createCoverType() method to create the mock coverType 1 and add it to the list of coverTypes
                    coverTypesDataService.createCoverType(mockCoverType1)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverType 1
                            expect(response).toEqual(mockCoverType1);
                        });

                    // Mock the createCoverType()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types`).flush(mockCoverType1);

                    // Call the updateCoverType() method
                    coverTypesDataService.updateCoverType(new CoverType({ id: 4, name: "Updated CoverType Four", description: "Updated CoverType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverType 2
                            expect(response).toEqual(mockCoverType2);
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

                            // Expect that the second response is equal to the first (created) CoverType
                            expect(response[1]).toEqual([mockCoverType1]);

                            // Expect that the second response is equal to the second (updated) CoverType
                            expect(response[2]).toEqual([mockCoverType2]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types`);

                    // Expect that the request method is of type PUT
                    expect(mockReq.request.method).toEqual('PUT');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked CoverType as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockCoverType2);

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

                    // Define a mock coverType
                    const mockCoverType = new CoverType({ id: 4, name: "CoverType 4", description: "CoverType 4 Description", version: 1 });

                    // Call createCoverType() method to create the mock coverType and add it to the list of coverTypes
                    coverTypesDataService.createCoverType(mockCoverType)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverType - i.e with the id / version upated
                            expect(response).toEqual(mockCoverType);
                        });

                    // Mock the createCoverType()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types`).flush(mockCoverType);

                    // Call deleteCoverType() method to delete the mock coverType from the list of coverTypes
                    coverTypesDataService.deleteCoverType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted CoverType
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

                            // Expect that the second response is equal to the created CoverType
                            expect(response[1]).toEqual([mockCoverType]);

                            // Expect that the second response is equal to an empty array - following the deletion of the created coverType
                            expect(response[2]).toEqual([]);
                        });

                    // Mock the deleteCoverType()'s corresponding HTTP DELETE Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/ids/4`).flush(1);

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
                    const mockCoverTypes = [
                        new CoverType({ id: 1, name: "CoverType 1", description: "CoverType 1 Description", version: 1 }),
                        new CoverType({ id: 2, name: "CoverType 2", description: "CoverType 2 Description", version: 1 }),
                        new CoverType({ id: 3, name: "CoverType 3", description: "CoverType 3 Description", version: 1 })
                    ];

                    // Call the getAllCoverTypes() method
                    coverTypesDataService.getAllCoverTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked CoverTypes
                            expect(response).toEqual(mockCoverTypes);
                        });

                    // Expect that a single request has been made, which matches the provided URL
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/cover_types/all`).flush(mockCoverTypes);

                    // Expect that the response is equal to an array containing the mocked CoverTypes
                    expect(coverTypesDataService.records).toEqual(mockCoverTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });
});
