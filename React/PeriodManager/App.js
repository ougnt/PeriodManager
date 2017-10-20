import React from 'react';
import { StyleSheet, Dimensions, View, StatusBar, Alert,TouchableWithoutFeedback, Image } from 'react-native';

import TitleBar from './components/information-screen/information-screen-titlebar.js';
import DateScroller from './components/information-screen/date-scroller.js';
import Menu from './components/menu-screen/menu.js';
import DateInfo from './components/information-screen/date-info.js';

let screenWidth = Dimensions.get('window').width;   
let screenHeight = Dimensions.get('window').height; 

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
                dateScrollerSideBarStyle= {styles.dateScrollerSideBarStyle}
                dateMeterWidth= {screenWidth * 0.2}
                fontColor='#999999'
                fontColorToday='#FFA800' /> 
              <DateInfo 
                givenStyle={styles.dateInfo}
                themeStyle={styles.dateDetailTheme}
                textStyle={styles.dateDetailText}
                theme = {global.DateDetail.Background.Beach}
                infoText='Hello world today is your day' />
              <Image 
                source={require('./images/finger_pointer.png')}
                style={styles.fingerIndex} /> 

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
  fingerIndex: {
    position: 'absolute',
    left: screenWidth / 2 - screenHeight * 0.05,
    top: screenHeight * 0.25, 
    height: screenHeight * 0.1,
    width: screenHeight * 0.1,
  },
  dateInfo: {
    height: screenHeight * 0.40,
    width: screenWidth,
    backgroundColor: '#FFF1E9',
  },
  dateDetailTheme: {
    height: screenHeight * 0.40,
    width: screenWidth,
    resizeMode: 'stretch',
    alignItems: 'flex-start',
  },
  dateDetailText: {
    marginTop: screenHeight * 0.05,
    marginLeft: screenWidth * 0.1,
  }
});
