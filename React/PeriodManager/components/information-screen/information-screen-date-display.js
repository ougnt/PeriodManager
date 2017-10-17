import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

export default class DateDisplay extends React.Component {
  render() {
    return (
        <Text
        style={this.props.givenStyle}>{this.props.date}</Text> 
    );
  }
}