import React from 'react';
import { StyleSheet, View, TouchableNativeFeedback, Image, FlatList, Text} from 'react-native';

export default class DateScroller extends React.Component {
    render() {
        return (
            <View
                style={this.props.givenStyle}>
                <Image 
                    source={require('../../images/left_edge.png')}
                    style={this.props.dateScrollerLeftEdgeStyle} /> 
                <View
                    style={this.props.dateScrollerMidEdgeStyle}>
                    <Image
                        source={require('../../images/side_bar.png')}
                        style={this.props.dateScrollerSideBarStyle} />
                    <View 
                        style={{flex: 2}}> 
                        <FlatList
                            onScroll={() => {}} 
                            horizontal={true}  
                            style={{flex: 2, backgroundColor: '#FFFFFF'}} 
                            data={[{key: ' item 1 '}, {key: ' item 2 '},{key: ' item 3 '}, {key: ' item 4 '},{key: ' item 5 '}, {key: ' item 6 '}, {key: ' item 7 '}, {key: ' item 8 '}, {key: ' item 9 '}, {key: ' item 10 '}, {key: ' item 11 '}]}
                            renderItem={({item}) => <View style={{flex: 1, backgroundColor: '#0f0fFf'}}><Text>{item.key}</Text><Text>{item.key}</Text></View>} />
                    </View> 
                    <Image
                        source={require('../../images/side_bar.png')}
                        style={this.props.dateScrollerSideBarStyle} />
                </View>
                <Image 
                    source={require('../../images/right_edge.png')}
                    style={this.props.dateScrollerRightEdgeStyle} />
            </View>
        );
    }
}