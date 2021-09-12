
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { Indicator } from '@modules/indicators/models/indicator.model';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';
import { AccountabilityRule } from '../models/accountability-rule.model';
import { AccountabilitiesRulesDataService } from './accountabilities-rules-data.service';
import { AccountabilitiesRulesRecordsTabulationService } from './accountabilities-rules-records-tabulation.service';


describe('AccountabilitiesRulesRecordsTabulationService', () => {

    let accountabilitiesRulesDataService: AccountabilitiesRulesDataService;
    let accountabilitiesRulesTableDataService: AccountabilitiesRulesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, AccountabilitiesRulesDataService, MessageService],
        });

        accountabilitiesRulesDataService = TestBed.inject(AccountabilitiesRulesDataService);
        accountabilitiesRulesTableDataService = TestBed.inject(AccountabilitiesRulesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('get accountabilitiesRules', () => {

 
        const accountabilitiesRules = [
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, version: 1 }),
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, version: 1 })
        ]; 
        
        const indicators = [
            new Indicator({ id: 1, name: "Number Of People Tested For Covid 19", version: 1 }),
            new Indicator({ id: 2, name: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
            new Indicator({ id: 3, name: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 })            
        ]; 

        it('should return an observable containing Accountabilities Rules Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedAccountabilitiesRules = [
                new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 }),
                new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
                new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, subsidiaryPartyTypeName: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 })
            ]; 
            
            // Mock get all request
            httpMock.match(`${environment.indicatorsBaseUrl}/api/v1/indicators/all`); 
            httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules/all`).flush(accountabilitiesRules);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to AccountabilitiesRules updates and verify the results
            accountabilitiesRulesTableDataService.accountabilitiesRules$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {
                    expect(response[0]).toEqual(sortedAccountabilitiesRules);
                });

        });


    });



    describe('get total', () => {

        const accountabilitiesRules = [
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, version: 1 }),
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, version: 1 })
        ];

        const indicators = [
            new Indicator({ id: 1, name: "Number Of People Tested For Covid 19", version: 1 }),
            new Indicator({ id: 2, name: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
            new Indicator({ id: 3, name: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 })            
        ];         

        it('should return an observable containing the total number of Accountabilities Rules Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock get all request
            httpMock.match(`${environment.indicatorsBaseUrl}/api/v1/indicators/all`); 
            httpMock.expectOne(`${environment.accountabilitiesRulesBaseUrl}/api/v1/accountabilities_rules/all`).flush(accountabilitiesRules);   

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            accountabilitiesRulesTableDataService.total$
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
            accountabilitiesRulesTableDataService.loading$
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
            expect(accountabilitiesRulesTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(accountabilitiesRulesTableDataService, 'set').and.callThrough();
            accountabilitiesRulesTableDataService.page = 2;
            expect(accountabilitiesRulesTableDataService.set).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(accountabilitiesRulesTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(accountabilitiesRulesTableDataService, 'set').and.callThrough();
            accountabilitiesRulesTableDataService.page = 10;
            expect(accountabilitiesRulesTableDataService.set).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(accountabilitiesRulesTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(accountabilitiesRulesTableDataService, 'set').and.callThrough();
            accountabilitiesRulesTableDataService.searchTerm = "test";
            expect(accountabilitiesRulesTableDataService.set).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(accountabilitiesRulesTableDataService, 'set');
            accountabilitiesRulesTableDataService.sortColumn = "name";
            expect(accountabilitiesRulesTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 

    
    
    
    describe('get parent id', () => {

        it('should return the currently set parent id', () => { 
            expect(accountabilitiesRulesTableDataService.accountabilityRuleId).toEqual(null);
        });

    });   
    
    
    describe('set parent id', () => {

        it('should update the currently set parent id and trigger the data transformation transformation exercise', () => { 
            spyOn(accountabilitiesRulesTableDataService, 'set').and.callThrough();
            accountabilitiesRulesTableDataService.accountabilityRuleId = 2;
            expect(accountabilitiesRulesTableDataService.set).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.accountabilityRuleId).toEqual(2);
        });

    }); 



    describe('filter by category id', () => {

        const accountabilitiesRules1 = [
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, version: 1 }),
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, version: 1 })
        ]; 


        const accountabilitiesRules2 = [
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, version: 1 })
        ];
        
        
        const accountabilitiesRules3 = [
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, version: 1 })
        ];       



        it('should return an observable containing Accountabilities Rules Records that have the specified parent if a parent id is specified', () => {

            expect(accountabilitiesRulesTableDataService.filterByAccountabilityTypeId(accountabilitiesRules1, 2)).toEqual(accountabilitiesRules2);
            expect(accountabilitiesRulesTableDataService.filterByAccountabilityTypeId(accountabilitiesRules1, 3)).toEqual(accountabilitiesRules3);

        });        


    });    



    describe('compare', () => {

        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(accountabilitiesRulesTableDataService.compare("Number Of People Tested For Covid 19", "Number Of People Confirmed As Being Covid 19 Positive")).toEqual(1);
            expect(accountabilitiesRulesTableDataService.compare("Number Of People Tested For Covid 19", "Number Of People Tested For Covid 19")).toEqual(0);
            expect(accountabilitiesRulesTableDataService.compare("Number Of People Confirmed As Being Covid 19 Positive", "Number Of People Tested For Covid 19")).toEqual(-1);

            // numbers
            expect(accountabilitiesRulesTableDataService.compare(1, 2)).toEqual(-1);
            expect(accountabilitiesRulesTableDataService.compare(1, 1)).toEqual(0);
            expect(accountabilitiesRulesTableDataService.compare(2, 1)).toEqual(1);            

        }); 


    }); 
    
    

    describe('sort', () => {

        const unsortedMockAccountabilitiesRules = [
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, subsidiaryPartyTypeName: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 }),
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 }),                
        ];

        const sortedMockAccountabilitiesRules1 = [
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, subsidiaryPartyTypeName: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 })
        ];  
        
        const sortedMockAccountabilitiesRules2 = [
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 }),               
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, subsidiaryPartyTypeName: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 })            
        ];   

        it('should sort Accountabilities Rules records by table column in ascending or descending order', () => {

            expect(accountabilitiesRulesTableDataService.sort(unsortedMockAccountabilitiesRules,"subsidiaryPartyTypeName", "asc")).toEqual(sortedMockAccountabilitiesRules1);
            expect(accountabilitiesRulesTableDataService.sort(unsortedMockAccountabilitiesRules,"subsidiaryPartyTypeName", "desc")).toEqual(sortedMockAccountabilitiesRules2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a Accountability Rule Record matches a search string or false otherwise', () => {

            // Define a couple of Mock AccountabilitiesRules
            let accountabilityRule1: AccountabilityRule = new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 });
            let accountabilityRule2: AccountabilityRule = new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 });
            let accountabilityRule3: AccountabilityRule = new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, subsidiaryPartyTypeName: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 });


            expect(accountabilitiesRulesTableDataService.matches(accountabilityRule1, "Tested")).toBeTrue(); 
            expect(accountabilitiesRulesTableDataService.matches(accountabilityRule1, "Confirmed")).toBeFalse(); 
            expect(accountabilitiesRulesTableDataService.matches(accountabilityRule2, "Aged")).toBeFalse(); 
            expect(accountabilitiesRulesTableDataService.matches(accountabilityRule3, "Aged")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockAccountabilitiesRules = [
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, subsidiaryPartyTypeName: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 })
        ]; 
        
        const indexedMockAccountabilitiesRules = [
            new AccountabilityRule({ pos: 1, id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 }),
            new AccountabilityRule({ pos: 2, id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
            new AccountabilityRule({ pos: 3, id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, subsidiaryPartyTypeName: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 })
        ];           

        it('should index Accountabilities Rules records', () => {
            expect(accountabilitiesRulesTableDataService.index(unIndexedMockAccountabilitiesRules)).toEqual(indexedMockAccountabilitiesRules);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockAccountabilitiesRules = [
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 }),
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, subsidiaryPartyTypeName: "Number Of Males Aged 0 - 9 Confirmed As Being Covid 19 Positive", version: 1 })
        ]; 
        
        const paginatedMockAccountabilitiesRules = [
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, subsidiaryPartyTypeName: "Number Of People Tested For Covid 19", version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, subsidiaryPartyTypeName: "Number Of People Confirmed As Being Covid 19 Positive", version: 1 })
        ];         

        it('should paginate Accountabilities Rules records by page and page sizes', () => {
            expect(accountabilitiesRulesTableDataService.paginate(unpaginatedMockAccountabilitiesRules, 1, 2)).toEqual(paginatedMockAccountabilitiesRules);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(accountabilitiesRulesTableDataService, 'transform');
            accountabilitiesRulesTableDataService.set({})
            expect(accountabilitiesRulesTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const accountabilitiesRules = [
            new AccountabilityRule({ id: 3, accountabilityRuleId: 3, subsidiaryPartyTypeId: 3, version: 1 }),
            new AccountabilityRule({ id: 1, accountabilityRuleId: 2, subsidiaryPartyTypeId: 1, version: 1 }),
            new AccountabilityRule({ id: 2, accountabilityRuleId: 3, subsidiaryPartyTypeId: 2, version: 1 })
        ];        

        it('should sorts, filter and paginate Accountabilities Rules Records ', () => {

            spyOn(accountabilitiesRulesTableDataService, 'filterByAccountabilityRuleId').and.callThrough();
            spyOn(accountabilitiesRulesTableDataService, 'appendSubsidiaryPartyTypeName').and.callThrough();
            spyOn(accountabilitiesRulesTableDataService, 'sort').and.callThrough();;
            spyOn(accountabilitiesRulesTableDataService, 'matches').and.callThrough();
            spyOn(accountabilitiesRulesTableDataService, 'index').and.callThrough();
            spyOn(accountabilitiesRulesTableDataService, 'paginate').and.callThrough();

            accountabilitiesRulesTableDataService.transform(accountabilitiesRules);

            expect(accountabilitiesRulesTableDataService.filterByAccountabilityTypeId).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.appendSubsidiaryPartyTypeName).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.sort).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.matches).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.index).toHaveBeenCalled();
            expect(accountabilitiesRulesTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
