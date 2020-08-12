import React from 'react';
import './FeelingDND.css';

/** 
 * Drag-and-drop feeling component for PANAS survey. 
 * 
 * Starter code for drag-and-drop functionality from:
 * https://www.freecodecamp.org/news/reactjs-implement-drag-and-drop-feature-without-using-external-libraries-ad8994429f1a/
 */
export default class FeelingDND extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
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
      ]
    };
  }

  /** Initiates transfer of data when object is dragged. */
  onDragStart(event, name) {
    console.log('dragstart: ', name);
    event.dataTransfer.setData("id", name);
  }

  /** Allows objects to be dragged over each other. */
  onDragOver(event) {
    event.preventDefault();
  }

  /** Updates object category when dropped. */
  onDrop (event, category) {
    let id = event.dataTransfer.getData("id");
    let feelings = this.state.feelings.filter((feeling) => {
      if (feeling.name == id) {
        feeling.category = category;
      }
      return feeling;
    });

    this.setState({feelings: feelings}, () => {this.props.onChange(this.state.feelings)});
  }

  render() {
    var categories = {
      NOT_AT_ALL: {pool: [], pretty_name: "Drag from below:"},
      VERY_SLIGHTLY: {pool: [], pretty_name: "Very Slightly"},
      A_LITTLE: {pool: [], pretty_name: "A Little"},
      MODERATELY: {pool: [], pretty_name: "Moderately"},
      QUITE_A_BIT: {pool: [], pretty_name: "Quite A Bit"},
      EXTREMELY: {pool: [], pretty_name: "Extremely"}
    }

    this.state.feelings.forEach ((feeling) => {
      categories[feeling.category].pool.push(
        <div key={feeling.name}
          className="Draggable"
          onDragStart = {(event) => this.onDragStart(event, feeling.name)}
          draggable
        >
          {feeling.name}
        </div>
      );
    });

    return (
      <div>
        {Object.keys(categories).map((category, index) => {
          return <div className="Category"
            onDragOver={(event) => this.onDragOver(event)}
            onDrop={(event) => {this.onDrop(event, Object.getOwnPropertyNames(categories)[index])}}>
            <span>{categories[category].pretty_name}</span>
            {categories[category].pool}
          </div>
        })}
      </div>
    );
  }
}
