package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Files.Song;
import app.pages.ArtistPage;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * The type Artist.
 */
public final class Artist extends ContentCreator {
    private ArrayList<Album> albums;
    private ArrayList<Merchandise> merch;
    private ArrayList<Event> events;
    @Getter
    @Setter
    private double songRevenue;
    @Getter
    @Setter
    private double merchRevenue;
    @Getter
    @Setter
    private Song mostProfitableSong;
    @Getter
    private LinkedHashMap<String, Integer> topAlbums;
    @Getter
    private LinkedHashMap<Song, Integer> topSongs;
    @Getter
    private LinkedHashMap<User, Integer> topUsers;
    @Getter
    private LinkedHashMap<Song, Double> mostProfitableSongs;
    @Getter
    private ArrayList<Notifications> notifications;
    @Getter
    private ArrayList<User> followingusers;

    /**
     * Instantiates a new Artist.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        merch = new ArrayList<>();
        events = new ArrayList<>();

        super.setPage(new ArtistPage(this));
        topAlbums = new LinkedHashMap<>();
        topSongs = new LinkedHashMap<>();
        topUsers = new LinkedHashMap<>();
        songRevenue = 0;
        merchRevenue = 0;
        mostProfitableSong = null;
        mostProfitableSongs = new LinkedHashMap<>();
        followingusers = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    /**
     * Gets albums.
     *
     * @return the albums
     */
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    /**
     * Gets merch.
     *
     * @return the merch
     */
    public ArrayList<Merchandise> getMerch() {
        return merch;
    }

    /**
     * Gets events.
     *
     * @return the events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Gets event.
     *
     * @param eventName the event name
     * @return the event
     */
    public Event getEvent(final String eventName) {
        for (Event event : events) {
            if (event.getName().equals(eventName)) {
                return event;
            }
        }

        return null;
    }

    /**
     * Gets album.
     *
     * @param albumName the album name
     * @return the album
     */
    public Album getAlbum(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }

        return null;
    }

    /**
     * Gets all songs.
     *
     * @return the all songs
     */
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        albums.forEach(album -> songs.addAll(album.getSongs()));

        return songs;
    }

    /**
     * Show albums array list.
     *
     * @return the array list
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutput = new ArrayList<>();
        for (Album album : albums) {
            albumOutput.add(new AlbumOutput(album));
        }

        return albumOutput;
    }

    /**
     * Get user type
     *
     * @return user type string
     */
    public String userType() {
        return "artist";
    }

    /**
     * Accepts a WrappedVisitor and invokes the visit method, returning the result.
     *
     * @param v The WrappedVisitor to accept.
     * @return An {@link ObjectNode} containing the result of the visit.
     */
    public ObjectNode accept(final WrappedVisitor v) {
        return v.visit(this);
    }

    /**
     * Accepts a GetNotificationsVisitor and invokes the visit method, returning the result.
     *
     * @param v The GetNotificationsVisitor to accept.
     * @return An {@link ObjectNode} containing the result of the visit.
     */
    public ObjectNode accept(final GetNotificationsVisitor v) {
        return v.visit(this);
    }

    /**
     * Checks if a song with the specified artist and name is present in the top songs.
     *
     * @param artist The artist of the song.
     * @param name   The name of the song.
     * @return The Song object if found; otherwise, returns null.
     */
    public Song containsSongInTopSongs(final String artist, final String name) {
        for (Song song : topSongs.keySet()) {
            if (song.getArtist().equals(artist) && song.getName().equals(name)) {
                return song;
            }
        }
        return null;
    }

    /**
     * Updates the most profitable song based on revenue.
     */
    public void updateMostProfitableSong() {
        Song maxProfitSong = null;
        double maxProfit = Double.MIN_VALUE;

        for (Map.Entry<Song, Double> entry : mostProfitableSongs.entrySet()) {
            if (entry.getValue() > maxProfit) {
                maxProfit = entry.getValue();
                maxProfitSong = entry.getKey();
            } else if (entry.getValue() == maxProfit) {
                if (maxProfitSong != null && entry.getKey().getName()
                        .compareTo(maxProfitSong.getName()) < 0) {
                    maxProfitSong = entry.getKey();
                }
            }
        }
        mostProfitableSong = maxProfitSong;
    }

    /**
     * Updates users' information (name and description).
     *
     * @param name        The new name.
     * @param description The new description.
     */
    public void updateusers(final String name, final String description) {
        for (User user : followingusers) {
            user.update(name, description);
        }
    }

    /**
     * Gets the top fans of the artist based on the number of liked songs.
     *
     * @param numberOfFans The number of top fans to retrieve.
     * @return A list of top fans.
     */
    public List<User> getTopFans(final int numberOfFans) {
        Map<User, Integer> userSongLikeCount = new HashMap<>();

        for (User user : Admin.getInstance().getUsers()) {
            for (Song likedSong : user.getLikedSongs()) {
                String artistName = likedSong.getArtist();

                userSongLikeCount.put(user, userSongLikeCount.getOrDefault(user, 0)
                        + (getAllSongsFromArtist(artistName).contains(likedSong) ? 1 : 0));
            }
        }

        List<User> sortedUsers = userSongLikeCount.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return sortedUsers;
    }


    private List<Song> getAllSongsFromArtist(final String artistName) {
        List<Song> songsFromArtist = new ArrayList<>();
        for (User user : Admin.getInstance().getUsers()) {
            for (Song likedSong : user.getLikedSongs()) {
                if (likedSong.getArtist().equals(artistName)) {
                    songsFromArtist.add(likedSong);
                }
            }
        }
        return songsFromArtist;
    }

    /**
     * Checks if a song with the specified artist, name,
     * and album is present in most profitable songs.
     *
     * @param artist The artist of the song.
     * @param name   The name of the song.
     * @param album  The album of the song.
     * @return The Song object if found; otherwise, returns null.
     */
    public Song containsSongmostprofitablesongs(final String artist,
                                                final String name,
                                                final String album) {
        for (Song song : mostProfitableSongs.keySet()) {
            if (song.getArtist().equals(artist) && song.getName().equals(name)) {
                return song;
            }
        }
        return null;
    }
    @Override
    public String getKeyRepresentation() {
        return getUsername();
    }
}
