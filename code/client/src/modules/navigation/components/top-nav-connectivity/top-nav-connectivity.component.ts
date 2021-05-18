import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ConnectivityStatusService } from '@common/services';

@Component({
    selector: 'sb-topnav-connectivity',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './top-nav-connectivity.component.html',
    styleUrls: ['top-nav-connectivity.component.scss'],
})
export class TopNavConnectivityComponent implements OnInit {
    constructor(public connectivityStatusService: ConnectivityStatusService) {}
    ngOnInit() {}
}
