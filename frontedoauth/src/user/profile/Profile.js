import React, { Component, forwardRef } from 'react';
import { Link, Redirect } from 'react-router-dom'
import './Profile.css';
import LoadingIndicator from '../../common/LoadingIndicator';
import {
    getRenterAllItems,
    getRenterAllOrders,
    approveOrderItem,
    rejectOrderItem,
    getTenantOrders,
    deleteOrderItem,
    getTenantWish,
    removeFromWishList,
    finishOrderItem, addReviewForTenant, addReviewForItem, getAllTenantReviews
} from "../../util/APIUtils";
import MaterialTable from 'material-table';
import { API_BASE_URL } from '../../constants';
import GradeIcon from '@material-ui/icons/Grade';
import {editItem, deleteItems, uploadFileItem, deleteItemsPicture, getItemPicURL} from "../../util/APIUtils";
import AddBox from '@material-ui/icons/AddBox';
import ArrowUpward from '@material-ui/icons/ArrowUpward';
import TextField from '@material-ui/core/TextField';
import RateReviewIcon from '@material-ui/icons/RateReview';
import MenuItem from '@material-ui/core/MenuItem';
import StarRatings from 'react-star-ratings';
import ListSubheader from '@material-ui/core/ListSubheader';
import Button from '@material-ui/core/IconButton';
import Check from '@material-ui/icons/Check';
import ChevronLeft from '@material-ui/icons/ChevronLeft';
import ChevronRight from '@material-ui/icons/ChevronRight';
import Clear from '@material-ui/icons/Clear';
import DeleteOutline from '@material-ui/icons/DeleteOutline';
import Edit from '@material-ui/icons/Edit';
import FilterList from '@material-ui/icons/FilterList';
import FirstPage from '@material-ui/icons/FirstPage';
import LastPage from '@material-ui/icons/LastPage';
import Remove from '@material-ui/icons/Remove';
import SaveAlt from '@material-ui/icons/SaveAlt';
import Search from '@material-ui/icons/Search';
import ViewColumn from '@material-ui/icons/ViewColumn';
import Alert from "react-s-alert";
import GridList from '@material-ui/core/GridList';
import Grid from '@material-ui/core/Grid';
import GridListTile from '@material-ui/core/GridListTile';
import GridListTileBar from '@material-ui/core/GridListTileBar';
import DeleteIcon from '@material-ui/icons/Delete';
import View from "../../items/view";
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Avatar from '@material-ui/core/Avatar';
import Typography from '@material-ui/core/Typography';

class Profile extends Component {
    constructor(props) {
        super(props);
        console.log(props);

        this.state = {
            loading: false,
            renterItems : null,
            renterAllOrdersWaiting : null,
            renterAllOrdersApproved : null,
            editItemMode : false,
            showItemView : false,
            tenantAllOrders : null,
            tenantWishList : null,
            wishAction : false,
            userID : this.props.currentUser.id,
            props : this.props,
            openD : false,
            openDItem : false,
            rankStars : 0,
            textReview : null,
            tenantEmail : null,
            ownerItemEmail : null,
            openTenantReview : false,
            itemReviewID : null
        }

        this.getRenterItems = this.getRenterItems.bind(this);
        this.getRenterAllOrders = this.getRenterAllOrders.bind(this);
        this.getTenantOrders = this.getTenantOrders.bind(this);
        this.getTenantWishList = this.getTenantWishList.bind(this);
        this.MaterialTableDemo = this.MaterialTableDemo.bind(this);
        this.MaterialTableOrdersWaiting = this.MaterialTableOrdersWaiting.bind(this);
        this.MaterialTableOrdersApproved = this.MaterialTableOrdersApproved.bind(this);
        this.MaterialTableOrdersTenant = this.MaterialTableOrdersTenant.bind(this);
        this.MaterialTableTenantWishList = this.MaterialTableTenantWishList.bind(this);
        this.handleAction = this.handleAction.bind(this);
        this.handleDeleteAction = this.handleDeleteAction.bind(this);
        this.handleApproveAction = this.handleApproveAction.bind(this);
        this.handleRemoveFromWithList = this.handleRemoveFromWithList.bind(this);
        this.handleRejectAction = this.handleRejectAction.bind(this);
        this.handleDeleteOrderItemAction = this.handleDeleteOrderItemAction.bind(this);
        this.handleViewAction = this.handleViewAction.bind(this);
        this.handleViewWishAction = this.handleViewWishAction.bind(this);
        this.handleFinishAction = this.handleFinishAction.bind(this);
        this.renderImage = this.renderImage.bind(this);
        this.handleRateTenantAction = this.handleRateTenantAction.bind(this);
        this.ReviewDialog = this.ReviewDialog.bind(this);
        this.handleClose = this.handleClose.bind(this);
        this.handleReviewInputChange = this.handleReviewInputChange.bind(this);
        this.changeRating = this.changeRating.bind(this);
        this.ReviewItemDialog = this.ReviewItemDialog.bind(this);
        this.handleItemClose = this.handleItemClose.bind(this);
        this.handelRateAction = this.handelRateAction.bind(this);
        this.handleRateItemAction = this.handleRateItemAction.bind(this);
        this.handleWatchTenantReview = this.handleWatchTenantReview.bind(this);
        this.ReviewTenantWatch = this.ReviewTenantWatch.bind(this);
    }

