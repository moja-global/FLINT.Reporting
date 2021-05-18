import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';

const LOG_PREFIX: string = "[Unit categories Records Tabulation Page]";

@Component({
    selector: 'sb-unit-categories-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './unit-categories-records-tabulation-page.component.html',
    styleUrls: ['unit-categories-records-tabulation-page.component.scss'],
})
export class UnitCategoriesRecordsTabulationPageComponent implements OnInit {

    constructor(public connectivityStatusService: ConnectivityStatusService,) {}

    ngOnInit() {}
      
}
