
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AccountabilityType } from '../models/accountability-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { AccountabilitiesTypesDataService } from './accountabilities-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';

import { AccountabilitiesTypesRecordsTabulationService } from './accountabilities-types-records-tabulation.service';
import { environment } from 'environments/environment';

describe('AccountabilitiesTypesRecordsTabulationService', () => {

    let accountabilitiesTypesDataService: AccountabilitiesTypesDataService;
    let accountabilitiesTypesTableDataService: AccountabilitiesTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, AccountabilitiesTypesDataService, MessageService],
        });

        accountabilitiesTypesDataService = TestBed.inject(AccountabilitiesTypesDataService);
        accountabilitiesTypesTableDataService = TestBed.inject(AccountabilitiesTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('get accountabilitiesTypes', () => {

 
        const accountabilitiesTypes = [
            new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 }),
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 })
        ];        

        it('should return an observable containing Accountabilities Types Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedAccountabilitiesTypes = [
                new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),
                new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 }),
                new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types/all`).flush(accountabilitiesTypes);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to AccountabilitiesTypes updates and verify the results
            accountabilitiesTypesTableDataService.accountabilitiesTypes$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(sortedAccountabilitiesTypes);
                });



                

        });


    });



    describe('get total', () => {

        const accountabilitiesTypes = [
            new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 }),
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 })
        ];

        it('should return an observable containing the total number of Accountabilities Types Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.accountabilitiesTypesBaseUrl}/api/v1/accountabilities_types/all`).flush(accountabilitiesTypes);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            accountabilitiesTypesTableDataService.total$
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
            accountabilitiesTypesTableDataService.loading$
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
            expect(accountabilitiesTypesTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(accountabilitiesTypesTableDataService, 'set').and.callThrough();
            accountabilitiesTypesTableDataService.page = 2;
            expect(accountabilitiesTypesTableDataService.set).toHaveBeenCalled();
            expect(accountabilitiesTypesTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(accountabilitiesTypesTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(accountabilitiesTypesTableDataService, 'set').and.callThrough();
            accountabilitiesTypesTableDataService.page = 10;
            expect(accountabilitiesTypesTableDataService.set).toHaveBeenCalled();
            expect(accountabilitiesTypesTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(accountabilitiesTypesTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(accountabilitiesTypesTableDataService, 'set').and.callThrough();
            accountabilitiesTypesTableDataService.searchTerm = "test";
            expect(accountabilitiesTypesTableDataService.set).toHaveBeenCalled();
            expect(accountabilitiesTypesTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(accountabilitiesTypesTableDataService, 'set');
            accountabilitiesTypesTableDataService.sortColumn = "name";
            expect(accountabilitiesTypesTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 


    describe('compare', () => {

        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(accountabilitiesTypesTableDataService.compare("AccountabilityType 1", "AccountabilityType 2")).toEqual(-1);
            expect(accountabilitiesTypesTableDataService.compare("AccountabilityType 1", "AccountabilityType 1")).toEqual(0);
            expect(accountabilitiesTypesTableDataService.compare("AccountabilityType 2", "AccountabilityType 1")).toEqual(1);

            // numbers
            expect(accountabilitiesTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(accountabilitiesTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(accountabilitiesTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 


    }); 
    
    

    describe('sort', () => {

        const unsortedMockAccountabilitiesTypes = [
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 }),
            new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 }),
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),                
        ];

        const sortedMockAccountabilitiesTypes1 = [
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 }),
            new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 })
        ];  
        
        const sortedMockAccountabilitiesTypes2 = [
            new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 }),                
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 }),
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),                
        ];   

        it('should sort Accountabilities Types Records by table column in ascending or descending order', () => {

            expect(accountabilitiesTypesTableDataService.sort(unsortedMockAccountabilitiesTypes,"name", "asc")).toEqual(sortedMockAccountabilitiesTypes1);
            expect(accountabilitiesTypesTableDataService.sort(unsortedMockAccountabilitiesTypes,"name", "desc")).toEqual(sortedMockAccountabilitiesTypes2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a AccountabilityType record matches a search string or false otherwise', () => {

            // Define a couple of Mock AccountabilitiesTypes
            let accountabilityType1: AccountabilityType = new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 });
            let accountabilityType2: AccountabilityType = new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 });
            let accountabilityType3: AccountabilityType = new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 });


            expect(accountabilitiesTypesTableDataService.matches(accountabilityType1, "3")).toBeFalse(); 
            expect(accountabilitiesTypesTableDataService.matches(accountabilityType2, "3")).toBeFalse(); 
            expect(accountabilitiesTypesTableDataService.matches(accountabilityType3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockAccountabilitiesTypes = [
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 }),
            new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 })
        ]; 
        
        const indexedMockAccountabilitiesTypes = [
            new AccountabilityType({ pos: 1, id: 1, name: "AccountabilityType 1", version: 1 }),
            new AccountabilityType({ pos: 2, id: 2, name: "AccountabilityType 2", version: 1 }),
            new AccountabilityType({ pos: 3, id: 3, name: "AccountabilityType 3", version: 1 })
        ];             


        it('should index Accountabilities Types Records', () => {
            expect(accountabilitiesTypesTableDataService.index(unIndexedMockAccountabilitiesTypes)).toEqual(indexedMockAccountabilitiesTypes);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockAccountabilitiesTypes = [
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 }),
            new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 })
        ]; 
        
        const paginatedMockAccountabilitiesTypes = [
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 })
        ];         

        it('should paginate Accountabilities Types Records by page and page sizes', () => {
            expect(accountabilitiesTypesTableDataService.paginate(unpaginatedMockAccountabilitiesTypes, 1, 2)).toEqual(paginatedMockAccountabilitiesTypes);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(accountabilitiesTypesTableDataService, 'transform');
            accountabilitiesTypesTableDataService.set({})
            expect(accountabilitiesTypesTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const accountabilitiesTypes = [
            new AccountabilityType({ id: 3, name: "AccountabilityType 3", version: 1 }),
            new AccountabilityType({ id: 1, name: "AccountabilityType 1", version: 1 }),
            new AccountabilityType({ id: 2, name: "AccountabilityType 2", version: 1 }) 
        ];        

        it('should sorts, filter and paginate Accountabilities Types Records', () => {

            spyOn(accountabilitiesTypesTableDataService, 'sort').and.callThrough();;
            spyOn(accountabilitiesTypesTableDataService, 'matches').and.callThrough();
            spyOn(accountabilitiesTypesTableDataService, 'index').and.callThrough();
            spyOn(accountabilitiesTypesTableDataService, 'paginate').and.callThrough();

            accountabilitiesTypesTableDataService.transform(accountabilitiesTypes);

            expect(accountabilitiesTypesTableDataService.sort).toHaveBeenCalled();
            expect(accountabilitiesTypesTableDataService.matches).toHaveBeenCalled();
            expect(accountabilitiesTypesTableDataService.index).toHaveBeenCalled();
            expect(accountabilitiesTypesTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
