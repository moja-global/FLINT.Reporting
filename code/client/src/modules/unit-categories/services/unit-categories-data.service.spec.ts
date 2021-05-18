import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { UnitCategoriesDataService } from './unit-categories-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { UnitCategory } from '../models';

describe('Unit categories Data Service', () => {

    let unitCategoriesDataService: UnitCategoriesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [UnitCategoriesDataService, MessageService],
        });

        unitCategoriesDataService = TestBed.inject(UnitCategoriesDataService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    describe('createUnitCategory', () => {
        it('should create and adds an instance of a new Unit category record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define the mock unitCategory
                    const mockUnitCategory = new UnitCategory({ id: 4, name: "UnitCategory 4", description: "UnitCategory 4 Description", version: 1 });

                    // Call & subscribe to the createUnitCategory() method
                    unitCategoriesDataService.createUnitCategory(new UnitCategory({ name: "UnitCategory 4", description: "UnitCategory 4 Description" }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked UnitCategory - i.e with the id / version upated
                            expect(response).toEqual(mockUnitCategory);
                        });


                    // Subscribe to the Unit categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked UnitCategory - i.e with the id / version upated
                            expect(response[1]).toEqual([mockUnitCategory]);
                        });


                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories`);

                    // Expect that the request method is of type POST
                    expect(mockReq.request.method).toEqual('POST');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked UnitCategory as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockUnitCategory);


                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('getUnitCategory', () => {
        it('should retrieve and add a single Unit category record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define the mock unitCategory
                    const mockUnitCategory = new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 });

                    // Call the getUnitCategory() method
                    unitCategoriesDataService.getUnitCategory(2)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked UnitCategory
                            expect(response).toEqual(mockUnitCategory);
                        });

                    // Subscribe to the Unit categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked UnitCategory
                            expect(response[1]).toEqual([mockUnitCategory]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/ids/2`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked UnitCategory as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockUnitCategory);



                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllUnitCategories', () => {

        it('should retrieve and add all or a subset of all Unit categories records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define a couple of mock Unit categories
                    const mockUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
                    ];

                    // Call the getAllUnitCategories() method
                    unitCategoriesDataService.getAllUnitCategories()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked UnitCategory
                            expect(response).toEqual(mockUnitCategories);
                        });

                    // Subscribe to the Unit categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked UnitCategories
                            expect(response[1]).toEqual(mockUnitCategories);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/all`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked UnitCategories as an argument to the flush method so that they are returned as the response
                    mockReq.flush(mockUnitCategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateUnitCategory', () => {
        it('should update a single Unit category record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define the mock unitCategory
                    const mockUnitCategory1 = new UnitCategory({ id: 4, name: "UnitCategory Four", description: "UnitCategory Four Description", version: 1 });
                    const mockUnitCategory2 = new UnitCategory({ id: 4, name: "Updated UnitCategory Four", description: "Updated UnitCategory Four Description", version: 2 });

                    // Call createUnitCategory() method to create the mock unitCategory 1 and add it to the list of unitCategories
                    unitCategoriesDataService.createUnitCategory(mockUnitCategory1)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked UnitCategory 1
                            expect(response).toEqual(mockUnitCategory1);
                        });

                    // Mock the createUnitCategory()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories`).flush(mockUnitCategory1);

                    // Call the updateUnitCategory() method
                    unitCategoriesDataService.updateUnitCategory(new UnitCategory({ id: 4, name: "Updated UnitCategory Four", description: "Updated UnitCategory Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked UnitCategory 2
                            expect(response).toEqual(mockUnitCategory2);
                        });

                    // Subscribe to the Unit categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the first (created) UnitCategory
                            expect(response[1]).toEqual([mockUnitCategory1]);

                            // Expect that the second response is equal to the second (updated) UnitCategory
                            expect(response[2]).toEqual([mockUnitCategory2]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories`);

                    // Expect that the request method is of type PUT
                    expect(mockReq.request.method).toEqual('PUT');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked UnitCategory as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockUnitCategory2);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteUnitCategory', () => {
        it('should delete a single Unit category record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define a mock unitCategory
                    const mockUnitCategory = new UnitCategory({ id: 4, name: "UnitCategory 4", description: "UnitCategory 4 Description", version: 1 });

                    // Call createUnitCategory() method to create the mock unitCategory and add it to the list of unitCategories
                    unitCategoriesDataService.createUnitCategory(mockUnitCategory)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked UnitCategory - i.e with the id / version upated
                            expect(response).toEqual(mockUnitCategory);
                        });

                    // Mock the createUnitCategory()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories`).flush(mockUnitCategory);

                    // Call deleteUnitCategory() method to delete the mock unitCategory from the list of unitCategories
                    unitCategoriesDataService.deleteUnitCategory(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted UnitCategory
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Unit categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the created UnitCategory
                            expect(response[1]).toEqual([mockUnitCategory]);

                            // Expect that the second response is equal to an empty array - following the deletion of the created unitCategory
                            expect(response[2]).toEqual([]);
                        });

                    // Mock the deleteUnitCategory()'s corresponding HTTP DELETE Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/ids/4`).flush(1);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });


    describe('records', () => {

        it('should retrieve the most recent Unit categories records from the local cache',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define a couple of mock Unit categories
                    const mockUnitCategories = [
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
                    ];

                    // Call the getAllUnitCategories() method
                    unitCategoriesDataService.getAllUnitCategories()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked UnitCategories
                            expect(response).toEqual(mockUnitCategories);
                        });

                    // Expect that a single request has been made, which matches the provided URL
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/all`).flush(mockUnitCategories);

                    // Expect that the response is equal to an array containing the mocked UnitCategories
                    expect(unitCategoriesDataService.records).toEqual(mockUnitCategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });
});
