import Card from "@material-ui/core/Card";
import React from 'react';
import SurveyResponseDialog from './SurveyResponseDialog.js';
import {Radar} from 'react-chartjs-2';

/** 
 * Function component representing one survey response in a card. Includes the feelings from the 
 * survey response represented as a radar graph, and a button to show more details in a dialog.
 */
export default function SurveyResponseCard(props) {
  var date = new Date(props.response.timestamp);

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

  data.labels.forEach ((feeling) => {
    data.datasets[0].data.push(
      props.intensities[props.response.feelings[feeling]]
    );
  });

  return (
    <Card
      key={props.response.timestamp}
      className="SurveyResponseCard"
    >
      <Radar data={data}/>
      <p>{date.toDateString()}</p>
      <SurveyResponseDialog date={date} data={data} response={props.response}/>
    </Card>
  );
}