import React from 'react';
import ReactDOM from 'react-dom';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import SignUp from '../templates/sign-up';
class Register extends React.Component{
    constructor(props){
        super(props);
    }

    render(){
        return (
            <SignUp />
        )
    }

}
ReactDOM.render(<Register/>,document.getElementById('register'));