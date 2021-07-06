import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { QuantityObservationsDataService } from './quantity-observations-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { QuantityObservation } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('Quantity Observations Data Service', () => {

    let quantityObservationsDataService: QuantityObservationsDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [QuantityObservationsDataService, MessageService, ConnectivityStatusService],
        });

    
        quantityObservationsDataService = TestBed.inject(QuantityObservationsDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createQuantityObservation', () => {
        it('should create and adds an instance of a new Quantity Observation record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, QuantityObservationsDataService],
                (httpMock: HttpTestingController, quantityObservationsDataService: QuantityObservationsDataService) => {

          
                    // Define a couple of mock Quantity Observations
                    const initialQuantityObservations: QuantityObservation[] = <Array<QuantityObservation>>[
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 })
                    ];

                    const newQuantityObservation = new QuantityObservation({ id: 4, name: "QuantityObservation 4", description: "QuantityObservation 4 Description", version: 1 });
                    
                    const finalQuantityObservations: QuantityObservation[] = <Array<QuantityObservation>>[
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 }),
                        new QuantityObservation({ id: 4, name: "QuantityObservation 4", description: "QuantityObservation 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createQuantityObservation() method
                    quantityObservationsDataService.createQuantityObservation(new QuantityObservation({ name: "QuantityObservation 4", description: "QuantityObservation 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Quantity Observation - i.e with the id / version upated
                            expect(response).toEqual(newQuantityObservation);
                        });


                    // Subscribe to the Quantity Observations observable
                    quantityObservationsDataService.quantityObservations$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Quantity Observations
                            expect(response[1]).toEqual(initialQuantityObservations);                            

                            // Expect that the third response is equal to the final Quantity Observations
                            expect(response[2]).toEqual(finalQuantityObservations);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types/all`).flush(initialQuantityObservations);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newQuantityObservation);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getQuantityObservation', () => {
        it('should retrieve and add a single Quantity Observation record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, QuantityObservationsDataService],
                (httpMock: HttpTestingController, quantityObservationsDataService: QuantityObservationsDataService) => {

                    // Define a couple of mock Quantity Observations
                    const allQuantityObservations: QuantityObservation[] = <Array<QuantityObservation>>[
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 })
                    ];

                    const targetQuantityObservation = new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 });

                    // Call the getQuantityObservation() method
                    quantityObservationsDataService.getQuantityObservation(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Quantity Observation
                            expect(response).toEqual(targetQuantityObservation);
                        });

                    // Subscribe to the Quantity Observations observable
                    quantityObservationsDataService.quantityObservations$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Quantity Observations
                            expect(response[1]).toEqual(allQuantityObservations);                            

                            // Expect that the third response is equal to all the Quantity Observations
                            expect(response[2]).toEqual(allQuantityObservations);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types/all`).flush(allQuantityObservations);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetQuantityObservation);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllQuantityObservations', () => {

        it('should retrieve and add all or a subset of all Quantity Observations records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, QuantityObservationsDataService],
                (httpMock: HttpTestingController, quantityObservationsDataService: QuantityObservationsDataService) => {

                    // Define a couple of mock Quantity Observations
                    const allQuantityObservations: QuantityObservation[] = <Array<QuantityObservation>>[
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 })
                    ];

                    // Call the getAllQuantityObservations() method
                    quantityObservationsDataService.getAllQuantityObservations()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked QuantityObservation
                            expect(response).toEqual(allQuantityObservations);
                        });

                    // Subscribe to the Quantity Observations observable
                    quantityObservationsDataService.quantityObservations$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Quantity Observations
                            expect(response[1]).toEqual(allQuantityObservations);                            

                            // Expect that the third response is equal to all the Quantity Observations
                            expect(response[2]).toEqual(allQuantityObservations);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/contact_types/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allQuantityObservations);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allQuantityObservations);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateQuantityObservation', () => {
        it('should update a single Quantity Observation record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, QuantityObservationsDataService],
                (httpMock: HttpTestingController, quantityObservationsDataService: QuantityObservationsDataService) => {

                    // Define a couple of mock Quantity Observations
                    const initialQuantityObservations: QuantityObservation[] = <Array<QuantityObservation>>[
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 }),
                        new QuantityObservation({ id: 4, name: "QuantityObservation 4", description: "QuantityObservation 4 Description", version: 1 })
                    ];  

                    const updatedQuantityObservation = new QuantityObservation({ id: 4, name: "Updated QuantityObservation Four", description: "Updated QuantityObservation Four Description", version: 2 });
                    
                    const finalQuantityObservations: QuantityObservation[] = <Array<QuantityObservation>>[
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 }),
                        new QuantityObservation({ id: 4, name: "Updated QuantityObservation Four", description: "Updated QuantityObservation Four Description", version: 2 })
                    ];                     


                    // Call the updateQuantityObservation() method
                    quantityObservationsDataService.updateQuantityObservation(new QuantityObservation({ id: 4, name: "Updated QuantityObservation Four", description: "Updated QuantityObservation Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Quantity Observation
                            expect(response).toEqual(updatedQuantityObservation);
                        });

                    // Subscribe to the Quantity Observations observable
                    quantityObservationsDataService.quantityObservations$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Quantity Observations
                            expect(response[1]).toEqual(initialQuantityObservations);                            

                            // Expect that the third response is equal to the final Quantity Observations
                            expect(response[2]).toEqual(finalQuantityObservations);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types/all`).flush(initialQuantityObservations);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedQuantityObservation);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteQuantityObservation', () => {
        it('should delete a single Quantity Observation record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, QuantityObservationsDataService],
                (httpMock: HttpTestingController, quantityObservationsDataService: QuantityObservationsDataService) => {

                    // Define a couple of mock Quantity Observations
                    const initialQuantityObservations: QuantityObservation[] = <Array<QuantityObservation>>[
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 }),
                        new QuantityObservation({ id: 4, name: "QuantityObservation 4", description: "QuantityObservation 4 Description", version: 1 })
                    ];  

                    const targetQuantityObservation = new QuantityObservation({ id: 4, name: "QuantityObservation 4", description: "QuantityObservation 4 Description", version: 1 });

                    const finalQuantityObservations: QuantityObservation[] = <Array<QuantityObservation>>[
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 })
                    ];

                    // Call deleteQuantityObservation() method to delete the target Quantity Observation from the list of Quantity Observations
                    quantityObservationsDataService.deleteQuantityObservation(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Quantity Observations
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Quantity Observations observable
                    quantityObservationsDataService.quantityObservations$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Quantity Observations
                            expect(response[1]).toEqual(initialQuantityObservations);                            

                            // Expect that the third response is equal to the final Quantity Observations
                            expect(response[2]).toEqual(finalQuantityObservations);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types/all`).flush(initialQuantityObservations);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types/ids/4`);

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

        it('should retrieve the most recent Quantity Observations records from the local cache',
            inject(
                [HttpTestingController, QuantityObservationsDataService],
                (httpMock: HttpTestingController, quantityObservationsDataService: QuantityObservationsDataService) => {

                    // Define a couple of mock Quantity Observations
                    const allQuantityObservations = [
                        new QuantityObservation({ id: 1, name: "QuantityObservation 1", description: "QuantityObservation 1 Description", version: 1 }),
                        new QuantityObservation({ id: 2, name: "QuantityObservation 2", description: "QuantityObservation 2 Description", version: 1 }),
                        new QuantityObservation({ id: 3, name: "QuantityObservation 3", description: "QuantityObservation 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/contact_types/all`).flush(allQuantityObservations);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Quantity Observations
                    expect(quantityObservationsDataService.records).toEqual(allQuantityObservations);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

