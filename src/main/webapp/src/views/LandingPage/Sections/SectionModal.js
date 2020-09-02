import React from "react";
// @material-ui/core components
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import Dialog from "@material-ui/core/Dialog";
import IconButton from "@material-ui/core/IconButton";
import Slide from "@material-ui/core/Slide";
import { makeStyles } from "@material-ui/core/styles";
// @material-ui/icons
import Close from "@material-ui/icons/Close";
import LibraryBooks from "@material-ui/icons/LibraryBooks";
// core components
import Button from "components/CustomButtons/Button.js";
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import styles from "assets/jss/material-kit-react/javascriptStyles.js";
import Survey from "views/LandingPage/Sections/Survey.js";

const useStyles = makeStyles(styles);

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

Transition.displayName = "Transition";

export default function SectionModal() {
  const classes = useStyles();
 
  const [classicModal, setClassicModal] = React.useState(false);
  return (
    <div className={classes.section}>
      <div className={classes.container}>
        
        <GridContainer>
          <GridItem xs={6} sm={6} md={6} lg={6} xl={6}>
            
            <GridContainer>
              <GridItem xs={6} sm={6} md={6} lg={6} xl={6}>
                <Button
                  color="info"
                  block
                  onClick={() => setClassicModal(true)}
                >
                  <LibraryBooks className={classes.icon} />
                  How are you feeling?
                </Button>
                <Dialog
                  classes={{
                    root: classes.center,
                    paper: classes.modal
                  }}
                  open={classicModal}
                  TransitionComponent={Transition}
                  keepMounted
                  onClose={() => setClassicModal(false)}
                  aria-labelledby="classic-modal-slide-title"
                  aria-describedby="classic-modal-slide-description"
                 >
                  <DialogTitle
                    id="classic-modal-slide-title"
                    disableTypography
                    className={classes.modalHeader}
                  >
                    <IconButton
                      className={classes.modalCloseButton}
                      key="close"
                      aria-label="Close"
                      color="inherit"
                      onClick={() => setClassicModal(false)}
                    >
                      <Close className={classes.modalClose} />
                    </IconButton>
                    <h4 className={classes.modalTitle}>How are you feeling?</h4>
                  </DialogTitle>
                  <Survey />
                  
                  
                </Dialog>
              </GridItem>
            </GridContainer>
            </GridItem>
            </GridContainer>
         
      </div>
    </div>
  );
}
