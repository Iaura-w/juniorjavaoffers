import * as React from 'react';
import {useState} from 'react';
import {Box, Button, Card, CardContent, CardHeader, FormControl, InputLabel, OutlinedInput} from "@mui/material";
import LoadingButton from '@mui/lab/LoadingButton';
import {Notification} from "./Notification";
import {addOffer} from "../services/client";
import {useNavigate} from "react-router-dom";

export const AddOffer = () => {
    const navigate = useNavigate();

    const [offer, setOffer] = useState({
        title: "",
        company: "",
        salary: "",
        offerUrl: "",
    });

    const [loading, setLoading] = useState(false);

    const [openNotification, setOpenNotification] = useState(false);
    const [notificationMessage, setNotificationMessage] = useState("");
    const [notificationType, setNotificationType] = useState("info");

    const handleCloseNotification = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpenNotification(false);
    }

    const handleChange = (prop) => (event) => {
        setOffer({...offer, [prop]: event.target.value});
    };

    const handleAddingOffer = (e) => {
        e.preventDefault();

        setLoading(true);
        console.log(JSON.stringify(offer))

        addOffer(offer)
            .then(async () => {
                    setNotificationMessage("Offer added");
                    setNotificationType("success");
                    setOpenNotification(true);
                    console.log("Offer added");
                }
            ).catch(error => {
            error.message.json().then(json => {
                console.log(json);
                setNotificationMessage(json.message);
                setNotificationType("error");
                setOpenNotification(true);
            });
        }).finally(() => {
            setLoading(false);
        });
    }

    const handleReturn = () => {
        navigate("/");
    }

    return (
        <Box paddingX={2}
             paddingY={20}
             margin={2}
             display={"flex"}
             justifyContent={"center"}
             component={"form"}
             onSubmit={handleAddingOffer}>

            {Notification(openNotification, handleCloseNotification, notificationType, notificationMessage)}

            <Card sx={{padding: 2, maxWidth: 450}}>
                <CardHeader title={"Add new offer"} sx={{marginX: 3}}/>
                <CardContent>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <InputLabel htmlFor="title">Title</InputLabel>
                        <OutlinedInput
                            id="title"
                            label="Title"
                            onChange={handleChange("title")}
                            required={true}
                        />
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <InputLabel htmlFor="title">Company</InputLabel>
                        <OutlinedInput
                            id="company"
                            label="Company"
                            onChange={handleChange("company")}
                            required={true}
                        />
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <InputLabel htmlFor="title">Salary</InputLabel>
                        <OutlinedInput
                            id="salary"
                            label="Salary"
                            onChange={handleChange("salary")}
                            required={true}
                        />
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <InputLabel htmlFor="title">Offer URL</InputLabel>
                        <OutlinedInput
                            id="offerUrl"
                            label="Offer URL"
                            onChange={handleChange("offerUrl")}
                            required={true}
                        />
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <LoadingButton loading={loading}
                                       variant="contained"
                                       type={"submit"}>
                            Add offer
                        </LoadingButton>
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '40%'}}>
                        <Button
                            color={"warning"}
                            variant="contained"
                            onClick={handleReturn}>
                            Go back
                        </Button>
                    </FormControl>
                </CardContent>

            </Card>
        </Box>
    )
}