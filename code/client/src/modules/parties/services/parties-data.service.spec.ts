import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { Party } from '../models';
import { ConnectivityStatusService } from '@common/services';
import { PartiesDataService } from '.';

describe('Parties Data Service', () => {

    let partiesDataService: PartiesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [PartiesDataService, MessageService, ConnectivityStatusService],
        });

    
        partiesDataService = TestBed.inject(PartiesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createParty', () => {
        it('should create and adds an instance of a new Party record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesDataService],
                (httpMock: HttpTestingController, partiesDataService: PartiesDataService) => {

          
                    // Define a couple of mock Parties
                    const initialParties: Party[] = <Array<Party>>[
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
                    ];

                    const newParty = new Party({ id: 4, name: "Party 4", description: "Party 4 Description", version: 1 });
                    
                    const finalParties: Party[] = <Array<Party>>[
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 }),
                        new Party({ id: 4, name: "Party 4", description: "Party 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createParty() method
                    partiesDataService.createParty(new Party({ name: "Party 4", description: "Party 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Party - i.e with the id / version upated
                            expect(response).toEqual(newParty);
                        });


                    // Subscribe to the Parties observable
                    partiesDataService.parties$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Parties
                            expect(response[1]).toEqual(initialParties);                            

                            // Expect that the third response is equal to the final Parties
                            expect(response[2]).toEqual(finalParties);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/parties/all`).flush(initialParties);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/parties`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newParty);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getParty', () => {
        it('should retrieve and add a single Party record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesDataService],
                (httpMock: HttpTestingController, partiesDataService: PartiesDataService) => {

                    // Define a couple of mock Parties
                    const allParties: Party[] = <Array<Party>>[
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
                    ];

                    const targetPartyType = new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 });

                    // Call the getParty() method
                    partiesDataService.getParty(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Party
                            expect(response).toEqual(targetPartyType);
                        });

                    // Subscribe to the Parties observable
                    partiesDataService.parties$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Parties
                            expect(response[1]).toEqual(allParties);                            

                            // Expect that the third response is equal to all the Parties
                            expect(response[2]).toEqual(allParties);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/parties/all`).flush(allParties);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/parties/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetPartyType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllParties', () => {

        it('should retrieve and add all or a subset of all Parties records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesDataService],
                (httpMock: HttpTestingController, partiesDataService: PartiesDataService) => {

                    // Define a couple of mock Parties
                    const allParties: Party[] = <Array<Party>>[
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
                    ];

                    // Call the getAllParties() method
                    partiesDataService.getAllParties()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Party
                            expect(response).toEqual(allParties);
                        });

                    // Subscribe to the Parties observable
                    partiesDataService.parties$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Parties
                            expect(response[1]).toEqual(allParties);                            

                            // Expect that the third response is equal to all the Parties
                            expect(response[2]).toEqual(allParties);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/parties/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allParties);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allParties);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateParty', () => {
        it('should update a single Party record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesDataService],
                (httpMock: HttpTestingController, partiesDataService: PartiesDataService) => {

                    // Define a couple of mock Parties
                    const initialParties: Party[] = <Array<Party>>[
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 }),
                        new Party({ id: 4, name: "Party 4", description: "Party 4 Description", version: 1 })
                    ];  

                    const updatedParty = new Party({ id: 4, name: "Updated Party Four", description: "Updated Party Four Description", version: 2 });
                    
                    const finalParties: Party[] = <Array<Party>>[
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 }),
                        new Party({ id: 4, name: "Updated Party Four", description: "Updated Party Four Description", version: 2 })
                    ];                     


                    // Call the updateParty() method
                    partiesDataService.updateParty(new Party({ id: 4, name: "Updated Party Four", description: "Updated Party Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Party
                            expect(response).toEqual(updatedParty);
                        });

                    // Subscribe to the Parties observable
                    partiesDataService.parties$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Parties
                            expect(response[1]).toEqual(initialParties);                            

                            // Expect that the third response is equal to the final Parties
                            expect(response[2]).toEqual(finalParties);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/parties/all`).flush(initialParties);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/parties`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedParty);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteParty', () => {
        it('should delete a single Party record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesDataService],
                (httpMock: HttpTestingController, partiesDataService: PartiesDataService) => {

                    // Define a couple of mock Parties
                    const initialParties: Party[] = <Array<Party>>[
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 }),
                        new Party({ id: 4, name: "Party 4", description: "Party 4 Description", version: 1 })
                    ];  

                    const targetPartyType = new Party({ id: 4, name: "Party 4", description: "Party 4 Description", version: 1 });

                    const finalParties: Party[] = <Array<Party>>[
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
                    ];

                    // Call deleteParty() method to delete the target Party from the list of Parties
                    partiesDataService.deleteParty(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Parties
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Parties observable
                    partiesDataService.parties$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Parties
                            expect(response[1]).toEqual(initialParties);                            

                            // Expect that the third response is equal to the final Parties
                            expect(response[2]).toEqual(finalParties);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/parties/all`).flush(initialParties);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/parties/ids/4`);

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

        it('should retrieve the most recent Parties records from the local cache',
            inject(
                [HttpTestingController, PartiesDataService],
                (httpMock: HttpTestingController, partiesDataService: PartiesDataService) => {

                    // Define a couple of mock Parties
                    const allParties = [
                        new Party({ id: 1, name: "Party 1", description: "Party 1 Description", version: 1 }),
                        new Party({ id: 2, name: "Party 2", description: "Party 2 Description", version: 1 }),
                        new Party({ id: 3, name: "Party 3", description: "Party 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/parties/all`).flush(allParties);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Parties
                    expect(partiesDataService.records).toEqual(allParties);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

