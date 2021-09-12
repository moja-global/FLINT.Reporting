
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { environment } from 'environments/environment';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';
import { Party } from '../models/party.model';
import { PartiesDataService } from './parties-data.service';
import { ParentPartiesRecordsTabulationService } from './parent-parties-records-tabulation.service';


describe('ParentPartiesRecordsTabulationService', () => {

    let partiesDataService: PartiesDataService;
    let partiesTableDataService: ParentPartiesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, PartiesDataService, MessageService],
        });

        partiesDataService = TestBed.inject(PartiesDataService);
        partiesTableDataService = TestBed.inject(ParentPartiesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('get parties', () => {

 
        const parties = [
            new Party({ id: 3, name: "Party 3", version: 1 }),
            new Party({ id: 1, name: "Party 1", version: 1 }),
            new Party({ id: 2, name: "Party 2", version: 1 })
        ];        

        it('should return an observable containing Parties Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedParties = [
                new Party({ id: 1, name: "Party 1", version: 1 }),
                new Party({ id: 2, name: "Party 2", version: 1 }),
                new Party({ id: 3, name: "Party 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.partiesBaseUrl}/api/v1/parties/all`).flush(parties);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to Parties updates and verify the results
            partiesTableDataService.parties$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {
                    expect(response[0]).toEqual(sortedParties);
                });



                

        });


    });



    describe('get total', () => {

        const parties = [
            new Party({ id: 3, name: "Party 3", version: 1 }),
            new Party({ id: 1, name: "Party 1", version: 1 }),
            new Party({ id: 2, name: "Party 2", version: 1 })
        ];

        it('should return an observable containing the total number of Parties Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.partiesBaseUrl}/api/v1/parties/all`).flush(parties);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            partiesTableDataService.total$
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
            partiesTableDataService.loading$
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
            expect(partiesTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(partiesTableDataService, 'set').and.callThrough();
            partiesTableDataService.page = 2;
            expect(partiesTableDataService.set).toHaveBeenCalled();
            expect(partiesTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(partiesTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(partiesTableDataService, 'set').and.callThrough();
            partiesTableDataService.page = 10;
            expect(partiesTableDataService.set).toHaveBeenCalled();
            expect(partiesTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(partiesTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(partiesTableDataService, 'set').and.callThrough();
            partiesTableDataService.searchTerm = "test";
            expect(partiesTableDataService.set).toHaveBeenCalled();
            expect(partiesTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(partiesTableDataService, 'set');
            partiesTableDataService.sortColumn = "name";
            expect(partiesTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 

    
    
    
    describe('get parent id', () => {

        it('should return the currently set parent id', () => { 
            expect(partiesTableDataService.partyTypeId).toEqual(null);
        });

    });   
    
    
    describe('set parent id', () => {

        it('should update the currently set parent id and trigger the data transformation transformation exercise', () => { 
            spyOn(partiesTableDataService, 'set').and.callThrough();
            partiesTableDataService.partyTypeId = 2;
            expect(partiesTableDataService.set).toHaveBeenCalled();
            expect(partiesTableDataService.partyTypeId).toEqual(2);
        });

    }); 



    describe('filter by parent id', () => {

        const parties1 = [
            new Party({ id: 1, partyTypeId: null, name: "Party 1", version: 1 }),
            new Party({ id: 2, partyTypeId: null, name: "Party 2", version: 1 }),
            new Party({ id: 3, partyTypeId: 1, name: "Party 3", version: 1 }),
            new Party({ id: 4, partyTypeId: 1, name: "Party 4", version: 1 }),
            new Party({ id: 5, partyTypeId: 2, name: "Party 5", version: 1 }),
            new Party({ id: 6, partyTypeId: 2, name: "Party 6", version: 1 })
        ]; 


        const parties2 = [
            new Party({ id: 1, partyTypeId: null, name: "Party 1", version: 1 }),
            new Party({ id: 2, partyTypeId: null, name: "Party 2", version: 1 })
        ];
        
        
        const parties3 = [
            new Party({ id: 3, partyTypeId: 1, name: "Party 3", version: 1 }),
            new Party({ id: 4, partyTypeId: 1, name: "Party 4", version: 1 })
        ];         

        it('should return an observable containing Parties Records that have no parent if a null parent id is specified', () => {

            expect(partiesTableDataService.filterByPartyTypeId(parties1, null)).toEqual(parties2);


        });


        it('should return an observable containing Parties Records that have the specified parent if a parent id is specified', () => {

            expect(partiesTableDataService.filterByPartyTypeId(parties1, 1)).toEqual(parties3);

        });        


    });    



    describe('compare', () => {

        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(partiesTableDataService.compare("Party 1", "Party 2")).toEqual(-1);
            expect(partiesTableDataService.compare("Party 1", "Party 1")).toEqual(0);
            expect(partiesTableDataService.compare("Party 2", "Party 1")).toEqual(1);

            // numbers
            expect(partiesTableDataService.compare(1, 2)).toEqual(-1);
            expect(partiesTableDataService.compare(1, 1)).toEqual(0);
            expect(partiesTableDataService.compare(2, 1)).toEqual(1);            

        }); 


    }); 
    
    

    describe('sort', () => {

        const unsortedMockParties = [
            new Party({ id: 2, name: "Party 2", version: 1 }),
            new Party({ id: 3, name: "Party 3", version: 1 }),
            new Party({ id: 1, name: "Party 1", version: 1 }),                
        ];

        const sortedMockParties1 = [
            new Party({ id: 1, name: "Party 1", version: 1 }),
            new Party({ id: 2, name: "Party 2", version: 1 }),
            new Party({ id: 3, name: "Party 3", version: 1 })
        ];  
        
        const sortedMockParties2 = [
            new Party({ id: 3, name: "Party 3", version: 1 }),                
            new Party({ id: 2, name: "Party 2", version: 1 }),
            new Party({ id: 1, name: "Party 1", version: 1 }),                
        ];   

        it('should sort Parties records by table column in ascending or descending order', () => {

            expect(partiesTableDataService.sort(unsortedMockParties,"name", "asc")).toEqual(sortedMockParties1);
            expect(partiesTableDataService.sort(unsortedMockParties,"name", "desc")).toEqual(sortedMockParties2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a Party Record matches a search string or false otherwise', () => {

            // Define a couple of Mock Parties
            let party1: Party = new Party({ id: 1, name: "Party 1", version: 1 });
            let party2: Party = new Party({ id: 2, name: "Party 2", version: 1 });
            let party3: Party = new Party({ id: 3, name: "Party 3", version: 1 });


            expect(partiesTableDataService.matches(party1, "3")).toBeFalse(); 
            expect(partiesTableDataService.matches(party2, "3")).toBeFalse(); 
            expect(partiesTableDataService.matches(party3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockParties = [
            new Party({ id: 1, name: "Party 1", version: 1 }),
            new Party({ id: 2, name: "Party 2", version: 1 }),
            new Party({ id: 3, name: "Party 3", version: 1 })
        ]; 
        
        const indexedMockParties = [
            new Party({ pos: 1, id: 1, name: "Party 1", version: 1 }),
            new Party({ pos: 2, id: 2, name: "Party 2", version: 1 }),
            new Party({ pos: 3, id: 3, name: "Party 3", version: 1 })
        ];             


        it('should index Parties records', () => {
            expect(partiesTableDataService.index(unIndexedMockParties)).toEqual(indexedMockParties);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockParties = [
            new Party({ id: 1, name: "Party 1", version: 1 }),
            new Party({ id: 2, name: "Party 2", version: 1 }),
            new Party({ id: 3, name: "Party 3", version: 1 })
        ]; 
        
        const paginatedMockParties = [
            new Party({ id: 1, name: "Party 1", version: 1 }),
            new Party({ id: 2, name: "Party 2", version: 1 })
        ];         

        it('should paginate Parties records by page and page sizes', () => {
            expect(partiesTableDataService.paginate(unpaginatedMockParties, 1, 2)).toEqual(paginatedMockParties);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(partiesTableDataService, 'transform');
            partiesTableDataService.set({})
            expect(partiesTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const parties = [
            new Party({ id: 3, name: "Party 3", version: 1 }),
            new Party({ id: 1, name: "Party 1", version: 1 }),
            new Party({ id: 2, name: "Party 2", version: 1 })
        ];        

        it('should sorts, filter and paginate Parties Records ', () => {

            spyOn(partiesTableDataService, 'filterByPartyTypeId').and.callThrough();
            spyOn(partiesTableDataService, 'sort').and.callThrough();;
            spyOn(partiesTableDataService, 'matches').and.callThrough();
            spyOn(partiesTableDataService, 'index').and.callThrough();
            spyOn(partiesTableDataService, 'paginate').and.callThrough();

            partiesTableDataService.transform(parties);

            expect(partiesTableDataService.filterByPartyTypeId).toHaveBeenCalled();
            expect(partiesTableDataService.sort).toHaveBeenCalled();
            expect(partiesTableDataService.matches).toHaveBeenCalled();
            expect(partiesTableDataService.index).toHaveBeenCalled();
            expect(partiesTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
