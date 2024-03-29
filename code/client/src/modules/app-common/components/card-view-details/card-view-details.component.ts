import { ChangeDetectionStrategy, Component, HostListener, Input, OnInit } from '@angular/core';

const LOG_PREFIX: string = "[Card View Details Component]";

@Component({
    selector: 'sb-card-view-details',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './card-view-details.component.html',
    styleUrls: ['card-view-details.component.scss'],
})
export class CardViewDetailsComponent implements OnInit {
    @Input() background!: string;
    @Input() color!: string;
    @Input() link = '';

    customClasses: string[] = [];

    constructor() {}
    ngOnInit() {
        if (this.background) {
            this.customClasses.push(this.background);
        }
        if (this.color) {
            this.customClasses.push(this.color);
        }
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
    }    
}
