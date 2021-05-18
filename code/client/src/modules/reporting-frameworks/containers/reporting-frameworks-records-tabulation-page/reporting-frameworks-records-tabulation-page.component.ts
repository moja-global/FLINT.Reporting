import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';

const LOG_PREFIX: string = "[Reporting Frameworks Records Tabulation Page]";

@Component({
    selector: 'sb-reporting-frameworks-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './reporting-frameworks-records-tabulation-page.component.html',
    styleUrls: ['reporting-frameworks-records-tabulation-page.component.scss'],
})
export class ReportingFrameworksRecordsTabulationPageComponent implements OnInit {

    constructor(public connectivityStatusService: ConnectivityStatusService,) {}

    ngOnInit() {}
      
}
