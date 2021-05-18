import { TestBed } from '@angular/core/testing';
import { MessageType } from '@common/models';
import { Message } from '@common/models/message.model';
import { isEmpty } from 'rxjs/operators';

import { MessageService } from './messages.service';

describe('MessageService', () => {

    let messagesService: MessageService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [MessageService],
        });
        messagesService = TestBed.inject(MessageService);
    });

    describe('messages$', () => {
        it('should return Observable<Message[]>', () => {
            messagesService.messages$.subscribe(response => {
                expect(response).toBeDefined();
            });
        });
    });


    describe('sendMessage', () => {
        it('should set the next message', () => {

            // Define the message
            let message: Message = { "type": MessageType.Error, "message": "Some Error Message" };

            // Subscribe to the message service
            messagesService.messages$.subscribe(response => {

                // Assert that the next message is the defined message
                expect(response).toEqual(message);
            });            

            // Send the message
            messagesService.sendMessage(message);



        });
    });

    describe('clearMessages', () => {

        it('should clear all messages', () => {

            // Define the message
            let message: Message = { "type": MessageType.Error, "message": "Some Error Message" };

            // Send the message
            messagesService.sendMessage(message);

            // Clear the messages
            messagesService.clearMessages();

            // Assert that there are no messages
            messagesService.messages$.pipe(isEmpty()).subscribe(response => {
                expect(response).toEqual(true)
            });
        });
    });
});
