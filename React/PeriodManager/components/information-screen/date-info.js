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

    var fireTheme = require('../../images/theme_fire.png');
    var grassTheme = require('../../images/theme_grass.png');
    var beachTheme = require('../../images/theme_beach.png');
    var theme;
    switch(this.props.theme) {
      case global.DateDetail.Background.Fire : theme = fireTheme; break;
      case global.DateDetail.Background.Grass: theme = grassTheme; break;
      case global.DateDetail.Background.Beach: theme = beachTheme; break;
    }

    return (
        <View
            style={this.props.givenStyle} >
            <Image 
              source={theme} 
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
