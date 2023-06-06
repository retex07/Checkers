import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckersDataTest {

    @Test
    void pieceAt() {
        CheckersData board = new CheckersData();
        board.setUpGame();
        assertEquals(CheckersData.BLACK, board.pieceAt(0, 0));
        assertEquals(CheckersData.WHITE, board.pieceAt(7, 7));
    }

    @Test
    void makeMove() {
        CheckersData board = new CheckersData();
        board.setUpGame();

        assertFalse(board.makeMove(5, 1, 4, 0));
        board.makeMove(2, 2, 3, 1);
        assertTrue(board.makeMove(4, 0, 2, 2));
    }

    @Test
    void canJump() {
        CheckersData board = new CheckersData();
        board.setUpGame();

        assertFalse(board.canJump(CheckersData.BLACK, 0, 0, 1, 1, 2, 2));
        assertFalse(board.canJump(CheckersData.BLACK, 1, 1, 2, 2, 3, 3));
        assertFalse(board.canJump(CheckersData.BLACK, 2, 2, 3, 3, 4, 4));

        assertFalse(board.canJump(CheckersData.WHITE, 7, 7, 6, 6, 5, 5));
        assertFalse(board.canJump(CheckersData.WHITE, 6, 6, 5, 5, 4, 4));
        assertFalse(board.canJump(CheckersData.WHITE, 5, 5, 4, 4, 3, 3));

        board.makeMove(5, 3, 4, 2);
        board.makeMove(2, 2, 3, 3);
        board.makeMove(4, 2, 3, 1);
        assertTrue(board.canJump(CheckersData.BLACK, 2, 0, 3, 1, 4, 2));

        board.setUpGame();

        board.makeMove(5, 1, 4, 0);
        board.makeMove(2, 2, 3, 1);
        assertTrue(board.canJump(CheckersData.WHITE, 4, 0, 3, 1, 2, 2));
    }

    @Test
    void canMove() {
        CheckersData board = new CheckersData();
        board.setUpGame();

        assertFalse(board.canMove(CheckersData.WHITE, 5, 1, 6, 2));
        assertFalse(board.canMove(CheckersData.WHITE, 7, 7, 8, 8));
        assertFalse(board.canMove(CheckersData.WHITE, 7, 7, 6, 6));
        assertFalse(board.canMove(CheckersData.WHITE, 5, 5, 3, 3));
        assertTrue(board.canMove(CheckersData.WHITE, 5, 1, 4, 0));
        assertTrue(board.canMove(CheckersData.WHITE, 5, 1, 4, 2));

        assertFalse(board.canMove(CheckersData.BLACK, 0, 0, -1, -1));
        assertFalse(board.canMove(CheckersData.BLACK, 0, 0, 1, 1));
        assertFalse(board.canMove(CheckersData.BLACK, 1, 1, 0, 0));
        assertFalse(board.canMove(CheckersData.BLACK, 1, 1, 3, 3));
        assertTrue(board.canMove(CheckersData.BLACK, 2, 2, 3, 0));
        assertTrue(board.canMove(CheckersData.BLACK, 2, 2, 3, 2));
    }
}