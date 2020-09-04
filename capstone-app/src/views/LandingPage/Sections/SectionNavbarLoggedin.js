import React from "react";
import { useAuth0 } from '@auth0/auth0-react';
import { Link } from 'react-router-dom';

// @material-ui/core components
import { makeStyles } from "@material-ui/core/styles";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
// @material-ui/icons
import AccountCircle from "@material-ui/icons/AccountCircle";
// core components
import Header from "components/Header/Header.js";
import Button from "components/CustomButtons/Button.js";

import styles from "assets/jss/material-kit-react/navbarsStyle.js";

const useStyles = makeStyles(styles);

export default function SectionNavbar() {
  const classes = useStyles();

  const {
    isAuthenticated,
    user,
    loginWithRedirect,
    logout,
  } = useAuth0();

  if (isAuthenticated){
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
                      className={classes.navLink}
                      onClick={e => e.preventDefault()}
                      color="transparent"
                    >
                      <AccountCircle className={classes.icons} />
                      {user.name}
                    </Button>
                  </ListItem>
                  <ListItem className={classes.listItem}>
                    <Button onClick={() => logout({ returnTo: window.location.origin })}>
                      Logout
                    </Button>
                  </ListItem>
                </List>
              }
            />
        </div>
      </div>
    );
  } else {
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
                    <Button onClick={loginWithRedirect}>
                      Login
                    </Button>
                  </ListItem>
                </List>
              }
            />
        </div>
      </div>
    );
  }
}