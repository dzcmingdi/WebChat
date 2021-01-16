import React, {useState} from 'react';
import {makeStyles, withStyles} from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Paper from '@material-ui/core/Paper';
import FolderIcon from '@material-ui/icons/Folder';
import ImageIcon from '@material-ui/icons/Image';
import AddIcon from '@material-ui/icons/Add';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import {
    CardHeader,
    TextField,
    InputBase,
    Grid,
    Avatar,
    IconButton,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Button,
    List,
    ListItem,
    ListItemAvatar, ListItemText, ListItemSecondaryAction
} from '@material-ui/core';
import {stompClient} from '../chat/chat';
import {
    localChatMessageInsert,
    user,

} from '../storage/storage';
import $ from 'jquery';
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import {MessageHandler} from "../utils";


const styles = {
    card: {
        minWidth: 275,
    },
    bullet: {
        display: 'inline-block',
        margin: '0 2px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
    },
    pos: {
        marginBottom: 12,
    },
    cardDiv: {
        width: "100%",
        height: "400px",
        borderTop: "1px solid lightgray",
        borderBottom: "1px solid lightgray",
        overflowY: "auto",

    },
    messageMediaInput: {
        display: 'none'
    },
    messageTextInput: {
        borderTop: "1px solid lightgray"
    },

    messagePaperA: { // other side
        marginTop: '3%',
        maxWidth: "20%",
        minHeight: "50px"


    },
    messagePaperB: {
        marginTop: '3%',
        marginLeft: '70%',
        maxWidth: "20%",
        minHeight: "50px"


    },
    messageDiv: {
        marginTop: '3%',
    },
    messageTime: {
        marginLeft: '40%',
        width: '20%',
        color: 'lightgray'
    },

    messageImage: {
        width: '100%',
        height: '100%'
    },

    friendTypography: {},
    friendInfoShowDiv: {
        margin: '10% auto auto auto',
        width: '90%',
    },
    addFriendInput: {
        width: "50%"
    },
    addFriendButton: {
        width: "10%"
    },
    addFriendList: {
        width: "50%"
    }
};

const useStyles = makeStyles(styles);


const messageOutHandler = new MessageHandler();


