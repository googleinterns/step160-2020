// TODO alphabetize
import './index.css';
import App from './App';
import Form from './Form';
import * as serviceWorker from './serviceWorker';
import React from "react";
import ReactDOM from "react-dom";
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
