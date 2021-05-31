import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { LandUseSubcategoriesDataService } from './land-use-subcategories-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { LandUseSubcategory } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('LandUseSubcategories Data Service', () => {

    let landUseSubcategoriesDataService: LandUseSubcategoriesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [LandUseSubcategoriesDataService, MessageService, ConnectivityStatusService],
        });

    
        landUseSubcategoriesDataService = TestBed.inject(LandUseSubcategoriesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createLandUseSubcategory', () => {
        it('should create and adds an instance of a new Land Use Subcategory record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseSubcategoriesDataService],
                (httpMock: HttpTestingController, landUseSubcategoriesDataService: LandUseSubcategoriesDataService) => {

          
                    // Define a couple of mock LandUseSubcategories
                    const initialLandUseSubcategories: LandUseSubcategory[] = <Array<LandUseSubcategory>>[
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
                    ];

                    const newLandUseSubcategory = new LandUseSubcategory({ id: 4, name: "LandUseSubcategory 4", description: "LandUseSubcategory 4 Description", version: 1 });
                    
                    const finalLandUseSubcategories: LandUseSubcategory[] = <Array<LandUseSubcategory>>[
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 }),
                        new LandUseSubcategory({ id: 4, name: "LandUseSubcategory 4", description: "LandUseSubcategory 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createLandUseSubcategory() method
                    landUseSubcategoriesDataService.createLandUseSubcategory(new LandUseSubcategory({ name: "LandUseSubcategory 4", description: "LandUseSubcategory 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new LandUseSubcategory - i.e with the id / version upated
                            expect(response).toEqual(newLandUseSubcategory);
                        });


                    // Subscribe to the Land Use Subcategories observable
                    landUseSubcategoriesDataService.landUseSubcategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUseSubcategories
                            expect(response[1]).toEqual(initialLandUseSubcategories);                            

                            // Expect that the third response is equal to the final LandUseSubcategories
                            expect(response[2]).toEqual(finalLandUseSubcategories);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories/all`).flush(initialLandUseSubcategories);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newLandUseSubcategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getLandUseSubcategory', () => {
        it('should retrieve and add a single Land Use Subcategory record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseSubcategoriesDataService],
                (httpMock: HttpTestingController, landUseSubcategoriesDataService: LandUseSubcategoriesDataService) => {

                    // Define a couple of mock LandUseSubcategories
                    const allLandUseSubcategories: LandUseSubcategory[] = <Array<LandUseSubcategory>>[
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
                    ];

                    const targetLandUseSubcategory = new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 });

                    // Call the getLandUseSubcategory() method
                    landUseSubcategoriesDataService.getLandUseSubcategory(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target LandUseSubcategory
                            expect(response).toEqual(targetLandUseSubcategory);
                        });

                    // Subscribe to the Land Use Subcategories observable
                    landUseSubcategoriesDataService.landUseSubcategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Land Use Subcategories
                            expect(response[1]).toEqual(allLandUseSubcategories);                            

                            // Expect that the third response is equal to all the Land Use Subcategories
                            expect(response[2]).toEqual(allLandUseSubcategories);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories/all`).flush(allLandUseSubcategories);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetLandUseSubcategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllLandUseSubcategories', () => {

        it('should retrieve and add all or a subset of all Land Use Subcategories records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseSubcategoriesDataService],
                (httpMock: HttpTestingController, landUseSubcategoriesDataService: LandUseSubcategoriesDataService) => {

                    // Define a couple of mock LandUseSubcategories
                    const allLandUseSubcategories: LandUseSubcategory[] = <Array<LandUseSubcategory>>[
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
                    ];

                    // Call the getAllLandUseSubcategories() method
                    landUseSubcategoriesDataService.getAllLandUseSubcategories()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked LandUseSubcategory
                            expect(response).toEqual(allLandUseSubcategories);
                        });

                    // Subscribe to the Land Use Subcategories observable
                    landUseSubcategoriesDataService.landUseSubcategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Land Use Subcategories
                            expect(response[1]).toEqual(allLandUseSubcategories);                            

                            // Expect that the third response is equal to all the Land Use Subcategories
                            expect(response[2]).toEqual(allLandUseSubcategories);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/land_use_subcategories/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allLandUseSubcategories);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allLandUseSubcategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateLandUseSubcategory', () => {
        it('should update a single Land Use Subcategory record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseSubcategoriesDataService],
                (httpMock: HttpTestingController, landUseSubcategoriesDataService: LandUseSubcategoriesDataService) => {

                    // Define a couple of mock LandUseSubcategories
                    const initialLandUseSubcategories: LandUseSubcategory[] = <Array<LandUseSubcategory>>[
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 }),
                        new LandUseSubcategory({ id: 4, name: "LandUseSubcategory 4", description: "LandUseSubcategory 4 Description", version: 1 })
                    ];  

                    const updatedLandUseSubcategory = new LandUseSubcategory({ id: 4, name: "Updated LandUseSubcategory Four", description: "Updated LandUseSubcategory Four Description", version: 2 });
                    
                    const finalLandUseSubcategories: LandUseSubcategory[] = <Array<LandUseSubcategory>>[
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 }),
                        new LandUseSubcategory({ id: 4, name: "Updated LandUseSubcategory Four", description: "Updated LandUseSubcategory Four Description", version: 2 })
                    ];                     


                    // Call the updateLandUseSubcategory() method
                    landUseSubcategoriesDataService.updateLandUseSubcategory(new LandUseSubcategory({ id: 4, name: "Updated LandUseSubcategory Four", description: "Updated LandUseSubcategory Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated LandUseSubcategory
                            expect(response).toEqual(updatedLandUseSubcategory);
                        });

                    // Subscribe to the Land Use Subcategories observable
                    landUseSubcategoriesDataService.landUseSubcategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUseSubcategories
                            expect(response[1]).toEqual(initialLandUseSubcategories);                            

                            // Expect that the third response is equal to the final LandUseSubcategories
                            expect(response[2]).toEqual(finalLandUseSubcategories);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories/all`).flush(initialLandUseSubcategories);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedLandUseSubcategory);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteLandUseSubcategory', () => {
        it('should delete a single Land Use Subcategory record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, LandUseSubcategoriesDataService],
                (httpMock: HttpTestingController, landUseSubcategoriesDataService: LandUseSubcategoriesDataService) => {

                    // Define a couple of mock LandUseSubcategories
                    const initialLandUseSubcategories: LandUseSubcategory[] = <Array<LandUseSubcategory>>[
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 }),
                        new LandUseSubcategory({ id: 4, name: "LandUseSubcategory 4", description: "LandUseSubcategory 4 Description", version: 1 })
                    ];  

                    const targetLandUseSubcategory = new LandUseSubcategory({ id: 4, name: "LandUseSubcategory 4", description: "LandUseSubcategory 4 Description", version: 1 });

                    const finalLandUseSubcategories: LandUseSubcategory[] = <Array<LandUseSubcategory>>[
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
                    ];

                    // Call deleteLandUseSubcategory() method to delete the target LandUseSubcategory from the list of LandUseSubcategories
                    landUseSubcategoriesDataService.deleteLandUseSubcategory(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted LandUseSubcategories
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Land Use Subcategories observable
                    landUseSubcategoriesDataService.landUseSubcategories$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial LandUseSubcategories
                            expect(response[1]).toEqual(initialLandUseSubcategories);                            

                            // Expect that the third response is equal to the final LandUseSubcategories
                            expect(response[2]).toEqual(finalLandUseSubcategories);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories/all`).flush(initialLandUseSubcategories);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories/ids/4`);

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

        it('should retrieve the most recent Land Use Subcategories records from the local cache',
            inject(
                [HttpTestingController, LandUseSubcategoriesDataService],
                (httpMock: HttpTestingController, landUseSubcategoriesDataService: LandUseSubcategoriesDataService) => {

                    // Define a couple of mock LandUseSubcategories
                    const allLandUseSubcategories = [
                        new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                        new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                        new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories/all`).flush(allLandUseSubcategories);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all LandUseSubcategories
                    expect(landUseSubcategoriesDataService.records).toEqual(allLandUseSubcategories);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