function WebChatCard(props) {
    const classes = useStyles();
    // const bull = <span className={classes.bullet}>•</span>;

    const [chatMessage, setChatMessage] = React.useState('');

    const [chatImage, setChatImage] = React.useState({
        dialogStatus: false,
        b64Src: ''
    });
    let messageTimeIndex = 0;
    const handleSendMsgBtnClick = (roomType, id, groupName) => {
        if (chatMessage === undefined || chatMessage === '') return;
        let message = {
            messageId: undefined, // wait ack subscribe to fix messageId
            fromUserUUID: user.userInfo.userUUID,
            roomType: roomType,
            groupUUID: undefined,
            messageTime: undefined,
            message: chatMessage,
            messageType: 'text',
            fromUserName: user.userInfo.userName,
            groupName: groupName
        };

        let sendDes;
        let sendHeaders = {
            fromUserUUID: user.userInfo.userUUID,
            fromUserName: user.userInfo.userName,
            groupName: groupName,
            messageType: 'text',
        };

        switch (roomType) {
            case "private":
                message.toUserUUID = id;
                sendDes = '/webchat/user/' + id + '/chat';
                break;
            case "group":
                message.groupUUID = id;
                sendDes = '/webchat/group/' + id + '/chat';
                break;

            default:
                break;
        }
        let messageLoc = localChatMessageInsert(message);
        messageOutHandler.invoke({messageLocKey: messageLoc.key}, message);
        // sendMessageHandler(message);
        sendHeaders.messageLocKey = messageLoc.key;
        sendHeaders.messageLocIndex = messageLoc.index;
        // sendHeaders.viewMessageIndex = viewMessageIndex;
        stompClient.send(sendDes, sendHeaders, chatMessage);

// clear textfield
        $('#chatMessageInput').val("");
    };


    let handleSendMsgChange = (e) => {
        setChatMessage(e.target.value);
    };

    const handleInputFileChange = (e) => {
        let file = (e.target.files)[0];
        if (file.size > 52428800) {
            alert("单个文件大小不能超过50M");
            return;
        }
        let type = (file.type).match(/^image\/(.*)$/)[1];
        let reader = new FileReader();
        reader.onload = (data) => {
            if (data === undefined) return;
            let b64Src = (data.target.result).match(/^data:image\/jpeg;base64,(.*)/)[1];
            setChatImage({
                dialogStatus: true,
                b64Src: b64Src,
                type: type,
                file: file
            })

        };
        reader.readAsDataURL(file);
    };


    // 2020-5-3 8:15
    async function uploadImageFile(args) {
        let formData = new FormData();
        formData.append("multipartFile", chatImage.file);
        formData.append("fileType", "image");
        formData.append("roomType", args.roomType);
        switch (args.roomType) {
            case "private":
                formData.append("fromUserUUID", user.userInfo.userUUID);
                formData.append("toUserUUID", args.toUserUUID);
                break;
            case "group":
                formData.append("groupUUID", args.groupUUID);
                break;
            default:
                break;
        }


        let src = undefined;

        await $.ajax({
            url: '/api/v1/upload/file',
            type: 'post',
            data: formData,
            processData: false,
            contentType: false,
            dataType: 'json',
            success: (res) => {
                if (res.message === 'success') {
                    src = res.src;
                } else {

                }
            }
        });

        return src;
    }

    const handleDialogAction = (args) => {
        if (args.option === 'close') {
            setChatImage({
                dialogStatus: false,
                b64Src: chatImage.b64Src,
                type: chatImage.type
            })
        }


        if (args.option === 'commit') {
            let message = {
                messageId: undefined, // wait ack subscribe to fix messageId and src
                src: undefined,
                fromUserUUID: user.userInfo.userUUID,
                roomType: args.roomType,
                messageTime: undefined,
                messageType: 'byte',
                fromUserName: user.userInfo.userName,
            };
            let sendDes;
            let sendHeaders = {
                fromUserUUID: user.userInfo.userUUID,
                fromUserName: user.userInfo.userName,
                messageType: 'byte',
                groupName: undefined
            };
            switch (args.roomType) {
                case "group":
                    message.groupUUID = args.id;
                    message.groupName = args.name;
                    sendHeaders.groupName = args.name;
                    sendDes = '/webchat/group/' + args.id + '/chat';
                    break;
                case "private":
                    message.toUserUUID = args.id;
                    sendDes = '/webchat/user/' + args.id + '/chat';
                    break;

                default:
                    break;
            }
            uploadImageFile({
                fromUserUUID: user.userInfo.userUUID,
                toUserUUID: args.id,
                groupUUID: args.id,
                roomType: args.roomType
            }).then((src) => {
                if (src === undefined) return;
                message.src = src;

                let messageLoc = localChatMessageInsert(message);
                messageOutHandler.invoke({messageLocKey: messageLoc.key}, message);
                sendHeaders.messageLocKey = messageLoc.key;
                sendHeaders.messageLocIndex = messageLoc.index;
                // sendHeaders.viewMessageIndex = viewMessageIndex;
                setChatImage({
                    dialogStatus: false,
                    b64Src: chatImage.b64Src,
                    type: chatImage.type
                });
                stompClient.send(sendDes, sendHeaders, JSON.stringify({'message':'','src': src}));
                $('#message-media-input').val(undefined);
            }).catch((reason)=>{

            });


        }
    };


    return (
        <Card className={classes.card} key={props.id}>
            <Dialog
                open={chatImage.dialogStatus}
                onClose={handleDialogAction.bind(this, {option: 'close'})}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{"确认发送此图片？"}</DialogTitle>
                <DialogContent>
                    <img src={"data:image/" + chatImage.type + ";base64," + chatImage.b64Src} alt={""}/>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDialogAction.bind(this, {option: 'close'})} color="primary">
                        关闭
                    </Button>
                    <Button onClick={handleDialogAction.bind(this, {
                        option: 'commit',
                        roomType: props.roomType,
                        id: props.id,
                        name: props.name
                    })} color="primary" autoFocus>
                        提交
                    </Button>
                </DialogActions>
            </Dialog>

            <CardHeader title={props.name}>
            </CardHeader>

            <CardContent>

                <div className={classes.cardDiv}>
                    {props.chatMessages.map((e, index) => {
                        let messageTimeShow = ((new Date(e.messageTime).getTime() - new Date(props.chatMessages[messageTimeIndex].messageTime).getTime()) / 1000) > 120;
                        if (messageTimeShow) messageTimeIndex = index;
                        return (

                            <div key={index} className={classes.messageDiv}>

                                {
                                    messageTimeShow
                                    &&
                                    <Typography variant={'button'} className={classes.messageTime}>
                                        {new Date(e.messageTime).format("yyyy-MM-dd HH:mm:ss")}
                                    </Typography>
                                }

                                {
                                    e.messageType === 'text' &&
                                    <Paper elevation={3}
                                           className={e.fromUserUUID === user.userInfo.userUUID ? classes.messagePaperB : classes.messagePaperA}
                                           key={index}>
                                        <Typography variant='body1'>
                                            {e.message}
                                        </Typography>

                                    </Paper>
                                }
                                {
                                    e.messageType === 'byte' &&
                                    <Paper elevation={3}
                                           className={e.fromUserUUID === user.userInfo.userUUID ? classes.messagePaperB : classes.messagePaperA}
                                           key={index}>
                                        {

                                                <img src={e.src}
                                                     className={classes.messageImage} alt={""}/>

                                        }
                                    </Paper>
                                }
                            </div>
                        )

                    })}

                    <br/>
                    <br/>
                </div>

            </CardContent>
            <CardActions>
                <Grid container>

                    <Grid container item justify='flex-end'>
                        <label htmlFor="message-media-input">
                            <IconButton color="primary" aria-label="upload picture" component='span'>
                                <FolderIcon/>
                            </IconButton>

                        </label>
                        <input accept="image/*" onChange={handleInputFileChange} className={classes.messageMediaInput}
                               id='message-media-input' type='file'/>
                    </Grid>

                    <Grid item xs={12}>
                        <InputBase autoFocus={true} fullWidth multiline rows={4} id='chatMessageInput'
                                   placeholder={"在这里输入聊天消息"}
                                   className={classes.messageTextInput}
                                   onChange={handleSendMsgChange}
                                   onKeyDown={(e) => {
                                       if (props.id === undefined) return;
                                       if (e.keyCode === 17) {
                                           handleSendMsgBtnClick(props.roomType, props.id, props.name);
                                           e.preventDefault();
                                       }
                                   }}
                        />

                    </Grid>
                    <Grid container item justify='flex-end'>
                        <Grid item xs={2}>
                            <Button fullWidth color='primary' variant='contained' disabled={props.id === undefined}
                                    onClick={handleSendMsgBtnClick.bind(this, props.roomType, props.id, props.name)}>发送(Ctrl)</Button>
                        </Grid>
                    </Grid>
                </Grid>
            </CardActions>
        </Card>
    );
}


