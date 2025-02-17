package app.pages;

import app.user.UserAbstract;
import app.utils.Enums;

/**
 * The interface Page.
 */
public interface Page {
    Enums.UserType USER_TYPE = Enums.UserType.USER;

    /**
     * Print current page string.
     *
     * @return the current page string
     */
    String printCurrentPage();
    /**
     * Gets the user type associated with this page.
     *
     * @return The user type.
     */
    Enums.UserType getType();
    /**
     * Gets the user abstract associated with this page.
     *
     * @return The user abstract.
     */
    UserAbstract getUserAbstract();

}
