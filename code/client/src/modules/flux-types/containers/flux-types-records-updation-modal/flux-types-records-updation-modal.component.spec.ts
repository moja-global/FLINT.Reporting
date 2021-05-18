import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component, DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';

import { FluxTypesRecordsUpdationModalComponent } from './flux-types-records-updation-modal.component';

@Component({
    template: `
        <sb-flux-types-records-updation-modal [someInput]="someInput" (someFunction)="someFunction($event)"></sb-flux-types-records-updation-modal>
    `,
})
class TestHostComponent {
    // someInput = 1;
    // someFunction(event: Event) {}
}

describe('FluxTypesRecordsUpdationModalComponent', () => {
    let fixture: ComponentFixture<TestHostComponent>;
    let hostComponent: TestHostComponent;
    let hostComponentDE: DebugElement;
    let hostComponentNE: Element;

    let component: FluxTypesRecordsUpdationModalComponent;
    let componentDE: DebugElement;
    let componentNE: Element;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [TestHostComponent, FluxTypesRecordsUpdationModalComponent],
            imports: [NoopAnimationsModule, HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [NgbActiveModal],            
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();

        fixture = TestBed.createComponent(TestHostComponent);
        hostComponent = fixture.componentInstance;
        hostComponentDE = fixture.debugElement;
        hostComponentNE = hostComponentDE.nativeElement;

        componentDE = hostComponentDE.children[0];
        component = componentDE.componentInstance;
        componentNE = componentDE.nativeElement;

        fixture.detectChanges();
    });

    it('should display the component', () => {
        expect(hostComponentNE.querySelector('sb-flux-types-records-updation-modal')).toEqual(jasmine.anything());
    });
});
