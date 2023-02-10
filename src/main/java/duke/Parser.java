package duke;

import command.*;
import exception.DukeException;
import exception.UnknownInputException;
import exception.EmptyDescriptionException;

/**
 * Represents a parser object to parse input commands
 */
public class Parser {
    private DukeList dukelist;
    private Storage storage;
    private Ui ui;

    /**
     * Creates a Parser object
     *
     * @param dukeList a DukeList that can be accessed by this Parser
     * @param storage a Storage that is used to read and write
     */
    public Parser (DukeList dukeList, Storage storage , Ui ui) {
        this.dukelist = dukeList;
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Returns the respective DukeExceptions
     *
     * @param firstWord the String that used to check the type of DukeException to return
     * @return EmptyDescriptionException if the firstWord is a recognized task name, and
     * UnknownInputException if it is not recognized.
     */

    private DukeException checkAddExceptions(String firstWord) {

        boolean isTodo = firstWord.equals("todo");
        boolean isDeadline = firstWord.equals("deadline");
        boolean isEvent = firstWord.equals("event");
        if (isTodo || isDeadline || isEvent){
            return new EmptyDescriptionException("Sorry, your " + firstWord + " task description is missing.");
        } else {
            return new UnknownInputException("Hmm, I'm not sure what you're saying man.");
        }

    }

    /**
     * Translates an input String to its corresponding command
     * @param inputText the input String that is to be parsed
     * @return the command that corresponds to the input text
     * @throws EmptyDescriptionException when there is a valid command with no content
     * @throws UnknownInputException when the input text doe not correspond to any known command
     */
    public Command parse(String inputText) throws DukeException{
        String[] array = inputText.split(" ", 2);
        try {
            String firstWord = array[0];
            boolean isWrongLength = array.length != 2;
            if (inputText.equals("bye")) {
                return new ExitCommand(this.dukelist, this.storage, this.ui);
            } else {
                switch (firstWord) {
                case "list":
                    return new ListCommand(this.dukelist, this.ui);
                case "mark":
                    return new MarkCommand(array[1], true, dukelist);
                case "unmark":
                    return new MarkCommand(array[1], false, dukelist);
                case "delete":
                    return new DeleteCommand(array[1], dukelist);
                case "find":
                    return new FindCommand(array[1], dukelist);
                default:
                    if (isWrongLength) {
                        throw checkAddExceptions(firstWord);
                    }
                    return new AddCommand(array[0], array[1], dukelist);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Your command is too short man");
        }
        return new InvalidCommand();
    }
}
