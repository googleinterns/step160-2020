import React from "react";
import { useAuth0 } from '@auth0/auth0-react';
// nodejs library that concatenates classes
import classNames from "classnames";
// @material-ui/core components
import { makeStyles } from "@material-ui/core/styles";

// @material-ui/icons
import Favorite from "@material-ui/icons/Favorite";
import LibraryBooksSharpIcon from '@material-ui/icons/LibraryBooksSharp';
import Palette from "@material-ui/icons/Palette";
import Timeline from "@material-ui/icons/Timeline";
import ViewModule from "@material-ui/icons/ViewModule";
// core components
import Footer from "components/Footer/Footer.js";
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import NavPills from "components/NavPills/NavPills.js";
import Parallax from "components/Parallax/Parallax.js";
import styles from "assets/jss/material-kit-react/views/profilePage.js";

// Sections for this page
import Journal from "./Sections/Journal.js";
import SurveyResponseContainer from "./Sections/SurveyResponse/SurveyResponseContainer.js";
import SurveyResponseSummary from "./Sections/SurveyResponse/SurveyResponseSummary.js";

const useStyles = makeStyles(styles);

export default function ProfilePage(props) {
  const classes = useStyles();

  const {
    isAuthenticated,
    user,
  } = useAuth0();


  const intensities = {
      NOT_AT_ALL: 0, 
      VERY_SLIGHTLY: 1, 
      A_LITTLE: 2, 
      MODERATELY: 3, 
      QUITE_A_BIT: 4, 
      EXTREMELY: 5
  };

  if (isAuthenticated){
  return (
    <div>
      <Parallax small filter image={require("assets/img/olivetree.jpg")} />
      <div className={classNames(classes.main, classes.mainRaised)}>
        <div>
          <div className={classes.container}>
            
            <GridContainer justify="center">
              <GridItem xs={12} sm={12} md={8} className={classes.navWrapper}>
                <NavPills
                color="info"
                horizontal={{
                  tabsGrid: { xs: 2, sm: 2, md: 2 },
                  contentGrid: { xs: 10, sm: 10, md: 10 }
                }}
                  tabs={[
                    {
                      tabButton: "Feelings Log",
                      tabIcon: ViewModule,
                      tabContent: (
                        <GridContainer spacing={4} justify="center">
                        <GridItem >

                        <SurveyResponseContainer user={user.name} intensities={intensities}/>
                        
                        </GridItem>
                        </GridContainer>
                      )
                    },
                    {
                      tabButton: "Feelings Over Time",
                      tabIcon: Timeline,

                      tabContent: (
                        <GridContainer spacing={4} justify="center">
                        <GridItem >

                        <SurveyResponseSummary user={user.name} intensities={intensities}/>
                        
                        </GridItem>
                        </GridContainer>
                      )
                    },
                    {
                      tabButton: "Journal",
                      tabIcon: LibraryBooksSharpIcon,
                      tabContent: (
                        <GridContainer spacing={4} justify="center">
                        <GridItem >

                        <Journal user={user.name} />
                        
                        </GridItem>
                        </GridContainer>
                      )
                    },
                    {
                      tabButton: "Responses",
                      tabIcon: Palette,
                      tabContent: (
                        <GridContainer justify="center">
                          <GridItem xs={12} sm={12} md={4}>
                            
                          </GridItem>
                        </GridContainer>
                      )
                    },
                    
                  ]}
                />
              </GridItem>
            </GridContainer>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
  } else {
      return (null);
  }
}
