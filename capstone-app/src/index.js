import './index.css';
import * as serviceWorker from './serviceWorker';
import React from "react";
import ReactDOM from "react-dom";
import { Auth0Provider } from "@auth0/auth0-react";
import { createBrowserHistory } from "history";
import { Router, Route, Switch } from "react-router-dom";

import "./assets/scss/material-kit-react.scss";

// pages for this product

import LandingPage from "./views/LandingPage/LandingPage.js";
import ProfilePage from "./views/ProfilePage/ProfilePage.js";

var hist = createBrowserHistory();
serviceWorker.unregister();

ReactDOM.render(
  <Router history={hist}>
    <Switch>     
      <Route path="/profile-page" component={ProfilePage} /> 
      <Route path="/" component={LandingPage} /> 
    </Switch>
  </Router>,
  document.getElementById("root")
);

ReactDOM.render(
  <Auth0Provider
    domain="dev-12xuu4pl.us.auth0.com"
    clientId="8oXDmm0KJY6sBevDcv5Uw9GIR8ctKj3W"
    redirectUri={'https://manage-at-scale-step-2020.uc.r.appspot.com/'}
  >
    <LandingPage />
    <ProfilePage />
  </Auth0Provider>,
  document.getElementById("root")
);
