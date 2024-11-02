import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGUI extends JFrame {
    private JButton[][] squares = new JButton[8][8];
    private ReducedChess game;
    private boolean whiteTurn = true; // Alternance des tours
    private boolean selectingPiece = true;
    private int selectedX, selectedY;

    public ChessGUI(ReducedChess game) {
        this.game = game;
        setTitle("Échecs Réduits - Deux Joueurs");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        initializeBoard();
        updateBoard();
    }

    public static void main(String[] args) {
        ReducedChess game = new ReducedChess();
        game.initializeBoard();
        ChessGUI gui = new ChessGUI(game);
        gui.setVisible(true);
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.PLAIN, 20));
                button.setFocusPainted(false);
                button.setBackground((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);

                final int x = i;
                final int y = j;

                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        handleSquareClick(x, y);
                    }
                });

                squares[i][j] = button;
                add(button);
            }
        }
    }

    private void handleSquareClick(int x, int y) {
        char piece = game.board[x][y];

        // Sélection de la pièce selon le tour du joueur
        if (selectingPiece) {
            if (whiteTurn && piece == 'R') { // Tour du joueur blanc
                selectedX = x;
                selectedY = y;
                selectingPiece = false;
                squares[x][y].setBackground(Color.YELLOW); // Marquer la pièce sélectionnée
            } else if (!whiteTurn && piece == 'k') { // Tour du joueur noir
                selectedX = x;
                selectedY = y;
                selectingPiece = false;
                squares[x][y].setBackground(Color.YELLOW); // Marquer la pièce sélectionnée
            }
        } else {
            // Tentative de déplacement de la pièce
            if (whiteTurn && game.isRookMoveValid(selectedX, selectedY, x, y)) {
                game.moveRook(selectedX, selectedY, x, y);
                whiteTurn = false; // Passer au joueur noir
                selectingPiece = true;
                updateBoard();

                // Vérification de l'échec du roi noir
                if (game.isKingInCheck(game.blackKingX, game.blackKingY)) {
                    if (game.isCheckmate()) {
                        JOptionPane.showMessageDialog(this, "Échec et mat ! Le joueur blanc gagne.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Le roi noir est en échec !");
                    }
                }
            } else if (!whiteTurn && game.isKingMoveValid(selectedX, selectedY, x, y)) {
                game.moveKing(selectedX, selectedY, x, y);
                whiteTurn = true; // Passer au joueur blanc
                selectingPiece = true;
                updateBoard();
            } else {
                JOptionPane.showMessageDialog(this, "Déplacement invalide !");
                selectingPiece = true;
                updateBoard();
            }
        }
    }

    private void updateBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char piece = game.board[i][j];
                squares[i][j].setText(piece == '.' ? "" : String.valueOf(piece));
                squares[i][j].setBackground((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY);
            }
        }
    }
}
