import { AfterViewInit, ChangeDetectionStrategy, Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BreadcrumbsComponent } from '@modules/navigation/components';
import { Breadcrumb } from '@modules/navigation/models/navigation.model';
import { NavigationService } from '@modules/navigation/services';
import { ReportingFramework } from '@modules/reporting-frameworks/models';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Reporting Frameworks Records Home Page]";

@Component({
    selector: 'sb-reporting-frameworks-records-home-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-frameworks-records-home-page.component.html',
    styleUrls: ['reporting-frameworks-records-home-page.component.scss'],
})
export class ReportingFrameworksRecordsHomePageComponent implements OnInit, AfterViewInit {

    //Keep tabs on the target Reporting Framework
    targetReportingFramework: ReportingFramework = new ReportingFramework();

    // Keep tabs on the currently opened Land Use Category id
    landUseCategoryId!: number;

    // Keep tabs on the currently opened Land Use Category name
    landUseCategoryName!: string;

    // Instantiate a central gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];



    constructor(
        private activatedRoute: ActivatedRoute,
        public navigationService: NavigationService,
        private log: NGXLogger) {

    }

    ngOnInit() {

        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(

            this.activatedRoute.queryParamMap
                .subscribe(params => {

                    // Get and update the id from the query parameters
                    this.log.trace(`${LOG_PREFIX} Getting and updating the id from the query parameters`);
                    const id: string | null = params.get('reportingFrameworkId');
                    this.log.debug(`${LOG_PREFIX} Id = ${id}`);

                    // Get the target Reporting Framework name from the query parameters
                    this.log.trace(`${LOG_PREFIX} Getting the target Reporting Framework name from the query parameters`);
                    const name: string | null = params.get('reportingFrameworkName');
                    this.log.debug(`${LOG_PREFIX} Reporting Framework name = ${name}`);


                    if (id != null && name != null) {

                        // Set the target Reporting Framework id in the current component
                        this.log.trace(`${LOG_PREFIX} Setting the target Reporting Framework id in the current component`);
                        this.targetReportingFrameworkId = parseInt(id);

                        // Set the target Reporting Framework name in the current component
                        this.log.trace(`${LOG_PREFIX} Setting the target Reporting Framework name in the current component`);
                        this.targetReportingFrameworkName = name;

                    } else {
                        // Reset the breadcrumb navigation
                        this.log.trace(`${LOG_PREFIX} Resetting the breadcrumb navigation`);
                        this.navigationService.dynamicBreadcrumbs = [
                            {
                                text: "Configurations",
                                active: false
                            }
                        ]
                    }



                })
        );


        this._subscriptions.push(
            this.navigationService.currentlySelectedInPageTab$().subscribe(num => {

                // Update the dynamic breadcrumb navigation
                this.log.trace(`${LOG_PREFIX} Updating the dynamic breadcrumb navigation`);

                let breadcrumbs: Breadcrumb[] = [
                    {
                        text: "Reporting Frameworks",
                        link: "/reporting_frameworks",
                        queryParams: "{}",
                        active: false
                    },
                    {
                        text: (this.targetReportingFramework.name) ? this.targetReportingFramework.name : '',
                        active: true
                    }
                ];

                let params = {
                    reportingFrameworkId: this.targetReportingFramework.id,
                    reportingFrameworkName: this.targetReportingFramework.name
                  };

                switch (num) {
                    case 1:
                        breadcrumbs.push(
                            {
                                text: "Reporting Tables",
                                active: true
                            }
                        );
                        break;
                    case 2:
                        breadcrumbs.push(
                            {
                                text: "Reporting Variables",
                                active: true
                            }
                        );
                        break;
                    case 3:
                        breadcrumbs.push(
                            {
                                text: "Land Use Categories",
                                active: true
                            }
                        );
                        break;
                    case 4:
                        breadcrumbs.push(
                            {
                                text: "Land Use Categories",
                                link: "/reporting_frameworks/home",
                                queryParams: params,
                                active: true
                            }
                        );
                        breadcrumbs.push(
                            {
                                text: this.landUseCategoryName + " Subcategories",
                                active: true
                            }
                        );
                        break;

                }


                this.navigationService.dynamicBreadcrumbs = breadcrumbs;

                /*this.navigationService.dynamicBreadcrumbs = [
                    {
                        text: "Reporting Frameworks",
                        link: "/reporting_frameworks",
                        queryParams: "{}",
                        active: false
                    },
                    {
                        text: name,
                        /*link: "/reporting_frameworks/home",*/
                /*queryParams: "{reportingFrameworkId: " + id + ", reportingFrameworkName: '" + name + "'}",
                active: true
            }
        ]*/
            })
        );


    }

    ngAfterViewInit() {


    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);

        // Clear all subscriptions
        this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
        this._subscriptions.forEach(s => s.unsubscribe());
    }

    /**
     * Sets the target reporting framework's id
     */
    set targetReportingFrameworkId(id: number | null) {
        this.log.trace(`${LOG_PREFIX} Setting Reporting Framework id to ${id}`);
        this.targetReportingFramework.id = id;
    }


    /**
     * Sets the target reporting framework's name
     */
    set targetReportingFrameworkName(name: string | null) {
        this.log.trace(`${LOG_PREFIX} Setting Reporting Framework name to ${name}`);
        this.targetReportingFramework.name = name;
    }

    onSelectItem(item: number) {

        // Update the currently selected in-page tab in the navigation service
        this.log.trace(`${LOG_PREFIX} Updating the currently selected in-page tab in the navigation service`);
        this.navigationService.currentlySelectedInPageTab = item;

    }


    /**
     * Opens Land Use Category Home Window
     */
    onOpenLandUseCategory(state: any) {

        this.log.trace(`${LOG_PREFIX} Opening Land Use Category Home Window`);
        this.log.debug(`${LOG_PREFIX} State = ${JSON.stringify(state)}`);

        this.landUseCategoryId = state.id;
        this.landUseCategoryName = state.name;

        this.onSelectItem(4);

    }

}
