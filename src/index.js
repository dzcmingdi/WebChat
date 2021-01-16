import React from 'react';
import ReactDOM from 'react-dom';
import {withStyles} from '@material-ui/styles';
import {WebChatCard, FriendCard, AddFriendCard, messageOutHandler, CreateGroupCard} from './templates/card';
import {SimpleContactMessage, SimpleMessageItem} from "./templates/simple-list-item";
import Grid from '@material-ui/core/Grid';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import ImageIcon from '@material-ui/icons/Image';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import TabPanel from './templates/tabpanel';
import Avatar from '@material-ui/core/Avatar';
import Divider from '@material-ui/core/Divider';
import _ from 'loadsh';
import Setting from './templates/setting';
import $ from 'jquery';
import AddIcon from '@material-ui/icons/Add';
import Button from "@material-ui/core/Button";
import {
    stompClient,
    groupSubscribe,
    privateSubscribe,
    messageAckSubscribe,
    addContactConfirmSubscribe,
    messagesInHandler,
    messagesAckInHandler,
    setAddContactMessageConfirmInHandler,
} from './chat/chat';
import {
    localMessages,
    localMessageList,
    user,
    loadUnAcceptedMessageFromServer,
    contextEnvInit,
    updateMessageList,
    getMessageListNewMessageNum,
    localChatMessageDelete,
    IDBChatMessageDelete,
    deleteMessageList,
    IDBMessageListDelete,
    localChatMessageUpdate, IDBChatMessageUpdate,
} from './storage/storage';


const styles = {
    bar: {
        width: '100%',
    },
    list: {
        width: '100%',

    }
};

