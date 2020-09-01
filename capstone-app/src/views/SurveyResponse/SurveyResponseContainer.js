import React from 'react';
import SurveyResponseCard from './SurveyResponseCard.js';
import SurveyResponseSummary from './SurveyResponseSummary.js';

/** 
 * Container component for the SurveyResponseSummary component and the SurveyResponseCard 
 * components. 
 */
export default class SurveyResponseContainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: 'peter',  // hardcoded for now - @DWIGHT PUT NAME HERE!
      responses: [],
    };
  }

  /** 
  * After first render, makes a GET request for user's survey responses and updates the 
  * component's state. 
  */
  async componentDidMount() {
    var requestParam = new URLSearchParams();
    requestParam.append('action', 'user');
    requestParam.append('data', this.state.user);
    
    const request = new Request(
      'https://manage-at-scale-step-2020.appspot.com/survey?' + requestParam
    );

    let data = await fetch(request).then(response => response.json());
    this.setState({responses: data});
  }

  render () {
    var cards = [];
    for (var index = 0; index < this.state.responses.length; index++) {
      cards.push(<SurveyResponseCard response={this.state.responses[index]}/>)
    }

    return (
      <div>
        <SurveyResponseSummary user={this.state.user} />
        {cards}
      </div>
    );
  }
}