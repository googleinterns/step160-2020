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
        {name:"Interested", category:"pool"},
        {name:"Distressed", category:"pool"},
        {name:"Excited", category:"pool"},
        {name:"Upset", category:"pool"},
        {name:"Strong", category:"pool"},
        {name:"Guilty", category:"pool"},
        {name:"Scared", category:"pool"},
        {name:"Hostile", category:"pool"},
        {name:"Enthusiastic", category:"pool"},
        {name:"Proud", category:"pool"},
        {name:"Irritable", category:"pool"},
        {name:"Alert", category:"pool"},
        {name:"Ashamed", category:"pool"},
        {name:"Inspired", category:"pool"},
        {name:"Nervous", category:"pool"},
        {name:"Determined", category:"pool"},
        {name:"Attentive", category:"pool"},
        {name:"Jittery", category:"pool"},
        {name:"Active", category:"pool"},
        {name:"Afraid", category:"pool"},
      ]
    };
  }

  onDragStart(event, name) {
    console.log('dragstart: ', name);
    event.dataTransfer.setData("id", name);
  }

  onDragOver(event) {
    event.preventDefault();
  }

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
      pool: [],
      verySlightly: [],
      aLittle: [],
      moderately: [],
      quiteABit: [],
      extremely: []
    }

    this.state.feelings.forEach ((feeling) => {
      categories[feeling.category].push(
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
            <span>{Object.getOwnPropertyNames(categories)[index]}</span>
            {categories[category]}
          </div>
        })}
      </div>
    );
  }
}