@withStyles(styles)
class Index extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: 0,
            contactPrivateList: [],
            contactGroupList: [],
            cardShowStatus: {
                webChatCardShowStatus: true,
                friendCardShowStatus: false,
                addFriendCardShowStatus: false,
                createGroupCardShowStatus:false
            },

            messageList: {},
            currentChat: {
                // userId: "",
                // name: "",

            },
            chatMessages: [],
            addContactMessages: [],
            currentFriend: {
                // userId: "",
                // name: "",
                // targetNumber:"",
            },

        };
        messagesInHandler.addHandler.bind(this);
        messagesInHandler.addFilter.bind(this);
        messageOutHandler.addHandler.bind(this);
        messageOutHandler.addFilter.bind(this);
        messagesAckInHandler.addHandler.bind(this);
        messagesAckInHandler.addFilter.bind(this);
        messagesInHandler.addHandler((args, message) => {

            let id, name;
            if (message === undefined) {
                return;
            }
            if (message.messageType !== 'text' && message.messageType !== 'byte') return;
            switch (message.roomType) {
                case "private":
                    id = message.fromUserUUID;
                    name = message.fromUserName
                    break;
                case "group":
                    id = message.groupUUID;
                    name = message.groupName;
                    break;

                default:
                    break;
            }
            if (id === this.state.currentChat.id) {
                updateMessageList({
                    message: message.message,
                    messageTime: message.messageTime,
                    messageType: message.messageType,
                    name: name,
                    roomType: message.roomType,
                    id: id
                });

                this.setState({
                    chatMessages: localMessages[args.messageLocKey].messages,
                    messageList: localMessageList['messageList']
                });
            }
            if (id !== this.state.currentChat.id || this.state.cardShowStatus.friendCardShowStatus) {
                /*当前面板如果不是消息对应的面板，则调用该方法*/
                this.handleInComingMessageCreateNewWebChatCard({
                    id: id,
                    roomType: message.roomType,
                    message: message.message,
                    messageTime: message.messageTime,
                    name: name,
                    messageType: message.messageType,
                    newMessageNum: getMessageListNewMessageNum(id) + 1
                });
            }
        }).addHandler((args, message) => {
            if (message === undefined || message.messageType !== 'add_contact') return;
            this.setState({
                addContactMessages: localMessages.addContact.messages
            })
        });
        messageOutHandler.addHandler((args, message) => {
            if (message.messageType === 'add_contact') {
                this.setState({
                    addContactMessages: localMessages.addContact.messages
                })
            } else if (message.messageType === 'text' || message.messageType === 'byte') {
                this.setState({
                    chatMessages: localMessages[args.messageLocKey].messages
                });
            }
        })

        messagesAckInHandler.addHandler((args, message) => {
            if (message.messageType === 'add_contact') {
                this.setState({
                    addContactMessages: localMessages.addContact.messages
                })
            } else if (message.messageType === 'text' || message.messageType === 'byte') {
                this.setState({chatMessages: localMessages[args.messageLocKey].messages});
            }
        });


        setAddContactMessageConfirmInHandler((message) => {
            switch (message.roomType) {
                case "private":
                    $.ajax({
                        url: '/user/private/info/load',
                        type: 'get',
                        data: {userUUID: message.fromUserUUID},
                        dataType: 'json',
                        success: (res) => {
                            if (res === undefined) return;
                            let contactPrivateList = this.state.contactPrivateList;
                            contactPrivateList.push(res);
                            this.setState({
                                contactPrivateList: contactPrivateList
                            })
                        }
                    });
                    break;

                case "group":
                    $.ajax({
                        url: '/user/group/info/load',
                        type: 'get',
                        data: {groupUUID: message.groupUUID},
                        dataType: 'json',
                        success: (res) => {
                            if (res === undefined) return;
                            let contactGroupList = this.state.contactGroupList;
                            contactGroupList.push(res);
                            this.setState({
                                contactPrivateList: contactGroupList
                            })
                        }
                    });

                    break;


                default:
                    break;
            }
            this.setState({
                addContactMessages: localMessages.addContact.messages
            })
        });


        contextEnvInit().then(() => {
            stompClient.connect({'userUUID': user.userInfo.userUUID}, (frame) => {
                this.getFriendsList();
                this.getGroupList();
                messageAckSubscribe();
                addContactConfirmSubscribe();
            })
        }).then(() => {
            loadUnAcceptedMessageFromServer(() => {
                this.setState({
                    messageList: localMessageList['messageList'],
                    addContactMessages: localMessages.addContact.messages
                })
            }, () => {
            });
        });
    }

    componentDidMount = () => {
        document.oncontextmenu = () => false;
    }
    getGroupList = () => {
        $.ajax({
            url: '/user/group/list',
            type: 'get',
            dataType: 'json',
            success: (res) => {
                if (res !== undefined) {
                    groupSubscribe(res);
                    this.setState({
                        contactGroupList: res
                    })

                }
            },
            error: (res) => {

            }
        })
    };
    getFriendsList = () => {
        $.ajax({
            url: '/user/friend/list',
            type: 'get',
            dataType: 'json',
            success: (res) => {
                if (res !== undefined) {
                    privateSubscribe();
                    this.setState({
                        contactPrivateList: res
                    })
                }
            },
            error: (res) => {
            }
        })
    };
    updateCardShowStatus = (which) => {
        let cardShowStatus = this.state.cardShowStatus;
        _.forEach(cardShowStatus, (value, key) => {
            if (key !== which) {
                cardShowStatus[key] = false;
            } else {
                cardShowStatus[key] = true;
            }

        });
        return cardShowStatus;
    };
    handleMessageListClick = (id, name, roomType) => {
        let chatMessages = [];
        if (localMessages[id] !== undefined) {
            // clone a new Array
            Object.assign(chatMessages, localMessages[id].messages);
        }
        updateMessageList({
            id: id,
            newMessageNum: 0
        });
        this.setState({

            currentChat: {
                id: id,
                name: name,
                roomType: roomType,


            },
            chatMessages: chatMessages,
            cardShowStatus: this.updateCardShowStatus("webChatCardShowStatus"),
            messageList: localMessageList['messageList']
        })
    };
    handleFriendListClick = (id, name,targetNumber, roomType) => {

        this.setState({
            currentFriend: {
                id: id,
                name: name,
                targetNumber:targetNumber,
                roomType: roomType,
            },
            cardShowStatus: this.updateCardShowStatus("friendCardShowStatus")
        })
    };
    a11yProps = (index) => {
        return {
            id: `full-width-tab-${index}`,
            'aria-controls': `full-width-tabpanel-${index}`,

        };
    };
    handleChange = (event, newValue) => {
        this.setState({
            value: newValue
        })

    };

    handleInComingMessageCreateNewWebChatCard = (messageItem) => {
        updateMessageList({...messageItem});
        this.setState({
            messageList: localMessageList['messageList']
        });
    };
    handleCreateNewWebChatCard = (messageItem) => {
        updateMessageList({...messageItem});
        // render, create new web chat card
        let chatMessages = [];
        if (localMessages[messageItem.id] !== undefined) {
            // clone a new Array
            Object.assign(chatMessages, localMessages[messageItem.id]['messages']);
        }
        this.setState({
            value: 0,
            messageList: localMessageList['messageList'],
            chatMessages: chatMessages,
            cardShowStatus: this.updateCardShowStatus("webChatCardShowStatus")
        }, () => {
            $('#' + messageItem.id).click();
        })
    };
    handleAddContactBtnClick = () => {
        this.setState({
            cardShowStatus: this.updateCardShowStatus("addFriendCardShowStatus")
        });
    };
    addContactMessageConfirmOutHandler = (message) => {
        switch (message.roomType) {
            case "private":
                $.ajax({
                    url: '/user/private/info/load',
                    type: 'get',
                    data: {userUUID: message.fromUserUUID},
                    dataType: 'json',
                    success: (res) => {
                        if (res === undefined) return;
                        let contactPrivateList = this.state.contactPrivateList;
                        contactPrivateList.push(res);
                        this.setState({
                            contactPrivateList: contactPrivateList
                        })
                    }
                });
                break;

            case "group":


                break;


            default:
                break;
        }
        this.setState({
            addContactMessages: localMessages.addContact.messages
        })
    }
    // 发送好友确认消息
    addContactMessageConfirmOut = (message) => {
        message.messageStatus = 'accept';
        let sendHeaders = {messageId: message.messageId, roomType: message.roomType};
        switch (message.roomType) {
            case "private":
                sendHeaders.fromUserUUID = user.userInfo.userUUID;
                break;
            case "group":
                sendHeaders.groupUUID = message.groupUUID;
                break;
            default:
                break;
        }
        let sendDes = "/webchat/contact/" + message.fromUserUUID + "/add/confirm";
        let messageLocIndex = _.findIndex(localMessages.addContact.messages, (ele) => {
            return ele.messageId === message.messageId;
        });
        localChatMessageUpdate('add_contact', messageLocIndex, message);
        this.addContactMessageConfirmOutHandler(message);
        IDBChatMessageUpdate(message.messageId, message);
        stompClient.send(sendDes, sendHeaders, "好友确认");
    };
    handleContactConfirm = (message) => {

        $.ajax({
            url: '/user/contact/add',
            type: 'POST',
            data: JSON.stringify({
                userUUID: message.fromUserUUID,
                targetType: message.roomType,
                groupUUID: message.groupUUID
            }),
            dataType: 'json',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            success: (res) => {
                if (res) {
                    this.addContactMessageConfirmOut(message);
                } else {
                    alert("添加失败:重复添加");
                }
            }
        })
    };
    handleDeleteContactMessage = (message) => {
        localChatMessageDelete(message);
        IDBChatMessageDelete(message);
        this.setState({
            addContactMessages: localMessages.addContact.messages
        })
    };
    handleDeleteContact = (contact) => {
        if (contact.roomType === 'private') {
            let contactPrivateList = this.state.contactPrivateList;
            _.remove(contactPrivateList, (e) => {
                return e.userUUID === contact.id;
            });
            this.setState({contactPrivateList: contactPrivateList});
        } else {
            let contactGroupList = this.state.contactGroupList;
            _.remove(contactGroupList, (e) => {
                return e.groupUUID === contact.id;
            });
            this.setState({contactGroupList: contactGroupList});
        }
    };
    handleDeleteMessageItem = (messageItem) => {
        deleteMessageList(messageItem);
        IDBMessageListDelete(messageItem);
        this.setState({
            messageList: localMessageList.messageList,
            currentChat: {},
            chatMessages: []

        })
    };
    handleCardChange = name => (event,value) =>{
        name += 'ShowStatus';
        this.setState({
            cardShowStatus: this.updateCardShowStatus(name)
        })
    }
    handleCreateGroup = (group) =>{
        let groups = this.state.contactGroupList;
        groups.push(group);
        this.setState({
            contactGroupList: groups
        })
    }


    render() {
        return (
            <Grid container spacing={2} style={{marginTop: "1%"}}>
                <Grid item xs={4}>
                    <div className={this.props.classes.bar}>
                        <AppBar position="static" color="default">
                            <Tabs
                                value={this.state.value}
                                onChange={this.handleChange}
                                indicatorColor="primary"
                                textColor="primary"
                                variant="standard"
                                centered={true}
                                aria-label="full width tabs example"
                            >
                                <Tab label="消息栏" {...this.a11yProps.bind(this, 0)} />
                                <Tab label="联系人" {...this.a11yProps.bind(this, 1)} />
                                <Tab label="设置" {...this.a11yProps.bind(this, 2)} />
                            </Tabs>
                        </AppBar>

                        <TabPanel value={this.state.value} index={0}>
                            <div className={this.props.classes.list}>


                                <List component="nav" aria-label="main mailbox folders">
                                    {
                                        _.map(this.state.messageList, (ele, index) =>
                                            (
                                                <div key={index}>


                                                    <SimpleMessageItem messageItem={{...ele, id: index}}
                                                                       onDelete={this.handleDeleteMessageItem}
                                                                       onClick={this.handleMessageListClick}
                                                    />

                                                    <Divider/>

                                                </div>
                                            )
                                        )
                                    }
                                </List>
                            </div>
                        </TabPanel>
                        <TabPanel value={this.state.value} index={1}>
                            <List>
                                <ListItem>
                                    <ListItemText primary='朋友'>

                                    </ListItemText>

                                </ListItem>
                                <Divider/>
                                {
                                    _.map(this.state.contactPrivateList, (ele) => (
                                        <div key={ele.userUUID}>
                                            <ListItem button id={ele.userUUID}
                                                      onClick={this.handleFriendListClick.bind(this, ele.userUUID, ele.userName,ele.userTargetNumber, "private")}>
                                                <ListItemAvatar>
                                                    <Avatar>
                                                        <ImageIcon/>
                                                    </Avatar>
                                                </ListItemAvatar>
                                                <ListItemText primary={ele.userName}/>

                                            </ListItem>
                                            <Divider/>
                                        </div>
                                    ))
                                }
                                <ListItem>
                                    <ListItemText primary='群聊'>

                                    </ListItemText>
                                </ListItem>


                                <Divider/>
                                {
                                    _.map(this.state.contactGroupList, (ele) => (
                                        <div key={ele.groupUUID}>
                                            <ListItem button
                                                      onClick={this.handleFriendListClick.bind(this, ele.groupUUID, ele.groupName,ele.groupTargetNumber, "group")}>
                                                <ListItemAvatar>
                                                    <Avatar>
                                                        <ImageIcon/>
                                                    </Avatar>
                                                </ListItemAvatar>
                                                <ListItemText primary={ele.groupName}/>

                                            </ListItem>
                                            <Divider/>
                                        </div>
                                    ))
                                }
                            </List>
                            <Button onClick={this.handleAddContactBtnClick} variant="contained" color="default"
                                    startIcon={<AddIcon/>}>添加朋友/群组</Button>

                            <Button variant={"contained"} color={"default"} startIcon={<AddIcon />} onClick={this.handleCardChange('createGroupCard')} >创建群组</Button>
                            <List>
                                {
                                    _.map(this.state.addContactMessages, (ele, index) => (
                                        <SimpleContactMessage key={index} message={ele}
                                                              onDelete={this.handleDeleteContactMessage}
                                                              onConfirm={this.handleContactConfirm}/>
                                    ))
                                }
                            </List>
                        </TabPanel>
                        <TabPanel value={this.state.value} index={2}>
                            <Setting/>
                        </TabPanel>


                    </div>
                </Grid>
                <Grid item xs={7}>
                    {
                        this.state.cardShowStatus.webChatCardShowStatus &&
                        <WebChatCard roomType={this.state.currentChat.roomType} name={this.state.currentChat.name}
                                     id={this.state.currentChat.id} chatMessages={this.state.chatMessages}/>
                    }
                    {
                        this.state.cardShowStatus.friendCardShowStatus &&
                        <FriendCard roomType={this.state.currentFriend.roomType} name={this.state.currentFriend.name}
                                    targetNumber={this.state.currentFriend.targetNumber}
                                    id={this.state.currentFriend.id}
                                    handleDeleteContact={this.handleDeleteContact}

                                    handleCreateNewWebChatCard={this.handleCreateNewWebChatCard}/>
                    }
                    {
                        this.state.cardShowStatus.addFriendCardShowStatus &&
                        <AddFriendCard contactPrivateList={this.state.contactPrivateList}
                                       contactGroupList={this.state.contactGroupList}/>
                    }

                    {
                        this.state.cardShowStatus.createGroupCardShowStatus &&
                            <CreateGroupCard handleCreateGroup={this.handleCreateGroup}/>
                    }

                </Grid>

            </Grid>

        )

    }

}


ReactDOM.render(<Index/>, document.getElementById('index'));