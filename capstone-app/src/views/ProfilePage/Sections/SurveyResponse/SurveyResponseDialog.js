import Button from '@material-ui/core/Button';
import CloseIcon from '@material-ui/icons/Close';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import IconButton from '@material-ui/core/IconButton';
import React from 'react';
import {Radar} from 'react-chartjs-2';

/** 
 * Function component representing one survey response in a dialog, showing all its 
 * information (city, state, text, timestamp, and feelings in a radar graph).
 */
export default function SurveyResponseDialog(props) {
  const [open, setOpen] = React.useState(false);

  /** Opens the dialog. */
  const handleClickOpen = () => {
    setOpen(true);
  };
  
  /** Closes the dialog. */
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
          <p>{'Survey submitted on ' + props.date.toString() + ' from ' + props.response.city + ', ' + props.response.state + '.'}</p>
          <p>{'Text submitted: "' + props.response.text + '"'}</p>
          <Radar data={props.data}/>
        </DialogContent>
      </Dialog>
    </div>
  );
}