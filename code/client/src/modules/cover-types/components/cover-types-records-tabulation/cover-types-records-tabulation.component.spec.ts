import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component, DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { PaginationComponent, LoadingAnimationComponent } from '@common/components';
import { ConnectivityStatusService } from '@common/services/connectivity-status.service';
import { CoverTypesRecordsTabulationService } from '@modules/cover-types/services/cover-types-records-tabulation.service';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { CoverTypesRecordsTabulationComponent } from './cover-types-records-tabulation.component';


@Component({
    template: `
        <sb-cover-types-records-tabulation [someInput]="someInput" (someFunction)="someFunction($event)"></sb-cover-types-records-tabulation>
    `,
})
class TestHostComponent {
    // someInput = 1;
    // someFunction(event: Event) {}
}

describe('CoverTypesRecordsTabulationComponent', () => {
    let fixture: ComponentFixture<TestHostComponent>;
    let hostComponent: TestHostComponent;
    let hostComponentDE: DebugElement;
    let hostComponentNE: Element;

    let component: CoverTypesRecordsTabulationComponent;
    let componentDE: DebugElement;
    let componentNE: Element;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [TestHostComponent, CoverTypesRecordsTabulationComponent, PaginationComponent, LoadingAnimationComponent],
            imports: [HttpClientTestingModule, LoggerModule.forRoot({ serverLoggingUrl: '/api/logs', level: NgxLoggerLevel.TRACE, serverLogLevel: NgxLoggerLevel.OFF })],
            providers: [ConnectivityStatusService, CoverTypesRecordsTabulationService],
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
        expect(hostComponentNE.querySelector('sb-cover-types-records-tabulation')).toEqual(jasmine.anything());
    });
});
