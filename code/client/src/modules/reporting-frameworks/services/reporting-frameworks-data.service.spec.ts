import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { ReportingFrameworksDataService } from './reporting-frameworks-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { ReportingFramework } from '../models';

describe('Reporting Frameworks Data Service', () => {

    let reportingFrameworksDataService: ReportingFrameworksDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ReportingFrameworksDataService, MessageService],
        });

        reportingFrameworksDataService = TestBed.inject(ReportingFrameworksDataService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    describe('createReportingFramework', () => {
        it('should create and adds an instance of a new Reporting Framework record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, ReportingFrameworksDataService],
                (httpMock: HttpTestingController, reportingFrameworksDataService: ReportingFrameworksDataService) => {

                    // Define the mock reportingFramework
                    const mockReportingFramework = new ReportingFramework({ id: 4, name: "ReportingFramework 4", description: "ReportingFramework 4 Description", version: 1 });

                    // Call & subscribe to the createReportingFramework() method
                    reportingFrameworksDataService.createReportingFramework(new ReportingFramework({ name: "ReportingFramework 4", description: "ReportingFramework 4 Description" }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingFramework - i.e with the id / version upated
                            expect(response).toEqual(mockReportingFramework);
                        });


                    // Subscribe to the Reporting Frameworks observable
                    reportingFrameworksDataService.reportingFrameworks$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked ReportingFramework - i.e with the id / version upated
                            expect(response[1]).toEqual([mockReportingFramework]);
                        });


                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks`);

                    // Expect that the request method is of type POST
                    expect(mockReq.request.method).toEqual('POST');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked ReportingFramework as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockReportingFramework);


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

                    // Define the mock reportingFramework
                    const mockReportingFramework = new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 });

                    // Call the getReportingFramework() method
                    reportingFrameworksDataService.getReportingFramework(2)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingFramework
                            expect(response).toEqual(mockReportingFramework);
                        });

                    // Subscribe to the Reporting Frameworks observable
                    reportingFrameworksDataService.reportingFrameworks$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked ReportingFramework
                            expect(response[1]).toEqual([mockReportingFramework]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/ids/2`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked ReportingFramework as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockReportingFramework);



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
                    const mockReportingFrameworks: ReportingFramework[] = <Array<ReportingFramework>>[
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
                    ];

                    // Call the getAllReportingFrameworks() method
                    reportingFrameworksDataService.getAllReportingFrameworks()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingFramework
                            expect(response).toEqual(mockReportingFrameworks);
                        });

                    // Subscribe to the Reporting Frameworks observable
                    reportingFrameworksDataService.reportingFrameworks$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked ReportingFrameworks
                            expect(response[1]).toEqual(mockReportingFrameworks);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/all`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked ReportingFrameworks as an argument to the flush method so that they are returned as the response
                    mockReq.flush(mockReportingFrameworks);

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

                    // Define the mock reportingFramework
                    const mockReportingFramework1 = new ReportingFramework({ id: 4, name: "ReportingFramework Four", description: "ReportingFramework Four Description", version: 1 });
                    const mockReportingFramework2 = new ReportingFramework({ id: 4, name: "Updated ReportingFramework Four", description: "Updated ReportingFramework Four Description", version: 2 });

                    // Call createReportingFramework() method to create the mock reportingFramework 1 and add it to the list of reportingFrameworks
                    reportingFrameworksDataService.createReportingFramework(mockReportingFramework1)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingFramework 1
                            expect(response).toEqual(mockReportingFramework1);
                        });

                    // Mock the createReportingFramework()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks`).flush(mockReportingFramework1);

                    // Call the updateReportingFramework() method
                    reportingFrameworksDataService.updateReportingFramework(new ReportingFramework({ id: 4, name: "Updated ReportingFramework Four", description: "Updated ReportingFramework Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingFramework 2
                            expect(response).toEqual(mockReportingFramework2);
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

                            // Expect that the second response is equal to the first (created) ReportingFramework
                            expect(response[1]).toEqual([mockReportingFramework1]);

                            // Expect that the second response is equal to the second (updated) ReportingFramework
                            expect(response[2]).toEqual([mockReportingFramework2]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks`);

                    // Expect that the request method is of type PUT
                    expect(mockReq.request.method).toEqual('PUT');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked ReportingFramework as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockReportingFramework2);

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

                    // Define a mock reportingFramework
                    const mockReportingFramework = new ReportingFramework({ id: 4, name: "ReportingFramework 4", description: "ReportingFramework 4 Description", version: 1 });

                    // Call createReportingFramework() method to create the mock reportingFramework and add it to the list of reportingFrameworks
                    reportingFrameworksDataService.createReportingFramework(mockReportingFramework)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingFramework - i.e with the id / version upated
                            expect(response).toEqual(mockReportingFramework);
                        });

                    // Mock the createReportingFramework()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks`).flush(mockReportingFramework);

                    // Call deleteReportingFramework() method to delete the mock reportingFramework from the list of reportingFrameworks
                    reportingFrameworksDataService.deleteReportingFramework(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted ReportingFramework
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

                            // Expect that the second response is equal to the created ReportingFramework
                            expect(response[1]).toEqual([mockReportingFramework]);

                            // Expect that the second response is equal to an empty array - following the deletion of the created reportingFramework
                            expect(response[2]).toEqual([]);
                        });

                    // Mock the deleteReportingFramework()'s corresponding HTTP DELETE Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/ids/4`).flush(1);

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
                    const mockReportingFrameworks = [
                        new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                        new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                        new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
                    ];

                    // Call the getAllReportingFrameworks() method
                    reportingFrameworksDataService.getAllReportingFrameworks()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked ReportingFrameworks
                            expect(response).toEqual(mockReportingFrameworks);
                        });

                    // Expect that a single request has been made, which matches the provided URL
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/all`).flush(mockReportingFrameworks);

                    // Expect that the response is equal to an array containing the mocked ReportingFrameworks
                    expect(reportingFrameworksDataService.records).toEqual(mockReportingFrameworks);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });
});
