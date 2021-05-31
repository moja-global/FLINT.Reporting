import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { ReportingTablesDataService } from './reporting-tables-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { ReportingTable } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('ReportingTables Data Service', () => {

    let reportingTablesDataService: ReportingTablesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ReportingTablesDataService, MessageService, ConnectivityStatusService],
        });

    
        reportingTablesDataService = TestBed.inject(ReportingTablesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createReportingTable', () => {
        it('should create and adds an instance of a new Reporting Table record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingTablesDataService],
                (httpMock: HttpTestingController, reportingTablesDataService: ReportingTablesDataService) => {

          
                    // Define a couple of mock ReportingTables
                    const initialReportingTables: ReportingTable[] = <Array<ReportingTable>>[
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
                    ];

                    const newReportingTable = new ReportingTable({ id: 4, name: "ReportingTable 4", description: "ReportingTable 4 Description", version: 1 });
                    
                    const finalReportingTables: ReportingTable[] = <Array<ReportingTable>>[
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 }),
                        new ReportingTable({ id: 4, name: "ReportingTable 4", description: "ReportingTable 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createReportingTable() method
                    reportingTablesDataService.createReportingTable(new ReportingTable({ name: "ReportingTable 4", description: "ReportingTable 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new ReportingTable - i.e with the id / version upated
                            expect(response).toEqual(newReportingTable);
                        });


                    // Subscribe to the Reporting Tables observable
                    reportingTablesDataService.reportingTables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial ReportingTables
                            expect(response[1]).toEqual(initialReportingTables);                            

                            // Expect that the third response is equal to the final ReportingTables
                            expect(response[2]).toEqual(finalReportingTables);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables/all`).flush(initialReportingTables);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newReportingTable);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getReportingTable', () => {
        it('should retrieve and add a single Reporting Table record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingTablesDataService],
                (httpMock: HttpTestingController, reportingTablesDataService: ReportingTablesDataService) => {

                    // Define a couple of mock ReportingTables
                    const allReportingTables: ReportingTable[] = <Array<ReportingTable>>[
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
                    ];

                    const targetReportingTable = new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 });

                    // Call the getReportingTable() method
                    reportingTablesDataService.getReportingTable(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target ReportingTable
                            expect(response).toEqual(targetReportingTable);
                        });

                    // Subscribe to the Reporting Tables observable
                    reportingTablesDataService.reportingTables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Reporting Tables
                            expect(response[1]).toEqual(allReportingTables);                            

                            // Expect that the third response is equal to all the Reporting Tables
                            expect(response[2]).toEqual(allReportingTables);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables/all`).flush(allReportingTables);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetReportingTable);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllReportingTables', () => {

        it('should retrieve and add all or a subset of all Reporting Tables records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingTablesDataService],
                (httpMock: HttpTestingController, reportingTablesDataService: ReportingTablesDataService) => {

                    // Define a couple of mock ReportingTables
                    const allReportingTables: ReportingTable[] = <Array<ReportingTable>>[
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
                    ];

                    // Call the getAllReportingTables() method
                    reportingTablesDataService.getAllReportingTables()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingTable
                            expect(response).toEqual(allReportingTables);
                        });

                    // Subscribe to the Reporting Tables observable
                    reportingTablesDataService.reportingTables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Reporting Tables
                            expect(response[1]).toEqual(allReportingTables);                            

                            // Expect that the third response is equal to all the Reporting Tables
                            expect(response[2]).toEqual(allReportingTables);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/reporting_tables/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allReportingTables);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allReportingTables);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateReportingTable', () => {
        it('should update a single Reporting Table record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingTablesDataService],
                (httpMock: HttpTestingController, reportingTablesDataService: ReportingTablesDataService) => {

                    // Define a couple of mock ReportingTables
                    const initialReportingTables: ReportingTable[] = <Array<ReportingTable>>[
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 }),
                        new ReportingTable({ id: 4, name: "ReportingTable 4", description: "ReportingTable 4 Description", version: 1 })
                    ];  

                    const updatedReportingTable = new ReportingTable({ id: 4, name: "Updated ReportingTable Four", description: "Updated ReportingTable Four Description", version: 2 });
                    
                    const finalReportingTables: ReportingTable[] = <Array<ReportingTable>>[
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 }),
                        new ReportingTable({ id: 4, name: "Updated ReportingTable Four", description: "Updated ReportingTable Four Description", version: 2 })
                    ];                     


                    // Call the updateReportingTable() method
                    reportingTablesDataService.updateReportingTable(new ReportingTable({ id: 4, name: "Updated ReportingTable Four", description: "Updated ReportingTable Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated ReportingTable
                            expect(response).toEqual(updatedReportingTable);
                        });

                    // Subscribe to the Reporting Tables observable
                    reportingTablesDataService.reportingTables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial ReportingTables
                            expect(response[1]).toEqual(initialReportingTables);                            

                            // Expect that the third response is equal to the final ReportingTables
                            expect(response[2]).toEqual(finalReportingTables);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables/all`).flush(initialReportingTables);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedReportingTable);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteReportingTable', () => {
        it('should delete a single Reporting Table record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingTablesDataService],
                (httpMock: HttpTestingController, reportingTablesDataService: ReportingTablesDataService) => {

                    // Define a couple of mock ReportingTables
                    const initialReportingTables: ReportingTable[] = <Array<ReportingTable>>[
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 }),
                        new ReportingTable({ id: 4, name: "ReportingTable 4", description: "ReportingTable 4 Description", version: 1 })
                    ];  

                    const targetReportingTable = new ReportingTable({ id: 4, name: "ReportingTable 4", description: "ReportingTable 4 Description", version: 1 });

                    const finalReportingTables: ReportingTable[] = <Array<ReportingTable>>[
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
                    ];

                    // Call deleteReportingTable() method to delete the target ReportingTable from the list of ReportingTables
                    reportingTablesDataService.deleteReportingTable(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted ReportingTables
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Reporting Tables observable
                    reportingTablesDataService.reportingTables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial ReportingTables
                            expect(response[1]).toEqual(initialReportingTables);                            

                            // Expect that the third response is equal to the final ReportingTables
                            expect(response[2]).toEqual(finalReportingTables);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables/all`).flush(initialReportingTables);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables/ids/4`);

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

        it('should retrieve the most recent Reporting Tables records from the local cache',
            inject(
                [HttpTestingController, ReportingTablesDataService],
                (httpMock: HttpTestingController, reportingTablesDataService: ReportingTablesDataService) => {

                    // Define a couple of mock ReportingTables
                    const allReportingTables = [
                        new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                        new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                        new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables/all`).flush(allReportingTables);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all ReportingTables
                    expect(reportingTablesDataService.records).toEqual(allReportingTables);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

