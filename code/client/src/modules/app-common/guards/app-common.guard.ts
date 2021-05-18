import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { Observable, of } from 'rxjs';

const LOG_PREFIX: string = "[App Common Guard]";

@Injectable()
export class AppCommonGuard implements CanActivate {
    canActivate(): Observable<boolean> {
        return of(true);
    }
}
