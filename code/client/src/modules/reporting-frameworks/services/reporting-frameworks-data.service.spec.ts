import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { ReportingFrameworksDataService } from './reporting-frameworks-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { ReportingFramework } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('Reporting Frameworks Data Service', () => {

    let reportingFrameworksDataService: ReportingFrameworksDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ReportingFrameworksDataService, MessageService, ConnectivityStatusService],
        });

    
        reportingFrameworksDataService = TestBed.inject(ReportingFrameworksDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createReportingFramework', () => {
        it('should create and adds an instance of a new Reporting Framework record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingFrameworksDataService],
                (httpMock: HttpTestingController, reportingFrameworksDataService: ReportingFrameworksDataService) => {

          
                    // Define a couple of mock Reporting Frameworks
                    const initialReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
                    ];

                    const newReportingFramework = new ReportingFramework({ id: 4, name: "ReportingFramework 4", description: "ReportingFramework 4 Description", version: 1 });
                    
                    const finalReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 }),
                        new ReportingFramework({ id: 4, name: "ReportingFramework 4", description: "ReportingFramework 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createReportingFramework() method
                    reportingFrameworksDataService.createReportingFramework(new ReportingFramework({ name: "ReportingFramework 4", description: "ReportingFramework 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Reporting Framework - i.e with the id / version upated
                            expect(response).toEqual(newReportingFramework);
                        });


                    // Subscribe to the Reporting Frameworks observable
                    reportingFrameworksDataService.reportingFrameworks$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Reporting Frameworks
                            expect(response[1]).toEqual(initialReportingFrameworks);                            

                            // Expect that the third response is equal to the final Reporting Frameworks
                            expect(response[2]).toEqual(finalReportingFrameworks);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/all`).flush(initialReportingFrameworks);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newReportingFramework);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getReportingFramework', () => {
        it('should retrieve and add a single Reporting Framework record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingFrameworksDataService],
                (httpMock: HttpTestingController, reportingFrameworksDataService: ReportingFrameworksDataService) => {

                    // Define a couple of mock Reporting Frameworks
                    const allReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
                    ];

                    const targetReportingFramework = new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 });

                    // Call the getReportingFramework() method
                    reportingFrameworksDataService.getReportingFramework(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Reporting Framework
                            expect(response).toEqual(targetReportingFramework);
                        });

                    // Subscribe to the Reporting Frameworks observable
                    reportingFrameworksDataService.reportingFrameworks$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Reporting Frameworks
                            expect(response[1]).toEqual(allReportingFrameworks);                            

                            // Expect that the third response is equal to all the Reporting Frameworks
                            expect(response[2]).toEqual(allReportingFrameworks);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/all`).flush(allReportingFrameworks);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetReportingFramework);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllReportingFrameworks', () => {

        it('should retrieve and add all or a subset of all Reporting Frameworks records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingFrameworksDataService],
                (httpMock: HttpTestingController, reportingFrameworksDataService: ReportingFrameworksDataService) => {

                    // Define a couple of mock Reporting Frameworks
                    const allReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
                    ];

                    // Call the getAllReportingFrameworks() method
                    reportingFrameworksDataService.getAllReportingFrameworks()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingFramework
                            expect(response).toEqual(allReportingFrameworks);
                        });

                    // Subscribe to the Reporting Frameworks observable
                    reportingFrameworksDataService.reportingFrameworks$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Reporting Frameworks
                            expect(response[1]).toEqual(allReportingFrameworks);                            

                            // Expect that the third response is equal to all the Reporting Frameworks
                            expect(response[2]).toEqual(allReportingFrameworks);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/reporting_frameworks/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allReportingFrameworks);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allReportingFrameworks);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateReportingFramework', () => {
        it('should update a single Reporting Framework record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingFrameworksDataService],
                (httpMock: HttpTestingController, reportingFrameworksDataService: ReportingFrameworksDataService) => {

                    // Define a couple of mock Reporting Frameworks
                    const initialReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 }),
                        new ReportingFramework({ id: 4, name: "ReportingFramework 4", description: "ReportingFramework 4 Description", version: 1 })
                    ];  

                    const updatedReportingFramework = new ReportingFramework({ id: 4, name: "Updated ReportingFramework Four", description: "Updated ReportingFramework Four Description", version: 2 });
                    
                    const finalReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 }),
                        new ReportingFramework({ id: 4, name: "Updated ReportingFramework Four", description: "Updated ReportingFramework Four Description", version: 2 })
                    ];                     


                    // Call the updateReportingFramework() method
                    reportingFrameworksDataService.updateReportingFramework(new ReportingFramework({ id: 4, name: "Updated ReportingFramework Four", description: "Updated ReportingFramework Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Reporting Framework
                            expect(response).toEqual(updatedReportingFramework);
                        });

                    // Subscribe to the Reporting Frameworks observable
                    reportingFrameworksDataService.reportingFrameworks$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Reporting Frameworks
                            expect(response[1]).toEqual(initialReportingFrameworks);                            

                            // Expect that the third response is equal to the final Reporting Frameworks
                            expect(response[2]).toEqual(finalReportingFrameworks);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/all`).flush(initialReportingFrameworks);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedReportingFramework);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteReportingFramework', () => {
        it('should delete a single Reporting Framework record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingFrameworksDataService],
                (httpMock: HttpTestingController, reportingFrameworksDataService: ReportingFrameworksDataService) => {

                    // Define a couple of mock Reporting Frameworks
                    const initialReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 }),
                        new ReportingFramework({ id: 4, name: "ReportingFramework 4", description: "ReportingFramework 4 Description", version: 1 })
                    ];  

                    const targetReportingFramework = new ReportingFramework({ id: 4, name: "ReportingFramework 4", description: "ReportingFramework 4 Description", version: 1 });

                    const finalReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
                    ];

                    // Call deleteReportingFramework() method to delete the target Reporting Framework from the list of Reporting Frameworks
                    reportingFrameworksDataService.deleteReportingFramework(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Reporting Frameworks
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Reporting Frameworks observable
                    reportingFrameworksDataService.reportingFrameworks$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Reporting Frameworks
                            expect(response[1]).toEqual(initialReportingFrameworks);                            

                            // Expect that the third response is equal to the final Reporting Frameworks
                            expect(response[2]).toEqual(finalReportingFrameworks);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/all`).flush(initialReportingFrameworks);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/ids/4`);

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

        it('should retrieve the most recent Reporting Frameworks records from the local cache',
            inject(
                [HttpTestingController, ReportingFrameworksDataService],
                (httpMock: HttpTestingController, reportingFrameworksDataService: ReportingFrameworksDataService) => {

                    // Define a couple of mock Reporting Frameworks
                    const allReportingFrameworks = [
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/all`).flush(allReportingFrameworks);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Reporting Frameworks
                    expect(reportingFrameworksDataService.records).toEqual(allReportingFrameworks);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

