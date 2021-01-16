import React from 'react';
import Switch from '@material-ui/core/Switch';
import FormGroup from '@material-ui/core/FormGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import {Divider} from '@material-ui/core';
import $ from 'jquery';
import {
    compareSettingInTwoStorage,
    IDBMessageListUpdate,
    localMessageList, updateMessageList,
    updateSettingToLocalStorage, updateUserToLocalStorage,
    user
} from "../storage/storage";
import Slider from "@material-ui/core/Slider";
import ListItemText from "@material-ui/core/ListItemText";
import ListItemSecondaryAction from "@material-ui/core/ListItemSecondaryAction";
import Button from "@material-ui/core/Button";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import Typography from "@material-ui/core/Typography";
import makeStyles from "@material-ui/core/styles/makeStyles";
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ExpansionPanelActions from "@material-ui/core/ExpansionPanelActions";
import TextField from "@material-ui/core/TextField";


const useStyles = makeStyles((theme) => ({
    root: {
        // width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
        flexBasis: '33.33%',
        flexShrink: 0,
    },
    secondaryHeading: {
        fontSize: theme.typography.pxToRem(15),
        color: theme.palette.text.secondary,
    },
}));
export default function Setting(props) {

    const [state, setState] = React.useState({
        settingMessageUpload: user.userSetting.settingMessageUpload,
        settingNotifyMessageCount: user.userSetting.settingNotifyMessageCount,
        updateButtonStatus: false,
        messageListSyncTime: user.userSetting.messageListSyncTime,
        expanded: '',
        userNameInput:user.userInfo.userName,
        passwordInput:undefined
    });
    const handleChange = name => (event, value) => {

        switch (name) {
            case "settingMessageUpload":
                user.userSetting.settingMessageUpload = value;
                setState({
                    ...state,
                    [name]: value,
                    updateButtonStatus: compareSettingInTwoStorage()
                })
                break;

            case "settingNotifyMessageCount":
                user.userSetting.settingNotifyMessageCount = value;
                setState({
                    ...state,
                    [name]: value,
                    updateButtonStatus: compareSettingInTwoStorage()
                })
                break;

            case (name.match(/.*?Panel/) || {}).input:
                setState({
                    ...state,
                    expanded: value ? name : ''
                })
                break;


            case (name.match(/.*?Input/) || {}).input:
                setState({
                    ...state,
                    [name]:event.target.value
                })

            default:
                break;

        }
    };
    // const handleSliderChange = name => (event, value) => {
    //
    //
    //     user.userSetting.settingNotifyMessageCount = value;
    //     setState({
    //         ...state, [name]: value, updateButtonStatus: compareSettingInTwoStorage()
    //     })
    // }
    const handleUpdateSetting = () => {
        $.ajax({
            url: '/api/v1/user/setting/update',
            data: {
                settingMessageUpload: user.userSetting.settingMessageUpload,
                settingNotifyMessageCount: user.userSetting.settingNotifyMessageCount
            }
            ,
            type: 'POST',
            dataType: 'json',
            success: (res) => {
                if (res === 'ok') {
                    updateSettingToLocalStorage({});
                    setState({...state, updateButtonStatus: false});
                } else {
                    alert("设置更新失败");
                }
            }
        })
    }
    const handleMessageListSync = () => {
        $.ajax({
            url: '/user/message/reload',
            data: {
                loadMessageList: 1,
                loadMessages: 0
            },
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
            },
            dataType: 'json',
            type: 'get',
            success: (res) => {
                if (res.messageList === undefined) return;
                let syncTime = new Date().format("yyyy-MM-dd HH:mm:ss");
                setState({...state, messageListSyncTime: syncTime});
                updateSettingToLocalStorage({messageListSyncTime: syncTime});
                _.forEach(res.messageList, (value, index) => {
                    updateMessageList(value);
                })

            }
        })
    }
    const handleUpdateUserSetting = () => {
        $.ajax({
            url:'/api/v1/user/info/update',
            data:{
                userName:state.userNameInput,
                password:state.passwordInput
            },
            type:'POST',
            dataType:'json',
            success:(res)=>{
                if (res === 'ok'){
                    updateUserToLocalStorage({userInfo:{userName:state.userNameInput}});
                }
            }
        })
    }
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <ExpansionPanel expanded={state.expanded === 'generalPanel'} onChange={handleChange('generalPanel')}>
                <ExpansionPanelSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1bh-content"
                    id="panel1bh-header"
                >
                    <Typography className={classes.heading}>通用设置</Typography>
                    <Typography className={classes.secondaryHeading}>聊天基础设置</Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <List style={{width:'100%'}}>

                        <ListItem>

                            <ListItemText primary="消息列表漫游"/>
                            <ListItemSecondaryAction>
                                <Switch checked={state.settingMessageUpload}
                                        onChange={handleChange('settingMessageUpload')}
                                        color="primary"/>
                            </ListItemSecondaryAction>
                        </ListItem>
                        <ListItem>

                            <ListItemText primary="未读通知消息数量"/>
                            <Slider style={{marginLeft:'10%'}}
                                defaultValue={state.settingNotifyMessageCount}
                                onChange={handleChange('settingNotifyMessageCount')}
                                aria-labelledby="discrete-slider"
                                valueLabelDisplay="auto"

                                step={10}
                                marks
                                min={10}
                                max={110}
                            />


                        </ListItem>
                    </List>
                </ExpansionPanelDetails>
                <ExpansionPanelActions>
                    <Button size="small" disabled={!state.updateButtonStatus} color={"primary"} onClick={handleUpdateSetting}>更新</Button>

                </ExpansionPanelActions>
            </ExpansionPanel>

            <ExpansionPanel expanded={state.expanded === 'advancedPanel'} onChange={handleChange('advancedPanel')}>
                <ExpansionPanelSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel3bh-content"
                    id="panel3bh-header"
                >
                    <Typography className={classes.heading}>高级设置</Typography>
                    <Typography className={classes.secondaryHeading}>
                        Filtering has been entirely disabled for whole web server
                    </Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <Typography>
                        Nunc vitae orci ultricies, auctor nunc in, volutpat nisl. Integer sit amet egestas eros,
                        vitae egestas augue. Duis vel est augue.
                    </Typography>
                </ExpansionPanelDetails>
            </ExpansionPanel>
            <ExpansionPanel expanded={state.expanded === 'userPanel'} onChange={handleChange('userPanel')}>
                <ExpansionPanelSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel4bh-content"
                    id="panel4bh-header"
                >
                    <Typography className={classes.heading}>用户设置</Typography>
                    <Typography className={classes.secondaryHeading} >
                        管理账户与密码
                    </Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                    <List>
                        <ListItem>
                            <ListItemText primary={"用户名"}/>
                            <TextField   defaultValue={state.userNameInput} onChange={handleChange("userNameInput")}/>

                        </ListItem>
                        <ListItem>
                            <ListItemText primary={"密码"}/>
                            {}
                            <TextField type={"password"} disabled={state.passwordInput === undefined} placeholder={'点击此处修改密码'}  onChange={handleChange("passwordInput")} onClick={(e)=>{setState({...state, passwordInput: ''})}}/>

                        </ListItem>
                    </List>
                </ExpansionPanelDetails>
                <ExpansionPanelActions>
                    <Button size="small"  color={"primary"} onClick={handleUpdateUserSetting}>更新</Button>

                </ExpansionPanelActions>
            </ExpansionPanel>
        </div>

    )

}
//
//     <FormGroup>
//
//         // <Divider/>
//         // <br/>
//         // <br/>
//         // <ListItem>
//         // <ListItemText primary="保存你的设置更新"/>
//         //
//         // <ListItemSecondaryAction>
//         // <Button disabled={!state.updateButtonStatus} variant="contained" color="primary"
//         //                         onClick={handleUpdateSetting}>
//         //                     更新设置
//         //                 </Button>
//         //             </ListItemSecondaryAction>
//         //         </ListItem>
//         //         <ListItem>
//         //             <ListItemText primary={"同步时间:" + state.messageListSyncTime}/>
//         //
//         //             <ListItemSecondaryAction>
//         //                 <Button variant={"contained"} color={"primary"} onClick={handleMessageListSync}>同步消息列表</Button>
//         //             </ListItemSecondaryAction>
//         //         </ListItem>
//         //
//         //         <ListItem>
//         //             <ListItemText primary={"你的用户代码:" + user.userInfo.userTargetNumber}/>
//         //
//         //
//         //         </ListItem>
//         //
//         //
//         //         {/*<ListItem>*/}
//         //         {/*    <FormControlLabel*/}
//         //         {/*        control={<Switch checked={state.checkedC} onChange={handleChange('checkedC') }  color="primary"/>}*/}
//         //         {/*        label="Normal" */}
//         //         {/*    />*/}
//         //         {/*</ListItem>*/}
//         //
//         //
//         //     </List>
//         //
//         //
//         // </FormGroup>
//         );
//         }