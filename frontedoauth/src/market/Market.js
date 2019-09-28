import React, { Component, forwardRef } from 'react';
import { Link, Redirect } from 'react-router-dom'
import './market.css';
import LoadingIndicator from '../common/LoadingIndicator';
import {getAllItems} from "../util/APIUtils";
import TitlebarGridList from '../common/MarketAlbum';

import 'react-datepicker/dist/react-datepicker.css'

class Market extends Component {
    constructor(props) {
        super(props);
        console.log(props);

        this.state = {
            loading: false,
            allItems : null,
            props : props
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

        getAllItems(userID).then(response => {
            this.setState({
                    allItems : response,
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
            <MarketView {...this.state}/>
        );
    }
}

class MarketView extends Component {
    constructor(props) {
        super(props);

        this.state = {
            allItems : this.props.allItems,
            filter : this.props.allItems,
            props : this.props.props
        };
    }

    render() {
        return(
            <div>
                <div className="marketTitle">
                    <TitlebarGridList {...this.state}/>
                </div>
            </div>

        );
    }
}

export default Market