function FriendCard(props) {
    const classes = useStyles();
    // const bull = <span className={classes.bullet}>•</span>;

    const handleCreateWebChatClick = () => {
        // only id,name . no userId, groupId , userName,groupName
        props.handleCreateNewWebChatCard({
            name: props.name,
            id: props.id,
            roomType: props.roomType,
            newMessageNum: 0
        })
    };
    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClose = () => {
        setAnchorEl(null);
    };
    const handleDeleteContact = () => {
        $.ajax({
            url: '/user/contact/delete',
            type: 'post',
            data: JSON.stringify({
                userUUID: props.roomType === 'private' ? props.id : undefined,
                targetType: props.roomType,
                groupUUID: props.roomType === 'group' ? props.id : undefined
            }),
            dataType: 'json',
            success: (res) => {
                if (res) {
                    props.handleDeleteContact({id: props.id, roomType: props.roomType});
                } else {
                    alert("删除失败");
                }
            }
        })
    };

    return (
        <Card className={classes.card} key={props.id}>

            <CardHeader title={props.name} action={
                <div>
                    <IconButton onClick={(e) => {
                        setAnchorEl(e.target);
                    }} aria-controls={"settings"}>
                        <MoreVertIcon/>
                    </IconButton>
                    <Menu id="settings" anchorEl={anchorEl}
                          keepMounted
                          open={Boolean(anchorEl)}
                          onClose={handleClose}>
                        <MenuItem
                            onClick={handleDeleteContact}>{props.roomType === 'private' ? "删除好友" : "退出群组"}</MenuItem>

                    </Menu>
                </div>
            }>

            </CardHeader>

            <CardContent>
                <div className={classes.cardDiv}>
                    <div className={classes.friendInfoShowDiv}>

                        <Grid container direction={'column'}
                              alignItems={'center'} justify={'center'} spacing={2}>
                            <Grid item xs={3}>
                                <Avatar>{props.name[0]}</Avatar>
                            </Grid>
                            <Grid item>
                                <Grid container spacing={2}>
                                    <Grid item>
                                        <Typography variant={"h5"}>
                                            {
                                                props.roomType === 'private' ? '好友' : '群组'
                                            }
                                        </Typography>

                                    </Grid>
                                    <Grid item>
                                        <Typography variant="h5" className={classes.friendTypography}>
                                            {
                                                props.name
                                            }
                                        </Typography>
                                    </Grid>

                                </Grid>


                            </Grid>
                            <Grid item>
                                <Grid container spacing={2}>
                                    <Grid item>
                                        <Typography variant={"inherit"}>
                                            {
                                                props.roomType === 'private' ? '好友代码' : '群组代码'
                                            }
                                        </Typography>

                                    </Grid>
                                    <Grid item>
                                        <Typography variant={"inherit"} className={classes.friendTypography}>
                                            {
                                                props.targetNumber
                                            }
                                        </Typography>
                                    </Grid>

                                </Grid>
                            </Grid>
                        </Grid>
                        {/*<Typography variant={"button"}>*/}
                        {/*    {props.roomType === 'private' ? '好友' : '群组'}*/}
                        {/*</Typography>*/}


                    </div>


                </div>
            </CardContent>
            <CardActions>
                <Button variant='contained' color='primary' onClick={handleCreateWebChatClick}>发消息</Button>
            </CardActions>


        </Card>
    );

}


