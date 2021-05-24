import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { UnitsDataService } from './units-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { Unit } from '../models';
import { ConnectivityStatusService } from '@common/services';

describe('Units Data Service', () => {

    let unitsDataService: UnitsDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [UnitsDataService, MessageService, ConnectivityStatusService],
        });

    
        unitsDataService = TestBed.inject(UnitsDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    describe('createUnit', () => {
        it('should create and adds an instance of a new Unit record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitsDataService],
                (httpMock: HttpTestingController, unitsDataService: UnitsDataService) => {

          
                    // Define a couple of mock Units
                    const initialUnits: Unit[] = <Array<Unit>>[
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
                    ];

                    const newUnit = new Unit({ id: 4, name: "Unit 4", description: "Unit 4 Description", version: 1 });
                    
                    const finalUnits: Unit[] = <Array<Unit>>[
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 }),
                        new Unit({ id: 4, name: "Unit 4", description: "Unit 4 Description", version: 1 })
                    ];                    

                    // Call & subscribe to the createUnit() method
                    unitsDataService.createUnit(new Unit({ name: "Unit 4", description: "Unit 4 Description" }))
                        .subscribe((response) => {

                            // Expect that the response is equal to the new Unit - i.e with the id / version upated
                            expect(response).toEqual(newUnit);
                        });


                    // Subscribe to the Units observable
                    unitsDataService.units$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Units
                            expect(response[1]).toEqual(initialUnits);                            

                            // Expect that the third response is equal to the final Units
                            expect(response[2]).toEqual(finalUnits);
                        });


                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/units/all`).flush(initialUnits);

                    // Expect that a single request was made during the addition phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/units`);

                    // Expect that the addition request was of type POST
                    expect(mockReq2.request.method).toEqual('POST');

                    // Expect that the addition request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the addition request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the creation request
                    mockReq2.flush(newUnit);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();

                }
            )
        );
    });

    describe('getUnit', () => {
        it('should retrieve and add a single Unit record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitsDataService],
                (httpMock: HttpTestingController, unitsDataService: UnitsDataService) => {

                    // Define a couple of mock Units
                    const allUnits: Unit[] = <Array<Unit>>[
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
                    ];

                    const targetUnit = new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 });

                    // Call the getUnit() method
                    unitsDataService.getUnit(2)
                        .subscribe((response) => {

                            // Expect that the response is equal to the target Unit
                            expect(response).toEqual(targetUnit);
                        });

                    // Subscribe to the Units observable
                    unitsDataService.units$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Units
                            expect(response[1]).toEqual(allUnits);                            

                            // Expect that the third response is equal to all the Units
                            expect(response[2]).toEqual(allUnits);

                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/units/all`).flush(allUnits);

                    // Expect that a single request was made during the retrieval phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/units/ids/2`);

                    // Expect that the retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the retrieval request
                    mockReq2.flush(targetUnit);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllUnits', () => {

        it('should retrieve and add all or a subset of all Units records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitsDataService],
                (httpMock: HttpTestingController, unitsDataService: UnitsDataService) => {

                    // Define a couple of mock Units
                    const allUnits: Unit[] = <Array<Unit>>[
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
                    ];

                    // Call the getAllUnits() method
                    unitsDataService.getAllUnits()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Unit
                            expect(response).toEqual(allUnits);
                        });

                    // Subscribe to the Units observable
                    unitsDataService.units$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to all the Units
                            expect(response[1]).toEqual(allUnits);                            

                            // Expect that the third response is equal to all the Units
                            expect(response[2]).toEqual(allUnits);
                        });

                    // Expect that two retrieval requests were made: 
                    // One during the class initialization phase; 
                    // One during the current retrieval phase
                    const requests: TestRequest[] = httpMock.match(`${environment.baseUrl}/api/v1/units/all`); 
                    expect(requests.length).toEqual(2); 
                    
                    // Get the first retrieval request
                    const mockReq1 = requests[0];
                    
                    // Resolve the first retrieval request
                    mockReq1.flush(allUnits);                    

                    // Get the second retrieval request
                    const mockReq2 = requests[1];

                    // Expect that the second retrieval request method was of type GET
                    expect(mockReq2.request.method).toEqual('GET');

                    // Expect that the second retrieval request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the second retrieval request response was of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the second retrieval request
                    mockReq2.flush(allUnits);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateUnit', () => {
        it('should update a single Unit record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitsDataService],
                (httpMock: HttpTestingController, unitsDataService: UnitsDataService) => {

                    // Define a couple of mock Units
                    const initialUnits: Unit[] = <Array<Unit>>[
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 }),
                        new Unit({ id: 4, name: "Unit 4", description: "Unit 4 Description", version: 1 })
                    ];  

                    const updatedUnit = new Unit({ id: 4, name: "Updated Unit Four", description: "Updated Unit Four Description", version: 2 });
                    
                    const finalUnits: Unit[] = <Array<Unit>>[
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 }),
                        new Unit({ id: 4, name: "Updated Unit Four", description: "Updated Unit Four Description", version: 2 })
                    ];                     


                    // Call the updateUnit() method
                    unitsDataService.updateUnit(new Unit({ id: 4, name: "Updated Unit Four", description: "Updated Unit Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the updated Unit
                            expect(response).toEqual(updatedUnit);
                        });

                    // Subscribe to the Units observable
                    unitsDataService.units$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Units
                            expect(response[1]).toEqual(initialUnits);                            

                            // Expect that the third response is equal to the final Units
                            expect(response[2]).toEqual(finalUnits);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/units/all`).flush(initialUnits);                

                    // Expect that a single request was made during the updation phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/units`);

                    // Expect that the updation request method was of type PUT
                    expect(mockReq2.request.method).toEqual('PUT');

                    // Expect that the updation request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Expect that the updation request response wss of type json
                    expect(mockReq2.request.responseType).toEqual('json');

                    // Resolve the updation request
                    mockReq2.flush(updatedUnit);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteUnit', () => {
        it('should delete a single Unit record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, UnitsDataService],
                (httpMock: HttpTestingController, unitsDataService: UnitsDataService) => {

                    // Define a couple of mock Units
                    const initialUnits: Unit[] = <Array<Unit>>[
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 }),
                        new Unit({ id: 4, name: "Unit 4", description: "Unit 4 Description", version: 1 })
                    ];  

                    const targetUnit = new Unit({ id: 4, name: "Unit 4", description: "Unit 4 Description", version: 1 });

                    const finalUnits: Unit[] = <Array<Unit>>[
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
                    ];

                    // Call deleteUnit() method to delete the target Unit from the list of Units
                    unitsDataService.deleteUnit(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Units
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the Units observable
                    unitsDataService.units$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the initial Units
                            expect(response[1]).toEqual(initialUnits);                            

                            // Expect that the third response is equal to the final Units
                            expect(response[2]).toEqual(finalUnits);
                        });

                    // Expect that a single retrieval request was made during the class initialization phase
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/units/all`).flush(initialUnits);  
                    
                    
                    // Expect that a single request was made during the deletion phase
                    const mockReq2 = httpMock.expectOne(`${environment.baseUrl}/api/v1/units/ids/4`);

                    // Expect that the deletion request method was of type DELETE
                    expect(mockReq2.request.method).toEqual('DELETE');

                    // Expect that the deletion request was not cancelled
                    expect(mockReq2.cancelled).toBeFalsy();

                    // Resolve the deletion request
                    mockReq2.flush(1);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });


    describe('records', () => {

        it('should retrieve the most recent Units records from the local cache',
            inject(
                [HttpTestingController, UnitsDataService],
                (httpMock: HttpTestingController, unitsDataService: UnitsDataService) => {

                    // Define a couple of mock Units
                    const allUnits = [
                        new Unit({ id: 1, name: "Unit 1", description: "Unit 1 Description", version: 1 }),
                        new Unit({ id: 2, name: "Unit 2", description: "Unit 2 Description", version: 1 }),
                        new Unit({ id: 3, name: "Unit 3", description: "Unit 3 Description", version: 1 })
                    ];

                    // Expect that a single retrieval request was made
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/units/all`).flush(allUnits);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                    
                    // Expect that the response is equal to the array of all Units
                    expect(unitsDataService.records).toEqual(allUnits);


                }
            )
        );

    });
});

function expectNone() {
    throw new Error('Function not implemented.');
}

