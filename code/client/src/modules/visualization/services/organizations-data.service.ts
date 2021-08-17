import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ORGANIZATIONS } from '../data/organizations';
import { Organization } from '../models/organization.model';

@Injectable()
export class OrganizationsDataService {

    constructor() { }

    getOrganizations$(): Observable<Organization[]> {
        return of(ORGANIZATIONS);
    }

    getOrganizationsByOrganizationType$(organizationTypeId: number): Observable<Organization[]> {
        return this.getOrganizations$()
            .pipe(map(organizations => organizations.filter(i => i.organizationTypeId == organizationTypeId)));
    }


    getOrganization$(organizationId: number): Observable<Organization> {
        return this.getOrganizations$()
            .pipe(
                map(groups => groups.filter(i => i.id == organizationId)),
                map(groups => groups[0]));
    }


}