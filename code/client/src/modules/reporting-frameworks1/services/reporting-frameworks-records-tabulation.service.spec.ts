
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ReportingFramework } from '../models/reporting-framework.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { ReportingFrameworksDataService } from './reporting-frameworks-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { ReportingFrameworksRecordsTabulationService } from './reporting-frameworks-records-tabulation.service';

describe('ReportingFrameworksRecordsTabulationService', () => {

    let reportingFrameworksDataService: ReportingFrameworksDataService;
    let reportingFrameworksTableDataService: ReportingFrameworksRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, ReportingFrameworksDataService, MessageService],
        });

        reportingFrameworksDataService = TestBed.inject(ReportingFrameworksDataService);
        reportingFrameworksTableDataService = TestBed.inject(ReportingFrameworksRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('reportingFrameworks$', () => {

        it('should return transformed Reporting Frameworks', () => {

            // Define a couple of mock Reporting Frameworks
            const mockReportingFrameworks = [
                new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
            ];


            reportingFrameworksTableDataService.reportingFrameworks$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockReportingFrameworks);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_frameworks/all`).flush(mockReportingFrameworks);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(reportingFrameworksTableDataService.compare("ReportingFramework 1", "ReportingFramework 2")).toEqual(-1);
            expect(reportingFrameworksTableDataService.compare("ReportingFramework 1", "ReportingFramework 1")).toEqual(0);
            expect(reportingFrameworksTableDataService.compare("ReportingFramework 2", "ReportingFramework 1")).toEqual(1);

            // numbers
            expect(reportingFrameworksTableDataService.compare(1, 2)).toEqual(-1);
            expect(reportingFrameworksTableDataService.compare(1, 1)).toEqual(0);
            expect(reportingFrameworksTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Reporting Frameworks records by table column in ascending or descending order', () => {

            // Define a couple of mock Reporting Frameworks

            const unsortedMockReportingFrameworks = [
                new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 }),
                new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),                
            ];

            const sortedMockReportingFrameworks1 = [
                new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
            ];  
            
            const sortedMockReportingFrameworks2 = [
                new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 }),                
                new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),                
            ];            


            expect(reportingFrameworksTableDataService.sort(unsortedMockReportingFrameworks,"id", "asc")).toEqual(sortedMockReportingFrameworks1);
            expect(reportingFrameworksTableDataService.sort(unsortedMockReportingFrameworks,"id", "desc")).toEqual(sortedMockReportingFrameworks2);

            expect(reportingFrameworksTableDataService.sort(unsortedMockReportingFrameworks,"name", "asc")).toEqual(sortedMockReportingFrameworks1);
            expect(reportingFrameworksTableDataService.sort(unsortedMockReportingFrameworks,"name", "desc")).toEqual(sortedMockReportingFrameworks2);
            
            expect(reportingFrameworksTableDataService.sort(unsortedMockReportingFrameworks,"description", "asc")).toEqual(sortedMockReportingFrameworks1);
            expect(reportingFrameworksTableDataService.sort(unsortedMockReportingFrameworks,"description", "desc")).toEqual(sortedMockReportingFrameworks2);            
        }); 
        
        
        it('should return true if a Reporting Framework record matches a search string or false otherwise', () => {

            // Define a couple of mock Reporting Frameworks
            let reportingFramework1: ReportingFramework = new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 });
            let reportingFramework2: ReportingFramework = new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 });
            let reportingFramework3: ReportingFramework = new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 });


            expect(reportingFrameworksTableDataService.matches(reportingFramework1, "3")).toBeFalse(); 
            expect(reportingFrameworksTableDataService.matches(reportingFramework2, "3")).toBeFalse(); 
            expect(reportingFrameworksTableDataService.matches(reportingFramework3, "3")).toBeTrue();
        }); 

        it('should index Reporting Frameworks records', () => {

            // Define a couple of mock Reporting Frameworks

            const unIndexedMockReportingFrameworks = [
                new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
            ]; 
            
            const indexedMockReportingFrameworks = [
                new ReportingFramework({ pos: 1, id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                new ReportingFramework({ pos: 2, id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                new ReportingFramework({ pos: 3, id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
            ];             


            expect(reportingFrameworksTableDataService.index(unIndexedMockReportingFrameworks)).toEqual(indexedMockReportingFrameworks);            
        }); 


        it('should paginate Reporting Frameworks records by page and page sizes', () => {

            // Define a couple of mock Reporting Frameworks

            const unpaginatedMockReportingFrameworks = [
                new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 }),
                new ReportingFramework({ id: 3, name: "ReportingFramework 3", description: "ReportingFramework 3 Description", version: 1 })
            ]; 
            
            const paginatedMockReportingFrameworks = [
                new ReportingFramework({ id: 1, name: "ReportingFramework 1", description: "ReportingFramework 1 Description", version: 1 }),
                new ReportingFramework({ id: 2, name: "ReportingFramework 2", description: "ReportingFramework 2 Description", version: 1 })
            ];             


            expect(reportingFrameworksTableDataService.paginate(unpaginatedMockReportingFrameworks, 1, 2)).toEqual(paginatedMockReportingFrameworks);            
        });         

    });
});
