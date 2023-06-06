import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckersMoveTest {

    @Test
    void isJump() {
        //Ход на 1 клетку (обычный ход - для обычной фигуры и для дамки)
        CheckersMove checkersMove = new CheckersMove(0, 0, 1, 1);
        int playerWhite = CheckersData.WHITE;
        int playerBlack = CheckersData.BLACK;
        int playerWhiteKing = CheckersData.WHITE_KING;
        int playerBlackKing = CheckersData.BLACK_KING;

        assertFalse(checkersMove.isJump(playerWhite));
        assertFalse(checkersMove.isJump(playerBlack));
        assertTrue(checkersMove.isJump(playerWhiteKing));
        assertTrue(checkersMove.isJump(playerBlackKing));

        //Ход на 2 клетки (срубить - для обычной фигуры, возможно обычный ход - для дамки)
        CheckersMove checkersMove2 = new CheckersMove(0, 0, 2, 2);
        int playerWhite2 = CheckersData.WHITE;
        int playerBlack2 = CheckersData.BLACK;
        int playerWhiteKing2 = CheckersData.WHITE_KING;
        int playerBlackKing2 = CheckersData.BLACK_KING;

        assertTrue(checkersMove2.isJump(playerWhite2));
        assertTrue(checkersMove2.isJump(playerBlack2));
        assertTrue(checkersMove2.isJump(playerWhiteKing2));
        assertTrue(checkersMove2.isJump(playerBlackKing2));
    }
}