import React, { Component } from 'react';
import { Link, Redirect } from 'react-router-dom'
import './Items.css';
import Alert from "react-s-alert";
import Dropdown from 'react-dropdown'
import 'react-dropdown/style.css'
import {createItems, getMainCategory, getSecCategory, getThirdCategory, uploadFileItem} from "../../util/APIUtils";
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import AddBox from '@material-ui/icons/AddBox';
import Button from '@material-ui/core/IconButton';

import MenuItem from '@material-ui/core/MenuItem';

class Items extends Component {
    constructor(props) {
        super(props);
        console.log(props);
    }
    render() {
        return (
            <div className="signup-container">
                <div className="signup-content">
                    <h1 className="signup-title">Create New Item</h1>
                    <CreateNewItemForm {...this.props} />
                </div>
            </div>
        );
    }
}

class CreateNewItemForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loading : false,
            renter_user_id: this.props.currentUser.id,
            pricePerDay: '',
            year_of_production: '',
            description : '',
            labels : '',
            sun : true,
            mon : true,
            tue : true,
            wed : true,
            thu : true,
            fri : true,
            sat : true,
            main_category: '',
            secondary_category: '',
            third_category: '',
            currency : 'ILS'
        };

        this.img_item = '';

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleCheckBoxInputChange = this.handleCheckBoxInputChange.bind(this);
        this.handleMainDropDownInputChange = this.handleMainDropDownInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.sendSecReq = this.sendSecReq.bind(this);
        this.sendThirdRe = this.sendThirdRe.bind(this);
        this.handleThirdDropDownInputChange = this.handleThirdDropDownInputChange.bind(this);
        this.handleSecDropDownInputChange = this.handleSecDropDownInputChange.bind(this);
        this.handleFileChange = this.handleFileChange.bind(this);

        this.mainCategory_options = [];
        this.showMainCategory = false;
        this.showSecCategory = false;
        this.showThirdCategory = false;
        this.secCategory_options = [];
        this.thirdCategory_options = [];
    }

    componentDidMount() {
        this.getMainCategoryOptions();
    }

    sendThirdRe(inputValue) {
        this.showThirdCategory = false;

        let test = {
            secondary_category: inputValue,
            main_category : this.state.main_category
        };
        const re = Object.assign({}, test);

        getThirdCategory(re).then(response => {
            this.showThirdCategory = true;
            this.thirdCategory_options = response;
            this.setState({
                loading : false,
                third_category : response[0]
            });
        }).catch(error => {
            this.thirdCategory_options = [];
            this.setState({
                loading : false,
                third_category : 'none'
            });
        });
    }


    sendSecReq(inputValue) {
        this.showSecCategory = false;

        let test = {
            main_category: inputValue
        };
        const re = Object.assign({}, test);

        getSecCategory(re).then(response => {
            this.showSecCategory = true;
            this.secCategory_options = response;
            this.setState({
                loading : false,
                secondary_category : response[0]
            });

            this.sendThirdRe(response[0]);
        }).catch(error => {
            this.secCategory_options = [];
            this.setState({
                loading : false,
                secondary_category : 'none'
            });
        });
    }

    getMainCategoryOptions() {
        this.showMainCategory = false;
        this.setState({
            loading : true
        });
        getMainCategory().then(response => {
            this.mainCategory_options = response;
            this.showMainCategory = true;
            this.setState({
                loading : false,
                main_category : response[0]
            });

            this.sendSecReq(response[0]);
        }).catch(error => {
            this.mainCategory_options = [];
            this.setState({
                loading : false
            });
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


    handleMainDropDownInputChange(event) {
        const inputValue = event.value;
        this.setState({
            main_category : inputValue,
            loading : true
        });

        this.sendSecReq(inputValue);
    }

    handleSecDropDownInputChange(event) {
        const inputValue = event.value;
        this.showThirdCategory = false;
        this.setState({
            secondary_category : inputValue,
            loading : true
        });

        this.sendThirdRe(inputValue);
    }

    handleThirdDropDownInputChange(event) {
        const inputValue = event.value;
        this.setState({
            third_category : inputValue,
        });
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

        const createItemRequest = Object.assign({}, this.state);

        createItems(createItemRequest)
            .then(response => {
                Alert.success("You're successfully created new item.");

                // const createFileRequest = Object.assign({}, this.test);
                uploadFileItem(this.img_item,response, this.props.currentUser.id).then(response => {
                    Alert.success("You're successfully upload new item image.");
                    window.location.replace('/profile')
                    // this.props.history.push('/profile');
                }).catch(error => {
                    Alert.success("error upload new item image.");
                });
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handleFileChange(event) {
        this.img_item = event.target.files;
    }


    render() {
        const currencies = [
            {
                value: 'USD',
                label: '$',
            },
            {
                value: 'EUR',
                label: '€',
            },
            {
                value: 'ILS',
                label: '₪',
            }
        ];

        return (
            <form onSubmit={this.handleSubmit}>
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <div className="form-item">
                            <input type="text" name="year_of_production"
                                   className="form-control" placeholder="Year Of Production"
                                   value={this.state.year_of_production} onChange={this.handleInputChange} required/>
                        </div>
                    </Grid>
                    <Grid item xs={6}>
                        <div className="form-item">
                            <input type="text" name="labels"
                                   className="form-control" placeholder="Labels"
                                   value={this.state.labels} onChange={this.handleInputChange}/>
                        </div>
                    </Grid>
                </Grid>
                <Grid container spacing={2}>
                    <Grid item xs={6}>
                        <div className="form-item ">
                            <div className="formLabel-items">
                                Category
                                <span className="checkboxRetrorent checkmark"/>
                            </div>

                            <Dropdown name="main_category" className={this.showMainCategory ? '' : 'hidden'} options={this.mainCategory_options} onChange={this.handleMainDropDownInputChange} value={this.state.main_category} placeholder="Select an option" />
                        </div>
                        <div className="form-item ">
                            <div className="formLabel-items">
                                Manufacturer
                                <span className="checkboxRetrorent checkmark"/>
                            </div>

                            <Dropdown name="sec_category" className={this.showSecCategory ? '' : 'hidden'} options={this.secCategory_options} onChange={this.handleSecDropDownInputChange} value={this.state.secondary_category} placeholder="Select an option" />
                        </div>
                        <div className="form-item ">
                            <div className="formLabel-items">
                                Model
                                <span className="checkboxRetrorent checkmark"/>
                            </div>

                            <Dropdown name="third_category" className={this.showThirdCategory ? '' : 'hidden'} options={this.thirdCategory_options} onChange={this.handleThirdDropDownInputChange} value={this.state.third_category} placeholder="Select an option" />
                        </div>
                    </Grid>
                    <Grid item xs={6}>
                        <div className="form-item ">
                            <div className="formLabel-items">
                                 Available At
                                <span className="checkboxRetrorent checkmark"/>
                            </div>
                            <div className="items-days-1">
                                <label className="container checkboxRetrorent">Sun
                                    <input type="checkbox" name="sun"
                                           className="form-control"
                                           value={this.state.sun} onChange={this.handleCheckBoxInputChange} checked={this.state.sun}/>
                                    <span className="checkboxRetrorent checkmark"/>
                                </label>
                                <label className="container checkboxRetrorent">Mon
                                    <input type="checkbox" name="mon"
                                           className="form-control"
                                           value={this.state.mon} onChange={this.handleCheckBoxInputChange} checked={this.state.mon}/>
                                    <span className="checkboxRetrorent checkmark"/>
                                </label>
                                <label className="container checkboxRetrorent">Tue
                                    <input type="checkbox" name="tue"
                                           className="form-control"
                                           value={this.state.tue} onChange={this.handleCheckBoxInputChange} checked={this.state.tue}/>
                                    <span className="checkboxRetrorent checkmark"/>
                                </label>
                                <label className="container checkboxRetrorent">Wed
                                    <input type="checkbox" name="wed"
                                           className="form-control"
                                           value={this.state.wed} onChange={this.handleCheckBoxInputChange} checked={this.state.wed}/>
                                    <span className="checkboxRetrorent checkmark"/>
                                </label>
                            </div>
                            <div className="items-days-1">
                                <label className="container checkboxRetrorent">Thu
                                    <input type="checkbox" name="thu"
                                           className="form-control"
                                           value={this.state.thu} onChange={this.handleCheckBoxInputChange} checked={this.state.thu}/>
                                    <span className="checkboxRetrorent checkmark"/>
                                </label>
                                <label className="container checkboxRetrorent">Fri
                                    <input type="checkbox" name="fri"
                                           className="form-control"
                                           value={this.state.fri} onChange={this.handleCheckBoxInputChange} checked={this.state.fri}/>
                                    <span className="checkboxRetrorent checkmark"/>
                                </label>
                                <label className="container checkboxRetrorent">Sat
                                    <input type="checkbox" name="sat"
                                           className="form-control"
                                           value={this.state.sat} onChange={this.handleCheckBoxInputChange} checked={this.state.sat}/>
                                    <span className="checkboxRetrorent checkmark"/>
                                </label>
                            </div>
                        </div>
                    </Grid>
                </Grid>

                <div className="form-item">
                    <TextField
                        id="standard-textarea"
                        label="Description"
                        name="description"
                        rowsMax={5}
                        value={this.state.description}
                        placeholder="Description"
                        onChange={this.handleInputChange}
                        multiline
                        fullWidth="100%"
                        margin="normal"
                    />
               </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={7}>
                            <input type="text" name="pricePerDay"
                                   className="form-control MuiFormControl-marginNormal" placeholder="Price Per Day"
                                   value={this.state.pricePerDay} onChange={this.handleInputChange} required/>

                        </Grid>
                        <Grid item xs={4}>
                            <TextField
                                id="standard-select-currency"
                                select

                                name="currency"
                                value={this.state.currency}
                                onChange={this.handleInputChange}
                                helperText="Please select your currency"
                                margin="normal"
                            >
                                {currencies.map(option => (
                                    <MenuItem key={option.value} value={option.value}>
                                        {option.label}
                                    </MenuItem>
                                ))}
                            </TextField>

                        </Grid>
                    </Grid>
                </div>
                <div className="form-item">
                    <label className="formLabel-items">Select Item Image :</label>
                    <input type="file" name="img_item"  multiple="multiple"
                           className="form-control" placeholder="Image Item"
                           onChange={this.handleFileChange} required/>
                </div>
                <div className="form-item">
                    <Button type="submit" variant="contained" color="primary" className="AddItemB" ><AddBox/>Create</Button>
                </div>
            </form>

        );
    }
}

export default Items