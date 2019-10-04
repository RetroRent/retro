import React, { Component , forwardRef} from 'react';
import { Link, Redirect } from 'react-router-dom'
import './cart.css';
import 'react-datepicker/dist/react-datepicker.css'
import {deleteOrderItemCart, getItemsCart, purchase} from "../../util/APIUtils";
import LoadingIndicator from "../../common/LoadingIndicator";
import AddBox from '@material-ui/icons/AddBox';
import ArrowUpward from '@material-ui/icons/ArrowUpward';
import MaterialTable from 'material-table';
import Check from '@material-ui/icons/Check';
import ChevronLeft from '@material-ui/icons/ChevronLeft';
import ChevronRight from '@material-ui/icons/ChevronRight';
import Clear from '@material-ui/icons/Clear';
import FilterList from '@material-ui/icons/FilterList';
import FirstPage from '@material-ui/icons/FirstPage';
import LastPage from '@material-ui/icons/LastPage';
import Remove from '@material-ui/icons/Remove';
import SaveAlt from '@material-ui/icons/SaveAlt';
import Search from '@material-ui/icons/Search';
import ViewColumn from '@material-ui/icons/ViewColumn';
import Alert from "react-s-alert";

import AddShoppingCartIcon from '@material-ui/icons/AddShoppingCart';
import Button from '@material-ui/core/IconButton';
import Edit from "@material-ui/icons/Edit";
import DeleteOutline from "@material-ui/icons/DeleteOutline";
import View from "../../items/view";

class Cart extends Component {
    constructor(props) {
        super(props);
        console.log(props);

        this.state = {
            loading : false,
            cartList : null,
            showItemView : false,
            props : this.props,
        };

        MaterialTableDemo = MaterialTableDemo.bind(this);
        handleAction = handleAction.bind(this);
        handleViewAction = handleViewAction.bind(this);

        this.getTenantCartItems = this.getTenantCartItems.bind(this);
    }

    componentDidMount() {
        this.getTenantCartItems();
    }

    getTenantCartItems()
    {
           this.setState({
               loading : true
           });

           getItemsCart().then(response => {
               this.setState({
                   loading : false,
                   cartList : response
               });
           }).catch(error => {
               Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
               this.setState({
                   loading : false,
                   cartList : null
               });
           });
    }

    render() {
        if (this.state.loading) {
            return (<LoadingIndicator/>);
        }

        if (this.state.showItemView) {
            this.state.showItemView = false;
            return (
                <div className="marketTitle">
                    <View {...this.state}/>
                </div>
            );
        }


        if (this.props.currentUser.role.role === 'RENTER') {
            return <Redirect
                to={{
                    pathname: "/profile",
                    state: { from: this.props.location }
                }}/>;
        }

        return (
            <div className="container">
                <MaterialTableDemo {...this.state.cartList}/>
                <Button aria-label={`buy`} variant="contained" color="primary" onClick={() => {
                    this.setState({
                        loading : true
                    });
                    purchase().then(response => {
                        if (response.success) {
                            Alert.success('Order created and want for approval by renters!');

                            response.paypalme.map(url => {
                                let randomnumber = Math.floor((Math.random() * 100) + 1);
                                window.open(url, "_blank",'PopUp',randomnumber, 'resizable=yes')
                            });
                            this.state.loading = false;
                            window.location.reload();
                        } else {
                            Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
                        }
                    })
                        .catch(error => {
                        Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
                    });

                }}>
                    <AddShoppingCartIcon /> Place an Order
                </Button>
            </div>
        );
    }
}

function handleViewAction(event, rowData) {
    this.setState({
        showItemView : true,
        selectedItem : rowData.item
    });
}

function handleAction(event, rowData) {
    this.test = ({
        orderItemID : rowData.id,
        userID : this.props.currentUser.id
    });

    const deleteOrderItemRe = Object.assign({}, this.test);

    deleteOrderItemCart(deleteOrderItemRe)
        .then(response => {
            if (response.success) {
                Alert.success('Item Order Remove from Cart!');
            } else {
                Alert.error((response && response.message) || 'Oops! Something went wrong. Please try again!');
            }
            window.location.reload();
            this.props.history.push('/cart');
        }).catch(error => {
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
    });
}


function MaterialTableDemo() {
    const [state, setState] = React.useState({
        columns: [

            { title: 'Renter', field: 'item.ownerName' },
            { title: 'Manufacturer', field: 'item.itemSCategory' },
            { title: 'Model', field: 'item.itemTCategory' },
            { title: 'Year', field: 'item.year_of_production' },
            { title: 'Total Days', field: 'totalDaysRent' },
            { title: 'total Price', field: 'totalPriceRent' },
            { title: 'Start Day', field: 'rentalStartDay' },
            { title: 'End Day', field: 'rentalEndDay' }
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

    if (this.state.cartList != null && this.state.cartList.length > 0) {
        return (
            <MaterialTable
                actions={[
                    rowData => ({  // <-- ***NOW A FUNCTION***
                        icon: DeleteOutline,
                        tooltip: 'Remove Item',
                        onClick: (event, rowData) =>
                        {
                            handleAction(event, rowData);
                        }
                    }),
                    rowData => ({  // <-- ***NOW A FUNCTION***
                        icon: ChevronRight,
                        tooltip: 'View Order Item',
                        onClick: (event, rowData) =>
                        {
                            handleViewAction(event, rowData);
                        }
                    })
                ]}
                title="Cart Items"
                columns={state.columns}
                icons={tableIcons}
                data={this.state.cartList}
            />
        );
    } else {
        return  (
            <div>
                <h2>Cart Empty</h2>
            </div>
        );
    }
}

export default Cart