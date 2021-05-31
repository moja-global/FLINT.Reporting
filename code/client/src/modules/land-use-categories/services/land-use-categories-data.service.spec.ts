import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { LandUseCategoriesDataService } from './land-use-categories-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { LandUseCategory } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('LandUseCategories Data Service', () => {

    let landUseCategoriesDataService: LandUseCategoriesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [LandUseCategoriesDataService, MessageService, ConnectivityStatusService],
        });

    
        landUseCategoriesDataService = TestBed.inject(LandUseCategoriesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createLandUseCategory', () => {
        it('should create and adds an instance of a new Land Use Category record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseCategoriesDataService],
                (httpMock: HttpTestingController, landUseCategoriesDataService: LandUseCategoriesDataService) => {

          
                    // Define a couple of mock LandUseCategories
                    const initialLandUseCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 })
                    ];

                    const newLandUseCategory = new LandUseCategory({ id: 4, name: "LandUseCategory 4", description: "LandUseCategory 4 Description", version: 1 });
                    
                    const finalLandUseCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 }),
                        new LandUseCategory({ id: 4, name: "LandUseCategory 4", description: "LandUseCategory 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createLandUseCategory() method
                    landUseCategoriesDataService.createLandUseCategory(new LandUseCategory({ name: "LandUseCategory 4", description: "LandUseCategory 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new LandUseCategory - i.e with the id / version upated
                            expect(response).toEqual(newLandUseCategory);
                        });


                    // Subscribe to the Land Use Categories observable
                    landUseCategoriesDataService.landUseCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUseCategories
                            expect(response[1]).toEqual(initialLandUseCategories);                            

                            // Expect that the third response is equal to the final LandUseCategories
                            expect(response[2]).toEqual(finalLandUseCategories);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories/all`).flush(initialLandUseCategories);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newLandUseCategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getLandUseCategory', () => {
        it('should retrieve and add a single Land Use Category record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseCategoriesDataService],
                (httpMock: HttpTestingController, landUseCategoriesDataService: LandUseCategoriesDataService) => {

                    // Define a couple of mock LandUseCategories
                    const allLandUseCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 })
                    ];

                    const targetLandUseCategory = new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 });

                    // Call the getLandUseCategory() method
                    landUseCategoriesDataService.getLandUseCategory(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target LandUseCategory
                            expect(response).toEqual(targetLandUseCategory);
                        });

                    // Subscribe to the Land Use Categories observable
                    landUseCategoriesDataService.landUseCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Land Use Categories
                            expect(response[1]).toEqual(allLandUseCategories);                            

                            // Expect that the third response is equal to all the Land Use Categories
                            expect(response[2]).toEqual(allLandUseCategories);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories/all`).flush(allLandUseCategories);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetLandUseCategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllLandUseCategories', () => {

        it('should retrieve and add all or a subset of all Land Use Categories records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseCategoriesDataService],
                (httpMock: HttpTestingController, landUseCategoriesDataService: LandUseCategoriesDataService) => {

                    // Define a couple of mock LandUseCategories
                    const allLandUseCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 })
                    ];

                    // Call the getAllLandUseCategories() method
                    landUseCategoriesDataService.getAllLandUseCategories()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked LandUseCategory
                            expect(response).toEqual(allLandUseCategories);
                        });

                    // Subscribe to the Land Use Categories observable
                    landUseCategoriesDataService.landUseCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Land Use Categories
                            expect(response[1]).toEqual(allLandUseCategories);                            

                            // Expect that the third response is equal to all the Land Use Categories
                            expect(response[2]).toEqual(allLandUseCategories);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/land_use_categories/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allLandUseCategories);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allLandUseCategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateLandUseCategory', () => {
        it('should update a single Land Use Category record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseCategoriesDataService],
                (httpMock: HttpTestingController, landUseCategoriesDataService: LandUseCategoriesDataService) => {

                    // Define a couple of mock LandUseCategories
                    const initialLandUseCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 }),
                        new LandUseCategory({ id: 4, name: "LandUseCategory 4", description: "LandUseCategory 4 Description", version: 1 })
                    ];  

                    const updatedLandUseCategory = new LandUseCategory({ id: 4, name: "Updated LandUseCategory Four", description: "Updated LandUseCategory Four Description", version: 2 });
                    
                    const finalLandUseCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 }),
                        new LandUseCategory({ id: 4, name: "Updated LandUseCategory Four", description: "Updated LandUseCategory Four Description", version: 2 })
                    ];                     


                    // Call the updateLandUseCategory() method
                    landUseCategoriesDataService.updateLandUseCategory(new LandUseCategory({ id: 4, name: "Updated LandUseCategory Four", description: "Updated LandUseCategory Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated LandUseCategory
                            expect(response).toEqual(updatedLandUseCategory);
                        });

                    // Subscribe to the Land Use Categories observable
                    landUseCategoriesDataService.landUseCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUseCategories
                            expect(response[1]).toEqual(initialLandUseCategories);                            

                            // Expect that the third response is equal to the final LandUseCategories
                            expect(response[2]).toEqual(finalLandUseCategories);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories/all`).flush(initialLandUseCategories);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedLandUseCategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteLandUseCategory', () => {
        it('should delete a single Land Use Category record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseCategoriesDataService],
                (httpMock: HttpTestingController, landUseCategoriesDataService: LandUseCategoriesDataService) => {

                    // Define a couple of mock LandUseCategories
                    const initialLandUseCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 }),
                        new LandUseCategory({ id: 4, name: "LandUseCategory 4", description: "LandUseCategory 4 Description", version: 1 })
                    ];  

                    const targetLandUseCategory = new LandUseCategory({ id: 4, name: "LandUseCategory 4", description: "LandUseCategory 4 Description", version: 1 });

                    const finalLandUseCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 })
                    ];

                    // Call deleteLandUseCategory() method to delete the target LandUseCategory from the list of LandUseCategories
                    landUseCategoriesDataService.deleteLandUseCategory(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted LandUseCategories
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Land Use Categories observable
                    landUseCategoriesDataService.landUseCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUseCategories
                            expect(response[1]).toEqual(initialLandUseCategories);                            

                            // Expect that the third response is equal to the final LandUseCategories
                            expect(response[2]).toEqual(finalLandUseCategories);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories/all`).flush(initialLandUseCategories);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories/ids/4`);

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

        it('should retrieve the most recent Land Use Categories records from the local cache',
            inject(
                [HttpTestingController, LandUseCategoriesDataService],
                (httpMock: HttpTestingController, landUseCategoriesDataService: LandUseCategoriesDataService) => {

                    // Define a couple of mock LandUseCategories
                    const allLandUseCategories = [
                        new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                        new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                        new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories/all`).flush(allLandUseCategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all LandUseCategories
                    expect(landUseCategoriesDataService.records).toEqual(allLandUseCategories);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

