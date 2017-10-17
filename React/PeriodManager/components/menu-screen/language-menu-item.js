import React from 'react';
import { StyleSheet, View, TouchableNativeFeedback, Text, Image} from 'react-native';
import IMenuItem from './i-menu-item.js';

export default class SettingMenuItem extends IMenuItem {
  
constructor(props) {
      super(props);
      this.getContent = this.getContent.bind(this);
}

    getContent = () => {
        return (
            <View
                style={this.props.givenStyle}> 
                <View
                    style={{
                        flexDirection: 'row',
                        flex:3,
                        alignItems: 'center'}}>
                    <Text
                        style={{
                            flex: 2,
                            alignSelf: 'center',
                            textAlign: 'center'}}>Language</Text> 
                    <Image
                        style={{
                            flex: 1, 
                            resizeMode: 'contain',
                            alignSelf: 'center'}}
                        source={require('../../images/flag_english.png')} />
                </View>
            </View>
            );
    };
}