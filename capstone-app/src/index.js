import * as serviceWorker from './serviceWorker';
import React from "react";
import ReactDOM from "react-dom";
import { createBrowserHistory } from "history";
import { Router, Route, Switch } from "react-router-dom";

import "./assets/scss/material-kit-react.scss";

// pages for this product

import LandingPage from "./views/LandingPage/LandingPage.js";
import ProfilePage from "./views/ProfilePage/ProfilePage.js";
import LoginPage from "./views/LoginPage/LoginPage.js";
import SignupPage from "./views/SignupPage/SignupPage.js";


var hist = createBrowserHistory();
serviceWorker.unregister();

ReactDOM.render(
  <Router history={hist}>
    <Switch>
      
      <Route path="/profile-page" component={ProfilePage} /> 
      <Route path="/login-page" component={LoginPage} /> 
      <Route path="/signup-page" component={SignupPage} /> 
      <Route path="/" component={LandingPage} /> 

    </Switch>
  </Router>,
  document.getElementById("root")
);
