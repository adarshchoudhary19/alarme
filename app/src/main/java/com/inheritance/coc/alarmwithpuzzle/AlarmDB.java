package com.inheritance.coc.alarmwithpuzzle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class AlarmDB extends SQLiteOpenHelper {
    final String sharedPrefs[] = {"ringtone", "snooze_time_secs", "puzzle_type", "difficulty"};

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "AlarmDB";
    private static final String DATABASE_NAME = "PuzzleAlarms.db";
    private static final String TABLE_Alarms = "Alarms";
    private static final String TABLE_GKQs = "GKQs";
    private final String Alarm_Tags[] = {"_id", "hour", "min", "switch_on", "msg", "repeat_days", "snooze_time_secs", "ringtone", "puzzle_type", "difficulty"};
    private final String GK_Tags[] = {"_id", "question", "opt1", "opt2", "opt3", "opt4", "correct_option"};
    final String ON_Status[] = {"on", "off"};
    //    final String Puzzle_types[] = {"random", "maths", "gk"};
    final int Difficulty[] = {1, 2, 3, 4, 5, 6, 7, 8};
    private int NO_OF_GK_QUES = 50;

    AlarmDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(
                "CREATE TABLE IF NOT EXISTS %s (%s INTEGER NOT NULL PRIMARY KEY, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER);",
                TABLE_Alarms, Alarm_Tags[0], Alarm_Tags[1], Alarm_Tags[2], Alarm_Tags[3], Alarm_Tags[4], Alarm_Tags[5], Alarm_Tags[6], Alarm_Tags[7], Alarm_Tags[8], Alarm_Tags[9]));
        db.execSQL(String.format(
                "CREATE TABLE IF NOT EXISTS %s (%s INTEGER NOT NULL PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                TABLE_GKQs, GK_Tags[0], GK_Tags[1], GK_Tags[2], GK_Tags[3], GK_Tags[4], GK_Tags[5], GK_Tags[6]));

        int i = 1;
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "What does the honey-bee make?", "Milk", "Water", "Honey", "Curd", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "What Does Our Heart Pump?", "Oxygen", "Blood", "Liquids", "Air", "2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "How many letters does the word \"VIOLIN\" have?", "Six", "Five", "Eight", "Seven", "1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "What is the 15th letter of the English alphabet?", "F", "I", "M", "O", "4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following organ is used to taste food?", "Tongue", "Lips", "Teeth", "Nose", "1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following is not an outdoor game?", "Cricket", "Football", "Hockey", "Table Tennis", "4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following is an outdoor game?", "Carom", "Chess", "Cricket", "Table Tennis", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Sun is a _____?", "Star", "Planet", "Comet", "Asteroid", "1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "How many sides are there in a pentagon?", "3", "4", "5", "7", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "How many sides are there in a Square?", "5", "3", "4", "6", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "In which season do we wear warm clothes?", "Autumn", "Spring", "Winter", "Summer", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Opposite of Tall is?", "Short", "Big", "Fat", "Thin", "1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which animal has a hump on its back?", "Horse", "Camel", "Elephant", "Giraffe", "2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Opposite of Happy is?", "Angry", "Weak", "Sad", "Weak", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "How many sides does a Triangle have?", "2", "3", "4", "5", "2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "There are how many hours in a day?", "60 Hours", "36 Hours", "24 Hours", "30 Hours", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which month has the least number of days?", "January", "April", "February", "March", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "We get Solar Energy from?", "Trees", "Sun", "Light", "Fire", "2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "The _____ is fine today.", "Whether", "Wether", "Weather", "Wheather", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "We breathe in Oxygen and breathe out _____.", "Nitrogen", "Chlorine", "Carbon Monoxide", "Carbon Dioxide", "4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following is not a domestic animal?", "Cat", "Dog", "Fox", "Goat", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following is correctly spelt?", "Competition", "Compitition", "Compitetion", "Competetion", "1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following letters will form word ORANGE?","GNAIOR", "EORMGA","NEAGOI","ROGAEN","4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "What gives us energy to work and play?", "Medicine", "Air", "Water", "Food","4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "During which festival Santa come to visit?", "Christmas", "New Year","Valentine" ,"Diwali","1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "What part of your body lets you see?", "Legs", "Eyes ","Fingers", "Tongue","2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "What language do the people of Japan speak?","English","Mandarin","Japanese","Pali","3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which color symbolises peace?", "White","Yellow", "Red", "Green","1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which is the nearest star to planet earth?", "Jupiter", "Saturn", "Sun", "Uranus","3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which is principle source of energy for earth?", "Water", "Air","Sun","Wind","3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "The normal boiling point of water is?", "150","100", "120","50","2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "How many colors are there in a rainbow?", "5", "7", "8", "12","2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "We use our eyes to?", "Hear", "Smell", "Feel", "See","4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Baby of Dog is called?", "Puppy", "Kitten", "Tadpole", "Cub","1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "How many Vowels are there in English alphabet?", "6", "7", "4","5","4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which planet consist of living creature?", "Mercury", "Moon", "Earth", "Jupiter","3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following is not a public transport?", "Bus", "Train", "Taxi", "Cycle", "4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which is seventh month of year?", "August", "July", "June", "September", "2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which thing is used to protect from rain water?", "Knife", "Spoon", "Clock", "Umbrella", "4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Name the place where we see many animals?", "Library", "School", "Zoo", "Aquarium", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which is the biggest planet of our solar system?", "Jupiter", "Venus", "Mercury", "Earth", "1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following bird cannot fly?", "Duck", "Pigeon", "Crow", "Parrot", "1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following is a fruit?", "Carrot", "Guava", "Peas", "Cauliflower","2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "A group of keys is called?", "School", "Flock", "Pack", "Bunch", "4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Write 6:00 P.M. as 24-hour time.", "18:00", "20:00", "14:00", "22:00", "1"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which word have nearly same meaning to the word GATHER?", "Throw", "Collect", "Fight", "Angry","2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "How many zeros are there in one hundred thousand?", "3", "4", "6", "5","4"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which product is used to protect eyes from sunlight?", "Soap", "Sunglasses", "Mobile", "Perfume", "2"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "A large area of land covered with sand is called?", "Ocean", "Island", "Desert", "Peninsula", "3"));
        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
                TABLE_GKQs, i++, "Which of the following word is correctly spelt?", "Flowars", "Fruiet", "Substance", "Piek", "3"));

