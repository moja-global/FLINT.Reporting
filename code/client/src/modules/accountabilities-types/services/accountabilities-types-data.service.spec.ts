import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { AccountabilitiesTypesDataService } from './accountabilities-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { AccountabilityType } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('AccountabilitiesTypes Data Service', () => {

    let accountabilitiesTypesDataService: AccountabilitiesTypesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [AccountabilitiesTypesDataService, MessageService, ConnectivityStatusService],
        });

    
        accountabilitiesTypesDataService = TestBed.inject(AccountabilitiesTypesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createAccountabilityType', () => {
        it('should create and adds an instance of a new AccountabilityType record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesTypesDataService],
                (httpMock: HttpTestingController, accountabilitiesTypesDataService: AccountabilitiesTypesDataService) => {

          
                    // Define a couple of mock AccountabilitiesTypes
                    const initialAccountabilitiesTypes: AccountabilityType[] = <Array<AccountabilityType>>[
                        new AccountabilityType({ id: 1, name: "AccountabilityType 1", description: "AccountabilityType 1 Description", version: 1 }),
                        new AccountabilityType({ id: 2, name: "AccountabilityType 2", description: "AccountabilityType 2 Description", version: 1 }),
                        new AccountabilityType({ id: 3, name: "AccountabilityType 3", description: "AccountabilityType 3 Description", version: 1 })
                    ];

                    const newAccountabilityType = new AccountabilityType({ id: 4, name: "AccountabilityType 4", description: "AccountabilityType 4 Description", version: 1 });
                    
                    const finalAccountabilitiesTypes: AccountabilityType[] = <Array<AccountabilityType>>[
                        new AccountabilityType({ id: 1, name: "AccountabilityType 1", description: "AccountabilityType 1 Description", version: 1 }),
                        new AccountabilityType({ id: 2, name: "AccountabilityType 2", description: "AccountabilityType 2 Description", version: 1 }),
                        new AccountabilityType({ id: 3, name: "AccountabilityType 3", description: "AccountabilityType 3 Description", version: 1 }),
                        new AccountabilityType({ id: 4, name: "AccountabilityType 4", description: "AccountabilityType 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createAccountabilityType() method
                    accountabilitiesTypesDataService.createAccountabilityType(new AccountabilityType({ name: "AccountabilityType 4", description: "AccountabilityType 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new AccountabilityType - i.e with the id / version upated
                            expect(response).toEqual(newAccountabilityType);
                        });


                    // Subscribe to the AccountabilitiesTypes observable
                    accountabilitiesTypesDataService.accountabilitiesTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial AccountabilitiesTypes
                            expect(response[1]).toEqual(initialAccountabilitiesTypes);                            

                            // Expect that the third response is equal to the final AccountabilitiesTypes
                            expect(response[2]).toEqual(finalAccountabilitiesTypes);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types/all`).flush(initialAccountabilitiesTypes);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newAccountabilityType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });


    describe('getAllAccountabilitiesTypes', () => {

        it('should retrieve and add all or a subset of all Accountabilities Types Records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesTypesDataService],
                (httpMock: HttpTestingController, accountabilitiesTypesDataService: AccountabilitiesTypesDataService) => {

                    // Define a couple of mock AccountabilitiesTypes
                    const allAccountabilitiesTypes: AccountabilityType[] = <Array<AccountabilityType>>[
                        new AccountabilityType({ id: 1, name: "AccountabilityType 1", description: "AccountabilityType 1 Description", version: 1 }),
                        new AccountabilityType({ id: 2, name: "AccountabilityType 2", description: "AccountabilityType 2 Description", version: 1 }),
                        new AccountabilityType({ id: 3, name: "AccountabilityType 3", description: "AccountabilityType 3 Description", version: 1 })
                    ];

                    // Call the getAllAccountabilitiesTypes() method
                    accountabilitiesTypesDataService.getAllAccountabilitiesTypes()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked AccountabilityType
                            expect(response).toEqual(allAccountabilitiesTypes);
                        });

                    // Subscribe to the AccountabilitiesTypes observable
                    accountabilitiesTypesDataService.accountabilitiesTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the AccountabilitiesTypes
                            expect(response[1]).toEqual(allAccountabilitiesTypes);                            

                            // Expect that the third response is equal to all the AccountabilitiesTypes
                            expect(response[2]).toEqual(allAccountabilitiesTypes);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allAccountabilitiesTypes);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allAccountabilitiesTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateAccountabilityType', () => {
        it('should update a single AccountabilityType record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesTypesDataService],
                (httpMock: HttpTestingController, accountabilitiesTypesDataService: AccountabilitiesTypesDataService) => {

                    // Define a couple of mock AccountabilitiesTypes
                    const initialAccountabilitiesTypes: AccountabilityType[] = <Array<AccountabilityType>>[
                        new AccountabilityType({ id: 1, name: "AccountabilityType 1", description: "AccountabilityType 1 Description", version: 1 }),
                        new AccountabilityType({ id: 2, name: "AccountabilityType 2", description: "AccountabilityType 2 Description", version: 1 }),
                        new AccountabilityType({ id: 3, name: "AccountabilityType 3", description: "AccountabilityType 3 Description", version: 1 }),
                        new AccountabilityType({ id: 4, name: "AccountabilityType 4", description: "AccountabilityType 4 Description", version: 1 })
                    ];  

                    const updatedAccountabilityType = new AccountabilityType({ id: 4, name: "Updated AccountabilityType Four", description: "Updated AccountabilityType Four Description", version: 2 });
                    
                    const finalAccountabilitiesTypes: AccountabilityType[] = <Array<AccountabilityType>>[
                        new AccountabilityType({ id: 1, name: "AccountabilityType 1", description: "AccountabilityType 1 Description", version: 1 }),
                        new AccountabilityType({ id: 2, name: "AccountabilityType 2", description: "AccountabilityType 2 Description", version: 1 }),
                        new AccountabilityType({ id: 3, name: "AccountabilityType 3", description: "AccountabilityType 3 Description", version: 1 }),
                        new AccountabilityType({ id: 4, name: "Updated AccountabilityType Four", description: "Updated AccountabilityType Four Description", version: 2 })
                    ];                     


                    // Call the updateAccountabilityType() method
                    accountabilitiesTypesDataService.updateAccountabilityType(new AccountabilityType({ id: 4, name: "Updated AccountabilityType Four", description: "Updated AccountabilityType Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated AccountabilityType
                            expect(response).toEqual(updatedAccountabilityType);
                        });

                    // Subscribe to the AccountabilitiesTypes observable
                    accountabilitiesTypesDataService.accountabilitiesTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial AccountabilitiesTypes
                            expect(response[1]).toEqual(initialAccountabilitiesTypes);                            

                            // Expect that the third response is equal to the final AccountabilitiesTypes
                            expect(response[2]).toEqual(finalAccountabilitiesTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types/all`).flush(initialAccountabilitiesTypes);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedAccountabilityType);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteAccountabilityType', () => {
        it('should delete a single AccountabilityType record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesTypesDataService],
                (httpMock: HttpTestingController, accountabilitiesTypesDataService: AccountabilitiesTypesDataService) => {

                    // Define a couple of mock AccountabilitiesTypes
                    const initialAccountabilitiesTypes: AccountabilityType[] = <Array<AccountabilityType>>[
                        new AccountabilityType({ id: 1, name: "AccountabilityType 1", description: "AccountabilityType 1 Description", version: 1 }),
                        new AccountabilityType({ id: 2, name: "AccountabilityType 2", description: "AccountabilityType 2 Description", version: 1 }),
                        new AccountabilityType({ id: 3, name: "AccountabilityType 3", description: "AccountabilityType 3 Description", version: 1 }),
                        new AccountabilityType({ id: 4, name: "AccountabilityType 4", description: "AccountabilityType 4 Description", version: 1 })
                    ];  

                    const targetAccountabilityType = new AccountabilityType({ id: 4, name: "AccountabilityType 4", description: "AccountabilityType 4 Description", version: 1 });

                    const finalAccountabilitiesTypes: AccountabilityType[] = <Array<AccountabilityType>>[
                        new AccountabilityType({ id: 1, name: "AccountabilityType 1", description: "AccountabilityType 1 Description", version: 1 }),
                        new AccountabilityType({ id: 2, name: "AccountabilityType 2", description: "AccountabilityType 2 Description", version: 1 }),
                        new AccountabilityType({ id: 3, name: "AccountabilityType 3", description: "AccountabilityType 3 Description", version: 1 })
                    ];

                    // Call deleteAccountabilityType() method to delete the target AccountabilityType from the list of AccountabilitiesTypes
                    accountabilitiesTypesDataService.deleteAccountabilityType(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted AccountabilitiesTypes
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the AccountabilitiesTypes observable
                    accountabilitiesTypesDataService.accountabilitiesTypes$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial AccountabilitiesTypes
                            expect(response[1]).toEqual(initialAccountabilitiesTypes);                            

                            // Expect that the third response is equal to the final AccountabilitiesTypes
                            expect(response[2]).toEqual(finalAccountabilitiesTypes);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types/all`).flush(initialAccountabilitiesTypes);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types/ids/4`);

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

        it('should retrieve the most recent Accountabilities Types Records from the local cache',
            inject(
                [HttpTestingController, AccountabilitiesTypesDataService],
                (httpMock: HttpTestingController, accountabilitiesTypesDataService: AccountabilitiesTypesDataService) => {

                    // Define a couple of mock AccountabilitiesTypes
                    const allAccountabilitiesTypes = [
                        new AccountabilityType({ id: 1, name: "AccountabilityType 1", description: "AccountabilityType 1 Description", version: 1 }),
                        new AccountabilityType({ id: 2, name: "AccountabilityType 2", description: "AccountabilityType 2 Description", version: 1 }),
                        new AccountabilityType({ id: 3, name: "AccountabilityType 3", description: "AccountabilityType 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types/all`).flush(allAccountabilitiesTypes);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all AccountabilitiesTypes
                    expect(accountabilitiesTypesDataService.records).toEqual(allAccountabilitiesTypes);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

