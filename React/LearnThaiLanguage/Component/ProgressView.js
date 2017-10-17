import React from 'react';
import { StyleSheet, Text, View, Image, Dimensions, StatusBar, TouchableHighlight } from 'react-native';
import * as Progress from 'react-native-progress';


let screenHeight =  Dimensions.get('window').height;
let screenWidth  = Dimensions.get('window').width;

export default class ProgressView extends React.Component {
    
    render() {

        return (
            <View
                alignItems='center'> 
                    <Progress.Circle  
                        size={screenWidth * 0.3} 
                        progress= {this.props.progress}
                        thickness={screenWidth * 0.02} 
                        style={styles.progressCycle}
                        showsText={true}
                        animated={false}
                        color='#AAAAAA'
                        unfilledColor='#FFFFFF' />
                    <Text
                        alignItems='center'>
                        {this.props.text}
                    </Text>
            </View>
        );
    };
}

const styles = StyleSheet.create({ 

  progressCycle: {
    margin: screenWidth * 0.1,
  },
});