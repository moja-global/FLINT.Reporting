import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { PhenomenonType } from '../models';
import { map } from 'rxjs/operators';
import { PHENOMENON_TYPES } from '../data/phenomenon-types';

@Injectable()
export class PhenomenonTypesDataService {

    constructor() { }

    getPhenomenonTypes$(): Observable<PhenomenonType[]> {
        return of(PHENOMENON_TYPES);
    }

    getPhenomenonType$(phenomenonTypeId: number): Observable<PhenomenonType> {
        return this.getPhenomenonTypes$()
            .pipe(
                map(phenomenonTypes => phenomenonTypes.filter(i => i.id == phenomenonTypeId)),
                map(phenomenonTypes => phenomenonTypes[0]));
    }


}
