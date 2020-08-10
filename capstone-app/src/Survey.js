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

  async handleSubmit(event) {
    // var xhr = new XMLHttpRequest();
    // xhr.addEventListener('load', () => {
    //   console.log(xhr.responseText)
    // });
    // xhr.open('POST', 'http://localhost:8080/survey');
    // xhr.send(JSON.stringify(this.state));
    // console.log(JSON.stringify(this.state));
    // event.preventDefault();

    var requestBody = new URLSearchParams();

    var property;
    for (property of Object.getOwnPropertyNames(this.state)) {
        if (property !== 'feelings') {
            requestBody.append(property, this.state[property]);
        } else {
            var feeling;
            console.log(this.state[property]);
            for (feeling in this.state[property]) {
                console.log(this.state[property][feeling].name);
                if (this.state[property][feeling].category !== "pool") {
                    requestBody.append(this.state[property][feeling].name, this.state[property][feeling].category);
                }
            }
        }
    }

    // console.log(requestBody);
    event.preventDefault();

    const request = new Request('http://localhost:8080/survey', {
        method: 'POST',
        body: requestBody
    });

    fetch(request).then(response => { window.location.href = response.url; });
    event.preventDefault();
  }

  render() {
    return (
      <form onSubmit={this.handleSubmit}>
        <label>
          User:
          <input type="text" name="user" value={this.state.user} onChange={this.handleChange} />
        </label>
        <label>
          Text:
          <input type="text" name="text" value={this.state.text} onChange={this.handleChange} />
        </label>

        <input type="hidden" name="country" id="countryId" value="US"/>
        <label>
          State:
          <select name="state" class="states order-alpha" id="stateId" value={this.state.state} onChange={this.handleChange}>
                <option value="">Select State</option>
          </select>
        </label>
        <label>
          <select name="city" class="cities order-alpha" id="cityId" value={this.state.city} onChange={this.handleChange}>
            <option value="">Select City</option>
          </select>
        </label>
        {<FeelingDND onChange={this.feelingUpdateHandler}/>}
        <input type="submit" value="Submit" />
      </form>
    );
  }
}
