package org.rbdc.sra.objects;

/**
 * Created by jordanreed on 1/7/15.
 */
public class DatapointTypes {
    public static String TEXT   = "Text";
    public static String NUMBER = "Number";
    public static String DATE   = "Date";
    public static String LIST_SINGLE_ANSWER = "List Single Answer";
    public static String LIST_MULTI_ANSWER  = "List Multi Answer";

    public static int getTypeIndex(String dataType) {
        if (dataType.equals(DatapointTypes.TEXT)) return 0;
        if (dataType.equals(DatapointTypes.NUMBER)) return 1;
        if (dataType.equals(DatapointTypes.DATE)) return 2;
        if (dataType.equals(DatapointTypes.LIST_SINGLE_ANSWER)) return 3;
        if (dataType.equals(DatapointTypes.LIST_MULTI_ANSWER)) return 4;
        return 0;
    }

    public static String getTypeFromIndex(int index) {
        switch (index) {
            case 0: return TEXT;
            case 1: return NUMBER;
            case 2: return DATE;
            case 3: return LIST_SINGLE_ANSWER;
            case 4: return LIST_MULTI_ANSWER;

            default: return TEXT;
        }
    }
}
