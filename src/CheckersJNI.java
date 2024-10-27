public class CheckersJNI {
    public native int[] getBoardState();
    public native int getBoardSize();
    public native boolean movePiece(int fromX, int fromY, int toX, int toY);
    public native boolean getCurrentPlayer();
}
