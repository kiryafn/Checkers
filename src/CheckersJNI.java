public class CheckersJNI {

    //Board class on C++
    public native int[] getBoardState();
    public native int getBoardSize();
    public native boolean movePiece(int fromX, int fromY, int toX, int toY);
    public native int getBoardValue(int x, int y);
    public native boolean getCurrentPlayer();

    //SelectedCell class on C++
    public native int getSelectedCol();
    public native int getSelectedRow();
    public native void setSelectedCol(int a);
    public native void setSelectedRow(int a);
    public native boolean isPieceSelected();
    public native void setPieceSelected(boolean a);


    public native boolean gameFinished();
}
