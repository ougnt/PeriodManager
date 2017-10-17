import React from 'react';
import { StyleSheet, View, TouchableNativeFeedback, Text} from 'react-native';
import IMenuItem from './i-menu-item.js';

export default class SummaryMenuItem extends IMenuItem {
  
constructor(props) {
      super(props);
      this.getContent = this.getContent.bind(this);
}

    getContent = () => {
        return (
            <View
                style={this.props.givenStyle}> 
                <Text>Summary</Text>
            </View>
            );
    };
}