import { TestBed } from '@angular/core/testing';
import { HttpTestingController, HttpClientTestingModule, TestRequest } from '@angular/common/http/testing';
import { QuantityObservationsDataService } from './quantity-observations-data.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { MessageService } from '../../app-common/services/messages.service';
import { ConnectivityStatusService } from '@common/services';

describe('Quantity Observations Data Service', () => {

    let quantityObservationsDataService: QuantityObservationsDataService;
    let httpMock: HttpTestingController;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [QuantityObservationsDataService, MessageService, ConnectivityStatusService],
        });

    
        quantityObservationsDataService = TestBed.inject(QuantityObservationsDataService);
        httpMock = TestBed.inject(HttpTestingController);

    });

    
});

function expectNone() {
    throw new Error('Function not implemented.');
}

