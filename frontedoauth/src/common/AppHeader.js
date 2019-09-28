import React, { Component } from 'react';
import { Link, NavLink } from 'react-router-dom';
import './AppHeader.css';
import IconButton from '@material-ui/core/IconButton';
import ShoppingCartIcon from '@material-ui/icons/ShoppingCart';
import {getAllMessageLength, getCartLength} from '../util/APIUtils'
import Alert from "react-s-alert";
import Badge from '@material-ui/core/Badge';
import { withStyles } from '@material-ui/core/styles';
import MailIcon from '@material-ui/icons/Mail';

import LoadingIndicator from '../common/LoadingIndicator';

class AppHeader extends Component {
    constructor(props) {
        super(props);
        this.state = {
            cartLength : 0,
            messageLength : 0,
            loading : false
        }

        this.getCartItemsNumber = this.getCartItemsNumber.bind(this);
        this.getMessageNumber = this.getMessageNumber.bind(this);
    }


    componentDidMount() {
        if (this.props.currentUser !== undefined && this.props.currentUser !== null && (this.props.currentUser.role.role === 'BOTH' || this.props.currentUser.role.role === 'TENANT')) {
            this.getCartItemsNumber();
            this.getMessageNumber();
        }
    }

    getCartItemsNumber() {
        this.setState({
            loading : true
        });

        getCartLength().then(response => {
            this.setState({
                cartLength : response,
                loading : false
            });
        }).catch(error => {
            this.setState({
                cartLength : 0,
                loading : false
            });
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }
    getMessageNumber() {
        this.setState({
            loading : true
        });

        getAllMessageLength().then(response => {
            this.setState({
                messageLength : response,
                loading : false
            });
        }).catch(error => {
            this.setState({
                messageLength : 0,
                loading : false
            });
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }


    render() {
        const StyledBadge = withStyles(theme => ({
            badge: {
                top: '50%',
                right: -3,
                // The border color match the background color.
                border: `2px solid ${
                    theme.palette.type === 'light' ? theme.palette.grey[200] : theme.palette.grey[900]
                    }`,
            },
        }))(Badge);

        if (this.state.loading) {
            return (<LoadingIndicator/>);
        }

        if (this.props.authenticated && this.props.currentLoginPhase === 2) {
            return (
                <header className="app-header">
                    <div className="container">
                        <div className="app-branding">
                            <Link to="/" className="app-title">RetroRent</Link>
                        </div>
                        <div className="app-options">
                            <nav className="app-nav">
                                <ul>
                                    <li>
                                        <NavLink to="/profile">Profile</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/market">Market</NavLink>
                                    </li>
                                    {this.props.currentUser.role.role === 'TENANT' || this.props.currentUser.role.role === 'BOTH' ? (
                                        <NavLink to="/cart">
                                            <IconButton aria-label="cart">
                                                <StyledBadge badgeContent={this.state.cartLength} color="primary">
                                                    <ShoppingCartIcon />
                                                </StyledBadge>
                                            </IconButton>
                                        </NavLink>
                                    ) : (
                                        null
                                    )}
                                    <NavLink to="/messages">
                                        <IconButton aria-label="messages">
                                            <StyledBadge badgeContent={this.state.messageLength} color="primary">
                                                <MailIcon />
                                            </StyledBadge>
                                        </IconButton>
                                    </NavLink>
                                    <li>
                                        <a onClick={this.props.onLogout}>Logout</a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </header>)
        } else if (this.props.authenticated && this.props.currentLoginPhase === 1) {
            return (
                <header className="app-header">
                    <div className="container">
                        <div className="app-branding">
                            <Link to="/" className="app-title">RetroRent </Link>
                        </div>
                        <div className="app-options">
                            <nav className="app-nav">
                                <ul>
                                    <li>
                                        <NavLink to="/registrationEnd">Registration</NavLink>
                                    </li>
                                    <li>
                                        <a onClick={this.props.onLogout}>Logout</a>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </header>)
        } else {
            return (
                <header className="app-header">
                    <div className="container">
                        <div className="app-branding">
                            <Link to="/" className="app-title">RetroRent </Link>
                        </div>
                        <div className="app-options">
                            <nav className="app-nav">
                                <ul>
                                    <li>
                                        <NavLink to="/login">Login</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/signup">Signup</NavLink>
                                    </li>
                                    <li>
                                        <NavLink to="/market">Market</NavLink>
                                    </li>
                                </ul>
                            </nav>
                        </div>
                    </div>
                </header>)
        }

    }
}

export default AppHeader;