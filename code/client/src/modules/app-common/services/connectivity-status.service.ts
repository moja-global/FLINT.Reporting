import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

const LOG_PREFIX: string = "[Connectivity Status Service]";

@Injectable({ providedIn: 'root' })
export class ConnectivityStatusService {

  // Instantiate an observable field of the online status.
  // This field's value will be updated every couple of milliseconds.
  private _onlineSubject$ = new BehaviorSubject<boolean>(true);
  readonly online$ = this._onlineSubject$.asObservable();  

  constructor(private http: HttpClient){

    // TODO: Create internal online test API
    // See: https://stackoverflow.com/questions/14283124/navigator-online-not-always-working
    setInterval(()=>{
        this.http.delete('https://jsonplaceholder.typicode.com/posts/1')
        .subscribe({
            next: data => {
                this._onlineSubject$.next(true);
            },
            error: error => {
                this._onlineSubject$.next(false);
            }
        });
    }, 5000)
 }

}