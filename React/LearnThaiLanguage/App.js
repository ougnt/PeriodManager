import React from 'react';
import { StyleSheet, Text, View, Image, Dimensions, StatusBar, TouchableHighlight } from 'react-native';

export default class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {clickedButton: 'progress'};
  }

  onButtonClick = (clickedButton) => {  
    this.setState({clickedButton: clickedButton}); 
  }

  render() {
    
    let pregressImage = this.state.clickedButton == 'progress'? 
      require('./img/progress-button-push.png') : 
      require('./img/progress-button.png');

    let lessonImage = this.state.clickedButton == 'lesson'? 
      require('./img/lesson-button-push.png') : 
      require('./img/lesson-button.png');
    
    let goalImage = this.state.clickedButton == 'goal'? 
      require('./img/goal-button-push.png') : 
      require('./img/goal-button.png'); 

    return (
      <View
        style={styles.container}> 
      <StatusBar hidden={true} /> 
        <Image  
          source={require('./img/main_bg.png')}
          style={styles.bgImage}>
          <Image 
            source={require('./img/title-bar.png')} 
            style={styles.titleBar}> 
            <Text
              style={styles.titleLabel}>
              Thai 101
            </Text> 
            <Image 
              source={require('./img/gear.png')}
              style={styles.settingIcon}
            />
          </Image>
          <View style={styles.buttonContainer}>  
            <TouchableHighlight 
              onPress={() => {this.onButtonClick('progress');}}>  
              <Image
                source={pregressImage} 
                style={styles.button}>
              </Image> 
            </TouchableHighlight>
            <TouchableHighlight 
              onPress={() => {this.onButtonClick('lesson');}}>  
              <Image
                source={lessonImage}
                style={styles.button}>
              </Image>
            </TouchableHighlight>
            <TouchableHighlight 
              onPress={() => {this.onButtonClick('goal');}}>  
              <Image
                source={goalImage}
                style={styles.button}>
              </Image>
            </TouchableHighlight>
          </View>     
        </Image> 
      </View> 
    ); 
  }
}


let screenHeight =  Dimensions.get('window').height;
let screenWidth  = Dimensions.get('window').width;

const styles = StyleSheet.create({

  container: {
    flex: 1,
    backgroundColor: '#CCFFCC',
    alignItems: 'stretch',
    justifyContent: 'flex-end',
  },
  bgImage: {
    width: screenWidth,
    height: screenHeight,
    alignItems: 'stretch',
    justifyContent: 'flex-start',
  },
  titleBar: {
    width: screenWidth,
    height: screenHeight * 0.1,
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'row',
  },
  titleLabel: {
  },
  buttonContainer: {
    width: screenWidth,
    height: screenHeight * 0.1,
    flexDirection: 'row',
  },
  button: {
    width: screenWidth / 3,
    height: screenHeight * 0.1,
    resizeMode: 'stretch',
  },
  settingIcon: {
    width: screenWidth * 0.08,
    height: screenHeight * 0.08,
    position: 'absolute',
    left: screenWidth * 0.9,
    resizeMode: 'stretch',
  },
});
