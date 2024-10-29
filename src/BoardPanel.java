import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class BoardPanel extends JPanel implements MouseListener{
    static {
        System.load("/Users/alieksieiev/CLionProjects/Checkers/cmake-build-debug/libCheckers.dylib");
    }

    ColorListener colorlistener;
    CheckersJNI jni = new CheckersJNI();

    //int selectedRow = -1;
    //int selectedCol = -1;


    public BoardPanel() {
        setFocusable(true);
        addMouseListener(this);
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

        int TILE_SIZE_X = getWidth() / 8;
        int TILE_SIZE_Y = getHeight() / 8;

        int col = e.getX() / TILE_SIZE_X;
        int row = e.getY() / TILE_SIZE_Y;

        if (!jni.isPieceSelected()) {
            //First click
            if (jni.getBoardValue(row, col)!= 0 &&
               (((jni.getBoardValue(row, col) == 1 || jni.getBoardValue(row, col) == 3) && jni.getCurrentPlayer()) ||
               ((jni.getBoardValue(row, col) == 2 ||  jni.getBoardValue(row, col) == 4) && !jni.getCurrentPlayer()))) {

                jni.setSelectedRow(row);
                jni.setSelectedCol(col);
                jni.setPieceSelected(true);  // Фиксируем, что фишка выбрана
            }
        } else {

            int toRow = row;
            int toCol = col;

            // Вызываем метод movePiece в C++ через JNI, передавая координаты
            boolean validMove = jni.movePiece(jni.getSelectedRow(), jni.getSelectedCol(), toRow, toCol);

            // Сброс выбранной фишки после хода
            jni.setPieceSelected(false);

            jni.setSelectedRow(-1);
            jni.setSelectedCol(-1);

           // selectedRow = -1;
            //selectedCol = -1;
        }

        repaint();
    }

    public void mouseClicked(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


    public void addColorListener(ColorListener listener) {
        colorlistener = listener;
    }
}