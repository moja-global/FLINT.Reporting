
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { EmissionType } from '../models/emission-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { EmissionsTypesDataService } from './emissions-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';

import { EmissionsTypesRecordsTabulationService } from './emissions-types-records-tabulation.service';
import { environment } from 'environments/environment';

describe('EmissionsTypesRecordsTabulationService', () => {

    let emissionsTypesDataService: EmissionsTypesDataService;
    let emissionsTypesTableDataService: EmissionsTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, EmissionsTypesDataService, MessageService],
        });

        emissionsTypesDataService = TestBed.inject(EmissionsTypesDataService);
        emissionsTypesTableDataService = TestBed.inject(EmissionsTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('get emissionsTypes', () => {

 
        const emissionsTypes = [
            new EmissionType({ id: 3, name: "EmissionType 3", version: 1 }),
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 })
        ];        

        it('should return an observable containing Emissions Types Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedEmissionsTypes = [
                new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),
                new EmissionType({ id: 2, name: "EmissionType 2", version: 1 }),
                new EmissionType({ id: 3, name: "EmissionType 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emissions_types/all`).flush(emissionsTypes);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to EmissionsTypes updates and verify the results
            emissionsTypesTableDataService.emissionsTypes$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(sortedEmissionsTypes);
                });



                

        });


    });



    describe('get total', () => {

        const emissionsTypes = [
            new EmissionType({ id: 3, name: "EmissionType 3", version: 1 }),
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 })
        ];

        it('should return an observable containing the total number of Emissions Types Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.emissionsTypesBaseUrl}/api/v1/emissions_types/all`).flush(emissionsTypes);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            emissionsTypesTableDataService.total$
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
            emissionsTypesTableDataService.loading$
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
            expect(emissionsTypesTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(emissionsTypesTableDataService, 'set').and.callThrough();
            emissionsTypesTableDataService.page = 2;
            expect(emissionsTypesTableDataService.set).toHaveBeenCalled();
            expect(emissionsTypesTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(emissionsTypesTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(emissionsTypesTableDataService, 'set').and.callThrough();
            emissionsTypesTableDataService.page = 10;
            expect(emissionsTypesTableDataService.set).toHaveBeenCalled();
            expect(emissionsTypesTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(emissionsTypesTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(emissionsTypesTableDataService, 'set').and.callThrough();
            emissionsTypesTableDataService.searchTerm = "test";
            expect(emissionsTypesTableDataService.set).toHaveBeenCalled();
            expect(emissionsTypesTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(emissionsTypesTableDataService, 'set');
            emissionsTypesTableDataService.sortColumn = "name";
            expect(emissionsTypesTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 


    describe('compare', () => {

        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(emissionsTypesTableDataService.compare("EmissionType 1", "EmissionType 2")).toEqual(-1);
            expect(emissionsTypesTableDataService.compare("EmissionType 1", "EmissionType 1")).toEqual(0);
            expect(emissionsTypesTableDataService.compare("EmissionType 2", "EmissionType 1")).toEqual(1);

            // numbers
            expect(emissionsTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(emissionsTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(emissionsTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 


    }); 
    
    

    describe('sort', () => {

        const unsortedMockEmissionsTypes = [
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 }),
            new EmissionType({ id: 3, name: "EmissionType 3", version: 1 }),
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),                
        ];

        const sortedMockEmissionsTypes1 = [
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 }),
            new EmissionType({ id: 3, name: "EmissionType 3", version: 1 })
        ];  
        
        const sortedMockEmissionsTypes2 = [
            new EmissionType({ id: 3, name: "EmissionType 3", version: 1 }),                
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 }),
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),                
        ];   

        it('should sort Emissions Types Records by table column in ascending or descending order', () => {

            expect(emissionsTypesTableDataService.sort(unsortedMockEmissionsTypes,"name", "asc")).toEqual(sortedMockEmissionsTypes1);
            expect(emissionsTypesTableDataService.sort(unsortedMockEmissionsTypes,"name", "desc")).toEqual(sortedMockEmissionsTypes2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a Emission Type record matches a search string or false otherwise', () => {

            // Define a couple of Mock EmissionsTypes
            let emissionType1: EmissionType = new EmissionType({ id: 1, name: "EmissionType 1", version: 1 });
            let emissionType2: EmissionType = new EmissionType({ id: 2, name: "EmissionType 2", version: 1 });
            let emissionType3: EmissionType = new EmissionType({ id: 3, name: "EmissionType 3", version: 1 });


            expect(emissionsTypesTableDataService.matches(emissionType1, "3")).toBeFalse(); 
            expect(emissionsTypesTableDataService.matches(emissionType2, "3")).toBeFalse(); 
            expect(emissionsTypesTableDataService.matches(emissionType3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockEmissionsTypes = [
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 }),
            new EmissionType({ id: 3, name: "EmissionType 3", version: 1 })
        ]; 
        
        const indexedMockEmissionsTypes = [
            new EmissionType({ pos: 1, id: 1, name: "EmissionType 1", version: 1 }),
            new EmissionType({ pos: 2, id: 2, name: "EmissionType 2", version: 1 }),
            new EmissionType({ pos: 3, id: 3, name: "EmissionType 3", version: 1 })
        ];             


        it('should index Emissions Types Records', () => {
            expect(emissionsTypesTableDataService.index(unIndexedMockEmissionsTypes)).toEqual(indexedMockEmissionsTypes);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockEmissionsTypes = [
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 }),
            new EmissionType({ id: 3, name: "EmissionType 3", version: 1 })
        ]; 
        
        const paginatedMockEmissionsTypes = [
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 })
        ];         

        it('should paginate Emissions Types Records by page and page sizes', () => {
            expect(emissionsTypesTableDataService.paginate(unpaginatedMockEmissionsTypes, 1, 2)).toEqual(paginatedMockEmissionsTypes);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(emissionsTypesTableDataService, 'transform');
            emissionsTypesTableDataService.set({})
            expect(emissionsTypesTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const emissionsTypes = [
            new EmissionType({ id: 3, name: "EmissionType 3", version: 1 }),
            new EmissionType({ id: 1, name: "EmissionType 1", version: 1 }),
            new EmissionType({ id: 2, name: "EmissionType 2", version: 1 }) 
        ];        

        it('should sorts, filter and paginate Emissions Types Records', () => {

            spyOn(emissionsTypesTableDataService, 'sort').and.callThrough();;
            spyOn(emissionsTypesTableDataService, 'matches').and.callThrough();
            spyOn(emissionsTypesTableDataService, 'index').and.callThrough();
            spyOn(emissionsTypesTableDataService, 'paginate').and.callThrough();

            emissionsTypesTableDataService.transform(emissionsTypes);

            expect(emissionsTypesTableDataService.sort).toHaveBeenCalled();
            expect(emissionsTypesTableDataService.matches).toHaveBeenCalled();
            expect(emissionsTypesTableDataService.index).toHaveBeenCalled();
            expect(emissionsTypesTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
