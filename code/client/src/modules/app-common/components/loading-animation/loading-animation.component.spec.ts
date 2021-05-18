import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { LoadingAnimationComponent } from './loading-animation.component';


describe('LoadingAnimationComponent', () => {

    let fixture: ComponentFixture<LoadingAnimationComponent>;

    let component: LoadingAnimationComponent;
    let componentDE: DebugElement;
    let componentNE: Element;

    beforeEach(() => {

        TestBed.configureTestingModule({
            declarations: [LoadingAnimationComponent],
            imports: [NoopAnimationsModule, FormsModule, HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [],
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();

        fixture = TestBed.createComponent(LoadingAnimationComponent);

        component = fixture.componentInstance;
        componentDE = fixture.debugElement;
        componentNE = fixture.nativeElement;

        fixture.detectChanges();
    });

    it('should display the component', () => {
        expect(component).toBeDefined();
    });


    it('should broadcasts the newly set loading status', () => {

        const mockTotal: number = 50;

        component.loading$
            .pipe(
                take(2),
                toArray()
            )
            .subscribe(response => {

                expect(response.length).toEqual(2)
                expect(response[0]).toEqual(false);
                expect(response[1]).toEqual(true);
            });

        component.loading = true;
    });
});
