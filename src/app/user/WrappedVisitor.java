package app.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class WrappedVisitor implements Visitor {
    private static WrappedVisitor instance;

    private WrappedVisitor() {
    }

    /**
     * Get the instance of the Wrapped (Singleton pattern).
     *
     * @return The SortingClass instance.
     */
    public static WrappedVisitor getInstance() {
        if (instance == null) {
            instance = new WrappedVisitor();
        }
        return instance;
    }
    /**
     * Visits a generic user and generates formatted information based on the user's
     * page configuration.
     *
     * @param user the user to visit
     * @return formatted information about the user
     */
    public ObjectNode visit(final User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        if (user.getTopArtists().isEmpty() && user.getTopGenres().isEmpty()
                && user.getTopSongs().isEmpty() && user.getTopAlbums().isEmpty()
                && user.getTopEpisodes().isEmpty()) {
            result.put("message", "No data to show for user " + user.getUsername() + ".");
            return result;
        }

        result.putObject("topArtists")
                .setAll(SortingClass.getInstance().createJsonNodeFromStringMap(SortingClass
                        .getInstance().sortByValueAndThenKeyString(user.getTopArtists())));

        result.putObject("topGenres")
                .setAll(SortingClass.getInstance().createJsonNodeFromStringMap(SortingClass
                        .getInstance().sortByValueAndThenKeyString(user.getTopGenres())));

        result.putObject("topSongs")
                .setAll(SortingClass.getInstance().createJsonNodeFromSongMap(SortingClass
                        .getInstance().sortByValueAndThenKeySong(user.getTopSongs())));

        result.putObject("topAlbums")
                .setAll(SortingClass.getInstance().createJsonNodeFromStringMap(SortingClass
                        .getInstance().sortByValueAndThenKeyString(user.getTopAlbums())));

        result.putObject("topEpisodes")
                .setAll(SortingClass.getInstance().createJsonNodeFromStringMap(SortingClass
                        .getInstance().sortByValueAndThenKeyString(user.getTopEpisodes())));

        return result;
    }

    /**
     * Visits a host user and generates formatted information about the host's
     * podcasts and announcements.
     *
     * @param host the host to visit
     * @return formatted information about the host
     */
    public ObjectNode visit(final Host host) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        if (host.getTopEpisodes().isEmpty() && host.getTopUsers().isEmpty()) {
            result.put("message", "No data to show for host " + host.getUsername() + ".");
            return result;
        }

        ObjectNode topFansNode = SortingClass.getInstance().createJsonNodeFromUserMap(SortingClass
                .getInstance().sortByValueAndThenKeyUser(host.getTopUsers()));

        result.putObject("topEpisodes")
                .setAll(SortingClass.getInstance().createJsonNodeFromEpisodeMap(SortingClass
                        .getInstance().sortByValueAndThenKeyEpisode(host.getTopEpisodes())));
        ObjectNode aux = objectMapper.createObjectNode();
        ArrayNode listenersArrayNode = aux.putArray("topFans");
        topFansNode.fieldNames().forEachRemaining(listenersArrayNode::add);
        result.put("listeners", host.getTopUsers().size());
        return result;
    }

    /**
     * Visits an artist user and generates formatted information about the artist's
     * albums, merch, and events.
     *
     * @param artist the artist to visit
     * @return formatted information about the artist
     */
    public ObjectNode visit(final Artist artist) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        if (artist.getTopAlbums().isEmpty() && artist.getTopSongs().isEmpty()
                && artist.getTopUsers().isEmpty()) {
            result.put("message", "No data to show for artist " + artist.getUsername() + ".");
            return result;
        }

        ObjectNode topFansNode = SortingClass.getInstance().createJsonNodeFromUserMap(SortingClass
                .getInstance().sortByValueAndThenKeyUser(artist.getTopUsers()));

        result.putObject("topAlbums")
                .setAll(SortingClass.getInstance().createJsonNodeFromStringMap(SortingClass
                        .getInstance().sortByValueAndThenKeyString(artist.getTopAlbums())));

        result.putObject("topSongs")
                .setAll(SortingClass.getInstance().createJsonNodeFromSongMap(SortingClass.
                        getInstance().sortByValueAndThenKeySong(artist.getTopSongs())));
        ArrayNode listenersArrayNode = result.putArray("topFans");
        topFansNode.fieldNames().forEachRemaining(listenersArrayNode::add);
        result.put("listeners", artist.getTopUsers().size());

        return result;
    }





}

