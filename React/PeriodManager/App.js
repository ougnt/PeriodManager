import React from 'react';
import { StyleSheet, Dimensions, View, StatusBar, Alert,TouchableWithoutFeedback } from 'react-native';

import TitleBar from './components/information-screen/information-screen-titlebar.js';
import DateScroller from './components/information-screen/date-scroller.js';
import Menu from './components/menu-screen/menu.js';

export default class PeriodManager extends React.Component {
  
  constructor(){
    super();
    this.state ={
      showMenu:false
    }
  }
  
  render() {
    return (
        <TouchableWithoutFeedback
          disabled= {!this.state.showMenu}
          onPress={ () => { this.setState({showMenu: false}) } }> 
          <View style={styles.container}>
              <StatusBar hidden={true} /> 
              <TitleBar 
                date = {new Date().toDateString()}
                givenStyle = {styles.titleBar}
                helpIconStyle = {styles.helpIcon}
                dateDisplayStyle = {styles.dateDisplay}
                hamburgerMenuStyle= {styles.hamburgerMenu} 
                onMenuClick={this.onMenuClick} />

              <DateScroller
                givenStyle= {styles.dateScroller}
                dateScrollerLeftEdgeStyle= {styles.dateScrollerLeftEdgeStyle}
                dateScrollerMidEdgeStyle = {styles.dateScrollerMidEdgeStyle}
                dateScrollerRightEdgeStyle = {styles.dateScrollerRightEdgeStyle}
                dateScrollerSideBarStyle= {styles.dateScrollerSideBarStyle} /> 

              <Menu 
                givenStyle= { this.state.showMenu ? styles.menu : styles.hidden}
                outsideClick={this.onMenuClick}
                menuItemStyle={styles.menuItem} /> 
              
          </View>
        </TouchableWithoutFeedback>
    );
  }

  onMenuClick = () => {
    this.setState({showMenu: !this.state.showMenu});
  }
}

let screenWidth = Dimensions.get('window').width;   
let screenHeight = Dimensions.get('window').height; 

const styles = StyleSheet.create({ 
  container: {
    flex: 10,
    backgroundColor: '#FF00FF',
    alignItems: 'flex-start',
    justifyContent: 'flex-start',
  },
  titleBar: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    backgroundColor: '#FFF1E9', 
    width: screenWidth,
    height: screenHeight * 0.1, 
  },
  helpIcon: {
    height: null, 
    width: screenWidth * 0.1, 
    resizeMode: 'contain',
  },
  hamburgerMenu: {
    height: screenWidth * 0.1,  
    width: screenWidth * 0.1,
    alignSelf: 'center', 
    resizeMode: 'contain',
  },
  dateDisplay: {
    textAlign: 'center',
    fontWeight: 'bold',
    height: null, 
    width: screenWidth * 0.8, 
    alignSelf: 'center',
  },
  menu: {
    position: 'absolute',
    left: screenWidth / 2 + screenWidth * 0.01,
    top: screenHeight * 0.1, 
    height: null,
    width: screenWidth / 2,
    backgroundColor: '#0F0F0F', 
    margin: screenWidth * 0.01,
  },
  hidden: {
    height: 0,
    width: 0,
  },
  menuItem: {
    backgroundColor: '#FFFFFF',
    height: screenHeight * 0.1,
    alignItems: 'center',
    justifyContent: 'center',
    margin: screenWidth * 0.005,
  },
  dateScroller: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    width: screenWidth,
    height: screenHeight * 0.2, 
  },
  dateScrollerLeftEdgeStyle: {
    margin: 0,
    height: screenHeight * 0.2, 
    width: screenWidth * 0.1, 
    resizeMode: 'stretch', 
  },
  dateScrollerRightEdgeStyle: { 
    height: screenHeight * 0.2, 
    width: screenWidth * 0.1, 
    resizeMode: 'stretch',
  },
  dateScrollerMidEdgeStyle: { 
    flexDirection: 'column',
    height: screenHeight * 0.2, 
    width: screenWidth * 0.8,
  },
  dateScrollerSideBarStyle: {
    height: screenHeight * 0.03,
    width: screenWidth * 0.8,
  },
});
