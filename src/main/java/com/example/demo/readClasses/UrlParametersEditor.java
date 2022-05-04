package com.example.demo.readClasses;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParametersEditor {

    public String editStationName(String stationName) {
        StringBuilder editedStationName = new StringBuilder();
        int size = stationName.length();
        for (int i = 0; i < size; i++) {
            switch (stationName.charAt(i)) {
                case ' ':
                    editedStationName.append("%20");
                    break;
                case 'á':
                    editedStationName.append("%C3%A1");
                    break;
                case 'ä':
                    editedStationName.append("%C3%A4");
                    break;
                case 'č':
                    editedStationName.append("%C4%8D");
                    break;
                case 'ď':
                    editedStationName.append("%C4%8F");
                    break;
                case 'é':
                    editedStationName.append("%C3%A9");
                    break;
                case 'ě':
                    editedStationName.append("%C4%9B");
                    break;
                case 'í':
                    editedStationName.append("%C3%AD");
                    break;
                case 'ĺ':
                    editedStationName.append("%C4%BA");
                    break;
                case 'ľ':
                    editedStationName.append("%C4%BE");
                    break;
                case 'ň':
                    editedStationName.append("%C5%88");
                    break;
                case 'ó':
                    editedStationName.append("%C3%B3");
                    break;
                case 'ô':
                    editedStationName.append("%C3%B4");
                    break;
                case 'ŕ':
                    editedStationName.append("%C5%95");
                    break;
                case 'ř':
                    editedStationName.append("%C5%99");
                    break;
                case 'š':
                    editedStationName.append("%C5%A1");
                    break;
                case 'ť':
                    editedStationName.append("%C5%A5");
                    break;
                case 'ú':
                    editedStationName.append("%C3%BA");
                    break;
                case 'ů':
                    editedStationName.append("%C5%AF");
                    break;
                case 'ý':
                    editedStationName.append("%C3%BD");
                    break;
                case 'ž':
                    editedStationName.append("%C5%BE");
                    break;
                case 'Á':
                    editedStationName.append("%C3%81");
                    break;
                case 'Ä':
                    editedStationName.append("%C3%84");
                    break;
                case 'Č':
                    editedStationName.append("%C4%8C");
                    break;
                case 'Ď':
                    editedStationName.append("%C4%8E");
                    break;
                case 'É':
                    editedStationName.append("%C3%89");
                    break;
                case 'Ě':
                    editedStationName.append("%C4%9A");
                    break;
                case 'Í':
                    editedStationName.append("%C3%8D");
                    break;
                case 'Ĺ':
                    editedStationName.append("%C4%B9");
                    break;
                case 'Ľ':
                    editedStationName.append("%C4%BD");
                    break;
                case 'Ň':
                    editedStationName.append("%C5%87");
                    break;
                case 'Ó':
                    editedStationName.append("%C3%93");
                    break;
                case 'Ô':
                    editedStationName.append("%C3%94");
                    break;
                case 'Ŕ':
                    editedStationName.append("%C5%94");
                    break;
                case 'Ř':
                    editedStationName.append("%C5%98");
                    break;
                case 'Š':
                    editedStationName.append("%C5%A0");
                    break;
                case 'Ť':
                    editedStationName.append("%C5%A4");
                    break;
                case 'Ú':
                    editedStationName.append("%C3%9A");
                    break;
                case 'Ů':
                    editedStationName.append("%C5%AE");
                    break;
                case 'Ý':
                    editedStationName.append("%C3%9D");
                    break;
                case 'Ž':
                    editedStationName.append("%C5%BD");
                    break;
                default:
                    //TODO dorobit na ostatne znaky nejake opravenie
                    editedStationName.append(stationName.charAt(i));
            }

        }
        System.out.println(editedStationName.toString());
        return editedStationName.toString();
    }

    public boolean isTimeCorrect(String time){
        Pattern pattern = Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
        Matcher matcher = pattern.matcher(time);
        return matcher.find();
    }

    public boolean isDateCorrect(String date){
        Pattern pattern = Pattern.compile("^(?=\\d)(?:(?!(?:(?:0?[5-9]|1[0-4])(?:\\.|-|\\/)10(?:\\.|-|\\/)(?:1582))|(?:(?:0?[3-9]|1[0-3])(?:\\.|-|\\/)0?9(?:\\.|-|\\/)(?:1752)))(31(?!(?:\\.|-|\\/)(?:0?[2469]|11))|30(?!(?:\\.|-|\\/)0?2)|(?:29(?:(?!(?:\\.|-|\\/)0?2(?:\\.|-|\\/))|(?=\\D0?2\\D(?:(?!000[04]|(?:(?:1[^0-6]|[2468][^048]|[3579][^26])00))(?:(?:(?:\\d\\d)(?:[02468][048]|[13579][26])(?!\\x20BC))|(?:00(?:42|3[0369]|2[147]|1[258]|09)\\x20BC))))))|2[0-8]|1\\d|0?[1-9])([-.\\/])(1[012]|(?:0?[1-9]))\\2((?=(?:00(?:4[0-5]|[0-3]?\\d)\\x20BC)|(?:\\d{4}(?:$|(?=\\x20\\d)\\x20)))\\d{4}(?:\\x20BC)?)(?:$|(?=\\x20\\d)\\x20))?((?:(?:0?[1-9]|1[012])(?::[0-5]\\d){0,2}(?:\\x20[aApP][mM]))|(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2})?$");
        Matcher matcher = pattern.matcher(date);
        return matcher.find();
    }

    public boolean isMaxTransportsCorrect(String maxTransports){
        Pattern pattern = Pattern.compile("^([1-9]|10)$");
        Matcher matcher = pattern.matcher(maxTransports);
        return matcher.find();
    }
}
