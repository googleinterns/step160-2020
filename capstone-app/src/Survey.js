import FeelingDND from './FeelingDND';
import React from 'react';

/** Survey component to collect PANAS survey data. */
export default class Survey extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: '',
      feelings: [],
      text: '',
      city: '',
      state: '',
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);

    this.feelingUpdateHandler = feelings => this.setState({feelings: feelings});
  }

  handleChange(event) {
    let name = event.target.name;
    let val = event.target.value;
    this.setState({[name]: val});
  }

  /**
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

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          User:
          <input type="text" name="user" value={this.state.user} onChange={this.handleChange} required/>
        </label>
        <label>
          Text:
          <input type="text" name="text" value={this.state.text} onChange={this.handleChange} />
        </label>

        <input type="hidden" name="country" id="countryId" value="US"/>
        <label>
          State:
          <select name="state" class="states order-alpha" id="stateId" value={this.state.state} onChange={this.handleChange} required>
                <option value="">Select State</option>
          </select>
        </label>
        <label>
          City:
          <select name="city" class="cities order-alpha" id="cityId" value={this.state.city} onChange={this.handleChange} required>
            <option value="">Select City</option>
          </select>
        </label>
        {<FeelingDND onChange={this.feelingUpdateHandler}/>}
        <input type="submit" value="Submit" />
      </form>
    );
  }
}
