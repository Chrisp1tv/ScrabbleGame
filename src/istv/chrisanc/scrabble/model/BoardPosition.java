package istv.chrisanc.scrabble.model;

/**
 * This class is used to send a board position to other objects during turn validation only, and only in this case.
 *
 * @author Christopher Anciaux
 */
public class BoardPosition implements Comparable<BoardPosition> {
    /**
     * The line on which the letter has been played
     */
    protected short line;

    /**
     * The column on which the letter has been played
     */
    protected short column;

    public BoardPosition(short line, short column) {
        this.line = line;
        this.column = column;
    }

    public short getLine() {
        return this.line;
    }

    public short getColumn() {
        return this.column;
    }

    @Override
    public int compareTo(BoardPosition o) {
        if (o.getLine() == this.getLine()) {
            return this.getColumn() - o.getColumn();
        }

        return this.getLine() - o.getLine();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        BoardPosition that = (BoardPosition) o;

        return this.line == that.line && this.column == that.column;
    }

    @Override
    public int hashCode() {
        int result = (int) this.line;
        result = 31 * result + (int) this.column;

        return result;
    }
}
