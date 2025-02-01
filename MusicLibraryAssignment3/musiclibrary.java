package Nursayat_assignment3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class Song {
    int id;
    String title;
    String artist;
    int duration;

    public Song(int id, String title, String artist, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " by " + artist + " (" + duration + "s)";
    }
}


class MusicLibraryDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/musiclibrary";
    private static final String USER = "postgres";
    private static final String PASSWORD = "dagis.04";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createTables() {
        String createSongsTable = "CREATE TABLE IF NOT EXISTS songs (" +
                "id SERIAL PRIMARY KEY, " +
                "title VARCHAR(255) NOT NULL, " +
                "artist VARCHAR(255) NOT NULL, " +
                "duration INT NOT NULL);";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createSongsTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addSong(String title, String artist, int duration) {
        String query = "INSERT INTO songs (title, artist, duration) VALUES (?, ?, ?);";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            pstmt.setInt(3, duration);
            pstmt.executeUpdate();
            System.out.println("Song added: " + title);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT * FROM songs;";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                songs.add(new Song(rs.getInt("id"), rs.getString("title"), rs.getString("artist"), rs.getInt("duration")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    public static void updateSong(int id, String newTitle, String newArtist, int newDuration) {
        String query = "UPDATE songs SET title = ?, artist = ?, duration = ? WHERE id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newArtist);
            pstmt.setInt(3, newDuration);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Song updated with ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSong(int id) {
        String query = "DELETE FROM songs WHERE id = ?;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Deleted song with ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


public class musiclibrary {
    public static void main(String[] args) {
        MusicLibraryDB.createTables();


        MusicLibraryDB.addSong("Shape of You", "Ed Sheeran", 234);
        MusicLibraryDB.addSong("Blinding Lights", "The Weeknd", 200);


        System.out.println("All Songs:");
        for (Song song : MusicLibraryDB.getAllSongs()) {
            System.out.println(song);
        }


        MusicLibraryDB.updateSong(2, "Save Your Tears", "The Weeknd", 215);


        System.out.println("All Songs After Update:");
        for (Song song : MusicLibraryDB.getAllSongs()) {
            System.out.println(song);
        }


        MusicLibraryDB.deleteSong(1);


        System.out.println("All Songs After Deletion:");
        for (Song song : MusicLibraryDB.getAllSongs()) {
            System.out.println(song);
        }
    }
}
