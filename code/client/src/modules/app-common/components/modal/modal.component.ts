import { ChangeDetectionStrategy, Component, EventEmitter, HostListener, OnInit, Output } from '@angular/core';
import { ThemesService } from '@common/services';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NGXLogger } from 'ngx-logger';
import { Subscription } from 'rxjs';

const LOG_PREFIX: string = "[Modal Component]";

@Component({
    selector: 'sb-modal',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './modal.component.html',
    styleUrls: ['modal.component.scss'],
})
export class ModalComponent implements OnInit {

    @Output() closed: EventEmitter<void> = new EventEmitter<void>();

    // Classes that are adjusted on the fly based on the prevailing theme
    customClasses: string[] = [];

    // A common gathering point for all the component's subscriptions.
    // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
    private _subscriptions: Subscription[] = [];

    constructor(
        private activeModal: NgbActiveModal,
        private themesService: ThemesService,
        private log: NGXLogger) { }

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);

        this._subscriptions.push(
            this.themesService.themes$.subscribe((theme) => {
                this.customClasses.length = 0;
                this.customClasses.push(`modal-${theme}`);
            })
        );


    }

      @HostListener('window:beforeunload')
  ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
        this._subscriptions.forEach((s) => s.unsubscribe());

    } 

    onClose() {
        this.activeModal.dismiss('Cross Click');
        this.closed.emit();
    }
}
