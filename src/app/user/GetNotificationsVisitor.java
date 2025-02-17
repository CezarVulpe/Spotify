package app.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.stream.Collectors;

public class GetNotificationsVisitor implements Visitor {

    /**
     * Visits and processes notifications for a User,
     * creating an ObjectNode containing notifications.
     *
     * @param user The User for whom notifications are processed.
     * @return An {@link ObjectNode} containing processed notifications for the User.
     */
    public ObjectNode visit(final User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        ArrayNode notificationsArray = objectMapper.createArrayNode();
        List<ObjectNode> notificationObjects = user.getNotifications().stream()
                .map(this::createNotificationObject)
                .collect(Collectors.toList());
        notificationsArray.addAll(notificationObjects);

        result.set("notifications", notificationsArray);
        user.getNotifications().clear();
        return result;
    }

    /**
     * Visits and processes notifications for a Host,
     * creating an ObjectNode containing notifications.
     *
     * @param host The Host for whom notifications are processed.
     * @return An {@link ObjectNode} containing processed notifications for the Host.
     */
    public ObjectNode visit(final Host host) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        ArrayNode notificationsArray = objectMapper.createArrayNode();
        List<ObjectNode> notificationObjects = host.getNotifications().stream()
                .map(this::createNotificationObject)
                .collect(Collectors.toList());
        notificationsArray.addAll(notificationObjects);

        result.set("notifications", notificationsArray);
        host.getNotifications().clear();
        return result;
    }

    /**
     * Visits and processes notifications for an Artist,
     * creating an ObjectNode containing notifications.
     *
     * @param artist The Artist for whom notifications are processed.
     * @return An {@link ObjectNode} containing processed notifications for the Artist.
     */
    public ObjectNode visit(final Artist artist) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        ArrayNode notificationsArray = objectMapper.createArrayNode();
        List<ObjectNode> notificationObjects = artist.getNotifications().stream()
                .map(this::createNotificationObject)
                .collect(Collectors.toList());
        notificationsArray.addAll(notificationObjects);

        result.set("notifications", notificationsArray);
        artist.getNotifications().clear();
        return result;
    }

    /**
     * Creates an ObjectNode containing information from a Notifications object.
     *
     * @param notification The Notifications object to be processed.
     * @return An {@link ObjectNode} containing information from the Notifications object.
     */
    private ObjectNode createNotificationObject(final Notifications notification) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode notificationObject = objectMapper.createObjectNode();

        notificationObject.put("name", notification.getName());
        notificationObject.put("description", notification.getDescription());


        return notificationObject;
    }
}
