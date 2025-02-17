package app.pages;

import app.audio.Collections.Podcast;
import app.user.Announcement;
import app.user.Host;
import app.user.UserAbstract;
import app.utils.Enums;
import lombok.Getter;

import java.util.List;

/**
 * The type Host page.
 */
public final class HostPage implements Page {
    private List<Podcast> podcasts;
    private List<Announcement> announcements;
    @Getter
    private Host host;
    @Getter
    private Enums.UserType userType;

    /**
     * Instantiates a new Host page.
     *
     * @param host the host
     */
    public HostPage(final Host host) {
        podcasts = host.getPodcasts();
        announcements = host.getAnnouncements();
        this.host = host;
        userType = Enums.UserType.HOST;
    }

    @Override
    public Enums.UserType getType() {
        return userType;
    }
    @Override
    public UserAbstract getUserAbstract() {
        return host;
    }

    @Override
    public String printCurrentPage() {
        return "Podcasts:\n\t%s\n\nAnnouncements:\n\t%s"
               .formatted(podcasts.stream().map(podcast -> "%s:\n\t%s\n"
                          .formatted(podcast.getName(),
                                     podcast.getEpisodes().stream().map(episode -> "%s - %s"
                          .formatted(episode.getName(), episode.getDescription())).toList()))
                          .toList(),
                          announcements.stream().map(announcement -> "%s:\n\t%s\n"
                          .formatted(announcement.getName(), announcement.getDescription()))
                          .toList());
    }
}
