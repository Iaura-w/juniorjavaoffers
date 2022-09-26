import {Button, Dialog, DialogActions, DialogTitle} from "@mui/material";

export const DeleteOfferDialog = (open, handleClose, handleConfirm) => {
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
        >
            <DialogTitle id="alert-dialog-title">
                {"Do you want delete this offer?"}
            </DialogTitle>
            <DialogActions>
                <Button onClick={handleClose}>No</Button>
                <Button onClick={handleConfirm} autoFocus>
                    Yes
                </Button>
            </DialogActions>
        </Dialog>
    );
}