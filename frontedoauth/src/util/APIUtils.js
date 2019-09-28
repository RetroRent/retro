import { API_BASE_URL, ACCESS_TOKEN } from '../constants';
import axios, { post } from 'axios';
import Alert from "react-s-alert";

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    
    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response => 
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

const requestFile = (file, itemID, userID) => {

    const formData = new FormData();

    for(let x = 0; x<file.length; x++) {
        formData.append('myImage', file[x])
    }

    formData.append('itemID', itemID);
    formData.append('userID', userID);

   return axios.post(API_BASE_URL + "/items/uploadFile",formData)
        .then(function (response) {
            Alert.success("You're successfully upload new item image.");
    });
};

export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function getPartLogin() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/getPartLogin",
        method: 'GET'
    });
}

export function getMainCategory() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/items/getMainCategoryOptions",
        method: 'GET'
    });
}

export function getSecCategory(request_u) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/items/getSecondaryCategoryOptions",
        method: 'POST',
        body: JSON.stringify(request_u)
    });
}

export function getThirdCategory(re) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/items/getThirdCategoryOptions",
        method: 'POST',
        body: JSON.stringify(re)
    });
}

export function getRenterAllItems() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/renter/getAllItems",
        method: 'GET'
    });
}

export function getRenterAllOrders() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/renter/getAllOrders",
        method: 'GET'
    });
}


export function getAllItems(userID) {
    return request({
        url: API_BASE_URL + "/items/getAllItems/" + userID,
        method: 'GET'
    });
}


export function getAllItemsForOwner(userID, ownerEmail) {
    let rq = {
        ownerEmail : ownerEmail
    };


    const signUpRequest = Object.assign({}, rq);

    return request({
        url: API_BASE_URL + "/items/getAllItemsOfOwner/" + userID,
        method: 'POST',
        body: JSON.stringify(signUpRequest)
    });
}

export function addReviewForTenant(rq) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/renter/reviewTenant",
        method: 'POST',
        body: JSON.stringify(rq)
    });
}

export function addReviewForItem(rq) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/tenant/reviewItem",
        method: 'POST',
        body: JSON.stringify(rq)
    });
}


export function getAllMessageLength() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/getAllMessageLength",
        method: 'GET'
    });
}

export function getAllMessage() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/getAllMessage",
        method: 'GET'
    });
}

export function clearMessage(messageID) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/clearMessage/" + messageID,
        method: 'GET'
    });
}

export function getAllTenantReviews(tenantEmail) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/tenant/allreview/" + tenantEmail,
        method: 'GET'
    });
}

export function edit(editRequest) {
    return request({
        url: API_BASE_URL + "/user/edit",
        method: 'POST',
        body: JSON.stringify(editRequest)
    });
}

export function purchase() {
    return request({
        url: API_BASE_URL + "/tenant/purchase",
        method: 'GET'
    });
}

export function editItem(editRequest) {
    return request({
        url: API_BASE_URL + "/items/editItem",
        method: 'POST',
        body: JSON.stringify(editRequest)
    });
}

export function getItemPicURL(getItemPicRequest) {
    return request({
        url: API_BASE_URL + "/items/imageURL/" + getItemPicRequest.userID + "/" + getItemPicRequest.itemID,
        method: 'GET'
    });
}

export function rejectOrderItem(orderItemID) {
    return request({
        url: API_BASE_URL + "/renter/rejectOrder/" + orderItemID,
        method: 'GET'
    });
}

export function deleteOrderItem(orderItemID) {
    return request({
        url: API_BASE_URL + "/tenant/deleteOrder/" + orderItemID,
        method: 'GET'
    });
}

export function addToWishList(ItemID) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/tenant/addToWishList/" + ItemID,
        method: 'GET'
    });
}

export function getTenantWish() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/tenant/getWishList/",
        method: 'GET'
    });
}

export function removeFromWishList(ItemID) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/tenant/removeFromWishList/" + ItemID,
        method: 'GET'
    });
}

export function getTenantOrders() {
    return request({
        url: API_BASE_URL + "/tenant/getTenantOrders/",
        method: 'GET'
    });
}

export function approveOrderItem(orderItemID) {
    return request({
        url: API_BASE_URL + "/renter/approveOrder/" + orderItemID,
        method: 'GET'
    });
}

export function finishOrderItem(orderItemID) {
    return request({
        url: API_BASE_URL + "/renter/finishOrder/" + orderItemID,
        method: 'GET'
    });
}

export function deleteItems(deleteItemsRequest) {
    return request({
        url: API_BASE_URL + "/renter/deleteItem",
        method: 'POST',
        body: JSON.stringify(deleteItemsRequest)
    });
}

export function deleteItemsPicture(deleteItemsRequest) {
    return request({
        url: API_BASE_URL + "/renter/deleteItemPic/" + deleteItemsRequest.imageName,
        method: 'POST',
        body: JSON.stringify(deleteItemsRequest)
    });
}

export function getItemsCart() {
    return request({
        url: API_BASE_URL + "/tenant/getAllItemsCart",
        method: 'GET'
    });
}

export function deleteOrderItemCart(deleteReq) {
    return request({
        url: API_BASE_URL + "/tenant/deleteItemCart",
        method: 'POST',
        body: JSON.stringify(deleteReq)
    });
}

export function getCartLength() {
    return request({
        url: API_BASE_URL + "/tenant/itemCartLength",
        method: 'GET'
    });
}

export function addItemsToCart(addItemsRequest) {
    return request({
        url: API_BASE_URL + "/tenant/addItemToCart",
        method: 'POST',
        body: JSON.stringify(addItemsRequest)
    });
}


export function ItemsAvailabel(itemsAvailabelR) {
    return request({
        url: API_BASE_URL + "/items/getAvailable",
        method: 'POST',
        body: JSON.stringify(itemsAvailabelR)
    });
}

export function createItems(createItemsRequest) {
    return request({
        url: API_BASE_URL + "/items/createItem",
        method: 'POST',
        body: JSON.stringify(createItemsRequest)
    });
}

export function uploadFileItem(file, itemID, userID) {
    return requestFile(file, itemID, userID);
}

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/login",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function registrationEnd(registrationEndRequest) {
    return request({
        url: API_BASE_URL + "/auth/registrationEnd",
        method: 'POST',
        body: JSON.stringify(registrationEndRequest)
    });
}