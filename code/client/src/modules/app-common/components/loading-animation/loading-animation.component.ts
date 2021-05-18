import { AfterViewInit, ChangeDetectionStrategy, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, fromEvent, Observable, Subscription } from 'rxjs';
import { filter, debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';

const LOG_PREFIX: string = "[Loading Animation Component]";

@Component({
  selector: 'sb-loading-animation',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './loading-animation.component.html',
  styleUrls: ['loading-animation.component.scss'],
})
export class LoadingAnimationComponent implements AfterViewInit {

  // Instantiate a loading status observable field.
  // This field's value will be updated by external processes whenever the user 
  // starts and completes a background task.  
  private _loadingSubject$ = new BehaviorSubject<boolean>(false);
  readonly loading$ = this._loadingSubject$.asObservable();

  constructor(private log: NGXLogger) { }

  ngAfterViewInit() {}

  ngOnDestroy() {}

  /**
   * Sets the current loading status: true or false
   */
  public set loading(loading: boolean) {
    this.log.debug(`${LOG_PREFIX} Setting loading status to ${JSON.stringify(loading)}`);
    this._loadingSubject$.next(loading);
  }
}