@withStyles(styles)
class AddFriendCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            addFriendInputValue: "",
            contactQuery: {
                user: {},
                group: {}
            }
        }
    }

    handleSendAddContactMessage = (message) => {
        message.messageType = 'add_contact';
        message.fromUserUUID = user.userInfo.userUUID;
        message.fromUserName = user.userInfo.userName;
        message.messageStatus = "wait";
        let sendHeaders = {
            ...message,
        };
        let sendDes;


        switch (message.roomType) {
            case "private":
                sendDes = "/webchat/user/" + message.toUserUUID + "/chat";
                message.message = "请求添加好友";
                break;
            case "group":
                sendDes = "/webchat/group/" + message.groupUUID + "/chat";
                message.message = "请求加入群组";
                break;
            default:
                break;
        }
        let messageLoc = localChatMessageInsert(message);
        messageOutHandler.invoke({}, message);
        sendHeaders.messageLocKey = messageLoc.key;
        sendHeaders.messageLocIndex = messageLoc.index;
        stompClient.send(sendDes, sendHeaders, message.message);

    };
    handleContactQueryBtnClick = () => {
        $.ajax({
            url: '/user/contacts/get',
            type: 'get',
            data: {
                targetNumber: this.state.addFriendInputValue
            },
            dataType: 'json',
            headers: {
                'Content-type': 'application/json;charset=utf-8'
            },
            success: (res) => {
                let queryUser = res.user === undefined ? {} : res.user;
                let queryGroup = res.group === undefined ? {} : res.group;
                let userStatus = true, groupStatus = true;
                if (_.findIndex(this.props.contactPrivateList, (ele) => {
                    return ele.userUUID === queryUser.userUUID;
                }) !== -1) {
                    userStatus = false;
                }
                if (_.findIndex(this.props.contactGroupList, (ele) => {
                    return ele.groupUUID === queryGroup.groupUUID
                }) !== -1) {
                    groupStatus = false;
                }

                if (queryUser.userUUID === user.userInfo.userUUID) {
                    userStatus = false;
                }
                this.setState({
                    contactQuery: {
                        user: queryUser,
                        group: queryGroup
                    },
                    contactQueryStatus: {
                        user: userStatus,
                        group: groupStatus
                    }
                })
            }
        })
    };

    render() {
        return (
            <Card className={this.props.classes.card}>
                <CardHeader title={"添加朋友/群组"}/>
                <CardContent>

                    <TextField onChange={(e) => {
                        this.setState({addFriendInputValue: e.target.value})
                    }} label={"请输入朋友/群组 Id"} className={this.props.classes.addFriendInput}/>
                    <Button onClick={this.handleContactQueryBtnClick} variant={"contained"}
                            className={this.props.classes.addFriendButton} color={"primary"}>
                        搜索
                    </Button>
                </CardContent>
                <List className={this.props.classes.addFriendList}>
                    {
                        this.state.contactQuery.user.userUUID !== undefined &&
                        <ListItem>
                            <ListItemAvatar>
                                <Avatar>
                                    <ImageIcon/>
                                </Avatar>
                            </ListItemAvatar>
                            <ListItemText primary={this.state.contactQuery.user.userName} secondary={"个人"}/>
                            {
                                this.state.contactQueryStatus.user &&
                                <ListItemSecondaryAction>
                                    <IconButton onClick={this.handleSendAddContactMessage.bind(this, {
                                        toUserUUID: this.state.contactQuery.user.userUUID,
                                        roomType: 'private'
                                    })}>
                                        <AddIcon/>
                                    </IconButton>
                                </ListItemSecondaryAction>
                            }
                        </ListItem>
                    }
                    {
                        this.state.contactQuery.group.groupUUID !== undefined &&
                        <ListItem>
                            <ListItemAvatar>
                                <Avatar>
                                    <ImageIcon/>
                                </Avatar>
                            </ListItemAvatar>
                            <ListItemText primary={this.state.contactQuery.group.groupName} secondary={"群组"}/>
                            {
                                this.state.contactQueryStatus.group &&
                                <ListItemSecondaryAction>
                                    <IconButton onClick={this.handleSendAddContactMessage.bind(this, {
                                        groupUUID: this.state.contactQuery.group.groupUUID,
                                        groupName: this.state.contactQuery.group.groupName,
                                        roomType: 'group'
                                    })}>
                                        <AddIcon/>
                                    </IconButton>
                                </ListItemSecondaryAction>
                            }
                        </ListItem>
                    }
                </List>

            </Card>
        )
    }
}

