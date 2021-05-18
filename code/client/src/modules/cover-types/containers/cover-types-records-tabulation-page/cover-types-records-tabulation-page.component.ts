import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';

const LOG_PREFIX: string = "[Cover Types Records Tabulation Page]";

@Component({
    selector: 'sb-cover-types-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './cover-types-records-tabulation-page.component.html',
    styleUrls: ['cover-types-records-tabulation-page.component.scss'],
})
export class CoverTypesRecordsTabulationPageComponent implements OnInit {

    constructor(public connectivityStatusService: ConnectivityStatusService,) {}

    ngOnInit() {}
      
}
