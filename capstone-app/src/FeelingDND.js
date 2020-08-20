import React from 'react';
import './FeelingDND.css';

/** 
 * Drag-and-drop feeling component for PANAS survey. 
 * 
 * Starter code for drag-and-drop functionality from:
 * https://www.freecodecamp.org/news/reactjs-implement-drag-and-drop-feature-without-using-external-libraries-ad8994429f1a/
 */
export default class FeelingDND extends React.Component {
  render() {
    var categories = {
      NOT_AT_ALL: {pool: [], pretty_name: "Drag from below:"},
      VERY_SLIGHTLY: {pool: [], pretty_name: "Very Slightly"},
      A_LITTLE: {pool: [], pretty_name: "A Little"},
      MODERATELY: {pool: [], pretty_name: "Moderately"},
      QUITE_A_BIT: {pool: [], pretty_name: "Quite A Bit"},
      EXTREMELY: {pool: [], pretty_name: "Extremely"}
    }

    this.props.feelings.forEach ((feeling) => {
      categories[feeling.category].pool.push(
        <div key={feeling.name}
          className="Draggable"
          onDragStart = {(event) => this.props.onDragStart(event, feeling.name)}
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
            onDragOver={(event) => this.props.onDragOver(event)}
            onDrop={(event) => {this.props.onDrop(event, Object.getOwnPropertyNames(categories)[index])}}>
            <span>{categories[category].pretty_name}</span>
            {categories[category].pool}
          </div>
        })}
      </div>
    );
  }
}
