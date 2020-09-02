import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import * as serviceWorker from './serviceWorker';
import { createBrowserHistory } from "history";
import { Router, Route, Switch } from "react-router-dom";

import "./assets/scss/material-kit-react.scss";

// pages for this product

import LandingPage from "./views/LandingPage/LandingPage.js";
import ProfilePage from "./views/ProfilePage/ProfilePage.js";
<<<<<<< HEAD:src/main/webapp/src/index.js
=======
import LoginPage from "./views/LoginPage/LoginPage.js";
import SignupPage from "./views/SignupPage/SignupPage.js";
>>>>>>> 98e6b9026629d7b430031728488ea39304dc5a21:capstone-app/src/index.js

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
