
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Unit } from '../models/unit.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { UnitsDataService } from './units-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { UnitsRecordsTabulationService } from './units-records-tabulation.service';

describe('UnitsRecordsTabulationService', () => {

    let unitsDataService: UnitsDataService;
    let unitsTableDataService: UnitsRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, UnitsDataService, MessageService],
        });

        unitsDataService = TestBed.inject(UnitsDataService);
        unitsTableDataService = TestBed.inject(UnitsRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('units$', () => {

        it('should return transformed Units', () => {

            // Define a couple of mock Units
            const mockUnits = [
                new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
            ];


            unitsTableDataService.units$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockUnits);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/units/all`).flush(mockUnits);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should filter Units records by unit category id', () => {

            // Define a couple of mock Units

            const unfilteredMockUnits = [
                new Unit({ id: 1, unitCategoryId: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                new Unit({ id: 2, unitCategoryId: 1, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                new Unit({ id: 3, unitCategoryId: 2, name: "Unit 3", description: "Unit 3 Description", version: 1 })
            ]; 
            
            const filteredMockUnits = [
                new Unit({ id: 1, unitCategoryId: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                new Unit({ id: 2, unitCategoryId: 1, name: "Unit 2", description: "Unit 2 Description", version: 1 })
            ];             


            expect(unitsTableDataService.filterByUnitCategory(unfilteredMockUnits, 1)).toEqual(filteredMockUnits);            
        });          


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(unitsTableDataService.compare("Unit 1", "Unit 2")).toEqual(-1);
            expect(unitsTableDataService.compare("Unit 1", "Unit 1")).toEqual(0);
            expect(unitsTableDataService.compare("Unit 2", "Unit 1")).toEqual(1);

            // numbers
            expect(unitsTableDataService.compare(1, 2)).toEqual(-1);
            expect(unitsTableDataService.compare(1, 1)).toEqual(0);
            expect(unitsTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Units records by table column in ascending or descending order', () => {

            // Define a couple of mock Units

            const unsortedMockUnits = [
                new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 }),
                new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),                
            ];

            const sortedMockUnits1 = [
                new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
            ];  
            
            const sortedMockUnits2 = [
                new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 }),                
                new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),                
            ];            


            expect(unitsTableDataService.sort(unsortedMockUnits,"id", "asc")).toEqual(sortedMockUnits1);
            expect(unitsTableDataService.sort(unsortedMockUnits,"id", "desc")).toEqual(sortedMockUnits2);

            expect(unitsTableDataService.sort(unsortedMockUnits,"name", "asc")).toEqual(sortedMockUnits1);
            expect(unitsTableDataService.sort(unsortedMockUnits,"name", "desc")).toEqual(sortedMockUnits2);
            
            expect(unitsTableDataService.sort(unsortedMockUnits,"description", "asc")).toEqual(sortedMockUnits1);
            expect(unitsTableDataService.sort(unsortedMockUnits,"description", "desc")).toEqual(sortedMockUnits2);            
        }); 
        
        
        it('should return true if a Unit record matches a search string or false otherwise', () => {

            // Define a couple of mock Units
            let unit1: Unit = new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 });
            let unit2: Unit = new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 });
            let unit3: Unit = new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 });


            expect(unitsTableDataService.matches(unit1, "3")).toBeFalse(); 
            expect(unitsTableDataService.matches(unit2, "3")).toBeFalse(); 
            expect(unitsTableDataService.matches(unit3, "3")).toBeTrue();
        }); 

        it('should index Units records', () => {

            // Define a couple of mock Units

            const unIndexedMockUnits = [
                new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
            ]; 
            
            const indexedMockUnits = [
                new Unit({ pos: 1, id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                new Unit({ pos: 2, id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                new Unit({ pos: 3, id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
            ];             


            expect(unitsTableDataService.index(unIndexedMockUnits)).toEqual(indexedMockUnits);            
        }); 


        it('should paginate Units records by page and page sizes', () => {

            // Define a couple of mock Units

            const unpaginatedMockUnits = [
                new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
            ]; 
            
            const paginatedMockUnits = [
                new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 })
            ];             


            expect(unitsTableDataService.paginate(unpaginatedMockUnits, 1, 2)).toEqual(paginatedMockUnits);            
        });         

    });
});
