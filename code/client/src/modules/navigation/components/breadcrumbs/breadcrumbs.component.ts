import { ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Breadcrumb } from '@modules/navigation/models';
import { NavigationService } from '@modules/navigation/services';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Breadcrumbs Component]";

@Component({
    selector: 'sb-breadcrumbs',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './breadcrumbs.component.html',
    styleUrls: ['breadcrumbs.component.scss'],
})
export class BreadcrumbsComponent implements OnInit, OnDestroy {

    // Allows the parent to state whether or not the breadcrumb is statically or dynamically generated.
    // If static, then the breadcrumb details will be read from the module's router configurations
    @Input() dynamic = false;

    // The list of breadcrumbs
    @Input() breadcrumbs!: Breadcrumb[];

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed. 
    private _subscriptions: Subscription[] = [];

    constructor(
        public navigationService: NavigationService, 
        private cd: ChangeDetectorRef, 
        private router: Router,
        private log: NGXLogger) {}

    ngOnInit() {

        if(this.dynamic) {
            this._subscriptions.push(
                this.navigationService.dynamicBreadcrumbs$().subscribe(dynamicBreadcrumbs => {
                    this.log.trace(`${LOG_PREFIX} Updating Dynamic Breadcrumb Navigation`);
                    this.log.debug(`${LOG_PREFIX} Breadcrumb = ${JSON.stringify(dynamicBreadcrumbs)}`);
                    this.breadcrumbs = Object.assign([], dynamicBreadcrumbs);
                    this.cd.detectChanges();
                })
            );
        } else {
            this._subscriptions.push(
                this.navigationService.routeData$().subscribe(routeData => {
                    this.log.trace(`${LOG_PREFIX} Updating Static Breadcrumb Navigation`);
                    this.log.debug(`${LOG_PREFIX} Breadcrumb = ${JSON.stringify(routeData.breadcrumbs)}`);                    
                    this.breadcrumbs = Object.assign([], routeData.breadcrumbs);
                    this.cd.detectChanges();
                })
            );
        }

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
        this._subscriptions.forEach((s) => s.unsubscribe());
    }

    onOpenLink(link: any) {

        this.log.trace(`${LOG_PREFIX} Opening Link`);

        this.router.navigate(link);

    }    
}
