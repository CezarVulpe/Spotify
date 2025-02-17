package app.player;

import app.pages.Page;
import app.user.User;

public class ChangePage implements Command {
    private final User user;
    private Page previousPage;
    private Page nextPage;

    /**
     * The ChangePage class represents a command for
     * changing the current page of a user.
     * It implements the Command interface, providing
     * functionality for execution, undo, and redo.
     */
    public ChangePage(final User user, final Page page) {
        this.user = user;
        this.nextPage = page;
    }

    /**
     * Executes the page change by setting the current page to the specified next page.
     */
    @Override
    public void execute() {
        previousPage = user.getCurrentPage();
        user.setCurrentPage(nextPage);
    }

    /**
     * Undoes the previous page change, reverting to the page before the execution.
     */
    @Override
    public void undo() {
        nextPage = previousPage;
        previousPage = user.getCurrentPage();
        user.setCurrentPage(nextPage);
    }

    /**
     * Redoes the page change, reverting to the page set during the initial execution.
     */
    @Override
    public void redo() {
        user.setCurrentPage(previousPage);
    }
}
