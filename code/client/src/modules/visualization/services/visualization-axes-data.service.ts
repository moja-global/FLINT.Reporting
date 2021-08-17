import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { VISUALIZATION_AXES } from '../data/visualization-axes';
import { VisualizationAxis } from '../models';

@Injectable()
export class VisualizationAxesDataService {

    constructor() { }

    getVisualizationAxes$(): Observable<VisualizationAxis[]> {
        return of(VISUALIZATION_AXES);
    }

    getVisualizationAxesByVisualization$(visualizationId: number): Observable<VisualizationAxis[]> {
        return this.getVisualizationAxes$()
            .pipe(map(groups => groups.filter(i => i.visualizationId == visualizationId)));
    }

    getVisualizationAxis$(visualizationAxisId: number): Observable<VisualizationAxis> {
        return this.getVisualizationAxes$()
            .pipe(
                map(groups => groups.filter(i => i.id == visualizationAxisId)),
                map(groups => groups[0]));
    }


}