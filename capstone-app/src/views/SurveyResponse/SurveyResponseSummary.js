import Button from '@material-ui/core/Button';
import {Line} from 'react-chartjs-2';
import React from 'react';

// TODO add all 20 feelings
const feelings = {
  INTERESTED: {mainColor: 'rgba(244, 67, 54, 1)', bgColor: 'rgba(239, 154, 154, 1)'},
  DISTRESSED: {mainColor: 'rgba(233, 30, 99, 1)', bgColor: 'rgba(244, 143, 177, 1)'},
  EXCITED: {mainColor: 'rgba(156, 39, 176, 1)', bgColor: 'rgba(206, 147, 216, 1)'},
  UPSET: {mainColor: 'rgba(103, 58, 183, 1)', bgColor: 'rgba(179, 157, 219, 1)'},
  STRONG: {mainColor: 'rgba(63, 81, 181, 1)', bgColor: 'rgba(159, 168, 218, 1)'},
  GUILTY: {mainColor: 'rgba(33, 150, 243, 1)', bgColor: 'rgba(144, 202, 249, 1)'},
  SCARED: {mainColor: 'rgba(3, 169, 244, 1)', bgColor: 'rgba(129, 212, 250, 1)'},
  HOSTILE: {mainColor: 'rgba(0, 188, 212, 1)', bgColor: 'rgba(128, 222, 234, 1)'},
  ENTHUSIASTIC: {mainColor: 'rgba(0, 150, 136, 1)', bgColor: 'rgba(128, 203, 196, 1)'},
  PROUD: {mainColor: 'rgba(76, 175, 80, 1)', bgColor: 'rgba(165, 214, 167, 1)'},
  IRRITABLE: {mainColor: 'rgba(139, 195, 74, 1)', bgColor: 'rgba(197, 225, 165, 1)'},
  ALERT: {mainColor: 'rgba(205, 220, 57, 1)', bgColor: 'rgba(230, 238, 156, 1)'},
  ASHAMED: {mainColor: 'rgba(255, 235, 59, 1)', bgColor: 'rgba(255, 245, 157, 1)'},
  INSPIRED: {mainColor: 'rgba(255, 193, 7, 1)', bgColor: 'rgba(255, 224, 130, 1)'},
  NERVOUS: {mainColor: 'rgba(255, 152, 0, 1)', bgColor: 'rgba(255, 204, 128, 1)'},
  DETERMINED: {mainColor: 'rgba(255, 87, 34, 1)', bgColor: 'rgba(255, 171, 145, 1)'},
  ATTENTIVE: {mainColor: 'rgba(121, 85, 72, 1)', bgColor: 'rgba(188, 170, 164, 1)'},
  JITTERY: {mainColor: 'rgba(158, 158, 158, 1)', bgColor: 'rgba(238, 238, 238, 1)'},
  ACTIVE: {mainColor: 'rgba(96, 125, 139, 1)', bgColor: 'rgba(176, 190, 197, 1)'},
  AFRAID: {mainColor: 'rgba(33, 33, 33, 1)', bgColor: 'rgba(97, 97, 97, 1)'},
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