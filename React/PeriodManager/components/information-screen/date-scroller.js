import React from 'react';
import { StyleSheet, View, TouchableNativeFeedback, Image, FlatList, Text} from 'react-native';
import DateMeter from './date-meter.js';

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
                            showsHorizontalScrollIndicator={false}
                            style={{flex: 2, backgroundColor: '#646464'}} 
                            data={[
                                {key: '20170628', day: '27', month: 'Jun', isToday: 0, iconFlags: global.Icon.Emotion.NormalFlag | global.Icon.Condition.PeriodFlag, bgColor: global.BgColor.Nothing},
                                {key: '20170629', day: '29', month: 'Jun', isToday: 1, iconFlags: global.Icon.Emotion.AngryFlag  | global.Icon.Condition.ExactPeriodFlag, bgColor: global.BgColor.Ovulation},
                                {key: '20170630', day: '30', month: 'Jun', isToday: 0, iconFlags: global.Icon.Emotion.SadFlag    | global.Icon.Condition.NoteFlag | global.Icon.Condition.IntercourseFlag | global.Icon.Condition.OvulationFlag, bgColor: global.BgColor.Period},
                                {key: '20170701', day: '1' , month: 'Jul', isToday: 0, iconFlags: global.Icon.Emotion.HappyFlag , bgColor: global.BgColor.Nothing},
                                {key: '20170702', day: '2' , month: 'Jul', isToday: 0, iconFlags: global.Icon.Emotion.StressFlag, bgColor: global.BgColor.Nothing}, 
                                {key: '20170703', day: '3' , month: 'Jul', isToday: 0, iconFlags: global.Icon.Emotion.StressFlag, bgColor: global.BgColor.Nothing}]} 
                            renderItem={({item}) => 
                                <DateMeter
                                    day={item.day}
                                    month={item.month}
                                    bgColor={item.bgColor}
                                    iconFlags={item.iconFlags}
                                    dateMeterWidth={this.props.dateMeterWidth}
                                    fontColor={this.props.fontColor}
                                    fontColorToday={this.props.fontColorToday}
                                    isToday={item.isToday} />
                            } />
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