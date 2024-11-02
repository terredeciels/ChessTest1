public class ReducedChess {
    static char[][] board = new char[8][8];
    static int whiteKingX = 7, whiteKingY = 4; // Position initiale du roi blanc
    static int blackKingX = 0, blackKingY = 4; // Position initiale du roi noir
    static int rook1X = 7, rook1Y = 0;         // Position initiale de la première tour blanche
    static int rook2X = 7, rook2Y = 7;         // Position initiale de la seconde tour blanche

    public static void main(String[] args) {
        initializeBoard();
        printBoard();

        // Simulation de tous les coups légaux pour les deux tours
        simulateAllMoves();
    }

    // Initialiser le plateau avec les pièces en place
    static void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = '.';
            }
        }
        board[whiteKingX][whiteKingY] = 'K';
        board[blackKingX][blackKingY] = 'k';
        board[rook1X][rook1Y] = 'R';
        board[rook2X][rook2Y] = 'R';
    }

    // Fonction pour afficher le plateau
    static void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < 8; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Simulation de tous les mouvements possibles pour les tours
    static void simulateAllMoves() {
        System.out.println("Simulation des coups légaux :");
        simulateRookMoves(rook1X, rook1Y);
        simulateRookMoves(rook2X, rook2Y);
    }

    // Simulation des coups pour une tour
    static void simulateRookMoves(int startX, int startY) {
        System.out.println("Coups légaux pour la tour en (" + (8 - startX) + ", " + (char) ('a' + startY) + "):");

        // Mouvements horizontaux
        for (int y = 0; y < 8; y++) {
            if (y != startY && isRookMoveValid(startX, startY, startX, y)) {
                System.out.print("Tour de (" + (8 - startX) + ", " + (char) ('a' + startY) + ") à (" + (8 - startX) + ", " + (char) ('a' + y) + ")");
                moveRook(startX, startY, startX, y);
                if (isKingInCheck(blackKingX, blackKingY)) {
                    System.out.println(" - Roi noir en échec !");
                } else {
                    System.out.println();
                }
                undoMove(startX, startY, startX, y); // Annule le mouvement pour restaurer la position initiale
            }
        }

        // Mouvements verticaux
        for (int x = 0; x < 8; x++) {
            if (x != startX && isRookMoveValid(startX, startY, x, startY)) {
                System.out.print("Tour de (" + (8 - startX) + ", " + (char) ('a' + startY) + ") à (" + (8 - x) + ", " + (char) ('a' + startY) + ")");
                moveRook(startX, startY, x, startY);
                if (isKingInCheck(blackKingX, blackKingY)) {
                    System.out.println(" - Roi noir en échec !");
                } else {
                    System.out.println();
                }
                undoMove(startX, startY, x, startY); // Annule le mouvement pour restaurer la position initiale
            }
        }
        System.out.println();
    }

    // Vérification du mouvement de la tour
    static boolean isRookMoveValid(int startX, int startY, int endX, int endY) {
        if (startX != endX && startY != endY) return false; // Le déplacement doit être en ligne droite
        if (board[endX][endY] == 'K' || board[endX][endY] == 'k') return false; // Ne pas capturer un roi
        return isPathClear(startX, startY, endX, endY);
    }

    // Déplacer la tour sans changer la position officielle pour simuler les coups
    static void moveRook(int startX, int startY, int endX, int endY) {
        board[startX][startY] = '.';
        board[endX][endY] = 'R';
    }

    // Annuler le mouvement de la tour
    static void undoMove(int startX, int startY, int endX, int endY) {
        board[endX][endY] = '.';
        board[startX][startY] = 'R';
    }

    // Vérifie si le roi noir est en échec par une tour blanche
    static boolean isKingInCheck(int kingX, int kingY) {
        // Vérifier les rangées et colonnes pour voir si une tour blanche menace le roi noir
        for (int i = 0; i < 8; i++) {
            // Vérifier la ligne du roi noir
            if (board[kingX][i] == 'R' && isPathClear(kingX, i, kingX, kingY)) {
                return true;
            }
            // Vérifier la colonne du roi noir
            if (board[i][kingY] == 'R' && isPathClear(i, kingY, kingX, kingY)) {
                return true;
            }
        }
        return false;
    }

    // Vérifie si le chemin entre deux cases est libre
    static boolean isPathClear(int startX, int startY, int endX, int endY) {
        if (startX == endX) { // Mouvement horizontal
            int minY = Math.min(startY, endY) + 1;
            int maxY = Math.max(startY, endY);
            for (int y = minY; y < maxY; y++) {
                if (board[startX][y] != '.') return false;
            }
        } else if (startY == endY) { // Mouvement vertical
            int minX = Math.min(startX, endX) + 1;
            int maxX = Math.max(startX, endX);
            for (int x = minX; x < maxX; x++) {
                if (board[x][startY] != '.') return false;
            }
        }
        return true;
    }

    public boolean isKingMoveValid(int startX, int startY, int endX, int endY) {
        if (Math.abs(startX - endX) <= 1 && Math.abs(startY - endY) <= 1) { // Déplacement d'une case autour
            char destination = board[endX][endY];
            if (destination == '.') { // La case doit être vide
                // Temporarily move king to check for check status
                int originalX = blackKingX, originalY = blackKingY;
                board[startX][startY] = '.';
                board[endX][endY] = 'k';
                blackKingX = endX;
                blackKingY = endY;
                boolean isInCheck = isKingInCheck(endX, endY);
                // Undo move
                board[startX][startY] = 'k';
                board[endX][endY] = destination;
                blackKingX = originalX;
                blackKingY = originalY;
                return !isInCheck; // Return true if the move does not place king in check
            }
        }
        return false;
    }

    public void moveKing(int startX, int startY, int endX, int endY) {
        board[startX][startY] = '.';
        board[endX][endY] = 'k';
        blackKingX = endX;
        blackKingY = endY;
    }

    // Vérifie si une position est dans les limites du plateau
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public boolean isCheckmate() {
        // Vérifie si le roi noir est en échec et ne peut pas échapper
        if (!isKingInCheck(blackKingX, blackKingY)) {
            return false;
        }

        // Essayer tous les mouvements légaux du roi noir pour voir s'il peut échapper à l'échec
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    int newX = blackKingX + dx;
                    int newY = blackKingY + dy;
                    if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && isKingMoveValid(blackKingX, blackKingY, newX, newY)) {
                        return false; // Il y a un mouvement légal pour sortir de l'échec
                    }
                }
            }
        }
        return true; // Le roi noir est en échec et ne peut pas sortir de l'échec : échec et mat
    }
}