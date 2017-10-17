import React from 'react';
import { StyleSheet, View, TouchableNativeFeedback, Text} from 'react-native';

import SettingMenuItem from './setting-menu-item.js';
import SummaryMenuItem from './summary-menu-item.js';
import LanguageMenuItem from './language-menu-item.js';
import SubmitReviewMenuItem from './submit-review-menu-item.js';

export default class Menu extends React.Component {
  clickAlert = (alertText) => {
      alert(alertText);
  }

  render() {
    return (
        <TouchableNativeFeedback
            onPress={this.props.outsideClick}> 
            <View
                style={this.props.givenStyle}>
                <View
                    style={this.props.menuItemStyle}>
                    <Text>Menu</Text>
                </View> 

                <SettingMenuItem 
                    itemName='SettingMenu'
                    onClick={this.clickAlert} 
                    givenStyle={this.props.menuItemStyle} />

                <SummaryMenuItem
                    itemName='SummaryMenu'
                    onClick={this.clickAlert} 
                    givenStyle={this.props.menuItemStyle} />

                <LanguageMenuItem
                    itemName='LanguageMenu'
                    onClick={this.clickAlert}
                    givenStyle={this.props.menuItemStyle} />

                <SubmitReviewMenuItem
                    itemName='SubmitReviewMenu'
                    onClick={this.clickAlert}
                    givenStyle={this.props.menuItemStyle} />
            </View>
        </TouchableNativeFeedback>
    );
  }
}