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
import {register} from "../services/auth";
import {Link, useNavigate} from "react-router-dom";
import {Notification} from "./Notification";

export const Register = () => {
    const navigate = useNavigate();

    const [user, setUser] = useState({
        username: "",
        password: "",
        confirmPassword: "",
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
        setUser({...user, [prop]: event.target.value});
    };

    const handleClickShowPassword = () => {
        setUser({
            ...user,
            showPassword: !user.showPassword,
        });
    };

    const handleRegister = (e) => {
        e.preventDefault();

        if (user.password !== user.confirmPassword) {
            setNotificationMessage("Passwords are different");
            setNotificationType("error");
            setOpenNotification(true);
        } else {
            setLoading(true);

            register(user.username, user.password)
                .then(() => {
                        setNotificationMessage("Successful register");
                        setNotificationType("success");
                        setOpenNotification(true);
                        navigate("/");
                        window.location.reload();
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
    }

    return (
        <Box paddingX={2}
             paddingY={20}
             margin={2}
             display={"flex"}
             justifyContent={"center"}
             component={"form"}
             onSubmit={handleRegister}>

            {Notification(openNotification, handleCloseNotification, notificationType, notificationMessage)}

            <Card sx={{padding: 2, maxWidth: 450}}>
                <CardHeader title={"Create an account"} sx={{marginX: 3}}/>
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
                            type={user.showPassword ? 'text' : 'password'}
                            value={user.password}
                            onChange={handleChange('password')}
                            required={true}
                            endAdornment={
                                <InputAdornment position="end">
                                    <IconButton aria-label="toggle password visibility"
                                                onClick={handleClickShowPassword}
                                                edge="end">
                                        {user.showPassword ? <VisibilityOff/> : <Visibility/>}
                                    </IconButton>
                                </InputAdornment>
                            }
                            label="Password"
                        />
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <InputLabel htmlFor="confirmPassword">Confirm password</InputLabel>
                        <OutlinedInput
                            id="confirmPassword"
                            type={user.showPassword ? 'text' : 'password'}
                            value={user.confirmPassword}
                            onChange={handleChange('confirmPassword')}
                            required={true}
                            label="ConfirmPassword"
                        />
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <LoadingButton loading={loading}
                                       variant="contained"
                                       type={"submit"}>
                            Create account
                        </LoadingButton>
                    </FormControl>

                    <FormControl sx={{margin: 1, width: '100%'}} variant="outlined">
                        <Link to={"/"}>
                            Login
                        </Link>
                    </FormControl>
                </CardContent>

            </Card>
        </Box>
    )
}