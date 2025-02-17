package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.pages.HomePage;
import app.pages.LikedContentPage;
import app.pages.Page;
import app.player.Navigator;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Comparator;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The type User.
 */
public final class User extends UserAbstract {
    private static final Integer TREI = 3;
    private static final Integer CINCI = 5;
    private static final Double ONEMIL = 1_000_000.0;
    @Getter
    private final Player player;
    @Getter
    private final Navigator navigator;
    @Getter
    private final SearchBar searchBar;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private boolean status;
    private boolean lastSearched;
    @Getter
    @Setter
    private Page currentPage;
    @Getter
    @Setter
    private HomePage homePage;
    @Getter
    @Setter
    private LikedContentPage likedContentPage;

    @Getter
    private ArrayList<Merchandise> merch;

    @Getter
    private LinkedHashMap<String, Integer> topArtists;
    @Getter
    private LinkedHashMap<String, Integer> topGenres;
    @Getter
    private LinkedHashMap<Song, Integer> topSongs;
    @Getter
    private LinkedHashMap<String, Integer> topAlbums;
    @Getter
    private LinkedHashMap<String, Integer> topEpisodes;
    @Getter
    @Setter
    private boolean premium;
    @Getter
    private HashMap<Song, Integer> songsDuringPremium;
    @Getter
    private HashMap<Artist, Integer> artistSongCount;
    @Getter
    private ArrayList<Notifications> notifications;
    @Getter
    private ArrayList<Artist> followedartists;
    @Getter
    private ArrayList<Host> followedhosts;
    @Getter
    private Song recommendedSong;
    @Getter
    private Playlist recommendedPlaylist;
    @Getter
    private HashMap<Song, Integer> songspreAd;
    @Getter
    @Setter
    private int adprice;
    @Getter
    @Setter
    private boolean refreshprice;
    @Getter
    @Setter
    private Integer remainingtimeadd;

    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city) {
        super(username, age, city);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player(this);
        searchBar = new SearchBar(username);
        lastSearched = false;
        status = true;

        homePage = new HomePage(this);
        currentPage = homePage;
        likedContentPage = new LikedContentPage(this);
        topArtists = new LinkedHashMap<>();
        topGenres = new LinkedHashMap<>();
        topSongs = new LinkedHashMap<>();
        topAlbums = new LinkedHashMap<>();
        topEpisodes = new LinkedHashMap<>();
        merch = new ArrayList<>();
        premium = false;
        songsDuringPremium = new HashMap<>();
        artistSongCount = new HashMap<>();
        notifications = new ArrayList<>();
        followedartists = new ArrayList<>();
        followedhosts = new ArrayList<>();
        navigator = new Navigator();
        this.recommendedSong = null;
        this.recommendedPlaylist = null;
        this.songspreAd = new HashMap<>();
        adprice = -1;
        refreshprice = false;
        remainingtimeadd = 0;

    }

    /**
     * Checks if a song with the specified artist and name is present in the top songs.
     *
     * @param artist The artist of the song.
     * @param name   The name of the song.
     * @return The song if found in the top songs, or null otherwise.
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
     * Gets the user type associated with this object.
     *
     * @return The user type as a string.
     */
    @Override
    public String userType() {
        return "user";
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();
        adprice = -1;

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();

        if (type.equals("artist") || type.equals("host")) {
            List<ContentCreator> contentCreatorsEntries =
                    searchBar.searchContentCreator(filters, type);

            for (ContentCreator contentCreator : contentCreatorsEntries) {
                results.add(contentCreator.getUsername());
            }
        } else {
            List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

            for (LibraryEntry libraryEntry : libraryEntries) {
                results.add(libraryEntry.getName());
            }
        }
        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if (searchBar.getLastSearchType().equals("artist")
                || searchBar.getLastSearchType().equals("host")) {
            ContentCreator selected = searchBar.selectContentCreator(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            currentPage = selected.getPage();
            return "Successfully selected %s's page.".formatted(selected.getUsername());
        } else {
            LibraryEntry selected = searchBar.select(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            return "Successfully selected %s.".formatted(selected.getName());
        }
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }
        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();
        if (!"podcast".equals(player.getType())) {
            Song song = (Song) player.getSource().getAudioFile();
            String artist = song.getArtist();
            String name = song.getName();
            Artist instanceArtist = Admin.getInstance().getArtist(artist);


            if (containsSongInTopSongs(artist, name) != null) {
                Song aux = containsSongInTopSongs(artist, name);
                int currentCount = topSongs.get(aux);
                topSongs.put(aux, currentCount + 1);
            } else {
                topSongs.put(song, 1);

            }
            if (instanceArtist.containsSongInTopSongs(artist, name) != null) {
                Song aux = instanceArtist.containsSongInTopSongs(artist, name);
                int currentCount = instanceArtist.getTopSongs().get(aux);
                instanceArtist.getTopSongs().put(aux, currentCount + 1);
            } else {
                instanceArtist.getTopSongs().put(song, 1);
            }

            if (premium) {
                addSongDuringPremium(song);
            } else {
                songspreAd.merge(song, 1, Integer::sum);
            }


            if (topArtists.containsKey(artist)) {
                int currentCount = topArtists.get(artist);
                topArtists.put(artist, currentCount + 1);
            } else {
                topArtists.put(artist, 1);
            }

            String genre = song.getGenre();
            if (topGenres.containsKey(genre)) {
                int currentCount = topGenres.get(genre);
                topGenres.put(genre, currentCount + 1);
            } else {
                topGenres.put(genre, 1);
            }

            String album = song.getAlbum();
            if (topAlbums.containsKey(album)) {
                int currentCount = topAlbums.get(album);
                topAlbums.put(album, currentCount + 1);
            } else {
                topAlbums.put(album, 1);
            }
            if (instanceArtist.getTopAlbums().containsKey(album)) {
                int currentCount = instanceArtist.getTopAlbums().get(album);
                instanceArtist.getTopAlbums().put(album, currentCount + 1);
            } else {
                instanceArtist.getTopAlbums().put(album, 1);
            }

            if (instanceArtist.getTopUsers().containsKey(this)) {
                int currentCount = instanceArtist.getTopUsers().get(this);
                instanceArtist.getTopUsers().put(this, currentCount + 1);
            } else {
                instanceArtist.getTopUsers().put(this, 1);
            }

        } else {
            Podcast podcast = (Podcast) player.getSource().getAudioCollection();
            Host instanceHost = Admin.getInstance().getHost(podcast.getOwner());
            String episodeName = player.getSource().getAudioFile().getName();
            Episode episode = (Episode) player.getSource().getAudioFile();
            if (instanceHost != null) {
                if (instanceHost.getTopUsers().containsKey(this)) {
                    int currentCount = instanceHost.getTopUsers().get(this);
                    instanceHost.getTopUsers().put(this, currentCount + 1);
                } else {
                    instanceHost.getTopUsers().put(this, 1);
                }
                if (instanceHost.getTopEpisodes().containsKey(episode)) {
                    int currentCount = instanceHost.getTopEpisodes().get(episode);
                    instanceHost.getTopEpisodes().put(episode, currentCount + 1);
                } else {
                    instanceHost.getTopEpisodes().put(episode, 1);
                }
            }

            if (topEpisodes.containsKey(episodeName)) {
                int currentCount = topEpisodes.get(episodeName);
                topEpisodes.put(episodeName, currentCount + 1);
            } else {
                topEpisodes.put(episodeName, 1);
            }
        }

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT -> {
                repeatStatus = "no repeat";
            }
            case REPEAT_ONCE -> {
                repeatStatus = "repeat once";
            }
            case REPEAT_ALL -> {
                repeatStatus = "repeat all";
            }
            case REPEAT_INFINITE -> {
                repeatStatus = "repeat infinite";
            }
            case REPEAT_CURRENT_SONG -> {
                repeatStatus = "repeat current song";
            }
            default -> {
                repeatStatus = "";
            }
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }
        Episode aux = (Episode) player.getSource().getAudioFile();
        player.skipNext();
        if (aux != player.getSource().getAudioFile()) {
            String episodeName = player.getSource().getAudioFile().getName();
            if (topEpisodes.containsKey(episodeName)) {
                int currentCount = topEpisodes.get(episodeName);
                topEpisodes.put(episodeName, currentCount + 1);
            } else {
                topEpisodes.put(episodeName, 1);
            }
            Podcast podcast = (Podcast) player.getSource().getAudioCollection();
            Host instanceHost = Admin.getInstance().getHost(podcast.getOwner());
            Episode episode = (Episode) player.getSource().getAudioFile();

            if (instanceHost.getTopUsers().containsKey(this)) {
                int currentCount = instanceHost.getTopUsers().get(this);
                instanceHost.getTopUsers().put(this, currentCount + 1);
            } else {
                instanceHost.getTopUsers().put(this, 1);
            }
            if (instanceHost.getTopEpisodes().containsKey(episode)) {
                int currentCount = instanceHost.getTopEpisodes().get(episode);
                instanceHost.getTopEpisodes().put(episode, currentCount + 1);
            } else {
                instanceHost.getTopEpisodes().put(episode, 1);
            }

        }

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }
        if (!"podcast".equals(player.getType())) {
            Song song = (Song) player.getSource().getAudioFile();
            String artist = song.getArtist();
            String name = song.getName();
            Artist instanceArtist = Admin.getInstance().getArtist(artist);


            if (containsSongInTopSongs(artist, name) != null) {
                Song aux = containsSongInTopSongs(artist, name);
                int currentCount = topSongs.get(aux);
                topSongs.put(aux, currentCount + 1);
            } else {
                topSongs.put(song, 1);
            }
            if (instanceArtist.containsSongInTopSongs(artist, name) != null) {
                Song aux = instanceArtist.containsSongInTopSongs(artist, name);
                int currentCount = instanceArtist.getTopSongs().get(aux);
                instanceArtist.getTopSongs().put(aux, currentCount + 1);
            } else {
                instanceArtist.getTopSongs().put(song, 1);
            }

            if (premium) {
                addSongDuringPremium(song);
            } else {
                songspreAd.merge(song, 1, Integer::sum);
            }


            if (topArtists.containsKey(artist)) {
                int currentCount = topArtists.get(artist);
                topArtists.put(artist, currentCount + 1);
            } else {
                topArtists.put(artist, 1);
            }

            String genre = song.getGenre();
            if (topGenres.containsKey(genre)) {
                int currentCount = topGenres.get(genre);
                topGenres.put(genre, currentCount + 1);
            } else {
                topGenres.put(genre, 1);
            }

            String album = song.getAlbum();
            if (topAlbums.containsKey(album)) {
                int currentCount = topAlbums.get(album);
                topAlbums.put(album, currentCount + 1);
            } else {
                topAlbums.put(album, 1);
            }
            if (instanceArtist.getTopAlbums().containsKey(album)) {
                int currentCount = instanceArtist.getTopAlbums().get(album);
                instanceArtist.getTopAlbums().put(album, currentCount + 1);
            } else {
                instanceArtist.getTopAlbums().put(album, 1);
            }

            if (instanceArtist.getTopUsers().containsKey(this)) {
                int currentCount = instanceArtist.getTopUsers().get(this);
                instanceArtist.getTopUsers().put(this, currentCount + 1);
            } else {
                instanceArtist.getTopUsers().put(this, 1);
            }

        } else {
            Podcast podcast = (Podcast) player.getSource().getAudioCollection();
            Host instanceHost = Admin.getInstance().getHost(podcast.getOwner());
            String episodeName = player.getSource().getAudioFile().getName();
            Episode episode = (Episode) player.getSource().getAudioFile();

            if (instanceHost.getTopUsers().containsKey(this)) {
                int currentCount = instanceHost.getTopUsers().get(this);
                instanceHost.getTopUsers().put(this, currentCount + 1);
            } else {
                instanceHost.getTopUsers().put(this, 1);
            }
            if (instanceHost.getTopEpisodes().containsKey(episode)) {
                int currentCount = instanceHost.getTopEpisodes().get(episode);
                instanceHost.getTopEpisodes().put(episode, currentCount + 1);
            } else {
                instanceHost.getTopEpisodes().put(episode, 1);
            }

            if (topEpisodes.containsKey(episodeName)) {
                int currentCount = topEpisodes.get(episodeName);
                topEpisodes.put(episodeName, currentCount + 1);
            } else {
                topEpisodes.put(episodeName, 1);
            }
        }
        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();
        if (!"podcast".equals(player.getType())) {
            Song song = (Song) player.getSource().getAudioFile();
            String artist = song.getArtist();
            String name = song.getName();
            Artist instanceArtist = Admin.getInstance().getArtist(artist);


            if (containsSongInTopSongs(artist, name) != null) {
                Song aux = containsSongInTopSongs(artist, name);
                int currentCount = topSongs.get(aux);
                topSongs.put(aux, currentCount + 1);
            } else {
                topSongs.put(song, 1);
            }
            if (instanceArtist.containsSongInTopSongs(artist, name) != null) {
                Song aux = instanceArtist.containsSongInTopSongs(artist, name);
                int currentCount = instanceArtist.getTopSongs().get(aux);
                instanceArtist.getTopSongs().put(aux, currentCount + 1);
            } else {
                instanceArtist.getTopSongs().put(song, 1);
            }
            if (premium) {
                addSongDuringPremium(song);
            } else {
                songspreAd.merge(song, 1, Integer::sum);
            }


            if (topArtists.containsKey(artist)) {
                int currentCount = topArtists.get(artist);
                topArtists.put(artist, currentCount + 1);
            } else {
                topArtists.put(artist, 1);
            }

            String genre = song.getGenre();
            if (topGenres.containsKey(genre)) {
                int currentCount = topGenres.get(genre);
                topGenres.put(genre, currentCount + 1);
            } else {
                topGenres.put(genre, 1);
            }

            String album = song.getAlbum();
            if (topAlbums.containsKey(album)) {
                int currentCount = topAlbums.get(album);
                topAlbums.put(album, currentCount + 1);
            } else {
                topAlbums.put(album, 1);
            }
            if (instanceArtist.getTopAlbums().containsKey(album)) {
                int currentCount = instanceArtist.getTopAlbums().get(album);
                instanceArtist.getTopAlbums().put(album, currentCount + 1);
            } else {
                instanceArtist.getTopAlbums().put(album, 1);
            }

            if (instanceArtist.getTopUsers().containsKey(this)) {
                int currentCount = instanceArtist.getTopUsers().get(this);
                instanceArtist.getTopUsers().put(this, currentCount + 1);
            } else {
                instanceArtist.getTopUsers().put(this, 1);
            }

        } else {
            Podcast podcast = (Podcast) player.getSource().getAudioCollection();
            Host instanceHost = Admin.getInstance().getHost(podcast.getOwner());
            String episodeName = player.getSource().getAudioFile().getName();
            Episode episode = (Episode) player.getSource().getAudioFile();

            if (instanceHost.getTopUsers().containsKey(this)) {
                int currentCount = instanceHost.getTopUsers().get(this);
                instanceHost.getTopUsers().put(this, currentCount + 1);
            } else {
                instanceHost.getTopUsers().put(this, 1);
            }
            if (instanceHost.getTopEpisodes().containsKey(episode)) {
                int currentCount = instanceHost.getTopEpisodes().get(episode);
                instanceHost.getTopEpisodes().put(episode, currentCount + 1);
            } else {
                instanceHost.getTopEpisodes().put(episode, 1);
            }

            if (topEpisodes.containsKey(episodeName)) {
                int currentCount = topEpisodes.get(episodeName);
                topEpisodes.put(episodeName, currentCount + 1);
            } else {
                topEpisodes.put(episodeName, 1);
            }
        }

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, getUsername(), timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(getUsername())) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();
        Admin.getInstance().getUser(playlist.getOwner()).getNotifications().
                add(new Notifications("New user followed", "New user notification"));
        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Switch status.
     */
    public void switchStatus() {
        status = !status;
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        if (!status) {
            return;
        }

        player.simulatePlayer(time);
    }

    /**
     * Accepts a WrappedVisitor and invokes the visit method, returning the result.
     *
     * @param v The WrappedVisitor to accept.
     * @return An {@link ObjectNode} containing the result of the visit.
     */
    @Override
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
     * Adds a song to the songs played during the premium period and updates associated counts.
     *
     * @param song The song to be added during the premium period.
     */
    public void addSongDuringPremium(final Song song) {
        String artistName = song.getArtist();

        Artist artist = Admin.getInstance().getArtist(song.getArtist());

        songsDuringPremium.merge(song, 1, Integer::sum);
        artistSongCount.merge(artist, 1, Integer::sum);
    }

    /**
     * Checks if a song is already in the premium collection based on album, name, and artist.
     *
     * @param album  The album of the song.
     * @param name   The name of the song.
     * @param artist The artist of the song.
     * @return True if the song is already in the premium collection, false otherwise.
     */
    private boolean containsSongInPremium(final String album,
                                          final String name, final String artist) {
        return songsDuringPremium.keySet().stream()
                .anyMatch(song -> song.getArtist().equals(artist)
                        && song.getName().equals(name)
                        && song.getAlbum().equals(album));
    }

    /**
     * Performs premium monetization calculations
     * and updates the revenue for artists and their songs.
     */
    public void premiumMonetization() {
        double totalRevenueIncrease = ONEMIL;

        for (Map.Entry<Artist, Integer> entry : artistSongCount.entrySet()) {
            Artist artist = entry.getKey();
            int songCount = entry.getValue();
            int totalSongsListened = artistSongCount.values().stream()
                    .mapToInt(Integer::intValue).sum();

            if (totalSongsListened > 0) {
                double proportion = (double) songCount / totalSongsListened;
                double artistRevenueIncrease = (double) proportion * totalRevenueIncrease;
                artist.setSongRevenue(artist.getSongRevenue() + artistRevenueIncrease);
                for (Map.Entry<Song, Integer> songEntry : songsDuringPremium.entrySet()) {
                    Song song = songEntry.getKey();
                    int songListenCount = songEntry.getValue();
                    if (song.getArtist().equals(artist.getUsername())) {
                        double songRevenueIncrease = artistRevenueIncrease
                                * ((double) songListenCount / songCount);

                        if (artist.containsSongmostprofitablesongs(song.getArtist(), song.getName(),
                                song.getAlbum()) != null) {
                            Song aux = artist.containsSongmostprofitablesongs(song.getArtist(),
                                    song.getName(), song.getAlbum());
                            double currentRevenue = artist.getMostProfitableSongs().get(aux);
                            artist.getMostProfitableSongs().put(aux,
                                    currentRevenue + songRevenueIncrease);
                        } else {
                            artist.getMostProfitableSongs().put(song, songRevenueIncrease);
                        }
                    }
                }
            }
        }
        artistSongCount.clear();
        songsDuringPremium.clear();
    }

    /**
     * Retrieves the page of the artist based on the currently playing audio file.
     *
     * @return The artist's page or null if not applicable.
     */
    public Page getartistpage() {
        if ("song".equals(player.getType()) || "playlist".equals(player.getType())
                || "album".equals(player.getType())) {
            if (player.getCurrentAudioFile() == null) {
                return null;
            }
            Song aux = (Song) player.getCurrentAudioFile();
            ContentCreator creator = Admin.getInstance().getArtist(aux.getArtist());
            return creator.getPage();

        }
        return null;
    }

    /**
     * Retrieves the page of the host based on the currently playing podcast.
     *
     * @return The host's page or null if not applicable.
     */
    public Page gethostpage() {
        if ("podcast".equals(player.getType())) {
            if (player.getCurrentAudioCollection() == null) {
                return null;
            }
            Podcast aux = (Podcast) player.getCurrentAudioCollection();
            ContentCreator creator = Admin.getInstance().getHost(aux.getOwner());
            return creator.getPage();
        }
        return null;
    }

    /**
     * Updates recommendations based on the given recommendation type.
     *
     * @param recommendationType The type of recommendation.
     */
    public void updateRecommendations(final String recommendationType) {
        switch (recommendationType) {
            case "random_song":
                updateRandomSongRecommendation();
                break;
            case "random_playlist":
                updateRandomPlaylistRecommendation();
                break;
            case "fans_playlist":
                updateFansPlaylistRecommendation();
                break;
            default:
                break;
        }
    }

    /**
     * Updates recommendations with random playlist recommendations.
     */
    private void updateRandomPlaylistRecommendation() {
        Set<String> genres = new HashSet<>();

        genres.addAll(likedSongs.stream().map(Song::getGenre).collect(Collectors.toList()));

        genres.addAll(playlists.stream()
                .flatMap(playlist -> playlist.getSongs().stream().map(Song::getGenre))
                .collect(Collectors.toList()));

        genres.addAll(followedPlaylists.stream()
                .flatMap(playlist -> playlist.getSongs().stream().map(Song::getGenre))
                .collect(Collectors.toList()));

        if (genres.size() >= TREI) {
            List<String> distinctGenres = new ArrayList<>(genres);
            distinctGenres.sort(Comparator.comparingInt(genre ->
                    likedSongs.stream().filter(song -> song.getGenre().equals(genre)).
                            mapToInt(Song::getLikes).sum()));

            List<Song> recommendedSongs = new ArrayList<>();

            recommendedSongs.addAll(likedSongs.stream()
                    .filter(song -> song.getGenre().equals(distinctGenres.get(0)))
                    .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                    .limit(CINCI)
                    .collect(Collectors.toList()));

            recommendedSongs.addAll(likedSongs.stream()
                    .filter(song -> song.getGenre().equals(distinctGenres.get(1)))
                    .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                    .limit(TREI)
                    .collect(Collectors.toList()));

            recommendedSongs.addAll(likedSongs.stream()
                    .filter(song -> song.getGenre().equals(distinctGenres.get(2)))
                    .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                    .limit(2)
                    .collect(Collectors.toList()));

            recommendedSongs = recommendedSongs.stream().distinct().collect(Collectors.toList());

            Playlist recommendationPlaylist = new Playlist(
                    String.format("%s's recommendations",
                            super.getUsername()), super.getUsername());
            recommendationPlaylist.setSongs(recommendedSongs);

            recommendedPlaylist = recommendationPlaylist;
        } else {
            List<Song> recommendedSongs = new ArrayList<>();

            for (String genre : genres) {
                recommendedSongs.addAll(likedSongs.stream()
                        .filter(song -> song.getGenre().equals(genre))
                        .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                        .limit(CINCI)
                        .collect(Collectors.toList()));
            }

            recommendedSongs = recommendedSongs.stream().distinct().collect(Collectors.toList());

            Playlist recommendationPlaylist = new Playlist(
                    String.format("%s's recommendations",
                            super.getUsername()),
                            super.getUsername());
            recommendationPlaylist.setSongs(recommendedSongs);

            recommendedPlaylist = recommendationPlaylist;
        }
    }

    /**
     * Updates recommendations with fan playlist recommendations.
     */
    private void updateFansPlaylistRecommendation() {
        Song currentSong = (Song) player.getCurrentAudioFile();

        if (currentSong != null) {
            Artist artist = Admin.getInstance().getArtist(currentSong.getArtist());

            if (artist != null) {
                List<User> topFans = artist.getTopFans(CINCI);

                if (!topFans.isEmpty()) {
                    List<Song> recommendedSongs = new ArrayList<>();

                    for (User fan : topFans) {
                        List<Song> fanLikedSongs = fan.getLikedSongs();
                        fanLikedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());

                        recommendedSongs.addAll(fanLikedSongs.stream()
                                .limit(CINCI)
                                .collect(Collectors.toList()));
                    }

                    recommendedSongs = recommendedSongs.stream().distinct()
                            .collect(Collectors.toList());

                    String playlistName = String
                            .format("%s Fan Club recommendations", artist.getUsername());

                    Playlist recommendationPlaylist =
                            new Playlist(playlistName, super.getUsername());
                    recommendationPlaylist.setSongs(recommendedSongs);
                    recommendedPlaylist = recommendationPlaylist;

                    return;
                }
            }
        }
    }

    /**
     * Updates recommendations with random song recommendations.
     */
    private void updateRandomSongRecommendation() {
        Song currentSong = (Song) player.getCurrentAudioFile();
        if (currentSong != null) {
            String currentGenre = currentSong.getGenre();
            List<Song> songsInSameGenre = Admin.getInstance().getSongsByGenre(currentGenre);
            if (!songsInSameGenre.isEmpty()) {
                Random random = new Random();
                random.setSeed(player.getSource().getAudioFile().getDuration()
                        - player.getSource().getRemainedDuration());

                int randomIndex = random.nextInt(songsInSameGenre.size());
                recommendedSong = songsInSameGenre.get(randomIndex);
            }
        }
    }

    /**
     * Updates the user with notifications containing the provided name and description.
     *
     * @param name        The name for the notification.
     * @param description The description for the notification.
     */
    public void update(final String name, final String description) {
        notifications.add(new Notifications(name, description));
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String loadRecommendations() {
        if (!status) {
            return "%s is offline.".formatted(getUsername());
        }

        if (recommendedSong == null && recommendedPlaylist == null) {
            return "No recommendations available.";
        }
        if (recommendedSong != null) {
            player.setSource(recommendedSong, "song");
            searchBar.clearSelection();
        } else {
            player.setSource(recommendedPlaylist, "playlist");
            searchBar.clearSelection();
        }
        if (!"podcast".equals(player.getType())) {
            Song song = (Song) player.getSource().getAudioFile();
            String artist = song.getArtist();
            String name = song.getName();
            Artist instanceArtist = Admin.getInstance().getArtist(artist);


            if (containsSongInTopSongs(artist, name) != null) {
                Song aux = containsSongInTopSongs(artist, name);
                int currentCount = topSongs.get(aux);
                topSongs.put(aux, currentCount + 1);
            } else {
                topSongs.put(song, 1);

            }
            if (instanceArtist.containsSongInTopSongs(artist, name) != null) {
                Song aux = instanceArtist.containsSongInTopSongs(artist, name);
                int currentCount = instanceArtist.getTopSongs().get(aux);
                instanceArtist.getTopSongs().put(aux, currentCount + 1);
            } else {
                instanceArtist.getTopSongs().put(song, 1);
            }

            if (premium) {
                addSongDuringPremium(song);
            } else {
                songspreAd.merge(song, 1, Integer::sum);
            }


            if (topArtists.containsKey(artist)) {
                int currentCount = topArtists.get(artist);
                topArtists.put(artist, currentCount + 1);
            } else {
                topArtists.put(artist, 1);
            }

            String genre = song.getGenre();
            if (topGenres.containsKey(genre)) {
                int currentCount = topGenres.get(genre);
                topGenres.put(genre, currentCount + 1);
            } else {
                topGenres.put(genre, 1);
            }

            String album = song.getAlbum();
            if (topAlbums.containsKey(album)) {
                int currentCount = topAlbums.get(album);
                topAlbums.put(album, currentCount + 1);
            } else {
                topAlbums.put(album, 1);
            }
            if (instanceArtist.getTopAlbums().containsKey(album)) {
                int currentCount = instanceArtist.getTopAlbums().get(album);
                instanceArtist.getTopAlbums().put(album, currentCount + 1);
            } else {
                instanceArtist.getTopAlbums().put(album, 1);
            }

            if (instanceArtist.getTopUsers().containsKey(this)) {
                int currentCount = instanceArtist.getTopUsers().get(this);
                instanceArtist.getTopUsers().put(this, currentCount + 1);
            } else {
                instanceArtist.getTopUsers().put(this, 1);
            }

        } else {
            Podcast podcast = (Podcast) player.getSource().getAudioCollection();
            Host instanceHost = Admin.getInstance().getHost(podcast.getOwner());
            String episodeName = player.getSource().getAudioFile().getName();
            Episode episode = (Episode) player.getSource().getAudioFile();
            if (instanceHost != null) {
                if (instanceHost.getTopUsers().containsKey(this)) {
                    int currentCount = instanceHost.getTopUsers().get(this);
                    instanceHost.getTopUsers().put(this, currentCount + 1);
                } else {
                    instanceHost.getTopUsers().put(this, 1);
                }
                if (instanceHost.getTopEpisodes().containsKey(episode)) {
                    int currentCount = instanceHost.getTopEpisodes().get(episode);
                    instanceHost.getTopEpisodes().put(episode, currentCount + 1);
                } else {
                    instanceHost.getTopEpisodes().put(episode, 1);
                }
            }

            if (topEpisodes.containsKey(episodeName)) {
                int currentCount = topEpisodes.get(episodeName);
                topEpisodes.put(episodeName, currentCount + 1);
            } else {
                topEpisodes.put(episodeName, 1);
            }
        }

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Initiates an ad break, distributing the revenue generated by ads among the played songs.
     *
     * @param price The total price to be distributed among the played songs during the ad break.
     */
    public void adBreak(final Integer price) {
        double totalplays = songspreAd.values().stream().mapToDouble(Integer::doubleValue).sum();

        if (totalplays != 0) {
            double valueToAdd = (double) price / totalplays;

            for (Map.Entry<Song, Integer> entry : songspreAd.entrySet()) {
                Song song = entry.getKey();
                int currentplays = entry.getValue();

                double newValue = valueToAdd * currentplays;
                double aux = Admin.getInstance().getArtist(song.getArtist()).getSongRevenue();
                Artist artist = Admin.getInstance().getArtist(song.getArtist());
                Admin.getInstance().getArtist(song.getArtist()).setSongRevenue(aux + newValue);
                LinkedHashMap<Song, Double> mostProfitableSongs =
                        Admin.getInstance().getArtist(song.getArtist()).getMostProfitableSongs();

                if (artist.containsSongmostprofitablesongs(song.getArtist(),
                        song.getName(), song.getAlbum()) != null) {
                    Song auxx = artist.containsSongmostprofitablesongs(song.getArtist(),
                            song.getName(), song.getAlbum());
                    double currentRevenue = artist.getMostProfitableSongs().get(auxx);
                    artist.getMostProfitableSongs().put(auxx, currentRevenue + newValue);
                } else {
                    artist.getMostProfitableSongs().put(song, newValue);
                }

            }
        }
        songspreAd.clear();
    }

    @Override
    public String getKeyRepresentation() {
        return getUsername();
    }

}
