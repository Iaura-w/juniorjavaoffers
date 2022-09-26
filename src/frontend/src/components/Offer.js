import {Box, Button, Card, CardContent, IconButton, Typography} from "@mui/material";
import LinkIcon from '@mui/icons-material/Link';
import AddCircleIcon from "@mui/icons-material/AddCircle";
import DeleteIcon from '@mui/icons-material/Delete';
import * as React from "react";
import {useEffect, useState} from "react";
import {getCurrentUserRole} from "../services/auth";
import {DeleteOfferDialog} from "./DeleteOfferDialog";
import {deleteOffer} from "../services/client";
import {Notification} from "./Notification";
import {useNavigate} from "react-router-dom";

export const Offer = ({offers}) => {
    const navigate = useNavigate();
    const userRole = getCurrentUserRole();

    const [admin, setAdmin] = useState(false);

    useEffect(() => {
        if (userRole) {
            setAdmin(userRole.includes("ROLE_ADMIN"));
        }
    }, [userRole]);

    const [openNotification, setOpenNotification] = useState(false);
    const [notificationMessage, setNotificationMessage] = useState("");
    const [notificationType, setNotificationType] = useState("info");

    const [openDeleteAlert, setOpenDeleteAlert] = useState(false);

    const handleCloseNotification = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpenNotification(false);
    }

    const handleOpenDeleteAlert = () => {
        setOpenDeleteAlert(true);
    };

    const handleCloseDeleteAlert = () => {
        setOpenDeleteAlert(false);
    };

    const handleAddOffer = () => {
        navigate("/add-offer");
    };

    const handleDeleteOffer = (offerId) =>
        deleteOffer(offerId)
            .then(() => {
                setOpenDeleteAlert(false);
                setNotificationMessage("Deleted offer with id: " + offerId);
                setNotificationType("success");
                setOpenNotification(true);
                console.log("Deleted offer with id: " + offerId);
            }).catch(error => {
            error.message.json().then(json => {
                console.log(json);
                setNotificationMessage(json.message);
                setNotificationType("error");
                setOpenNotification(true);
                console.log(json.message);
            });
        });

    const renderData = offers => {
        if (offers.length <= 0) {
            return (
                <Card sx={{marginX: 4, marginY: 2}}>
                    <CardContent>
                        <Box display={"flex"} justifyContent={"center"}>
                            <Typography variant="h6">
                                no data
                            </Typography>
                        </Box>
                    </CardContent>
                </Card>
            );
        } else {
            return (
                offers.map((offer) => (
                    <Card sx={{marginX: 4, marginY: 2}} key={offer.id}>
                        <CardContent sx={{bgcolor: "ghostwhite"}}>
                            <Box sx={{display: {xs: "block", sm: "flex"}}} justifyContent={"space-between"}>
                                <Typography variant="h6">
                                    {offer.company}
                                </Typography>
                                <Typography variant="h6">
                                    {offer.title}
                                </Typography>
                                <Typography variant="h6">
                                    {offer.salary}
                                </Typography>
                                <Button endIcon={<LinkIcon/>} size="large" href={offer.offerUrl}>GO TO OFFER</Button>
                                {admin && (
                                    <IconButton color={"error"}
                                                onClick={handleOpenDeleteAlert}>
                                        <DeleteIcon/>
                                    </IconButton>)}
                                {DeleteOfferDialog(openDeleteAlert, handleCloseDeleteAlert, () => handleDeleteOffer(offer.id))}
                                {Notification(openNotification, handleCloseNotification, notificationType, notificationMessage)}
                            </Box>
                        </CardContent>
                    </Card>
                ))
            );
        }
    }
    return (
        <Box marginX={6} minWidth={340}>
            {admin && (
                <Button sx={{marginBottom: 4, marginX: 4}}
                        color={"success"}
                        variant="contained"
                        endIcon={<AddCircleIcon/>}
                        onClick={handleAddOffer}>
                    Add offer
                </Button>)}
            <Box sx={{display: {xs: "none", sm: "none", md: "flex"}}}
                 justifyContent={"space-between"}
                 marginX={6}
                 color={"darkgrey"}>
                <Typography variant="subtitle1">
                    Company
                </Typography>
                <Typography variant="subtitle1">
                    Title
                </Typography>
                <Typography variant="subtitle1">
                    Salary
                </Typography>
                <Typography variant="subtitle1">
                </Typography>
            </Box>
            {renderData(offers)}
        </Box>
    );
}