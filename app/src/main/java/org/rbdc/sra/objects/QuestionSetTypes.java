package org.rbdc.sra.objects;

/**
 * Created by jordanreed on 1/19/15.
 */
public class QuestionSetTypes {
    public static String HOUSEHOLD = "HOUSEHOLD";
    public static String AREA      = "AREA";

    public static int getTypeIndex(String dataType) {
        if (dataType == null) return 0;
        if (dataType.equals(HOUSEHOLD)) return 0;
        if (dataType.equals(AREA     )) return 1;
        return 0;
    }

    public static String getTypeFromIndex(int index) {
        switch (index) {
            case 0: return HOUSEHOLD;
            case 1: return AREA;
            default: return HOUSEHOLD;
        }
    }
}
