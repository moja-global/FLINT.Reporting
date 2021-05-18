import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { take, toArray } from 'rxjs/operators';

import { PaginationComponent } from './pagination.component';


describe('PaginationComponent', () => {

    let fixture: ComponentFixture<PaginationComponent>;

    let component: PaginationComponent;
    let componentDE: DebugElement;
    let componentNE: Element;

    beforeEach(() => {

        TestBed.configureTestingModule({
            declarations: [PaginationComponent],
            imports: [NoopAnimationsModule, FormsModule, HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [],
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();

        fixture = TestBed.createComponent(PaginationComponent);

        component = fixture.componentInstance;
        componentDE = fixture.debugElement;
        componentNE = fixture.nativeElement;

        fixture.detectChanges();
    });

    it('should display the component', () => {
        expect(component).toBeDefined();
    });

    it('should initialize the component', () => {

        component.initialize(3, 6);
        expect(component.page).toEqual(3);
        expect(component.pageSize).toEqual(6);
    });

    it('should emit the newly set page number', () => {

        spyOn(component.pageChanged, "emit");

        // Get the select element
        let pagination: NgbPagination = componentDE.query(By.css('ngb-pagination')).componentInstance;
    
        // Sets the selected page to the 3
        pagination.page = 3;

        // Watch out for changes
        fixture.detectChanges();
    
        expect(component.page).toEqual(3);  
        expect(component.pageChanged.emit).toHaveBeenCalledOnceWith(3);

    });

    it('should emit the newly set page size', () => {

        spyOn(component.pageSizeChanged, "emit");

        // Get the select element
        let select = componentDE.query(By.css('select')).nativeElement;
    
        // Sets its selected value to the 4th option
        select.value = select.options[3].value;

        // Trigger a change event
        select.dispatchEvent(new Event('change'));

        // Watch out for changes
        fixture.detectChanges();
    
        expect(component.pageSize).toEqual(8);  
        expect(component.pageSizeChanged.emit).toHaveBeenCalledOnceWith(8);

    });

    it('should broadcasts the newly set page totals', () => {

        const mockTotal: number = 50;

        component.total$
            .pipe(
                take(2),
                toArray()
            )
            .subscribe(response => {

                expect(response.length).toEqual(2)
                expect(response[0]).toEqual(0);
                expect(response[1]).toEqual(mockTotal);
            });

        component.total = mockTotal;
    });
});
