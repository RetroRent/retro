import React, { Component, forwardRef } from 'react';
import { Link, Redirect } from 'react-router-dom'
import './ViewRenterPage.css'
import {getAllItemsForOwner} from "../../util/APIUtils";
import TitlebarGridList from '../../common/MarketAlbum';

import LoadingIndicator from '../../common/LoadingIndicator';

class ViewRenterPage extends Component {
    constructor(props) {
        super(props);
        console.log(props);

        this.state = {
            loading: false,
            allItems : null,
            props : props,
            titleShop : this.props.selectedItem.ownerName
        };

        this.getAllItemsF = this.getAllItemsF.bind(this);
    }

    componentDidMount() {
        this.getAllItemsF();
    }

    getAllItemsF() {
        this.setState({
            loading : true
        });

        let userID;
        if (this.props.currentUser === undefined || this.props.currentUser === null) {
            userID = -1;
        } else {
            userID = this.props.currentUser.id;
        }

        getAllItemsForOwner(userID, this.props.selectedItem.ownerEmail).then(response => {
            this.setState({
                allItems : response,
                filter : response,
                loading : false
            });
        }).catch(error => {
            this.setState({
                allItems : null,
                loading : false
            });
        });
    }



    render() {
        if (this.state.loading) {
            return (<LoadingIndicator/>);
        }

        if (this.state.allItems == null) {
            return  (
                <div className="marketTitle">
                    <h2>Market Empty</h2>
                </div>
            );
        }

        return (
            <div>
                <div>
                    <TitlebarGridList {...this.state}/>
                </div>
            </div>
        );
    }
}

export default ViewRenterPage;
