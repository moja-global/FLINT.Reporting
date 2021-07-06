import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component, DebugElement, NO_ERRORS_SCHEMA} from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';

import { FilterComponent } from './filter.component';

@Component({
    template: `
        <sb-filter (filtered)="onFilter($event)"></sb-filter>
    `,
})
class TestHostComponent {
    onFilter(event: Event) { }
}

describe('FilterComponent', () => {

    let fixture: ComponentFixture<TestHostComponent>;

    let hostComponent: TestHostComponent;
    let hostComponentDE: DebugElement;
    let hostComponentNE: HTMLElement;

    let filterComponent: FilterComponent;
    let filterComponentDE: DebugElement;
    let filterComponentNE: HTMLElement;

    const event = new KeyboardEvent('keyup', {
        bubbles: true, cancelable: true, shiftKey: false
    });

    beforeEach(() => {

        TestBed.configureTestingModule({
            declarations: [TestHostComponent, FilterComponent],
            imports: [NoopAnimationsModule, FormsModule, HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [],
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();

        fixture = TestBed.createComponent(TestHostComponent);

        hostComponent = fixture.componentInstance;
        hostComponentDE = fixture.debugElement;
        hostComponentNE = fixture.nativeElement;

        filterComponentDE = hostComponentDE.children[0];
        filterComponent = filterComponentDE.componentInstance;
        filterComponentNE = filterComponentDE.nativeElement;

        fixture.detectChanges();
    });


    it('should display the component', () => {
        expect(hostComponentNE.querySelector('sb-filter')).toBeDefined();
    });

    it('should sync input values when the user inputs a filter term', waitForAsync(() => {

        let input = filterComponentDE.query(By.css('input'));
        let el = input.nativeElement;

        expect(filterComponent.filterTerm).toBe('');

        el.value = 'x';
        el.dispatchEvent(event);

        expect(filterComponent.filterTerm).toBe('x');

    }));

    it('should emit a filter request when the user inputs a filter term', waitForAsync(() => {

        spyOn(filterComponent.filtered, 'emit');

        let input = filterComponentDE.query(By.css('input'));
        let el = input.nativeElement;

        el.value = 'y';
        el.dispatchEvent(event);

        fixture.whenStable().then(() => {
            fixture.detectChanges();
            expect(filterComponent.filtered.emit).toHaveBeenCalledWith("y");
        }); 


    }));

});
