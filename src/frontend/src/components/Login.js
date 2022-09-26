import * as React from 'react';
import {useState} from 'react';
import {
    Box,
    Card,
    CardContent,
    CardHeader,
    FormControl,
    IconButton,
    InputAdornment,
    InputLabel,
    OutlinedInput
} from "@mui/material";
import LoadingButton from '@mui/lab/LoadingButton';
import {Visibility, VisibilityOff} from "@mui/icons-material";
import {login} from "../services/auth";
import {Link, useNavigate} from "react-router-dom";
import {Notification} from "./Notification";

export const Login = () => {
    const navigate = useNavigate();

    const [values, setValues] = useState({
        username: '',
        password: '',
        showPassword: false,
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
        setValues({...values, [prop]: event.target.value});
    };

    const handleClickShowPassword = () => {
        setValues({
            ...values,
            showPassword: !values.showPassword,
        });
    };

    const handleLogin = (e) => {
        e.preventDefault();

        setLoading(true);

        login(values.username, values.password)
            .then(() => {
                    setNotificationMessage("Successful login");
                    setNotificationType("success");
                    setOpenNotification(true);
                    navigate("/");
                    window.location.reload();
                }
            ).catch(error => {
            error.message.json().then(json => {
                console.log(json);
                setNotificationMessage("Invalid login or password. Please try again.");
                setNotificationType("error");
                setOpenNotification(true);
            });
        }).finally(() => {
            setLoading(false);
        });
    }

    return (
        <Box paddingX={2}
             paddingY={20}
             margin={2}
             display={"flex"}
             justifyContent={"center"}
             component={"form"}
             onSubmit={handleLogin}>

            {Notification(openNotification, handleCloseNotification, notificationType, notificationMessage)}

            <Card sx={{padding: 2, maxWidth: 450}}>
                <CardHeader title={"Login"} sx={{marginX: 3}}/>
                <CardContent>
                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <InputLabel htmlFor="username">Username</InputLabel>
                        <OutlinedInput
                            id="Username"
                            label="Username"
                            onChange={handleChange("username")}
                            required={true}
                        />
                    </FormControl>
                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <InputLabel htmlFor="password">Password</InputLabel>
                        <OutlinedInput
                            id="password"
                            type={values.showPassword ? 'text' : 'password'}
                            value={values.password}
                            onChange={handleChange('password')}
                            required={true}
                            endAdornment={
                                <InputAdornment position="end">
                                    <IconButton aria-label="toggle password visibility"
                                                onClick={handleClickShowPassword}
                                                edge="end">
                                        {values.showPassword ? <VisibilityOff/> : <Visibility/>}
                                    </IconButton>
                                </InputAdornment>
                            }
                            label="Password"
                        />
                    </FormControl>
                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <LoadingButton loading={loading}
                                       variant="contained"
                                       type={"submit"}>
                            Login
                        </LoadingButton>
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <Link to={"/signup"}>
                            Create account
                        </Link>
                    </FormControl>
                </CardContent>

            </Card>
        </Box>
    )
}
