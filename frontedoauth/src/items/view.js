import React, {Component} from 'react';
import {Link, Redirect} from 'react-router-dom'
import './view.css';
import 'react-datepicker/dist/react-datepicker.css'
import GridList from '@material-ui/core/GridList';
import Grid from '@material-ui/core/Grid';
import GridListTile from '@material-ui/core/GridListTile';
import {API_BASE_URL} from "../constants";
import Button from '@material-ui/core/IconButton';
import AddShoppingCartIcon from '@material-ui/icons/AddShoppingCart';
import {addItemsToCart} from "../util/APIUtils";
import Alert from "react-s-alert";
import DatePicker from "react-datepicker";
import PersonIcon from '@material-ui/icons/Person';
import ViewRenterPage from "../user/renter/ViewRenterPage";
import StarRatings from "react-star-ratings";
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Avatar from '@material-ui/core/Avatar';
import Typography from '@material-ui/core/Typography';
import StarBorderIcon from '@material-ui/icons/StarBorder';
import StarIcon from '@material-ui/icons/Star';
import moment from 'moment'
import Carousel from 'react-bootstrap/Carousel'
import {removeFromWishList, addToWishList} from '../util/APIUtils'
import "bootstrap/dist/css/bootstrap.css";

class View extends Component {
    constructor(props) {
        super(props);
        console.log(props);

        this.state = {
            selectedItem: this.props.selectedItem,
            authenticated: this.props.props.authenticated,
            currentUser: this.props.props.currentUser,
            currentLoginPhase: this.props.props.currentLoginPhase,
            history: this.props.props.history,
            viewOwner: false,

        };
    }

    componentDidMount() {
    }

    removeFromWishlist(tile) {
        if ((!this.props.props.authenticated) || (this.props.props.currentUser !== null && this.props.props.currentUser !== undefined && (this.props.props.currentUser.role.role === 'RENTER'))) {
            Alert.error('Only registered Tenant allow to check items as star');
            return;
        }

        removeFromWishList(tile.id)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'Item removed from wish list');
                    this.setState({
                        wishAction: true
                    });

                    window.location.reload();

                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    addToWishlist(tile) {
        if ((!this.props.props.authenticated) || (this.props.props.currentUser !== null && this.props.props.currentUser !== undefined && (this.props.props.currentUser.role.role === 'RENTER'))) {
            Alert.error('Only registered Tenant allow to check items as star');
            return;
        }


        addToWishList(tile.id)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'Item Added to wish list');
                    this.setState({
                        wishAction: true
                    });
                    window.location.reload();

                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }


    render() {
        if (this.state.viewOwner) {
            return (
                <ViewRenterPage {...this.state}/>
            );
        }
        return (
            <div>

                <h2> {this.state.selectedItem.itemSCategory} - {this.state.selectedItem.itemTCategory}{
                    this.props.selectedItem.star ?
                        <Button aria-label={`star`} className="icon"  onClick={() => {
                            this.removeFromWishlist(this.state.selectedItem)
                        }}>
                            <StarIcon color="primary"/>
                        </Button>
                        :
                        <Button aria-label={`star`} className="icon"  onClick={() => {
                            this.addToWishlist(this.state.selectedItem)
                        }}>
                            <StarBorderIcon color="primary"/>
                        </Button>
                }</h2>

                <Grid container>
                    <Grid item xs={12} lg={6}>
                        <Grid container className="des">
                            <Grid item xs={6} lg={3}>
                                <h7>Description</h7>
                            </Grid>
                            <Grid item xs={6} lg={3} className="">
                                {this.state.selectedItem.description}
                            </Grid>
                        </Grid>
                        <Grid container className="des">
                            <Grid item xs={6} lg={3}>
                                <h7> Year</h7>
                            </Grid>
                            <Grid xs={6} lg={3} className="">
                                {this.state.selectedItem.year_of_production}
                            </Grid>
                        </Grid>
                        <Grid container className="des">
                            <Grid item xs={6} lg={3}>
                                <h7> Price Per Day</h7>
                            </Grid>
                            <Grid item xs={6} lg={3} className="">
                                {this.state.selectedItem.pricePerDay}

                            </Grid>
                        </Grid>
                        <Grid container className="des">
                            <Grid item xs={6} lg={3}>
                                <h7> Renter Name</h7>
                            </Grid>
                            <Grid item xs={6} lg={3} className="">
                                <Button color="secondary" className="userlink" onClick={() => {
                                    this.setState({
                                        viewOwner: true
                                    });
                                }}>
                                    <PersonIcon/>
                                    {this.state.selectedItem.ownerName}
                                </Button>
                            </Grid>
                        </Grid>
                        <Grid container className="des">
                            <Grid item xs={6} lg={3}>
                                <h7> Renter Email</h7>
                            </Grid>
                            <Grid item xs={6} lg={3} className="">
                                {this.state.selectedItem.ownerEmail}</Grid>

                        </Grid>
                    </Grid>
                    <Grid item xs={6} lg={6}>
                        <Carousel>
                            {this.state.selectedItem.imageItemNames.map(imageName => (
                                <Carousel.Item>
                                    <img
                                        className="d-block w-100 h-100"
                                        src={API_BASE_URL + "/items/image/" + this.state.selectedItem.userID + "/" + this.state.selectedItem.id + "/" + imageName}
                                        alt={this.state.selectedItem.itemSCategory + "-" + this.state.selectedItem.itemTCategory}
                                    />
                                </Carousel.Item>
                            ))}
                        </Carousel>
                    </Grid>
                </Grid>
                <Grid container className="des">
                    {this.state.currentUser !== undefined && this.state.currentUser !== null && (this.state.currentUser.role.role === 'TENANT' || this.state.currentUser.role.role === 'BOTH') ? (
                        <AddOrderdItemForm {...this.state}/>
                    ) : (null)}

                    <div style={{padding: 1}}>
                        <Grid item xs={12} lg={12}>
                            <h5>Item Reviews</h5>
                            <Grid container spacing={1}>
                                {this.state.selectedItem.itemReviews.map(review => (
                                    <Grid item xs={12}>
                                        <Card className="card">
                                            <CardHeader
                                                avatar={
                                                    <Avatar className="avatar">
                                                        <img className="avatar-img" src={review.given_by.imageUrl}
                                                             alt={review.given_by.name}/>
                                                    </Avatar>
                                                }
                                                title={review.given_by.name}
                                                subheader={review.givenOn.toString()}

                                            />
                                            <CardContent>
                                                <Grid item xs={12} lg={12}>
                                                    <StarRatings
                                                        className="stars"
                                                        rating={review.rank}
                                                        starRatedColor="blue"
                                                        disabled
                                                        starDimension="20px"
                                                        starSpacing="1px"
                                                        numberOfStars={5}
                                                        name='rankStars'
                                                    />
                                                </Grid>
                                                <Grid item xs={12} lg={6}>
                                                    <Typography paragraph color="textSecondary" >
                                                        {review.text}
                                                    </Typography>
                                                </Grid>
                                            </CardContent>
                                        </Card>
                                    </Grid>
                                ))}
                            </Grid>
                        </Grid>
                    </div>
                </Grid>
                {/*<div className="container">*/}
                {/*<Carousel>*/}
                {/*{this.state.selectedItem.imageItemNames.map(imageName => (*/}
                {/*<Carousel.Item>*/}
                {/*<img*/}
                {/*className="d-block w-100"*/}
                {/*src={API_BASE_URL + "/items/image/" + this.state.selectedItem.userID + "/" + this.state.selectedItem.id + "/" + imageName}*/}
                {/*alt={this.state.selectedItem.itemSCategory + "-" + this.state.selectedItem.itemTCategory}*/}
                {/*/>*/}
                {/*</Carousel.Item>*/}
                {/*))}*/}
                {/*</Carousel>*/}
                {/*</div>*/}
            </div>
        );
    }
}


class AddOrderdItemForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            itemID: props.selectedItem.id,
            userID: props.currentUser.id,
            rentalStartDay: null,
            rentalEndDay: null
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.setStartDate = this.setStartDate.bind(this);
        this.setEndDate = this.setEndDate.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();

        const addItemToCartRequest = Object.assign({}, this.state);

        addItemsToCart(addItemToCartRequest)
            .then(response => {
                if (response.success) {
                    Alert.success("You're successfully add new item to cart.");
                    window.location.reload();
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }
                // this.props.history.push('/market');
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    setStartDate(date) {
        this.setState({
            rentalStartDay: date
        });
    }

    setEndDate(date) {
        this.setState({
            rentalEndDay: date
        });
    }

    addDays(days, currentDate) {
        var date = new Date(currentDate);
        date.setDate(date.getDate() + days);
        return date;
    }

    getDates(startDate, stopDate, data) {
        let currentDate = startDate;
        while (currentDate <= stopDate) {
            data.push(currentDate);
            currentDate = this.addDays(1, currentDate);
        }
        return data;
    }

    render() {
        const isGoodday = date => {
            const day = date.getDay();

            let checkDate = false;
            this.props.selectedItem.daysAvailable.map((numbers) => {

                if (day === numbers) {
                    checkDate = true;
                }
            });

            return checkDate;
        };

        let dateArray = new Array();
        let index = 0;
        if (this.props.selectedItem.start !== null) {
            this.props.selectedItem.start.map((date) => {
                dateArray = this.getDates(new Date(date), new Date(this.props.selectedItem.end[index]), dateArray);
                index++;
            });
        }

        const exclude = dateArray;

        return (
            <Grid item xs={12} lg={6}>
                <h5>Rent Item</h5>
                <form onSubmit={this.handleSubmit}>
                    <div className="form-item">
                        <label className="edit labelSize">Start Date</label>
                        <DatePicker
                            minDate={moment().toDate()}
                            selected={this.state.rentalStartDay}
                            filterDate={isGoodday}
                            onChange={date => this.setStartDate(date)}
                            excludeDates={exclude}
                            selectsStart
                            startDate={this.state.rentalStartDay}
                            endDate={this.state.rentalEndDay}
                        />
                    </div>
                    <div className="form-item">
                        <label className="edit labelSize">End Date</label>
                        <DatePicker
                            minDate={moment().toDate()}
                            selected={this.state.rentalEndDay}
                            filterDate={isGoodday}
                            excludeDates={exclude}
                            onChange={date => this.setEndDate(date)}
                            selectsEnd
                            endDate={this.state.rentalEndDay}
                            minDate={this.state.rentalStartDay}
                        />
                    </div>

                    <div className="form-item">
                        <label>Add to Cart</label>
                        <Button type="submit" variant="contained" color="primary" className="AddItemB">
                            <AddShoppingCartIcon/>
                        </Button>
                    </div>
                </form>
            </Grid>
        );
    }
}

export default View