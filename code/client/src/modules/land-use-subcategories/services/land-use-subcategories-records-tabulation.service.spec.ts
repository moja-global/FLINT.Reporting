
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LandUseSubcategory } from '@modules/land-use-subcategories/models/land-use-subcategory.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { LandUseSubcategoriesDataService } from './land-use-subcategories-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { LandUseSubcategoriesRecordsTabulationService } from './land-use-subcategories-records-tabulation.service';

describe('LandUseSubcategoriesRecordsTabulationService', () => {

    let landUseSubcategoriesDataService: LandUseSubcategoriesDataService;
    let landUseSubcategoriesTableDataService: LandUseSubcategoriesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, LandUseSubcategoriesDataService, MessageService],
        });

        landUseSubcategoriesDataService = TestBed.inject(LandUseSubcategoriesDataService);
        landUseSubcategoriesTableDataService = TestBed.inject(LandUseSubcategoriesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('landUseSubcategories$', () => {

        it('should return transformed LandUseSubcategories', () => {

            // Define a couple of mock LandUseSubcategories
            const mockLandUseSubcategories = [
                new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
            ];


            landUseSubcategoriesTableDataService.landUseSubcategories$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockLandUseSubcategories);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_subcategories/all`).flush(mockLandUseSubcategories);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should filter Land Use Subcategories records by Reporting Framework id', () => {

            // Define a couple of mock LandUseSubcategories

            const unfilteredMockLandUseSubcategories = [
                new LandUseSubcategory({ id: 1, reportingFrameworkId: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                new LandUseSubcategory({ id: 2, reportingFrameworkId: 1, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                new LandUseSubcategory({ id: 3, reportingFrameworkId: 2, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
            ]; 
            
            const filteredMockLandUseSubcategories = [
                new LandUseSubcategory({ id: 1, reportingFrameworkId: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                new LandUseSubcategory({ id: 2, reportingFrameworkId: 1, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 })
            ];             


            expect(landUseSubcategoriesTableDataService.filterByReportingFramework(unfilteredMockLandUseSubcategories, 1)).toEqual(filteredMockLandUseSubcategories);            
        });          


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(landUseSubcategoriesTableDataService.compare("LandUseSubcategory 1", "LandUseSubcategory 2")).toEqual(-1);
            expect(landUseSubcategoriesTableDataService.compare("LandUseSubcategory 1", "LandUseSubcategory 1")).toEqual(0);
            expect(landUseSubcategoriesTableDataService.compare("LandUseSubcategory 2", "LandUseSubcategory 1")).toEqual(1);

            // numbers
            expect(landUseSubcategoriesTableDataService.compare(1, 2)).toEqual(-1);
            expect(landUseSubcategoriesTableDataService.compare(1, 1)).toEqual(0);
            expect(landUseSubcategoriesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Land Use Subcategories records by table column in ascending or descending order', () => {

            // Define a couple of mock LandUseSubcategories

            const unsortedMockLandUseSubcategories = [
                new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 }),
                new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),                
            ];

            const sortedMockLandUseSubcategories1 = [
                new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
            ];  
            
            const sortedMockLandUseSubcategories2 = [
                new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 }),                
                new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),                
            ];            


            expect(landUseSubcategoriesTableDataService.sort(unsortedMockLandUseSubcategories,"id", "asc")).toEqual(sortedMockLandUseSubcategories1);
            expect(landUseSubcategoriesTableDataService.sort(unsortedMockLandUseSubcategories,"id", "desc")).toEqual(sortedMockLandUseSubcategories2);

            expect(landUseSubcategoriesTableDataService.sort(unsortedMockLandUseSubcategories,"name", "asc")).toEqual(sortedMockLandUseSubcategories1);
            expect(landUseSubcategoriesTableDataService.sort(unsortedMockLandUseSubcategories,"name", "desc")).toEqual(sortedMockLandUseSubcategories2);
            
            expect(landUseSubcategoriesTableDataService.sort(unsortedMockLandUseSubcategories,"description", "asc")).toEqual(sortedMockLandUseSubcategories1);
            expect(landUseSubcategoriesTableDataService.sort(unsortedMockLandUseSubcategories,"description", "desc")).toEqual(sortedMockLandUseSubcategories2);            
        }); 
        
        
        it('should return true if a Land Use Subcategory record matches a search string or false otherwise', () => {

            // Define a couple of mock LandUseSubcategories
            let landUseSubcategory1: LandUseSubcategory = new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 });
            let landUseSubcategory2: LandUseSubcategory = new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 });
            let landUseSubcategory3: LandUseSubcategory = new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 });


            expect(landUseSubcategoriesTableDataService.matches(landUseSubcategory1, "3")).toBeFalse(); 
            expect(landUseSubcategoriesTableDataService.matches(landUseSubcategory2, "3")).toBeFalse(); 
            expect(landUseSubcategoriesTableDataService.matches(landUseSubcategory3, "3")).toBeTrue();
        }); 

        it('should index Land Use Subcategories records', () => {

            // Define a couple of mock LandUseSubcategories

            const unIndexedMockLandUseSubcategories = [
                new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
            ]; 
            
            const indexedMockLandUseSubcategories = [
                new LandUseSubcategory({ pos: 1, id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                new LandUseSubcategory({ pos: 2, id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                new LandUseSubcategory({ pos: 3, id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
            ];             


            expect(landUseSubcategoriesTableDataService.index(unIndexedMockLandUseSubcategories)).toEqual(indexedMockLandUseSubcategories);            
        }); 


        it('should paginate Land Use Subcategories records by page and page sizes', () => {

            // Define a couple of mock LandUseSubcategories

            const unpaginatedMockLandUseSubcategories = [
                new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 }),
                new LandUseSubcategory({ id: 3, name: "LandUseSubcategory 3", description: "LandUseSubcategory 3 Description", version: 1 })
            ]; 
            
            const paginatedMockLandUseSubcategories = [
                new LandUseSubcategory({ id: 1, name: "LandUseSubcategory 1", description: "LandUseSubcategory 1 Description", version: 1 }),
                new LandUseSubcategory({ id: 2, name: "LandUseSubcategory 2", description: "LandUseSubcategory 2 Description", version: 1 })
            ];             


            expect(landUseSubcategoriesTableDataService.paginate(unpaginatedMockLandUseSubcategories, 1, 2)).toEqual(paginatedMockLandUseSubcategories);            
        });         

    });
});
