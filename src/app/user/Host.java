package app.user;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.pages.HostPage;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * The type Host.
 */
public final class Host extends ContentCreator {
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;
    @Getter
    private LinkedHashMap<Episode, Integer> topEpisodes;
    @Getter
    private LinkedHashMap<User, Integer> topUsers;
    @Getter
    private ArrayList<User> followingusers;
    @Getter
    private ArrayList<Notifications> notifications;

    /**
     * Instantiates a new Host.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();

        super.setPage(new HostPage(this));
        topUsers = new LinkedHashMap<>();
        topEpisodes = new LinkedHashMap<>();
        followingusers = new ArrayList<>();
        notifications = new ArrayList<>();
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    /**
     * Sets podcasts.
     *
     * @param podcasts the podcasts
     */
    public void setPodcasts(final ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    /**
     * Gets announcements.
     *
     * @return the announcements
     */
    public ArrayList<Announcement> getAnnouncements() {
        return announcements;
    }

    /**
     * Sets announcements.
     *
     * @param announcements the announcements
     */
    public void setAnnouncements(final ArrayList<Announcement> announcements) {
        this.announcements = announcements;
    }

    /**
     * Gets podcast.
     *
     * @param podcastName the podcast name
     * @return the podcast
     */
    public Podcast getPodcast(final String podcastName) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(podcastName)) {
                return podcast;
            }
        }

        return null;
    }

    /**
     * Gets announcement.
     *
     * @param announcementName the announcement name
     * @return the announcement
     */
    public Announcement getAnnouncement(final String announcementName) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(announcementName)) {
                return announcement;
            }
        }

        return null;
    }

    /**
     * Gets the user type, indicating that this is a host.
     *
     * @return The string "host".
     */
    @Override
    public String userType() {
        return "host";
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
     * Updates information (name and description) for all users following this host.
     *
     * @param name        The new name.
     * @param description The new description.
     */
    public void updateusers(final String name, final String description) {
        for (User user : followingusers) {
            user.update(name, description);
        }
    }
    @Override
    public String getKeyRepresentation() {
        return getUsername();
    }
}
