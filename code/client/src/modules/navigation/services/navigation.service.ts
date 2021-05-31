import { Injectable } from '@angular/core';
import { ActivatedRoute, ChildActivationEnd, Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter } from 'rxjs/operators';

import { Breadcrumb, SBRouteData } from '../models';

@Injectable()
export class NavigationService {

    // Main Navigation
    _sideNavVisibleSubject$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(true);
    _routeDataSubject$: BehaviorSubject<SBRouteData> = new BehaviorSubject<SBRouteData>({} as SBRouteData);
    _currentURLSubject$: BehaviorSubject<string> = new BehaviorSubject<string>('');

    // Breadcrumb Navigation
    _dynamicBreadcrumbsSubject$ : BehaviorSubject<Breadcrumb[]> = new BehaviorSubject<Breadcrumb[]>([]);

    // In-Page Navigation
    _currentlySelectedInPageTabSubject$: BehaviorSubject<number> = new BehaviorSubject<number>(1);

    constructor(public route: ActivatedRoute, public router: Router) {
        this.router.events
            .pipe(filter(event => event instanceof ChildActivationEnd))
            .subscribe(event => {
                let snapshot = (event as ChildActivationEnd).snapshot;
                while (snapshot.firstChild !== null) {
                    snapshot = snapshot.firstChild;
                }
                this._routeDataSubject$.next(snapshot.data as SBRouteData);
                this._currentURLSubject$.next(router.url);
            });
    }

    toggleSideNav(visibility?: boolean) {
        if (typeof visibility !== 'undefined') {
            this._sideNavVisibleSubject$.next(visibility);
        } else {
            this._sideNavVisibleSubject$.next(!this._sideNavVisibleSubject$.value);
        }
    }    

    sideNavVisible$(): Observable<boolean> {
        return this._sideNavVisibleSubject$.asObservable();;
    }

    routeData$(): Observable<SBRouteData> {
        return this._routeDataSubject$.asObservable();;
    }

    currentURL$(): Observable<string> {
        return this._currentURLSubject$.asObservable();;
    }

    set dynamicBreadcrumbs(dynamicBreadcrumbs: Breadcrumb[]){
        this._dynamicBreadcrumbsSubject$.next(Object.assign([],dynamicBreadcrumbs));
    }

    dynamicBreadcrumbs$(): Observable<Breadcrumb[]> {
        return this._dynamicBreadcrumbsSubject$.asObservable();
    }    

    set currentlySelectedInPageTab(currentlySelectedInPageTab: number){
        this._currentlySelectedInPageTabSubject$.next(currentlySelectedInPageTab);
    }

    currentlySelectedInPageTab$(): Observable<number> {
        return this._currentlySelectedInPageTabSubject$.asObservable();
    }    
}
