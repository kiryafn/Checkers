public class CheckersJNI {

    //Board class on C++
    public native int[] getBoardState();
    public native int getBoardSize();
    public native boolean movePiece(int fromX, int fromY, int toX, int toY);
    public native int getBoardValue(int x, int y);
    public native boolean getCurrentPlayer();
    public native void setCurrentPlayer(boolean a);

    public native void setBoardValue(int x, int y, int val);
    //SelectedCell class on C++
    public native int getSelectedCol();
    public native int getSelectedRow();
    public native void setSelectedCol(int a);
    public native void setSelectedRow(int a);
    public native boolean isCellSelected();

    public native void setCellSelected(boolean a);
    public native void setFromRow(int a);
    public native void setFromCol(int a);
    public native void setToCol(int a);

    public native void setToRow(int a);
    public native int getFromRow();
    public native int getFromCol();
    public native int getToCol();

    public native int getToRow();

    public native boolean gameFinished();
    public native void resetGame();
}
