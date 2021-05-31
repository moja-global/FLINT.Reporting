import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { ReportingVariablesDataService } from './reporting-variables-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { ReportingVariable } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('ReportingVariables Data Service', () => {

    let reportingVariablesDataService: ReportingVariablesDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ReportingVariablesDataService, MessageService, ConnectivityStatusService],
        });

    
        reportingVariablesDataService = TestBed.inject(ReportingVariablesDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createReportingVariable', () => {
        it('should create and adds an instance of a new Reporting Variable record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingVariablesDataService],
                (httpMock: HttpTestingController, reportingVariablesDataService: ReportingVariablesDataService) => {

          
                    // Define a couple of mock ReportingVariables
                    const initialReportingVariables: ReportingVariable[] = <Array<ReportingVariable>>[
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
                    ];

                    const newReportingVariable = new ReportingVariable({ id: 4, name: "ReportingVariable 4", description: "ReportingVariable 4 Description", version: 1 });
                    
                    const finalReportingVariables: ReportingVariable[] = <Array<ReportingVariable>>[
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 }),
                        new ReportingVariable({ id: 4, name: "ReportingVariable 4", description: "ReportingVariable 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createReportingVariable() method
                    reportingVariablesDataService.createReportingVariable(new ReportingVariable({ name: "ReportingVariable 4", description: "ReportingVariable 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new ReportingVariable - i.e with the id / version upated
                            expect(response).toEqual(newReportingVariable);
                        });


                    // Subscribe to the Reporting Variables observable
                    reportingVariablesDataService.reportingVariables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial ReportingVariables
                            expect(response[1]).toEqual(initialReportingVariables);                            

                            // Expect that the third response is equal to the final ReportingVariables
                            expect(response[2]).toEqual(finalReportingVariables);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables/all`).flush(initialReportingVariables);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newReportingVariable);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getReportingVariable', () => {
        it('should retrieve and add a single Reporting Variable record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingVariablesDataService],
                (httpMock: HttpTestingController, reportingVariablesDataService: ReportingVariablesDataService) => {

                    // Define a couple of mock ReportingVariables
                    const allReportingVariables: ReportingVariable[] = <Array<ReportingVariable>>[
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
                    ];

                    const targetReportingVariable = new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 });

                    // Call the getReportingVariable() method
                    reportingVariablesDataService.getReportingVariable(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target ReportingVariable
                            expect(response).toEqual(targetReportingVariable);
                        });

                    // Subscribe to the Reporting Variables observable
                    reportingVariablesDataService.reportingVariables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Reporting Variables
                            expect(response[1]).toEqual(allReportingVariables);                            

                            // Expect that the third response is equal to all the Reporting Variables
                            expect(response[2]).toEqual(allReportingVariables);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables/all`).flush(allReportingVariables);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetReportingVariable);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllReportingVariables', () => {

        it('should retrieve and add all or a subset of all Reporting Variables records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingVariablesDataService],
                (httpMock: HttpTestingController, reportingVariablesDataService: ReportingVariablesDataService) => {

                    // Define a couple of mock ReportingVariables
                    const allReportingVariables: ReportingVariable[] = <Array<ReportingVariable>>[
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
                    ];

                    // Call the getAllReportingVariables() method
                    reportingVariablesDataService.getAllReportingVariables()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingVariable
                            expect(response).toEqual(allReportingVariables);
                        });

                    // Subscribe to the Reporting Variables observable
                    reportingVariablesDataService.reportingVariables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Reporting Variables
                            expect(response[1]).toEqual(allReportingVariables);                            

                            // Expect that the third response is equal to all the Reporting Variables
                            expect(response[2]).toEqual(allReportingVariables);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/reporting_variables/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allReportingVariables);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allReportingVariables);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateReportingVariable', () => {
        it('should update a single Reporting Variable record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingVariablesDataService],
                (httpMock: HttpTestingController, reportingVariablesDataService: ReportingVariablesDataService) => {

                    // Define a couple of mock ReportingVariables
                    const initialReportingVariables: ReportingVariable[] = <Array<ReportingVariable>>[
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 }),
                        new ReportingVariable({ id: 4, name: "ReportingVariable 4", description: "ReportingVariable 4 Description", version: 1 })
                    ];  

                    const updatedReportingVariable = new ReportingVariable({ id: 4, name: "Updated ReportingVariable Four", description: "Updated ReportingVariable Four Description", version: 2 });
                    
                    const finalReportingVariables: ReportingVariable[] = <Array<ReportingVariable>>[
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 }),
                        new ReportingVariable({ id: 4, name: "Updated ReportingVariable Four", description: "Updated ReportingVariable Four Description", version: 2 })
                    ];                     


                    // Call the updateReportingVariable() method
                    reportingVariablesDataService.updateReportingVariable(new ReportingVariable({ id: 4, name: "Updated ReportingVariable Four", description: "Updated ReportingVariable Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated ReportingVariable
                            expect(response).toEqual(updatedReportingVariable);
                        });

                    // Subscribe to the Reporting Variables observable
                    reportingVariablesDataService.reportingVariables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial ReportingVariables
                            expect(response[1]).toEqual(initialReportingVariables);                            

                            // Expect that the third response is equal to the final ReportingVariables
                            expect(response[2]).toEqual(finalReportingVariables);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables/all`).flush(initialReportingVariables);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedReportingVariable);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteReportingVariable', () => {
        it('should delete a single Reporting Variable record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingVariablesDataService],
                (httpMock: HttpTestingController, reportingVariablesDataService: ReportingVariablesDataService) => {

                    // Define a couple of mock ReportingVariables
                    const initialReportingVariables: ReportingVariable[] = <Array<ReportingVariable>>[
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 }),
                        new ReportingVariable({ id: 4, name: "ReportingVariable 4", description: "ReportingVariable 4 Description", version: 1 })
                    ];  

                    const targetReportingVariable = new ReportingVariable({ id: 4, name: "ReportingVariable 4", description: "ReportingVariable 4 Description", version: 1 });

                    const finalReportingVariables: ReportingVariable[] = <Array<ReportingVariable>>[
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
                    ];

                    // Call deleteReportingVariable() method to delete the target ReportingVariable from the list of ReportingVariables
                    reportingVariablesDataService.deleteReportingVariable(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted ReportingVariables
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Reporting Variables observable
                    reportingVariablesDataService.reportingVariables$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial ReportingVariables
                            expect(response[1]).toEqual(initialReportingVariables);                            

                            // Expect that the third response is equal to the final ReportingVariables
                            expect(response[2]).toEqual(finalReportingVariables);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables/all`).flush(initialReportingVariables);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables/ids/4`);

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

        it('should retrieve the most recent Reporting Variables records from the local cache',
            inject(
                [HttpTestingController, ReportingVariablesDataService],
                (httpMock: HttpTestingController, reportingVariablesDataService: ReportingVariablesDataService) => {

                    // Define a couple of mock ReportingVariables
                    const allReportingVariables = [
                        new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                        new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                        new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables/all`).flush(allReportingVariables);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all ReportingVariables
                    expect(reportingVariablesDataService.records).toEqual(allReportingVariables);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

