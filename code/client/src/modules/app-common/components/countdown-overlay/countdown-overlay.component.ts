import { Component, ChangeDetectionStrategy, AfterViewInit, Input, Output, ElementRef, EventEmitter, AfterContentInit } from "@angular/core";
import { NGXLogger } from "ngx-logger";

const LOG_PREFIX: string = "[Countdown Overlay Component]";

@Component({
  selector: 'sb-countdown-overlay',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './countdown-overlay.component.html',
  styleUrls: ['countdown-overlay.component.scss'],
})
export class CountdownOverlayComponent{

  // Declare and avail a crosshead variable to the parent component.
  // This will provide the parent component with a means of specifying what the countdown is all about.
  @Input() crosshead: string = "";

  // Instantiate and avail the desired number of counts to the parent component for customizations
  @Input() counter: number = 10;

  // Instantiate and avail the time period that should be skipped in between counts to the parent component for customizations (ms)
  @Input() interval: number = 1000;

  // Instantiate the count down completion Event Emitter
  @Output() public completed: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate and initialize the current count
  count: number = this.counter;

  customClasses: string[] = ["text-primary"];

  constructor() { }

  onValueChange(event: any){
    this.count = event;
    if(this.count == 0) {
      this.completed.emit();
    }
  }

}
