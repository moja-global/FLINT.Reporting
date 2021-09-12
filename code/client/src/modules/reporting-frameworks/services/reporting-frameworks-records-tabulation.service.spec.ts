
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ReportingFramework } from '../models/reporting-framework.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { ReportingFrameworksDataService } from './reporting-frameworks-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';

import { ReportingFrameworksRecordsTabulationService } from './reporting-frameworks-records-tabulation.service';
import { environment } from 'environments/environment';

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


    describe('get reportingFrameworks', () => {

 
        const reportingFrameworks = [
            new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 }),
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 })
        ];        

        it('should return an observable containing Reporting Frameworks Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedReportingFrameworks = [
                new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),
                new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 }),
                new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.reportingFrameworksBaseUrl}/api/v1/reporting_frameworks/all`).flush(reportingFrameworks);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to ReportingFrameworks updates and verify the results
            reportingFrameworksTableDataService.reportingFrameworks$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(sortedReportingFrameworks);
                });



                

        });


    });



    describe('get total', () => {

        const reportingFrameworks = [
            new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 }),
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 })
        ];

        it('should return an observable containing the total number of Reporting Frameworks Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.reportingFrameworksBaseUrl}/api/v1/reporting_frameworks/all`).flush(reportingFrameworks);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            reportingFrameworksTableDataService.total$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(3);
                });


        });


    });    


    describe('get loading', () => {

        it('should return an observable indicating whether or not a data operation exercise (sorting, searching etc.) is currently underway', () => { 

            // Subscribe to loading updates and verify the results
            reportingFrameworksTableDataService.loading$
                .pipe(
                    take(4),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(4)
                    expect(response[0]).toEqual(false);
                    expect(response[1]).toEqual(true);
                    expect(response[2]).toEqual(false);
                });
        });


    });   
    
    
    describe('get page', () => {

        it('should return the currently active page', () => { 
            expect(reportingFrameworksTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(reportingFrameworksTableDataService, 'set').and.callThrough();
            reportingFrameworksTableDataService.page = 2;
            expect(reportingFrameworksTableDataService.set).toHaveBeenCalled();
            expect(reportingFrameworksTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(reportingFrameworksTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(reportingFrameworksTableDataService, 'set').and.callThrough();
            reportingFrameworksTableDataService.page = 10;
            expect(reportingFrameworksTableDataService.set).toHaveBeenCalled();
            expect(reportingFrameworksTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(reportingFrameworksTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(reportingFrameworksTableDataService, 'set').and.callThrough();
            reportingFrameworksTableDataService.searchTerm = "test";
            expect(reportingFrameworksTableDataService.set).toHaveBeenCalled();
            expect(reportingFrameworksTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(reportingFrameworksTableDataService, 'set');
            reportingFrameworksTableDataService.sortColumn = "name";
            expect(reportingFrameworksTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 


    describe('compare', () => {

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


    }); 
    
    

    describe('sort', () => {

        const unsortedMockReportingFrameworks = [
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 }),
            new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 }),
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),                
        ];

        const sortedMockReportingFrameworks1 = [
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 }),
            new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 })
        ];  
        
        const sortedMockReportingFrameworks2 = [
            new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 }),                
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 }),
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),                
        ];   

        it('should sort Reporting Frameworks Records by table column in ascending or descending order', () => {

            expect(reportingFrameworksTableDataService.sort(unsortedMockReportingFrameworks,"name", "asc")).toEqual(sortedMockReportingFrameworks1);
            expect(reportingFrameworksTableDataService.sort(unsortedMockReportingFrameworks,"name", "desc")).toEqual(sortedMockReportingFrameworks2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a Reporting Framework record matches a search string or false otherwise', () => {

            // Define a couple of Mock ReportingFrameworks
            let reportingFramework1: ReportingFramework = new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 });
            let reportingFramework2: ReportingFramework = new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 });
            let reportingFramework3: ReportingFramework = new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 });


            expect(reportingFrameworksTableDataService.matches(reportingFramework1, "3")).toBeFalse(); 
            expect(reportingFrameworksTableDataService.matches(reportingFramework2, "3")).toBeFalse(); 
            expect(reportingFrameworksTableDataService.matches(reportingFramework3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockReportingFrameworks = [
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 }),
            new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 })
        ]; 
        
        const indexedMockReportingFrameworks = [
            new ReportingFramework({ pos: 1, id: 1, name: "ReportingFramework 1", version: 1 }),
            new ReportingFramework({ pos: 2, id: 2, name: "ReportingFramework 2", version: 1 }),
            new ReportingFramework({ pos: 3, id: 3, name: "ReportingFramework 3", version: 1 })
        ];             


        it('should index Reporting Frameworks Records', () => {
            expect(reportingFrameworksTableDataService.index(unIndexedMockReportingFrameworks)).toEqual(indexedMockReportingFrameworks);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockReportingFrameworks = [
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 }),
            new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 })
        ]; 
        
        const paginatedMockReportingFrameworks = [
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 })
        ];         

        it('should paginate Reporting Frameworks Records by page and page sizes', () => {
            expect(reportingFrameworksTableDataService.paginate(unpaginatedMockReportingFrameworks, 1, 2)).toEqual(paginatedMockReportingFrameworks);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(reportingFrameworksTableDataService, 'transform');
            reportingFrameworksTableDataService.set({})
            expect(reportingFrameworksTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const reportingFrameworks = [
            new ReportingFramework({ id: 3, name: "ReportingFramework 3", version: 1 }),
            new ReportingFramework({ id: 1, name: "ReportingFramework 1", version: 1 }),
            new ReportingFramework({ id: 2, name: "ReportingFramework 2", version: 1 }) 
        ];        

        it('should sorts, filter and paginate Reporting Frameworks Records', () => {

            spyOn(reportingFrameworksTableDataService, 'sort').and.callThrough();;
            spyOn(reportingFrameworksTableDataService, 'matches').and.callThrough();
            spyOn(reportingFrameworksTableDataService, 'index').and.callThrough();
            spyOn(reportingFrameworksTableDataService, 'paginate').and.callThrough();

            reportingFrameworksTableDataService.transform(reportingFrameworks);

            expect(reportingFrameworksTableDataService.sort).toHaveBeenCalled();
            expect(reportingFrameworksTableDataService.matches).toHaveBeenCalled();
            expect(reportingFrameworksTableDataService.index).toHaveBeenCalled();
            expect(reportingFrameworksTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
