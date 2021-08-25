package com.mobdeve.s15.group1.attendancetrackerteacher;

import java.util.ArrayList;

public class ClassDataHelper {

    public static ArrayList<ClassModel> initializeData() {
        ArrayList<ClassModel> data = new ArrayList<>();
        data.add(new ClassModel(
                "0001",
                "MOBDEVE",
                "S12",
                "MOBILE DEVELOPMENT",
                32,
                true
        ));
        data.add(new ClassModel(
                "0002",
                "CCAPDEV",
                "S13",
                "WEB DEVELOPMENT",
                34,
                true
        ));
        data.add(new ClassModel(
                "0003",
                "MACHLRN",
                "S14",
                "INTRO TO MACHINE LEARNING",
                50,
                true
        ));
        data.add(new ClassModel(
                "0004",
                "GELITPH",
                "Y15",
                "LITERATURE IN THE PHILIPPINES",
                28,
                true
        ));
        data.add(new ClassModel(
                "0005",
                "COCIMIC",
                "EK16",
                "MICROPROCESSORS",
                38,
                true
        ));
        data.add(new ClassModel(
                "0006",
                "CIRLOGI",
                "EK17",
                "LOGIC CIRCUITS",
                35,
                true
        ));
        data.add(new ClassModel(
                "0007",
                "THSEC1A",
                "EK18",
                "THESIS 1",
                36,
                true
        ));
        return data;
    }

}
