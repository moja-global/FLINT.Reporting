import { inject, TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { DomainsDataService } from './domains-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { environment } from 'environments/environment';
import { take, toArray } from 'rxjs/operators';
import { Domain } from '../models/domain.model';

describe('DomainsDataService', () => {

    let domainsDataService: DomainsDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [DomainsDataService, MessageService],
        });

        domainsDataService = TestBed.inject(DomainsDataService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    describe('createDomain', () => {
        it('should create and adds an instance of a new Domain Record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DomainsDataService],
                (httpMock: HttpTestingController, domainsDataService: DomainsDataService) => {

                    // Define the mock domain
                    const mockDomain = new Domain({ id: 4, name: "Domain 4", description: "Domain 4 Description", version: 1 });

                    // Call & subscribe to the createDomain() method
                    domainsDataService.createDomain(new Domain({ name: "Domain 4", description: "Domain 4 Description" }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Domain - i.e with the id / version upated
                            expect(response).toEqual(mockDomain);
                        });


                    // Subscribe to the domains observable
                    domainsDataService.domains$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked Domain - i.e with the id / version upated
                            expect(response[1]).toEqual([mockDomain]);
                        });


                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/domains`);

                    // Expect that the request method is of type POST
                    expect(mockReq.request.method).toEqual('POST');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked Domain as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockDomain);


                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('getDomain', () => {
        it('should retrieve and add a single Domain Record to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DomainsDataService],
                (httpMock: HttpTestingController, domainsDataService: DomainsDataService) => {

                    // Define the mock domain
                    const mockDomain = new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 });

                    // Call the getDomain() method
                    domainsDataService.getDomain(2)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Domain
                            expect(response).toEqual(mockDomain);
                        });

                    // Subscribe to the domains observable
                    domainsDataService.domains$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked Domain
                            expect(response[1]).toEqual([mockDomain]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/domains/ids/2`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked Domain as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockDomain);



                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });



    describe('getAllDomains', () => {

        it('should retrieve and add all or a subset of all domains records to the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DomainsDataService],
                (httpMock: HttpTestingController, domainsDataService: DomainsDataService) => {

                    // Define a couple of mock domains
                    const mockDomains: Domain[] = <Array<Domain>>[
                        new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),
                        new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                        new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 })
                    ];

                    // Call the getAllDomains() method
                    domainsDataService.getAllDomains()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Domain
                            expect(response).toEqual(mockDomains);
                        });

                    // Subscribe to the domains observable
                    domainsDataService.domains$
                        .pipe(
                            take(2),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to an array containing the mocked Domains
                            expect(response[1]).toEqual(mockDomains);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/domains/all`);

                    // Expect that the request method is of type GET
                    expect(mockReq.request.method).toEqual('GET');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked Domains as an argument to the flush method so that they are returned as the response
                    mockReq.flush(mockDomains);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });

    describe('updateDomain', () => {
        it('should update a single Domain Record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DomainsDataService],
                (httpMock: HttpTestingController, domainsDataService: DomainsDataService) => {

                    // Define the mock domain
                    const mockDomain1 = new Domain({ id: 4, name: "Domain Four", description: "Domain Four Description", version: 1 });
                    const mockDomain2 = new Domain({ id: 4, name: "Updated Domain Four", description: "Updated Domain Four Description", version: 2 });

                    // Call createDomain() method to create the mock domain 1 and add it to the list of domains
                    domainsDataService.createDomain(mockDomain1)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Domain 1
                            expect(response).toEqual(mockDomain1);
                        });

                    // Mock the createDomain()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/domains`).flush(mockDomain1);

                    // Call the updateDomain() method
                    domainsDataService.updateDomain(new Domain({ id: 4, name: "Updated Domain Four", description: "Updated Domain Four Description", version: 1 }))
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Domain 2
                            expect(response).toEqual(mockDomain2);
                        });

                    // Subscribe to the domains observable
                    domainsDataService.domains$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the first (created) Domain
                            expect(response[1]).toEqual([mockDomain1]);

                            // Expect that the second response is equal to the second (updated) Domain
                            expect(response[2]).toEqual([mockDomain2]);
                        });

                    // Expect that a single request has been made, which matches the provided URL, and return its mock
                    const mockReq = httpMock.expectOne(`${environment.baseUrl}/api/v1/domains`);

                    // Expect that the request method is of type PUT
                    expect(mockReq.request.method).toEqual('PUT');

                    // Expect that the request was not cancelled
                    expect(mockReq.cancelled).toBeFalsy();

                    // Expect that the request response is of type json
                    expect(mockReq.request.responseType).toEqual('json');

                    // Resolve the request by returning a body plus additional HTTP information (such as response headers) if provided.
                    // Pass the mocked Domain as an argument to the flush method so that it is returned as the response
                    mockReq.flush(mockDomain2);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });

    describe('deleteDomain', () => {
        it('should delete a single Domain Record and its corresponding counterpart in the local cache and then broadcast the changes to all subscribers',
            inject(
                [HttpTestingController, DomainsDataService],
                (httpMock: HttpTestingController, domainsDataService: DomainsDataService) => {

                    // Define a mock domain
                    const mockDomain = new Domain({ id: 4, name: "Domain 4", description: "Domain 4 Description", version: 1 });

                    // Call createDomain() method to create the mock domain and add it to the list of domains
                    domainsDataService.createDomain(mockDomain)
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Domain - i.e with the id / version upated
                            expect(response).toEqual(mockDomain);
                        });

                    // Mock the createDomain()'s corresponding HTTP POST Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/domains`).flush(mockDomain);

                    // Call deleteDomain() method to delete the mock domain from the list of domains
                    domainsDataService.deleteDomain(4)
                        .subscribe((response) => {
                            // Expect that the response is equal to 1: the total number of deleted Domain
                            expect(response).toEqual(1);
                        });

                    // Subscribe to the domains observable
                    domainsDataService.domains$
                        .pipe(
                            take(3),
                            toArray()
                        )
                        .subscribe(response => {

                            // Expect that the first response is equal to an empty array
                            expect(response[0]).toEqual([]);

                            // Expect that the second response is equal to the created Domain
                            expect(response[1]).toEqual([mockDomain]);

                            // Expect that the second response is equal to an empty array - following the deletion of the created domain
                            expect(response[2]).toEqual([]);
                        });

                    // Mock the deleteDomain()'s corresponding HTTP DELETE Request
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/domains/ids/4`).flush(1);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );
    });


    describe('records', () => {

        it('should retrieve the most recent domains records from the local cache',
            inject(
                [HttpTestingController, DomainsDataService],
                (httpMock: HttpTestingController, domainsDataService: DomainsDataService) => {

                    // Define a couple of mock domains
                    const mockDomains = [
                        new Domain({ id: 1, name: "Domain 1", description: "Domain 1 Description", version: 1 }),
                        new Domain({ id: 2, name: "Domain 2", description: "Domain 2 Description", version: 1 }),
                        new Domain({ id: 3, name: "Domain 3", description: "Domain 3 Description", version: 1 })
                    ];

                    // Call the getAllDomains() method
                    domainsDataService.getAllDomains()
                        .subscribe((response) => {
                            // Expect that the response is equal to the mocked Domains
                            expect(response).toEqual(mockDomains);
                        });

                    // Expect that a single request has been made, which matches the provided URL
                    httpMock.expectOne(`${environment.baseUrl}/api/v1/domains/all`).flush(mockDomains);

                    // Expect that the response is equal to an array containing the mocked Domains
                    expect(domainsDataService.records).toEqual(mockDomains);

                    // Ensure that there are no outstanding requests to be made
                    httpMock.verify();
                }
            )
        );

    });
});
