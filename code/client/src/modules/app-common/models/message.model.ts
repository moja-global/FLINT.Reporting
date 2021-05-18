import { MessageType } from "./message.type.model";

export interface Message {
    type: MessageType;
    message: string;
}