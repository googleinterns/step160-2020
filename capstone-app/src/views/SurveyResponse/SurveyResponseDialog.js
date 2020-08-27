import Button from '@material-ui/core/Button';
import CloseIcon from '@material-ui/icons/Close';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import IconButton from '@material-ui/core/IconButton';
import {Radar} from 'react-chartjs-2';
import React from 'react';

export default function SurveyResponseDialog(props) {
  const [open, setOpen] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
      <Button variant="outlined" color="primary" onClick={handleClickOpen}>
        Details
      </Button>
      <Dialog onClose={handleClose} aria-labelledby="customized-dialog-title" open={open}>
        <DialogTitle id="customized-dialog-title" onClose={handleClose}>
          <h6>Survey Response Details</h6>
          <IconButton aria-label="close" onClick={handleClose}>
          <CloseIcon />
          </IconButton>
        </DialogTitle>
        <DialogContent dividers>
          <p>{'Survey submitted on ' + props.date + ' from ' + props.response.city + ', ' + props.response.state + '.'}</p>
          <p>{'Text submitted: "' + props.response.text + '"'}</p>
          <Radar data={props.data}/>
        </DialogContent>
      </Dialog>
    </div>
  );
}