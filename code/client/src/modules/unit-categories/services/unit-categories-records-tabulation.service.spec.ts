
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { UnitCategory } from '../models/unit-category.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { UnitCategoriesDataService } from './unit-categories-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { UnitCategoriesRecordsTabulationService } from './unit-categories-records-tabulation.service';

describe('UnitCategoriesRecordsTabulationService', () => {

    let unitCategoriesDataService: UnitCategoriesDataService;
    let unitCategoriesTableDataService: UnitCategoriesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, UnitCategoriesDataService, MessageService],
        });

        unitCategoriesDataService = TestBed.inject(UnitCategoriesDataService);
        unitCategoriesTableDataService = TestBed.inject(UnitCategoriesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('unitCategories$', () => {

        it('should return transformed Unit Categories', () => {

            // Define a couple of mock Unit Categories
            const mockUnitCategories = [
                new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
            ];


            unitCategoriesTableDataService.unitCategories$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockUnitCategories);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/unit_categories/all`).flush(mockUnitCategories);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(unitCategoriesTableDataService.compare("UnitCategory 1", "UnitCategory 2")).toEqual(-1);
            expect(unitCategoriesTableDataService.compare("UnitCategory 1", "UnitCategory 1")).toEqual(0);
            expect(unitCategoriesTableDataService.compare("UnitCategory 2", "UnitCategory 1")).toEqual(1);

            // numbers
            expect(unitCategoriesTableDataService.compare(1, 2)).toEqual(-1);
            expect(unitCategoriesTableDataService.compare(1, 1)).toEqual(0);
            expect(unitCategoriesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Unit Categories records by table column in ascending or descending order', () => {

            // Define a couple of mock Unit Categories

            const unsortedMockUnitCategories = [
                new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 }),
                new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),                
            ];

            const sortedMockUnitCategories1 = [
                new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
            ];  
            
            const sortedMockUnitCategories2 = [
                new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 }),                
                new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),                
            ];            


            expect(unitCategoriesTableDataService.sort(unsortedMockUnitCategories,"id", "asc")).toEqual(sortedMockUnitCategories1);
            expect(unitCategoriesTableDataService.sort(unsortedMockUnitCategories,"id", "desc")).toEqual(sortedMockUnitCategories2);

            expect(unitCategoriesTableDataService.sort(unsortedMockUnitCategories,"name", "asc")).toEqual(sortedMockUnitCategories1);
            expect(unitCategoriesTableDataService.sort(unsortedMockUnitCategories,"name", "desc")).toEqual(sortedMockUnitCategories2);
            
            expect(unitCategoriesTableDataService.sort(unsortedMockUnitCategories,"description", "asc")).toEqual(sortedMockUnitCategories1);
            expect(unitCategoriesTableDataService.sort(unsortedMockUnitCategories,"description", "desc")).toEqual(sortedMockUnitCategories2);            
        }); 
        
        
        it('should return true if a Unit Category record matches a search string or false otherwise', () => {

            // Define a couple of mock Unit Categories
            let unitCategory1: UnitCategory = new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 });
            let unitCategory2: UnitCategory = new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 });
            let unitCategory3: UnitCategory = new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 });


            expect(unitCategoriesTableDataService.matches(unitCategory1, "3")).toBeFalse(); 
            expect(unitCategoriesTableDataService.matches(unitCategory2, "3")).toBeFalse(); 
            expect(unitCategoriesTableDataService.matches(unitCategory3, "3")).toBeTrue();
        }); 

        it('should index Unit Categories records', () => {

            // Define a couple of mock Unit Categories

            const unIndexedMockUnitCategories = [
                new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
            ]; 
            
            const indexedMockUnitCategories = [
                new UnitCategory({ pos: 1, id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                new UnitCategory({ pos: 2, id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                new UnitCategory({ pos: 3, id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
            ];             


            expect(unitCategoriesTableDataService.index(unIndexedMockUnitCategories)).toEqual(indexedMockUnitCategories);            
        }); 


        it('should paginate Unit Categories records by page and page sizes', () => {

            // Define a couple of mock Unit Categories

            const unpaginatedMockUnitCategories = [
                new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 }),
                new UnitCategory({ id: 3, name: "UnitCategory 3", description: "UnitCategory 3 Description", version: 1 })
            ]; 
            
            const paginatedMockUnitCategories = [
                new UnitCategory({ id: 1, name: "UnitCategory 1", description: "UnitCategory 1 Description", version: 1 }),
                new UnitCategory({ id: 2, name: "UnitCategory 2", description: "UnitCategory 2 Description", version: 1 })
            ];             


            expect(unitCategoriesTableDataService.paginate(unpaginatedMockUnitCategories, 1, 2)).toEqual(paginatedMockUnitCategories);            
        });         

    });
});
