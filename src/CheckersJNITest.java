import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckersJNITest {
    static {
        System.load("/Users/alieksieiev/CLionProjects/Checkers/cmake-build-debug/libCheckers.dylib");
    }

    private CheckersJNI jni;

    @BeforeEach
    public void setUp() {
        jni = new CheckersJNI();
    }

    @Test
    void mapInitializationTest() {
        int size = jni.getBoardSize();
        assertEquals(8, size);

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
    void testValidMove() {
        // Test a simple valid move for Player 2
        boolean isValidMove = jni.movePiece(5, 0, 4, 1); // assuming movePiece() handles move logic
        assertTrue(isValidMove, "Expected valid move from (5, 0) to (4, 1)");

        // Verify that piece was moved
        assertEquals(0, jni.getBoardValue(5, 0), "Starting cell should now be empty");
        assertEquals(2, jni.getBoardValue(4, 1), "Piece should be moved to (4, 1)");
    }

    @Test
    void testInvalidMove() {
        boolean isValidMove = jni.movePiece(5, 0, 3, 2); // assuming this is an invalid move
        assertFalse(isValidMove, "Expected invalid move from (5, 0) to (3, 2)");
    }

    @Test
    void testCaptureMove() {
        // Set up a scenario where Player 1 can capture Player 2
        jni.movePiece(5, 4, 4, 3);
        jni.movePiece(2, 1, 3, 2);
        boolean isNotCaptureMove = jni.movePiece(4, 3, 3, 5);
        assertFalse(isNotCaptureMove, "Expected invalid move from (4, 3) to (3, 5)");
        boolean isCaptureMove = jni.movePiece(4, 3, 2, 1);
        assertTrue(isCaptureMove, "Expected capture move from (4, 3) to (2, 1)");
    }

    @Test
    void testKingPromotion() {
        jni.setBoardValue(1,0,2);
        jni.setBoardValue(0,1,0);
        jni.setBoardValue(2,1,0);
        jni.movePiece(1, 0, 0, 1); // assuming this moves the piece to the promotion row

        // Check if piece is promoted to king
       assertEquals(4, jni.getBoardValue(0, 1), "Piece should be promoted to king at (0, 1)");
    }
}