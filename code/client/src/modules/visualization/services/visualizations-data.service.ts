import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { VISUALIZATIONS } from '../data/visualizations';
import { Visualization } from '../models';

@Injectable()
export class VisualizationsDataService {

    constructor() { }

    getVisualizations$(): Observable<Visualization[]> {
        return of(VISUALIZATIONS);
    }

    getVisualizationsByGroup$(groupId: number): Observable<Visualization[]> {
        return this.getVisualizations$()
            .pipe(map(groups => groups.filter(i => i.groupId == groupId)));
    }

    getVisualization$(visualizationId: number): Observable<Visualization> {
        return this.getVisualizations$()
            .pipe(
                map(groups => groups.filter(i => i.id == visualizationId)),
                map(groups => groups[0]));
    }


}