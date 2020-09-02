import React from "react";
// nodejs library that concatenates classes
import classNames from "classnames";
// @material-ui/core components
import { makeStyles } from "@material-ui/core/styles";

// @material-ui/icons

// core components
import Footer from "components/Footer/Footer.js";
import GridContainer from "components/Grid/GridContainer.js";
import GridItem from "components/Grid/GridItem.js";
import Parallax from "components/Parallax/Parallax.js";

import styles from "assets/jss/material-kit-react/views/landingPage.js";

// Sections for this page
import ProductSection from "./Sections/ProductSection.js";
import SectionModal from "./Sections/SectionModal.js";
import SectionNavbar from "./Sections/SectionNavbar.js";

const useStyles = makeStyles(styles);

export default function LandingPage(props) {
  const classes = useStyles();
  return (
    <div>
     
      <SectionNavbarSignup />
      <Parallax filter image={require("assets/img/olive.jpg")}>
        <div className={classes.container}>
          <GridContainer >
            <GridItem xs={6} sm={6} md={6} lg={6} xl={6} >
              <h1 className={classes.title}>Eleos</h1>
              <h4>
                Feeling of the World.
              </h4>
              <h3>
              See how others around you are feeling
              </h3>
              <br />
              <SectionModal width="80%"/>
              
            </GridItem>

            <GridItem xs={6} sm={6} md={6} lg={6} xl={6} >
            <div id="embed-map">               
                <iframe width="600" height="600" src="https://datastudio.google.com/embed/reporting/2848f178-862d-4a89-9567-fba28d475595/page/N4NZB" ></iframe>
            </div>
            </GridItem>

          </GridContainer>

        </div>
      </Parallax>
      <div className={classNames(classes.main, classes.mainRaised)}>
        <div className={classes.container}>
        
          <ProductSection />
        </div>
        
      </div>
      <Footer />
    </div>
  );
}
