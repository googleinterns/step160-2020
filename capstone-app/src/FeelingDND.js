import React from 'react';
import './FeelingDND.css';

// TODO comments, class names for css should be caps first (check app.css)
// source https://www.freecodecamp.org/news/reactjs-implement-drag-and-drop-feature-without-using-external-libraries-ad8994429f1a/
class FeelingDND extends React.Component {
  state = {
    feelings: [
      {name:"Interested", category:"pool"},
      {name:"Distressed", category:"pool"},
      {name:"Excited", category:"pool"}
    ]
  }

  onDragStart = (event, name) => {
    console.log('dragstart: ', name);
    event.dataTransfer.setData("id", name);
  }

  onDragOver = (event) => {
    event.preventDefault();
  }

  onDrop = (event, category) => {
    let id = event.dataTransfer.getData("id");
    let feelings = this.state.feelings.filter((feeling) => {
      if (feeling.name == id) {
        feeling.category = category;
      }
      return feeling;
    });

    this.setState({feelings: feelings}, () => {this.props.onChange(this.state.feelings)});
  }

  onSubmit = () => {
    return this.state.feelings;
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
          onDragStart = {(event) => this.onDragStart(event, feeling.name)}
          draggable
          // className="draggable" TODO make some style for this later
        >
          {feeling.name}
        </div>
      );
    });

    return (
      <div className="drag-container">
        {Object.keys(categories).map((category, index) => {
          return <div className="Category"
            onDragOver={(event) => this.onDragOver(event)}
            onDrop={(event) => {this.onDrop(event, Object.getOwnPropertyNames(categories)[index])}}>
            <span className="category-header">{Object.getOwnPropertyNames(categories)[index]}</span>
            {categories[category]}
          </div>
        })}
      </div>
    );
  }
}

export default FeelingDND;
