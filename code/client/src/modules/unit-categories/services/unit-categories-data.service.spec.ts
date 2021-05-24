import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { UnitCategory } from '../models';
import { ConnectivityStatusService } from '@common/services';
import { UnitCategoriesDataService } from '.';

describe('Unit Categories Data Service', () => {

    let unitCategoriesDataService: UnitCategoriesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [UnitCategoriesDataService, MessageService, ConnectivityStatusService],
        });

    
        unitCategoriesDataService = TestBed.inject(UnitCategoriesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createUnitCategory', () => {
        it('should create and adds an instance of a new Unit Category record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

          
                    // Define a couple of mock Unit Categories
                    const initialUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
                    ];

                    const newUnitCategory = new UnitCategory({ id: 4, name: "UnitCategory 4", description: "UnitCategory 4 Description", version: 1 });
                    
                    const finalUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 }),
                        new UnitCategory({ id: 4, name: "UnitCategory 4", description: "UnitCategory 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createUnitCategory() method
                    unitCategoriesDataService.createUnitCategory(new UnitCategory({ name: "UnitCategory 4", description: "UnitCategory 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Unit Category - i.e with the id / version upated
                            expect(response).toEqual(newUnitCategory);
                        });


                    // Subscribe to the Unit Categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Unit Categories
                            expect(response[1]).toEqual(initialUnitCategories);                            

                            // Expect that the third response is equal to the final Unit Categories
                            expect(response[2]).toEqual(finalUnitCategories);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/all`).flush(initialUnitCategories);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newUnitCategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getUnitCategory', () => {
        it('should retrieve and add a single Unit Category record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define a couple of mock Unit Categories
                    const allUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
                    ];

                    const targetUnitCategory = new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 });

                    // Call the getUnitCategory() method
                    unitCategoriesDataService.getUnitCategory(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Unit Category
                            expect(response).toEqual(targetUnitCategory);
                        });

                    // Subscribe to the Unit Categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Unit Categories
                            expect(response[1]).toEqual(allUnitCategories);                            

                            // Expect that the third response is equal to all the Unit Categories
                            expect(response[2]).toEqual(allUnitCategories);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/all`).flush(allUnitCategories);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetUnitCategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllUnitCategories', () => {

        it('should retrieve and add all or a subset of all Unit Categories records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define a couple of mock Unit Categories
                    const allUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
                    ];

                    // Call the getAllUnitCategories() method
                    unitCategoriesDataService.getAllUnitCategories()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked UnitCategory
                            expect(response).toEqual(allUnitCategories);
                        });

                    // Subscribe to the Unit Categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Unit Categories
                            expect(response[1]).toEqual(allUnitCategories);                            

                            // Expect that the third response is equal to all the Unit Categories
                            expect(response[2]).toEqual(allUnitCategories);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/unit_categories/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allUnitCategories);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allUnitCategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateUnitCategory', () => {
        it('should update a single Unit Category record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define a couple of mock Unit Categories
                    const initialUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 }),
                        new UnitCategory({ id: 4, name: "UnitCategory 4", description: "UnitCategory 4 Description", version: 1 })
                    ];  

                    const updatedUnitCategory = new UnitCategory({ id: 4, name: "Updated UnitCategory Four", description: "Updated UnitCategory Four Description", version: 2 });
                    
                    const finalUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 }),
                        new UnitCategory({ id: 4, name: "Updated UnitCategory Four", description: "Updated UnitCategory Four Description", version: 2 })
                    ];                     


                    // Call the updateUnitCategory() method
                    unitCategoriesDataService.updateUnitCategory(new UnitCategory({ id: 4, name: "Updated UnitCategory Four", description: "Updated UnitCategory Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Unit Category
                            expect(response).toEqual(updatedUnitCategory);
                        });

                    // Subscribe to the Unit Categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Unit Categories
                            expect(response[1]).toEqual(initialUnitCategories);                            

                            // Expect that the third response is equal to the final Unit Categories
                            expect(response[2]).toEqual(finalUnitCategories);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/all`).flush(initialUnitCategories);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedUnitCategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteUnitCategory', () => {
        it('should delete a single Unit Category record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define a couple of mock Unit Categories
                    const initialUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 }),
                        new UnitCategory({ id: 4, name: "UnitCategory 4", description: "UnitCategory 4 Description", version: 1 })
                    ];  

                    const targetUnitCategory = new UnitCategory({ id: 4, name: "UnitCategory 4", description: "UnitCategory 4 Description", version: 1 });

                    const finalUnitCategories: UnitCategory[] = <Array<UnitCategory>>[
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
                    ];

                    // Call deleteUnitCategory() method to delete the target Unit Category from the list of Unit Categories
                    unitCategoriesDataService.deleteUnitCategory(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Unit Categories
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Unit Categories observable
                    unitCategoriesDataService.unitCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Unit Categories
                            expect(response[1]).toEqual(initialUnitCategories);                            

                            // Expect that the third response is equal to the final Unit Categories
                            expect(response[2]).toEqual(finalUnitCategories);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/all`).flush(initialUnitCategories);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/ids/4`);

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

        it('should retrieve the most recent Unit Categories records from the local cache',
            inject(
                [HttpTestingController, UnitCategoriesDataService],
                (httpMock: HttpTestingController, unitCategoriesDataService: UnitCategoriesDataService) => {

                    // Define a couple of mock Unit Categories
                    const allUnitCategories = [
                        new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                        new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                        new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/all`).flush(allUnitCategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Unit Categories
                    expect(unitCategoriesDataService.records).toEqual(allUnitCategories);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

