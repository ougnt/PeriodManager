import React from 'react';
import { StyleSheet, Text, View, Image } from 'react-native';

global.DateDetail = {
  Background : {
    Fire : 1,
    Beach : 2,
    Grass : 3,
  }
}

export default class DateInfo extends React.Component {

  render() {
    return (
        <View
            style={this.props.givenStyle} > 
            <Image 
              source={require('../../images/theme_fire.png')}
              style={this.props.themeStyle} >
              <Image
                source={require('../../images/date_detail.png')}
                style={this.props.themeStyle} >
                <Text style={this.props.textStyle}>{this.props.infoText}</Text>
              </Image>
            </Image>
            
        </View>
    );
  }

  // getBackground = (background) => {
  //   switch(background) {
  //     case global.DateDetail.Background.Fire : return();
  //   }
  // }
}
