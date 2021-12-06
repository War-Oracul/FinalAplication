package com.example.myapplication;

import android.provider.BaseColumns;

public class DataBaseContract {
    private DataBaseContract() {}
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME_1 = "USERS";
        public static final String T1_ATR1 = "U_ID";
        public static final String T1_ATR2 = "U_LOGIN";
        public static final String T1_ATR3 = "U_PASSWORD";
    }
    public static class DataEntry implements BaseColumns {
        public static final String TABLE_NAME_2 = "DATA";
        public static final String T2_ATR1 = "D_ID";
        public static final String T2_ATR2 = "D_U_ID";
        public static final String T2_ATR3 = "D_NAME";
        public static final String T2_ATR4 = "D_TYPE";
        public static final String T2_ATR5 = "D_EDITION";
    }
}
