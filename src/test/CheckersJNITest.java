package test;

import domain.CheckersJNI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckersJNITest {
    static {
        System.load("/Users/alieksieiev/CLionProjects/Checkers/cmake-build-debug/libCheckers.dylib");
    }

    private CheckersJNI jni;

    public void setCustomBoardState(int[][] boardState) {
        for (int row = 0; row < boardState.length; row++) {
            for (int col = 0; col < boardState[row].length; col++) {
                jni.setBoardValue(row, col, boardState[row][col]);
            }
        }
    }

    @BeforeEach
    public void setUp() {
        jni = new CheckersJNI();
    }

    @Test
    void mapInitializationTest() {//GWT given when then
        int size = jni.getBoardSize();

        for (int x=0; x<size; x++) {
            for (int y=0; y<size; y++) {
                if (x<3 && (x+y)%2 == 1) {
                    assertEquals(1, jni.getBoardValue(x,y));
                }
                else if (x>4 && (x+y)%2 == 1) {
                    assertEquals(2, jni.getBoardValue(x,y));
                }
                else assertEquals(0, jni.getBoardValue(x,y));
            }
        }
    }

    @Test
    void mapSizeTest() {//GWT given when then
        int size = jni.getBoardSize();
        assertEquals(8, size);
    }

    @Test
    void testWhiteValidMove() {
        boolean isValidMove = jni.movePiece(5, 0, 4, 1);

        assertAll(
                () ->  assertTrue(isValidMove, "Expected valid move from (5, 0) to (4, 1)"),
                () ->  assertEquals(0, jni.getBoardValue(5, 0), "Starting cell should now be empty"),
                () ->  assertEquals(2, jni.getBoardValue(4, 1), "Piece should be moved to (4, 1)")
        );
    }

    @Test
    void testBlackValidMove() {
        jni.setCurrentPlayer(true);
        boolean isValidMove = jni.movePiece(2, 1, 3, 2);

        assertAll(
                () -> assertTrue(isValidMove),
                () -> assertEquals(0, jni.getBoardValue(2, 1), "Starting cell should now be empty"),
                () -> assertEquals(1, jni.getBoardValue(3, 2), "Piece should be moved to (3, 2)")
        );
    }

    @Test
    void testWhiteInvalidMove() {
        boolean isValidMove = jni.movePiece(5, 0, 3, 2); // assuming this is an invalid move
        assertFalse(isValidMove);
    }

    @Test
    void testBlackInvalidMove() {
        boolean isValidMove = jni.movePiece(2, 1, 2, 3); // assuming this is an invalid move
        assertFalse(isValidMove);
    }


    @Test
    public void testWhiteCaptureBlack() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        setCustomBoardState(customState);

        // Выполняем захват черной фишки
        boolean validMove = jni.movePiece(3, 3, 1, 1);

        assertAll(
                () -> assertTrue(validMove),
                () -> assertEquals(2, jni.getBoardValue(1, 1)),
                () -> assertEquals(0, jni.getBoardValue(2, 2)),
                () -> assertEquals(0, jni.getBoardValue(3, 3))
        );
    }

    @Test
    public void testBlackCaptureWhite() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        setCustomBoardState(customState);
        jni.setCurrentPlayer(true);

        boolean validMove = jni.movePiece(3, 3, 1, 1);

        assertAll(
                () -> assertTrue(validMove),
                () -> assertEquals(1, jni.getBoardValue(1, 1)),
                () -> assertEquals(0, jni.getBoardValue(2, 2)),
                () -> assertEquals(0, jni.getBoardValue(3, 3))
        );
    }

    @Test
    public void testWhiteKingPromotion() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        setCustomBoardState(customState);

        boolean validMove = jni.movePiece(1, 1, 0, 0);

        assertAll(
                () -> assertTrue(validMove),
                () -> assertEquals(4, jni.getBoardValue(0, 0)),
                () -> assertEquals(0, jni.getBoardValue(1, 1))
        );
    }

    @Test
    public void testBlackKingPromotion() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        setCustomBoardState(customState);
        jni.setCurrentPlayer(true);

        boolean validMove = jni.movePiece(6, 1, 7, 0);

        assertAll(
                () -> assertTrue(validMove),
                () -> assertEquals(3, jni.getBoardValue(7, 0)),
                () -> assertEquals(0, jni.getBoardValue(6, 1))

        );
    }


    @Test
    public void testWhiteKingCapturesTwoPiecesInOneMove() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 0, 4, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0} // 4 - king, 1 - opponent checkers
        };
        setCustomBoardState(customState);

        boolean validMove = jni.movePiece(6, 0, 5, 1);

        assertAll(
                () -> assertTrue(validMove),
                () -> assertEquals(0, jni.getBoardValue(4, 1)),
                () -> assertEquals(0, jni.getBoardValue(1, 4))

        );
    }

    @Test
    public void testBlackKingCapturesTwoPiecesInOneMove() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 3, 0} // 3 - king, 2 - opponent pieces
        };
        setCustomBoardState(customState);
        jni.setCurrentPlayer(true);

        boolean validMove = jni.movePiece(7, 6, 1, 0);

        assertAll(
                () -> assertTrue(validMove),
                () -> assertEquals(0, jni.getBoardValue(6, 5)),
                () -> assertEquals(0, jni.getBoardValue(4, 3)),
                () -> assertEquals(0, jni.getBoardValue(2, 1)),
                () -> assertEquals(3, jni.getBoardValue(1, 0))
        );
    }

    @Test
    public void testBlackWin() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 2, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0} // 3 - king, 2 - opponent pieces
        };
        setCustomBoardState(customState);
        jni.setCurrentPlayer(true);

        boolean validMove = jni.movePiece(1, 1, 3, 3);

        assertAll(
                () -> assertTrue(validMove),
                () -> assertTrue(jni.gameFinished())
        );
    }

    @Test
    public void testWhiteWin() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 2, 0, 0},
                {0, 2, 0, 2, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {2, 0, 2, 0, 2, 0, 0, 0} // 3 - king, 2 - opponent pieces
        };
        setCustomBoardState(customState);
        boolean validMove = jni.movePiece(1, 1, 3, 3);

        assertAll(
                () -> assertTrue(validMove),
                () -> assertTrue(jni.gameFinished())
        );
    }

    @Test
    public void testBlackCaptureManyOpponentPieces() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 2, 0, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        setCustomBoardState(customState);
        jni.setCurrentPlayer(true);

        boolean validMove1 = jni.movePiece(5, 5, 3, 3);
        boolean validMove2 = jni.movePiece(3, 3, 5, 1);
        boolean validMove3 = jni.movePiece(5, 1, 7, 3);

        assertAll(
                () -> assertTrue(validMove1),
                () -> assertTrue(validMove2),
                () -> assertTrue(validMove3),
                () -> assertEquals(0, jni.getBoardValue(4, 4)),
                () -> assertEquals(0, jni.getBoardValue(4, 1)),
                () -> assertEquals(0, jni.getBoardValue(6, 2))
        );
    }

    @Test
    public void testWhiteCaptureManyOpponentPieces() {
        int[][] customState = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}
        };
        setCustomBoardState(customState);

        boolean validMove1 = jni.movePiece(5, 5, 3, 3);
        boolean validMove2 = jni.movePiece(3, 3, 5, 1);
        boolean validMove3 = jni.movePiece(5, 1, 7, 3);

        assertAll(
                () -> assertTrue(validMove1),
                () -> assertTrue(validMove2),
                () -> assertTrue(validMove3),
                () -> assertEquals(0, jni.getBoardValue(4, 4)),
                () -> assertEquals(0, jni.getBoardValue(4, 1)),
                () -> assertEquals(0, jni.getBoardValue(6, 2))
        );
    }

}