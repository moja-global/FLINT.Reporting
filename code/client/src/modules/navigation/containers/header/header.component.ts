import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { ThemesService } from '@common/services';
import { Subscription } from 'rxjs';

@Component({
    selector: 'sb-header',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './header.component.html',
    styleUrls: ['header.component.scss'],
})
export class HeaderComponent implements OnInit {

    @Input() title!: string;
    @Input() subtitle!: string;

    // Classes that are adjusted on the fly based on the prevailing theme
    customClasses: string[] = [];

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(private themesService: ThemesService) { }

    ngOnInit() {
        this._subscriptions.push(
            this.themesService.themes$.subscribe((theme) => {
                this.customClasses.length = 0;
                this.customClasses.push(`header-${theme}`);
            })
        );
    }

      
  ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }
}
