import React from 'react';
import ReactDOM from 'react-dom';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import SignIn from '../templates/sign-in';
class Login extends React.Component{
    constructor(props){
        super(props);
        window.onbeforeunload = undefined;
    }

    render(){
        return (
            <SignIn />
        )
    }

}
ReactDOM.render(<Login/>,document.getElementById('login'));