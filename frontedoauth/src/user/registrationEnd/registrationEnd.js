import React, { Component } from 'react';
import './registrationEnd.css';
import { Link, Redirect } from 'react-router-dom'
import { registrationEnd } from '../../util/APIUtils';
import Alert from 'react-s-alert';
import Button from '@material-ui/core/IconButton';
import InfoIcon from '@material-ui/icons/Info';
import Grid from '@material-ui/core/Grid';
import SaveAlt from '@material-ui/icons/SaveAlt';


class RegistrationEnd extends Component {
    render() {
        if(this.props.authenticated && this.props.currentLoginPhase === 2) {
            return <Redirect
                to={{
                    pathname: "/",
                    state: { from: this.props.location }
                }}/>;
        }

        if(this.props.currentLoginPhase === 0) {
            return <Redirect
                to={{
                    pathname: "/login",
                    state: { from: this.props.location }
                }}/>;
        }

        return (
            <div className="signup-container">
                <div className="signup-content">
                <h1 className="signup-title">Registration to RetroRent-Step 2</h1>
                <RegistrationEndForm {...this.props} />
                </div>
            </div>
        );
    }
}

class RegistrationEndForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            phone_number: '',
            city: '',
            street: '',
            home_number: '',
            postal_code: '',
            paymentLink: '',
            user_type_tenant: 'false',
            user_type_renter: 'false',
            email: props.currentUser.email
        }
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleCheckBoxInputChange = this.handleCheckBoxInputChange.bind(this);
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

    handleCheckBoxInputChange(event) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        if (inputValue === 'false'){
            this.setState({
                [inputName] : 'true'
            });
        } else {
            this.setState({
                [inputName] : 'false'
            });
        }

    }

    handleSubmit(event) {
        event.preventDefault();   

        const registrationEndRequest = Object.assign({}, this.state);

        registrationEnd(registrationEndRequest)
        .then(response => {
            if (response.success) {
                Alert.success("You're successfully registered.");
                this.props.history.push("/");
                window.location.reload();
            } else {
                Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
            }
        }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');            
        });
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div className="form-item">
                    <input type="tel" name="phone_number"
                           className="form-control" placeholder="Phone Number"
                           value={this.state.phone_number} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <input type="text" name="city"
                           className="form-control" placeholder="City"
                           value={this.state.city} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <input type="text" name="street"
                           className="form-control" placeholder="street"
                           value={this.state.street} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <input type="text" name="home_number"
                           className="form-control" placeholder="Home Number"
                           value={this.state.home_number} onChange={this.handleInputChange} required/>
                </div>
                <div className="form-item">
                    <input type="text" name="postal_code"
                           className="form-control" placeholder="Postal Code"
                           value={this.state.postal_code} onChange={this.handleInputChange} required/>
                </div>

                <div className="form-item">
                    <Grid container>
                        <Grid item xs={11}>
                            <input type="text" name="paymentLink"
                                   className="form-control payment" placeholder="Payment Link - PayPal.Me"

                                   value={this.state.paymentLink} onChange={this.handleInputChange}/>
                            <label>      Example: https://paypal.me/username</label>
                        </Grid>
                        <Grid item xs={1}>
                            <Button aria-label={`info`} className="iconIn" color="primary" onClick={() => {
                                window.location.replace('https://www.paypal.me')
                            }}>
                                <InfoIcon />
                            </Button>
                        </Grid>
                    </Grid>
                </div>
                <label>Choose Your Role</label>
                <div className="form-item">
                    <label className="container checkboxRetrorent">Renter
                        <input type="checkbox" name="user_type_renter"
                               className="form-control"
                               value={this.state.user_type_renter} onChange={this.handleCheckBoxInputChange}/>
                            <span className="checkboxRetrorent checkmark"/>
                    </label>
                    <label className="container checkboxRetrorent">Tenant
                        <input type="checkbox" name="user_type_tenant"
                               className="form-control" value={this.state.user_type_tenant} onChange={this.handleCheckBoxInputChange}/>
                        <span className="checkboxRetrorent checkmark"/>
                    </label>
                    <label>*You can choose both </label>

                </div>
                <div className="form-item">
                    <label className="container checkboxRetrorent"><Link to="/policy">I Accept Terms and Conditions</Link>
                        <input type="checkbox" name="policy"
                               className="form-control" required/>
                        <span className="checkboxRetrorent checkmark"/>
                    </label>

                </div>

                <div className="form-item">
                    <Button type="submit" color="primary" className="AddItemB"><SaveAlt/>Register</Button>
                </div>
            </form>                    

        );
    }
}

export default RegistrationEnd