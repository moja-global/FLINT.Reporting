import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { ThemesService } from '@common/services';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Card Component]";

@Component({
    selector: 'sb-card',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './card.component.html',
    styleUrls: ['card.component.scss'],
})
export class CardComponent implements OnInit {
    
    // Classes that are adjusted on the fly based on the prevailing theme
    customClasses: string[] = [];

    // Instantiate a gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(private themesService: ThemesService) { }

    ngOnInit() {
        this._subscriptions.push(
            this.themesService.themes$.subscribe((theme) => {
                this.customClasses.length = 0;
                this.customClasses.push(`card-${theme}`);
            })
        );
    }

    ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }    
}
