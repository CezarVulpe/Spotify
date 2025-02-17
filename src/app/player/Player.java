package app.player;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Player.
 */
public final class Player {
    private static final int ADD_SIZE = 10;
    private final int skipTime = 90;
    private User user;
    private Enums.RepeatMode repeatMode;
    private boolean shuffle;
    private boolean paused;
    @Getter
    private PlayerSource source;
    @Getter
    private String type;
    private ArrayList<PodcastBookmark> bookmarks = new ArrayList<>();


    /**
     * Instantiates a new Player.
     */
    public Player() {
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
    }

    public Player(final User user) {
        this();
        this.user = user;
    }

    /**
     * Create source player source.
     *
     * @param type      the type
     * @param entry     the entry
     * @param bookmarks the bookmarks
     * @return the player source
     */
    public static PlayerSource createSource(final String type,
                                            final LibraryEntry entry,
                                            final List<PodcastBookmark> bookmarks) {
        if ("song".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
        } else if ("playlist".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        } else if ("podcast".equals(type)) {
            return createPodcastSource((AudioCollection) entry, bookmarks);
        } else if ("album".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.ALBUM, (AudioCollection) entry);
        }

        return null;
    }

    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection);
    }

    /**
     * Stop.
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        repeatMode = Enums.RepeatMode.NO_REPEAT;
        paused = true;
        source = null;
        shuffle = false;
    }

    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark =
                    new PodcastBookmark(source.getAudioCollection().getName(),
                            source.getIndex(),
                            source.getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }

    /**
     * Sets source.
     *
     * @param entry      the entry
     * @param sourceType the sourceType
     */
    public void setSource(final LibraryEntry entry, final String sourceType) {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        this.type = sourceType;
        this.source = createSource(sourceType, entry, bookmarks);
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;
    }

    /**
     * Pause.
     */
    public void pause() {
        paused = !paused;
    }

    /**
     * Shuffle.
     *
     * @param seed the seed
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        if (source.getType() == Enums.PlayerSourceType.PLAYLIST
                || source.getType() == Enums.PlayerSourceType.ALBUM) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }

    /**
     * Repeat enums . repeat mode.
     *
     * @return the enums . repeat mode
     */
    public Enums.RepeatMode repeat() {
        if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
            if (source.getType() == Enums.PlayerSourceType.LIBRARY) {
                repeatMode = Enums.RepeatMode.REPEAT_ONCE;
            } else {
                repeatMode = Enums.RepeatMode.REPEAT_ALL;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
                repeatMode = Enums.RepeatMode.REPEAT_INFINITE;
            } else {
                if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                    repeatMode = Enums.RepeatMode.REPEAT_CURRENT_SONG;
                } else {
                    repeatMode = Enums.RepeatMode.NO_REPEAT;
                }
            }
        }

        return repeatMode;
    }

    /**
     * Simulate player.
     *
     * @param time the time
     */
    public void simulatePlayer(final int time) {
        int elapsedTime = time;
        if (user.isRefreshprice()) {
            if (elapsedTime >= user.getRemainingtimeadd()) {
                elapsedTime -= user.getRemainingtimeadd();
                user.adBreak(user.getAdprice());
                user.setAdprice(-1);
                user.setRefreshprice(false);
                user.setRemainingtimeadd(0);
            } else {
                user.setRemainingtimeadd(user.getRemainingtimeadd() - elapsedTime);
                return;
            }
        }
        if (!paused) {
            while (elapsedTime >= source.getDuration()) {
                elapsedTime -= source.getDuration();
                if (user.getAdprice() >= 0) {
                    if (elapsedTime >= ADD_SIZE) {
                        if (!user.isPremium()) {
                            user.adBreak(user.getAdprice());
                        }
                        user.setAdprice(-1);
                        elapsedTime -= ADD_SIZE;
                    } else {
                        user.setRefreshprice(true);
                        user.setRemainingtimeadd(ADD_SIZE - elapsedTime);
                        continue;
                    }
                }
                next();
                if (paused) {
                    break;
                }
                if (!"podcast".equals(this.getType())) {
                    Song song = (Song) this.getSource().getAudioFile();
                    String artist = song.getArtist();
                    String name = song.getName();
                    Artist instanceArtist = Admin.getInstance().getArtist(artist);


                    if (user.containsSongInTopSongs(artist, name) != null) {
                        Song aux = user.containsSongInTopSongs(artist, name);
                        int currentCount = user.getTopSongs().get(aux);
                        user.getTopSongs().put(aux, currentCount + 1);
                    } else {
                        user.getTopSongs().put(song, 1);
                    }
                    if (instanceArtist.containsSongInTopSongs(artist, name) != null) {
                        Song aux = instanceArtist.containsSongInTopSongs(artist, name);
                        int currentCount = instanceArtist.getTopSongs().get(aux);
                        instanceArtist.getTopSongs().put(aux, currentCount + 1);
                    } else {
                        instanceArtist.getTopSongs().put(song, 1);
                    }
                    if (user.isPremium()) {
                        user.addSongDuringPremium(song);
                    } else {
                        user.getSongspreAd().merge(song, 1, Integer::sum);
                    }


                    if (user.getTopArtists().containsKey(artist)) {
                        int currentCount = user.getTopArtists().get(artist);
                        user.getTopArtists().put(artist, currentCount + 1);
                    } else {
                        user.getTopArtists().put(artist, 1);
                    }

                    String genre = song.getGenre();
                    if (user.getTopGenres().containsKey(genre)) {
                        int currentCount = user.getTopGenres().get(genre);
                        user.getTopGenres().put(genre, currentCount + 1);
                    } else {
                        user.getTopGenres().put(genre, 1);
                    }

                    String album = song.getAlbum();
                    if (user.getTopAlbums().containsKey(album)) {
                        int currentCount = user.getTopAlbums().get(album);
                        user.getTopAlbums().put(album, currentCount + 1);
                    } else {
                        user.getTopAlbums().put(album, 1);
                    }
                    if (instanceArtist.getTopAlbums().containsKey(album)) {
                        int currentCount = instanceArtist.getTopAlbums().get(album);
                        instanceArtist.getTopAlbums().put(album, currentCount + 1);
                    } else {
                        instanceArtist.getTopAlbums().put(album, 1);
                    }

                    if (instanceArtist.getTopUsers().containsKey(user)) {
                        int currentCount = instanceArtist.getTopUsers().get(user);
                        instanceArtist.getTopUsers().put(user, currentCount + 1);
                    } else {
                        instanceArtist.getTopUsers().put(user, 1);
                    }

                } else {
                    Podcast podcast = (Podcast) this.getSource().getAudioCollection();
                    Host instanceHost = Admin.getInstance().getHost(podcast.getOwner());
                    String episodeName = this.getSource().getAudioFile().getName();
                    Episode episode = (Episode) this.getSource().getAudioFile();

                    if (instanceHost.getTopUsers().containsKey(user)) {
                        int currentCount = instanceHost.getTopUsers().get(user);
                        instanceHost.getTopUsers().put(user, currentCount + 1);
                    } else {
                        instanceHost.getTopUsers().put(user, 1);
                    }
                    if (instanceHost.getTopEpisodes().containsKey(episode)) {
                        int currentCount = instanceHost.getTopEpisodes().get(episode);
                        instanceHost.getTopEpisodes().put(episode, currentCount + 1);
                    } else {
                        instanceHost.getTopEpisodes().put(episode, 1);
                    }

                    if (user.getTopEpisodes().containsKey(episodeName)) {
                        int currentCount = user.getTopEpisodes().get(episodeName);
                        user.getTopEpisodes().put(episodeName, currentCount + 1);
                    } else {
                        user.getTopEpisodes().put(episodeName, 1);
                    }
                }
            }
            if (!paused) {
                source.skip(-elapsedTime);
            }
        }
    }

    /**
     * Next.
     */
    public void next() {
        paused = source.setNextAudioFile(repeatMode, shuffle);
        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }

    /**
     * Prev.
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }

    private void skip(final int duration) {
        AudioFile aux;
        aux = source.getAudioFile();
        source.skip(duration);
        if (aux != source.getAudioFile()) {
            String episodeName = source.getAudioFile().getName();
            if (user.getTopEpisodes().containsKey(episodeName)) {
                int currentCount = user.getTopEpisodes().get(episodeName);
                user.getTopEpisodes().put(episodeName, currentCount + 1);
            } else {
                user.getTopEpisodes().put(episodeName, 1);
            }
        }
        paused = false;
    }

    /**
     * Skip next.
     */
    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(-skipTime);
        }
    }

    /**
     * Skip prev.
     */
    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(skipTime);
        }
    }

    /**
     * Gets current audio file.
     *
     * @return the current audio file
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }
        return source.getAudioFile();
    }

    /**
     * Gets current audio collection.
     *
     * @return the current audio collection
     */
    public AudioCollection getCurrentAudioCollection() {
        if (source == null) {
            return null;
        }
        return source.getAudioCollection();
    }

    /**
     * Gets paused.
     *
     * @return the paused
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * Gets shuffle.
     *
     * @return the shuffle
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * Gets stats.
     *
     * @return the stats
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;
        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }

        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }
}
