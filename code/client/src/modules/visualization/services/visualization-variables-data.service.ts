import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { VISUALIZATION_VARIABLES } from '../data/visualization-variable';
import { VisualizationVariable } from '../models';

@Injectable()
export class VisualizationVariablesDataService {

    constructor() { }

    getVisualizationVariables$(): Observable<VisualizationVariable[]> {
        return of(VISUALIZATION_VARIABLES);
    }

    getVisualizationVariablesByVisualization$(visualizationId: number): Observable<VisualizationVariable[]> {
        return this.getVisualizationVariables$()
            .pipe(map(groups => groups.filter(i => i.visualizationId == visualizationId)));
    }

    getVisualizationVariable$(visualizationVariableId: number): Observable<VisualizationVariable> {
        return this.getVisualizationVariables$()
            .pipe(
                map(groups => groups.filter(i => i.id == visualizationVariableId)),
                map(groups => groups[0]));
    }


}