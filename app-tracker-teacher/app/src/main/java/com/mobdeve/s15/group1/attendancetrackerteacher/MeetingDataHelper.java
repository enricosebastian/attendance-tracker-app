package com.mobdeve.s15.group1.attendancetrackerteacher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MeetingDataHelper {

    public static ArrayList<MeetingModel> initializeData() {
        ArrayList<MeetingModel> data = new ArrayList<>();
        data.add(new MeetingModel(
                "0001",
                new GregorianCalendar(2021, Calendar.JANUARY, 11).getTime(),
                52
        ));
        data.add(new MeetingModel(
                "0002",
                new GregorianCalendar(2021, Calendar.JANUARY, 12).getTime(),
                51
        ));
        data.add(new MeetingModel(
                "0003",
                new GregorianCalendar(2021, Calendar.JANUARY, 13).getTime(),
                54
        ));
        data.add(new MeetingModel(
                "0004",
                new GregorianCalendar(2021, Calendar.JANUARY, 14).getTime(),
                51
        ));
        data.add(new MeetingModel(
                "0005",
                new GregorianCalendar(2021, Calendar.JANUARY, 15).getTime(),
                55
        ));
        return data;
    }

}
