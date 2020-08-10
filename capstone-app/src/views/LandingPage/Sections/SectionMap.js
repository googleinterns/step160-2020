import React from "react";
// @material-ui/core components
import { makeStyles } from "@material-ui/core/styles";


// core components
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";

import styles from "assets/jss/material-kit-react/views/componentsSections/pillsStyle.js";

const useStyles = makeStyles(styles);

export default function SectionMap() {
  const classes = useStyles();
  return (
    <div className={classes.section}>
      <div className={classes.container}>
        <div id="embed-map">               
            <iframe width="150" height="150" src="https://datastudio.google.com/embed/reporting/2848f178-862d-4a89-9567-fba28d475595/page/N4NZB" ></iframe>
            
        </div>
      </div>
    </div>
  );
}
