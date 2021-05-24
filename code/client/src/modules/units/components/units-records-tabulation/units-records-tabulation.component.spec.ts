import { DecimalPipe } from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { LoadingAnimationComponent, PaginationComponent } from '@common/components';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';

import { UnitsRecordsTabulationComponent } from './units-records-tabulation.component';

describe('UnitsRecordsTabulationComponent', () => {

    let fixture: ComponentFixture<UnitsRecordsTabulationComponent>;

    let component: UnitsRecordsTabulationComponent;
    let componentDE: DebugElement;
    let componentNE: Element;

    

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [LoadingAnimationComponent, PaginationComponent, UnitsRecordsTabulationComponent],
            imports: [NoopAnimationsModule, FormsModule, HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [DecimalPipe],
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();

        fixture = TestBed.createComponent(UnitsRecordsTabulationComponent);

        componentDE = fixture.debugElement;
        component = fixture.componentInstance;
        componentNE = fixture.nativeElement;

        fixture.detectChanges();
    });

    it('should display the component', () => {
        expect(component).toBeDefined();
    });

    it('should display the Units headers correctly', () => {

        // #
        const th1: HTMLTableHeaderCellElement = componentDE.query(By.css('tr th:nth-of-type(1)')).nativeElement;

        expect(th1).toBeDefined();
        expect(th1.innerText).toEqual("#");


        // Name
        const th2: HTMLTableHeaderCellElement = componentDE.query(By.css('tr th:nth-of-type(2)')).nativeElement;

        expect(th2).toBeDefined();
        expect(th2.innerText).toEqual("Name");


        // Plural
        const th3: HTMLTableHeaderCellElement = componentDE.query(By.css('tr th:nth-of-type(3)')).nativeElement;

        expect(th3).toBeDefined();
        expect(th3.innerText).toEqual("Plural");  
        
        
        // Symbol
        const th4: HTMLTableHeaderCellElement = componentDE.query(By.css('tr th:nth-of-type(4)')).nativeElement;

        expect(th4).toBeDefined();
        expect(th4.innerText).toEqual("Symbol");        
    

        // Scale Factor
        const th5: HTMLTableHeaderCellElement = componentDE.query(By.css('tr th:nth-of-type(5)')).nativeElement;

        expect(th5).toBeDefined();
        expect(th5.innerText).toEqual("Scale Factor");
        
        
        // Actions
        const th6: HTMLTableHeaderCellElement = componentDE.query(By.css('tr th:nth-of-type(6)')).nativeElement;

        expect(th6).toBeDefined();
        expect(th6.innerText).toEqual("Actions");        
        
    }); 
    
    it('should trigger the search for Units records with the specified search term', () => {

        const searchTermSetter = spyOnProperty(component.unitsTableService, 'searchTerm', 'set');
        
        const searchComponent: DebugElement = componentDE.query(By.css('sb-search'));

        expect(searchComponent).toBeDefined();

        searchComponent.triggerEventHandler("searched", "x");

        fixture.detectChanges();

        expect(searchTermSetter).toHaveBeenCalledOnceWith("x");

    });  
    
    it('should trigger the loading of the Units records page change', () => {

        const pageSetter = spyOnProperty(component.unitsTableService, 'page', 'set');
        
        const paginationComponent: DebugElement = componentDE.query(By.css('sb-pagination'));

        expect(paginationComponent).toBeDefined();

        paginationComponent.triggerEventHandler("pageChanged", 3);

        fixture.detectChanges();

        expect(pageSetter).toHaveBeenCalledOnceWith(3);

    });
    
    it('should trigger the loading of the Units records page size change', () => {

        const pageSizeSetter = spyOnProperty(component.unitsTableService, 'pageSize', 'set');
        
        const paginationComponent: DebugElement = componentDE.query(By.css('sb-pagination'));

        expect(paginationComponent).toBeDefined();

        paginationComponent.triggerEventHandler("pageSizeChanged", 10);

        fixture.detectChanges();

        expect(pageSizeSetter).toHaveBeenCalledOnceWith(10);

    });    
    
    it('should trigger the sorting of Units records by name', () => {

        const sortColumnSetter = spyOnProperty(component.unitsTableService, 'sortColumn', 'set');
        const sortDirectionSetter = spyOnProperty(component.unitsTableService, 'sortDirection', 'set');

        const th2: DebugElement = componentDE.query(By.css('tr th:nth-of-type(2)'));

        expect(th2).toBeDefined();

        // asc
        th2.triggerEventHandler('sort', {column: th2.nativeElement.getAttribute('sbSortable'), direction: "asc"});

        fixture.detectChanges();

        expect(component.sortedColumn).toEqual("name");
	    expect(component.sortedDirection).toEqual("asc");
        expect(sortColumnSetter).toHaveBeenCalledOnceWith("name");
        expect(sortDirectionSetter).toHaveBeenCalledOnceWith("asc");

        // desc
        th2.triggerEventHandler('sort', {column: th2.nativeElement.getAttribute('sbSortable'), direction: "desc"});

        fixture.detectChanges();

        expect(component.sortedColumn).toEqual("name");
	    expect(component.sortedDirection).toEqual("desc"); 
        expect(sortColumnSetter).toHaveBeenCalledTimes(2); 
        expect(sortColumnSetter).toHaveBeenCalledWith("name");
        expect(sortDirectionSetter).toHaveBeenCalledTimes(2);
        expect(sortDirectionSetter).toHaveBeenCalledWith("desc"); 


    }); 

    it('should trigger the sorting of Units records by plural', () => {

        const sortColumnSetter = spyOnProperty(component.unitsTableService, 'sortColumn', 'set');
        const sortDirectionSetter = spyOnProperty(component.unitsTableService, 'sortDirection', 'set');

        const th3: DebugElement = componentDE.query(By.css('tr th:nth-of-type(3)'));

        expect(th3).toBeDefined();

        // asc
        th3.triggerEventHandler('sort', {column: th3.nativeElement.getAttribute('sbSortable'), direction: "asc"});

        fixture.detectChanges();

        expect(component.sortedColumn).toEqual("plural");
	    expect(component.sortedDirection).toEqual("asc");
        expect(sortColumnSetter).toHaveBeenCalledOnceWith("plural");
        expect(sortDirectionSetter).toHaveBeenCalledOnceWith("asc");

        // desc
        th3.triggerEventHandler('sort', {column: th3.nativeElement.getAttribute('sbSortable'), direction: "desc"});

        fixture.detectChanges();

        expect(component.sortedColumn).toEqual("plural");
	    expect(component.sortedDirection).toEqual("desc"); 
        expect(sortColumnSetter).toHaveBeenCalledTimes(2); 
        expect(sortColumnSetter).toHaveBeenCalledWith("plural");
        expect(sortDirectionSetter).toHaveBeenCalledTimes(2);
        expect(sortDirectionSetter).toHaveBeenCalledWith("desc"); 


    }); 
    
    it('should trigger the sorting of Units records by symbol', () => {

        const sortColumnSetter = spyOnProperty(component.unitsTableService, 'sortColumn', 'set');
        const sortDirectionSetter = spyOnProperty(component.unitsTableService, 'sortDirection', 'set');

        const th4: DebugElement = componentDE.query(By.css('tr th:nth-of-type(4)'));

        expect(th4).toBeDefined();

        // asc
        th4.triggerEventHandler('sort', {column: th4.nativeElement.getAttribute('sbSortable'), direction: "asc"});

        fixture.detectChanges();

        expect(component.sortedColumn).toEqual("symbol");
	    expect(component.sortedDirection).toEqual("asc");
        expect(sortColumnSetter).toHaveBeenCalledOnceWith("symbol");
        expect(sortDirectionSetter).toHaveBeenCalledOnceWith("asc");

        // desc
        th4.triggerEventHandler('sort', {column: th4.nativeElement.getAttribute('sbSortable'), direction: "desc"});

        fixture.detectChanges();

        expect(component.sortedColumn).toEqual("symbol");
	    expect(component.sortedDirection).toEqual("desc"); 
        expect(sortColumnSetter).toHaveBeenCalledTimes(2); 
        expect(sortColumnSetter).toHaveBeenCalledWith("symbol");
        expect(sortDirectionSetter).toHaveBeenCalledTimes(2);
        expect(sortDirectionSetter).toHaveBeenCalledWith("desc"); 


    });     
        
});
