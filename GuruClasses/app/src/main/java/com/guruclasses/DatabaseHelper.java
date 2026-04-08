package com.guruclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "guruclasses.db";
    private static final int DB_VERSION = 1;

    // Tables
    private static final String TABLE_ADMIN = "admin";
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_VIDEOS = "videos";
    private static final String TABLE_QUIZ = "quiz";
    private static final String TABLE_NOTES = "notes";
    private static final String TABLE_LIVE_CLASSES = "live_classes";
    private static final String TABLE_RESULTS = "results";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Admin table
        db.execSQL("CREATE TABLE " + TABLE_ADMIN + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)");

        // Students table
        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "full_name TEXT NOT NULL," +
                "mobile TEXT NOT NULL," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "class_name TEXT," +
                "joined_date TEXT)");

        // Videos table
        db.execSQL("CREATE TABLE " + TABLE_VIDEOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subject TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "link TEXT NOT NULL," +
                "description TEXT," +
                "added_date TEXT)");

        // Quiz table
        db.execSQL("CREATE TABLE " + TABLE_QUIZ + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subject TEXT NOT NULL," +
                "question TEXT NOT NULL," +
                "option_a TEXT NOT NULL," +
                "option_b TEXT NOT NULL," +
                "option_c TEXT NOT NULL," +
                "option_d TEXT NOT NULL," +
                "correct_answer TEXT NOT NULL)");

        // Notes table
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subject TEXT NOT NULL," +
                "title TEXT NOT NULL," +
                "link TEXT NOT NULL," +
                "description TEXT," +
                "added_date TEXT)");

        // Live Classes table
        db.execSQL("CREATE TABLE " + TABLE_LIVE_CLASSES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "subject TEXT NOT NULL," +
                "class_name TEXT NOT NULL," +
                "meet_link TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "time TEXT NOT NULL)");

        // Results table
        db.execSQL("CREATE TABLE " + TABLE_RESULTS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_id INTEGER NOT NULL," +
                "subject TEXT NOT NULL," +
                "score INTEGER NOT NULL," +
                "total INTEGER NOT NULL," +
                "date TEXT NOT NULL)");

        // Insert default admin
        ContentValues cv = new ContentValues();
        cv.put("username", "admin");
        cv.put("password", "admin123");
        db.insert(TABLE_ADMIN, null, cv);

        // Insert sample quiz questions
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIVE_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTS);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Sample Maths questions
        insertQuizQ(db, "Maths", "2 + 2 = ?", "3", "4", "5", "6", "B");
        insertQuizQ(db, "Maths", "5 x 5 = ?", "20", "25", "30", "15", "B");
        insertQuizQ(db, "Maths", "√144 = ?", "10", "14", "12", "11", "C");
        // Sample GK questions
        insertQuizQ(db, "GK", "Bharat ki Rajdhani kaun si hai?", "Mumbai", "Kolkata", "Delhi", "Chennai", "C");
        insertQuizQ(db, "GK", "Bharat ka Rashtriya Khel kaunsa hai?", "Cricket", "Hockey", "Football", "Kabaddi", "B");
        // Sample Science questions
        insertQuizQ(db, "Science", "Paani ka chemical formula kya hai?", "CO2", "H2O", "O2", "NaCl", "B");
        insertQuizQ(db, "Science", "Suraj ek kya hai?", "Grah", "Tara", "Chandra", "Dhoomketu", "B");
        // Sample Physics
        insertQuizQ(db, "Physics", "Prakash ki gati kitni hai?", "3x10^6 m/s", "3x10^8 m/s", "3x10^10 m/s", "3x10^4 m/s", "B");
        // Sample Biology
        insertQuizQ(db, "Biology", "DNA ka poora naam kya hai?", "Deoxyribose Nitrogen Acid", "Deoxyribonucleic Acid", "Diribose Nucleic Acid", "Double Nucleic Acid", "B");
        // Sample Chemistry
        insertQuizQ(db, "Chemistry", "NaCl kya hai?", "Cheeni", "Namak", "Sirka", "Pani", "B");
        // Sample Hindi
        insertQuizQ(db, "Hindi", "Hindi ki lipi kya hai?", "Roman", "Urdu", "Devanagari", "Gujarati", "C");
        // Sample English
        insertQuizQ(db, "English", "Plural of 'child' is?", "Childs", "Childes", "Children", "Childies", "C");
    }

    private void insertQuizQ(SQLiteDatabase db, String subject, String question,
                              String a, String b, String c, String d, String correct) {
        ContentValues cv = new ContentValues();
        cv.put("subject", subject);
        cv.put("question", question);
        cv.put("option_a", a);
        cv.put("option_b", b);
        cv.put("option_c", c);
        cv.put("option_d", d);
        cv.put("correct_answer", correct);
        db.insert(TABLE_QUIZ, null, cv);
    }

    // ===================== ADMIN =====================

    public boolean validateAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADMIN, null,
                "username=? AND password=?",
                new String[]{username, password}, null, null, null);
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    public boolean updateAdminPassword(String oldPass, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_ADMIN, null, "password=?",
                new String[]{oldPass}, null, null, null);
        if (cursor.getCount() == 0) { cursor.close(); return false; }
        cursor.close();
        ContentValues cv = new ContentValues();
        cv.put("password", newPass);
        int rows = db.update(TABLE_ADMIN, cv, "password=?", new String[]{oldPass});
        return rows > 0;
    }

    // ===================== STUDENTS =====================

    public boolean registerStudent(String fullName, String mobile, String username,
                                   String password, String className) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("full_name", fullName);
        cv.put("mobile", mobile);
        cv.put("username", username);
        cv.put("password", password);
        cv.put("class_name", className);
        cv.put("joined_date", java.text.DateFormat.getDateInstance().format(new java.util.Date()));
        long result = db.insert(TABLE_STUDENTS, null, cv);
        return result != -1;
    }

    public Cursor validateStudent(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_STUDENTS, null,
                "username=? AND password=?",
                new String[]{username, password}, null, null, null);
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, null, "username=?",
                new String[]{username}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_STUDENTS, null, null, null, null, null, "id DESC");
    }

    public int getStudentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_STUDENTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public boolean deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_STUDENTS, "id=?", new String[]{String.valueOf(studentId)});
        return rows > 0;
    }

    // ===================== VIDEOS =====================

    public boolean addVideo(String subject, String title, String link, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject", subject);
        cv.put("title", title);
        cv.put("link", link);
        cv.put("description", description);
        cv.put("added_date", java.text.DateFormat.getDateInstance().format(new java.util.Date()));
        long result = db.insert(TABLE_VIDEOS, null, cv);
        return result != -1;
    }

    public Cursor getAllVideos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_VIDEOS, null, null, null, null, null, "id DESC");
    }

    public Cursor getVideosBySubject(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (subject.equals("All")) {
            return getAllVideos();
        }
        return db.query(TABLE_VIDEOS, null, "subject=?", new String[]{subject}, null, null, "id DESC");
    }

    public boolean deleteVideo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_VIDEOS, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public int getVideoCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_VIDEOS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // ===================== QUIZ =====================

    public boolean addQuizQuestion(String subject, String question, String optA,
                                    String optB, String optC, String optD, String correct) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject", subject);
        cv.put("question", question);
        cv.put("option_a", optA);
        cv.put("option_b", optB);
        cv.put("option_c", optC);
        cv.put("option_d", optD);
        cv.put("correct_answer", correct);
        return db.insert(TABLE_QUIZ, null, cv) != -1;
    }

    public Cursor getQuizBySubject(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (subject.equals("All")) {
            return db.query(TABLE_QUIZ, null, null, null, null, null, "RANDOM()");
        }
        return db.query(TABLE_QUIZ, null, "subject=?", new String[]{subject},
                null, null, "RANDOM()");
    }

    public Cursor getAllQuestions() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_QUIZ, null, null, null, null, null, "subject");
    }

    public boolean deleteQuestion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_QUIZ, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public int getQuizCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_QUIZ, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // ===================== NOTES =====================

    public boolean addNote(String subject, String title, String link, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject", subject);
        cv.put("title", title);
        cv.put("link", link);
        cv.put("description", description);
        cv.put("added_date", java.text.DateFormat.getDateInstance().format(new java.util.Date()));
        return db.insert(TABLE_NOTES, null, cv) != -1;
    }

    public Cursor getNotesBySubject(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (subject.equals("All")) {
            return db.query(TABLE_NOTES, null, null, null, null, null, "id DESC");
        }
        return db.query(TABLE_NOTES, null, "subject=?", new String[]{subject}, null, null, "id DESC");
    }

    public Cursor getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NOTES, null, null, null, null, null, "id DESC");
    }

    public boolean deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NOTES, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ===================== LIVE CLASSES =====================

    public boolean addLiveClass(String subject, String className, String meetLink,
                                 String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject", subject);
        cv.put("class_name", className);
        cv.put("meet_link", meetLink);
        cv.put("date", date);
        cv.put("time", time);
        return db.insert(TABLE_LIVE_CLASSES, null, cv) != -1;
    }

    public Cursor getAllLiveClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LIVE_CLASSES, null, null, null, null, null, "id DESC");
    }

    public boolean deleteLiveClass(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_LIVE_CLASSES, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    // ===================== RESULTS =====================

    public boolean saveResult(int studentId, String subject, int score, int total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("student_id", studentId);
        cv.put("subject", subject);
        cv.put("score", score);
        cv.put("total", total);
        cv.put("date", java.text.DateFormat.getDateInstance().format(new java.util.Date()));
        return db.insert(TABLE_RESULTS, null, cv) != -1;
    }

    public Cursor getResultsByStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RESULTS, null, "student_id=?",
                new String[]{String.valueOf(studentId)}, null, null, "id DESC");
    }

    public int getTotalQuizByStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RESULTS +
                " WHERE student_id=?", new String[]{String.valueOf(studentId)});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public double getAvgScoreByStudent(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(CAST(score AS REAL)/total*100) FROM " +
                TABLE_RESULTS + " WHERE student_id=?", new String[]{String.valueOf(studentId)});
        cursor.moveToFirst();
        double avg = cursor.getDouble(0);
        cursor.close();
        return avg;
    }
}
