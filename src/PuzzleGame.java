import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class PuzzleGame extends JFrame {

	private JButton[][] buttons = new JButton[4][4];
	private int[][] board = new int[4][4];
	private int emptyRow, emptyCol;
	private JLabel timerLabel;
	private JLabel jogadasLabel;
	private int jogadas = 0;
	private long startTime;
	private Timer gameTimer;

	public PuzzleGame() {
		setTitle("Quebra-cabeça 4x4");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 450);
		setLayout(new BorderLayout());

		initializeGame();
		setupUI();
		setVisible(true);
	}

	private void initializeGame() {
		int num = 1;
		jogadas = 0;
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				board[r][c] = num++;
			}
		}

		board[3][3] = 0;
		emptyRow = 3;
		emptyCol = 3;

		shuffleBoard();
	}

	private void shuffleBoard() {
		Random rand = new Random();

		for (int i = 0; i < 200; i++) {
			int direction = rand.nextInt(4);
			moveTitle(direction);
		}
	}

	private boolean moveTitle(int direction) {
		// 0 cima, 1 direita, 2 baixo, 3 esquerda
		int newRow = emptyRow,
				newCol = emptyCol;

		switch (direction) {
			case 0:
				newRow++;
				break;
			case 1:
				newCol--;
				break;
			case 2:
				newRow--;
				break;
			case 3:
				newCol++;
				break;
		}

		if (newRow >= 0 && newRow < 4 && newCol >= 0 && newCol < 4) {
			board[emptyRow][emptyCol] = board[newRow][newCol];
			board[newRow][newCol] = 0;
			emptyRow = newRow;
			emptyCol = newCol;
			return true;
		}
		return false;
	}

	private void setupUI() {
		JPanel boardPanel = new JPanel(new GridLayout(4, 4));

		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				buttons[r][c] = new JButton();

				if (board[r][c] != 0) {
					buttons[r][c].setText(String.valueOf(board[r][c]));
				} else {
					buttons[r][c].setText("");
					buttons[r][c].setEnabled(false);
				}

				final int row = r, col = c;
				buttons[r][c].addActionListener(e -> handleTileClick(row, col));
				boardPanel.add(buttons[r][c]);
			}
		}

		JPanel controlPanel = new JPanel();
		timerLabel = new JLabel("Tempo: 0s");
		jogadasLabel = new JLabel("Jogadas: " + jogadas);
		controlPanel.add(jogadasLabel);
		controlPanel.add(timerLabel);

		JButton restartButton = new JButton("Reiniciar");
		restartButton.addActionListener(e -> restartGame());
		controlPanel.add(restartButton);

		add(boardPanel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);

		startTimer();
	}

	private void handleTileClick(int row, int col) {
		if ((Math.abs(row - emptyRow) == 1 && col == emptyCol) ||
				(Math.abs(col - emptyCol) == 1 && row == emptyRow)) {

			board[emptyRow][emptyCol] = board[row][col];
			board[row][col] = 0;

			buttons[emptyRow][emptyCol].setText(buttons[row][col].getText());
			buttons[emptyRow][emptyCol].setEnabled(true);

			buttons[row][col].setText("");
			buttons[row][col].setEnabled(false);

			emptyRow = row;
			emptyCol = col;

			jogadas++;

			if (checkWin()) {
				gameTimer.stop();
				JOptionPane.showMessageDialog(this, "Parabéns! Você venceu em " +
						timerLabel.getText());
			}
		}
	}

	private boolean checkWin() {
		int num = 1;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				if (row == 3 && col == 3) {
					if (board[row][col] != 0)
						return false;
				} else {
					if (board[row][col] != num++)
						return false;
				}
			}
		}
		return true;
	}

	private void startTimer() {
		startTime = System.currentTimeMillis();
		gameTimer = new Timer(1000, e -> updateTimer());
		gameTimer.start();
	}

	private void updateTimer() {
		long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
		timerLabel.setText("Tempo: " + elapsedTime + "s");
		jogadasLabel.setText("Jogadas: " + jogadas);

		if (elapsedTime == 180) {
			gameTimer.stop();
			JOptionPane.showMessageDialog(this, "Tempo esgotado! Você perdeu!");
			restartGame();
		}
	}

	private void restartGame() {
		gameTimer.stop();
		initializeGame();
		updateBoard();
		startTimer();
	}

	private void updateBoard() {
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				if (board[row][col] != 0) {
					buttons[row][col].setText(String.valueOf(board[row][col]));
					buttons[row][col].setEnabled(true);
				} else {
					buttons[row][col].setText("");
					buttons[row][col].setEnabled(false);
				}
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PuzzleGame());
	}

}
