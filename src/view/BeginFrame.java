package view;

import controller.GameController;
import model.Chessboard;
import model.PlayMusic;
import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class BeginFrame extends JFrame {
    private final int WIDTH = 400;
    private final int Height = 300;
    ChessGameFrame chessGameFrame;
    SelectFrame selectFrame;
    public BeginFrame () {
        setTitle("舒飏和吉俊昌制作的Jungle Chess");

        setSize(WIDTH, Height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        ChessGameFrame gameFrame = new ChessGameFrame(1100, 810);
        GameController controller = new GameController(gameFrame.getChessboardComponent(), new Chessboard());
        this.chessGameFrame = gameFrame;
        gameFrame.beginFrame = this;
        this.selectFrame = new SelectFrame();
        selectFrame.beginFrame = this;

        addBeginButton();
        addAIButton();

        JLabel background = new JLabel();
        Image image = new ImageIcon("resource/beginFrame.jpg").getImage();
        image = image.getScaledInstance(400, 600,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        background.setIcon(icon);
        background.setBounds(0, 0, 400, 600);
        add(background);

    }

    private void addBeginButton() {
        JButton button = new JButton("开始");
        button.addActionListener((e) -> {
            this.setVisible(false);

            chessGameFrame.turnLabel.setLocation(770, 81);
            chessGameFrame.repaint();
            chessGameFrame.timeLabel.setVisible(true);
            chessGameFrame.getChessboardComponent().getGameController().reset();
            chessGameFrame.setVisible(true);
            String musicPath = "resource/music/bgm.wav";
            PlayMusic.playMusic(musicPath, 0);
        });
        button.setLocation(100, 50);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addAIButton() {
        JButton button = new JButton("人机对战");
        button.addActionListener((e) -> {
            this.setVisible(false);

            selectFrame.setVisible(true);
        });
        button.setLocation(100, 150);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
}
