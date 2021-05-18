import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'sb-main-header',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './main-header.component.html',
    styleUrls: ['main-header.component.scss'],
})
export class MainHeaderComponent implements OnInit {
    
    @Input() title!: string;
    @Input() subtitle!: string;
    @Input() hideBreadcrumbs = false;

    constructor() {}
    ngOnInit() {}
}