//        db.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s VALUES (%d, '%s', '%s', '%s', '%s', '%s', '%s');",
//                TABLE_GKQs, i++, "Question", "Opt1", "Opt2", "Opt3", "Opt4", "Corr_Opt"));
//        db.close();
        Log.e(TAG, "onCreate: NO_OF_GK_QUES = "+NO_OF_GK_QUES );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // not to be used
    }

    public Alarm getAlarmObj(Context context, int _id) {
        Log.e(TAG, "getAlarmObj: _id = " + _id); //test
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor query = database.rawQuery("SELECT * FROM " + TABLE_Alarms + " WHERE _id = " + _id + ";", null);
        try {
            if (query.moveToFirst()) {
                Alarm alarm = getAlarmObj(context, query);
                query.close();
//                database.close();
                return alarm;
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "getAlarmObj: error = " + e);
        }
        return null;
    }

    private Alarm getAlarmObj(Context context, Cursor query) {
        Log.e(TAG, "getAlarmObj: Cursor");  //test
        Alarm alarm = new Alarm(context, query.getInt(query.getColumnIndex(Alarm_Tags[1])), query.getInt(query.getColumnIndex(Alarm_Tags[2])));
        alarm._id = query.getInt(query.getColumnIndex(Alarm_Tags[0]));
        Log.e(TAG, "getAlarmObj: alarm._id = " + alarm._id);  //test
        alarm.switch_on = query.getString(query.getColumnIndex(Alarm_Tags[3]));
        alarm.msg = query.getString(query.getColumnIndex(Alarm_Tags[4]));
        alarm.difficulty = query.getInt(query.getColumnIndex(Alarm_Tags[9]));
        alarm.ringtone = query.getInt(query.getColumnIndex(Alarm_Tags[7]));
        alarm.puzzle_type = query.getInt(query.getColumnIndex(Alarm_Tags[8]));
        alarm.snooze_time_secs = query.getInt(query.getColumnIndex(Alarm_Tags[6]));
        alarm.set_repeat_days(query.getString(query.getColumnIndex(Alarm_Tags[5])));
        alarm.log();
        return alarm;
    }

    public String[] get_GK_ques() {
        Log.e(TAG, "onCreate: NO_OF_GK_QUES = "+NO_OF_GK_QUES );
        // Do we need difficulty?? No
        String ques[] = new String[6];
        Random rand = new Random();
        int _id = rand.nextInt(NO_OF_GK_QUES) + 1;
        Log.e(TAG, "get_GK_ques: _id = " + _id);  //test
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor query = database.rawQuery("SELECT * FROM " + TABLE_GKQs + " WHERE _id = " + _id + ";", null);
        try {
            if (query.moveToFirst()) {
                ques[0] = query.getString(query.getColumnIndex(GK_Tags[1]));
                ques[1] = query.getString(query.getColumnIndex(GK_Tags[2]));
                ques[2] = query.getString(query.getColumnIndex(GK_Tags[3]));
                ques[3] = query.getString(query.getColumnIndex(GK_Tags[4]));
                ques[4] = query.getString(query.getColumnIndex(GK_Tags[5]));
                ques[5] = query.getString(query.getColumnIndex(GK_Tags[6]));
                query.close();
            }
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "get_GK_ques: error = " + e);
        }
