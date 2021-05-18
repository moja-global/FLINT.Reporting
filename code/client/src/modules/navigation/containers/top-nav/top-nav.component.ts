import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { ThemesService } from '@common/services';
import { NavigationService } from '@modules/navigation/services';
import { Subscription } from 'rxjs';

@Component({
    selector: 'sb-topnav',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './top-nav.component.html',
    styleUrls: ['top-nav.component.scss'],
})
export class TopNavComponent implements OnInit, OnDestroy {

    // Classes that are adjusted on the fly based on the prevailing theme
    customClasses: string[] = [];

    // Instantiate a gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscriptions: Subscription[] = [];    

    constructor(private navigationService: NavigationService, private themesService: ThemesService) {}

    ngOnInit() {
        this._subscriptions.push(
            this.themesService.themes$.subscribe((theme) => {
                this.customClasses.length = 0;
                this.customClasses.push(`navbar-${theme}`);
                this.customClasses.push(`bg-${theme}`);
            })
        );
    }

    ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }    

    toggleSideNav() {
        this.navigationService.toggleSideNav();
    }
}
