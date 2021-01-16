import {setUploadToServer} from '../event/pageEvent';
import $ from 'jquery';
import localForage from "localforage";
import Cookies from 'js-cookie';
import _ from 'loadsh';

var messageLocalStorage = window.localStorage;
var localMessages;
var localMessageList;
// var userUUID;
var user;
// var userName;
var IDBMessagesId;
var IDBMessageList;
var IDBMessages;
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "H+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};
setUploadToServer(() => {
    let serverUploadMessageList = [];
    if (localMessageList === undefined) return;
    _.map(localMessageList['messageList'], (element, key) => {
        // if (element.changeStatus) {
        // element.changeStatus = false;
        serverUploadMessageList.push({
            messageItemId: key,
            ...element
        });
        // }

    });


    navigator.sendBeacon("/api/v1/user/chat/message/upload", JSON.stringify({
        userUUID: user.userInfo.userUUID, messageList: serverUploadMessageList
    }));

    // $.ajax({
    //     url: '/user/chat/message/upload',
    //     type: 'post',
    //     data: JSON.stringify({
    //         'messageUser': {userUUID:user.userInfo.userUUID, messageList: serverUploadMessageList},
    //     }),
    //     dataType: 'json',
    //     headers: {
    //         'Content-Type': 'application/json;charset=utf-8'
    //     }
    // })
});

async function contextEnvInit(clearStorage) {
    if (!localForage.supports(localForage.INDEXEDDB)) {
        alert("This browser don't support indexedDB");
        return;
    }
    localMessages = {'addContact': {'messages': []}};
    localMessageList = {
        'messageList': {}
    };
    IDBMessagesId = localForage.createInstance({
        driver: localForage.INDEXEDDB,
        name: "webchat2020",
        storeName: 'messagesid'
    });
    IDBMessages = localForage.createInstance({
        driver: localForage.INDEXEDDB,
        name: 'webchat2020',
        storeName: 'messages'
    });
    IDBMessageList = localForage.createInstance({
        driver: localForage.INDEXEDDB,
        name: 'webchat2020',
        storeName: 'messagelist'
    });
    if (clearStorage) {
        await IDBMessagesId.clear();
        await IDBMessageList.clear();
        await IDBMessages.clear();
    }
    await IDBMessagesId.iterate((value, key, number) => {
        if (key === 'addContact') {
            localMessages[key] = {messages: []}
        } else {
            localMessages[key] = {
                roomType: value.roomType,
                messages: []
            };
        }
        let messagesId = value.messagesId;
        let i = 0;
        for (; i < messagesId.length; i++) {
            let messageId = messagesId[i];
            IDBMessages.getItem(messageId).then((message) => {
                localMessages[key].messages.push({
                    messageId: messageId,
                    ...message
                })
            })
        }


    });

    await IDBMessageList.iterate((value, key, number) => {
        localMessageList['messageList'][key] = {
            ...value
        };
    });
    user = JSON.parse(messageLocalStorage.getItem('user'));
    // userUUID = user.userInfo.userUUID;
    // userName = user['userName'];
}

async function localChatMessageIsNeedUpdate() {
    let length0 = await IDBMessages.length();
    return length0 === 0 ? 1 : 0;
}

