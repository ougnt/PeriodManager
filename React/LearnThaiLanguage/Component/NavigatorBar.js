import React from 'react';
import { StyleSheet, Text, View, Image, Dimensions, StatusBar, TouchableHighlight } from 'react-native';

let screenHeight =  Dimensions.get('window').height;
let screenWidth  = Dimensions.get('window').width;

export default class NavigatorBar extends React.Component {
    
    render() {

        return (
            <View style={styles.buttonContainer}>  

                <TouchableNativeFeedback 
                    onPress={() => {this.onButtonClick('progress');}}>  
                   <Image
                       source={pregressImage} 
                       style={styles.button}>
                   </Image> 
                </TouchableNativeFeedback>
                <TouchableNativeFeedback 
                    onPress={() => {this.onButtonClick('lesson');}}>  
                    <Image
                        source={lessonImage}
                        style={styles.button}>
                    </Image>
                </TouchableNativeFeedback>
                <TouchableNativeFeedback 
                    onPress={() => {this.onButtonClick('goal');}}>  
                    <Image
                        source={goalImage}
                        style={styles.button}>
                    </Image>
                </TouchableNativeFeedback>
          </View>
        );
    };
}

const styles = StyleSheet.create({ 

  progressCycle: {
    margin: screenWidth * 0.1,
  },
});