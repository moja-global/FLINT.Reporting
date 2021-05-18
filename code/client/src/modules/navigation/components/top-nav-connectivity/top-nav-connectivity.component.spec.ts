import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component, DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ConnectivityStatusService } from '@common/services';

import { TopNavConnectivityComponent } from './top-nav-connectivity.component';

@Component({
    template: `
        <sb-topnav-connectivity
            [someInput]="someInput"
            (someFunction)="someFunction($event)"
        ></sb-topnav-connectivity>
    `,
})
class TestHostComponent {
    // someInput = 1;
    // someFunction(event: Event) {}
}

describe('TopNavConnectivityComponent', () => {
    let fixture: ComponentFixture<TestHostComponent>;
    let hostComponent: TestHostComponent;
    let hostComponentDE: DebugElement;
    let hostComponentNE: Element;

    let component: TopNavConnectivityComponent;
    let componentDE: DebugElement;
    let componentNE: Element;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [TestHostComponent, TopNavConnectivityComponent],
            imports: [NoopAnimationsModule, HttpClientTestingModule,],
            providers: [ConnectivityStatusService],
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
        expect(hostComponentNE.querySelector('sb-topnav-connectivity')).toEqual(jasmine.anything());
    });
});
