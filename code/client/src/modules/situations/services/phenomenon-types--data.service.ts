import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { PhenomenonType } from '../models';
import { PHENOMENON_TYPES } from '@modules/situations/data/phenomenon-types';
import { map } from 'rxjs/operators';

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
