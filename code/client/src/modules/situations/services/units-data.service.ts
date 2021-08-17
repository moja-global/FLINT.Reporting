import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Unit } from '../models';
import { UNITS } from '@modules/situations/data/units';
import { map } from 'rxjs/operators';

@Injectable()
export class UnitsDataService {

    constructor() { }

    getUnits$(): Observable<Unit[]> {
        return of(UNITS);
    }

    getUnit$(unitId: number): Observable<Unit> {
        return this.getUnits$()
            .pipe(
                map(units => units.filter(i => i.id == unitId)),
                map(units => units[0]));
    }



}