package app.user;

import app.audio.Files.Episode;
import app.audio.Files.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;

public class SortingClass {

    private static final int LIMIT = 5;
    private static SortingClass instance;

    private SortingClass() {
    }

    /**
     * Get the instance of the SortingClass (Singleton pattern).
     *
     * @return The SortingClass instance.
     */
    public static SortingClass getInstance() {
        if (instance == null) {
            synchronized (SortingClass.class) {
                if (instance == null) {
                    instance = new SortingClass();
                }
            }
        }
        return instance;
    }

    /**
     * Sorts a map of String keys by their values in descending
     * order and then by keys in ascending order.
     * Limits the result to a specified number.
     *
     * @param map The input map to be sorted.
     * @return A LinkedHashMap sorted by value and then by key.
     */
    public LinkedHashMap<String, Integer> sortByValueAndThenKeyString(
            final LinkedHashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
        Collections.sort(entryList, (entry1, entry2) -> {
            int compare = entry2.getValue().compareTo(entry1.getValue());
            return compare != 0 ? compare : entry1.getKey().compareTo(entry2.getKey());
        });

        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        int limit = Math.min(entryList.size(), LIMIT);
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Integer> entry = entryList.get(i);
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Sorts a map of Song keys by their values in descending
     * order and then by key representation in ascending order.
     * Limits the result to a specified number.
     *
     * @param map The input map to be sorted.
     * @return A LinkedHashMap sorted by value and then by key representation.
     */
    public LinkedHashMap<Song, Integer> sortByValueAndThenKeySong(
            final LinkedHashMap<Song, Integer> map) {
        List<Map.Entry<Song, Integer>> entryList = new ArrayList<>(map.entrySet());
        Collections.sort(entryList, (entry1, entry2) -> {
            int compare = entry2.getValue().compareTo(entry1.getValue());
            return compare != 0 ? compare : entry1.getKey().
                    getKeyRepresentation().compareTo(entry2.getKey().getKeyRepresentation());
        });

        LinkedHashMap<Song, Integer> result = new LinkedHashMap<>();
        int limit = Math.min(entryList.size(), LIMIT);
        for (int i = 0; i < limit; i++) {
            Map.Entry<Song, Integer> entry = entryList.get(i);
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Sorts a map of Episode keys by their values in descending
     * order and then by key representation in ascending order.
     * Limits the result to a specified number.
     *
     * @param map The input map to be sorted.
     * @return A LinkedHashMap sorted by value and then by key representation.
     */
    public LinkedHashMap<Episode, Integer> sortByValueAndThenKeyEpisode(
            final LinkedHashMap<Episode, Integer> map) {
        List<Map.Entry<Episode, Integer>> entryList = new ArrayList<>(map.entrySet());
        Collections.sort(entryList, (entry1, entry2) -> {
            int compare = entry2.getValue().compareTo(entry1.getValue());
            return compare != 0 ? compare : entry1.getKey().
                    getKeyRepresentation().compareTo(entry2.getKey().getKeyRepresentation());
        });

        LinkedHashMap<Episode, Integer> result = new LinkedHashMap<>();
        int limit = Math.min(entryList.size(), LIMIT);
        for (int i = 0; i < limit; i++) {
            Map.Entry<Episode, Integer> entry = entryList.get(i);
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Sorts a map of User keys by their values
     * in descending order and then by key representation in ascending order.
     * Limits the result to a specified number.
     *
     * @param map The input map to be sorted.
     * @return A LinkedHashMap sorted by value and then by key representation.
     */
    public LinkedHashMap<User, Integer> sortByValueAndThenKeyUser(
            final LinkedHashMap<User, Integer> map) {
        List<Map.Entry<User, Integer>> entryList = new ArrayList<>(map.entrySet());
        Collections.sort(entryList, (entry1, entry2) -> {
            int compare = entry2.getValue().compareTo(entry1.getValue());
            return compare != 0 ? compare : entry1.getKey().
                    getKeyRepresentation().compareTo(entry2.getKey().getKeyRepresentation());
        });

        LinkedHashMap<User, Integer> result = new LinkedHashMap<>();
        int limit = Math.min(entryList.size(), LIMIT);
        for (int i = 0; i < limit; i++) {
            Map.Entry<User, Integer> entry = entryList.get(i);
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Creates a JSON node from a map of String keys. Limits the result to a specified number.
     *
     * @param map The input map to create a JSON node from.
     * @return The created JSON node.
     */
    public ObjectNode createJsonNodeFromStringMap(final LinkedHashMap<String, Integer> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        int limit = Math.min(map.size(), LIMIT);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (limit == 0) {
                break;
            }

            jsonNode.put(entry.getKey(), entry.getValue());
            limit--;
        }

        return jsonNode;
    }

    /**
     * Creates a JSON node from a map of Song keys. Limits the result to a specified number.
     *
     * @param map The input map to create a JSON node from.
     * @return The created JSON node.
     */
    public ObjectNode createJsonNodeFromSongMap(final LinkedHashMap<Song, Integer> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        int limit = Math.min(map.size(), LIMIT);

        for (Map.Entry<Song, Integer> entry : map.entrySet()) {
            if (limit == 0) {
                break;
            }

            Song key = entry.getKey();
            jsonNode.put(key.getName(), entry.getValue());
            limit--;
        }

        return jsonNode;
    }

    /**
     * Creates a JSON node from a map of Episode keys. Limits the result to a specified number.
     *
     * @param map The input map to create a JSON node from.
     * @return The created JSON node.
     */
    public ObjectNode createJsonNodeFromEpisodeMap(final LinkedHashMap<Episode, Integer> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        int limit = Math.min(map.size(), LIMIT);

        for (Map.Entry<Episode, Integer> entry : map.entrySet()) {
            if (limit == 0) {
                break;
            }

            Episode key = entry.getKey();
            jsonNode.put(key.getName(), entry.getValue());
            limit--;
        }

        return jsonNode;
    }

    /**
     * Creates a JSON node from a map of User keys. Limits the result to a specified number.
     *
     * @param map The input map to create a JSON node from.
     * @return The created JSON node.
     */
    public ObjectNode createJsonNodeFromUserMap(final LinkedHashMap<User, Integer> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        int limit = Math.min(map.size(), LIMIT);

        for (Map.Entry<User, Integer> entry : map.entrySet()) {
            if (limit == 0) {
                break;
            }

            User key = entry.getKey();
            jsonNode.put(key.getUsername(), entry.getValue());
            limit--;
        }

        return jsonNode;
    }
}
