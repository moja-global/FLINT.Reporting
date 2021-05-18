
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { first} from 'rxjs/operators';
import { ConnectivityStatusService } from '.';


describe('ConnectivityService', () => {

    let service: ConnectivityStatusService;

    beforeEach(() => {

        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService],
        });

        service = TestBed.inject(ConnectivityStatusService);
    });

    describe('online$', () => {
        it('should be defined', () => {
            service.online$.pipe(first()).subscribe(response => {
                expect(response).toBeDefined();
            });
        });
    });

});
