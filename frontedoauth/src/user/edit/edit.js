import React, { Component } from 'react';
import './edit.css';
import {edit} from "../../util/APIUtils";
import Alert from "react-s-alert";
import Edit from '@material-ui/icons/Edit';
import Button from '@material-ui/core/IconButton';
import Grid from '@material-ui/core/Grid';

class EditProfile extends Component {
    componentDidMount() {
    }


    constructor(props) {
        super(props);
        console.log(props);
    }
    render() {
        return (
            <div className="signup-container">
                <div className="signup-content">
                    <h1 className="signup-title">Edit User Info : </h1>
                    <EditUserForm {...this.props}/>
                </div>
            </div>
        );
    }
}


class EditUserForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.currentUser.id,
            first_name: props.currentUser.name,
            last_name: props.currentUser.lastName,
            user_name: props.currentUser.userName,
            email: props.currentUser.email,
            phone_number: props.currentUser.phoneNumber,
            city: props.currentUser.city,
            paymentLink : props.currentUser.paymentLink,
            street: props.currentUser.street,
            home_number: props.currentUser.address_number,
            postal_code: props.currentUser.postal_code,
            img_url: props.currentUser.imageUrl,
            user_type_tenant: props.currentUser.role.role === 'TENANT',
            user_type_renter: props.currentUser.role.role === 'RENTER'
        };

        if (props.currentUser.role.role === 'BOTH') {
            this.state.user_type_tenant = true;
            this.state.user_type_renter = true;
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

        if (inputValue === "false"){
            this.setState({
                [inputName] : true
            });
        } else {
            this.setState({
                [inputName] : false
            });
        }

    }


    handleSubmit(event) {
        event.preventDefault();

        const editRequest = Object.assign({}, this.state);

        edit(editRequest)
            .then(response => {
                if (response.success) {
                    Alert.success("You're successfully edit user info !");
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
                    <Grid container>
                        <Grid item xs={3}>
                            <label className="">First Name:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="first_name"
                                   className="form-control edit formSize" placeholder="First Name"
                                   value={this.state.first_name} onChange={this.handleInputChange} required/>
                        </Grid>
                    </Grid>
                </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >Last Name:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="last_name"
                                   className="form-control edit formSize" placeholder="Last Name"
                                   value={this.state.last_name} onChange={this.handleInputChange} required/>

                        </Grid>
                    </Grid>
                </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >User Name:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="user_name"
                                   className="form-control edit formSize" placeholder="User Name"
                                   value={this.state.user_name} onChange={this.handleInputChange} required/>
                        </Grid>
                    </Grid>
                </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >Email:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="email" name="email"
                                   className="form-control edit formSize" placeholder="Email"
                                   value={this.state.email} onChange={this.handleInputChange} required/>
                        </Grid>
                    </Grid>
                </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >Phone:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="tel" name="phone_number"
                                   className="form-control edit formSize" placeholder="Phone Number"
                                   value={this.state.phone_number} onChange={this.handleInputChange} required/>
                        </Grid>
                    </Grid>
                </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >City:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="city"
                                   className="form-control edit formSize" placeholder="City"
                                   value={this.state.city} onChange={this.handleInputChange} required/>
                        </Grid>
                    </Grid>
                 </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >Street:</label>

                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="street"
                                   className="form-control edit formSize" placeholder="street"
                                   value={this.state.street} onChange={this.handleInputChange} required/>
                        </Grid>
                    </Grid>
                  </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >Home:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="home_number"
                                   className="form-control edit formSize" placeholder="Home Number"
                                   value={this.state.home_number} onChange={this.handleInputChange} required/>
                        </Grid>
                    </Grid>
                    </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >Postal Code:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="postal_code"
                                   className="form-control edit formSize" placeholder="Postal Code"
                                   value={this.state.postal_code} onChange={this.handleInputChange} required/>
                        </Grid>
                    </Grid>
                   </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >PayPal-Me:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="paymentLink"
                                   className="form-control edit formSize" placeholder="Payment Link - PayPalMe"
                                   value={this.state.paymentLink} onChange={this.handleInputChange}/>
                        </Grid>
                    </Grid>
                 </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label >Image URL:</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="img_url"
                                   className="form-control edit formSize" placeholder="Image Url"
                                   value={this.state.img_url} onChange={this.handleInputChange}/>
                        </Grid>
                    </Grid>
                   </div>
                <div className="form-item">
                    <label className="container checkboxRetrorent">renter
                        <input type="checkbox" name="user_type_renter"
                               className="form-control"
                               value={this.state.user_type_renter} onChange={this.handleCheckBoxInputChange} checked={this.state.user_type_renter}/>
                        <span className="checkboxRetrorent checkmark"/>
                    </label>
                    <label className="container checkboxRetrorent">tenant
                        <input type="checkbox" name="user_type_tenant"
                               className="form-control"
                               value={this.state.user_type_tenant} onChange={this.handleCheckBoxInputChange} checked={this.state.user_type_tenant}/>
                        <span className="checkboxRetrorent checkmark"/>
                    </label>
                </div>
                <div className="form-item">
                    <Button type="submit" variant="contained" color="primary" className="AddItemB"><Edit/> Edit</Button>
                </div>
            </form>
        );
    }
}

export default EditProfile