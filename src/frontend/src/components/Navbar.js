import {AppBar, IconButton, Link, styled, Toolbar, Typography} from "@mui/material";
import LogoutIcon from '@mui/icons-material/Logout';
import {getCurrentUserRole, logout} from "../services/auth";
import {useEffect, useState} from "react";

const MyToolbar = styled(Toolbar)({
    justifyContent: "space-between",
})

export const Navbar = () => {
    const userRole = getCurrentUserRole();

    const [admin, setAdmin] = useState(false);

    useEffect(() => {
        if (userRole) {
            setAdmin(userRole.includes("ROLE_ADMIN"));
        }
    }, [userRole]);

    const logoutUser = () => {
        logout();
    }

    return (
        <AppBar position={"sticky"} sx={{bgcolor: "royalblue", marginBottom: 2}}>
            <MyToolbar>
                <Link href={"/"} underline={"none"} color={"inherit"}>
                    <Typography variant={"h5"}>JUNIOR JAVA OFFERS</Typography>
                </Link>
                {
                    admin && (
                        <Typography variant={"h6"}>ADMIN</Typography>
                    )
                }
                {userRole && (
                    <IconButton sx={{color: "white"}} size={"large"} href={"/"} onClick={logoutUser}>
                        <LogoutIcon/>
                    </IconButton>
                )
                }

            </MyToolbar>
        </AppBar>
    )
}