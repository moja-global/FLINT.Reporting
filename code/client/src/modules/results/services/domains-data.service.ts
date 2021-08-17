import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { DOMAINS } from '../data/domains';
import { Domain } from '../models/domain.model';

@Injectable()
export class DomainsDataService {

    constructor() { }

    getDomains$(): Observable<Domain[]> {
        return of(DOMAINS);
    }

    getDomain$(domainId: number): Observable<Domain> {
        return this.getDomains$()
            .pipe(
                map(domains => domains.filter(i => i.id == domainId)),
                map(domains => domains[0]));
    }


}