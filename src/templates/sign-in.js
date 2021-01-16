import React from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import {makeStyles} from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import $ from 'jquery';
import Cookies from 'js-cookie';
import {
    localChatMessageIsNeedUpdate,
    contextEnvInit,
    localMessages,
    localChatMessageInsert,
    IDBChatMessageInsert, updateMessageList, IDBMessageListUpdate
} from '../storage/storage';
import {messageAckSubscribe} from "../chat/chat";

function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            <Link color="inherit" href="">
                Web Chat
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const useStyles = makeStyles(theme => ({
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(1),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
}));

export default function SignIn() {
    const classes = useStyles();
    const [reducer, setReducer] = React.useReducer((state, action) => {
        switch (action.type) {
            case 'mail':
                state.mail = action.data;
                return state;

            case 'password':
                state.password = action.data;
                return state;

            case 'rememberMe':
                state.rememberMe = action.data;
                return state;
            default:
                return state
        }
    }, {
        mail: "",
        password: "",
        rememberMe: false
    });

    const [loginBtnDisabled, setLoginBtnDisabled] = React.useState(false);
    const handleLoginBtnClick = (e) => {
        setLoginBtnDisabled(true);
        $.ajax({
            url: '/user/login',
            type: 'post',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
            },
            dataType: 'json',
            data: {
                mail: reducer.mail,
                password: reducer.password,
                remember: reducer.rememberMe
            },
            success: (res) => {
                if (res.CODE === '1') {
                    // if current user is not same as previous user, clear all data;
                    let clearStorage = false;
                    let previousUser = window.localStorage.getItem('user');
                    if (previousUser === null) {
                        window.localStorage.setItem("user", JSON.stringify({
                            userInfo: res.user,
                            userSetting: {
                                ...res.setting,
                                messageListSyncTime: new Date().format("yyyy-MM-dd HH:mm:ss")
                            },
                        }));
                        clearStorage = true;
                    } else {
                        previousUser = JSON.parse(previousUser);
                        if (res.user.userUUID !== previousUser.userInfo.userUUID) {
                            clearStorage = true;
                            window.localStorage.setItem("user", JSON.stringify({
                                userInfo: res.user,
                                userSetting: {
                                    ...res.setting,
                                    messageListSyncTime: new Date().format("yyyy-MM-dd HH:mm:ss")

                                },
                            }));

                        }
                    }
                    contextEnvInit(clearStorage).then(localChatMessageIsNeedUpdate).then((reload) => {
                        reloadMessage(reload)
                    });
                } else {
                    setLoginBtnDisabled(false);
                    alert("Error");
                }
            }
        })
    };
    const reloadMessage = (reloadIsNeed) => {
        $.ajax({
            url: '/user/message/reload',
            data: {
                loadMessages: reloadIsNeed,
                loadMessageList: 1,
            },
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
            },
            dataType: 'json',
            type: 'get',
            success: (res) => {
                async function reloadMessageList() {
                    if (res.messageList === undefined) return;
                    let i = 0;
                    for (; i < res.messageList.length; i++) {
                        let messageItem = res.messageList[i];
                        let messageItemId = messageItem.messageItemId;
                        delete messageItem.messageItemId;
                        await IDBMessageListUpdate({
                            id: messageItemId,
                            ...messageItem
                        });
                    }
                }

                async function reloadMessages() {
                    if (res.messages === undefined) return;
                    let i = 0;
                    for (; i < res.messages.length; i++) {
                        localChatMessageInsert(res.messages[i]);
                        await IDBChatMessageInsert(res.messages[i]);
                    }
                }

                reloadMessageList().then(reloadMessages).then(() => {
                    window.location.href = '/index';
                });
            },
            error: () => {
                window.location.href = '/index';
            },

        })
    };

    const handleMailInputChange = (e) => {
        setReducer({type: 'mail', data: e.target.value});
    };

    const handlePasswordInputChange = (e) => {
        setReducer({type: 'password', data: e.target.value});
    };

    const handleRememberMeInputChange = (e) => {
        setReducer({type: 'rememberMe', data: e.target.value});
    };

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline/>
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon/>
                </Avatar>
                <Typography component="h1" variant="h5">
                    登录
                </Typography>
                <form className={classes.form} noValidate>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="mail"
                        label="邮件地址"
                        onChange={handleMailInputChange}
                        autoFocus
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        label="密码"
                        onChange={handlePasswordInputChange}
                        type="password"
                        id="password"
                        autoComplete="current-password"
                    />
                    <FormControlLabel
                        control={<input type='checkbox' onChange={handleRememberMeInputChange}/>}
                        label="记住我"
                    />
                    <Button
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                        onClick={handleLoginBtnClick}
                        disabled={loginBtnDisabled}
                    >
                        登录
                    </Button>
                    <Grid container>
                        <Grid item xs>
                            <Link href="#" variant="body2">
                                忘记密码?
                            </Link>
                        </Grid>
                        <Grid item>
                            <Link href="/user/register" variant="body2">
                                {"没有账户？开始注册"}
                            </Link>
                        </Grid>
                    </Grid>
                </form>
            </div>
            <Box mt={8}>
                <Copyright/>
            </Box>
        </Container>
    );
}