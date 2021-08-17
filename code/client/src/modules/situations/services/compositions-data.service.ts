import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Composition } from '../models';
import { map } from 'rxjs/operators';
import { COMPOSITIONS } from '../data/compositions';

@Injectable()
export class CompositionsDataService {

    constructor() { }

    getCompositions$(): Observable<Composition[]> {
        return of(COMPOSITIONS);
    }

    getComposition$(progressionId: number): Observable<Composition> {
        return this.getCompositions$()
            .pipe(
                map(progressions => progressions.filter(i => i.id == progressionId)),
                map(progressions => progressions[0]));
    }


}