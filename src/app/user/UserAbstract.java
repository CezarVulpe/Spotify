package app.user;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The type User abstract.
 */
public abstract class UserAbstract {
    private String username;
    private int age;
    private String city;

    /**
     * Instantiates a new User abstract.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public UserAbstract(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Gets age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets age.
     *
     * @param age the age
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * Gets city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets city.
     *
     * @param city the city
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * User type string.
     *
     * @return the string
     */
    public abstract String userType();

    /**
     * Accepts a WrappedVisitor and invokes the corresponding visit method.
     *
     * @param v The WrappedVisitor to accept.
     * @return The ObjectNode result of the visit operation.
     */
    public ObjectNode accept(final WrappedVisitor v) {
        return null;
    }
    /**
     * Accepts a GetNotificationsVisitor and invokes the corresponding visit method.
     *
     * @param v The GetNotificationsVisitor to accept.
     * @return The ObjectNode result of the visit operation.
     */
    public ObjectNode accept(final GetNotificationsVisitor v) {
        return null;
    }

    /**
     * Gets a string representation of the key. Implementing
     * classes should provide a meaningful
     * representation of the object that serves as a key.
     *
     * @return A string representation of the key.
     */
    public abstract String getKeyRepresentation();
}
