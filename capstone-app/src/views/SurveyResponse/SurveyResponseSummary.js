import Button from '@material-ui/core/Button';
import {Line} from 'react-chartjs-2';
import React from 'react';

// TODO add all 20 feelings
const feelings = {
  INTERESTED: {mainColor: '', bgColor: ''},
  DISTRESSED: {},
  EXCITED: {mainColor: 'rgba(66,229,50, 0.8)', bgColor: 'rgba(198,247,193, 0.8)'},
  UPSET: {},
  STRONG: {},
  GUILTY: {},
  SCARED: {},
  HOSTILE: {},
  ENTHUSIASTIC: {mainColor: 'rgba(50,66,229, 0.8)', bgColor: 'rgba(193,198,247, 0.8)'},
  PROUD: {},
  IRRITABLE: {},
  ALERT: {mainColor: 'rgba(229,50,66, 0.8)', bgColor: 'rgba(247,193,198, 0.8)'},
  ASHAMED: {},
  INSPIRED: {},
  NERVOUS: {},
  DETERMINED: {},
  ATTENTIVE: {},
  JITTERY: {},
  ACTIVE: {},
  AFRAID: {}
};

// TODO documentation for all the things
export default class SurveyResponseSummary extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loaded: [],
      data: {
				datasets: []
			}
    };
  }

  async handleClick(feeling, event) {
    if (!this.state.loaded.includes(feeling)) {
      var newLoaded = this.state.loaded.concat(feeling);
      this.setState({ loaded: newLoaded });

      var requestParam = new URLSearchParams();
      requestParam.append('action', 'feeling');
      requestParam.append('data', feeling);
      
      const request = new Request(
        'https://manage-at-scale-step-2020.appspot.com/survey?' + requestParam
      );

      let data = await fetch(request).then(response => response.json());

      this.sortResponsesByUser(data, feeling);
    }
  }

  sortResponsesByUser(data, feeling) {
    var userResponses = [];
    var worldResponses = [];
    for (const response of data) {
      if (response.user === this.props.user) {
        userResponses.push(response);
      } else {
        worldResponses.push(response);
      }
    }
    var newDatasets = this.state.data.datasets;
    newDatasets.push(this.buildDataset(userResponses, true, feeling));
    newDatasets.push(this.buildDataset(worldResponses, false, feeling));

    var newData = {datasets: newDatasets};
    this.setState({ data: newData });
  }

  buildDataset(responses, isUserData, feeling) {
    var sortedResponses = responses.sort(this.compareTimestamp);
    const dataset = {
      fill: false,
      data: []
    }

    if (isUserData) {
      dataset.label = feeling + ' (you)';
      dataset.backgroundColor = feelings[feeling].mainColor;
      dataset.borderColor = feelings[feeling].mainColor;
    } else {
      dataset.label = feeling + ' (world)';
      dataset.backgroundColor = feelings[feeling].bgColor;
      dataset.borderColor = feelings[feeling].bgColor;
    }

    var intensities = {
      NOT_AT_ALL: 0, 
      VERY_SLIGHTLY: 1, 
      A_LITTLE: 2, 
      MODERATELY: 3, 
      QUITE_A_BIT: 4, 
      EXTREMELY: 5
    };

    for (const response of sortedResponses) {
      var newDataPoint = {
        x: new Date(response.timestamp),
        y: intensities[response.feelings[feeling]]
      };
      
      dataset.data.push(newDataPoint);
    }

    return dataset;
  }

  compareTimestamp(fooResponse, barResponse) {
    if (fooResponse.timestamp < barResponse.timestamp) {
      return -1;
    } else if (fooResponse.timestamp > barResponse.timestamp) {
      return 1;
    } else {
      return 0;
    }
  }

  render() {
    var buttons = [];
    for (const feeling in feelings) {
      buttons.push(
        <Button variant="outlined" color="primary" onClick={(event) => this.handleClick(feeling, event)}>
          {feeling}
        </Button>
      )
    };

    return (
      <div>
        {buttons}
        <Line data={this.state.data} options={{
          scales: {
            xAxes: [{type: 'time', time: {unit: 'day'}, distribution: 'linear'}], 
            yAxes: [{ticks: {suggestedMin: 0, suggestedMax: 5}}]
          }
        }}/>
      </div>
    );
  }
}