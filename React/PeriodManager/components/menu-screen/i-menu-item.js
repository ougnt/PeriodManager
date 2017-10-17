import React from 'react';
import { StyleSheet, View, TouchableNativeFeedback} from 'react-native';


export default class IMenuItem extends React.Component {
  
  getContent = () => {};
  
  render() {
    return (
        <TouchableNativeFeedback
            onPress={ () => this.props.onClick(this.props.itemName)}>
            {this.getContent()}
        </TouchableNativeFeedback>
    );
  }
}