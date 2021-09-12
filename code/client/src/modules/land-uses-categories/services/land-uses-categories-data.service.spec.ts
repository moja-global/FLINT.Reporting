import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { ConnectivityStatusService } from '@common/services';
import { LandUseCategory } from '../models/land-use-category.model';
import { LandUsesCategoriesDataService } from './land-uses-categories-data.service';

describe('LandUsesCategories Data Service', () => {

    let landUsesCategoriesDataService: LandUsesCategoriesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [LandUsesCategoriesDataService, MessageService, ConnectivityStatusService],
        });

    
        landUsesCategoriesDataService = TestBed.inject(LandUsesCategoriesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createLandUseCategory', () => {
        it('should create and adds an instance of a new Land Use Category Record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUsesCategoriesDataService],
                (httpMock: HttpTestingController, landUsesCategoriesDataService: LandUsesCategoriesDataService) => {

          
                    // Define a couple of mock LandUsesCategories
                    const initialLandUsesCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, reportingFrameworkId: 1, name: "LandUseCategory 1", version: 1 }),
                        new LandUseCategory({ id: 2, reportingFrameworkId: 2, name: "LandUseCategory 2", version: 1 }),
                        new LandUseCategory({ id: 3, reportingFrameworkId: 3, name: "LandUseCategory 3", version: 1 })
                    ];

                    const newLandUseCategory = new LandUseCategory({ id: 4, reportingFrameworkId: 4, name: "LandUseCategory 4", version: 1 });
                    
                    const finalLandUsesCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, reportingFrameworkId: 1, name: "LandUseCategory 1", version: 1 }),
                        new LandUseCategory({ id: 2, reportingFrameworkId: 2, name: "LandUseCategory 2", version: 1 }),
                        new LandUseCategory({ id: 3, reportingFrameworkId: 3, name: "LandUseCategory 3", version: 1 }),
                        new LandUseCategory({ id: 4, reportingFrameworkId: 4, name: "LandUseCategory 4", version: 1 })
                    ];                    

                    // Call & subscribe to the createLandUseCategory() method
                    landUsesCategoriesDataService.createLandUseCategory(new LandUseCategory({ name: "LandUseCategory 4"}))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new LandUseCategory - i.e with the id / version upated
                            expect(response).toEqual(newLandUseCategory);
                        });


                    // Subscribe to the LandUsesCategories observable
                    landUsesCategoriesDataService.landUsesCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUsesCategories
                            expect(response[1]).toEqual(initialLandUsesCategories);                            

                            // Expect that the third response is equal to the final LandUsesCategories
                            expect(response[2]).toEqual(finalLandUsesCategories);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories/all`).flush(initialLandUsesCategories);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories`);

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


    describe('getAllLandUsesCategories', () => {

        it('should retrieve and add all or a subset of all LandUsesCategories records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUsesCategoriesDataService],
                (httpMock: HttpTestingController, landUsesCategoriesDataService: LandUsesCategoriesDataService) => {

                    // Define a couple of mock LandUsesCategories
                    const allLandUsesCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, reportingFrameworkId: 1, name: "LandUseCategory 1", version: 1 }),
                        new LandUseCategory({ id: 2, reportingFrameworkId: 2, name: "LandUseCategory 2", version: 1 }),
                        new LandUseCategory({ id: 3, reportingFrameworkId: 3, name: "LandUseCategory 3", version: 1 })
                    ];

                    // Call the getAllLandUsesCategories() method
                    landUsesCategoriesDataService.getAllLandUsesCategories()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked LandUseCategory
                            expect(response).toEqual(allLandUsesCategories);
                        });

                    // Subscribe to the LandUsesCategories observable
                    landUsesCategoriesDataService.landUsesCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the LandUsesCategories
                            expect(response[1]).toEqual(allLandUsesCategories);                            

                            // Expect that the third response is equal to all the LandUsesCategories
                            expect(response[2]).toEqual(allLandUsesCategories);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allLandUsesCategories);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allLandUsesCategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateLandUseCategory', () => {
        it('should update a single Land Use Category Record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUsesCategoriesDataService],
                (httpMock: HttpTestingController, landUsesCategoriesDataService: LandUsesCategoriesDataService) => {

                    // Define a couple of mock LandUsesCategories
                    const initialLandUsesCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, reportingFrameworkId: 1, name: "LandUseCategory 1", version: 1 }),
                        new LandUseCategory({ id: 2, reportingFrameworkId: 2, name: "LandUseCategory 2", version: 1 }),
                        new LandUseCategory({ id: 3, reportingFrameworkId: 3, name: "LandUseCategory 3", version: 1 }),
                        new LandUseCategory({ id: 4, reportingFrameworkId: 4, name: "LandUseCategory 4", version: 1 })
                    ];  

                    const updatedLandUseCategory = new LandUseCategory({ id: 4, reportingFrameworkId: 4, name: "Updated LandUseCategory Four", description: "Updated LandUseCategory Four Description", version: 2 });
                    
                    const finalLandUsesCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, reportingFrameworkId: 1, name: "LandUseCategory 1", version: 1 }),
                        new LandUseCategory({ id: 2, reportingFrameworkId: 2, name: "LandUseCategory 2", version: 1 }),
                        new LandUseCategory({ id: 3, reportingFrameworkId: 3, name: "LandUseCategory 3", version: 1 }),
                        new LandUseCategory({ id: 4, reportingFrameworkId: 4, name: "Updated LandUseCategory Four", description: "Updated LandUseCategory Four Description", version: 2 })
                    ];                     


                    // Call the updateLandUseCategory() method
                    landUsesCategoriesDataService.updateLandUseCategory(new LandUseCategory({ id: 4, reportingFrameworkId: 4, name: "Updated LandUseCategory Four", description: "Updated LandUseCategory Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated LandUseCategory
                            expect(response).toEqual(updatedLandUseCategory);
                        });

                    // Subscribe to the LandUsesCategories observable
                    landUsesCategoriesDataService.landUsesCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUsesCategories
                            expect(response[1]).toEqual(initialLandUsesCategories);                            

                            // Expect that the third response is equal to the final LandUsesCategories
                            expect(response[2]).toEqual(finalLandUsesCategories);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories/all`).flush(initialLandUsesCategories);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories`);

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
        it('should delete a single Land Use Category Record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUsesCategoriesDataService],
                (httpMock: HttpTestingController, landUsesCategoriesDataService: LandUsesCategoriesDataService) => {

                    // Define a couple of mock LandUsesCategories
                    const initialLandUsesCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, reportingFrameworkId: 1, name: "LandUseCategory 1", version: 1 }),
                        new LandUseCategory({ id: 2, reportingFrameworkId: 2, name: "LandUseCategory 2", version: 1 }),
                        new LandUseCategory({ id: 3, reportingFrameworkId: 3, name: "LandUseCategory 3", version: 1 }),
                        new LandUseCategory({ id: 4, reportingFrameworkId: 4, name: "LandUseCategory 4", version: 1 })
                    ];  

                    const targetLandUseCategory = new LandUseCategory({ id: 4, reportingFrameworkId: 4, name: "LandUseCategory 4", version: 1 });

                    const finalLandUsesCategories: LandUseCategory[] = <Array<LandUseCategory>>[
                        new LandUseCategory({ id: 1, reportingFrameworkId: 1, name: "LandUseCategory 1", version: 1 }),
                        new LandUseCategory({ id: 2, reportingFrameworkId: 2, name: "LandUseCategory 2", version: 1 }),
                        new LandUseCategory({ id: 3, reportingFrameworkId: 3, name: "LandUseCategory 3", version: 1 })
                    ];

                    // Call deleteLandUseCategory() method to delete the target LandUseCategory from the list of LandUsesCategories
                    landUsesCategoriesDataService.deleteLandUseCategory(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted LandUsesCategories
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the LandUsesCategories observable
                    landUsesCategoriesDataService.landUsesCategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUsesCategories
                            expect(response[1]).toEqual(initialLandUsesCategories);                            

                            // Expect that the third response is equal to the final LandUsesCategories
                            expect(response[2]).toEqual(finalLandUsesCategories);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories/all`).flush(initialLandUsesCategories);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories/ids/4`);

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

        it('should retrieve the most recent LandUsesCategories records from the local cache',
            inject(
                [HttpTestingController, LandUsesCategoriesDataService],
                (httpMock: HttpTestingController, landUsesCategoriesDataService: LandUsesCategoriesDataService) => {

                    // Define a couple of mock LandUsesCategories
                    const allLandUsesCategories = [
                        new LandUseCategory({ id: 1, reportingFrameworkId: 1, name: "LandUseCategory 1", version: 1 }),
                        new LandUseCategory({ id: 2, reportingFrameworkId: 2, name: "LandUseCategory 2", version: 1 }),
                        new LandUseCategory({ id: 3, reportingFrameworkId: 3, name: "LandUseCategory 3", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories/all`).flush(allLandUsesCategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all LandUsesCategories
                    expect(landUsesCategoriesDataService.records).toEqual(allLandUsesCategories);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