const localChatMessageInsert = (message) => { // [fromId,toId,groupId,roomType,messageTime,message]

    let key, index;
    if (message.messageType === 'add_contact') {
        if (localMessages['addContact'] === undefined) {
            localMessages['addContact'] = {'messages': []};
        }
        localMessages.addContact.messages.push(message);
        key = 'addContact';
        index = localMessages[key]['messages'].length - 1;
    } else {
        let keyId = message['groupUUID'] === undefined ? (message['fromUserUUID'] === user.userInfo.userUUID ? message['toUserUUID'] : message['fromUserUUID']) : message['groupUUID'];


        if (localMessages[keyId] === undefined) {
            localMessages[keyId] = {'roomType': message.roomType, 'messages': []};
        }
        // message.messageId = (parseInt(chatMessagePrivate[message.fromId]['messages'].length) + 1).toString();
        localMessages[keyId]['messages'].push(message);
        key = keyId;
        index = localMessages[keyId]['messages'].length - 1;


    }
    return {key: key, index: index};


    // insert message into server message array


};
const localChatMessageDelete = (message) => {
    if (message.messageType === 'add_contact') {
        _.remove(localMessages.addContact.messages, (ele) => {
            return ele.messageId === message.messageId;
        });
    } else if (message.messageType === 'byte' || message.messageType === 'text') {
        let keyId = message['groupUUID'] === undefined ? (message['fromUserUUID'] === user.userInfo.userUUID ? message['toUserUUID'] : message['fromUserUUID']) : message['groupUUID'];
        _.remove(localMessages[keyId].messages, (ele) => {
            return ele.messageId === message.messageId;
        });
    }
};
const localChatMessageUpdate = (messageLocKey, messageLocIndex, message) => {
    let oldMessage = localMessages[messageLocKey].messages[messageLocIndex];
    localMessages[messageLocKey].messages[messageLocIndex] = {...oldMessage, ...message};
}
const updateMessageList = (messageItem) => {
    if (messageItem === undefined || messageItem.id === undefined) return undefined;
    let messageItemId = messageItem.id;
    delete messageItem.id;
    if (localMessageList.messageList[messageItemId] === undefined) {
        localMessageList.messageList[messageItemId] = {};
    }
    let changed = false;
    _.forEach(messageItem, (value, key) => {
        let oldValue = localMessageList.messageList[messageItemId][key];
        localMessageList.messageList[messageItemId][key] = value;
        if (value !== oldValue) changed = true;
    });
    if (changed) {
        IDBMessageListUpdate({
            id: messageItemId,
            ...localMessageList.messageList[messageItemId]
        });
    }
    messageItem.id = messageItemId;

};
const deleteMessageList = (messageItem) => {
    delete localMessageList.messageList[messageItem.id];
};

async function IDBMessageListUpdate(messageItem) {
    let id = messageItem.id;
    delete messageItem.id;
    await IDBMessageList.setItem(id, messageItem);
    messageItem.id = id;
}

async function IDBMessageListDelete(messageItem) {
    await IDBMessageList.removeItem(messageItem.id);
}

async function IDBChatMessageInsert(message) {
    if (message === undefined || message.messageId === undefined) return;
    let messageId = message.messageId;
    let key;
    delete message.messageId;
    await IDBMessages.setItem(messageId, {...message});
    let messagesId;
    if (message.messageType === 'add_contact') {
        key = 'addContact';
        messagesId = await IDBMessagesId.getItem(key);
        if (messagesId === null) {
            messagesId = {
                messagesId: []
            }
        }
        messagesId.messagesId.push(messageId);

    } else {
        key = message.groupUUID === undefined ? (message.fromUserUUID === user.userInfo.userUUID ? message.toUserUUID : message.fromUserUUID) : message.groupUUID;
        messagesId = await IDBMessagesId.getItem(key);
        if (messagesId === null) {
            messagesId = {
                roomType: message.roomType,
                messagesId: []
            };
        }
        messagesId.messagesId.push(messageId);
    }
    await IDBMessagesId.setItem(key, messagesId);
    message.messageId = messageId;
}

async function IDBChatMessageDelete(message) {
    await IDBMessages.removeItem(message.messageId);
    if (message.messageType === 'add_contact') {
        let addContact = await IDBMessagesId.getItem('addContact');
        _.remove(addContact.messagesId, (ele) => {
            return ele === message.messageId;
        });
        await IDBMessagesId.setItem('addContact', addContact);
    } else if (message.messageType === 'byte' || message.messageType === 'text') {
        let keyId = message['groupUUID'] === undefined ? (message['fromUserUUID'] === user.userInfo.userUUID ? message['toUserUUID'] : message['fromUserUUID']) : message['groupUUID'];
        let messages = await IDBMessagesId.getItem(keyId);
        _.remove(messages.messagesId, (ele) => {
            return ele === message.messageId;
        });
        await IDBMessagesId.setItem(keyId, messages);
    }
}

