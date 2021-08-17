import { JsonPipe } from '@angular/common';
import { Injectable } from '@angular/core';
import { DatabaseFilter } from '@common/models';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject} from 'rxjs';

const LOG_PREFIX: string = "[Database Filter Service]";

@Injectable({ providedIn: 'root' })
export class DatabaseFilterService {

    private _filterSubject$ = new BehaviorSubject<DatabaseFilter>({ databaseId: -1, startYear: -1, endYear: -1, landUseCategoryId: -1, partyId: 48});
    readonly filter$ = this._filterSubject$.asObservable();

    constructor(private log: NGXLogger){
    }

    set filter(f: DatabaseFilter) {

        this.log.trace(`${LOG_PREFIX} Setting filter to ${JSON.stringify(f)}`);
        this._filterSubject$.next(Object.assign({}, f));
    } 

    get filter(): DatabaseFilter {

        //this.log.trace(`${LOG_PREFIX} Returning ${JSON.stringify(this._filterSubject$.value)} as filter`);
        return this._filterSubject$.value;
    }

}