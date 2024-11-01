import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class BoardPanel extends JPanel implements MouseListener, KeyListener{

    static {
        System.load("/Users/alieksieiev/CLionProjects/Checkers/cmake-build-debug/libCheckers.dylib");
    }

    ArrayList<ColorListener> colorlistener = new ArrayList<>();
    CheckersJNI jni = new CheckersJNI();

    public BoardPanel() {
        setFocusable(true);
        addMouseListener(this);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintBoard(g);
        paintPieces(g);
    }

    public void paintBoard(Graphics g){
        int squareSizeX = getWidth() / 8;
        int squareSizeY = getHeight() / 8;

        for(int i=0, x=0; i<8; i++, x+=squareSizeX){
            for(int j=0, y=0; j<8; j++, y+=squareSizeY){
                boolean isDarkSquare = (j + i) % 2 == 1;

                // Подсветка выбранной клетки
                if (j == jni.getSelectedRow() && i == jni.getSelectedCol())
                    g.setColor(isDarkSquare ? new Color(168,182,88) : new Color(201, 223, 126));
                else
                    g.setColor(isDarkSquare ? new Color(174,137,104) : new Color(236, 219, 185));

                g.fillRect(x, y, squareSizeX, squareSizeY);
            }

            for (ColorListener colorListener : colorlistener){
                colorListener.colorChanged(new ColorChangedEvent(this, jni.getCurrentPlayer() ? Color.WHITE : Color.BLACK, !jni.getCurrentPlayer() ? Color.WHITE : Color.BLACK));
            }
        }
    }

    public void paintPieces(Graphics g){

        int squareSizeX = getWidth() / 8;
        int squareSizeY = getHeight() / 8;

        for(int i=0, x=0; i<jni.getBoardSize(); i++, x+=squareSizeX){
            for(int j=0, y=0; j<jni.getBoardSize(); j++, y+=squareSizeY){
                if (jni.getBoardValue(j,i) == 1) {
                    g.setColor(Color.BLACK);
                    g.fillOval(x + (squareSizeX - squareSizeX + 10) / 2,
                               y + (squareSizeY - squareSizeY + 10) / 2,
                            squareSizeX - 10,
                            squareSizeY - 10);
                }
                else if (jni.getBoardValue(j,i) == 2) {
                    g.setColor(Color.WHITE);
                    g.fillOval(x + (squareSizeX - squareSizeX+10) / 2,
                               y + (squareSizeY - squareSizeY+10) / 2,
                            squareSizeX-10,
                            squareSizeY-10);
                }

                else if (jni.getBoardValue(j,i) == 3) {
                    g.setColor(Color.WHITE);
                    g.fillOval(x + (squareSizeX - squareSizeX+10) / 2,
                            y + (squareSizeY - squareSizeY+10) / 2,
                            squareSizeX-10,
                            squareSizeY-10);
                    g.setColor(Color.BLACK);
                    g.fillOval(x + (squareSizeX - squareSizeX+20) / 2,
                            y + (squareSizeY - squareSizeY+20) / 2,
                            squareSizeX-20,
                            squareSizeY-20);
                }

                else if (jni.getBoardValue(j,i) == 4) {
                    g.setColor(Color.BLACK);
                    g.fillOval(x + (squareSizeX - squareSizeX+10) / 2,
                            y + (squareSizeY - squareSizeY+10) / 2,
                            squareSizeX-10,
                            squareSizeY-10);
                    g.setColor(Color.WHITE);
                    g.fillOval(x + (squareSizeX - squareSizeX+20) / 2,
                            y + (squareSizeY - squareSizeY+20) / 2,
                            squareSizeX-20,
                            squareSizeY-20);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

        int cellWidth = getWidth() / 8;
        int cellHeight = getHeight() / 8;

        int col = e.getX() / cellWidth;
        int row = e.getY() / cellHeight;

        if (!jni.isCellSelected()) {
            //First click
            if (jni.getBoardValue(row, col)!= 0 &&
               (((jni.getBoardValue(row, col) == 1 || jni.getBoardValue(row, col) == 3) && jni.getCurrentPlayer()) ||
               ((jni.getBoardValue(row, col) == 2 ||  jni.getBoardValue(row, col) == 4) && !jni.getCurrentPlayer()))) {

                jni.setFromRow(row);
                jni.setFromCol(col);
                jni.setSelectedCol(col);
                jni.setSelectedRow(row);
                jni.setCellSelected(true);  // Фиксируем, что фишка выбрана
            }
        } else {

            jni.setToCol(col);
            jni.setToRow(row);

            // Вызываем метод movePiece в C++ через JNI, передавая координаты
            boolean validMove = jni.movePiece(jni.getFromRow(), jni.getFromCol(), jni.getToRow(), jni.getToCol());

            if (validMove) {
                if (jni.gameFinished()) {
                    repaint();
                    gameOver();
                }
            }

            // Сброс выбранной фишки после хода
            jni.setCellSelected(false);
            jni.setSelectedCol(-1);
            jni.setSelectedRow(-1);
            jni.setToRow(-1);
            jni.setToCol(-1);
            jni.setFromRow(-1);
            jni.setFromCol(-1);
        }
        repaint();
    }

    public void mouseClicked(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}


    public void addColorListener(ColorListener listener) {colorlistener.add(listener);}


    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        int selectedRow = jni.getSelectedRow();
        int selectedCol = jni.getSelectedCol();

        // Инициализация начальных координат выбора
        if (keyCode == KeyEvent.VK_Q) {
            // Начинаем выбор с центральной клетки
            jni.setSelectedRow(3);
            jni.setSelectedCol(3);
            jni.setCellSelected(false);
            repaint();
            return; // Прерываем выполнение, чтобы избежать дальнейшей обработки
        }

        if (!jni.isCellSelected()) {
            // Если фишка не выбрана, перемещаем указатель для выбора фишки
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    if (selectedCol > 0) jni.setSelectedCol(selectedCol - 1);
                    break;
                case KeyEvent.VK_RIGHT:
                    if (selectedCol < 7) jni.setSelectedCol(selectedCol + 1);
                    break;
                case KeyEvent.VK_UP:
                    if (selectedRow > 0) jni.setSelectedRow(selectedRow - 1);
                    break;
                case KeyEvent.VK_DOWN:
                    if (selectedRow < 7) jni.setSelectedRow(selectedRow + 1);
                    break;
                case KeyEvent.VK_SPACE:
                    int boardValue = jni.getBoardValue(selectedRow, selectedCol);

                    // Проверка допустимости выбора фишки
                    if (boardValue != 0 &&
                        (((boardValue == 1 || boardValue == 3) && jni.getCurrentPlayer()) ||
                        ((boardValue == 2 || boardValue == 4) && !jni.getCurrentPlayer()))) {
                        jni.setCellSelected(true);
                        jni.setFromRow(selectedRow);
                        jni.setFromCol(selectedCol);
                    }

                    break;
            }

        } else {
            // Если фишка выбрана, перемещаем её
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    if (selectedCol > 0) jni.setSelectedCol(selectedCol - 1);
                    break;
                case KeyEvent.VK_RIGHT:
                    if (selectedCol < 7) jni.setSelectedCol(selectedCol + 1);
                    break;
                case KeyEvent.VK_UP:
                    if (selectedRow > 0) jni.setSelectedRow(selectedRow - 1);
                    break;
                case KeyEvent.VK_DOWN:
                    if (selectedRow < 7) jni.setSelectedRow(selectedRow + 1);
                    break;
                case KeyEvent.VK_SPACE:
                    // Подтверждение нового места перемещени

                    jni.setToRow(jni.getSelectedRow());
                    jni.setToCol(jni.getSelectedCol());

                    boolean validMove = jni.movePiece(jni.getFromRow(), jni.getFromCol(), jni.getToRow(), jni.getToCol());

                    if (validMove) {
                        if (jni.gameFinished()) {
                            repaint();
                            gameOver();
                        }
                    }


                    // Сброс выбора фишки
                    jni.setCellSelected(false);
                    break;
            }
        }
        repaint(); // Обновление отображения
    }

    public void gameOver(){
        String winner = jni.getCurrentPlayer() ? "White" : "Black";
        int response = JOptionPane.showConfirmDialog(
                null,
                winner + " won! Want to play again?",
                "GAME OVER",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (response == JOptionPane.OK_OPTION) {
            jni.resetGame();
        } else {
            System.exit(0);
        }
    }
}