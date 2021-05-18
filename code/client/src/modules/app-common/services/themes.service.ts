import { Injectable } from '@angular/core';
import { BehaviorSubject} from 'rxjs';

const LOG_PREFIX: string = "[Themes Service]";

@Injectable({ providedIn: 'root' })
export class ThemesService {

    private _themesSubject$ = new BehaviorSubject<string>("light");
    readonly themes$ = this._themesSubject$.asObservable();

    set theme(theme: string) {
        this._themesSubject$.next(theme);
    }

}