import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable()
export class HomeService {
    constructor() {}

    getHome$(): Observable<{}> {
        return of({});
    }
}
