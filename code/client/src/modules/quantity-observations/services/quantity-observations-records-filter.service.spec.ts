
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { ConnectivityStatusService, MessageService } from '@common/services';
import { QuantityObservationsDataService } from './quantity-observations-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';

import { QuantityObservationsRecordsFilterService } from './quantity-observations-records-filter.service';

describe('QuantityObservationsRecordsFilterService', () => {

    let quantityObservationsDataService: QuantityObservationsDataService;
    let quantityObservationsTableDataService: QuantityObservationsRecordsFilterService;
    let httpMock: HttpTestingController;


    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, QuantityObservationsDataService, MessageService],
        });

        quantityObservationsDataService = TestBed.inject(QuantityObservationsDataService);
        quantityObservationsTableDataService = TestBed.inject(QuantityObservationsRecordsFilterService);
        httpMock = TestBed.inject(HttpTestingController);
    });


    describe('quantityObservations$', () => {
        

    });
});
