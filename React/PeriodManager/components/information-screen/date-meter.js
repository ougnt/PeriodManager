import React from 'react';
import { StyleSheet, View, TouchableNativeFeedback, Platform, Image, FlatList, Text, Dimensions, PixelRatio} from 'react-native';

global.BgColor = { Period : '#FFBBD4', Ovulation : '#99FFD4', Nothing : '#CAFFFF'};
global.Icon = { 
    Emotion : {
        EmotionFlags    : 0x0000FFFF, 
        NormalFlag      : 0x00000001, 
        AngryFlag	    : 0x00000002, 
        SadFlag	        : 0x00000004, 
        HappyFlag	    : 0x00000008, 
        StressFlag	    : 0x00000010,
    },
    Condition : {
        ConditionFlags  : 0xFFFF0000,
        IntercourseFlag	: 0x00010000,
        PeriodFlag	    : 0x00020000,
        ExactPeriodFlag	: 0x00040000,
        OvulationFlag	: 0x00080000,
        NoteFlag        : 0x00100000,
    }
};

export default class DateMeter extends React.Component {
    render() {
        return (
            <View style={this.styles.container}> 
                <View
                    style={this.styles.datePart}>
                	<View
                        style={this.styles.dayPart}>
                        <Text style={{
                            color: this.getFontColor(this.props.isToday), 
                            fontSize: this.normalize(30)}}>{this.props.day}</Text>
                    </View>
                    <View
                        style={this.styles.monthPart}> 
                        <Text style={{color: this.getFontColor(this.props.isToday), fontSize: this.normalize(14)}}>Thu</Text> 
                        <Text style={{color: this.getFontColor(this.props.isToday), fontSize: this.normalize(18)}}>{this.props.month}</Text> 
                    </View>
                </View> 
                <View style={{flexDirection: 'row'}}>
                    {this.getDayConditionIcon(this.props.iconFlags)}
                    {this.getEmotionIcon(this.props.iconFlags)}
                    {this.getIntercourseIcon(this.props.iconFlags)}
                    {this.getNoteIcon(this.props.iconFlags)}
                </View>  
            </View>
        );
    }

    getFontColor = (isToday) => {
        return(this.props.isToday ? 
            this.props.fontColorToday : 
            this.props.fontColor); 
    }

    getNoteIcon = (flags) => {
        if(flags & global.Icon.Condition.ConditionFlags & global.Icon.Condition.NoteFlag) {
            return(<Image style={this.styles.icon} source={require('../../images/pencil_icon.png')} />);
        }
    }

    getIntercourseIcon = (flags) => {
        if(flags & global.Icon.Condition.ConditionFlags & global.Icon.Condition.IntercourseFlag) {
            return(<Image style={this.styles.icon} source={require('../../images/intercourse_icon.png')} />);
        }
    }

    getDayConditionIcon = (flags) => {
        iconFlag = flags & global.Icon.Condition.ConditionFlags;
        if(iconFlag & global.Icon.Condition.PeriodFlag) { 
            return(
                <View style={{flexDirection: 'row'}}> 
                    <Image style={this.styles.icon} source={require('../../images/menstrual_icon.png')} />
                    <Image style={this.styles.icon} source={require('../../images/non_ovulation_icon.png')} />
                </View>);
        }

        if(iconFlag & global.Icon.Condition.ExactPeriodFlag) {
            return(
                <View style={{flexDirection: 'row'}}>
                    <Image style={this.styles.icon} source={require('../../images/estimated_menstrual_icon.png')} />
                    <Image style={this.styles.icon} source={require('../../images/non_ovulation_icon.png')} />
                </View>);
        }

        if(iconFlag & global.Icon.Condition.OvulationFlag) {
            return(<Image style={this.styles.icon} source={require('../../images/ovulation_icon.png')} />);
        }
        else {
            return(<Image style={this.styles.icon} source={require('../../images/non_ovulation_icon.png')} />);
        }
    }

    getEmotionIcon = (flags) => {
        iconFlag = flags & global.Icon.Emotion.EmotionFlags;
        switch(iconFlag) {
            case global.Icon.Emotion.NormalFlag: return(<Image style={this.styles.icon} source={require('../../images/emotion_nothing.png')} />);
            case global.Icon.Emotion.AngryFlag: return(<Image style={this.styles.icon}  source={require('../../images/emotion_angry.png')} />);
            case global.Icon.Emotion.SadFlag: return(<Image style={this.styles.icon}  source={require('../../images/emotion_sad.png')} />);
            case global.Icon.Emotion.StressFlag: return(<Image style={this.styles.icon}  source={require('../../images/emotion_stress.png')} />);
            case global.Icon.Emotion.HappyFlag: return(<Image style={this.styles.icon}  source={require('../../images/emotion_happy.png')} />);
        }
        return(<Image style={this.styles.icon} source={require('../../images/emotion_nothing.png')} />);
    }

    normalize = (size) => {
        if (Platform.OS === 'ios') {
            return Math.round(PixelRatio.roundToNearestPixel(size))
        } else {
            return Math.round(PixelRatio.roundToNearestPixel(size)) - 2
        }
    }

    styles = StyleSheet.create({ 
        container: {
            flex: 4,
            width: this.props.dateMeterWidth,
            flexDirection: 'column',
            margin: 2,
            backgroundColor: this.props.bgColor,
            alignItems: 'flex-start',
            justifyContent: 'flex-start',
        },
        datePart: {
            flex: 3,
            flexDirection: 'row',
        },
        dayPart: {
            flex: 2,
            alignItems: 'center',
            justifyContent: 'center',
        },
        monthPart: {
            flex: 1,
        },
        icon: {
            resizeMode: 'stretch',
            height: this.props.dateMeterWidth * 0.2,
            width: this.props.dateMeterWidth * 0.2,
        },
    });
}

