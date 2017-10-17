import React from 'react';
import { StyleSheet, Text, View, Image, TouchableNativeFeedback } from 'react-native';
import DateDisplay from './information-screen-date-display.js';

export default class TitleBar extends React.Component {
  render() {
    return (
        <View
            style={this.props.givenStyle} > 
            <Image 
                source={require('../../images/question_mark_icon.png')} 
                style={this.props.helpIconStyle} />    
            
            <DateDisplay
                date = {this.props.date}
                givenStyle={this.props.dateDisplayStyle} />

            <TouchableNativeFeedback
                onPress={this.props.onMenuClick} >
                <Image
                    source={require('./../../images/hamburger_manu_icon.png')}
                    style={this.props.hamburgerMenuStyle} />
            </TouchableNativeFeedback>
            
        </View>
    );
  }
}
