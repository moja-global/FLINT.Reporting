import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';

const LOG_PREFIX: string = "[Flux Types Records Tabulation Page]";

@Component({
    selector: 'sb-flux-types-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './flux-types-records-tabulation-page.component.html',
    styleUrls: ['flux-types-records-tabulation-page.component.scss'],
})
export class FluxTypesRecordsTabulationPageComponent implements OnInit {

    constructor(public connectivityStatusService: ConnectivityStatusService,) {}

    ngOnInit() {}
      
}
