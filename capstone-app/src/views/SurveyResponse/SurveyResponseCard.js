import Card from "@material-ui/core/Card";
import React from 'react';
import SurveyResponseDialog from './SurveyResponseDialog.js';
import {Radar} from 'react-chartjs-2';

// TODO documentation
// from props will get: {this.props.response.foo}
// - feelings with all 20 feelings inside
// - user
// - city
// - state
// - text
// - timestamp
export default class SurveyResponseCard extends React.Component {
  render() {
    var date = new Date(this.props.response.timestamp);

    var data = {
      labels: [
        'INTERESTED',
        'DISTRESSED',
        'EXCITED',
        'UPSET',
        'STRONG',
        'GUILTY',
        'SCARED',
        'HOSTILE',
        'ENTHUSIASTIC',
        'PROUD',
        'IRRITABLE',
        'ALERT',
        'ASHAMED',
        'INSPIRED',
        'NERVOUS',
        'DETERMINED',
        'ATTENTIVE',
        'JITTERY',
        'ACTIVE',
        'AFRAID'
      ],
        datasets: [
          {
            label: date,
            backgroundColor: 'rgba(179,181,198,0.2)',
            borderColor: 'rgba(179,181,198,1)',
            pointBackgroundColor: 'rgba(179,181,198,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(179,181,198,1)',
            data: []
          }
        ]
      };

    var intensities = {
      NOT_AT_ALL: 0, 
      VERY_SLIGHTLY: 1, 
      A_LITTLE: 2, 
      MODERATELY: 3, 
      QUITE_A_BIT: 4, 
      EXTREMELY: 5
    };

    data.labels.forEach ((feeling) => {
      data.datasets[0].data.push(
        intensities[this.props.response.feelings[feeling]]
      );
    });

    return (
      <Card
        key={this.props.response.timestamp}
        className="SurveyResponseCard"
      >
        <Radar data={data}/>
        <p>{date.toDateString()}</p>
        <SurveyResponseDialog date={date} data={data} response={this.props.response}/>
      </Card>
    );
  }
}