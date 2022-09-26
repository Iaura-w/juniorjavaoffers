import fetch from "unfetch";
import {authHeader} from "./authHeader";

const checkStatus = response => {
    if (response.ok) {
        return response;
    }
    // convert non-2xx HTTP responses into errors:
    const error = new Error(response.statusText);
    error.message = response;
    return Promise.reject(error);
}

export const getAllOffers = () =>
    fetch("api/offers", {
        method: "GET",
        headers: {
            "Authorization": authHeader()
        }
    }).then(checkStatus);

export const deleteOffer = (id) =>
    fetch(`api/offers/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": authHeader()
        }
    }).then(checkStatus);

export const addOffer = (offer) =>
    fetch("api/offers/", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": authHeader()
        },
        body: JSON.stringify(offer)
    }).then(checkStatus);