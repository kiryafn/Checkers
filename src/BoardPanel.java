import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class BoardPanel extends JPanel implements MouseListener, KeyListener {

    int fromX = 0;
    int fromY = 0;
    static {
        System.load("/Users/alieksieiev/CLionProjects/Checkers/cmake-build-debug/libCheckers.dylib");
    }

    ColorListener colorlistener;
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

            colorlistener.colorChanged(new ColorChangedEvent(this, jni.getCurrentPlayer() ? Color.BLACK : Color.WHITE));
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

        if (!jni.isPieceSelected()) {
            //First click
            if (jni.getBoardValue(row, col)!= 0 &&
               (((jni.getBoardValue(row, col) == 1 || jni.getBoardValue(row, col) == 3) && jni.getCurrentPlayer()) ||
               ((jni.getBoardValue(row, col) == 2 ||  jni.getBoardValue(row, col) == 4) && !jni.getCurrentPlayer()))) {

                jni.setSelectedRow(row);
                jni.setSelectedCol(col);
                jni.setPieceSelected(true);  // Фиксируем, что фишка выбрана
                fromX = row;
                fromY = col;
            }
        } else {

            int toRow = row;
            int toCol = col;

            // Вызываем метод movePiece в C++ через JNI, передавая координаты
            boolean validMove = jni.movePiece(jni.getSelectedRow(), jni.getSelectedCol(), toRow, toCol);

            if (validMove) {
                if (jni.gameFinished()){
                    System.exit(0);
                }
            }

            // Сброс выбранной фишки после хода
            jni.setPieceSelected(false);
            jni.setSelectedRow(-1);
            jni.setSelectedCol(-1);

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


    public void addColorListener(ColorListener listener) {
        colorlistener = listener;
    }


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
            jni.setPieceSelected(false);
            repaint();
            return; // Прерываем выполнение, чтобы избежать дальнейшей обработки
        }

        if (!jni.isPieceSelected()) {
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
                    System.out.println("ПРОБЕЛ 1: Выбор фишки для перемещения");
                    int boardValue = jni.getBoardValue(selectedRow, selectedCol);

                    // Проверка допустимости выбора фишки
                    if (boardValue != 0 &&
                            (((boardValue == 1 || boardValue == 3) && jni.getCurrentPlayer()) ||
                                    ((boardValue == 2 || boardValue == 4) && !jni.getCurrentPlayer()))) {
                        jni.setPieceSelected(true);
                        fromX = selectedRow;
                        fromY = selectedCol;
                        System.out.println("Фишка выбрана на (" + fromX + ", " + fromY + ")");
                    } else {
                        System.out.println("Невозможно выбрать фишку на (" + fromX + ", " + fromY + ")");
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
                    // Подтверждение нового места перемещения
                    int toRow = jni.getSelectedRow();
                    int toCol = jni.getSelectedCol();

                    System.out.println("ПРОБЕЛ 2: Подтверждение перемещения");
                    System.out.println("FROM: (" + fromX + ", " + fromY + ")");
                    System.out.println("TO: (" + toRow + ", " + toCol + ")");

                    boolean validMove = jni.movePiece(fromX, fromY, toRow, toCol);

                    if (validMove) {
                        System.out.println("Успешное перемещение: (" + fromX + ", " + fromY + ") -> (" + toRow + ", " + toCol + ")");

                        // Проверка завершения игры
                        if (jni.gameFinished()) {
                            System.exit(0);
                        }

                        // Сброс выбора фишки
                        jni.setPieceSelected(false);
                        jni.setSelectedRow(-1);
                        jni.setSelectedCol(-1);
                    } else {
                        System.out.println("Невозможно выполнить перемещение");
                        jni.setPieceSelected(false);
                    }
                    break;
            }
        }

        // Вывод текущей позиции для отладки
        System.out.println("Текущая позиция1: (" + fromX + ", " + fromY + ")");
        System.out.println("Текущая позиция2: (" + jni.getSelectedRow() + ", " + jni.getSelectedCol() + ")");
        repaint(); // Обновление отображения
    }
}