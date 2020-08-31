import React from 'react';
import SurveyResponseCard from './SurveyResponseCard.js';
import SurveyResponseSummary from './SurveyResponseSummary.js';

/** TODO */
export default class SurveyResponseContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: 'peter',
      responses: [],
    };
  }

  async componentDidMount() {
    var requestParam = new URLSearchParams();
    requestParam.append('action', 'user');
    requestParam.append('data', this.state.user);
    
    const request = new Request(
      'https://manage-at-scale-step-2020.appspot.com/survey?' + requestParam
    );

    let data = await fetch(request).then(response => response.json());
    console.log("data:");
    console.log(data);

    this.setState({responses: data});
  }

  render () {
    var cards = [];
    var index;
    for (var index = 0; index < this.state.responses.length; index++) {
      cards.push(<SurveyResponseCard response={this.state.responses[index]}/>)
    }
    console.log("cards:");
    console.log(cards);

    return (
      <div>
        <SurveyResponseSummary user={this.state.user} />
        {cards}
      </div>
    );
  }
}