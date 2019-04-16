package io.oskin;

import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    public static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/notes";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "yuramega070898";

    public static final String NOTE_ID = "NOTE_ID";
    public static final String NOTE_NAME = "NOTE_NAME";
    public static final String NOTE_TEXT = "NOTE_TEXT";
    public static final String NOTES_TABLE = "NOTES_TABLE";

    public Storage() {
        try {
            createDBNotesTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }

    private void createDBNotesTable() throws SQLException {
        Connection dbConnection = null;
        Statement statement = null;

        String createTableSQL = "CREATE TABLE " + NOTES_TABLE + "("
                + NOTE_ID + " VARCHAR(1000) NOT NULL, "
                + NOTE_NAME + " VARCHAR(1000) NOT NULL, "
                + NOTE_TEXT + " VARCHAR(1000) NOT NULL, "
                + "PRIMARY KEY (" + NOTE_ID + ") "
                + ")";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выполнить SQL запрос
            statement.execute(createTableSQL);
            System.out.println("Table \"" + NOTES_TABLE + "\" is created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        }
    }

    public void insertNote(Note note) {
        Connection dbConnection = null;
        Statement statement = null;
        String insertTableSQL = "INSERT INTO " + NOTES_TABLE
                + " VALUES"
                + "('" + note.getmId() + "','" + note.getmName() + "','" + note.getmText() + "')";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(insertTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNote(Note note) {
        Connection dbConnection = null;
        Statement statement = null;
        String updateTableSQL = "UPDATE " + NOTES_TABLE +
                " SET " + NOTE_NAME + " = '" + note.getmName() + "', " + NOTE_TEXT + " = '" + note.getmText() + "'" +
                " WHERE " + NOTE_ID + " = '" + note.getmId() + "'";
        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();
            statement.executeUpdate(updateTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNotesJson() {
        Connection dbConnection = null;
        Statement statement = null;

        Gson gson = new Gson();

        String selectTableSQL = "SELECT * FROM " + NOTES_TABLE;
        ArrayList<Note> noteArrayList = new ArrayList<>();

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выбираем данные с БД
            ResultSet rs = statement.executeQuery(selectTableSQL);

            // И если что то было получено то цикл while сработает
            while (rs.next()) {
                Note note = new Note();
                note.setmId(rs.getString(NOTE_ID));
                note.setmName(rs.getString(NOTE_NAME));
                note.setmText(rs.getString(NOTE_TEXT));
                noteArrayList.add(note);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return gson.toJson(noteArrayList);
    }

    public String getNotesJsonByID(String id) {
        Connection dbConnection = null;
        Statement statement = null;

        Gson gson = new Gson();

        Note note = new Note();

        String selectTableSQL = "SELECT * FROM " + NOTES_TABLE + " WHERE " + NOTE_ID + " = '" + id + "'";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выбираем данные с БД
            ResultSet rs = statement.executeQuery(selectTableSQL);

            // И если что то было получено то цикл while сработает
            while (rs.next()) {
                note.setmId(rs.getString(NOTE_ID));
                note.setmName(rs.getString(NOTE_NAME));
                note.setmText(rs.getString(NOTE_TEXT));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return gson.toJson(note);
    }

    public void deleteNote(Note note) {
        Connection dbConnection = null;
        Statement statement = null;

        String deleteTableSQL = "DELETE FROM " + NOTES_TABLE + " WHERE " + NOTE_ID + " = '" + note.getmId() + "'";

        try {
            dbConnection = getDBConnection();
            statement = dbConnection.createStatement();

            // выполняем запрос delete SQL
            statement.execute(deleteTableSQL);
            System.out.println("Record is deleted!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
