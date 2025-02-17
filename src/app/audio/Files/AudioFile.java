package app.audio.Files;

import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioFile extends LibraryEntry {
    private final Integer duration;

    public AudioFile(final String name, final Integer duration) {
        super(name);
        this.duration = duration;
    }

    /**
     * Gets a string representation of the key. Implementing classes
     * should provide a meaningful
     * representation of the object that serves as a key.
     *
     * @return A string representation of the key.
     */
    public String getKeyRepresentation() {
        return "";
    }
}
