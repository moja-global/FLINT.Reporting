import { Injectable } from '@angular/core';
import { Message } from '@common/models/message.model';
import { BehaviorSubject, Observable, Subject } from 'rxjs';

const LOG_PREFIX: string = "[Message Service]";

@Injectable({ providedIn: 'root' })
export class MessageService {

    private _messageSubject$ = new Subject<Message>();
    readonly messages$ = this._messageSubject$.asObservable();

    sendMessage(message: Message) {
        this._messageSubject$.next(message);
    }

    clearMessages() {
        this._messageSubject$.next();
    }

}