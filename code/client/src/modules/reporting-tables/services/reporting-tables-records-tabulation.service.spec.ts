
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ReportingTable } from '@modules/reporting-tables/models/reporting-table.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { ReportingTablesDataService } from './reporting-tables-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { ReportingTablesRecordsTabulationService } from './reporting-tables-records-tabulation.service';

describe('ReportingTablesRecordsTabulationService', () => {

    let reportingTablesDataService: ReportingTablesDataService;
    let reportingTablesTableDataService: ReportingTablesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, ReportingTablesDataService, MessageService],
        });

        reportingTablesDataService = TestBed.inject(ReportingTablesDataService);
        reportingTablesTableDataService = TestBed.inject(ReportingTablesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('reportingTables$', () => {

        it('should return transformed ReportingTables', () => {

            // Define a couple of mock ReportingTables
            const mockReportingTables = [
                new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
            ];


            reportingTablesTableDataService.reportingTables$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockReportingTables);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/reporting_tables/all`).flush(mockReportingTables);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should filter Reporting Tables records by reportingTable category id', () => {

            // Define a couple of mock ReportingTables

            const unfilteredMockReportingTables = [
                new ReportingTable({ id: 1, reportingFrameworkId: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                new ReportingTable({ id: 2, reportingFrameworkId: 1, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                new ReportingTable({ id: 3, reportingFrameworkId: 2, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
            ]; 
            
            const filteredMockReportingTables = [
                new ReportingTable({ id: 1, reportingFrameworkId: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                new ReportingTable({ id: 2, reportingFrameworkId: 1, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 })
            ];             


            expect(reportingTablesTableDataService.filterByReportingFramework(unfilteredMockReportingTables, 1)).toEqual(filteredMockReportingTables);            
        });          


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(reportingTablesTableDataService.compare("ReportingTable 1", "ReportingTable 2")).toEqual(-1);
            expect(reportingTablesTableDataService.compare("ReportingTable 1", "ReportingTable 1")).toEqual(0);
            expect(reportingTablesTableDataService.compare("ReportingTable 2", "ReportingTable 1")).toEqual(1);

            // numbers
            expect(reportingTablesTableDataService.compare(1, 2)).toEqual(-1);
            expect(reportingTablesTableDataService.compare(1, 1)).toEqual(0);
            expect(reportingTablesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Reporting Tables records by table column in ascending or descending order', () => {

            // Define a couple of mock ReportingTables

            const unsortedMockReportingTables = [
                new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 }),
                new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),                
            ];

            const sortedMockReportingTables1 = [
                new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
            ];  
            
            const sortedMockReportingTables2 = [
                new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 }),                
                new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),                
            ];            


            expect(reportingTablesTableDataService.sort(unsortedMockReportingTables,"id", "asc")).toEqual(sortedMockReportingTables1);
            expect(reportingTablesTableDataService.sort(unsortedMockReportingTables,"id", "desc")).toEqual(sortedMockReportingTables2);

            expect(reportingTablesTableDataService.sort(unsortedMockReportingTables,"name", "asc")).toEqual(sortedMockReportingTables1);
            expect(reportingTablesTableDataService.sort(unsortedMockReportingTables,"name", "desc")).toEqual(sortedMockReportingTables2);
            
            expect(reportingTablesTableDataService.sort(unsortedMockReportingTables,"description", "asc")).toEqual(sortedMockReportingTables1);
            expect(reportingTablesTableDataService.sort(unsortedMockReportingTables,"description", "desc")).toEqual(sortedMockReportingTables2);            
        }); 
        
        
        it('should return true if a Reporting Table record matches a search string or false otherwise', () => {

            // Define a couple of mock ReportingTables
            let reportingTable1: ReportingTable = new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 });
            let reportingTable2: ReportingTable = new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 });
            let reportingTable3: ReportingTable = new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 });


            expect(reportingTablesTableDataService.matches(reportingTable1, "3")).toBeFalse(); 
            expect(reportingTablesTableDataService.matches(reportingTable2, "3")).toBeFalse(); 
            expect(reportingTablesTableDataService.matches(reportingTable3, "3")).toBeTrue();
        }); 

        it('should index Reporting Tables records', () => {

            // Define a couple of mock ReportingTables

            const unIndexedMockReportingTables = [
                new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
            ]; 
            
            const indexedMockReportingTables = [
                new ReportingTable({ pos: 1, id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                new ReportingTable({ pos: 2, id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                new ReportingTable({ pos: 3, id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
            ];             


            expect(reportingTablesTableDataService.index(unIndexedMockReportingTables)).toEqual(indexedMockReportingTables);            
        }); 


        it('should paginate Reporting Tables records by page and page sizes', () => {

            // Define a couple of mock ReportingTables

            const unpaginatedMockReportingTables = [
                new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 }),
                new ReportingTable({ id: 3, name: "ReportingTable 3", description: "ReportingTable 3 Description", version: 1 })
            ]; 
            
            const paginatedMockReportingTables = [
                new ReportingTable({ id: 1, name: "ReportingTable 1", description: "ReportingTable 1 Description", version: 1 }),
                new ReportingTable({ id: 2, name: "ReportingTable 2", description: "ReportingTable 2 Description", version: 1 })
            ];             


            expect(reportingTablesTableDataService.paginate(unpaginatedMockReportingTables, 1, 2)).toEqual(paginatedMockReportingTables);            
        });         

    });
});
