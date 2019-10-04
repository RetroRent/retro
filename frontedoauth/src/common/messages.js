import React, { Component } from 'react';
import { Link, Redirect } from 'react-router-dom'
import './messages.css';
import 'react-datepicker/dist/react-datepicker.css'
import Grid from '@material-ui/core/Grid';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import "bootstrap/dist/css/bootstrap.css";
import LoadingIndicator from "./LoadingIndicator";
import {getAllMessage, clearMessage} from "../util/APIUtils";
import Alert from "react-s-alert";
import Clear from '@material-ui/icons/Clear';
import Button from '@material-ui/core/IconButton';
import _ from 'lodash'
class Messages extends Component {
    constructor(props) {
        super(props);
        console.log(props);

        this.state = {
            loading : false,
            messages : null
        };


        this.getMessages = this.getMessages.bind(this);
    }

    componentDidMount() {
        if (this.props.currentUser !== undefined && this.props.currentUser !== null && (this.props.currentUser.role.role === 'BOTH' || this.props.currentUser.role.role === 'TENANT')) {
            this.getMessages();
        }
    }

    getMessages() {
        this.setState({
            loading : true
        });

        getAllMessage().then(response => {
            const sortedBy = _.orderBy(response, ['id'], ['desc']);
            this.setState({
                messages : sortedBy,
                loading : false
            });
        }).catch(error => {
            this.setState({
                messages : null,
                loading : false
            });
            Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
        });
    }


    render() {
        if (this.state.loading) {
            return (
                <LoadingIndicator/>
            );
        }

        if (this.state.messages == null) {
            return (
                <div className="border-messages">
                    <h2>Message List Empty</h2>
                </div>
            );
        }

        return (
            <div className="border-messages">
                <Grid container className="des">
                    <Grid item xs={12}>
                        <h2>Notifications</h2>
                        <Grid container spacing={2}>
                            {this.state.messages.map(message => (
                                <Grid item xs={12}>
                                    <Card className="card">
                                        <CardHeader
                                            title={message.messageTitle}
                                            subheader={message.date.toString()}
                                        />
                                        <CardContent>
                                            <Grid item xs={12}>
                                                <Typography variant="body2" color="textSecondary" component="p">
                                                    {message.text}
                                                </Typography>
                                            </Grid>
                                            <Button aria-label={`clear`} className="iconMessage" onClick={() => {
                                                clearMessage(message.id).then(response => {
                                                    window.location.reload();
                                                }).catch(error => {
                                                    Alert.error((error && error.message) || 'Oops! Something went wrong. Please try again!');
                                                });
                                            }}>
                                                <Clear />
                                            </Button>
                                        </CardContent>
                                    </Card>
                                </Grid>
                            ))}
                        </Grid>
                    </Grid>
                </Grid>

            </div>
        );
    }
}

export default Messages