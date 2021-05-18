import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Component({
    selector: 'sb-dashboard',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './dashboard.component.html',
    styleUrls: ['dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {

    private _subject$ = new BehaviorSubject<number>(Date.now());
    readonly time$ = this._subject$.asObservable();

    constructor() {
        setInterval(() => {
          this._subject$.next(Date.now());
        }, 1);
    }
    ngOnInit() {}
}
