import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'sb-dashboard-header',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './dashboard-header.component.html',
    styleUrls: ['dashboard-header.component.scss'],
})
export class DashboardHeaderComponent implements OnInit {
    @Input() title!: string;
    @Input() hideBreadcrumbs = false;

    constructor() {}
    ngOnInit() {}
}