    componentDidMount() {
        if (this.props.currentUser.role.role === 'RENTER' || this.props.currentUser.role.role === 'BOTH') {
            this.getRenterItems();
            this.getRenterAllOrders();
        }

        if (this.props.currentUser.role.role === 'TENANT' || this.props.currentUser.role.role === 'BOTH') {
            this.getTenantOrders();
            this.getTenantWishList();
        }
    }

    getRenterItems() {
        this.setState({
            loading : true
        });

        getRenterAllItems().then(response => {
            this.setState({
                    renterItems : response,
                    loading : false
                });
        }).catch(error => {
            this.setState({
                renterItems : null,
                loading : false
            });
        });
    }

    getTenantOrders() {
        this.setState({
            loading : true
        });

        getTenantOrders().then(response => {
            this.setState({
                tenantAllOrders : response,
                loading : false
            });
        }).catch(error => {
            this.setState({
                tenantAllOrders : null,
                loading : false
            });
        });
    }

    getTenantWishList() {
        this.setState({
            loading : true
        });

        getTenantWish().then(response => {
            this.setState({
                tenantWishList : response,
                loading : false
            });
        }).catch(error => {
            this.setState({
                tenantWishList : null,
                loading : false
            });
        });
    }

    getRenterAllOrders() {
        this.setState({
            loading : true
        });

        getRenterAllOrders().then(response => {

            const waiting = Array();
            const approved = Array();
            response.map(orderItem => {
                if (orderItem.status === 'WAITING') {
                    waiting.push(orderItem);
                } else if (orderItem.status !== 'CART') {
                    approved.push(orderItem);
                }
            });
            this.setState({
                renterAllOrdersWaiting : waiting,
                renterAllOrdersApproved :approved,
                loading : false
            });
        }).catch(error => {
            this.setState({
                renterAllOrdersWaiting : null,
                renterAllOrdersApproved : null,
                loading : false
            });
        });
    }

    renderImage(imageUrl) {
        return (
            <div>
                <img src={imageUrl} width="2%" height="2%" />
                <img src={imageUrl} width="2%" height="2%" />
            </div>

        );
    }

