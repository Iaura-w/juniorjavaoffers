import {Box, CircularProgress} from "@mui/material";
import {Offer} from "./Offer";
import {getAllOffers} from "../services/client";
import * as React from "react";
import {useEffect, useState} from "react";
import {Notification} from "./Notification";

export const Home = () => {

    const [openNotification, setOpenNotification] = useState(false);
    const [notificationMessage, setNotificationMessage] = useState("");
    const [notificationType, setNotificationType] = useState("info");

    const handleCloseNotification = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpenNotification(false);
    }

    const [offers, setOffers] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchOffers = () =>
        getAllOffers()
            .then(response => response.json())
            .then(data => {
                console.log(data);
                setOffers(data);
                setLoading(false);
            }).catch(error => {
            error.message.json().then(json => {
                console.log(json);
                setNotificationMessage(json.message);
                setNotificationType("error");
                setOpenNotification(true);
            });
        });

    useEffect(() => {
        console.log("fetched all offers");
        fetchOffers();
    }, []);

    return (
        <Box padding={1} marginY={1} marginX={12}>
            <Offer offers={offers}/>
            <Box display={"flex"} justifyContent={"center"} marginTop={2}>
                {loading && (<CircularProgress/>)}
            </Box>
            {Notification(openNotification, handleCloseNotification, notificationType, notificationMessage)}
        </Box>
    )
}