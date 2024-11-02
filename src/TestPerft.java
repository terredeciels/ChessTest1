public class TestPerft {
    public static void main(String[] args) {
        ReducedChess game = new ReducedChess();
        game.initializeBoard();

        PerftChecker perftChecker = new PerftChecker(game);

        System.out.println("Résultats de PERFT jusqu'à une profondeur de 5 :");
        for (int depth = 1; depth <= 5; depth++) {
            long nodes = perftChecker.perft(depth);
            System.out.println("Profondeur " + depth + ": " + nodes + " positions");
        }
    }
}