    handelDeleteItemPic(userId, itemId, imageName){
        this.test = ({
            itemID : itemId,
            userID : userId,
            imageName : imageName
        });

        const deleteItemRequest = Object.assign({}, this.test);

        deleteItemsPicture(deleteItemRequest)
            .then(response => {
                Alert.success("You're successfully deleted item pic.");
                window.location.reload();
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }


    render() {
        if (this.state.loading) {
            return (<LoadingIndicator/>);
        }

        if (this.state.openD) {
            return (<this.ReviewDialog/>);
        }

        if (this.state.openDItem) {
            return (<this.ReviewItemDialog/>);
        }

        if (this.state.openTenantReview)
        {
            this.state.openTenantReview = false;
            return (<this.ReviewTenantWatch/>);
        }

        if ((this.state.editItemMode) && (this.state.currentItem !== null) && (this.props.currentUser.role.role === 'RENTER' || this.props.currentUser.role.role === 'BOTH') && this.state.currentItem.ownerEmail === this.props.currentUser.email)
        {
            this.state.editItemMode = false;
            return (
                <Grid className="marketTitle">
                    <Grid container>
                        <Grid item xs={12} className="signup-container">
                            <Grid item xs={6} className="signup-content">
                                <h1 className="signup-title">Edit Item Info </h1>
                                <EditItemForm {...this.state}/>
                                <h4 className="signup-title">Add New Pictures </h4>
                                <EditItemPicForm {...this.state}/>
                            </Grid>
                        </Grid>
                        <Grid item xs={12}>
                            <div className="root">
                                <GridList cellHeight={350} className="gridList" cols={2}>
                                    <GridListTile key="Subheader" cols={2} style={{ height: 'auto' }}>
                                        <ListSubheader component="div">All Item Current Photos</ListSubheader>
                                    </GridListTile>
                                    {this.state.currentItem.imageItemNames.map(imageName => (
                                        <GridListTile key={imageName}>
                                            <img src={API_BASE_URL + "/items/image/" + this.state.currentItem.userID + "/" + this.state.currentItem.id + "/" + imageName} alt={this.state.currentItem.itemSCategory + "-" + this.state.currentItem.itemTCategory} />
                                            <GridListTileBar
                                                actionIcon={
                                                    <Button aria-label={`delete`} className="icon" onClick={() => {
                                                        this.handelDeleteItemPic(this.state.currentItem.userID, this.state.currentItem.id, imageName)
                                                    }}>
                                                        <DeleteIcon />
                                                    </Button>
                                                }
                                            />
                                        </GridListTile>
                                    ))}
                                </GridList>
                            </div>
                        </Grid>
                    </Grid>
                </Grid>
            );
        }

        if (this.state.showItemView) {
            this.state.showItemView = false;
            return (
                <div className="marketTitle">
                    <View {...this.state}/>
                </div>
            );
        }

        return (
            <div className="profile-container">
                <div className="container">
                    <div className="profile-info">
                        <div className="profile-avatar">
                            { 
                                this.props.currentUser.imageUrl ? (
                                    <img src={this.props.currentUser.imageUrl} alt={this.props.currentUser.name}/>
                                ) : (

                                <Avatar style={{positon:"relative",marginTop:"25px",top:"50%",left:"50%",transform:"translate(-50%, -50%)"}}>{this.props.currentUser.name && this.props.currentUser.name[0]}</Avatar>

                                )
                            }
                        </div>
                        <div className="profile-name">
                            <p className="profile-name">{this.props.currentUser.name}</p>
                           <p className="profile-email">{this.props.currentUser.email}</p>
                        </div>

                        <span className="signup-link"> <Link to="/edit">Edit Profile</Link></span>
                    </div>

                </div>

                {this.props.currentUser.role.role === 'RENTER' || this.props.currentUser.role.role === 'BOTH' ? (
                  <div>
                    <div className="container tableTop">
                        <this.MaterialTableDemo {...this.state.renterItems}/>
                        <Button variant="contained" color="primary" className="AddItemBC" onClick={() => {this.props.history.push('/items')}}>
                            <AddBox />
                            Add New Item
                        </Button>
                    </div>
                    <div className="container tableTop">
                        <this.MaterialTableOrdersWaiting {...this.state.renterAllOrdersWaiting}/>
                    </div>

                      <div className="container tableTop">
                          <this.MaterialTableOrdersApproved {...this.state.renterAllOrdersApproved}/>
                      </div>
                  </div>
                ) : (
                    null
                ) }
                {this.props.currentUser.role.role === 'TENANT' || this.props.currentUser.role.role === 'BOTH' ? (
                    <div>
                        <div className="container tableTop">
                            <this.MaterialTableOrdersTenant {...this.state.tenantAllOrders}/>
                        </div>
                        <div className="container tableTop">
                            <this.MaterialTableTenantWishList {...this.state.tenantWishList}/>
                        </div>
                    </div>
                ) : (
                    null
                ) }

            </div>
        );
    }

    handleAction(event, rowData) {
        this.test = ({
            itemID : rowData.id,
            userID : this.props.currentUser.id
        });

        const getItemPicRequest = Object.assign({}, this.test);

        getItemPicURL(getItemPicRequest)
            .then(response => {
                let imageUrls = [];
                let index = 0;
                response.map((urls) => {
                    imageUrls[index] = {
                        original : API_BASE_URL + "/items/image/" + this.props.currentUser.id + "/" + rowData.id + "/" + urls,
                        thumbnail : API_BASE_URL + "/items/image/" + this.props.currentUser.id + "/" + rowData.id + "/" + urls
                    };
                    index++;
                });

                this.setState({
                    imageUrls : imageUrls,
                    currentItem: rowData,
                    editItemMode : true
                });
            }).catch(error => {
        });
    }

    handleViewAction(event, rowData) {
        this.setState({
            showItemView : true,
            selectedItem : rowData.item
        });
    }

    handleViewWishAction(event, rowData) {
        this.setState({
            showItemView : true,
            selectedItem : rowData
        });
    }

    handleRemoveFromWithList(event, rowData) {
        removeFromWishList(rowData.id)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'Item removed from wish list');
                    this.setState({
                        wishAction : true
                    });

                    window.location.reload();
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handleDeleteOrderItemAction(event, rowData) {
        if (rowData.status === 'REJECTED' || rowData.status === 'CANCELED' || rowData.status === 'FINISHED') {
            Alert.error('Can not canceled order');
            return;
        }


        deleteOrderItem(rowData.id)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'Order Item Deleted');
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }

                window.location.reload();
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handleDeleteAction(event, rowData) {

        this.test = ({
            itemID : rowData.id,
            userID : this.props.currentUser.id
        });

        const deleteItemRequest = Object.assign({}, this.test);

        deleteItems(deleteItemRequest)
            .then(response => {
                if (response.success) {
                    Alert.success("You're successfully deleted item picture.");
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }
                this.props.history.push('/');
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handleRejectAction(event, rowData) {
        this.setState({
            loading : true
        });

        rejectOrderItem(rowData.id)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'Order Item Rejected');
                    window.location.reload();
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }

            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handleApproveAction(event, rowData) {
        this.setState({
            loading : true
        });

        approveOrderItem(rowData.id)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'Order Item Rejected');
                    window.location.reload();
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handleFinishAction(event, rowData) {
        if (rowData.status === 'REJECTED' || rowData.status === 'CANCELED' || rowData.status === 'FINISHED') {
            Alert.error('Can not set order to be finished');
            return;
        }

        finishOrderItem(rowData.id)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'Order is Done');
                    // window.location.reload();
                    //

                    this.handelRateAction(event, rowData);
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }

            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    ReviewTenantWatch() {
        return (
            <div className="container tableTop">
                <Grid container spacing={2}>
                {this.state.tenantReviews.map(review => (
                    <Grid item xs={3}>
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
                            <Grid item xs={5}>
                            <StarRatings
                                className="stars"
                                rating={review.rank}
                                starRatedColor="blue"
                                disabled
                                starDimension="20px"
                                starSpacing="1px"
                                numberOfStars={5}
                                name='rankStars'
                                starHoverColor={'rgb(109, 122, 130)'}
                            />
                            </Grid>
                            <Grid item xs={12}>
                            <Typography variant="body2" color="textSecondary" component="p">
                                {review.text}
                            </Typography>
                            </Grid>
                        </CardContent>
                    </Card>
                    </Grid>
                ))}
                </Grid>

                <Link to="/profile"><button className="go-back-btn btn btn-primary goback" type="button">Go Back</button></Link>

            </div>
        );
    }

    MaterialTableDemo() {
        const [state1, setState1] = React.useState({
            columns: [
                { title: 'Manufacturer', field: 'itemSCategory' },
                { title: 'Model', field: 'itemTCategory' },
                { title: 'Description', field: 'description' },
                { title: 'Year', field: 'year_of_production' },
                { title: 'Price Per Day', field: 'pricePerDay' }

            ]
        });

        const tableIcons = {
            Add: forwardRef((props, ref) => <AddBox {...props} ref={ref} />),
            Check: forwardRef((props, ref) => <Check {...props} ref={ref} />),
            Clear: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Delete: forwardRef((props, ref) => <DeleteOutline {...props} ref={ref} />),
            DetailPanel: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            Edit: forwardRef((props, ref) => <Edit {...props} ref={ref} />),
            Export: forwardRef((props, ref) => <SaveAlt {...props} ref={ref} />),
            Filter: forwardRef((props, ref) => <FilterList {...props} ref={ref} />),
            FirstPage: forwardRef((props, ref) => <FirstPage {...props} ref={ref} />),
            LastPage: forwardRef((props, ref) => <LastPage {...props} ref={ref} />),
            NextPage: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            PreviousPage: forwardRef((props, ref) => <ChevronLeft {...props} ref={ref} />),
            ResetSearch: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Search: forwardRef((props, ref) => <Search {...props} ref={ref} />),
            SortArrow: forwardRef((props, ref) => <ArrowUpward {...props} ref={ref} />),
            ThirdStateCheck: forwardRef((props, ref) => <Remove {...props} ref={ref} />),
            ViewColumn: forwardRef((props, ref) => <ViewColumn {...props} ref={ref} />)
        };

        if (this.state.renterItems != null && this.state.renterItems.length > 0) {
            return (
                <div className="MG">
                    <MaterialTable
                        actions={[
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: Edit,
                                tooltip: 'Edit Table',
                                onClick: (event, rowData) =>
                                {
                                    this.handleAction(event, rowData);
                                }
                            }),
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: DeleteOutline,
                                tooltip: 'Delete Item',
                                onClick: (event, rowData) =>
                                {
                                    this.handleDeleteAction(event, rowData);
                                }
                            }),
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: ChevronRight,
                                tooltip: 'View Item',
                                onClick: (event, rowData) =>
                                {
                                    this.handleViewWishAction(event, rowData);
                                }
                            })
                        ]}
                        title="Renter Items"
                        columns={state1.columns}
                        icons={tableIcons}
                        data={this.state.renterItems}
                    />
                </div>
            );
        } else {
            return  (
                <div className="MG">
                    <h4>Renter Items Empty</h4>
                </div>
            );
        }
    }

    MaterialTableTenantWishList() {
        const [state1, setState1] = React.useState({
            columns: [
                { title: 'Manufacturer', field: 'itemSCategory' },
                { title: 'Model', field: 'itemTCategory' },
                { title: 'Owner Name', field: 'ownerName' },
                { title: 'Owner Email', field: 'ownerEmail' },
                { title: 'Description', field: 'description' },
                { title: 'Year', field: 'year_of_production' },
                { title: 'Price Per Day', field: 'pricePerDay' },

            ]
        });

        const tableIcons = {
            Add: forwardRef((props, ref) => <AddBox {...props} ref={ref} />),
            Check: forwardRef((props, ref) => <Check {...props} ref={ref} />),
            Clear: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Delete: forwardRef((props, ref) => <DeleteOutline {...props} ref={ref} />),
            DetailPanel: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            Edit: forwardRef((props, ref) => <Edit {...props} ref={ref} />),
            Export: forwardRef((props, ref) => <SaveAlt {...props} ref={ref} />),
            Filter: forwardRef((props, ref) => <FilterList {...props} ref={ref} />),
            FirstPage: forwardRef((props, ref) => <FirstPage {...props} ref={ref} />),
            LastPage: forwardRef((props, ref) => <LastPage {...props} ref={ref} />),
            NextPage: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            PreviousPage: forwardRef((props, ref) => <ChevronLeft {...props} ref={ref} />),
            ResetSearch: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Search: forwardRef((props, ref) => <Search {...props} ref={ref} />),
            SortArrow: forwardRef((props, ref) => <ArrowUpward {...props} ref={ref} />),
            ThirdStateCheck: forwardRef((props, ref) => <Remove {...props} ref={ref} />),
            ViewColumn: forwardRef((props, ref) => <ViewColumn {...props} ref={ref} />)
        };

        if (this.state.tenantWishList != null && this.state.tenantWishList.length > 0) {
            return (
                <div className="MG">
                    <MaterialTable
                        actions={[
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: DeleteOutline,
                                tooltip: 'Remove from With list',
                                onClick: (event, rowData) =>
                                {
                                    this.handleRemoveFromWithList(event, rowData);
                                }
                            }),
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: ChevronRight,
                                tooltip: 'View Order Item',
                                onClick: (event, rowData) =>
                                {
                                    this.handleViewWishAction(event, rowData);
                                }
                            })
                        ]}
                        title="Wish List"
                        columns={state1.columns}
                        icons={tableIcons}
                        data={this.state.tenantWishList}
                    />
                </div>
            );
        } else {
            return  (
                <div className="MG">
                    <h4>Wish List Empty</h4>
                </div>
            );
        }
    }

    MaterialTableOrdersWaiting() {
        const [state1, setState1] = React.useState({
            columns: [
                { title: 'Tenant User Name', field: 'tenantName' },
                { title: 'Tenant Email', field: 'tenantEmail' },
                { title: 'Total Days', field: 'totalDaysRent' },
                { title: 'total Price', field: 'totalPriceRent' },
                { title: 'Start Day', field: 'rentalStartDay' },
                { title: 'End Day', field: 'rentalEndDay' },


            ]
        });

        const tableIcons = {
            Add: forwardRef((props, ref) => <AddBox {...props} ref={ref} />),
            Check: forwardRef((props, ref) => <Check {...props} ref={ref} />),
            Clear: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Delete: forwardRef((props, ref) => <DeleteOutline {...props} ref={ref} />),
            DetailPanel: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            Edit: forwardRef((props, ref) => <Edit {...props} ref={ref} />),
            Export: forwardRef((props, ref) => <SaveAlt {...props} ref={ref} />),
            Filter: forwardRef((props, ref) => <FilterList {...props} ref={ref} />),
            FirstPage: forwardRef((props, ref) => <FirstPage {...props} ref={ref} />),
            LastPage: forwardRef((props, ref) => <LastPage {...props} ref={ref} />),
            NextPage: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            PreviousPage: forwardRef((props, ref) => <ChevronLeft {...props} ref={ref} />),
            ResetSearch: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Search: forwardRef((props, ref) => <Search {...props} ref={ref} />),
            SortArrow: forwardRef((props, ref) => <ArrowUpward {...props} ref={ref} />),
            ThirdStateCheck: forwardRef((props, ref) => <Remove {...props} ref={ref} />),
            ViewColumn: forwardRef((props, ref) => <ViewColumn {...props} ref={ref} />)
        };

        if (this.state.renterAllOrdersWaiting != null && this.state.renterAllOrdersWaiting.length > 0) {
            return (
                <div className="MG">
                    <MaterialTable
                        actions={[
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: Check,
                                tooltip: 'Approve Order',
                                onClick: (event, rowData) =>
                                {
                                    this.handleApproveAction(event, rowData);
                                }
                            }),
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: Clear,
                                tooltip: 'Reject Order',
                                onClick: (event, rowData) =>
                                {
                                    this.handleRejectAction(event, rowData);
                                }
                            }),
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: GradeIcon,
                                tooltip: 'Tenant Reviews',
                                onClick: (event, rowData) =>
                                {
                                    this.handleWatchTenantReview(event, rowData);
                                }
                            })
                        ]}
                        title="Incoming Orders - Pending"
                        columns={state1.columns}
                        icons={tableIcons}
                        data={this.state.renterAllOrdersWaiting}
                    />
                </div>
            );
        } else {
            return  (
                <div className="MG">
                    <h4>Incoming Orders - Pending list Empty</h4>
                </div>
            );
        }
    }

    handleWatchTenantReview(event, rowData) {
        getAllTenantReviews(rowData.tenantEmail)
            .then(response => {
                this.setState({
                    openTenantReview : true,
                    tenantEmail : rowData.tenantEmail,
                    tenantReviews : response
                });
            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handelRateAction(event, rowData)
    {
        this.setState({
            openD : true,
            tenantEmail : rowData.tenantEmail
        });
    }

    MaterialTableOrdersApproved() {
        const [state1, setState1] = React.useState({
            columns: [
                { title: 'Tenant User Name', field: 'tenantName' },
                { title: 'Tenant Email', field: 'tenantEmail'},
                { title: 'Status', field: 'status'  },
                { title: 'Total Days', field: 'totalDaysRent'  },
                { title: 'Total Price', field: 'totalPriceRent'  },
                { title: 'Start Day', field: 'rentalStartDay' },
                { title: 'End Day', field: 'rentalEndDay'  },

            ]
        });

        const tableIcons = {
            Add: forwardRef((props, ref) => <AddBox {...props} ref={ref} />),
            Check: forwardRef((props, ref) => <Check {...props} ref={ref} />),
            Clear: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Delete: forwardRef((props, ref) => <DeleteOutline {...props} ref={ref} />),
            DetailPanel: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            Edit: forwardRef((props, ref) => <Edit {...props} ref={ref} />),
            Export: forwardRef((props, ref) => <SaveAlt {...props} ref={ref} />),
            Filter: forwardRef((props, ref) => <FilterList {...props} ref={ref} />),
            FirstPage: forwardRef((props, ref) => <FirstPage {...props} ref={ref} />),
            LastPage: forwardRef((props, ref) => <LastPage {...props} ref={ref} />),
            NextPage: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            PreviousPage: forwardRef((props, ref) => <ChevronLeft {...props} ref={ref} />),
            ResetSearch: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Search: forwardRef((props, ref) => <Search {...props} ref={ref} />),
            SortArrow: forwardRef((props, ref) => <ArrowUpward {...props} ref={ref} />),
            ThirdStateCheck: forwardRef((props, ref) => <Remove {...props} ref={ref} />),
            ViewColumn: forwardRef((props, ref) => <ViewColumn {...props} ref={ref} />),
            RateReview: forwardRef((props, ref) => <RateReviewIcon {...props} ref={ref} />),
        };

        if (this.state.renterAllOrdersApproved != null && this.state.renterAllOrdersApproved.length > 0) {
            return (
                <div className="MG">
                    <MaterialTable
                        actions={[
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: Check,
                                tooltip: 'Order Done',
                                onClick: (event, rowData) =>
                                {
                                    this.handleFinishAction(event, rowData);
                                }
                            }),
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: RateReviewIcon,
                                tooltip: 'Rate Order tenant',
                                onClick: (event, rowData) =>
                                {
                                    this.handelRateAction(event, rowData);
                                }
                            })
                        ]}
                        title="Incoming Orders - Approved"
                        columns={state1.columns}
                        icons={tableIcons}
                        data={this.state.renterAllOrdersApproved}
                    />
                </div>
            );
        } else {
            return  (
                <div className="MG">
                    <h4>Incoming Orders - Approved list Empty</h4>
                </div>
            );
        }
    }

    handleRateTenantAction(event) {
        if (this.state.rankStars === 0) {
            Alert.error('Review must have rank');
            return;
        }

        event.preventDefault();

        let rq = {
            tenantEmail : this.state.tenantEmail,
            rankStars : this.state.rankStars,
            textReview : this.state.textReview
        };

        const addItemToCartRequest = Object.assign({}, rq);


        addReviewForTenant(addItemToCartRequest)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'review add');
                    window.location.reload();
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }

            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handleRateItemAction(event) {
        if (this.state.rankStars === 0) {
            Alert.error('Review must have rank');
            return;
        }

        event.preventDefault();

        let rq = {
            ownerEmail : this.state.ownerItemEmail,
            rankStars : this.state.rankStars,
            textReview : this.state.textReview,
            itemID : this.state.itemReviewID
        };

        const addItemToCartRequest = Object.assign({}, rq);


        addReviewForItem(addItemToCartRequest)
            .then(response => {
                if (response.success) {
                    Alert.success((response && response.message) || 'review add');
                    window.location.reload();
                } else {
                    Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                }

            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }

    handleClose() {
        this.setState({
            openD : false,
            rankStars : 0,
            textReview : null
        });
    }

    handleItemClose() {
        this.setState({
            openDItem : false,
            rankStars : 0,
            textReview : null
        });
    }

    handleReviewInputChange(event) {
        const target = event.target;
        const inputName = target.name;
        const inputValue = target.value;

        this.setState({
            [inputName] : inputValue
        });
    }

    changeRating( newRating, name ) {
        this.setState({
            rankStars: newRating
        });
    }


    ReviewDialog() {
        return <div>
            <Dialog open={this.state.openD} onClose={this.handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Review Tenant - {this.state.tenantEmail}</DialogTitle>
                <DialogContent>
                    <Grid container>
                        <Grid item xs={12}>
                            <StarRatings
                                className="stars"
                                rating={this.state.rankStars}
                                starRatedColor="blue"
                                changeRating={this.changeRating}
                                numberOfStars={5}
                                name='rankStars'
                                starHoverColor={'rgb(109, 122, 130)'}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                id="standard-textarea"
                                label="free text"
                                name="textReview"
                                rowsMax="4"
                                placeholder="Placeholder"
                                onChange={this.handleReviewInputChange}
                                multiline
                                margin="normal"
                            />
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.handleClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={this.handleRateTenantAction} color="primary">
                        Send
                    </Button>
                </DialogActions>
            </Dialog>
        </div>;
    }


    ReviewItemDialog() {
        return <div>
            <Dialog open={this.state.openDItem} onClose={this.handleItemClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Review Item Owner - {this.state.ownerItemEmail}</DialogTitle>
                <DialogContent>
                    <Grid container>
                        <Grid item xs={12}>
                            <StarRatings
                                className="stars"
                                rating={this.state.rankStars}
                                starRatedColor="blue"
                                changeRating={this.changeRating}
                                numberOfStars={5}
                                name='rankStars'
                                starHoverColor={'rgb(109, 122, 130)'}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                id="standard-textarea"
                                label="free text"
                                name="textReview"
                                rowsMax="4"
                                placeholder="Placeholder"
                                onChange={this.handleReviewInputChange}
                                multiline
                                margin="normal"
                            />
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.handleItemClose} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={this.handleRateItemAction} color="primary">
                        Send
                    </Button>
                </DialogActions>
            </Dialog>
        </div>;
    }

    MaterialTableOrdersTenant() {
        const [state1, setState1] = React.useState({
            columns: [
                { title: 'Status', field: 'status' },
                { title: 'Manufacturer', field: 'item.itemSCategory' },
                { title: 'Model', field: 'item.itemTCategory' },
                { title: 'Year', field: 'item.year_of_production' },
                { title: 'Total Days', field: 'totalDaysRent' },
                { title: 'Total Price', field: 'totalPriceRent' },
                { title: 'Start Day', field: 'rentalStartDay' },
                { title: 'End Day', field: 'rentalEndDay' },
                { title: 'Owner Name', field: 'item.ownerName' },
                { title: 'Owner Email', field: 'item.ownerEmail' }
            ]
        });

        const tableIcons = {
            Add: forwardRef((props, ref) => <AddBox {...props} ref={ref} />),
            Check: forwardRef((props, ref) => <Check {...props} ref={ref} />),
            Clear: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Delete: forwardRef((props, ref) => <DeleteOutline {...props} ref={ref} />),
            DetailPanel: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            Edit: forwardRef((props, ref) => <Edit {...props} ref={ref} />),
            Export: forwardRef((props, ref) => <SaveAlt {...props} ref={ref} />),
            Filter: forwardRef((props, ref) => <FilterList {...props} ref={ref} />),
            FirstPage: forwardRef((props, ref) => <FirstPage {...props} ref={ref} />),
            LastPage: forwardRef((props, ref) => <LastPage {...props} ref={ref} />),
            NextPage: forwardRef((props, ref) => <ChevronRight {...props} ref={ref} />),
            PreviousPage: forwardRef((props, ref) => <ChevronLeft {...props} ref={ref} />),
            ResetSearch: forwardRef((props, ref) => <Clear {...props} ref={ref} />),
            Search: forwardRef((props, ref) => <Search {...props} ref={ref} />),
            SortArrow: forwardRef((props, ref) => <ArrowUpward {...props} ref={ref} />),
            ThirdStateCheck: forwardRef((props, ref) => <Remove {...props} ref={ref} />),
            ViewColumn: forwardRef((props, ref) => <ViewColumn {...props} ref={ref} />)
        };

        if (this.state.tenantAllOrders != null && this.state.tenantAllOrders.length > 0) {
            return (
                <div className="MG">
                    <MaterialTable
                        actions={[
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: DeleteOutline,
                                tooltip: 'Delete Order Item',
                                onClick: (event, rowData) =>
                                {
                                    this.handleDeleteOrderItemAction(event, rowData);
                                }
                            }),
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: ChevronRight,
                                tooltip: 'View Order Item',
                                onClick: (event, rowData) =>
                                {
                                    this.handleViewAction(event, rowData);
                                }
                            }),
                            rowData => ({  // <-- ***NOW A FUNCTION***
                                icon: RateReviewIcon,
                                tooltip: 'Rate Order Item',
                                onClick: (event, rowData) =>
                                {
                                    if (rowData.status === 'WAITING' || rowData.status === 'APPROVED') {
                                        Alert.error('You can rank the renter after order is on status finished');
                                    } else {
                                        this.setState({
                                            openDItem: true,
                                            ownerItemEmail: rowData.item.ownerEmail,
                                            itemReviewID : rowData.item.id
                                        });
                                    }
                                }
                            })
                        ]}
                        title="Ordered Items"
                        columns={state1.columns}
                        icons={tableIcons}
                        data={this.state.tenantAllOrders}
                    />
                </div>
            );
        } else {
            return  (
                <div className="MG">
                    <h4>Ordered Items Empty</h4>
                </div>
            );
        }
    }
}

