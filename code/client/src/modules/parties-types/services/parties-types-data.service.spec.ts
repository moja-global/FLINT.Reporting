import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { PartiesTypesDataService } from './parties-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { PartyType } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('PartiesTypes Data Service', () => {

    let partiesTypesDataService: PartiesTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [PartiesTypesDataService, MessageService, ConnectivityStatusService],
        });

    
        partiesTypesDataService = TestBed.inject(PartiesTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createPartyType', () => {
        it('should create and adds an instance of a new PartyType record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesTypesDataService],
                (httpMock: HttpTestingController, partiesTypesDataService: PartiesTypesDataService) => {

          
                    // Define a couple of mock PartiesTypes
                    const initialPartiesTypes: PartyType[] = <Array<PartyType>>[
                        new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                        new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                        new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
                    ];

                    const newPartyType = new PartyType({ id: 4, name: "PartyType 4", description: "PartyType 4 Description", version: 1 });
                    
                    const finalPartiesTypes: PartyType[] = <Array<PartyType>>[
                        new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                        new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                        new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 }),
                        new PartyType({ id: 4, name: "PartyType 4", description: "PartyType 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createPartyType() method
                    partiesTypesDataService.createPartyType(new PartyType({ name: "PartyType 4", description: "PartyType 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new PartyType - i.e with the id / version upated
                            expect(response).toEqual(newPartyType);
                        });


                    // Subscribe to the PartiesTypes observable
                    partiesTypesDataService.partiesTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial PartiesTypes
                            expect(response[1]).toEqual(initialPartiesTypes);                            

                            // Expect that the third response is equal to the final PartiesTypes
                            expect(response[2]).toEqual(finalPartiesTypes);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types/all`).flush(initialPartiesTypes);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newPartyType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });


    describe('getAllPartiesTypes', () => {

        it('should retrieve and add all or a subset of all Parties Types Records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesTypesDataService],
                (httpMock: HttpTestingController, partiesTypesDataService: PartiesTypesDataService) => {

                    // Define a couple of mock PartiesTypes
                    const allPartiesTypes: PartyType[] = <Array<PartyType>>[
                        new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                        new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                        new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
                    ];

                    // Call the getAllPartiesTypes() method
                    partiesTypesDataService.getAllPartiesTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked PartyType
                            expect(response).toEqual(allPartiesTypes);
                        });

                    // Subscribe to the PartiesTypes observable
                    partiesTypesDataService.partiesTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the PartiesTypes
                            expect(response[1]).toEqual(allPartiesTypes);                            

                            // Expect that the third response is equal to all the PartiesTypes
                            expect(response[2]).toEqual(allPartiesTypes);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.partiesTypesBaseUrl}/api/v1/parties_types/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allPartiesTypes);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allPartiesTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updatePartyType', () => {
        it('should update a single PartyType record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesTypesDataService],
                (httpMock: HttpTestingController, partiesTypesDataService: PartiesTypesDataService) => {

                    // Define a couple of mock PartiesTypes
                    const initialPartiesTypes: PartyType[] = <Array<PartyType>>[
                        new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                        new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                        new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 }),
                        new PartyType({ id: 4, name: "PartyType 4", description: "PartyType 4 Description", version: 1 })
                    ];  

                    const updatedPartyType = new PartyType({ id: 4, name: "Updated PartyType Four", description: "Updated PartyType Four Description", version: 2 });
                    
                    const finalPartiesTypes: PartyType[] = <Array<PartyType>>[
                        new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                        new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                        new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 }),
                        new PartyType({ id: 4, name: "Updated PartyType Four", description: "Updated PartyType Four Description", version: 2 })
                    ];                     


                    // Call the updatePartyType() method
                    partiesTypesDataService.updatePartyType(new PartyType({ id: 4, name: "Updated PartyType Four", description: "Updated PartyType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated PartyType
                            expect(response).toEqual(updatedPartyType);
                        });

                    // Subscribe to the PartiesTypes observable
                    partiesTypesDataService.partiesTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial PartiesTypes
                            expect(response[1]).toEqual(initialPartiesTypes);                            

                            // Expect that the third response is equal to the final PartiesTypes
                            expect(response[2]).toEqual(finalPartiesTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types/all`).flush(initialPartiesTypes);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedPartyType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deletePartyType', () => {
        it('should delete a single PartyType record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, PartiesTypesDataService],
                (httpMock: HttpTestingController, partiesTypesDataService: PartiesTypesDataService) => {

                    // Define a couple of mock PartiesTypes
                    const initialPartiesTypes: PartyType[] = <Array<PartyType>>[
                        new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                        new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                        new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 }),
                        new PartyType({ id: 4, name: "PartyType 4", description: "PartyType 4 Description", version: 1 })
                    ];  

                    const targetPartyType = new PartyType({ id: 4, name: "PartyType 4", description: "PartyType 4 Description", version: 1 });

                    const finalPartiesTypes: PartyType[] = <Array<PartyType>>[
                        new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                        new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                        new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
                    ];

                    // Call deletePartyType() method to delete the target PartyType from the list of PartiesTypes
                    partiesTypesDataService.deletePartyType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted PartiesTypes
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the PartiesTypes observable
                    partiesTypesDataService.partiesTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial PartiesTypes
                            expect(response[1]).toEqual(initialPartiesTypes);                            

                            // Expect that the third response is equal to the final PartiesTypes
                            expect(response[2]).toEqual(finalPartiesTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types/all`).flush(initialPartiesTypes);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types/ids/4`);

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

        it('should retrieve the most recent Parties Types Records from the local cache',
            inject(
                [HttpTestingController, PartiesTypesDataService],
                (httpMock: HttpTestingController, partiesTypesDataService: PartiesTypesDataService) => {

                    // Define a couple of mock PartiesTypes
                    const allPartiesTypes = [
                        new PartyType({ id: 1, name: "PartyType 1", description: "PartyType 1 Description", version: 1 }),
                        new PartyType({ id: 2, name: "PartyType 2", description: "PartyType 2 Description", version: 1 }),
                        new PartyType({ id: 3, name: "PartyType 3", description: "PartyType 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types/all`).flush(allPartiesTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all PartiesTypes
                    expect(partiesTypesDataService.records).toEqual(allPartiesTypes);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

