import { Component, ChangeDetectionStrategy, Input, AfterContentInit, OnInit, OnDestroy, HostListener } from "@angular/core";
import { NGXLogger } from "ngx-logger";

const LOG_PREFIX: string = "[Message Overlay Component]";

@Component({
  selector: 'sb-message-overlay',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './message-overlay.component.html',
  styleUrls: ['message-overlay.component.scss'],
})
export class MessageOverlayComponent implements OnInit, OnDestroy, AfterContentInit {

  @Input() activity!: string; // creating, updating, deleting
  @Input() status!: string; // saving, succeeded, failed

  private _icon!: string;
  private _crosshead!: string;
  private _content!: string;

  iconClasses: string[] = [];

  constructor(private log: NGXLogger) { }

  ngOnInit() {
    this.log.trace(`${LOG_PREFIX} Initializing Component`);
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {
    this.log.trace(`${LOG_PREFIX} Destroying Component`);
  }

  ngAfterContentInit() {
    switch (this.activity) {
      case "creating":
        switch (this.status) {
          case "initializing":
            this._icon = "wrench";
            this._crosshead = "Initializing";
            this.iconClasses.push("text-primary");
            break;
          case "saving":
            this._icon = "stopwatch";
            this._crosshead = "Saving";
            this.iconClasses.push("text-primary");
            break;
          case "succeeded":
            this._icon = "check-circle";
            this._crosshead = "Done";
            this._content = "Do you want to add another record?";
            this.iconClasses.push("text-success");
            break;
          case "failed":
            this._icon = "times-circle";
            this._crosshead = "Something unexpected happened";
            this._content = "Should we retry the request?";
            this.iconClasses.push("text-danger");
            break;
        }
        break;
      case "updating":
        switch (this.status) {
          case "initializing":
            this._icon = "wrench";
            this._crosshead = "Initializing";
            this.iconClasses.push("text-primary");
            break;
          case "saving":
            this._icon = "stopwatch";
            this._crosshead = "Saving";
            this.iconClasses.push("text-primary");
            break;
          case "succeeded":
            this._icon = "check-circle";
            this._crosshead = "Done";
            this._content = "";
            this.iconClasses.push("text-success");
            break;
          case "failed":
            this._icon = "times-circle";
            this._crosshead = "Something unexpected happened";
            this._content = "Should we retry the request?";
            this.iconClasses.push("text-danger");
            break;
        }
        break;
      case "deleting":
        switch (this.status) {
          case "deleting":
            this._icon = "stopwatch";
            this._crosshead = "Deleting";
            this.iconClasses.push("text-primary");
            break;
          case "succeeded":
            this._icon = "check-circle";
            this._crosshead = "Done";
            this._content = "";
            this.iconClasses.push("text-success");
            break;
          case "failed":
            this._icon = "times-circle";
            this._crosshead = "Something unexpected happened";
            this._content = "Should we retry the request?";
            this.iconClasses.push("text-danger");
            break;
        }
        break;
    }
  }

  public get icon(): string {
    return this._icon;
  }

  public get crosshead(): string {
    return this._crosshead;
  }

  public get content(): string {
    return this._content;
  }

}
