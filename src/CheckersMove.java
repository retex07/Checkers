/** Пермещение координат (ходы) в игре */
public class CheckersMove {
    int fromRow, fromCol;
    int toRow, toCol;

    CheckersMove(int r1, int c1, int r2, int c2) {
        fromRow = r1;
        fromCol = c1;
        toRow = r2;
        toCol = c2;
    }

    /* Проверяем на правильность хода "срубить". Если это дамка,
    то любой ход примем за "срубить" */
    boolean isJump(int player) {
        if (player == CheckersData.BLACK_KING || player == CheckersData.WHITE_KING)
            return true;
        else if ((player == CheckersData.WHITE || player == CheckersData.BLACK) && (fromRow - toRow == 2 || fromRow - toRow == -2))
            return true;
        return false;
    }
}
