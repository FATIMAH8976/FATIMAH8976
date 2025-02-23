import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToe extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton[] buttons = new JButton[9];
    private boolean playerTurn = true;
    private int playerScore = 0;
    private int computerScore = 0;
    private JLabel scoreLabel;

    public TicTacToe() {
        setLayout(new BorderLayout());

        // Panel for the game grid
        JPanel panel = new BackgroundPanel("/Users/fatimah/eclipse-workspace/TicTacToeGame/src/TicTacToeImages/WhatsApp Image 2025-02-22 at 18.29.18.jpeg");
        panel.setLayout(new GridLayout(3, 3));

        // Initialize buttons
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(new ButtonListener());
            panel.add(buttons[i]);
        }

        add(panel, BorderLayout.CENTER);

        // Score Label
        scoreLabel = new JLabel("Player Score: " + playerScore + " Computer Score: " + computerScore);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(scoreLabel, BorderLayout.NORTH);

        // Window settings
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    // Background panel class
    class BackgroundPanel extends JPanel {
        private ImageIcon background;

        public BackgroundPanel(String imagePath) {
            background = new ImageIcon(imagePath);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Handles player moves
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (playerTurn) {
                button.setText("X");
                button.setEnabled(false);
                playerTurn = false;
                checkWinner();

                // Delay computer's turn to make the game feel more interactive
                if (!isGameOver()) {
                    Timer timer = new Timer(500, new ActionListener() { // 500ms delay
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            computerTurn();
                        }
                    });
                    timer.setRepeats(false); // Only execute once
                    timer.start();
                }
            }
        }
    }

    // Handles computer moves
    private void computerTurn() {
        List<JButton> availableButtons = new ArrayList<>();
        for (JButton button : buttons) {
            if (button.getText().isEmpty()) {
                availableButtons.add(button);
            }
        }

        if (!availableButtons.isEmpty()) {
            Random random = new Random();
            JButton randomButton = availableButtons.get(random.nextInt(availableButtons.size()));
            randomButton.setText("O");
            randomButton.setEnabled(false);
            checkWinner();
            playerTurn = true;
        }
    }

    // Checks for a winner or a draw
    private void checkWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!buttons[i * 3].getText().isEmpty() &&
                    buttons[i * 3].getText().equals(buttons[i * 3 + 1].getText()) &&
                    buttons[i * 3].getText().equals(buttons[i * 3 + 2].getText())) {
                declareWinner(buttons[i * 3].getText());
                return;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!buttons[i].getText().isEmpty() &&
                    buttons[i].getText().equals(buttons[i + 3].getText()) &&
                    buttons[i].getText().equals(buttons[i + 6].getText())) {
                declareWinner(buttons[i].getText());
                return;
            }
        }

        // Check diagonals
        if (!buttons[0].getText().isEmpty() &&
                buttons[0].getText().equals(buttons[4].getText()) &&
                buttons[0].getText().equals(buttons[8].getText())) {
            declareWinner(buttons[0].getText());
            return;
        }

        if (!buttons[2].getText().isEmpty() &&
                buttons[2].getText().equals(buttons[4].getText()) &&
                buttons[2].getText().equals(buttons[6].getText())) {
            declareWinner(buttons[2].getText());
            return;
        }

        // Check for draw
        if (isGameOver()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            resetGame();
        }
    }

    // Checks if the board is full
    private boolean isGameOver() {
        for (JButton button : buttons) {
            if (button.getText().isEmpty()) {
                return false; // Game still ongoing
            }
        }
        return true; // All spots filled, game over
    }

    // Declares winner and updates score
    private void declareWinner(String winner) {
        if (winner.equals("You")) {
            playerScore++;
        } else {
            computerScore++;
        }

        scoreLabel.setText("Player Score: " + playerScore + " Computer Score: " + computerScore);
        JOptionPane.showMessageDialog(this, "Player " + winner + " wins!");
        resetGame();
    }

    // Resets the game board
    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
        }
        playerTurn = true;
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
