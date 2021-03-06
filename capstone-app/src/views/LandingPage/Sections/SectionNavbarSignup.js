import React from "react";
import { Link } from 'react-router-dom';
// @material-ui/core components
import { makeStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import Icon from "@material-ui/core/Icon";
// @material-ui/icons
import AccountCircle from "@material-ui/icons/AccountCircle";
import Explore from "@material-ui/icons/Explore";
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
// core components
import Header from "components/Header/Header.js";
import Button from "components/CustomButtons/Button.js";

import styles from "assets/jss/material-kit-react/navbarsStyle.js";

const useStyles = makeStyles(styles);

export default function SectionNavbar() {
  const classes = useStyles();
  return (
    <div className={classes.section}>
      <div className={classes.container}> 
      </div>
      <div id="navbar" className={classes.navbar}>
          <Header
            brand="Eleos"
            color="info"
            rightLinks={
              <List className={classes.list}>
                <ListItem className={classes.listItem}>
                
                  <Button
                    href="#pablo"
                    className={classes.navLink + " " + classes.navLinkActive}
                    onClick={e => e.preventDefault()}
                    color="transparent"
                  >
                    <ExitToAppIcon className={classes.icons} /> 
                    <Link to="/login-page" className={classes.link}>
                    Log In
                    </Link>
                  </Button>
                  

                </ListItem>
            
                <ListItem className={classes.listItem}>
                  <Button
                    href="#pablo"
                    className={classes.navLink}
                    onClick={e => e.preventDefault()}
                    color="transparent"
                  >
                    <AccountCircle className={classes.icons} /> 
                    <Link to="/signup-page" className={classes.link}>
                    Sign Up
                    </Link>
                  </Button>
                </ListItem>
                
              </List>
            }
          />
      </div>
    </div>
  );
}
