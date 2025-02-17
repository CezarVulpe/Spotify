package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import app.user.UserAbstract;
import app.utils.Enums;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

/**
 * The type Home page.
 */
public final class HomePage implements Page {
    private List<Song> likedSongs;
    private List<Playlist> followedPlaylists;
    private final int limit = 5;
    @Getter
    private User user;
    @Getter
    private Enums.UserType userType;

    /**
     * Instantiates a new Home page.
     *
     * @param user the user
     */
    public HomePage(final User user) {
        likedSongs = user.getLikedSongs();
        followedPlaylists = user.getFollowedPlaylists();
        this.user = user;
        userType = Enums.UserType.USER;
    }

    @Override
    public Enums.UserType getType() {
        return userType;
    }
    @Override
    public UserAbstract getUserAbstract() {
        return user;
    }

    @Override
    public String printCurrentPage() {
        StringBuilder result = new StringBuilder();

        // Liked songs
        result.append("Liked songs:\n\t")
                .append(likedSongs.stream()
                        .sorted(Comparator.comparing(Song::getLikes).reversed())
                        .limit(limit)
                        .map(Song::getName)
                        .toList())
                .append("\n\n");

        // Followed playlists
        result.append("Followed playlists:\n\t")
                .append(followedPlaylists.stream()
                        .sorted((o1, o2) -> o2.getSongs().stream()
                                .map(Song::getLikes)
                                .reduce(Integer::sum)
                                .orElse(0)
                                - o1.getSongs().stream()
                                .map(Song::getLikes)
                                .reduce(Integer::sum)
                                .orElse(0))
                        .limit(limit)
                        .map(Playlist::getName)
                        .toList())
                .append("\n\n");

        if (user.getRecommendedSong() != null) {
            result.append("Song recommendations:\n\t[")
                    .append(user.getRecommendedSong().getName())
                    .append("]\n\n");
        } else {
            result.append("Song recommendations:\n\t[]\n\n");
        }

        if (user.getRecommendedPlaylist() != null) {
            result.append("Playlists recommendations:\n\t[")
                    .append(user.getRecommendedPlaylist().getName())
                    .append("]");
        } else {
            result.append("Playlists recommendations:\n\t[]");
        }

        return result.toString();
    }

}
