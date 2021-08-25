import React from "react";
// @material-ui/core components
import { makeStyles } from "@material-ui/core/styles";

// @material-ui/icons
import man from "@material-ui/icons/DirectionsRun";
import person from "@material-ui/icons/EmojiPeople";
import heart from "@material-ui/icons/Favorite";
import call from "@material-ui/icons/Call";

// core components
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import InfoArea from "components/InfoArea/InfoArea.js";

import styles from "assets/jss/material-kit-react/views/landingPageSections/productStyle.js";

const useStyles = makeStyles(styles);

export default function ProductSection() {
  const classes = useStyles();
  return (
    <div className={classes.section}>
      <GridContainer justify="center">
        <GridItem xs={12} sm={12} md={8}>
        
          <h2 className={classes.title}>Eleos</h2>
          <h5 className={classes.description}>
            For the past few months, many industries have told their employees to work from home, 
            and while some people are adjusted quite well to this new lifestyle, others may still 
            be uncomfortable with the change. With this new distance between everybody, it can be hard 
            to get an understanding of how others are truly feeling about various issues and events in their lives.
            With Eleos, people can express how they feel and see how others are feeling near them and in other areas across the country. 

            We hope Eleos alleviates some of the emotional distancing that the COVID-19 era has put between each of us. 
            Coping with loneliness is difficult, so here are some more ways to help.  

          </h5>
        </GridItem>
      </GridContainer>
      <div>
        <GridContainer>
          <GridItem xs={12} sm={12} md={3}>
            <InfoArea
              title="Take Care of Your Mind and Body"
              description="Stay active by taking walks or doing at-home workouts. Practice self-compassion and patience to yourself in these trying times."
              icon={man}
              iconColor="info"
              vertical
            />
          </GridItem>
          <GridItem xs={12} sm={12} md={3}>
            <InfoArea
              title="Find Sources of Comfort and Meaning"
              description="Indulge in your favorite comforting activities such as watching your favorite shows or drinking your favorite tea. Delve into a new hobby or project"
              icon={person}
              iconColor="info"
              vertical
            />
          </GridItem>
          <GridItem xs={12} sm={12} md={3}>
            <InfoArea
              title="Connect With Others"
              description="Check in with friends and family. Find companionship in online groups based on your interests or identities." 
              icon={heart}
              iconColor="info"
              vertical
            />
          </GridItem>
          <GridItem xs={12} sm={12} md={3}>
            <InfoArea
              title="React Out"
              description="Reach out to resources such as the National Suicide Prevention Lifeline (Call 1-800-273-TALK) and the Crisis Text Line (Text 'HELLO' to 741741)."
              icon={call}
              iconColor="info"
              vertical
            />
          </GridItem>
        </GridContainer>
      </div>
    </div>
  );
}