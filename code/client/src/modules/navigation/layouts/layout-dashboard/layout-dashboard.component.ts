import {
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    HostBinding,
    HostListener,
    Input,
    OnDestroy,
    OnInit,
} from '@angular/core';
import { sideNavItems, sideNavSections } from '@modules/navigation/data';
import { NavigationService } from '@modules/navigation/services';
import { Subscription } from 'rxjs';

@Component({
    selector: 'sb-layout-dashboard',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './layout-dashboard.component.html',
    styleUrls: ['layout-dashboard.component.scss'],
})
export class LayoutDashboardComponent implements OnInit, OnDestroy {

    @Input() static = false;
    @Input() light = false;

    @HostBinding('class.sb-sidenav-toggled') sideNavHidden = false;

    subscription: Subscription = new Subscription();
    sideNavItems = sideNavItems;
    sideNavSections = sideNavSections;

    constructor(
        public navigationService: NavigationService,
        private cd: ChangeDetectorRef
    ) { }
    ngOnInit() {
        this.subscription.add(
            this.navigationService.sideNavVisible$().subscribe(isVisible => {
                this.sideNavHidden = !isVisible;
                this.cd.markForCheck();
            })
        );
    }
    
    ngOnDestroy() {
        this.subscription.unsubscribe();
    }
}
