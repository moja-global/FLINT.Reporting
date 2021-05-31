import { ChangeDetectionStrategy, Component, HostListener, OnInit} from '@angular/core';
import { NGXLogger } from 'ngx-logger';

const LOG_PREFIX: string = "[Land Use Categories Records Tabulation Page]";

@Component({
    selector: 'sb-land-use-categories-records-tabulation-page',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './land-use-categories-records-tabulation-page.component.html',
    styleUrls: ['land-use-categories-records-tabulation-page.component.scss'],
})
export class LandUseCategoriesRecordsTabulationPageComponent implements OnInit {

    constructor(private log: NGXLogger) { }

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }

}
