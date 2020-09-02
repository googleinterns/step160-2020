import { makeStyles } from "@material-ui/core/styles";
import Button from "components/CustomButtons/Button.js";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import FeelingDND from './FeelingDND';
import React from 'react';
import './Survey.css';

import styles from "assets/jss/material-kit-react/javascriptStyles.js";

/** Survey component to collect PANAS survey data. */
export default class Survey extends React.Component {
  constructor(props) {
    super(props);
    this.classes = makeStyles(styles);
    this.state = {
      user: '',
      feelings: [
        {name:"Interested", category:"NOT_AT_ALL"},
        {name:"Distressed", category:"NOT_AT_ALL"},
        {name:"Excited", category:"NOT_AT_ALL"},
        {name:"Upset", category:"NOT_AT_ALL"},
        {name:"Strong", category:"NOT_AT_ALL"},
        {name:"Guilty", category:"NOT_AT_ALL"},
        {name:"Scared", category:"NOT_AT_ALL"},
        {name:"Hostile", category:"NOT_AT_ALL"},
        {name:"Enthusiastic", category:"NOT_AT_ALL"},
        {name:"Proud", category:"NOT_AT_ALL"},
        {name:"Irritable", category:"NOT_AT_ALL"},
        {name:"Alert", category:"NOT_AT_ALL"},
        {name:"Ashamed", category:"NOT_AT_ALL"},
        {name:"Inspired", category:"NOT_AT_ALL"},
        {name:"Nervous", category:"NOT_AT_ALL"},
        {name:"Determined", category:"NOT_AT_ALL"},
        {name:"Attentive", category:"NOT_AT_ALL"},
        {name:"Jittery", category:"NOT_AT_ALL"},
        {name:"Active", category:"NOT_AT_ALL"},
        {name:"Afraid", category:"NOT_AT_ALL"},
      ],
      text: '',
      city: '',
      state: '',
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.onDrop = this.onDrop.bind(this);
  }

  /** Updates the component's state when given user input. */
  handleChange(event) {
    let name = event.target.name;
    let val = event.target.value;
    this.setState({[name]: val});
  }

  /** Sends a POST request to the backend containing the survey response. */
  async handleSubmit() {
    var requestBody = new URLSearchParams();

    var property;
    for (property of Object.getOwnPropertyNames(this.state)) {
      if (property !== 'feelings') {
        requestBody.append(property, this.sanitize(this.state[property]));
      } else {
        var feeling;
        for (feeling in this.state[property]) {
          requestBody.append(
              this.state[property][feeling].name.toUpperCase(),
              this.state[property][feeling].category);
        }
      }
    }

    const request = new Request('https://manage-at-scale-step-2020.appspot.com/survey', {
      method: 'POST',
      body: requestBody
    });

    fetch(request).then(response => console.log(response));
  }

  /**
   * Sanitizes the given string using HTML encoding.
   * 
   * Source:
   * https://stackoverflow.com/questions/2794137/sanitizing-user-input-before-adding-it-to-the-dom-in-javascript
   */
  sanitize(string) {
    const map = {
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      '"': '&quot;',
      "'": '&#x27;',
      "/": '&#x2F;',
    };
    const regex = /[&<>"'/]/ig;
    return string.replace(regex, (match)=>(map[match]));
  }

  /** Initiates transfer of data when an object is dragged. */
  onDragStart(event, name) {
    console.log('dragstart: ', name);
    event.dataTransfer.setData("id", name);
  }

  /** Allows objects to be dragged over each other. */
  onDragOver(event) {
    event.preventDefault();
  }

  /** Updates an object's category when dropped. */
  onDrop (event, category) {
    let id = event.dataTransfer.getData("id");
    let feelings = this.state.feelings.filter((feeling) => {
      if (feeling.name === id) {
        feeling.category = category;
      }
      return feeling;
    });

    this.setState({feelings: feelings});
  }

  render() {
    return (
      <form>
        {<DialogContent
          id="classic-modal-slide-description"
          className={this.classes.modalBody}
        >
          <label className="FormInput">
            User:
            <input type="text" name="user" value={this.state.user} onChange={this.handleChange} required/>
          </label>
          <label className="FormInput">
            Text:
            <input type="text" name="text" value={this.state.text} onChange={this.handleChange} />
          </label>
          <input type="hidden" name="country" id="countryId" value="US"/>
          <label className="FormInput">
            State:
            <select name="state" class="states order-alpha" id="stateId" value={this.state.state} onChange={this.handleChange} required>
                  <option value="">Select State</option>
            </select>
          </label>
          <label className="FormInput">
            City:
            <select name="city" class="cities order-alpha" id="cityId" value={this.state.city} onChange={this.handleChange} required>
              <option value="">Select City</option>
            </select>
          </label>
        </DialogContent>}
        {<DialogContent
          id="classic-modal-slide-description"
          className={this.classes.modalBody}
        >
          <FeelingDND 
            feelings={this.state.feelings}
            onDragStart={this.onDragStart}
            onDragOver={this.onDragOver}
            onDrop={this.onDrop}
          />
        </DialogContent>}
        {<DialogActions className={this.classes.modalFooter}>
            <Button
              onClick={this.handleSubmit}
              color="danger"
              simple
            >
              SUBMIT
            </Button>
        </DialogActions>}

      </form>
    );
  }
}
