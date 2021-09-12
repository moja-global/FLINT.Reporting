
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';
import { LandUseCategory } from '../models/land-use-category.model';
import { LandUsesCategoriesDataService } from './land-uses-categories-data.service';
import { LandUsesCategoriesRecordsTabulationService } from './land-uses-categories-records-tabulation.service';


describe('LandUsesCategoriesRecordsTabulationService', () => {

    let landUsesCategoriesDataService: LandUsesCategoriesDataService;
    let landUsesCategoriesTableDataService: LandUsesCategoriesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, LandUsesCategoriesDataService, MessageService],
        });

        landUsesCategoriesDataService = TestBed.inject(LandUsesCategoriesDataService);
        landUsesCategoriesTableDataService = TestBed.inject(LandUsesCategoriesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('get landUsesCategories', () => {

 
        const landUsesCategories = [
            new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 }),
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 })
        ];        

        it('should return an observable containing Land Uses Categories Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedLandUsesCategories = [
                new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),
                new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 }),
                new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories/all`).flush(landUsesCategories);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to LandUsesCategories updates and verify the results
            landUsesCategoriesTableDataService.landUsesCategories$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {
                    expect(response[0]).toEqual(sortedLandUsesCategories);
                });



                

        });


    });



    describe('get total', () => {

        const landUsesCategories = [
            new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 }),
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 })
        ];

        it('should return an observable containing the total number of Land Uses Categories Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.landUsesCategoriesBaseUrl}/api/v1/land_uses_categories/all`).flush(landUsesCategories);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            landUsesCategoriesTableDataService.total$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect((response[0])).toEqual(3);
                });


        });


    });    


    describe('get loading', () => {

        it('should return an observable indicating whether or not a data operation exercise (sorting, searching etc.) is currently underway', () => { 

            // Subscribe to loading updates and verify the results
            landUsesCategoriesTableDataService.loading$
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
            expect(landUsesCategoriesTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(landUsesCategoriesTableDataService, 'set').and.callThrough();
            landUsesCategoriesTableDataService.page = 2;
            expect(landUsesCategoriesTableDataService.set).toHaveBeenCalled();
            expect(landUsesCategoriesTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(landUsesCategoriesTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(landUsesCategoriesTableDataService, 'set').and.callThrough();
            landUsesCategoriesTableDataService.page = 10;
            expect(landUsesCategoriesTableDataService.set).toHaveBeenCalled();
            expect(landUsesCategoriesTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(landUsesCategoriesTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(landUsesCategoriesTableDataService, 'set').and.callThrough();
            landUsesCategoriesTableDataService.searchTerm = "test";
            expect(landUsesCategoriesTableDataService.set).toHaveBeenCalled();
            expect(landUsesCategoriesTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(landUsesCategoriesTableDataService, 'set');
            landUsesCategoriesTableDataService.sortColumn = "name";
            expect(landUsesCategoriesTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 

    
    
    
    describe('get parent id', () => {

        it('should return the currently set parent id', () => { 
            expect(landUsesCategoriesTableDataService.reportingFrameworkId).toEqual(null);
        });

    });   
    
    
    describe('set parent id', () => {

        it('should update the currently set parent id and trigger the data transformation transformation exercise', () => { 
            spyOn(landUsesCategoriesTableDataService, 'set').and.callThrough();
            landUsesCategoriesTableDataService.reportingFrameworkId = 2;
            expect(landUsesCategoriesTableDataService.set).toHaveBeenCalled();
            expect(landUsesCategoriesTableDataService.reportingFrameworkId).toEqual(2);
        });

    }); 



    describe('filter by parent id', () => {

        const landUsesCategories1 = [
            new LandUseCategory({ id: 1, reportingFrameworkId: null, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, reportingFrameworkId: null, name: "LandUseCategory 2", version: 1 }),
            new LandUseCategory({ id: 3, reportingFrameworkId: 1, name: "LandUseCategory 3", version: 1 }),
            new LandUseCategory({ id: 4, reportingFrameworkId: 1, name: "LandUseCategory 4", version: 1 }),
            new LandUseCategory({ id: 5, reportingFrameworkId: 2, name: "LandUseCategory 5", version: 1 }),
            new LandUseCategory({ id: 6, reportingFrameworkId: 2, name: "LandUseCategory 6", version: 1 })
        ]; 


        const landUsesCategories2 = [
            new LandUseCategory({ id: 1, reportingFrameworkId: null, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, reportingFrameworkId: null, name: "LandUseCategory 2", version: 1 })
        ];
        
        
        const landUsesCategories3 = [
            new LandUseCategory({ id: 3, reportingFrameworkId: 1, name: "LandUseCategory 3", version: 1 }),
            new LandUseCategory({ id: 4, reportingFrameworkId: 1, name: "LandUseCategory 4", version: 1 })
        ];         

        it('should return an observable containing Land Uses Categories Records that have no parent if a null parent id is specified', () => {

            expect(landUsesCategoriesTableDataService.filterByReportingFrameworkId(landUsesCategories1, null)).toEqual(landUsesCategories2);


        });


        it('should return an observable containing Land Uses Categories Records that have the specified parent if a parent id is specified', () => {

            expect(landUsesCategoriesTableDataService.filterByReportingFrameworkId(landUsesCategories1, 1)).toEqual(landUsesCategories3);

        });        


    });    



    describe('compare', () => {

        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(landUsesCategoriesTableDataService.compare("LandUseCategory 1", "LandUseCategory 2")).toEqual(-1);
            expect(landUsesCategoriesTableDataService.compare("LandUseCategory 1", "LandUseCategory 1")).toEqual(0);
            expect(landUsesCategoriesTableDataService.compare("LandUseCategory 2", "LandUseCategory 1")).toEqual(1);

            // numbers
            expect(landUsesCategoriesTableDataService.compare(1, 2)).toEqual(-1);
            expect(landUsesCategoriesTableDataService.compare(1, 1)).toEqual(0);
            expect(landUsesCategoriesTableDataService.compare(2, 1)).toEqual(1);            

        }); 


    }); 
    
    

    describe('sort', () => {

        const unsortedMockLandUsesCategories = [
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 }),
            new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 }),
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),                
        ];

        const sortedMockLandUsesCategories1 = [
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 }),
            new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 })
        ];  
        
        const sortedMockLandUsesCategories2 = [
            new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 }),                
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 }),
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),                
        ];   

        it('should sort LandUsesCategories records by table column in ascending or descending order', () => {

            expect(landUsesCategoriesTableDataService.sort(unsortedMockLandUsesCategories,"name", "asc")).toEqual(sortedMockLandUsesCategories1);
            expect(landUsesCategoriesTableDataService.sort(unsortedMockLandUsesCategories,"name", "desc")).toEqual(sortedMockLandUsesCategories2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a Land Use Category Record matches a search string or false otherwise', () => {

            // Define a couple of Mock LandUsesCategories
            let landUseCategory1: LandUseCategory = new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 });
            let landUseCategory2: LandUseCategory = new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 });
            let landUseCategory3: LandUseCategory = new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 });


            expect(landUsesCategoriesTableDataService.matches(landUseCategory1, "3")).toBeFalse(); 
            expect(landUsesCategoriesTableDataService.matches(landUseCategory2, "3")).toBeFalse(); 
            expect(landUsesCategoriesTableDataService.matches(landUseCategory3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockLandUsesCategories = [
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 }),
            new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 })
        ]; 
        
        const indexedMockLandUsesCategories = [
            new LandUseCategory({ pos: 1, id: 1, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ pos: 2, id: 2, name: "LandUseCategory 2", version: 1 }),
            new LandUseCategory({ pos: 3, id: 3, name: "LandUseCategory 3", version: 1 })
        ];             


        it('should index LandUsesCategories records', () => {
            expect(landUsesCategoriesTableDataService.index(unIndexedMockLandUsesCategories)).toEqual(indexedMockLandUsesCategories);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockLandUsesCategories = [
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 }),
            new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 })
        ]; 
        
        const paginatedMockLandUsesCategories = [
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 })
        ];         

        it('should paginate LandUsesCategories records by page and page sizes', () => {
            expect(landUsesCategoriesTableDataService.paginate(unpaginatedMockLandUsesCategories, 1, 2)).toEqual(paginatedMockLandUsesCategories);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(landUsesCategoriesTableDataService, 'transform');
            landUsesCategoriesTableDataService.set({})
            expect(landUsesCategoriesTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const landUsesCategories = [
            new LandUseCategory({ id: 3, name: "LandUseCategory 3", version: 1 }),
            new LandUseCategory({ id: 1, name: "LandUseCategory 1", version: 1 }),
            new LandUseCategory({ id: 2, name: "LandUseCategory 2", version: 1 })
        ];        

        it('should sorts, filter and paginate Land Uses Categories Records ', () => {

            spyOn(landUsesCategoriesTableDataService, 'filterByReportingFrameworkId').and.callThrough();
            spyOn(landUsesCategoriesTableDataService, 'sort').and.callThrough();;
            spyOn(landUsesCategoriesTableDataService, 'matches').and.callThrough();
            spyOn(landUsesCategoriesTableDataService, 'index').and.callThrough();
            spyOn(landUsesCategoriesTableDataService, 'paginate').and.callThrough();

            landUsesCategoriesTableDataService.transform(landUsesCategories);

            expect(landUsesCategoriesTableDataService.filterByReportingFrameworkId).toHaveBeenCalled();
            expect(landUsesCategoriesTableDataService.sort).toHaveBeenCalled();
            expect(landUsesCategoriesTableDataService.matches).toHaveBeenCalled();
            expect(landUsesCategoriesTableDataService.index).toHaveBeenCalled();
            expect(landUsesCategoriesTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
