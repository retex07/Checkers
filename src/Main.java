import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Main extends JPanel {
    public static final int sizeBoardGame = 164 * 3;

    public static void main(String[] args) {
        JFrame window = new JFrame("Шашки");
        Main content = new Main();
        window.setContentPane(content);
        window.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screensize.width - window.getWidth()) / 2,
                (screensize.height - window.getHeight()) / 2);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
    }

    private JButton newGameButton;
    private JButton resignButton;
    private JLabel message;
    private JLabel counter;
    private JLabel WhitePlayer;
    private JLabel BlackPlayer;
    private JTextField playerOne;
    private JTextField playerTwo;
    private JRadioButton gameWithPlayer;
    private JRadioButton gameWithRobot;
    private JButton aboutProgram;

    public Main() {
        int sizeStartGuiWindow = sizeBoardGame + 108;

        setLayout(null);
        setPreferredSize(new Dimension(sizeStartGuiWindow - 66, sizeStartGuiWindow + 100));

        setBackground(new Color(255, 255, 255));

        Board board = new Board();

        add(board);
        add(newGameButton);
        add(resignButton);
        add(gameWithPlayer);
        add(gameWithRobot);
        add(playerOne);
        add(playerTwo);
        add(WhitePlayer);
        add(BlackPlayer);
        add(counter);
        add(message);
        add(aboutProgram);

        board.setBounds(20, 20, sizeBoardGame, sizeBoardGame);
        counter.setBounds(0, sizeBoardGame + 20, sizeBoardGame, 30);
        message.setBounds(0, sizeBoardGame + 40, sizeBoardGame, 30);
        WhitePlayer.setBounds(sizeBoardGame / 4 + 2, sizeBoardGame + 60, sizeBoardGame / 4, 30);
        BlackPlayer.setBounds(sizeBoardGame / 2 + 20, sizeBoardGame + 60, sizeBoardGame / 4, 30);
        playerOne.setBounds(sizeBoardGame / 2 - 120, sizeBoardGame + 86, 120, 30);
        playerTwo.setBounds(sizeBoardGame / 2 + 20, sizeBoardGame + 86, 120, 30);
        newGameButton.setBounds(sizeBoardGame / 2 - 120, sizeBoardGame + 126, 120, 30);
        resignButton.setBounds(sizeBoardGame / 2 + 20, sizeBoardGame + 126, 120, 30);
        gameWithPlayer.setBounds(sizeBoardGame / 2 - 120, sizeBoardGame + 166, 120, 30);
        gameWithRobot.setBounds(sizeBoardGame / 2 + 20, sizeBoardGame + 166, 120, 30);
        aboutProgram.setBounds(sizeBoardGame / 2 - 80, 1, 180, 20);
    }

    public class Board extends JPanel implements ActionListener, MouseListener {
        CheckersData board; //Данные и правила для шашечной доски
        boolean gameInProgress; //Идет ли игра в данный момент

        int currentPlayer; //Чья теперь очередь ходить

        /* Если текущий игрок выбрал фигуру для перемещения,
        то эти две переменные дают строку и столбец, содержащие эту шашку.
        Если ни одна шашка еще не выбрана, то выбранная строка равна -1 */
        int selectedRow, selectedCol;

        int counterBlack = 0; //Кол-во срубленных черных (для счета)
        int counterWhite = 0; //Кол-во срубленных белых (для счета)

        CheckersMove[] legalMoves; //Массив, который содержит допустимые ходы для текущего игрока

        Board() {
            setBackground(Color.BLACK);
            addMouseListener(this);
            aboutProgram = new JButton("О программе");
            aboutProgram.addActionListener(this);
            gameWithPlayer = new JRadioButton("Игра с другом");
            gameWithPlayer.addActionListener(this);
            gameWithPlayer.setBackground(new Color(0xFFFFFF));
            gameWithRobot = new JRadioButton("Игра с роботом");
            gameWithRobot.addActionListener(this);
            gameWithRobot.setBackground(new Color(0xFFFFFF));
            gameWithRobot.setSelected(true);
            ButtonGroup groupButtonGame = new ButtonGroup();
            groupButtonGame.add(gameWithPlayer);
            groupButtonGame.add(gameWithRobot);
            resignButton = new JButton("Сдаться");
            resignButton.addActionListener(this);
            resignButton.setEnabled(false);
            resignButton.setBackground(Color.RED);
            resignButton.setForeground(Color.WHITE);
            newGameButton = new JButton("Начать игру");
            newGameButton.addActionListener(this);
            newGameButton.setBackground(Color.GREEN);
            newGameButton.setForeground(Color.WHITE);
            WhitePlayer = new JLabel("Ник белых:");
            WhitePlayer.setFont(new Font("Serif", Font.BOLD, 14));
            WhitePlayer.setForeground(Color.BLACK);
            BlackPlayer = new JLabel("Ник черных:");
            BlackPlayer.setFont(new Font("Serif", Font.BOLD, 14));
            BlackPlayer.setForeground(Color.BLACK);
            playerOne = new JTextField("Player1");
            playerOne.setHorizontalAlignment(JTextField.CENTER);
            playerTwo = new JTextField("Player2");
            playerTwo.setHorizontalAlignment(JTextField.CENTER);
            playerTwo.setEnabled(false);
            message = new JLabel("", JLabel.CENTER);
            message.setFont(new Font("Serif", Font.BOLD, 14));
            counter = new JLabel("", JLabel.CENTER);
            counter.setFont(new Font("Serif", Font.BOLD, 17));
            counter.setForeground(Color.BLACK);
            board = new CheckersData();
        }

        //Проверка на клик кнопок
        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();

            if (src == gameWithRobot)
                playerTwo.setEnabled(false);
            else if (src == gameWithPlayer) {
                playerTwo.setEnabled(true);
            } else if (src == newGameButton) {
                if ((playerOne.getText().equals("") || playerTwo.getText().equals("")) && !gameWithRobot.isSelected()) {
                    message.setForeground(Color.RED);
                    message.setText("Необходимо ввести имена игроков!");
                    counter.setText("");
                } else if (playerOne.getText().equals("") && gameWithRobot.isSelected()) {
                    message.setForeground(Color.RED);
                    message.setText("Необходимо ввести имя игрока белых шашек!");
                    counter.setText("");
                } else
                    doNewGame();
            } else if (src == aboutProgram) {
                JOptionPane.showMessageDialog(this,
                        """
                                ★ -_-_-_-_-_-_-_-_-_-_-_-_-_-_- | @retex07 | -_-_-_-_-_-_-_-_-_-_-_-_-_-_- ★

                                🛈 Об игре:
                                 Данное приложение представляет из
                                 себя классическую игру в русские шашки.
                                
                                ★ -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_- ★
                                
                                🛈 Начало игры:
                                 Выберите тип игры: "С другом" или "С роботом".
                                 Затем введите ник игрока(-ков) при необходимости
                                 и нажмите "Начать игру".
                                
                                ★ -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_- ★
                                
                                🛈 Ходы фигур:
                                 - Каждая фигура, если она не дамка, ходит всеголишь через одну
                                  клетку - по диагонали через фигуру;
                                 - Дамкой становится лишь та фигура, которая доходит до
                                  противоположного конца поля;
                                 - Игрок может срубить фигуру противника в случае, если фигура
                                  другого игрока стоит в соседней клетке по диагонали;
                                 - Игрок может срубить фигуру противника по всей диагонали поля,
                                  если фигура текущего игрока является дамкой;
                                 - Игрок не может сходить в соседнюю клекту, если в этой клетке
                                  стоит другая фигура;
                                 - Игрок не может срубить фигуру противника, если за его фигурой
                                  стоит вторая фигура подряд.
                                
                                ★ -_-_-_-_-_-_-_-_-_-_-_-_-_-_- | @retex07 | -_-_-_-_-_-_-_-_-_-_-_-_-_-_- ★
                                """,
                        "О программе", JOptionPane.PLAIN_MESSAGE
                );
            } else if (src == resignButton)
                doResign();
        }

        //Новая игра
        void doNewGame() {
            if (gameInProgress) {
                message.setForeground(Color.RED);
                message.setText("Сначала завершите текущую игру!");
                return;
            }

            counterBlack = 0; //Обнуляем счет по началу игры
            counterWhite = 0;

            board.setUpGame(); //Расставляем шашки по местам
            currentPlayer = CheckersData.WHITE; //Устанавливаем поочередность ходов, что белые ходят первыми
            legalMoves = board.getLegalMoves(CheckersData.WHITE); //Заполняем массив правильных ходов для начала игры у белых
            selectedRow = -1; //Устанавливаем, что белые еще не выбирали фигуру для перемещения
            message.setForeground(new Color(0xFFC900));
            message.setText("Сейчас ходит: " + playerOne.getText());
            counter.setText(counterBlack + " : " + counterWhite);
            gameInProgress = true;
            newGameButton.setEnabled(false);
            resignButton.setEnabled(true);

            if (gameWithRobot.isSelected())
                gameWithPlayer.setEnabled(false);
            else
                gameWithRobot.setEnabled(false);

            repaint();
        }

        //Сдаться
        void doResign() {
            if (!gameInProgress) {
                message.setText("Игра не запущена!");
                return;
            }
            if (currentPlayer == CheckersData.WHITE) {
                if (gameWithRobot.isSelected())
                    gameOver("Компьютер выиграл! " + playerOne.getText() + " сдался(-лась).");
                else
                    gameOver(playerTwo.getText() + " выиграл(-а)! " + playerOne.getText() + " сдался(-лась).");
            } else
                gameOver(playerOne.getText() + " выиграл(-а)! " + playerTwo.getText() + " сдался(-лась).");

            gameWithPlayer.setEnabled(true);
            gameWithRobot.setEnabled(true);
        }

        //Игра окончена
        void gameOver(String str) {
            message.setText(str);
            newGameButton.setEnabled(true);
            resignButton.setEnabled(false);
            gameInProgress = false;
        }

        /**
         * Это вызывается, когда игрок нажимает на квадрат в указанной строке и столбце.
         */
        void doClickSquare(int row, int col) {
             /* Если игрок нажал на одну из фигур, которую он может переместить,
             то отмечаем эту строку и столбец как выбранные.
             Сбрасываем сообщение, если ранее оно отображало сообщение об ошибке */

            for (int i = 0; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow == row && legalMoves[i].fromCol == col) {
                    selectedRow = row;
                    selectedCol = col;
                    if (currentPlayer == CheckersData.WHITE)
                        message.setText("Сейчас ходит: " + playerOne.getText());
                    else {
                        if (!gameWithRobot.isSelected())
                            message.setText("Сейчас ходит Компьютер");
                        else
                            message.setText("Сейчас ходит: " + playerTwo.getText());
                    }
                    repaint();
                    return;
                }
         
             /* Если ни одна фигура не была выбрана для перемещения,
             то игрок должен сначала выбрать фигуру */

            if (selectedRow < 0) {
                message.setText("Нажмите на фигуру, которую вы хотите переместить.");
                return;
            }
         
             /* Если игрок нажал на квадрат, куда выбранная фигура
             может быть перемещена, то делаем ход */

            for (int i = 0; i < legalMoves.length; i++)
                if (legalMoves[i].fromRow == selectedRow && legalMoves[i].fromCol == selectedCol
                        && legalMoves[i].toRow == row && legalMoves[i].toCol == col) {
                    doMakeMove(legalMoves[i]);
                    return;
                }
         
             /* Если функция не завершилась ранее и дошли до этого момента,
             пишем сообщение, что игрок должен нажать на нуный квадрат */

            message.setText("Щелкните квадрат, в который вы хотите переместиться.");
        }

        /**
         * Это вызывается, когда текущий игрок выбрал указанный ход.
         * Делаем ход, а затем либо завершаем, либо продолжаем игру
         */
        void doMakeMove(CheckersMove move) {
            boolean isCutDown = board.makeMove(move); //Проверяем, была ли срублена шашка другого игрока этим ходом

             /* Если ход был "срубить", то возможно, что у игрока есть еще один ход "срубить".
             Проверяем, можно ли срубить с квадрата, на который
             только что попал игрок. Если есть такой ход, то игрок должен срубить.
             Если игрок управляем Дамкой, то проверяем, что предыдущим ходом
             он действительно что-то срубил */

            if (move.isJump(board.pieceAt(move.toRow, move.toCol))) {
                //Если управляем дамкой и ход задействовал удаление шашки, то обновляем счет
                if (board.pieceAt(move.toRow, move.toCol) == CheckersData.WHITE_KING && isCutDown) {
                    counterBlack++;
                } else if (board.pieceAt(move.toRow, move.toCol) == CheckersData.BLACK_KING && isCutDown) {
                    counterWhite++;
                }

                /* Так как в проверке функции isJump для обычных фигур провряем переменщение на 2 клетки,
                то априори тут не может быть другого расклада и вражеская шашка была срублена.
                Обновляем счет */

                else if (board.pieceAt(move.toRow, move.toCol) == CheckersData.WHITE)
                    counterBlack++;
                else if (board.pieceAt(move.toRow, move.toCol) == CheckersData.BLACK)
                    counterWhite++;

                counter.setText(counterBlack + " : " + counterWhite);

                legalMoves = board.getLegalJumpsFrom(currentPlayer, move.toRow, move.toCol);

                if (gameWithRobot.isSelected() && legalMoves != null && currentPlayer == CheckersData.BLACK) {
                    CheckersMove temp = legalMoves[(int) (Math.random() * legalMoves.length)];
                    doClickSquare(temp.fromRow, temp.fromCol);
                    doClickSquare(temp.toRow, temp.toCol);
                }

                /* Если есть ходы "срубить" и предыдущим ходом мы рубили,
                то сообщаем об этом игроку и обновляем координаты */

                if (legalMoves != null && isCutDown) {
                    if (currentPlayer == CheckersData.WHITE)
                        message.setText(playerOne.getText() + ", продолжайте рубить.");
                    else
                        message.setText(playerTwo.getText() + ", продолжайте рубить.");
                    selectedRow = move.toRow;  //Так как можно переместить только одну шашку, то выбираем ее
                    selectedCol = move.toCol;
                    repaint();
                    return;
                }
            }
         
             /* Ход текущего игрока закончен, поэтому переключаемся на другого игрока.
             Получаем все возможные ходы этого игрока, и если у игрока нет
             праивльных ходов, то игра заканчивается */

            if (currentPlayer == CheckersData.WHITE) {
                currentPlayer = CheckersData.BLACK;
                legalMoves = board.getLegalMoves(currentPlayer);

                /* Если игра с роботом, то запустим автоматический рандомный выбор хода
                из списка возможных для черных шашек */

                if (legalMoves == null)
                    if (gameWithRobot.isSelected())
                        gameOver("У Компьютера больше нет ходов. " + playerOne.getText() + " выиграл(-а)!");
                    else
                        gameOver("У " + playerTwo.getText() + " больше нет ходов. " + playerOne.getText() + " выиграл(-а)!");
                else if (legalMoves[0].isJump(currentPlayer) && !gameWithRobot.isSelected())
                    message.setText(playerTwo.getText() + ", необходимо рубить.");
                else {
                    if (gameWithRobot.isSelected())
                        message.setText("Сейчас ходит Компьютер");
                    else
                        message.setText("Сейчас ходит: " + playerTwo.getText());
                }
            } else {
                currentPlayer = CheckersData.WHITE;
                legalMoves = board.getLegalMoves(currentPlayer);

                if (legalMoves == null)
                    if (gameWithRobot.isSelected())
                        gameOver("У " + playerOne.getText() + " больше нет ходов. " + "Компьютер" + " выиграл!");
                    else
                        gameOver("У " + playerOne.getText() + " больше нет ходов. " + playerTwo.getText() + " выиграл(-а)!");
                else if (legalMoves[0].isJump(currentPlayer))
                    message.setText(playerOne.getText() + ", необходимо рубить.");
                else
                    message.setText("Сейчас ходит: " + playerOne.getText());
            }
         
             /* Устанавливаем selectedRow = -1, чтобы указать,
             что игрок еще не выбрал фигуру для перемещения */

            selectedRow = -1;
         
             /* Если правильным ходом можно воспользоваться только одной шашкой,
             то автоматически ее выберем для удобства */

            if (legalMoves != null) {
                boolean sameStartSquare = true;
                for (int i = 1; i < legalMoves.length; i++)
                    if (legalMoves[i].fromRow != legalMoves[0].fromRow
                            || legalMoves[i].fromCol != legalMoves[0].fromCol) {
                        sameStartSquare = false;
                        break;
                    }
                if (sameStartSquare) {
                    selectedRow = legalMoves[0].fromRow;
                    selectedCol = legalMoves[0].fromCol;
                }
            }

            if (gameWithRobot.isSelected() && legalMoves != null && currentPlayer == CheckersData.BLACK) {
                CheckersMove temp = legalMoves[(int) (Math.random() * legalMoves.length)];
                doClickSquare(temp.fromRow, temp.fromCol);
                doClickSquare(temp.toRow, temp.toCol);
            }

            repaint();
        }

        /**
         * Рисуем доску и раскидываем по доске шашки.
         * Если игра продолжается, перечисляем возможные ходы в массиве
         */
        public void paintComponent(Graphics g) {
            //Рисуем черную рамку по краям доски
            g.setColor(Color.black);
            g.drawRect(1, 1, getSize().width - 3, getSize().height - 3);

            //Рассчитываем размеры шашек и их координаты на доске
            int sizeRowAndCol = sizeBoardGame / 8;
            int widthAndHeight = (int) (sizeBoardGame / 10.5);
            int cordCheckersBoard = (sizeRowAndCol - widthAndHeight) / 2 + 2;

            //Рисуем шашки и квадраты шахматной доски
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (row % 2 == col % 2)
                        g.setColor(Color.LIGHT_GRAY);
                    else
                        g.setColor(Color.GRAY);

                    g.fillRect(
                            2 + col * sizeRowAndCol,
                            2 + row * sizeRowAndCol,
                            sizeRowAndCol,
                            sizeRowAndCol
                    );

                    switch (board.pieceAt(row, col)) {
                        case CheckersData.WHITE -> {
                            g.setColor(Color.WHITE);
                            g.fillOval(cordCheckersBoard + col * sizeRowAndCol, cordCheckersBoard + row * sizeRowAndCol,
                                    widthAndHeight, widthAndHeight
                            );
                        }
                        case CheckersData.BLACK -> {
                            g.setColor(Color.BLACK);
                            g.fillOval(cordCheckersBoard + col * sizeRowAndCol, cordCheckersBoard + row * sizeRowAndCol,
                                    widthAndHeight, widthAndHeight
                            );
                        }
                        case CheckersData.WHITE_KING -> {
                            g.setColor(Color.WHITE);
                            g.fillOval(cordCheckersBoard + col * sizeRowAndCol, cordCheckersBoard + row * sizeRowAndCol,
                                    widthAndHeight, widthAndHeight
                            );
                            g.setColor(Color.BLACK);
                            g.drawString(
                                    "Д",
                                    sizeBoardGame / 17 + col * sizeRowAndCol,
                                    sizeBoardGame / 14 + row * sizeRowAndCol
                            );
                        }
                        case CheckersData.BLACK_KING -> {
                            g.setColor(Color.BLACK);
                            g.fillOval(cordCheckersBoard + col * sizeRowAndCol, cordCheckersBoard + row * sizeRowAndCol,
                                    widthAndHeight, widthAndHeight
                            );
                            g.setColor(Color.WHITE);
                            g.drawString(
                                    "Д",
                                    sizeBoardGame / 17 + col * sizeRowAndCol,
                                    sizeBoardGame / 14 + row * sizeRowAndCol
                            );
                        }
                    }
                }
            }

            //Если игра продолжается, перечисляем возможные ходы
            if (gameInProgress) {

                //Рисуем голубую рамку вокруг фигур, которые можно перемещать
                g.setColor(Color.cyan);

                for (int i = 0; i < legalMoves.length; i++) {
                    g.drawRect(
                            2 + legalMoves[i].fromCol * (sizeBoardGame / 8),
                            2 + legalMoves[i].fromRow * (sizeBoardGame / 8),
                            sizeBoardGame / 8 - 1, sizeBoardGame / 8 - 1
                    );
                }

                /* Если фигура выбрана для перемещения, то рисуем белую
                рамку вокруг этой фигуры и рисуем зеленые границы вокруг каждого квадрата,
                в который эта фигура может быть перемещена */

                if (selectedRow >= 0) {
                    g.setColor(Color.white);
                    g.drawRect(
                            2 + selectedCol * (sizeBoardGame / 8),
                            2 + selectedRow * (sizeBoardGame / 8),
                            sizeBoardGame / 8 - 1, sizeBoardGame / 8 - 1
                    );

                    g.setColor(Color.green);

                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].fromCol == selectedCol && legalMoves[i].fromRow == selectedRow) {
                            g.drawRect(
                                    2 + legalMoves[i].toCol * (sizeBoardGame / 8),
                                    2 + legalMoves[i].toRow * (sizeBoardGame / 8),
                                    sizeBoardGame / 8 - 1, sizeBoardGame / 8 - 1
                            );
                        }
                    }
                }
            }

        }

        /**
         * Отчеваем на нажатие игрока по доске. Если ход не выполняется,
         * отобразится сообщение об ошибке. Если ошибки нет, ищем строку и столбец,
         * по которым щелкнул пользователь, и вызываем doClickSquare(), чтобы обработать ход
         */
        public void mousePressed(MouseEvent evt) {
            if (!gameInProgress)
                message.setText("Нажмите \"Начать игру\" для начала новой партии.");
            else {
                int col = (evt.getX() - 2) / (sizeBoardGame / 8);
                int row = (evt.getY() - 2) / (sizeBoardGame / 8);
                if (col >= 0 && col < 8 && row >= 0 && row < 8)
                    doClickSquare(row, col);
            }
        }

        public void mouseReleased(MouseEvent evt) {
        }

        public void mouseClicked(MouseEvent evt) {
        }

        public void mouseEntered(MouseEvent evt) {
        }

        public void mouseExited(MouseEvent evt) {
        }
    }
}