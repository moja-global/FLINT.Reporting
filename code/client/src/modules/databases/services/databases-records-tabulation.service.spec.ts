
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Database } from '../models/database.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { DatabasesDataService } from './databases-data.service';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { DatabasesRecordsTabulationService } from './databases-records-tabulation.service';

describe('DatabasesRecordsTabulationService', () => {

    let databasesDataService: DatabasesDataService;
    let databasesTableDataService: DatabasesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, DatabasesDataService, MessageService],
        });

        databasesDataService = TestBed.inject(DatabasesDataService);
        databasesTableDataService = TestBed.inject(DatabasesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('databases$', () => {

        it('should return transformed Databases', () => {

            // Define a couple of mock Databases
            const mockDatabases = [
                new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
            ];


            databasesTableDataService.databases$
                .pipe(
                    take(2),
                    toArray()
                )
                .subscribe(response => {

                    expect(response.length).toEqual(2)
                    expect(response[0]).toEqual([]);
                    expect(response[1]).toEqual(mockDatabases);
                });

            // Mock the get all request
            httpMock.expectOne(`${environment.baseUrl}/api/v1/databases/all`).flush(mockDatabases);

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();
        });


        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(databasesTableDataService.compare("Database 1", "Database 2")).toEqual(-1);
            expect(databasesTableDataService.compare("Database 1", "Database 1")).toEqual(0);
            expect(databasesTableDataService.compare("Database 2", "Database 1")).toEqual(1);

            // numbers
            expect(databasesTableDataService.compare(1, 2)).toEqual(-1);
            expect(databasesTableDataService.compare(1, 1)).toEqual(0);
            expect(databasesTableDataService.compare(2, 1)).toEqual(1);            

        }); 
        
        
        it('should sort Databases records by table column in ascending or descending order', () => {

            // Define a couple of mock Databases

            const unsortedMockDatabases = [
                new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 }),
                new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),                
            ];

            const sortedMockDatabases1 = [
                new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
            ];  
            
            const sortedMockDatabases2 = [
                new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 }),                
                new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),                
            ];            


            expect(databasesTableDataService.sort(unsortedMockDatabases,"id", "asc")).toEqual(sortedMockDatabases1);
            expect(databasesTableDataService.sort(unsortedMockDatabases,"id", "desc")).toEqual(sortedMockDatabases2);

            expect(databasesTableDataService.sort(unsortedMockDatabases,"name", "asc")).toEqual(sortedMockDatabases1);
            expect(databasesTableDataService.sort(unsortedMockDatabases,"name", "desc")).toEqual(sortedMockDatabases2);
            
            expect(databasesTableDataService.sort(unsortedMockDatabases,"description", "asc")).toEqual(sortedMockDatabases1);
            expect(databasesTableDataService.sort(unsortedMockDatabases,"description", "desc")).toEqual(sortedMockDatabases2);            
        }); 
        
        
        it('should return true if a Database record matches a search string or false otherwise', () => {

            // Define a couple of mock Databases
            let database1: Database = new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 });
            let database2: Database = new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 });
            let database3: Database = new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 });


            expect(databasesTableDataService.matches(database1, "3")).toBeFalse(); 
            expect(databasesTableDataService.matches(database2, "3")).toBeFalse(); 
            expect(databasesTableDataService.matches(database3, "3")).toBeTrue();
        }); 

        it('should index Databases records', () => {

            // Define a couple of mock Databases

            const unIndexedMockDatabases = [
                new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
            ]; 
            
            const indexedMockDatabases = [
                new Database({ pos: 1, id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                new Database({ pos: 2, id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                new Database({ pos: 3, id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
            ];             


            expect(databasesTableDataService.index(unIndexedMockDatabases)).toEqual(indexedMockDatabases);            
        }); 


        it('should paginate Databases records by page and page sizes', () => {

            // Define a couple of mock Databases

            const unpaginatedMockDatabases = [
                new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 }),
                new Database({ id: 3, name: "Database 3", description: "Database 3 Description", version: 1 })
            ]; 
            
            const paginatedMockDatabases = [
                new Database({ id: 1, name: "Database 1", description: "Database 1 Description", version: 1 }),
                new Database({ id: 2, name: "Database 2", description: "Database 2 Description", version: 1 })
            ];             


            expect(databasesTableDataService.paginate(unpaginatedMockDatabases, 1, 2)).toEqual(paginatedMockDatabases);            
        });         

    });
});
