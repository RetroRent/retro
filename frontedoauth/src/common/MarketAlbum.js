import React ,{Component} from 'react';
import './MarketAlbum.css';
import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import GridListTileBar from '@material-ui/core/GridListTileBar';
import ListSubheader from '@material-ui/core/ListSubheader';
import Button from '@material-ui/core/IconButton';
import Search from '@material-ui/icons/Search';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import StarBorderIcon from '@material-ui/icons/StarBorder';
import StarIcon from '@material-ui/icons/Star';
import InfoIcon from '@material-ui/icons/Info';
import {API_BASE_URL} from "../constants";
import View from '../items/view'
import { Link, Redirect } from 'react-router-dom'
import {removeFromWishList, addToWishList} from '../util/APIUtils'
import Alert from "react-s-alert";

/**
 * The example data is structured as follows:
 *
 * import image from 'path/to/image.jpg';
 * [etc...]
 *
 * const tileData = [
 *   {
 *     img: image,
 *     title: 'Image',
 *     author: 'author',
 *   },
 *   {
 *     [etc...]
 *   },
 * ];
 */

class TitlebarGridList extends Component{
    constructor(props) {
        super(props);
        console.log(props);

        this.state = {
            tileData : props.allItems,
            allItems : props.allItems,
            props : props.props,
            showItemView : false,
            wishAction : false
        };

        this.handleSearch = this.handleSearch.bind(this);
        this.removeFromWishlist = this.removeFromWishlist.bind(this);
        this.addToWishlist = this.addToWishlist.bind(this);
    }

    handleSearch(event)
    {
        const target = event.target;
        const inputValue = target.value.toLowerCase();

        let filter = [];
        let index = 0;
        this.state.allItems.map(tile => {
            if (tile.labels.includes(inputValue) ||
                tile.year_of_production.toString().toLowerCase().includes(inputValue) ||
                tile.itemFCategory.toLowerCase().includes(inputValue) ||
                tile.itemSCategory.toLowerCase().includes(inputValue) ||
                tile.itemTCategory.toLowerCase().includes(inputValue) ||
                tile.ownerName.toLowerCase().includes(inputValue)
            ) {
                filter[index] = tile;
                index++;
            }
        });

        this.setState({
            tileData : filter
        });
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

    render(){
        if (this.state.showItemView) {
            this.state.showItemView = false;
            return (<View {...this.state}/>);
        }

        return (
            <div>
                {this.props.titleShop !== undefined ? (
                    <h2>{this.props.titleShop}</h2>
                ) : (null)}

                <div>
                <Grid container spacing={1} alignItems="flex-end">
                    <Grid item>
                        <Search />
                    </Grid>
                    <Grid item>
                        <TextField id="marketSearch" label="Search" onChange={this.handleSearch}/>
                    </Grid>
                </Grid>
            </div>
            <div className="root">

                <GridList cellHeight={350} className="gridList">
                    <GridListTile key="Subheader" cols={2} style={{ height: 'auto' }}>
                        <ListSubheader component="div">Click for more info</ListSubheader>
                   </GridListTile>
                    {this.state.tileData.map(tile => (
                        <GridListTile key={tile.id}>

                            <img src={API_BASE_URL + "/items/image/" + tile.userID + "/" + tile.id + "/" + tile.imageItemNames[0]} alt={tile.itemSCategory + "-" + tile.itemTCategory} onClick={() => {
                            this.setState({
                                showItemView : true,
                                selectedItem : tile
                            });
                        }}/>

                            <GridListTileBar
                                title={tile.itemSCategory}
                                subtitle={<span>Renter - {tile.ownerName} </span>}
                                actionIcon={[
                                    <Button aria-label={`info`} className="icon" onClick={() => {
                                            this.setState({
                                                showItemView : true,
                                            selectedItem : tile
                                        });
                                    }}>
                                        <InfoIcon />
                                    </Button>,
                                    tile.star ? (
                                        <Button aria-label={`star`} className="icon" onClick={() => {
                                            this.removeFromWishlist(tile)
                                        }}>
                                            <StarIcon />
                                        </Button>

                                    ) : (
                                        <Button aria-label={`star`} className="icon" onClick={() => {
                                            this.addToWishlist(tile)
                                        }}>
                                            <StarBorderIcon />
                                        </Button>
                                    )
                                ]}
                            />
                        </GridListTile>
                    ))}
                </GridList>
            </div>
            </div>
        );
    }
}

export default TitlebarGridList;