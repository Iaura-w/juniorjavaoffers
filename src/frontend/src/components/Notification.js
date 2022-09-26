import {Alert, Snackbar} from "@mui/material";
import * as React from "react";

export const Notification = (open, close, type, message) => {
    return (
        <Snackbar open={open} autoHideDuration={6000} onClose={close}
                  anchorOrigin={{vertical: 'top', horizontal: 'right'}}>
            <Alert onClose={close} severity={type} sx={{width: '100%'}}>
                {message}
            </Alert>
        </Snackbar>);
}