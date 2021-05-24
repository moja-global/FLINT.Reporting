import { Component, ChangeDetectionStrategy, HostListener} from "@angular/core";
import { NGXLogger } from "ngx-logger";

const LOG_PREFIX: string = "[Overlay Component]";

@Component({
  selector: 'sb-overlay',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './overlay.component.html',
  styleUrls: ['overlay.component.scss'],
})
export class OverlayComponent {

  constructor(private log: NGXLogger) { }

    ngOnInit() {
        this.log.trace(`${LOG_PREFIX} Initializing Component`);
    }

    @HostListener('window:beforeunload')
    ngOnDestroy() {
        this.log.trace(`${LOG_PREFIX} Destroying Component`);
    }

}
