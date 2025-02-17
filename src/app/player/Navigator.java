package app.player;

import java.util.LinkedList;

public class Navigator {
    private LinkedList<Command> history = new LinkedList<>();
    private LinkedList<Command> historynext = new LinkedList<>();

    /**
     * Executes a command, adds it to the command history, and clears the redo history.
     *
     * @param command The command to execute and add to the history.
     */
    public void change(final Command command) {
        history.push(command);
        command.execute();
        historynext.clear();
    }

    /**
     * Undoes the last executed command by popping it from the history stack,
     * pushing it onto the redo stack, and calling the command's undo method.
     *
     * @return True if undo was successful, false otherwise (history is empty).
     */
    public boolean undo() {
        if (history.isEmpty()) {
            return false;
        }

        Command command = history.pop();
        historynext.push(command);
        if (command != null) {
            command.undo();
        }
        return true;
    }
    /**
     * Redoes the last undone command by popping it from the redo stack,
     * pushing it onto the history stack, and calling the command's redo method.
     *
     * @return True if redo was successful, false otherwise (redo history is empty).
     */
    public boolean redo() {
        if (historynext.isEmpty()) {
            return false;
        }

        Command command = historynext.pop();
        history.push(command);
        command.redo();
        return true;
    }

}
