import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { Database } from '../models';
import { ConnectivityStatusService } from '@common/services';
import { DatabasesDataService } from '.';

describe('Databases Data Service', () => {

    let databasesDataService: DatabasesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [DatabasesDataService, MessageService, ConnectivityStatusService],
        });

    
        databasesDataService = TestBed.inject(DatabasesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createDatabase', () => {
        it('should create and adds an instance of a new Database record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DatabasesDataService],
                (httpMock: HttpTestingController, databasesDataService: DatabasesDataService) => {

          
                    // Define a couple of mock Databases
                    const initialDatabases: Database[] = <Array<Database>>[
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
                    ];

                    const newDatabase = new Database({ id: 4, name: "Database 4", description: "Database 4 Description", version: 1 });
                    
                    const finalDatabases: Database[] = <Array<Database>>[
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 }),
                        new Database({ id: 4, name: "Database 4", description: "Database 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createDatabase() method
                    databasesDataService.createDatabase(new Database({ name: "Database 4", description: "Database 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Database - i.e with the id / version upated
                            expect(response).toEqual(newDatabase);
                        });


                    // Subscribe to the Databases observable
                    databasesDataService.databases$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Databases
                            expect(response[1]).toEqual(initialDatabases);                            

                            // Expect that the third response is equal to the final Databases
                            expect(response[2]).toEqual(finalDatabases);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/databases/all`).flush(initialDatabases);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/databases`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newDatabase);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getDatabase', () => {
        it('should retrieve and add a single Database record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DatabasesDataService],
                (httpMock: HttpTestingController, databasesDataService: DatabasesDataService) => {

                    // Define a couple of mock Databases
                    const allDatabases: Database[] = <Array<Database>>[
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
                    ];

                    const targetDatabase = new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 });

                    // Call the getDatabase() method
                    databasesDataService.getDatabase(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Database
                            expect(response).toEqual(targetDatabase);
                        });

                    // Subscribe to the Databases observable
                    databasesDataService.databases$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Databases
                            expect(response[1]).toEqual(allDatabases);                            

                            // Expect that the third response is equal to all the Databases
                            expect(response[2]).toEqual(allDatabases);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/databases/all`).flush(allDatabases);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/databases/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetDatabase);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllDatabases', () => {

        it('should retrieve and add all or a subset of all Databases records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DatabasesDataService],
                (httpMock: HttpTestingController, databasesDataService: DatabasesDataService) => {

                    // Define a couple of mock Databases
                    const allDatabases: Database[] = <Array<Database>>[
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
                    ];

                    // Call the getAllDatabases() method
                    databasesDataService.getAllDatabases()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Database
                            expect(response).toEqual(allDatabases);
                        });

                    // Subscribe to the Databases observable
                    databasesDataService.databases$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Databases
                            expect(response[1]).toEqual(allDatabases);                            

                            // Expect that the third response is equal to all the Databases
                            expect(response[2]).toEqual(allDatabases);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/databases/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allDatabases);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allDatabases);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateDatabase', () => {
        it('should update a single Database record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DatabasesDataService],
                (httpMock: HttpTestingController, databasesDataService: DatabasesDataService) => {

                    // Define a couple of mock Databases
                    const initialDatabases: Database[] = <Array<Database>>[
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 }),
                        new Database({ id: 4, name: "Database 4", description: "Database 4 Description", version: 1 })
                    ];  

                    const updatedDatabase = new Database({ id: 4, name: "Updated Database Four", description: "Updated Database Four Description", version: 2 });
                    
                    const finalDatabases: Database[] = <Array<Database>>[
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 }),
                        new Database({ id: 4, name: "Updated Database Four", description: "Updated Database Four Description", version: 2 })
                    ];                     


                    // Call the updateDatabase() method
                    databasesDataService.updateDatabase(new Database({ id: 4, name: "Updated Database Four", description: "Updated Database Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Database
                            expect(response).toEqual(updatedDatabase);
                        });

                    // Subscribe to the Databases observable
                    databasesDataService.databases$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Databases
                            expect(response[1]).toEqual(initialDatabases);                            

                            // Expect that the third response is equal to the final Databases
                            expect(response[2]).toEqual(finalDatabases);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/databases/all`).flush(initialDatabases);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/databases`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedDatabase);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteDatabase', () => {
        it('should delete a single Database record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DatabasesDataService],
                (httpMock: HttpTestingController, databasesDataService: DatabasesDataService) => {

                    // Define a couple of mock Databases
                    const initialDatabases: Database[] = <Array<Database>>[
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 }),
                        new Database({ id: 4, name: "Database 4", description: "Database 4 Description", version: 1 })
                    ];  

                    const targetDatabase = new Database({ id: 4, name: "Database 4", description: "Database 4 Description", version: 1 });

                    const finalDatabases: Database[] = <Array<Database>>[
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
                    ];

                    // Call deleteDatabase() method to delete the target Database from the list of Databases
                    databasesDataService.deleteDatabase(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Databases
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Databases observable
                    databasesDataService.databases$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Databases
                            expect(response[1]).toEqual(initialDatabases);                            

                            // Expect that the third response is equal to the final Databases
                            expect(response[2]).toEqual(finalDatabases);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/databases/all`).flush(initialDatabases);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/databases/ids/4`);

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

        it('should retrieve the most recent Databases records from the local cache',
            inject(
                [HttpTestingController, DatabasesDataService],
                (httpMock: HttpTestingController, databasesDataService: DatabasesDataService) => {

                    // Define a couple of mock Databases
                    const allDatabases = [
                        new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                        new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                        new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/databases/all`).flush(allDatabases);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Databases
                    expect(databasesDataService.records).toEqual(allDatabases);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

