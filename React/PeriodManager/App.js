import React from 'react';
import { StyleSheet, Dimensions, View, StatusBar, Alert,TouchableWithoutFeedback } from 'react-native';

import TitleBar from './components/information-screen/information-screen-titlebar.js';
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
          onPress={ () => { if(this.state.showMenu) this.onMenuClick() } }> 
          <View style={styles.container}>
              <StatusBar hidden={true} /> 
              <TitleBar 
                date = {new Date().toDateString()}
                givenStyle = {styles.titleBar}
                helpIconStyle = {styles.helpIcon}
                dateDisplayStyle = {styles.dateDisplay}
                hamburgerMenuStyle= {styles.hamburgerMenu} 
                onMenuClick={this.onMenuClick} />

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
    backgroundColor: '#FFF0FF',
    width: screenWidth,
    height: screenHeight * 0.1, 
  },
  helpIcon: {
    height: null, 
    width: screenWidth * 0.1, 
    resizeMode: 'contain',
  },
  hamburgerMenu: {
    height: null, 
    width: screenWidth * 0.1, 
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
});
