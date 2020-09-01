
import { makeStyles } from "@material-ui/core/styles";
import { TextField } from "@material-ui/core";

import Button from "components/CustomButtons/Button.js";
import React from 'react';

import styles from "assets/jss/material-kit-react/javascriptStyles.js";

/** journal component that records title and journal text */
export default class Journal extends React.Component {
  constructor(props) {
    super(props);
    this.classes = makeStyles(styles);
    this.state = {
      title: '',
      journal: '', 
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    
  }

  /** updates when the user adds text */
  handleChange(event) {
    let name = event.target.name;
    let val = event.target.value;
    this.setState({[name]: val});
  }

  /** sends journal title and text to the backend */
  async handleSubmit() {
    var requestBody = new URLSearchParams();

    var property;
    for (property of Object.getOwnPropertyNames(this.state)) {
        requestBody.append(property, this.state[property]);
    }

    const request = new Request('https://manage-at-scale-step-2020.appspot.com/journal', {
      method: 'POST',
      body: requestBody
    });

    fetch(request).then(response => console.log(response));
  }


  render() {
    return (
      <form>
        <TextField 
            required 
            id="journal-title"
            label="Title"
            style = {{width: '100%'}}
            margin="normal"
            name = "title"
            value = {this.state.title}
            onChange = {this.handleChange}
        />
        <TextField
            id="journal-text"
            label="Journal"
            multiline
            rows={30}
            variant="outlined"
            margin="normal"
            style ={{width: '100%'}}
            name = "journal"
            value = {this.state.journal}
            onChange = {this.handleChange}
        />
    
        <Button 
            simple 
            color="info" 
            size="lg"
            type="submit"
            onClick={this.handleSubmit}
        >
            Submit
        </Button>
                  

      </form>
    );
  }
}
