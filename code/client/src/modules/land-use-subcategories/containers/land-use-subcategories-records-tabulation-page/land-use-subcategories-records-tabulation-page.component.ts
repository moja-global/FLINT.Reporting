import { ChangeDetectionStrategy, Component, HostListener, OnInit} from '@angular/core';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Land Use Subcategories Records Tabulation Page]";

@Component({
    selector: 'sb-land-use-subcategories-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './land-use-subcategories-records-tabulation-page.component.html',
    styleUrls: ['land-use-subcategories-records-tabulation-page.component.scss'],
})
export class LandUseSubcategoriesRecordsTabulationPageComponent implements OnInit {

    constructor(private log: NGXLogger) { }

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }

}
