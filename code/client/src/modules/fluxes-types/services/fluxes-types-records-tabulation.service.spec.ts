
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { FluxType } from '../models/flux-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { FluxesTypesDataService } from './fluxes-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';

import { FluxesTypesRecordsTabulationService } from './fluxes-types-records-tabulation.service';
import { environment } from 'environments/environment';

describe('FluxesTypesRecordsTabulationService', () => {

    let fluxesTypesDataService: FluxesTypesDataService;
    let fluxesTypesTableDataService: FluxesTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, FluxesTypesDataService, MessageService],
        });

        fluxesTypesDataService = TestBed.inject(FluxesTypesDataService);
        fluxesTypesTableDataService = TestBed.inject(FluxesTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('get fluxesTypes', () => {

 
        const fluxesTypes = [
            new FluxType({ id: 3, name: "FluxType 3", version: 1 }),
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),
            new FluxType({ id: 2, name: "FluxType 2", version: 1 })
        ];        

        it('should return an observable containing Fluxes Types Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedFluxesTypes = [
                new FluxType({ id: 1, name: "FluxType 1", version: 1 }),
                new FluxType({ id: 2, name: "FluxType 2", version: 1 }),
                new FluxType({ id: 3, name: "FluxType 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.fluxesTypesBaseUrl}/api/v1/fluxes_types/all`).flush(fluxesTypes);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to FluxesTypes updates and verify the results
            fluxesTypesTableDataService.fluxesTypes$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(sortedFluxesTypes);
                });



                

        });


    });



    describe('get total', () => {

        const fluxesTypes = [
            new FluxType({ id: 3, name: "FluxType 3", version: 1 }),
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),
            new FluxType({ id: 2, name: "FluxType 2", version: 1 })
        ];

        it('should return an observable containing the total number of Fluxes Types Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.fluxesTypesBaseUrl}/api/v1/fluxes_types/all`).flush(fluxesTypes);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            fluxesTypesTableDataService.total$
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
            fluxesTypesTableDataService.loading$
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
            expect(fluxesTypesTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(fluxesTypesTableDataService, 'set').and.callThrough();
            fluxesTypesTableDataService.page = 2;
            expect(fluxesTypesTableDataService.set).toHaveBeenCalled();
            expect(fluxesTypesTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(fluxesTypesTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(fluxesTypesTableDataService, 'set').and.callThrough();
            fluxesTypesTableDataService.page = 10;
            expect(fluxesTypesTableDataService.set).toHaveBeenCalled();
            expect(fluxesTypesTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(fluxesTypesTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(fluxesTypesTableDataService, 'set').and.callThrough();
            fluxesTypesTableDataService.searchTerm = "test";
            expect(fluxesTypesTableDataService.set).toHaveBeenCalled();
            expect(fluxesTypesTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(fluxesTypesTableDataService, 'set');
            fluxesTypesTableDataService.sortColumn = "name";
            expect(fluxesTypesTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 


    describe('compare', () => {

        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(fluxesTypesTableDataService.compare("FluxType 1", "FluxType 2")).toEqual(-1);
            expect(fluxesTypesTableDataService.compare("FluxType 1", "FluxType 1")).toEqual(0);
            expect(fluxesTypesTableDataService.compare("FluxType 2", "FluxType 1")).toEqual(1);

            // numbers
            expect(fluxesTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(fluxesTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(fluxesTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 


    }); 
    
    

    describe('sort', () => {

        const unsortedMockFluxesTypes = [
            new FluxType({ id: 2, name: "FluxType 2", version: 1 }),
            new FluxType({ id: 3, name: "FluxType 3", version: 1 }),
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),                
        ];

        const sortedMockFluxesTypes1 = [
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),
            new FluxType({ id: 2, name: "FluxType 2", version: 1 }),
            new FluxType({ id: 3, name: "FluxType 3", version: 1 })
        ];  
        
        const sortedMockFluxesTypes2 = [
            new FluxType({ id: 3, name: "FluxType 3", version: 1 }),                
            new FluxType({ id: 2, name: "FluxType 2", version: 1 }),
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),                
        ];   

        it('should sort Fluxes Types Records by table column in ascending or descending order', () => {

            expect(fluxesTypesTableDataService.sort(unsortedMockFluxesTypes,"name", "asc")).toEqual(sortedMockFluxesTypes1);
            expect(fluxesTypesTableDataService.sort(unsortedMockFluxesTypes,"name", "desc")).toEqual(sortedMockFluxesTypes2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a Flux Type record matches a search string or false otherwise', () => {

            // Define a couple of Mock FluxesTypes
            let fluxType1: FluxType = new FluxType({ id: 1, name: "FluxType 1", version: 1 });
            let fluxType2: FluxType = new FluxType({ id: 2, name: "FluxType 2", version: 1 });
            let fluxType3: FluxType = new FluxType({ id: 3, name: "FluxType 3", version: 1 });


            expect(fluxesTypesTableDataService.matches(fluxType1, "3")).toBeFalse(); 
            expect(fluxesTypesTableDataService.matches(fluxType2, "3")).toBeFalse(); 
            expect(fluxesTypesTableDataService.matches(fluxType3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockFluxesTypes = [
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),
            new FluxType({ id: 2, name: "FluxType 2", version: 1 }),
            new FluxType({ id: 3, name: "FluxType 3", version: 1 })
        ]; 
        
        const indexedMockFluxesTypes = [
            new FluxType({ pos: 1, id: 1, name: "FluxType 1", version: 1 }),
            new FluxType({ pos: 2, id: 2, name: "FluxType 2", version: 1 }),
            new FluxType({ pos: 3, id: 3, name: "FluxType 3", version: 1 })
        ];             


        it('should index Fluxes Types Records', () => {
            expect(fluxesTypesTableDataService.index(unIndexedMockFluxesTypes)).toEqual(indexedMockFluxesTypes);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockFluxesTypes = [
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),
            new FluxType({ id: 2, name: "FluxType 2", version: 1 }),
            new FluxType({ id: 3, name: "FluxType 3", version: 1 })
        ]; 
        
        const paginatedMockFluxesTypes = [
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),
            new FluxType({ id: 2, name: "FluxType 2", version: 1 })
        ];         

        it('should paginate Fluxes Types Records by page and page sizes', () => {
            expect(fluxesTypesTableDataService.paginate(unpaginatedMockFluxesTypes, 1, 2)).toEqual(paginatedMockFluxesTypes);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(fluxesTypesTableDataService, 'transform');
            fluxesTypesTableDataService.set({})
            expect(fluxesTypesTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const fluxesTypes = [
            new FluxType({ id: 3, name: "FluxType 3", version: 1 }),
            new FluxType({ id: 1, name: "FluxType 1", version: 1 }),
            new FluxType({ id: 2, name: "FluxType 2", version: 1 }) 
        ];        

        it('should sorts, filter and paginate Fluxes Types Records', () => {

            spyOn(fluxesTypesTableDataService, 'sort').and.callThrough();;
            spyOn(fluxesTypesTableDataService, 'matches').and.callThrough();
            spyOn(fluxesTypesTableDataService, 'index').and.callThrough();
            spyOn(fluxesTypesTableDataService, 'paginate').and.callThrough();

            fluxesTypesTableDataService.transform(fluxesTypes);

            expect(fluxesTypesTableDataService.sort).toHaveBeenCalled();
            expect(fluxesTypesTableDataService.matches).toHaveBeenCalled();
            expect(fluxesTypesTableDataService.index).toHaveBeenCalled();
            expect(fluxesTypesTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
