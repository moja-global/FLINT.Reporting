
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { CoverType } from '../models/cover-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { CoversTypesDataService } from './covers-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';

import { CoversTypesRecordsTabulationService } from './covers-types-records-tabulation.service';
import { environment } from 'environments/environment';

describe('CoversTypesRecordsTabulationService', () => {

    let coversTypesDataService: CoversTypesDataService;
    let coversTypesTableDataService: CoversTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, CoversTypesDataService, MessageService],
        });

        coversTypesDataService = TestBed.inject(CoversTypesDataService);
        coversTypesTableDataService = TestBed.inject(CoversTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('get coversTypes', () => {

 
        const coversTypes = [
            new CoverType({ id: 3, name: "CoverType 3", version: 1 }),
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),
            new CoverType({ id: 2, name: "CoverType 2", version: 1 })
        ];        

        it('should return an observable containing Covers Types Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedCoversTypes = [
                new CoverType({ id: 1, name: "CoverType 1", version: 1 }),
                new CoverType({ id: 2, name: "CoverType 2", version: 1 }),
                new CoverType({ id: 3, name: "CoverType 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/covers_types/all`).flush(coversTypes);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to CoversTypes updates and verify the results
            coversTypesTableDataService.coversTypes$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(sortedCoversTypes);
                });



                

        });


    });



    describe('get total', () => {

        const coversTypes = [
            new CoverType({ id: 3, name: "CoverType 3", version: 1 }),
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),
            new CoverType({ id: 2, name: "CoverType 2", version: 1 })
        ];

        it('should return an observable containing the total number of Covers Types Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.coversTypesBaseUrl}/api/v1/covers_types/all`).flush(coversTypes);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            coversTypesTableDataService.total$
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
            coversTypesTableDataService.loading$
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
            expect(coversTypesTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(coversTypesTableDataService, 'set').and.callThrough();
            coversTypesTableDataService.page = 2;
            expect(coversTypesTableDataService.set).toHaveBeenCalled();
            expect(coversTypesTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(coversTypesTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(coversTypesTableDataService, 'set').and.callThrough();
            coversTypesTableDataService.page = 10;
            expect(coversTypesTableDataService.set).toHaveBeenCalled();
            expect(coversTypesTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(coversTypesTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(coversTypesTableDataService, 'set').and.callThrough();
            coversTypesTableDataService.searchTerm = "test";
            expect(coversTypesTableDataService.set).toHaveBeenCalled();
            expect(coversTypesTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(coversTypesTableDataService, 'set');
            coversTypesTableDataService.sortColumn = "name";
            expect(coversTypesTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 


    describe('compare', () => {

        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(coversTypesTableDataService.compare("CoverType 1", "CoverType 2")).toEqual(-1);
            expect(coversTypesTableDataService.compare("CoverType 1", "CoverType 1")).toEqual(0);
            expect(coversTypesTableDataService.compare("CoverType 2", "CoverType 1")).toEqual(1);

            // numbers
            expect(coversTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(coversTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(coversTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 


    }); 
    
    

    describe('sort', () => {

        const unsortedMockCoversTypes = [
            new CoverType({ id: 2, name: "CoverType 2", version: 1 }),
            new CoverType({ id: 3, name: "CoverType 3", version: 1 }),
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),                
        ];

        const sortedMockCoversTypes1 = [
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),
            new CoverType({ id: 2, name: "CoverType 2", version: 1 }),
            new CoverType({ id: 3, name: "CoverType 3", version: 1 })
        ];  
        
        const sortedMockCoversTypes2 = [
            new CoverType({ id: 3, name: "CoverType 3", version: 1 }),                
            new CoverType({ id: 2, name: "CoverType 2", version: 1 }),
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),                
        ];   

        it('should sort Covers Types Records by table column in ascending or descending order', () => {

            expect(coversTypesTableDataService.sort(unsortedMockCoversTypes,"name", "asc")).toEqual(sortedMockCoversTypes1);
            expect(coversTypesTableDataService.sort(unsortedMockCoversTypes,"name", "desc")).toEqual(sortedMockCoversTypes2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a Cover Type record matches a search string or false otherwise', () => {

            // Define a couple of Mock CoversTypes
            let coverType1: CoverType = new CoverType({ id: 1, name: "CoverType 1", version: 1 });
            let coverType2: CoverType = new CoverType({ id: 2, name: "CoverType 2", version: 1 });
            let coverType3: CoverType = new CoverType({ id: 3, name: "CoverType 3", version: 1 });


            expect(coversTypesTableDataService.matches(coverType1, "3")).toBeFalse(); 
            expect(coversTypesTableDataService.matches(coverType2, "3")).toBeFalse(); 
            expect(coversTypesTableDataService.matches(coverType3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockCoversTypes = [
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),
            new CoverType({ id: 2, name: "CoverType 2", version: 1 }),
            new CoverType({ id: 3, name: "CoverType 3", version: 1 })
        ]; 
        
        const indexedMockCoversTypes = [
            new CoverType({ pos: 1, id: 1, name: "CoverType 1", version: 1 }),
            new CoverType({ pos: 2, id: 2, name: "CoverType 2", version: 1 }),
            new CoverType({ pos: 3, id: 3, name: "CoverType 3", version: 1 })
        ];             


        it('should index Covers Types Records', () => {
            expect(coversTypesTableDataService.index(unIndexedMockCoversTypes)).toEqual(indexedMockCoversTypes);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockCoversTypes = [
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),
            new CoverType({ id: 2, name: "CoverType 2", version: 1 }),
            new CoverType({ id: 3, name: "CoverType 3", version: 1 })
        ]; 
        
        const paginatedMockCoversTypes = [
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),
            new CoverType({ id: 2, name: "CoverType 2", version: 1 })
        ];         

        it('should paginate Covers Types Records by page and page sizes', () => {
            expect(coversTypesTableDataService.paginate(unpaginatedMockCoversTypes, 1, 2)).toEqual(paginatedMockCoversTypes);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(coversTypesTableDataService, 'transform');
            coversTypesTableDataService.set({})
            expect(coversTypesTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const coversTypes = [
            new CoverType({ id: 3, name: "CoverType 3", version: 1 }),
            new CoverType({ id: 1, name: "CoverType 1", version: 1 }),
            new CoverType({ id: 2, name: "CoverType 2", version: 1 }) 
        ];        

        it('should sorts, filter and paginate Covers Types Records', () => {

            spyOn(coversTypesTableDataService, 'sort').and.callThrough();;
            spyOn(coversTypesTableDataService, 'matches').and.callThrough();
            spyOn(coversTypesTableDataService, 'index').and.callThrough();
            spyOn(coversTypesTableDataService, 'paginate').and.callThrough();

            coversTypesTableDataService.transform(coversTypes);

            expect(coversTypesTableDataService.sort).toHaveBeenCalled();
            expect(coversTypesTableDataService.matches).toHaveBeenCalled();
            expect(coversTypesTableDataService.index).toHaveBeenCalled();
            expect(coversTypesTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
