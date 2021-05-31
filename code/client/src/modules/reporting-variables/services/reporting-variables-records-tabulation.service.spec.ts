
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ReportingVariable } from '@modules/reporting-variables/models/reporting-variable.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { ReportingVariablesDataService } from './reporting-variables-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { ReportingVariablesRecordsTabulationService } from './reporting-variables-records-tabulation.service';

describe('ReportingVariablesRecordsTabulationService', () => {

    let reportingVariablesDataService: ReportingVariablesDataService;
    let reportingVariablesTableDataService: ReportingVariablesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, ReportingVariablesDataService, MessageService],
        });

        reportingVariablesDataService = TestBed.inject(ReportingVariablesDataService);
        reportingVariablesTableDataService = TestBed.inject(ReportingVariablesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('reportingVariables$', () => {

        it('should return transformed ReportingVariables', () => {

            // Define a couple of mock ReportingVariables
            const mockReportingVariables = [
                new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
            ];


            reportingVariablesTableDataService.reportingVariables$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockReportingVariables);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_variables/all`).flush(mockReportingVariables);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should filter Reporting Variables records by Reporting Framework id', () => {

            // Define a couple of mock ReportingVariables

            const unfilteredMockReportingVariables = [
                new ReportingVariable({ id: 1, reportingFrameworkId: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                new ReportingVariable({ id: 2, reportingFrameworkId: 1, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                new ReportingVariable({ id: 3, reportingFrameworkId: 2, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
            ]; 
            
            const filteredMockReportingVariables = [
                new ReportingVariable({ id: 1, reportingFrameworkId: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                new ReportingVariable({ id: 2, reportingFrameworkId: 1, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 })
            ];             


            expect(reportingVariablesTableDataService.filterByReportingFramework(unfilteredMockReportingVariables, 1)).toEqual(filteredMockReportingVariables);            
        });          


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(reportingVariablesTableDataService.compare("ReportingVariable 1", "ReportingVariable 2")).toEqual(-1);
            expect(reportingVariablesTableDataService.compare("ReportingVariable 1", "ReportingVariable 1")).toEqual(0);
            expect(reportingVariablesTableDataService.compare("ReportingVariable 2", "ReportingVariable 1")).toEqual(1);

            // numbers
            expect(reportingVariablesTableDataService.compare(1, 2)).toEqual(-1);
            expect(reportingVariablesTableDataService.compare(1, 1)).toEqual(0);
            expect(reportingVariablesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Reporting Variables records by table column in ascending or descending order', () => {

            // Define a couple of mock ReportingVariables

            const unsortedMockReportingVariables = [
                new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 }),
                new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),                
            ];

            const sortedMockReportingVariables1 = [
                new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
            ];  
            
            const sortedMockReportingVariables2 = [
                new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 }),                
                new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),                
            ];            


            expect(reportingVariablesTableDataService.sort(unsortedMockReportingVariables,"id", "asc")).toEqual(sortedMockReportingVariables1);
            expect(reportingVariablesTableDataService.sort(unsortedMockReportingVariables,"id", "desc")).toEqual(sortedMockReportingVariables2);

            expect(reportingVariablesTableDataService.sort(unsortedMockReportingVariables,"name", "asc")).toEqual(sortedMockReportingVariables1);
            expect(reportingVariablesTableDataService.sort(unsortedMockReportingVariables,"name", "desc")).toEqual(sortedMockReportingVariables2);
            
            expect(reportingVariablesTableDataService.sort(unsortedMockReportingVariables,"description", "asc")).toEqual(sortedMockReportingVariables1);
            expect(reportingVariablesTableDataService.sort(unsortedMockReportingVariables,"description", "desc")).toEqual(sortedMockReportingVariables2);            
        }); 
        
        
        it('should return true if a Reporting Variable record matches a search string or false otherwise', () => {

            // Define a couple of mock ReportingVariables
            let reportingVariable1: ReportingVariable = new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 });
            let reportingVariable2: ReportingVariable = new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 });
            let reportingVariable3: ReportingVariable = new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 });


            expect(reportingVariablesTableDataService.matches(reportingVariable1, "3")).toBeFalse(); 
            expect(reportingVariablesTableDataService.matches(reportingVariable2, "3")).toBeFalse(); 
            expect(reportingVariablesTableDataService.matches(reportingVariable3, "3")).toBeTrue();
        }); 

        it('should index Reporting Variables records', () => {

            // Define a couple of mock ReportingVariables

            const unIndexedMockReportingVariables = [
                new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
            ]; 
            
            const indexedMockReportingVariables = [
                new ReportingVariable({ pos: 1, id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                new ReportingVariable({ pos: 2, id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                new ReportingVariable({ pos: 3, id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
            ];             


            expect(reportingVariablesTableDataService.index(unIndexedMockReportingVariables)).toEqual(indexedMockReportingVariables);            
        }); 


        it('should paginate Reporting Variables records by page and page sizes', () => {

            // Define a couple of mock ReportingVariables

            const unpaginatedMockReportingVariables = [
                new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 }),
                new ReportingVariable({ id: 3, name: "ReportingVariable 3", description: "ReportingVariable 3 Description", version: 1 })
            ]; 
            
            const paginatedMockReportingVariables = [
                new ReportingVariable({ id: 1, name: "ReportingVariable 1", description: "ReportingVariable 1 Description", version: 1 }),
                new ReportingVariable({ id: 2, name: "ReportingVariable 2", description: "ReportingVariable 2 Description", version: 1 })
            ];             


            expect(reportingVariablesTableDataService.paginate(unpaginatedMockReportingVariables, 1, 2)).toEqual(paginatedMockReportingVariables);            
        });         

    });
});
