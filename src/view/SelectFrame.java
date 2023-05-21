package view;

import model.PlayMusic;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SelectFrame extends JFrame {
    public BeginFrame beginFrame;

    private final int WIDTH;
    private final int HEIGHT;

    public SelectFrame() {
        setTitle("难度选择");
        this.WIDTH = 400;
        this.HEIGHT = 350;

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);

        addBackButton();
        addEasyButton();
        addNormalButton();

        Image image = new ImageIcon("resource/beginFrame.jpg").getImage();
        image = image.getScaledInstance(400, 350,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel backGround = new JLabel(icon);
        backGround.setSize(400, 350);
        backGround.setLocation(0, 0);
        add(backGround);
    }

    private void addBackButton() {
        JButton button = new JButton("Back");
        button.addActionListener((e) -> {
            this.setVisible(false);
            beginFrame.setVisible(true);
        });
        button.setLocation(100, 30);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addEasyButton() {
        JButton button = new JButton("Easy");
        button.addActionListener((e) -> {

            beginFrame.chessGameFrame.getChessboardComponent().getGameController().reset();
            this.setVisible(false);
            beginFrame.chessGameFrame.timeLabel.setVisible(false);
            beginFrame.chessGameFrame.turnLabel.setLocation(770, 81);
            beginFrame.chessGameFrame.repaint();
            beginFrame.chessGameFrame.setVisible(true);
            beginFrame.chessGameFrame.getChessboardComponent().getGameController().isAIPlaying = 1;
            String musicPath = "resource/music/bgm.wav";
            PlayMusic.playMusic(musicPath, 0);
        });
        button.setLocation(100, 120);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addNormalButton() {
        JButton button = new JButton("Normal");
        button.addActionListener((e) -> {

            beginFrame.chessGameFrame.getChessboardComponent().getGameController().reset();
            this.setVisible(false);
            beginFrame.chessGameFrame.timeLabel.setVisible(false);
            beginFrame.chessGameFrame.turnLabel.setLocation(770, 81);
            beginFrame.chessGameFrame.repaint();
            beginFrame.chessGameFrame.setVisible(true);
            beginFrame.chessGameFrame.getChessboardComponent().getGameController().isAIPlaying = 2;
            String musicPath = "resource/music/bgm.wav";
            PlayMusic.playMusic(musicPath, 0);
        });
        button.setLocation(100, 210);
        button.setSize(200, 60);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }
}