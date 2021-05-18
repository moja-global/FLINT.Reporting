import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

@Component({
    selector: 'sb-layout-home',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './layout-home.component.html',
    styleUrls: ['layout-home.component.scss'],
})
export class LayoutHomeComponent implements OnInit {
    constructor() {}
    ngOnInit() {}
}
