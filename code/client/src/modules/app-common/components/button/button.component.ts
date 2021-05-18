import { Component, ChangeDetectionStrategy, AfterViewInit, Input, Output, ElementRef, EventEmitter } from "@angular/core";
import { ConnectivityStatusService } from "@common/services";
import { NGXLogger } from "ngx-logger";
import { BehaviorSubject, Observable, Subscription } from "rxjs";

const LOG_PREFIX: string = "[Button Component]";

@Component({
  selector: 'sb-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './button.component.html',
  styleUrls: ['button.component.scss'],
})
export class ButtonComponent {

  // Instantiate Events Emitters.
  @Output() add: EventEmitter<void> = new EventEmitter<void>();
  @Output() update: EventEmitter<number> = new EventEmitter<number>();
  @Output() delete: EventEmitter<number> = new EventEmitter<number>();
  @Output() save: EventEmitter<void> = new EventEmitter<void>();
  @Output() yes: EventEmitter<void> = new EventEmitter<void>();
  @Output() no: EventEmitter<void> = new EventEmitter<void>();

  // Instantiate and avail the action variable to the parent component for customization.
  // Supported actions are add, update and delete
  @Input() action!: string;


  // Instantiate and avail the entity variable to the parent component for customization.
  // The entity is the thing that we are adding, updating or deleting
  @Input() entity: string = "";


  // Instantiate and avail the id variable to the parent component for customization.
  // The id is the unique identifier of the entity
  @Input() id!: number;


  // Instantiate and avail the guarded variable to the parent component for customization.
  // The guard variable, will determine whether or not a button is disabled when the system goes offline
  @Input() guarded: boolean = true;

  // Instantiate and avail the disabled variable to the parent component for customization.
  // The disabled variable, will determine whether or not a button is disabled.
  // If true, this value will override the disabled property that is automatically set when the system goes offline.
  // It should thus not be confused with the disabled$ observable
  @Input() disabled: boolean = false;

  // Instantiate an array containing offline css classes
  offlineClasses: string[] = ["btn", "btn-sm", "btn-secondary", "mr-1"];

  // Instantiate an array containing offline css classes
  onlineClasses: string[] = ["btn", "btn-sm", "btn-primary", "mr-1"];

  // Keep tabs on whether or not we are online
  online: boolean = false;

  // Keep tabs on the current css classes.
  // Should be updated when the online status changes
  private _classesSubject$: BehaviorSubject<string[]> = new BehaviorSubject<string[]>(this.offlineClasses);
  readonly classes$: Observable<string[]> = this._classesSubject$.asObservable();

  // Keep tabs on the current tooltip value.
  // Should be updated when the online status changes
  private _tooltipSubject$: BehaviorSubject<string> = new BehaviorSubject<string>("");
  readonly tooltip$: Observable<string> = this._tooltipSubject$.asObservable();

  // Keep tabs on the current button disabled value.
  // Should be updated when the online status changes
  private _disabledSubject$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);
  readonly disabled$: Observable<boolean> = this._disabledSubject$.asObservable();

  // Instantiate a gathering point for all the component's subscriptions.
  // This will make it easier to unsubscribe from all of them when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    public connectivityStatusService: ConnectivityStatusService,
    private log: NGXLogger) {
  }

  ngOnInit() {

    // Subscribe to connectivity status notifications.
    this.log.trace(`${LOG_PREFIX} Subscribing to connectivity status notifications`);
    this._subscriptions.push(
      this.connectivityStatusService.online$.subscribe(
        (status) => {
          if (status != this.online) {
            this.online = status;
            this._classesSubject$.next(this.getClasses());
            this._tooltipSubject$.next(this.getTooltip());
            this._disabledSubject$.next(this.getDisabled());
          }
        }));
  }

  ngOnDestroy() {

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach(s => s.unsubscribe());
  }

  onAdd(): void {
    this.add.emit();
  }

  onUpdate(): void {
    this.update.emit(this.id);
  }

  onDelete(): void {
    this.delete.emit(this.id);
  }

  onSave(): void {
    this.save.emit();
  }

  onYes(): void {
    this.yes.emit();
  }

  onNo(): void {
    this.no.emit();
  }


  getClasses(): string[] {

    if (this.disabled) {
      return this.offlineClasses;
    } else {
      return this.guarded ?
        this.online ? this.onlineClasses : this.offlineClasses :
        this.onlineClasses;
    }

  }

  getTooltip(): string {

    const disabledSuffix1: string = "is currently disabled because the running process is yet to be completed";
    const disabledSuffix2: string = "is currently disabled because you seem to be offline";

    switch (this.action) {

      case "add":

        if (this.disabled) {
          return `Add ${this.entity} ${disabledSuffix1}`;
        } else {
          return this.guarded ?
            this.online ? `Add ${this.entity}` : `Add ${this.entity} ${disabledSuffix2}` :
            `Add ${this.entity}`;
        }

      case "update":

        if (this.disabled) {
          return `Update ${this.entity} ${disabledSuffix1}`;
        } else {
          return this.guarded ?
            this.online ? `Update ${this.entity}` : `Update ${this.entity} ${disabledSuffix2}` :
            `Update ${this.entity}`;
        }

      case "delete":

        if (this.disabled) {
          return `Delete ${this.entity} ${disabledSuffix1}`;
        } else {
          return this.guarded ?
            this.online ? `Delete ${this.entity}` : `Delete ${this.entity} ${disabledSuffix2}` :
            `Delete ${this.entity}`;
        }

      case "save":

        if (this.disabled) {
          return `Save option ${disabledSuffix1}`;
        } else {
          return this.guarded ?
            this.online ? `Save` : `Save option ${disabledSuffix2}` :
            `Save`;
        }

      case "yes":

        if (this.disabled) {
          return `Yes option ${disabledSuffix1}`;
        } else {
          return this.guarded ?
            this.online ? `Yes` : `Yes option ${disabledSuffix2}` :
            `Yes`;
        }

      case "no":

        if (this.disabled) {
          return `No option ${disabledSuffix1}`;
        } else {
          return this.guarded ?
            this.online ? `No` : `No option ${disabledSuffix2}` :
            `No`;
        }

      default:
        return "";
    }

  }

  getDisabled(): boolean {

    if (this.disabled) {
      return true;
    } else {
      return this.guarded ?
        this.online ? false : true :
        false;
    }

  }

}
