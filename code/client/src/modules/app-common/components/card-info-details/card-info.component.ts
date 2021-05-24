import { ChangeDetectionStrategy, Component, HostListener, Input, OnInit } from '@angular/core';

const LOG_PREFIX: string = "[Card Info Component]";

@Component({
    selector: 'sb-card-info',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './card-info.component.html',
    styleUrls: ['card-info.component.scss'],
})
export class CardInfoComponent implements OnInit {

    constructor() {}

    ngOnInit() {

    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
    }    
}
