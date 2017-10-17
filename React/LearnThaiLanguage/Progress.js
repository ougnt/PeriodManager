import React from 'react';
import { StyleSheet, Text, View, Image, Dimensions, StatusBar, TouchableHighlight } from 'react-native';
import * as Progress from 'react-native-progress';
import ProgressView from './Component/ProgressView.js';

let screenHeight =  Dimensions.get('window').height;
let screenWidth  = Dimensions.get('window').width;

export default class ProgressScreen extends React.Component {
    
    render() {

        return (
            <View> 
                <View
                    flexDirection='row'>
                    <ProgressView 
                        progress={this.props.learntWords}
                        text='Learnt Words'/>
                    <ProgressView
                        progress={this.props.learntGramma}
                        text='Learnt Grammas' /> 
                </View>
                <View
                    flexDirection='row'>
                    <ProgressView 
                        progress={this.props.dailyGoal}
                        text='Daily Goal'/>
                    <ProgressView
                        progress={this.props.overallGoal}
                        text='Overall Goal' /> 
                </View>
            </View> 
        );
    };
}

const styles = StyleSheet.create({ 

  progressCycle: {
    margin: screenWidth * 0.1,
  },
});