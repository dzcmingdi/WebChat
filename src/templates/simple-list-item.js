import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import Avatar from "@material-ui/core/Avatar";
import ImageIcon from "@material-ui/icons/Image";
import ListItemText from "@material-ui/core/ListItemText";
import ListItem from "@material-ui/core/ListItem";
import React from "react";
import {user} from "../storage/storage";
import {ListItemSecondaryAction} from "@material-ui/core";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import {Badge} from "@material-ui/core";
function SimpleContactMessage(props) {
    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleContextMenu = event => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);

    };

    const onConfirm = () =>{
        props.onConfirm(props.message);
    };
    const onDelete = () =>{
        props.onDelete(props.message);
    };


    return (
        <div>
            {
                props.message.fromUserUUID === user.userInfo.userUUID &&
                <ListItem aria-controls={"simple-contact-menu"} onContextMenu={handleContextMenu}>
                    <ListItemAvatar>
                        <Avatar>
                            <ImageIcon/>
                        </Avatar>
                    </ListItemAvatar>
                    <ListItemText primary={props.message.message} secondary={props.message.messageStatus === 'wait' ? '等待回复' : '已同意'}/>

                </ListItem>
            }
            {
                props.message.fromUserUUID !== user.userInfo.userUUID &&
                <ListItem onContextMenu={handleContextMenu}>
                    <ListItemAvatar>
                        <Avatar>
                            <ImageIcon/>
                        </Avatar>
                    </ListItemAvatar>
                    <ListItemText primary={props.message.fromUserName + props.message.message} secondary={props.message.messageStatus === 'wait' ? "" : '已同意'}/>

                    {
                        props.message.messageStatus === 'wait' &&
                        <ListItemSecondaryAction>
                            <Button onClick={onConfirm}>同意</Button>
                        </ListItemSecondaryAction>
                    }
                </ListItem>
            }
            <Menu
                id="simple-contact-menu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleClose}
            >
                <MenuItem onClick={onDelete}>删除</MenuItem>
            </Menu>
        </div>

    )
}

function SimpleMessageItem(props) {

    const onDelete = ()=>{
        props.onDelete(props.messageItem);
    };
    const onClick = ()=>{
        props.onClick(props.messageItem.id,props.messageItem.name,props.messageItem.roomType);
    };
    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleContextMenu = event => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);

    };

    return (
        <div>
        <ListItem id={props.messageItem.id} aria-controls={"simple-message-item-menu"} button onClick={onClick} onContextMenu={handleContextMenu}>
            <ListItemAvatar>
                <Avatar>
                    <ImageIcon/>
                </Avatar>
            </ListItemAvatar>
            <ListItemText primary={props.messageItem.name} secondary={props.messageItem.message}/>
            <ListItemSecondaryAction>
                <Badge badgeContent={props.messageItem.newMessageNum} color={"primary"} />
            </ListItemSecondaryAction>
        </ListItem>
            <Menu
                id="simple-message-item-menu"
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleClose}
            >
                <MenuItem onClick={onDelete}>删除</MenuItem>
            </Menu>
        </div>
    )
}






export {
    SimpleContactMessage,
    SimpleMessageItem
}