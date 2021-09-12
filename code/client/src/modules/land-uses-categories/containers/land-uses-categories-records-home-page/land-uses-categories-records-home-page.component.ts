import { ChangeDetectionStrategy, Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { LandUseCategory } from '@modules/land-uses-categories/models/land-use-category.model';
import { Breadcrumb } from '@modules/navigation/models/navigation.model';
import { NavigationService } from '@modules/navigation/services/navigation.service';
import { ReportingFramework } from '@modules/reporting-frameworks/models/reporting-framework.model';
import { ReportingFrameworksDataService } from '@modules/reporting-frameworks/services/reporting-frameworks-data.service';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';

const LOG_PREFIX: string = "[Land Uses Categories Records Home Page]";

@Component({
    selector: 'sb-land-uses-categories-records-home-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './land-uses-categories-records-home-page.component.html',
    styleUrls: ['land-uses-categories-records-home-page.component.scss'],
})
export class LandUsesCategoriesRecordsHomePageComponent implements OnInit {

    //Keep tabs on the target landUseCategory type
    targetReportingFramework: ReportingFramework | undefined;

    //Keep tabs on the target landUseCategory
    targetLandUseCategory: LandUseCategory | undefined;    

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private activatedRoute: ActivatedRoute,
        private navigationService: NavigationService,
        private reportingFrameworksDataService: ReportingFrameworksDataService,
        private log: NGXLogger) { }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this.initReportingFrameworks(
            () => this.init());

    }


    initReportingFrameworks(callback: () => void) {

        // Check if there's a need for Reporting Frameworks initialization
        this.log.trace(`${LOG_PREFIX} Checking if there's a need for Reporting Frameworks initialization`);
        if (this.reportingFrameworksDataService.records.length != 0) {

            // The Reporting Frameworks have already been initialized
            this.log.trace(`${LOG_PREFIX} The Reporting Frameworks have already been initialized`);

            // Transfer control to the callback() method
            callback();

        } else {

            // There's a need for Reporting Frameworks initialization
            this.log.trace(`${LOG_PREFIX} There's a need for Reporting Frameworks initialization`);

            // Initializing the Reporting Frameworks
            this.log.trace(`${LOG_PREFIX} Initializing the Reporting Frameworks`);
            this.reportingFrameworksDataService
                .getAllReportingFrameworks()
                .pipe(first()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                .subscribe((response => {

                    // Reporting Frameworks initialization successfully completed
                    this.log.trace(`${LOG_PREFIX} Reporting Frameworks initialization successfully completed`);

                    // Transfer control to the callback() method
                    callback();

                }));

        }

    }   

    init() {

        this._subscriptions.push(
            this.activatedRoute.paramMap.subscribe(params => {

                // Get the Reporting Framework id from the path parameters
                this.log.trace(`${LOG_PREFIX} Getting the Reporting Framework id from the path parameters`);
                const temp: string | null = this.activatedRoute.snapshot.paramMap.get("reportingFrameworkId")
                const id: number | null = (temp == null ? null : parseInt(temp));
                this.log.debug(`${LOG_PREFIX} Reporting Framework Id = ${id}`);

                // Use the Reporting Framework id to get the Reporting Framework record
                this.log.trace(`${LOG_PREFIX} Using the Reporting Framework id to get the landUseCategory record`);
                if (id) {
                    this.targetReportingFramework = this.reportingFrameworksDataService.records.find((c:ReportingFramework) => c.id == id);
                    this.log.debug(`${LOG_PREFIX} Target ReportingFramework = ${JSON.stringify(this.targetReportingFramework)}`);
                }

                // Initialize a new breadcrumb navigation
                this.log.trace(`${LOG_PREFIX} Initializing a new breadcrumb navigation`);
                let breadcrumbs: Breadcrumb[] = [];                

                // Configure the breadcrumb navigation
                this.log.trace(`${LOG_PREFIX} Configuring the breadcrumb navigation`);

                breadcrumbs.push({
                    text: "Reporting Frameworks",
                    link: "/reporting_frameworks"
                });


                if (this.targetReportingFramework) {

                    breadcrumbs.push({
                        text: "" + this.targetReportingFramework.name
                    });

                }

                // Push the updated breadcrumb navigation to the navigation service
                this.log.trace(`${LOG_PREFIX} Pushing the updated breadcrumb navigation to the navigation service`);
                this.navigationService.dynamicBreadcrumbs = breadcrumbs;
                this.log.debug(`${LOG_PREFIX} Breadcrumbs = ${JSON.stringify(breadcrumbs)}`);
            })
        );


    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach(s => s.unsubscribe());
    }


}