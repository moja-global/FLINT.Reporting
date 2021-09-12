import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { AccountabilitiesDataService } from './accountabilities-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { Accountability } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('Accountabilities Data Service', () => {

    let accountabilitiesDataService: AccountabilitiesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [AccountabilitiesDataService, MessageService, ConnectivityStatusService],
        });

    
        accountabilitiesDataService = TestBed.inject(AccountabilitiesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createAccountability', () => {
        it('should create and adds an instance of a new Accountability record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesDataService],
                (httpMock: HttpTestingController, accountabilitiesDataService: AccountabilitiesDataService) => {

          
                    // Define a couple of mock Accountabilities
                    const initialAccountabilities: Accountability[] = <Array<Accountability>>[
                        new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                        new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                        new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
                    ];

                    const newAccountability = new Accountability({ id: 4, name: "Accountability 4", description: "Accountability 4 Description", version: 1 });
                    
                    const finalAccountabilities: Accountability[] = <Array<Accountability>>[
                        new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                        new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                        new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 }),
                        new Accountability({ id: 4, name: "Accountability 4", description: "Accountability 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createAccountability() method
                    accountabilitiesDataService.createAccountability(new Accountability({ name: "Accountability 4", description: "Accountability 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Accountability - i.e with the id / version upated
                            expect(response).toEqual(newAccountability);
                        });


                    // Subscribe to the Accountabilities observable
                    accountabilitiesDataService.accountabilities$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Accountabilities
                            expect(response[1]).toEqual(initialAccountabilities);                            

                            // Expect that the third response is equal to the final Accountabilities
                            expect(response[2]).toEqual(finalAccountabilities);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesBaseUrl}/api/v1/accountabilities/all`).flush(initialAccountabilities);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesBaseUrl}/api/v1/accountabilities`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newAccountability);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });


    describe('getAllAccountabilities', () => {

        it('should retrieve and add all or a subset of all Accountabilities Records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesDataService],
                (httpMock: HttpTestingController, accountabilitiesDataService: AccountabilitiesDataService) => {

                    // Define a couple of mock Accountabilities
                    const allAccountabilities: Accountability[] = <Array<Accountability>>[
                        new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                        new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                        new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
                    ];

                    // Call the getAllAccountabilities() method
                    accountabilitiesDataService.getAllAccountabilities()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Accountability
                            expect(response).toEqual(allAccountabilities);
                        });

                    // Subscribe to the Accountabilities observable
                    accountabilitiesDataService.accountabilities$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Accountabilities
                            expect(response[1]).toEqual(allAccountabilities);                            

                            // Expect that the third response is equal to all the Accountabilities
                            expect(response[2]).toEqual(allAccountabilities);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.accountabilitiesBaseUrl}/api/v1/accountabilities/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allAccountabilities);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allAccountabilities);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateAccountability', () => {
        it('should update a single Accountability record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesDataService],
                (httpMock: HttpTestingController, accountabilitiesDataService: AccountabilitiesDataService) => {

                    // Define a couple of mock Accountabilities
                    const initialAccountabilities: Accountability[] = <Array<Accountability>>[
                        new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                        new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                        new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 }),
                        new Accountability({ id: 4, name: "Accountability 4", description: "Accountability 4 Description", version: 1 })
                    ];  

                    const updatedAccountability = new Accountability({ id: 4, name: "Updated Accountability Four", description: "Updated Accountability Four Description", version: 2 });
                    
                    const finalAccountabilities: Accountability[] = <Array<Accountability>>[
                        new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                        new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                        new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 }),
                        new Accountability({ id: 4, name: "Updated Accountability Four", description: "Updated Accountability Four Description", version: 2 })
                    ];                     


                    // Call the updateAccountability() method
                    accountabilitiesDataService.updateAccountability(new Accountability({ id: 4, name: "Updated Accountability Four", description: "Updated Accountability Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Accountability
                            expect(response).toEqual(updatedAccountability);
                        });

                    // Subscribe to the Accountabilities observable
                    accountabilitiesDataService.accountabilities$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Accountabilities
                            expect(response[1]).toEqual(initialAccountabilities);                            

                            // Expect that the third response is equal to the final Accountabilities
                            expect(response[2]).toEqual(finalAccountabilities);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesBaseUrl}/api/v1/accountabilities/all`).flush(initialAccountabilities);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesBaseUrl}/api/v1/accountabilities`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedAccountability);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteAccountability', () => {
        it('should delete a single Accountability record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesDataService],
                (httpMock: HttpTestingController, accountabilitiesDataService: AccountabilitiesDataService) => {

                    // Define a couple of mock Accountabilities
                    const initialAccountabilities: Accountability[] = <Array<Accountability>>[
                        new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                        new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                        new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 }),
                        new Accountability({ id: 4, name: "Accountability 4", description: "Accountability 4 Description", version: 1 })
                    ];  

                    const targetAccountability = new Accountability({ id: 4, name: "Accountability 4", description: "Accountability 4 Description", version: 1 });

                    const finalAccountabilities: Accountability[] = <Array<Accountability>>[
                        new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                        new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                        new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
                    ];

                    // Call deleteAccountability() method to delete the target Accountability from the list of Accountabilities
                    accountabilitiesDataService.deleteAccountability(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Accountabilities
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Accountabilities observable
                    accountabilitiesDataService.accountabilities$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Accountabilities
                            expect(response[1]).toEqual(initialAccountabilities);                            

                            // Expect that the third response is equal to the final Accountabilities
                            expect(response[2]).toEqual(finalAccountabilities);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesBaseUrl}/api/v1/accountabilities/all`).flush(initialAccountabilities);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesBaseUrl}/api/v1/accountabilities/ids/4`);

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

        it('should retrieve the most recent Accountabilities Records from the local cache',
            inject(
                [HttpTestingController, AccountabilitiesDataService],
                (httpMock: HttpTestingController, accountabilitiesDataService: AccountabilitiesDataService) => {

                    // Define a couple of mock Accountabilities
                    const allAccountabilities = [
                        new Accountability({ id: 1, name: "Accountability 1", description: "Accountability 1 Description", version: 1 }),
                        new Accountability({ id: 2, name: "Accountability 2", description: "Accountability 2 Description", version: 1 }),
                        new Accountability({ id: 3, name: "Accountability 3", description: "Accountability 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.accountabilitiesBaseUrl}/api/v1/accountabilities/all`).flush(allAccountabilities);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Accountabilities
                    expect(accountabilitiesDataService.records).toEqual(allAccountabilities);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

