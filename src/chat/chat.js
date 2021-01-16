import SockJs from 'sockjs-client';
import Stomp from 'stompjs';
import {
    IDBChatMessageInsert,
    localChatMessageUpdate,
    localChatMessageInsert,
    user,
    localMessages, IDBChatMessageUpdate
} from '../storage/storage';
import {MessageHandler} from '../utils';
import _ from "loadsh";
const sock = new SockJs('/webchat');

const stompClient = Stomp.over(sock);


var addContactMessageConfirmInHandler;

const setAddContactMessageConfirmInHandler = (call)=>{
    addContactMessageConfirmInHandler = call;
};

const privateSubscribe = () => {
    stompClient.subscribe('/subscribe/user/' + user.userInfo.userUUID + '/private/chat', (res) => {
        res.ack({'ack': 1});
        let messageRes = JSON.parse(res.body);
        let message = {
            ...messageRes,
            roomType: 'private',
            toUserUUID: user.userInfo.userUUID
        };
        let messageLoc = localChatMessageInsert(message);
        messagesInHandler.invoke({messageLocKey:messageLoc.key},message);
        IDBChatMessageInsert(message);
    }, {ack: 'client'})

};
const groupSubscribe = (contactList) => {

    contactList.forEach(element => {
        stompClient.subscribe('/subscribe/user/' + element.groupUUID + '/group/chat', (res) => {
            // wirte callback func here.
            let messageRes = JSON.parse(res.body);
            let message = {
                ...messageRes,
                roomType: 'group'
            };
            if (messageRes.fromUserUUID === user.userInfo.userUUID) {
                res.nack({'nack': 'duplicate message'});
                return;
            }
            let messageLoc = localChatMessageInsert(message);
            messagesInHandler.invoke({messageLocKey: messageLoc.key},message);
            IDBChatMessageInsert(message);

        }, {ack: 'client'});
        if (element.groupRole === 'host'){
            stompClient.subscribe('/subscribe/user/' + element.groupUUID + '/group/host/chat',(res)=>{
                let messageRes = JSON.parse(res.body);
                let message = {
                    ...messageRes,
                    roomType: 'group',
                    toUserUUID: user.userInfo.userUUID
                };
                let messageLoc = localChatMessageInsert(message);
                messagesInHandler.invoke({messageLocKey: messageLoc.key},message);
                IDBChatMessageInsert(message);
            })
        }

    });


};


const messageAckSubscribe = () => {
    stompClient.subscribe("/subscribe/user/" + user.userInfo.userUUID + "/message/ack", (res) => {
        let ackRes = JSON.parse(res.body);
        let messageId = ackRes.messageId;
        let messageTime = ackRes.messageTime;
        let messageLocKey = res.headers.messageLocKey;
        let messageLocIndex = res.headers.messageLocIndex;
        // let viewMessageIndex = res.headers.viewMessageIndex;
        localChatMessageUpdate(messageLocKey, messageLocIndex, {messageId:messageId, messageTime:messageTime});
        messagesAckInHandler.invoke({messageLocKey:messageLocKey},localMessages[messageLocKey].messages[messageLocIndex])
        IDBChatMessageInsert({
            ...localMessages[messageLocKey].messages[messageLocIndex]
        });
    });
};

const addContactConfirmSubscribe = ()=>{
    stompClient.subscribe("/subscribe/user/" + user.userInfo.userUUID + "/contact/add/confirm",(res)=>{
        let ackRes = JSON.parse(res.body);
        let message = {messageId:ackRes.messageId,messageStatus:"accept"};
        let messageLocKey = 'add_contact';
        let messageLocIndex = _.findIndex(localMessages.addContact.messages,(ele)=>{
            return ele.messageId === message.messageId;
        });
        if (messageLocIndex === -1) return;
        localChatMessageUpdate(messageLocKey,messageLocIndex,message);
        addContactMessageConfirmInHandler(message);
        IDBChatMessageUpdate(message.messageId,message);
    })
};


const messagesInHandler = new MessageHandler();
const messagesAckInHandler = new MessageHandler();

export {
    stompClient,
    privateSubscribe,
    messageAckSubscribe,
    groupSubscribe,
    addContactConfirmSubscribe,
    setAddContactMessageConfirmInHandler,
    messagesInHandler,
    messagesAckInHandler,
    addContactMessageConfirmInHandler

}