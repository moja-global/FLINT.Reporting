import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component, DebugElement, NO_ERRORS_SCHEMA} from '@angular/core';
import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';

import { SearchComponent } from './search.component';

@Component({
    template: `
        <sb-search (searched)="onSearch($event)"></sb-search>
    `,
})
class TestHostComponent {
    onSearch(event: Event) { }
}

describe('SearchComponent', () => {

    let fixture: ComponentFixture<TestHostComponent>;

    let hostComponent: TestHostComponent;
    let hostComponentDE: DebugElement;
    let hostComponentNE: HTMLElement;

    let searchComponent: SearchComponent;
    let searchComponentDE: DebugElement;
    let searchComponentNE: HTMLElement;

    const event = new KeyboardEvent('keyup', {
        bubbles: true, cancelable: true, shiftKey: false
    });

    beforeEach(() => {

        TestBed.configureTestingModule({
            declarations: [TestHostComponent, SearchComponent],
            imports: [NoopAnimationsModule, FormsModule, HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [],
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();

        fixture = TestBed.createComponent(TestHostComponent);

        hostComponent = fixture.componentInstance;
        hostComponentDE = fixture.debugElement;
        hostComponentNE = fixture.nativeElement;

        searchComponentDE = hostComponentDE.children[0];
        searchComponent = searchComponentDE.componentInstance;
        searchComponentNE = searchComponentDE.nativeElement;

        fixture.detectChanges();
    });


    it('should display the component', () => {
        expect(hostComponentNE.querySelector('sb-search')).toBeDefined();
    });

    it('should sync input values when the user inputs a search term', waitForAsync(() => {

        let input = searchComponentDE.query(By.css('input'));
        let el = input.nativeElement;

        expect(searchComponent.searchTerm).toBe('');

        el.value = 'x';
        el.dispatchEvent(event);

        expect(searchComponent.searchTerm).toBe('x');

    }));

    it('should emit a search request when the user inputs a search term', waitForAsync(() => {

        spyOn(searchComponent.searched, 'emit');

        let input = searchComponentDE.query(By.css('input'));
        let el = input.nativeElement;

        el.value = 'y';
        el.dispatchEvent(event);

        fixture.whenStable().then(() => {
            fixture.detectChanges();
            expect(searchComponent.searched.emit).toHaveBeenCalledWith("y");
        }); 


    }));

});
