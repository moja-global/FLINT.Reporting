
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { PartyType } from '../models/party-type.model';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { PartiesTypesDataService } from './parties-types-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, takeLast, toArray } from 'rxjs/operators';

import { PartiesTypesRecordsTabulationService } from './parties-types-records-tabulation.service';
import { environment } from 'environments/environment';

describe('PartiesTypesRecordsTabulationService', () => {

    let partiesTypesDataService: PartiesTypesDataService;
    let partiesTypesTableDataService: PartiesTypesRecordsTabulationService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, PartiesTypesDataService, MessageService],
        });

        partiesTypesDataService = TestBed.inject(PartiesTypesDataService);
        partiesTypesTableDataService = TestBed.inject(PartiesTypesRecordsTabulationService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('get partiesTypes', () => {

 
        const partiesTypes = [
            new PartyType({ id: 3, name: "PartyType 3", version: 1 }),
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),
            new PartyType({ id: 2, name: "PartyType 2", version: 1 })
        ];        

        it('should return an observable containing Parties Types Records that have been filtered as per the Current User Defined Criteria', () => {

            const sortedPartiesTypes = [
                new PartyType({ id: 1, name: "PartyType 1", version: 1 }),
                new PartyType({ id: 2, name: "PartyType 2", version: 1 }),
                new PartyType({ id: 3, name: "PartyType 3", version: 1 })
            ]; 
            
            // Mock the get all request
            httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types/all`).flush(partiesTypes);   


            // Ensure that there are no outstanding requests to be made
            httpMock.verify();            
          
            // Subscribe to PartiesTypes updates and verify the results
            partiesTypesTableDataService.partiesTypes$
                .pipe(
                    takeLast(1),
                    toArray()
                )
                .subscribe(response => {

                    expect(response[0]).toEqual(sortedPartiesTypes);
                });



                

        });


    });



    describe('get total', () => {

        const partiesTypes = [
            new PartyType({ id: 3, name: "PartyType 3", version: 1 }),
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),
            new PartyType({ id: 2, name: "PartyType 2", version: 1 })
        ];

        it('should return an observable containing the total number of Parties Types Records that have been filtered as per the Current User Defined Criteria', () => {  

            // Mock the get all request
            httpMock.expectOne(`${environment.partiesTypesBaseUrl}/api/v1/parties_types/all`).flush(partiesTypes);  

            // Ensure that there are no outstanding requests to be made
            httpMock.verify();

            // Subscribe to total updates and verify the results
            partiesTypesTableDataService.total$
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
            partiesTypesTableDataService.loading$
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
            expect(partiesTypesTableDataService.page).toEqual(1);
        });

    });   
    
    
    describe('set page', () => {

        it('should update the currently set active page detail and trigger the data transformation transformation exercise', () => {
            spyOn(partiesTypesTableDataService, 'set').and.callThrough();
            partiesTypesTableDataService.page = 2;
            expect(partiesTypesTableDataService.set).toHaveBeenCalled();
            expect(partiesTypesTableDataService.page).toEqual(2);
        });

    });
    
    
    describe('get page size', () => {

        it('should return the currently set page size', () => { 
            expect(partiesTypesTableDataService.pageSize).toEqual(4);
        });

    });   
    
    
    describe('set page size', () => {

        it('should update the currently set page size detail and trigger the data transformation transformation exercise', () => { 
            spyOn(partiesTypesTableDataService, 'set').and.callThrough();
            partiesTypesTableDataService.page = 10;
            expect(partiesTypesTableDataService.set).toHaveBeenCalled();
            expect(partiesTypesTableDataService.page).toEqual(10);
        });


    });  
    


    describe('get search term', () => {

        it('should return the currently set search term', () => { 
            expect(partiesTypesTableDataService.searchTerm).toEqual("");
        });

    });   
    
    
    describe('set search term', () => {

        it('should update the currently set search term and trigger the data transformation transformation exercise', () => { 
            spyOn(partiesTypesTableDataService, 'set').and.callThrough();
            partiesTypesTableDataService.searchTerm = "test";
            expect(partiesTypesTableDataService.set).toHaveBeenCalled();
            expect(partiesTypesTableDataService.searchTerm).toEqual("test");
        });

    });  
    

    describe('set sort column', () => {

        it('should update the currently set sort column and trigger the data transformation transformation exercise', () => { 
            spyOn(partiesTypesTableDataService, 'set');
            partiesTypesTableDataService.sortColumn = "name";
            expect(partiesTypesTableDataService.set).toHaveBeenCalled();
        });

    }); 


    describe('set sort direction', () => {

        it('should update the currently set sort direction and trigger the data transformation transformation exercise', () => { 
  
        });

    }); 


    describe('compare', () => {

        it('should return a numerical value indicating how the first value compares to the second value for sorting purposes', () => {

            // strings
            expect(partiesTypesTableDataService.compare("PartyType 1", "PartyType 2")).toEqual(-1);
            expect(partiesTypesTableDataService.compare("PartyType 1", "PartyType 1")).toEqual(0);
            expect(partiesTypesTableDataService.compare("PartyType 2", "PartyType 1")).toEqual(1);

            // numbers
            expect(partiesTypesTableDataService.compare(1, 2)).toEqual(-1);
            expect(partiesTypesTableDataService.compare(1, 1)).toEqual(0);
            expect(partiesTypesTableDataService.compare(2, 1)).toEqual(1);            

        }); 


    }); 
    
    

    describe('sort', () => {

        const unsortedMockPartiesTypes = [
            new PartyType({ id: 2, name: "PartyType 2", version: 1 }),
            new PartyType({ id: 3, name: "PartyType 3", version: 1 }),
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),                
        ];

        const sortedMockPartiesTypes1 = [
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),
            new PartyType({ id: 2, name: "PartyType 2", version: 1 }),
            new PartyType({ id: 3, name: "PartyType 3", version: 1 })
        ];  
        
        const sortedMockPartiesTypes2 = [
            new PartyType({ id: 3, name: "PartyType 3", version: 1 }),                
            new PartyType({ id: 2, name: "PartyType 2", version: 1 }),
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),                
        ];   

        it('should sort Parties Types Records by table column in ascending or descending order', () => {

            expect(partiesTypesTableDataService.sort(unsortedMockPartiesTypes,"name", "asc")).toEqual(sortedMockPartiesTypes1);
            expect(partiesTypesTableDataService.sort(unsortedMockPartiesTypes,"name", "desc")).toEqual(sortedMockPartiesTypes2);
                       
        }); 

    });   
    
    
    describe('matches', () => {

        it('should return true if a PartyType record matches a search string or false otherwise', () => {

            // Define a couple of Mock PartiesTypes
            let partyType1: PartyType = new PartyType({ id: 1, name: "PartyType 1", version: 1 });
            let partyType2: PartyType = new PartyType({ id: 2, name: "PartyType 2", version: 1 });
            let partyType3: PartyType = new PartyType({ id: 3, name: "PartyType 3", version: 1 });


            expect(partiesTypesTableDataService.matches(partyType1, "3")).toBeFalse(); 
            expect(partiesTypesTableDataService.matches(partyType2, "3")).toBeFalse(); 
            expect(partiesTypesTableDataService.matches(partyType3, "3")).toBeTrue();
        });

    }); 
    
    

    describe('index', () => {

        const unIndexedMockPartiesTypes = [
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),
            new PartyType({ id: 2, name: "PartyType 2", version: 1 }),
            new PartyType({ id: 3, name: "PartyType 3", version: 1 })
        ]; 
        
        const indexedMockPartiesTypes = [
            new PartyType({ pos: 1, id: 1, name: "PartyType 1", version: 1 }),
            new PartyType({ pos: 2, id: 2, name: "PartyType 2", version: 1 }),
            new PartyType({ pos: 3, id: 3, name: "PartyType 3", version: 1 })
        ];             


        it('should index Parties Types Records', () => {
            expect(partiesTypesTableDataService.index(unIndexedMockPartiesTypes)).toEqual(indexedMockPartiesTypes);            
        }); 

    });   
    
    
    describe('paginate', () => {

        const unpaginatedMockPartiesTypes = [
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),
            new PartyType({ id: 2, name: "PartyType 2", version: 1 }),
            new PartyType({ id: 3, name: "PartyType 3", version: 1 })
        ]; 
        
        const paginatedMockPartiesTypes = [
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),
            new PartyType({ id: 2, name: "PartyType 2", version: 1 })
        ];         

        it('should paginate Parties Types Records by page and page sizes', () => {
            expect(partiesTypesTableDataService.paginate(unpaginatedMockPartiesTypes, 1, 2)).toEqual(paginatedMockPartiesTypes);            
        }); 

    }); 
    
    

    describe('set', () => {

        it('should update the the sort / filter criteria and triggers the data transformation exercise', () => { 
            spyOn(partiesTypesTableDataService, 'transform');
            partiesTypesTableDataService.set({})
            expect(partiesTypesTableDataService.transform).toHaveBeenCalled();
        });

    });     




    describe('transform', () => {

        const partiesTypes = [
            new PartyType({ id: 3, name: "PartyType 3", version: 1 }),
            new PartyType({ id: 1, name: "PartyType 1", version: 1 }),
            new PartyType({ id: 2, name: "PartyType 2", version: 1 }) 
        ];        

        it('should sorts, filter and paginate Parties Types Records', () => {

            spyOn(partiesTypesTableDataService, 'sort').and.callThrough();;
            spyOn(partiesTypesTableDataService, 'matches').and.callThrough();
            spyOn(partiesTypesTableDataService, 'index').and.callThrough();
            spyOn(partiesTypesTableDataService, 'paginate').and.callThrough();

            partiesTypesTableDataService.transform(partiesTypes);

            expect(partiesTypesTableDataService.sort).toHaveBeenCalled();
            expect(partiesTypesTableDataService.matches).toHaveBeenCalled();
            expect(partiesTypesTableDataService.index).toHaveBeenCalled();
            expect(partiesTypesTableDataService.paginate).toHaveBeenCalled();

        });        


    });
});
