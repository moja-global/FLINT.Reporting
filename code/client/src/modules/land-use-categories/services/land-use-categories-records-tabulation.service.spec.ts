
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LandUseCategory } from '@modules/land-use-categories/models/land-use-category.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { LandUseCategoriesDataService } from './land-use-categories-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { LandUseCategoriesRecordsTabulationService } from './land-use-categories-records-tabulation.service';

describe('LandUseCategoriesRecordsTabulationService', () => {


    let landUseCategoriesDataService: LandUseCategoriesDataService;
    let landUseCategoriesTableDataService: LandUseCategoriesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, LandUseCategoriesDataService, MessageService],
        });

        landUseCategoriesDataService = TestBed.inject(LandUseCategoriesDataService);
        landUseCategoriesTableDataService = TestBed.inject(LandUseCategoriesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('landUseCategories$', () => {
 
        it('should return transformed Land Use Categories', () => {

            // Define a couple of mock LandUseCategories
            const mockLandUseCategories = [
                new LandUseCategory({ id: 1, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Forest land", version: 1 }),
                new LandUseCategory({ id: 9, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 2, name: "Cropland", version: 1 }),
                new LandUseCategory({ id: 17, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 3, name: "Grassland", version: 1 })
            ];

            landUseCategoriesTableDataService.reportingFrameworkId = 1;


            landUseCategoriesTableDataService.landUseCategories$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(3)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockLandUseCategories);

                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/land_use_categories/all`).flush(mockLandUseCategories);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should filter Land Use Categories records by Reporting Framework Id', () => {

            // Define a couple of mock Land Use Categories

            const unfilteredMockLandUseCategories = [
                new LandUseCategory({ id: 1, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Forest land", version: 1 }),
                new LandUseCategory({ id: 2, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Peat land", version: 1 })
            ]; 
            
            const filteredMockLandUseCategories = [
                new LandUseCategory({ id: 2, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Peat land", version: 1 })
            ];             


            expect(landUseCategoriesTableDataService.filterByReportingFramework(unfilteredMockLandUseCategories, 2)).toEqual(filteredMockLandUseCategories);            
        });  
        
        
        it('should filter Land Use Categories records by the Parent Land Use Category Id', () => {

            // Define a couple of mock Land Use Categories

            const unfilteredMockLandUseCategories = [
                new LandUseCategory({ id: 1, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Forest land", version: 1 }),
                new LandUseCategory({ id: 2, reportingFrameworkId: 1, parentLandUseCategoryId: 1, coverTypeId: 2, name: "Forest land Remaining Forest land", version: 1 }),
                new LandUseCategory({ id: 3, reportingFrameworkId: 1, parentLandUseCategoryId: 1, coverTypeId: 3, name: "Land Converted To Forest land", version: 1 })
            ]; 
            
            const filteredMockLandUseCategories = [
                new LandUseCategory({ id: 2, reportingFrameworkId: 1, parentLandUseCategoryId: 1, coverTypeId: 2, name: "Forest land Remaining Forest land", version: 1 }),
                new LandUseCategory({ id: 3, reportingFrameworkId: 1, parentLandUseCategoryId: 1, coverTypeId: 3, name: "Land Converted To Forest land", version: 1 })
            ];             


            expect(landUseCategoriesTableDataService.filterByParentLandUseCategory(unfilteredMockLandUseCategories, 1)).toEqual(filteredMockLandUseCategories);            
        });         


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {          

            // strings
            expect(landUseCategoriesTableDataService.compare("Forest land", "Grassland 1")).toEqual(-1);
            expect(landUseCategoriesTableDataService.compare("Forest land", "Forest land")).toEqual(0);
            expect(landUseCategoriesTableDataService.compare("Forest land", "Cropland")).toEqual(1);

            // numbers
            expect(landUseCategoriesTableDataService.compare(1, 9)).toEqual(-1);
            expect(landUseCategoriesTableDataService.compare(1, 1)).toEqual(0);
            expect(landUseCategoriesTableDataService.compare(17, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Land Use Categories records by table column in ascending or descending order', () => {

            // Define a couple of mock LandUseCategories

            const unsortedMockLandUseCategories = [
                new LandUseCategory({ id: 1, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Forest land", version: 1 }),
                new LandUseCategory({ id: 9, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 2, name: "Cropland", version: 1 }),
                new LandUseCategory({ id: 17, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 3, name: "Grassland", version: 1 })                 
            ];

            const sortedMockLandUseCategories1 = [
                new LandUseCategory({ id: 1, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Forest land", version: 1 }),
                new LandUseCategory({ id: 9, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 2, name: "Cropland", version: 1 }),
                new LandUseCategory({ id: 17, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 3, name: "Grassland", version: 1 }) 
            ];  
            
            const sortedMockLandUseCategories2 = [
                new LandUseCategory({ id: 17, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 3, name: "Grassland", version: 1 }),
                new LandUseCategory({ id: 9, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 2, name: "Cropland", version: 1 }),
                new LandUseCategory({ id: 1, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Forest land", version: 1 })             
            ]; 
            
            const sortedMockLandUseCategories3 = [
                new LandUseCategory({ id: 9, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 2, name: "Cropland", version: 1 }),
                new LandUseCategory({ id: 1, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Forest land", version: 1 }),
                new LandUseCategory({ id: 17, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 3, name: "Grassland", version: 1 }) 
            ];  
            
            const sortedMockLandUseCategories4 = [
                new LandUseCategory({ id: 17, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 3, name: "Grassland", version: 1 }),
                new LandUseCategory({ id: 1, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 1, name: "Forest land", version: 1 }),
                new LandUseCategory({ id: 9, reportingFrameworkId: 1, parentLandUseCategoryId: null, coverTypeId: 2, name: "Cropland", version: 1 })
                              
            ];            


            expect(landUseCategoriesTableDataService.sort(unsortedMockLandUseCategories,"id", "asc")).toEqual(sortedMockLandUseCategories1);
            expect(landUseCategoriesTableDataService.sort(unsortedMockLandUseCategories,"id", "desc")).toEqual(sortedMockLandUseCategories2);

            expect(landUseCategoriesTableDataService.sort(unsortedMockLandUseCategories,"name", "asc")).toEqual(sortedMockLandUseCategories3);
            expect(landUseCategoriesTableDataService.sort(unsortedMockLandUseCategories,"name", "desc")).toEqual(sortedMockLandUseCategories4);
                       
        }); 
        
        
        it('should return true if a Land Use Category record matches a search string or false otherwise', () => {

            // Define a couple of mock LandUseCategories
            let landUseCategory1: LandUseCategory = new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 });
            let landUseCategory2: LandUseCategory = new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 });
            let landUseCategory3: LandUseCategory = new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 });


            expect(landUseCategoriesTableDataService.matches(landUseCategory1, "3")).toBeFalse(); 
            expect(landUseCategoriesTableDataService.matches(landUseCategory2, "3")).toBeFalse(); 
            expect(landUseCategoriesTableDataService.matches(landUseCategory3, "3")).toBeTrue();
        }); 

        it('should index Land Use Categories records', () => {

            // Define a couple of mock LandUseCategories

            const unIndexedMockLandUseCategories = [
                new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 })
            ]; 
            
            const indexedMockLandUseCategories = [
                new LandUseCategory({ pos: 1, id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                new LandUseCategory({ pos: 2, id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                new LandUseCategory({ pos: 3, id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 })
            ];             


            expect(landUseCategoriesTableDataService.index(unIndexedMockLandUseCategories)).toEqual(indexedMockLandUseCategories);            
        }); 


        it('should paginate Land Use Categories records by page and page sizes', () => {

            // Define a couple of mock LandUseCategories

            const unpaginatedMockLandUseCategories = [
                new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 }),
                new LandUseCategory({ id: 3, name: "LandUseCategory 3", description: "LandUseCategory 3 Description", version: 1 })
            ]; 
            
            const paginatedMockLandUseCategories = [
                new LandUseCategory({ id: 1, name: "LandUseCategory 1", description: "LandUseCategory 1 Description", version: 1 }),
                new LandUseCategory({ id: 2, name: "LandUseCategory 2", description: "LandUseCategory 2 Description", version: 1 })
            ];             


            expect(landUseCategoriesTableDataService.paginate(unpaginatedMockLandUseCategories, 1, 2)).toEqual(paginatedMockLandUseCategories);            
        });         

    });
});
