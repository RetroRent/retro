import React, { Component } from 'react';
import {
  Route,
  Switch
} from 'react-router-dom';
import AppHeader from '../common/AppHeader';
import { Link, Redirect } from 'react-router-dom';
import Home from '../home/Home';
import Login from '../user/login/Login';
import Signup from '../user/signup/Signup';
import RegistrationEnd from '../user/registrationEnd/registrationEnd';
import EditProfile from '../user/edit/edit';
import Profile from '../user/profile/Profile';
import OAuth2RedirectHandler from '../user/oauth2/OAuth2RedirectHandler';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import { getCurrentUser, getPartLogin} from '../util/APIUtils';
import { ACCESS_TOKEN } from '../constants';
import PrivateRoute from '../common/PrivateRoute';
import Alert from 'react-s-alert';
import 'react-s-alert/dist/s-alert-default.css';
import 'react-s-alert/dist/s-alert-css-effects/slide.css';
import './App.css';
import Items from "../user/renter/Items";
import Market from "../market/Market";
import Cart from "../user/tenant/cart";
import Policy from "../common/policy";
import Messages from "../common/messages";

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      authenticated: false,
      currentUser: null,
      currentLoginPhase : 0,
      loading: false,
        enablePrivateRouts : false,
    }

    this.loadCurrentlyLoggedInUser = this.loadCurrentlyLoggedInUser.bind(this);
    this.handleLogout = this.handleLogout.bind(this);
  }

  loadCurrentlyLoggedInUser() {
    this.setState({
      loading: true
    });

    getCurrentUser()
      .then(response => {
         if (response.role.role === 'USER') {
             this.setState({
                 currentLoginPhase: 1,
                 currentUser : response,
                 authenticated: true,
                 loading: false,
                 enablePrivateRouts : true
             });
         } else {
             this.setState({
                 currentLoginPhase: 2,
                 currentUser: response,
                 authenticated: true,
                 enablePrivateRouts : true,
                 loading: false
              });

         }
      }).catch(error => {
      this.setState({
          currentLoginPhase : 0,
          enablePrivateRouts : true,
          loading: false
      });
    });
  }

  handleLogout() {
    localStorage.removeItem(ACCESS_TOKEN);
    this.setState({
      authenticated: false,
      currentUser: null,
      currentLoginPhase : 0
    });
    Alert.success("You're safely logged out!");

    window.location.replace('/')
  }

  componentDidMount() {
    this.loadCurrentlyLoggedInUser();
  }

  render() {
    if(this.state.loading) {
      return <LoadingIndicator />
    }

    return (
      <div className="app">
        <div className="app-top-box">
          <AppHeader authenticated={this.state.authenticated}  currentLoginPhase= {this.state.currentLoginPhase} currentUser={this.state.currentUser}  onLogout={this.handleLogout} />
        </div>
        <div className="app-body">
          <Switch>
            <Route exact path="/" component={Home}></Route>           
            <Route path="/registrationEnd" render={(props) => <RegistrationEnd authenticated={this.state.authenticated} currentLoginPhase= {this.state.currentLoginPhase} currentUser={this.state.currentUser} {...props} />}></Route>
              {this.state.enablePrivateRouts ? (
                  <PrivateRoute path="/profile" authenticated={this.state.authenticated} currentUser={this.state.currentUser} currentLoginPhase= {this.state.currentLoginPhase}
                                component={Profile}></PrivateRoute>

              ) : (null)}
              {this.state.enablePrivateRouts ? (
                  <PrivateRoute path="/edit" authenticated={this.state.authenticated} currentUser={this.state.currentUser} currentLoginPhase= {this.state.currentLoginPhase}
                                component={EditProfile}></PrivateRoute>

              ) :(null)}
              {this.state.enablePrivateRouts ? (
                  <PrivateRoute path="/items" authenticated={this.state.authenticated} currentUser={this.state.currentUser} currentLoginPhase= {this.state.currentLoginPhase}
                                component={Items}></PrivateRoute>
              ) :(null)}
              {this.state.enablePrivateRouts ? (
                  <PrivateRoute path="/cart" authenticated={this.state.authenticated} currentUser={this.state.currentUser} currentLoginPhase= {this.state.currentLoginPhase}
                                component={Cart}></PrivateRoute>
              ) :(null)}
              {this.state.enablePrivateRouts ? (
                  <PrivateRoute path="/messages" authenticated={this.state.authenticated} currentUser={this.state.currentUser} currentLoginPhase= {this.state.currentLoginPhase}
                                component={Messages}></PrivateRoute>
              ) :(null)}
              <Route path="/policy"
                     render={(props) => <Policy authenticated={this.state.authenticated}  currentLoginPhase= {this.state.currentLoginPhase} {...props} />}></Route>

              <Route path="/market"
                     render={(props) => <Market authenticated={this.state.authenticated} currentUser={this.state.currentUser} currentLoginPhase= {this.state.currentLoginPhase} {...props}/>}></Route>
            <Route path="/login"
              render={(props) => <Login authenticated={this.state.authenticated}  currentLoginPhase= {this.state.currentLoginPhase} {...props} />}></Route>
            <Route path="/signup"
              render={(props) => <Signup authenticated={this.state.authenticated}  currentLoginPhase= {this.state.currentLoginPhase} {...props} />}></Route>
            <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}></Route>  
            <Route component={NotFound}></Route>
          </Switch>
        </div>
        <Alert stack={{limit: 3}} 
          timeout = {5000}
          position='top-right' effect='slide' offset={65} />
      </div>
    );
  }
}

export default App;
