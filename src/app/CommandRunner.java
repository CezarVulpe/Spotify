package app;

import app.audio.Collections.AlbumOutput;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.PodcastOutput;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.searchBar.Filters;

import app.user.GetNotificationsVisitor;
import app.user.Merchandise;
import app.user.WrappedVisitor;
import app.user.Host;
import app.user.Artist;
import app.user.User;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    private static final int RECOMMENDATION_THRESHOLD = 30;
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Admin admin;

    private CommandRunner() {
    }

    /**
     * Update admin.
     */
    public static void updateAdmin() {
        admin = Admin.getInstance();
    }

    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();
        ArrayList<String> results = new ArrayList<>();
        String message = "%s is offline.".formatted(user.getUsername());

        if (user.isStatus()) {
            results = user.search(filters, type);
            message = "Search returned " + results.size() + " results";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.like();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Switch connection status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        String message = admin.switchStatus(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add user object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String message = admin.addNewUser(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Delete user object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        String message = admin.deleteUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add album object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        String message = admin.addAlbum(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Remove album object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        String message = admin.removeAlbum(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show albums object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        Artist artist = admin.getArtist(commandInput.getUsername());
        ArrayList<AlbumOutput> albums = artist.showAlbums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Add event object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        String message = admin.addEvent(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Remove event object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        String message = admin.removeEvent(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        String message = admin.addPodcast(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Remove podcast object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        String message = admin.removePodcast(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show podcasts object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        Host host = admin.getHost(commandInput.getUsername());
        List<PodcastOutput> podcasts = host.getPodcasts().stream().map(PodcastOutput::new).toList();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(podcasts));

        return objectNode;
    }

    /**
     * Add merch object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        String message = admin.addMerch(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add announcement object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        String message = admin.addAnnouncement(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Remove announcement object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        String message = admin.removeAnnouncement(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets online users.
     *
     * @param commandInput the command input
     * @return the online users
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> onlineUsers = admin.getOnlineUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(onlineUsers));

        return objectNode;
    }

    /**
     * Gets all users.
     *
     * @param commandInput the command input
     * @return the all users
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<String> users = admin.getAllUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(users));

        return objectNode;
    }

    /**
     * Change page object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        String message = admin.changePage(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Print current page object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        String message = admin.printCurrentPage(commandInput);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets top 5 album list.
     *
     * @param commandInput the command input
     * @return the top 5 album list
     */
    public static ObjectNode getTop5AlbumList(final CommandInput commandInput) {
        List<String> albums = admin.getTop5AlbumList();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Gets top 5 artist list.
     *
     * @param commandInput the command input
     * @return the top 5 artist list
     */
    public static ObjectNode getTop5ArtistList(final CommandInput commandInput) {
        List<String> artists = admin.getTop5ArtistList();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(artists));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Wraps the execution of a command, generating a response in the form of an {@link ObjectNode}.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode wrapped(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        WrappedVisitor v = WrappedVisitor.getInstance();
        ObjectNode node;
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user != null) {
            node = user.accept(v);
            if (node.has("message")) {
                objectNode.setAll(node);
            } else {
                objectNode.set("result", node);
            }
        }

        Artist artist = admin.getArtist(commandInput.getUsername());
        Host host = admin.getHost(commandInput.getUsername());

        if (artist != null) {
            node = artist.accept(v);
            if (node.has("message")) {
                objectNode.setAll(node);
            } else {
                objectNode.set("result", node);
            }
        } else if (host != null) {
            node = host.accept(v);
            if (node.has("message")) {
                objectNode.setAll(node);
            } else {
                objectNode.set("result", node);
            }
        }
        return objectNode;
    }

    /**
     * Allows a user to buy merchandise and updates relevant information.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode buyMerch(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (user.getCurrentPage().getType().equals(Enums.UserType.ARTIST)) {
            Artist artist = (Artist) user.getCurrentPage().getUserAbstract();
            String merchName = commandInput.getName();

            boolean merchExists = artist.getMerch().stream()
                    .anyMatch(merchItem -> merchItem.getName().equals(merchName));

            if (merchExists) {
                Merchandise merch = artist.getMerch().stream()
                        .filter(merchItem -> merchItem.getName().equals(merchName))
                        .findFirst()
                        .orElse(null);
                message = commandInput.getUsername() + " has added new merch successfully.";
                user.getMerch().add(merch);
                artist.setMerchRevenue(artist.getMerchRevenue() + merch.getPrice());

            } else {
                message = "The merch " + merchName + " doesn't exist.";
            }
        } else {
            message = "Cannot buy merch from this page.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Retrieves and displays the merchandise owned by a user.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode seeMerch(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        List<String> merchNames = user.getMerch().stream()
                .map(Merchandise::getName)
                .collect(Collectors.toList());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        ArrayNode resultArray = objectMapper.createArrayNode();
        merchNames.forEach(resultArray::add);
        objectNode.set("result", resultArray);

        return objectNode;
    }

    /**
     * Allows a user to buy a premium subscription.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode buyPremium(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();

        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (user.isPremium()) {
            message = commandInput.getUsername() + " is already a premium user.";
        } else {
            user.setPremium(true);
            message = commandInput.getUsername() + " bought the subscription successfully.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Cancels a user's premium subscription.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode cancelPremium(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (!user.isPremium()) {
            message = commandInput.getUsername() + " is not a premium user.";
        } else {
            user.setPremium(false);
            user.premiumMonetization();
            message = commandInput.getUsername() + " cancelled the subscription successfully.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Allows a user to subscribe or unsubscribe to an artist or host.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode subscribe(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();

        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            if ("artist".equals(user.getSearchBar().getLastSearchType())) {
                Artist artist = (Artist) user.getSearchBar().getLastContentCreatorSelected();
                if (user.getFollowedartists().contains(artist)) {
                    user.getFollowedartists().remove(artist);
                    message = user.getUsername() + " unsubscribed from "
                            + artist.getUsername() + " successfully.";
                    artist.getFollowingusers().remove(user);
                } else {
                    user.getFollowedartists().add(artist);
                    artist.getFollowingusers().add(user);
                    message = user.getUsername() + " subscribed to "
                            + artist.getUsername() + " successfully.";
                }
            } else if ("host".equals(user.getSearchBar().getLastSearchType())) {
                Host host = (Host) user.getSearchBar().getLastContentCreatorSelected();
                if (user.getFollowedhosts().contains(host)) {
                    user.getFollowedhosts().remove(host);
                    message = user.getUsername() + " unsubscribed from "
                            + host.getUsername() + " successfully.";
                    host.getFollowingusers().remove(user);
                } else {
                    user.getFollowedhosts().add(host);
                    host.getFollowingusers().add(user);
                    message = user.getUsername() + " subscribed to "
                            + host.getUsername() + " successfully.";
                }
            } else {
                user.setPremium(false);
                user.premiumMonetization();
                message = "To subscribe you need to be on the page of an artist or host.";
            }
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Retrieves notifications for a user.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode getNotifications(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        GetNotificationsVisitor v = new GetNotificationsVisitor();
        ObjectNode node;
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user != null) {
            node = user.accept(v);
            objectNode.setAll(node);
        }

        Artist artist = admin.getArtist(commandInput.getUsername());
        Host host = admin.getHost(commandInput.getUsername());

        if (artist != null) {
            node = artist.accept(v);
            objectNode.setAll(node);
        } else if (host != null) {
            node = host.accept(v);
            objectNode.setAll(node);
        }
        return objectNode;
    }

    /**
     * Navigates the user to the previous page in their interaction history.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode previousPage(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message;
        if (!user.getNavigator().undo()) {
            message = "There are no pages left to go back.";
        } else {
            message = "The user " + user.getUsername()
                    + " has navigated successfully to the previous page.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Navigates the user to the next page in their interaction history.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode nextPage(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message;
        if (!user.getNavigator().redo()) {
            message = "There are no pages left to go forward.";
        } else {
            message = "The user " + user.getUsername()
                    + " has navigated successfully to the next page.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Updates the recommendations for a user.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode updateRecommendations(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            Artist artist = admin.getArtist(commandInput.getUsername());
            Host host = admin.getHost(commandInput.getUsername());
            if (artist == null && host == null) {
                message = "The username " + commandInput.getUsername() + " doesn't exist.";
            } else {
                message = commandInput.getUsername() + " is not a normal user.";
            }
        } else if (user.getPlayer().getSource() != null
                && commandInput.getRecommendationType().equals("random_song")
                && user.getPlayer().getSource().getAudioFile().getDuration()
                - user.getPlayer().getSource().getRemainedDuration() < RECOMMENDATION_THRESHOLD) {
            message = "No new recommendations were found";
        } else {
            Song aux = user.getRecommendedSong();
            user.updateRecommendations(commandInput.getRecommendationType());
            message = "The recommendations for user "
                    + user.getUsername() + " have been updated successfully.";
            if (user.getRecommendedSong() == aux) {
                if (user.getRecommendedPlaylist() != null) {
                    if (user.getRecommendedPlaylist().getSongs().isEmpty()) {
                        message = "No new recommendations were found";
                    }
                } else {
                    message = "No new recommendations were found";
                }
            }
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Loads saved recommendations for a user.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode loadRecommendations(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());
        String message = user.loadRecommendations();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Inserts an advertisement break during user playback.
     *
     * @param commandInput The input containing the command details.
     * @return An {@link ObjectNode} containing the result of the command execution.
     */
    public static ObjectNode adBreak(final CommandInput commandInput) {
        User user = admin.getUser(commandInput.getUsername());

        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else if (user.getPlayer().getCurrentAudioFile() == null) {
            message = commandInput.getUsername() + " is not playing any music.";
        } else {
            user.setAdprice(commandInput.getPrice());
            message = "Ad inserted successfully.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Ends the program, finalizing user interactions and providing a summary.
     *
     * @return An {@link ObjectNode} containing the result of ending the program.
     */
    public static ObjectNode endProgram() {
        ObjectNode node = admin.endProgram();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "endProgram");
        objectNode.set("result", node);

        return objectNode;
    }
}
