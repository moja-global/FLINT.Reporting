import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ConnectivityStatusService } from '@common/services';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';

import { ButtonComponent } from './button.component';

describe('ButtonComponent', () => {
    
    let fixture: ComponentFixture<ButtonComponent>;

    let component: ButtonComponent;
    let componentDE: DebugElement;
    let componentNE: Element;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [ButtonComponent],
            imports: [NoopAnimationsModule, HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService],
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();

        fixture = TestBed.createComponent(ButtonComponent);

        component = fixture.componentInstance;
        componentDE = fixture.debugElement;
        componentNE = fixture.nativeElement;

        fixture.detectChanges();
    });

    it('should display the component', () => {
        expect(component).toBeDefined();
    });
});
