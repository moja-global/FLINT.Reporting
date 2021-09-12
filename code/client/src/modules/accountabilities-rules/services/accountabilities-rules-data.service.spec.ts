import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { ConnectivityStatusService } from '@common/services';
import { AccountabilityRule } from '../models/accountability-rule.model';
import { AccountabilitiesRulesDataService } from './accountabilities-rules-data.service';

describe('AccountabilitiesRules Data Service', () => {

    let accountabilitiesRulesDataService: AccountabilitiesRulesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [AccountabilitiesRulesDataService, MessageService, ConnectivityStatusService],
        });


        accountabilitiesRulesDataService = TestBed.inject(AccountabilitiesRulesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createAccountabilityRule', () => {
        it('should create and adds an instance of a new Accountability Rule Record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesRulesDataService],
                (httpMock: HttpTestingController, accountabilitiesRulesDataService: AccountabilitiesRulesDataService) => {


                    // Define a couple of mock Accountabilities Rules
                    const initialAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 })
                    ];

                    const newAccountabilityRule = new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Accountability Rule 4", version: 1 });

                    const finalAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 }),
                        new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Accountability Rule 4", version: 1 })
                    ];

                    // Call & subscribe to the createAccountabilityRule() method
                    accountabilitiesRulesDataService.createAccountabilityRule(new AccountabilityRule({ name: "Accountability Rule 4" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new AccountabilityRule - i.e with the id / version upated
                            expect(response).toEqual(newAccountabilityRule);
                        });


                    // Subscribe to the AccountabilitiesRules observable
                    accountabilitiesRulesDataService.accountabilitiesRules$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial AccountabilitiesRules
                            expect(response[1]).toEqual(initialAccountabilitiesRules);

                            // Expect that the third response is equal to the final AccountabilitiesRules
                            expect(response[2]).toEqual(finalAccountabilitiesRules);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules/all`).flush(initialAccountabilitiesRules);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newAccountabilityRule);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });


    describe('createAccountabilityRules', () => {
        it('should create and add new instances of Accountability Rules Records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesRulesDataService],
                (httpMock: HttpTestingController, accountabilitiesRulesDataService: AccountabilitiesRulesDataService) => {


                    // Define a couple of mock Accountabilities Rules
                    const initialAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 })
                    ];

                    const newAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Accountability Rule 4", version: 1 }),
                        new AccountabilityRule({ id: 5, accountabilityRuleId: 5, subsidiaryPartyTypeId: 5, name: "Accountability Rule 5", version: 1 })
                    ];

                    const finalAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 }),
                        new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Accountability Rule 4", version: 1 }),
                        new AccountabilityRule({ id: 5, accountabilityRuleId: 5, subsidiaryPartyTypeId: 5, name: "Accountability Rule 5", version: 1 })
                    ];

                    // TODO
                    // Find out how to test a get all and post all request on the same url

                    // Call & subscribe to the createAccountabilityRules() method
                    /*accountabilitiesRulesDataService.createAccountabilityRules(
                        [
                            new AccountabilityRule({ accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Accountability Rule 4" }),
                            new AccountabilityRule({ accountabilityRuleId: 5, subsidiaryPartyTypeId: 5, name: "Accountability Rule 5" })
                        ])
                        .subscribe((response) => {

                            // Expect that the response is equal to newAccountabilitiesRules - i.e with the id / version upated
                            expect(response).toEqual(newAccountabilitiesRules);
                        });


                    // Subscribe to the AccountabilitiesRules observable
                    accountabilitiesRulesDataService.accountabilitiesRules$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial AccountabilitiesRules
                            expect(response[1]).toEqual(initialAccountabilitiesRules);

                            // Expect that the third response is equal to the final AccountabilitiesRules
                            expect(response[2]).toEqual(finalAccountabilitiesRules);
                        });*/




                }
            )
        );
    });


    describe('getAllAccountabilitiesRules', () => {

        it('should retrieve and add all or a subset of all Accountabilities Rules records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesRulesDataService],
                (httpMock: HttpTestingController, accountabilitiesRulesDataService: AccountabilitiesRulesDataService) => {

                    // Define a couple of mock Accountabilities Rules
                    const allAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 })
                    ];

                    // Call the getAllAccountabilitiesRules() method
                    accountabilitiesRulesDataService.getAllAccountabilitiesRules()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked AccountabilityRule
                            expect(response).toEqual(allAccountabilitiesRules);
                        });

                    // Subscribe to the AccountabilitiesRules observable
                    accountabilitiesRulesDataService.accountabilitiesRules$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the AccountabilitiesRules
                            expect(response[1]).toEqual(allAccountabilitiesRules);

                            // Expect that the third response is equal to all the AccountabilitiesRules
                            expect(response[2]).toEqual(allAccountabilitiesRules);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules/all`);
                    expect(requests.length).toEqual(2);

                    // Get the first retrieval request
                    const mockReq1 = requests[0];

                    // Resolve the first retrieval request
                    mockReq1.flush(allAccountabilitiesRules);

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allAccountabilitiesRules);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateAccountabilityRule', () => {
        it('should update a single Accountability Rule Record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesRulesDataService],
                (httpMock: HttpTestingController, accountabilitiesRulesDataService: AccountabilitiesRulesDataService) => {

                    // Define a couple of mock Accountabilities Rules
                    const initialAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 }),
                        new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Accountability Rule 4", version: 1 })
                    ];

                    const updatedAccountabilityRule = new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Updated AccountabilityRule Four", description: "Updated AccountabilityRule Four Description", version: 2 });

                    const finalAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 }),
                        new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Updated AccountabilityRule Four", description: "Updated AccountabilityRule Four Description", version: 2 })
                    ];


                    // Call the updateAccountabilityRule() method
                    accountabilitiesRulesDataService.updateAccountabilityRule(new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Updated AccountabilityRule Four", description: "Updated AccountabilityRule Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated AccountabilityRule
                            expect(response).toEqual(updatedAccountabilityRule);
                        });

                    // Subscribe to the AccountabilitiesRules observable
                    accountabilitiesRulesDataService.accountabilitiesRules$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial AccountabilitiesRules
                            expect(response[1]).toEqual(initialAccountabilitiesRules);

                            // Expect that the third response is equal to the final AccountabilitiesRules
                            expect(response[2]).toEqual(finalAccountabilitiesRules);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules/all`).flush(initialAccountabilitiesRules);

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedAccountabilityRule);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteAccountabilityRule', () => {
        it('should delete a single Accountability Rule Record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, AccountabilitiesRulesDataService],
                (httpMock: HttpTestingController, accountabilitiesRulesDataService: AccountabilitiesRulesDataService) => {

                    // Define a couple of mock Accountabilities Rules
                    const initialAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 }),
                        new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Accountability Rule 4", version: 1 })
                    ];

                    const targetAccountabilityRule = new AccountabilityRule({ id: 4, accountabilityRuleId: 4, subsidiaryPartyTypeId: 4, name: "Accountability Rule 4", version: 1 });

                    const finalAccountabilitiesRules: AccountabilityRule[] = <Array<AccountabilityRule>>[
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 })
                    ];

                    // Call deleteAccountabilityRule() method to delete the target AccountabilityRule from the list of AccountabilitiesRules
                    accountabilitiesRulesDataService.deleteAccountabilityRule(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted AccountabilitiesRules
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the AccountabilitiesRules observable
                    accountabilitiesRulesDataService.accountabilitiesRules$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial AccountabilitiesRules
                            expect(response[1]).toEqual(initialAccountabilitiesRules);

                            // Expect that the third response is equal to the final AccountabilitiesRules
                            expect(response[2]).toEqual(finalAccountabilitiesRules);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules/all`).flush(initialAccountabilitiesRules);


                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules/ids/4`);

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

        it('should retrieve the most recent Accountabilities Rules records from the local cache',
            inject(
                [HttpTestingController, AccountabilitiesRulesDataService],
                (httpMock: HttpTestingController, accountabilitiesRulesDataService: AccountabilitiesRulesDataService) => {

                    // Define a couple of mock Accountabilities Rules
                    const allAccountabilitiesRules = [
                        new AccountabilityRule({ id: 1, accountabilityRuleId: 1, subsidiaryPartyTypeId: 1, name: "Accountability Rule 1", version: 1 }),
                        new AccountabilityRule({ id: 2, accountabilityRuleId: 2, subsidiaryPartyTypeId: 2, name: "Accountability Rule 2", version: 1 }),
                        new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, name: "Accountability Rule 3", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules/all`).flush(allAccountabilitiesRules);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                    // Expect that the response is equal to the array of all AccountabilitiesRules
                    expect(accountabilitiesRulesDataService.records).toEqual(allAccountabilitiesRules);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

