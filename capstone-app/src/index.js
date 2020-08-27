import * as serviceWorker from './serviceWorker';
import React from "react";
import ReactDOM from "react-dom";
import { Auth0Provider } from "@auth0/auth0-react";
import { createBrowserHistory } from "history";
import { Router, Route, Switch } from "react-router-dom";

import "./assets/scss/material-kit-react.scss";

// pages for this product

import LandingPage from "./views/LandingPage/LandingPage.js";


var hist = createBrowserHistory();
serviceWorker.unregister();

ReactDOM.render(
  <Router history={hist}>
    <Switch>
      <Route path="/" component={LandingPage} /> 
    </Switch>
  </Router>,
  document.getElementById("root")
);

ReactDOM.render(
  <Auth0Provider
    domain="eleos.us.auth0.com"
    clientId="S67WqW3SLpYn1pbsKyAYifI6ehs5X5Pd"
    redirectUri={'https://5000-cs-588358946862-default.us-central1.cloudshell.dev/?authuser=0&environment_name=default'}
  >
    <LandingPage />
  </Auth0Provider>,
  document.getElementById("root")
);