function CreateGroupCard(props) {
    const classes = makeStyles(styles);

    const [state, setState] = React.useState({
        groupNameInput: '',
        groupMaxInput: ''
    });

    const handleChange = name => (event, value) => {
        setState({...state, [name]: event.target.value});
    }
    const handleCreateGroup = () => {
        $.ajax({
            url: '/api/v1/group/add',
            type: 'POST',
            dataType: 'json',
            data: {
                groupName: state.groupNameInput,
                groupMax: parseInt(state.groupMaxInput),
                groupHost: user.userInfo.userUUID
            },
            success: (res) => {
                if (res.message === 'ok') {
                    props.handleCreateGroup({
                        groupUUID: res.groupUUID,
                        groupTargetNumber: res.groupTargetNumber,
                        groupName: state.groupNameInput,
                    });
                }
            }
        })
    }

    return (
        <Card>
            <CardHeader title={"创建群组"} className={classes.card}/>
            <CardContent>

                <Grid container direction={'column'} spacing={4} alignItems={'center'}>
                    <Grid item>
                        <TextField
                            label={"群组名"} onChange={handleChange('groupNameInput')}/>

                    </Grid>
                    <Grid item>
                        <TextField label={"群组最大成员数"} onChange={handleChange('groupMaxInput')}/>

                    </Grid>
                </Grid>


                <Button variant={"contained"}
                        color={"primary"} onClick={handleCreateGroup}>
                    创建
                </Button>
            </CardContent>


        </Card>
    )
}

export {WebChatCard, FriendCard, AddFriendCard, CreateGroupCard, messageOutHandler}