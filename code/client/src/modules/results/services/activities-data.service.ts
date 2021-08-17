import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';
import { ACTIVITIES } from '../data/activities';
import { Activity } from '../models';

@Injectable()
export class ActivitiesDataService {

    constructor() { }

    getActivities$(domainId: number): Observable<Activity[]> {
        return of(ACTIVITIES)
            .pipe(
                map(activities => activities.filter(q => q.domainId == domainId)));
    }   
    
    getActivity$(activityId: number): Observable<Activity> {
        return of(ACTIVITIES)
            .pipe(
                map(activities => activities.filter(i => i.id == activityId)),
                map(activities => activities[0]));
    }    

}
