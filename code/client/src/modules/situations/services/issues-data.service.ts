import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Issue } from '../models';
import { ISSUES } from '@modules/situations/data/issue';
import { map } from 'rxjs/operators';

@Injectable()
export class IssuesDataService {

    constructor() { }

    getIssues$(): Observable<Issue[]> {
        return of(ISSUES);
    }

    getIssue$(issueId: number): Observable<Issue> {
        return this.getIssues$()
            .pipe(
                map(issues => issues.filter(i => i.id == issueId)),
                map(issues => issues[0]));
    }


}