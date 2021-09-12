package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.Scrabble;

/**
 * This is the base controller, all others controllers should inherit from this BaseController.
 *
 * @author Christopher Anciaux
 */
abstract public class BaseController {
    protected Scrabble scrabble;

    /**
     * Is called by the main application to give a reference back to itself
     *
     * @param scrabble The main application
     */
    public void setScrabble(Scrabble scrabble) {
        this.scrabble = scrabble;
    }
}
