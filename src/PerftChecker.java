public class PerftChecker {
    private ReducedChess game;

    public PerftChecker(ReducedChess game) {
        this.game = game;
    }

    // Méthode principale pour calculer le nombre de mouvements légaux à une profondeur donnée
    public long perft(int depth) {
        if (depth == 0) {
            return 1;
        }

        long nodes = 0;

        // Générer et tester tous les mouvements des tours blanches
        nodes += generateRookMoves(game.rook1X, game.rook1Y, depth);
        nodes += generateRookMoves(game.rook2X, game.rook2Y, depth);

        // Générer et tester tous les mouvements du roi noir
        nodes += generateKingMoves(game.blackKingX, game.blackKingY, depth);

        return nodes;
    }

    // Générer les mouvements légaux pour une tour et effectuer une analyse PERFT
    private long generateRookMoves(int startX, int startY, int depth) {
        long nodes = 0;

        // Mouvement horizontal
        for (int y = 0; y < 8; y++) {
            if (y != startY && game.isRookMoveValid(startX, startY, startX, y)) {
                game.moveRook(startX, startY, startX, y);
                nodes += perft(depth - 1);
                game.undoMove(startX, startY, startX, y); // Restaurer la position
            }
        }

        // Mouvement vertical
        for (int x = 0; x < 8; x++) {
            if (x != startX && game.isRookMoveValid(startX, startY, x, startY)) {
                game.moveRook(startX, startY, x, startY);
                nodes += perft(depth - 1);
                game.undoMove(startX, startY, x, startY); // Restaurer la position
            }
        }

        return nodes;
    }

    // Générer les mouvements légaux pour le roi noir et effectuer une analyse PERFT
    private long generateKingMoves(int startX, int startY, int depth) {
        long nodes = 0;

        // Boucle sur les cases autour du roi pour générer les mouvements
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) { // Ne pas rester sur place
                    int newX = startX + dx;
                    int newY = startY + dy;
                    if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && game.isKingMoveValid(startX, startY, newX, newY)) {
                        game.moveKing(startX, startY, newX, newY);
                        nodes += perft(depth - 1);
                        game.undoMove(startX, startY, newX, newY); // Restaurer la position
                    }
                }
            }
        }

        return nodes;
    }
}
