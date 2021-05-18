import { Component, ChangeDetectionStrategy} from "@angular/core";

const LOG_PREFIX: string = "[Overlay Component]";

@Component({
  selector: 'sb-overlay',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './overlay.component.html',
  styleUrls: ['overlay.component.scss'],
})
export class OverlayComponent {

  constructor() { }

}
