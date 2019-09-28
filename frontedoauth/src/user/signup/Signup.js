import React, { Component } from 'react';
import './Signup.css';
import { Link, Redirect } from 'react-router-dom'
import {ACCESS_TOKEN, FACEBOOK_AUTH_URL, GOOGLE_AUTH_URL} from '../../constants';
import { signup } from '../../util/APIUtils';
import Alert from 'react-s-alert';
import RegistrationEnd from "../registrationEnd/registrationEnd";
import {FacebookLoginButton, GoogleLoginButton} from "react-social-login-buttons";

class Signup extends Component {
    render() {
        if(this.props.authenticated && this.props.currentLoginPhase === 2) {
            return <Redirect
                to={{
                    pathname: "/",
                    state: { from: this.props.location }
                }}/>;
        }

        if(this.props.authenticated && this.props.currentLoginPhase === 1) {
            return <Redirect
                to={{
                    pathname: "/registrationEnd",
                    state: { from: this.props.location }
                }}/>;
        }

        return (
            <div className="signup-container">
                <div className="signup-content">
                    <h1 className="signup-title">Signup to RetroRent, step 1</h1>
                    <SocialLogin />
                    <div className="or-separator">
                        <span className="or-text">OR</span>
                    </div>
                    <SignupForm {...this.props} />
                    <span className="login-link">Already have an account? <Link to="/login">Login!</Link></span>
                </div>
            </div>
        );
    }
}

class SocialLogin extends Component {
    render() {
        return (
            <div className="social-login">
                <GoogleLoginButton onClick={() =>
                    window.location.replace(GOOGLE_AUTH_URL)
                } />
                <FacebookLoginButton onClick={() =>
                    window.location.replace(FACEBOOK_AUTH_URL)
                } />
            </div>
        );
    }
}

class SignupForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            first_name: '',
			last_name: '',
			user_name: '',
            email: '',
            password: ''
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleInputChange(event) {
        const target = event.target;
        const inputName = target.name;        
        const inputValue = target.value;

        this.setState({
            [inputName] : inputValue
        });        
    }

    handleSubmit(event) {
        event.preventDefault();   

        const signUpRequest = Object.assign({}, this.state);

        signup(signUpRequest)
        .then(response => {
            localStorage.setItem(ACCESS_TOKEN, response.accessToken);
            // Alert.success("You're successfully registered.");
            this.props.history.push("/");
            window.location.reload();

        }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');            
        });
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div className="form-item">
                    <input type="text" name="first_name" 
                        className="form-control" placeholder="First Name"
                        value={this.state.first_name} onChange={this.handleInputChange} required/>
                </div>
				<div className="form-item">
                    <input type="text" name="last_name" 
                        className="form-control" placeholder="Last Name"
                        value={this.state.last_name} onChange={this.handleInputChange} required/>
                </div>
				<div className="form-item">
                    <input type="text" name="user_name" 
                        className="form-control" placeholder="User Name"
                        value={this.state.user_name} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <input type="email" name="email" 
                        className="form-control" placeholder="Email"
                        value={this.state.email} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <input type="password" name="password" 
                        className="form-control" placeholder="Password"
                        value={this.state.password} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <button type="submit" className="btn btn-block btn-primary" >Sign Up</button>
                </div>
            </form>                    

        );
    }
}

export default Signup