class EditItemPicForm extends Component {
    constructor(props) {
        super(props);
        this.img_item = '';

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleFileChange = this.handleFileChange.bind(this);
    }

    handleSubmit(event) {
        // const createFileRequest = Object.assign({}, this.test);
        uploadFileItem(this.img_item,this.props.currentItem.id, this.props.userID).then(response => {
            Alert.success("You're successfully upload new item image.");

        }).catch(error => {
            Alert.success("error upload new item image.");
        });
    }

    handleFileChange(event) {
        this.img_item = event.target.files;
    }


    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <div className="form-item formLabel-items">Select Item Image
                    <input type="file" name="img_item"  multiple="multiple"
                           className="form-control" placeholder="Image Item"
                           onChange={this.handleFileChange}/>
                </div>
                <div className="form-item buttonItem">
                    <Button type="submit" variant="contained" color="primary" className="AddItemB" ><AddBox/>Add</Button>
                </div>
            </form>

        );
    }
}


class EditItemForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            itemID: props.currentItem.id,
            userID: props.userID,
            pricePerDay: props.currentItem.pricePerDay,
            description : props.currentItem.description,
            labels : props.currentItem.labels,
            currency : props.currentItem.currency
        };

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

        this.setState({
            editItemMode : false
        });

        const editItemR = Object.assign({}, this.state);

        editItem(editItemR)
            .then(response => {
                Alert.success("You're successfully edit item info !");

                window.location.reload();

            }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
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
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label className="MuiFormControl-marginNormal">Price Per Day</label>
                        </Grid>
                        <Grid item xs={7}>
                            <input type="text" name="pricePerDay"
                                   className="form-control MuiFormControl-marginNormal" placeholder="Price Per Day"
                                   value={this.state.pricePerDay} onChange={this.handleInputChange} required/>

                        </Grid>
                        <Grid item xs={2}>
                            <TextField
                                id="standard-select-currency"
                                select
                                name="currency"
                                value={this.state.currency}
                                onChange={this.handleInputChange}
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
                    <Grid container>
                        <Grid item xs={3}>
                            <label>Description</label>
                        </Grid>
                        <Grid item xs={9}>
                    <textarea name="description"
                              className="form-controle textA" placeholder="Description"
                              value={this.state.description} onChange={this.handleInputChange}/>
                        </Grid>
                    </Grid>
                </div>
                <div className="form-item">
                    <Grid container>
                        <Grid item xs={3}>
                            <label>Labels</label>
                        </Grid>
                        <Grid item xs={9}>
                            <input type="text" name="labels"
                                   className="form-control" placeholder="Labels"
                                   value={this.state.labels} onChange={this.handleInputChange}/>
                        </Grid>
                    </Grid>
                </div>

                <div className="form-item buttonItem">
                    <Button type="submit" variant="contained" color="primary" className="AddItemB"><Edit/>Save</Button>
                </div>
            </form>
        );
    }
}

export default Profile
