import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Progression } from '../models';
import { map } from 'rxjs/operators';
import { PROGRESSIONS } from '../data/progressions';

@Injectable()
export class ProgressionsDataService {

    constructor() { }

    getProgressions$(): Observable<Progression[]> {
        return of(PROGRESSIONS);
    }

    getProgression$(progressionId: number): Observable<Progression> {
        return this.getProgressions$()
            .pipe(
                map(progressions => progressions.filter(i => i.id == progressionId)),
                map(progressions => progressions[0]));
    }


}