async function IDBChatMessageUpdate(messageId, message) {
    let oldMessage = await IDBMessages.getItem(messageId);
    //
    // newMessage.messageStatus = message.messageStatus;
    await IDBMessages.setItem(messageId)
    await IDBMessages.setItem(messageId, {...oldMessage, ...message});
}

// const localAddContactMessageUpdate = (message)=>{
//
//     localMessages.addContact.messages[index].messageStatus = message.messageStatus;
//
// };
// const localChatMessageUpdate = (...messageUpdate) => {
//     localMessages[messageUpdate[0]]['messages'][messageUpdate[1]].messageId = messageUpdate[2];
//     localMessages[messageUpdate[0]]['messages'][messageUpdate[1]].messageTime = messageUpdate[3];
//     localMessages[messageUpdate[0]]['messages'][messageUpdate[1]].src = messageUpdate[4];
//     delete localMessages[messageUpdate[0]]['messages'][messageUpdate[1]].b64Src;
//     delete localMessages[messageUpdate[0]]['messages'][messageUpdate[1]].b64Type;
//
// };


const getMessageListNewMessageNum = (id) => {
    return localMessageList.messageList[id] === undefined ? 0 : localMessageList.messageList[id].newMessageNum;
};
const loadUnAcceptedMessageFromServer = (successCall, errorCall) => {
    $.ajax({
        url: '/user/chat/message/load_unaccepted',
        data: {
            userUUID: user.userInfo.userUUID
        },
        type: 'GET',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
        },
        dataType: 'json',
        success: (res) => {
            // fixme : how to solve unaccepted message perfectly?
            if (res.messageList !== undefined) {
                res.messageList.forEach(element => {
                    let messageItemId = element.messageItemId;
                    delete element.messageItemId;
                    element.id = messageItemId;
                    updateMessageList(element);
                })
            }
            if (res.messages !== undefined) {
                res.messages.forEach(element => {
                    localChatMessageInsert(element);
                    IDBChatMessageInsert(element);
                });
                successCall();
            }
        }
    })
};

const updateSettingToLocalStorage = (newSetting) => {
    /*
    args: [settingMessageUpload,settingNotifyMessageCount]
     */
    user.userSetting = {
        ...user.userSetting,
        ...newSetting
    }
    localStorage.setItem('user', JSON.stringify(user));
}

const updateUserToLocalStorage = (newUser) => {
    user.userInfo = {
        ...user.userInfo,
        ...newUser.userInfo
    }
    user.userSetting = {
        ...user.userSetting,
        ...newUser.userSetting
    }
    localStorage.setItem('user', JSON.stringify(user));

}

const compareSettingInTwoStorage = () => {
    const oldUser = JSON.parse(localStorage.getItem('user'));
    if (user.userSetting.settingMessageUpload !== oldUser.userSetting.settingMessageUpload) return true;
    if (user.userSetting.settingNotifyMessageCount !== oldUser.userSetting.settingNotifyMessageCount) return true;
    return false;
}

export {
    localChatMessageInsert,
    localChatMessageIsNeedUpdate,
    localMessages,
    localMessageList,
    user,
    loadUnAcceptedMessageFromServer,
    contextEnvInit,
    localChatMessageUpdate,
    // localAddContactMessageUpdate,
    IDBChatMessageInsert,
    IDBMessageListUpdate,
    IDBChatMessageUpdate,
    updateMessageList,
    getMessageListNewMessageNum,
    deleteMessageList,
    IDBMessageListDelete,
    localChatMessageDelete,
    IDBChatMessageDelete,
    compareSettingInTwoStorage,
    updateSettingToLocalStorage,
    updateUserToLocalStorage
}