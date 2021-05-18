import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ThemesService } from '@common/services';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Modal Component]";

@Component({
    selector: 'sb-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './modal.component.html',
    styleUrls: ['modal.component.scss'],
})
export class ModalComponent implements OnInit {

    // Classes that are adjusted on the fly based on the prevailing theme
    customClasses: string[] = [];

    // Instantiate a gathering point for all the component's subscriptions.
    // This will make it easier to unsubscribe from all of them when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        public activeModal: NgbActiveModal,
        private themesService: ThemesService) { }

    ngOnInit() {
        this._subscriptions.push(
            this.themesService.themes$.subscribe((theme) => {
                this.customClasses.length = 0;
                this.customClasses.push(`modal-${theme}`);
            })
        );
    }

    ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    } 
}