//        database.close();
        return ques;
    }

    public ArrayList<Alarm> getAllAlarms(Context context) {
        Log.e(TAG, "getAllAlarms: in");  //test
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Alarm> alarmArrayList = new ArrayList<>();
        Cursor query = database.rawQuery("SELECT * FROM " + TABLE_Alarms + " ;", null);
        try {
            if (query.moveToFirst()) {
                do {
                    alarmArrayList.add(getAlarmObj(context, query));
                } while (query.moveToNext());
            }
            query.close();
            Log.e(TAG, "getAllAlarms: out");  //test
        } catch (Exception e) {
            e.getMessage();
            Log.e(TAG, "getAllAlarms: error = " + e);
        }
//        database.close();
        return alarmArrayList;
    }

    public int storeAlarm(Alarm alarm) {
        SQLiteDatabase database = this.getWritableDatabase();
        int _id;
        if (alarm._id == -1) {
            Cursor query = database.rawQuery("SELECT * FROM " + TABLE_Alarms + " ;", null);
            alarm._id = query.getCount() + 1;
            _id = alarm._id;
            query.close();
//            database.execSQL(String.format("INSERT INTO %s (%s) SELECT * FROM (SELECT '%s') AS tmp WHERE NOT EXISTS ( SELECT %s FROM %s WHERE %s = '%s' ) LIMIT 1;",
//                    TABLE_Alarms, Alarm_Tags[0], _id, Alarm_Tags[0], TABLE_Alarms, Alarm_Tags[0], _id));
            database.execSQL(String.format(Locale.ENGLISH, "INSERT INTO %s (%s) VALUES (%d);", TABLE_Alarms, Alarm_Tags[0], _id));
        }
        _id = alarm._id;
        Log.e(TAG, "storeAlarm: _id = " + Integer.toString(_id));  //test
//        database.execSQL(String.format("INSERT INTO %s (%s) SELECT * FROM (SELECT '%s') AS tmp WHERE NOT EXISTS ( SELECT %s FROM %s WHERE %s = '%s' ) LIMIT 1;",
//                TABLE_Alarms, Alarm_Tags[0], _id, Alarm_Tags[0], TABLE_Alarms, Alarm_Tags[0], _id));
        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = %d WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[1], alarm.hour, Alarm_Tags[0], _id));
        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = %d WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[2], alarm.min, Alarm_Tags[0], _id));
        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = %d WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[6], alarm.snooze_time_secs, Alarm_Tags[0], _id));
        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = %d WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[9], alarm.difficulty, Alarm_Tags[0], _id));
        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = %d WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[7], alarm.ringtone, Alarm_Tags[0], _id));
        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = %d WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[8], alarm.puzzle_type, Alarm_Tags[0], _id));

        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = '%s' WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[3], alarm.switch_on, Alarm_Tags[0], _id));
        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = '%s' WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[4], alarm.msg, Alarm_Tags[0], _id));
        database.execSQL(String.format(Locale.ENGLISH, "UPDATE %s SET %s = '%s' WHERE %s = %d ;", TABLE_Alarms, Alarm_Tags[5], alarm.get_repeat_day_string(), Alarm_Tags[0], _id));
//        database.close();
//        if(alarm.switch_on.equalsIgnoreCase("on")) {
//            alarm.set(MainScreen.this);
//        } else if(alarm.switch_on.equalsIgnoreCase("off")) {
//
//        } else {
//            Log.e(TAG, "storeAlarm: alarm.switch_on="+ alarm.switch_on);
//        }
        return alarm._id;
    }

    public void deleteAlarm(Alarm alarm) {
        deleteAlarm(alarm._id);
    }

    public void deleteAlarm(int _id) {
        Log.e(TAG, "deleteAlarm: _id = " + _id);  //test
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_Alarms + " WHERE _id = '" + _id + "';");
//        database.close();
    }

}
