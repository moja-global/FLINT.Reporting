import { ChangeDetectorRef, Component, DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NavigationService } from '@modules/navigation/services';
import { NavigationServiceStub } from '@testing/stubs';

import { LayoutMainComponent } from './layout-main.component';

@Component({
    template: `
        <sb-layout-main
            [someInput]="someInput"
            (someFunction)="someFunction($event)"
        ></sb-layout-main>
    `,
})
class TestHostComponent {
    // someInput = 1;
    // someFunction(event: Event) {}
}

describe('LayoutMainComponent', () => {
    let fixture: ComponentFixture<TestHostComponent>;
    let hostComponent: TestHostComponent;
    let hostComponentDE: DebugElement;
    let hostComponentNE: Element;

    let component: LayoutMainComponent;
    let componentDE: DebugElement;
    let componentNE: Element;

    let navigationService: NavigationService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [TestHostComponent, LayoutMainComponent],
            imports: [NoopAnimationsModule],
            providers: [
                { provide: NavigationService, useValue: NavigationServiceStub },
                ChangeDetectorRef,
            ],
            schemas: [NO_ERRORS_SCHEMA],
        }).compileComponents();

        fixture = TestBed.createComponent(TestHostComponent);
        hostComponent = fixture.componentInstance;
        hostComponentDE = fixture.debugElement;
        hostComponentNE = hostComponentDE.nativeElement;

        componentDE = hostComponentDE.children[0];
        component = componentDE.componentInstance;
        componentNE = componentDE.nativeElement;

        navigationService = TestBed.inject(NavigationService);

        fixture.detectChanges();
    });

    it('should display the component', () => {
        expect(hostComponentNE.querySelector('sb-layout-main')).toEqual(jasmine.anything());
    });
});
