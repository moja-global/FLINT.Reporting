import { Component, ChangeDetectionStrategy, AfterViewInit, Input, Output, ElementRef, EventEmitter, HostListener } from "@angular/core";
import { NGXLogger } from "ngx-logger";
import { fromEvent, Observable, Subscription } from "rxjs";
import { debounceTime, distinctUntilChanged, map } from "rxjs/operators";

const LOG_PREFIX: string = "[Search Component]";

@Component({
  selector: 'sb-search',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './search.component.html',
  styleUrls: ['search.component.scss'],
})
export class SearchComponent implements AfterViewInit {

  // Instantiate and avail the placeholder variable field to the parent component for customization.
  @Input() placeholder: string = "Search";

  // Instantiate and avail the delay variable field to the parent component for customization.
  // Delay, in this context, is the gap in time that should precede the emitting of search requests for actioning.
  @Input() delay: number = 300;

  // Instantiate a Search Events Emitter.
  @Output() searched: EventEmitter<string> = new EventEmitter<string>();

  // Instantiate the search term
  public searchTerm: string = '';

  // A common gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.  
  private _subscription: Subscription = new Subscription();

  constructor(
    private elementRef: ElementRef,
    private log: NGXLogger) { }


  ngAfterViewInit() {


    // Subscribe to the search input field's keyup events. 
    // Broadcast a minimum of one event every 'delay' milliseconds    
    this.log.trace(`${LOG_PREFIX} Subscribing to the search input field's keyup events`);
    this._subscription.add(
      fromEvent(this.elementRef.nativeElement, 'keyup')
        .pipe(
          map(() => this.searchTerm),
          debounceTime(this.delay),
          distinctUntilChanged())
        .subscribe(input => {

          this.log.debug(`${LOG_PREFIX} Emitting search request with '${this.searchTerm}' as the search term`);
          this.searched.emit(this.searchTerm);
        })
    );

  }


  ngOnInit() {
    this.log.trace(`${LOG_PREFIX} Initializing Component`);
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscription.unsubscribe();
  }

}
