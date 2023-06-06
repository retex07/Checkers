import java.util.ArrayList;

/**
 * Объект этого класса содержит данные и правила об игре. Он знает,
 * какая фигура находится на каждом квадрате шахматной доски и как должна двигаться
 */
public class CheckersData {
    static final int
            EMPTY = 0,
            WHITE = 1,
            WHITE_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;

    int[][] board;  // board[r][c] - содержимое строки r, столбца c.

    /**
     * Создаем доску и настраиваем ее для новой игры
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    /**
     * Расставляем шашки по местам на доске для начала игры.
     * Шашки можно найти только в квадратах, которые соответствуеют строке "row % 2 == col % 2".
     * В начале игры в первых трех рядах содержат черные шашки,
     * а в последних трех рядах содержат белые шашки
     */
    void setUpGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == col % 2) {
                    if (row < 3)
                        board[row][col] = BLACK;
                    else if (row > 4)
                        board[row][col] = WHITE;
                    else
                        board[row][col] = EMPTY;
                } else {
                    board[row][col] = EMPTY;
                }
            }
        }
    }

    /**
     * Возвращает содержимое квадрата в указанных строке и столбце
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }

    /**
     * Делаем указанный переданный в функцию ход. Заудманно, что перемещение не равно нулю и что
     * перемещение, которое оно представляет, является правильным.
     * Возвращается булевый ответ на вопрос: была ли срублена фигура?
     */
    boolean makeMove(CheckersMove move) {
        return makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
    }

    /**
     * Выполняем переход от (fromRow, fromCol) к (toRow, toCol)
     * Если ход - срубить, то срубленную фигуру удаляеяем с доски.
     * Если фигура перемещается на последний ряд на стороне противника на доске, она становится дамкой.
     * Возвращается булевый ответ на вопрос: была ли срублена фигура?
     */
    boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        boolean flag = false;

        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = EMPTY;
        if (board[toRow][toCol] == WHITE || board[toRow][toCol] == BLACK) {
            if (fromRow - toRow == 2 || fromRow - toRow == -2) {
                //Этот ход - срубить, поэтому убираем срубившую фигуру с доски
                int jumpRow = (fromRow + toRow) / 2;
                int jumpCol = (fromCol + toCol) / 2;
                board[jumpRow][jumpCol] = EMPTY;
                flag = true;
            }
        } else if (board[toRow][toCol] == WHITE_KING || board[toRow][toCol] == BLACK_KING) {
            int jumpRow, jumpCol;
            int counterCol = 0;

            if (fromRow < toRow && fromCol < toCol) {
                for (int i = fromRow + 1; i < toRow; i++) {
                    counterCol++;
                    if (
                            board[i][fromCol + counterCol] == WHITE
                                    || board[i][fromCol + counterCol] == BLACK
                                    || board[i][fromCol + counterCol] == WHITE_KING
                                    || board[i][fromCol + counterCol] == BLACK_KING
                    ) {
                        jumpRow = i;
                        jumpCol = fromCol + counterCol;
                        board[jumpRow][jumpCol] = EMPTY;
                        flag = true;
                        break;
                    }
                }
            } else if (fromRow > toRow && fromCol > toCol) {
                for (int i = fromRow - 1; i > toRow; i--) {
                    counterCol++;
                    if (
                            board[i][fromCol - counterCol] == WHITE
                                    || board[i][fromCol - counterCol] == BLACK
                                    || board[i][fromCol - counterCol] == WHITE_KING
                                    || board[i][fromCol - counterCol] == BLACK_KING
                    ) {
                        jumpRow = i;
                        jumpCol = fromCol - counterCol;
                        board[jumpRow][jumpCol] = EMPTY;
                        flag = true;
                        break;
                    }
                }
            } else if (fromRow < toRow && fromCol > toCol) {
                for (int i = fromRow + 1; i < toRow; i++) {
                    counterCol++;
                    if (
                            board[i][fromCol - counterCol] == WHITE
                                    || board[i][fromCol - counterCol] == BLACK
                                    || board[i][fromCol - counterCol] == WHITE_KING
                                    || board[i][fromCol - counterCol] == BLACK_KING
                    ) {
                        jumpRow = i;
                        jumpCol = fromCol - counterCol;
                        board[jumpRow][jumpCol] = EMPTY;
                        flag = true;
                        break;
                    }
                }
            } else if (fromRow > toRow && fromCol < toCol) {
                for (int i = fromRow - 1; i > toRow; i--) {
                    counterCol++;
                    if (
                            board[i][fromCol + counterCol] == WHITE
                                    || board[i][fromCol + counterCol] == BLACK
                                    || board[i][fromCol + counterCol] == WHITE_KING
                                    || board[i][fromCol + counterCol] == BLACK_KING
                    ) {
                        jumpRow = i;
                        jumpCol = fromCol + counterCol;
                        board[jumpRow][jumpCol] = EMPTY;
                        flag = true;
                        break;
                    }
                }
            }
        }

        if (toRow == 0 && board[toRow][toCol] == WHITE)
            board[toRow][toCol] = WHITE_KING;
        if (toRow == 7 && board[toRow][toCol] == BLACK)
            board[toRow][toCol] = BLACK_KING;

        return flag;
    }

    /**
     * Возвращает массив, в котором лежат все возможные ходы шашек для указанного
     * квадрата с шашкой на доске. Если у игрока нет ходов, то возвращается
     * значение null. Если значение "player" != BLACK или WHITE, то вернется null.
     * Если возвращаемое значение не равно null, оно состоит либо из хода "срубить", либо из обычных.
     * Если хода "срубить" не нашлось, то заполняем возможными обычными ходами.
     */
    CheckersMove[] getLegalMoves(int player) {
        if (player != WHITE && player != BLACK)
            return null;

        int playerKing;
        if (player == WHITE)
            playerKing = WHITE_KING;
        else
            playerKing = BLACK_KING;

        ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();

             /*  Сначала проверяем, нет ли каких-либо возможных ходов "срубить".
             Смотрим на каждый квадрат на доске. Если в квадрате находится
             одна из фигур игрока, смотрим на возможный ход "срубить" в каждом
             из четырех направлений с этого квадрата. Если есть ход "срубить"
             в этом направлении, помещаем его в массив "moves" */

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == player) {
                    if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                        moves.add(new CheckersMove(row, col, row + 2, col + 2));
                    if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                        moves.add(new CheckersMove(row, col, row - 2, col + 2));
                    if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                        moves.add(new CheckersMove(row, col, row + 2, col - 2));
                    if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                        moves.add(new CheckersMove(row, col, row - 2, col - 2));
                } else if (board[row][col] == playerKing) {
                    for (int i = 0; i < 8; i++) {
                        if (canJump(playerKing, row, col, 0, 0, row + i + 1, col + i + 1))
                            moves.add(new CheckersMove(row, col, row + i + 1, col + i + 1));
                        if (canJump(playerKing, row, col, 0, 0, row - i - 1, col + i + 1))
                            moves.add(new CheckersMove(row, col, row - i - 1, col + i + 1));
                        if (canJump(playerKing, row, col, 0, 0, row + i + 1, col - i - 1))
                            moves.add(new CheckersMove(row, col, row + i + 1, col - i - 1));
                        if (canJump(playerKing, row, col, 0, 0, row - i - 1, col - i - 1))
                            moves.add(new CheckersMove(row, col, row - i - 1, col - i - 1));
                    }
                }
            }
        }

             /*  Если были найдены какие-либо ходы "срубить", то игрок
             должен рубить, поэтому мы не добавляем никаких обычных ходов.
             Если же хода "срубить" не нашлось, то проверяем наличие
             каких-либо обычных ходов.
             Смотрм на каждый квадрат на доске, если на этом квадрате находится одна из фигур игрока,
             смотрим на возможный ход в каждом из четырех направлений от этого квадрата.
             Если есть возможный ход в этом направлении, заносим его в массив "moves" */

        if (moves.size() == 0) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == player) {
                        //Отсюда добавляем проверку на ходы для обынчых фигур и дамок в 1 клетку
                        if (canMove(player, row, col, row + 1, col + 1))
                            moves.add(new CheckersMove(row, col, row + 1, col + 1));
                        if (canMove(player, row, col, row - 1, col + 1))
                            moves.add(new CheckersMove(row, col, row - 1, col + 1));
                        if (canMove(player, row, col, row + 1, col - 1))
                            moves.add(new CheckersMove(row, col, row + 1, col - 1));
                        if (canMove(player, row, col, row - 1, col - 1))
                            moves.add(new CheckersMove(row, col, row - 1, col - 1));
                    } else if (board[row][col] == playerKing) {
                        //Отсюда добавляем проверку на ходы для дамок от 2х до 7 клеток
                        for (int i = 0; i < 8; i++) {
                            if (canMove(playerKing, row, col, row + i, col + i))
                                moves.add(new CheckersMove(row, col, row + i, col + i));
                            if (canMove(playerKing, row, col, row - i, col + i))
                                moves.add(new CheckersMove(row, col, row - i, col + i));
                            if (canMove(playerKing, row, col, row + i, col - i))
                                moves.add(new CheckersMove(row, col, row + i, col - i));
                            if (canMove(playerKing, row, col, row - i, col - i))
                                moves.add(new CheckersMove(row, col, row - i, col - i));
                        }
                    }
                }
            }
        }

             /* Если нет никаких правильных ходов, то возвращаем значение null.
             Если же ходы есть, то создаем массив, чтобы вместить туда все ходы.
             Копируем все ходы из "moves" в массив "moveArray" и вохвращем его */

        if (moves.size() == 0)
            return null;
        else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }

    /**
     * Возвращаем список возможных ходов "срубить", которые может совершить
     * указанный игрок, начиная с указанных строки и столбца.
     * Если такие ходы невозможны, то возвращается значение null
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        if (player != WHITE && player != BLACK)
            return null;

        int playerKing;
        if (player == WHITE)
            playerKing = WHITE_KING;
        else
            playerKing = BLACK_KING;

        ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();

        if (board[row][col] == player) {
            if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
                moves.add(new CheckersMove(row, col, row + 2, col + 2));
            if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
                moves.add(new CheckersMove(row, col, row - 2, col + 2));
            if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
                moves.add(new CheckersMove(row, col, row + 2, col - 2));
            if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
                moves.add(new CheckersMove(row, col, row - 2, col - 2));
        } else if (board[row][col] == playerKing) {
            for (int i = 0; i < 8; i++) {
                if (canJump(playerKing, row, col, 0, 0, row + i + 1, col + i + 1))
                    moves.add(new CheckersMove(row, col, row + i + 1, col + i + 1));
                if (canJump(playerKing, row, col, 0, 0, row - i - 1, col + i + 1))
                    moves.add(new CheckersMove(row, col, row - i - 1, col + i + 1));
                if (canJump(playerKing, row, col, 0, 0, row + i + 1, col - i - 1))
                    moves.add(new CheckersMove(row, col, row + i + 1, col - i - 1));
                if (canJump(playerKing, row, col, 0, 0, row - i - 1, col - i - 1))
                    moves.add(new CheckersMove(row, col, row - i - 1, col - i - 1));
            }
        }

        if (moves.size() == 0)
            return null;
        else {
            CheckersMove[] moveArray = new CheckersMove[moves.size()];
            for (int i = 0; i < moves.size(); i++)
                moveArray[i] = moves.get(i);
            return moveArray;
        }
    }

    /**
     * Эту функцию вызываем, чтобы проверить, может ли
     * игрок действительно перейти с (r1, c1) на (r3, c3)
     * (r2, c2) - квадрат между (r1, c1) и (r3, c3)
     */
    boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {
        //При проверке ходов дамок считаем кол-во черных и белых в промежутке
        // между ходом. Если больше одной шашки противника подряд, значит ход невозможен
        int countBlack = 0, countWhite = 0;

        int check = 0; //Прибавляем к каждому слобцу количество сдвига по строкам при проверке хода дамки

        if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
            return false;  //(r3,c3) выходит за пределы доски

        else if (board[r3][c3] != EMPTY)
            return false;  //(r3,c3) уже содержит фигуру

        else if (player == WHITE) {
            if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING)
                return false;  //Здесь нет черной фигуры, которую можно было бы срубить
            return true;  //Шашку можно срубить
        } else if (player == BLACK) {
            if (board[r2][c2] != WHITE && board[r2][c2] != WHITE_KING)
                return false;  //Здесь нет белой фигуры, которую можно было бы срубить
            return true;  //Шашку можно срубить
        } else {
            if ((r3 - r1) > 1 && (c3 - c1) > 1) {
                for (int i = r1 + 1; i < r3; i++) {
                    check++;
                    if (board[i][c1 + check] == BLACK || board[i][c1 + check] == BLACK_KING)
                        countBlack++;
                    else if (board[i][c1 + check] == WHITE || board[i][c1 + check] == WHITE_KING)
                        countWhite++;
                }
            } else if ((r3 - r1) < -1 && (c3 - c1) > 1) {
                for (int i = r1 - 1; i > r3; i--) {
                    check++;
                    if (board[i][c1 + check] == BLACK || board[i][c1 + check] == BLACK_KING)
                        countBlack++;
                    else if (board[i][c1 + check] == WHITE || board[i][c1 + check] == WHITE_KING)
                        countWhite++;
                }
            } else if ((r3 - r1) > 1 && (c3 - c1) < -1) {
                for (int i = r1 + 1; i < r3; i++) {
                    check++;
                    if (board[i][c1 - check] == BLACK || board[i][c1 - check] == BLACK_KING)
                        countBlack++;
                    if (board[i][c1 - check] == WHITE || board[i][c1 - check] == WHITE_KING)
                        countWhite++;
                }
            } else if ((r3 - r1) < -1 && (c3 - c1) < -1) {
                for (int i = r1 - 1; i > r3; i--) {
                    check++;
                    if (board[i][c1 - check] == BLACK || board[i][c1 - check] == BLACK_KING)
                        countBlack++;
                    else if (board[i][c1 - check] == WHITE || board[i][c1 - check] == WHITE_KING)
                        countWhite++;
                }
            }

            //Рубить либо некого, либо больше одной подряд
            if (player == WHITE_KING && (countBlack == 0 || countBlack > 1 || countWhite >= 1))
                return false;
            if (player == BLACK_KING && (countWhite == 0 || countWhite > 1 || countBlack >= 1))
                return false;
            else
                return true;
        }
    }

    /**
     * Это вызывается, чтобы определить,
     * может ли игрок легально перейти из (r1,c1) в (r2,c2).
     * Предполагается, что (r1,r2) содержит одну из фигур игрока
     * и что (r2,c2) является соседней клеткой
     */
    boolean canMove(int player, int r1, int c1, int r2, int c2) {
        //При проверке ходов дамок считаем кол-во черных и белых в промежутке
        // между ходом. Если больше одной шашки противника подряд, значит ход невозможен
        int countBlack = 0, countWhite = 0;

        int check = 0; //Прибавляем к каждому слобцу количество сдвига по строкам при проверке хода дамки

        if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
            return false;  //(r2,c2) выходят за пределы доски

        else if (board[r2][c2] != EMPTY)
            return false;  //(r2,c2) уже содержит фигуру

        else if (player == WHITE) {
            if ((board[r1][c1] == WHITE && r2 > r1) || (board[r1][c1] == WHITE && r1 - r2 > 1))
                return false;  //Обычная белая фигура может двигаться только вниз на одну клетку
            return true;  //Этот ход разрешен
        } else if (player == BLACK) {
            if ((board[r1][c1] == BLACK && r2 < r1) || (board[r1][c1] == BLACK && r2 - r1 > 1))
                return false;  //Обычная черная фигура может двигаться только вверх на одну клетку
            return true;  //Этот ход разрешен
        } else {
            if ((r2 - r1) > 2 && (c2 - c1) > 2) {
                for (int i = r1 + 1; i < r2; i++) {
                    check++;
                    if (board[i][c1 + check] == BLACK || board[i][c1 + check] == BLACK_KING)
                        countBlack++;
                    else if (board[i][c1 + check] == WHITE || board[i][c1 + check] == WHITE_KING)
                        countWhite++;
                }
            } else if ((r2 - r1) < -2 && (c2 - c1) > 2) {
                for (int i = r1 - 1; i > r2; i--) {
                    check++;
                    if (board[i][c1 + check] == BLACK || board[i][c1 + check] == BLACK_KING)
                        countBlack++;
                    else if (board[i][c1 + check] == WHITE || board[i][c1 + check] == WHITE_KING)
                        countWhite++;
                }
            } else if ((r2 - r1) > 2 && (c2 - c1) < -2) {
                for (int i = r1 + 1; i < r2; i++) {
                    check++;
                    if (board[i][c1 - check] == BLACK || board[i][c1 - check] == BLACK_KING)
                        countBlack++;
                    if (board[i][c1 - check] == WHITE || board[i][c1 - check] == WHITE_KING)
                        countWhite++;
                }
            } else if ((r2 - r1) < -2 && (c2 - c1) < -2) {
                for (int i = r1 - 1; i > r2; i--) {
                    check++;
                    if (board[i][c1 - check] == BLACK || board[i][c1 - check] == BLACK_KING)
                        countBlack++;
                    else if (board[i][c1 - check] == WHITE || board[i][c1 - check] == WHITE_KING)
                        countWhite++;
                }
            }

            //Пустое ли пространство для хода
            if ((player == WHITE_KING || player == BLACK_KING) && (countBlack > 0 || countWhite > 0))
                return false;
            else
                return true;
        }
    }
}
