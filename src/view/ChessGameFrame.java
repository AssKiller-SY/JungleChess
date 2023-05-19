package view;

import controller.GameController;
import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 这个类表示游戏过程中的整个游戏界面，是一切的载体
 */
public class ChessGameFrame extends JFrame {
    //    public final Dimension FRAME_SIZE ;
    public BeginFrame beginFrame;
    private final int WIDTH;
    private final int HEIGTH;
    private final int ONE_CHESS_SIZE;
    public static Timer timer;

    JLabel turnLabel;
    JLabel timeLabel;

    private ChessboardComponent chessboardComponent;

    public ChessGameFrame(int width, int height) {
        setTitle("2023 CS109 Project Demo"); //设置标题
        this.WIDTH = width;
        this.HEIGTH = height;
        this.ONE_CHESS_SIZE = (HEIGTH * 4 / 5) / 9;

        setSize(WIDTH, HEIGTH);
        setLocationRelativeTo(null); // Center the window.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setLayout(null);


        addTurnLabel();
        addTimeLabel();
        addChessboard();
        addResetButton();
        addSaveButton();
        addLoadButton();
        addRegretButton();

        Image image = new ImageIcon("resource\\background.jpg").getImage();
        image = image.getScaledInstance(1100, 810,Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        JLabel backGround = new JLabel(icon);
        backGround.setSize(1100, 810);
        backGround.setLocation(0, 0);
        add(backGround);
    }


    public void setChessboardComponent(ChessboardComponent chessboardComponent) {
        this.chessboardComponent = chessboardComponent;
    }

    public ChessboardComponent getChessboardComponent() {
        return chessboardComponent;
    }
    /**
     * 在游戏面板中添加棋盘
     */
    private void addChessboard() {
        chessboardComponent = new ChessboardComponent(ONE_CHESS_SIZE , turnLabel , timeLabel);
        chessboardComponent.setLocation(HEIGTH / 5, HEIGTH / 10);
        add(chessboardComponent);
    }

    /**
     * 在游戏面板中添加标签
     */
    private void addTurnLabel() {
        turnLabel = new JLabel("Turn1: BLUE");
        turnLabel.setLocation(HEIGTH - 60 , HEIGTH / 10 );
        turnLabel.setSize(200, 60);
        turnLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(turnLabel);
    }

    public void addTimeLabel() {
        timeLabel = new JLabel("Time: 90");
        timeLabel.setLocation(HEIGHT +910, HEIGHT / 10 + 81);
        timeLabel.setSize(200, 60);
        timeLabel.setFont(new Font("Rockwell", Font.BOLD, 20));
        final int[] secondsLeft = {90};
        timeLabel.setText("Time: " + secondsLeft[0] + "s");
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsLeft[0]--;
                timeLabel.setText("Time: " + secondsLeft[0] + "s");
                if (secondsLeft[0] == 0) {
                    ((Timer) e.getSource()).stop();
                    timeLabel.setText("Time: " + secondsLeft[0] + "s");
                }
            };

        });
        timer.start();
        add(timeLabel);
    }
    /**
     * 在游戏面板中增加一个按钮，如果按下的话就会显示Hello, world!
     */
    private void addResetButton() {
        JButton button = new JButton("Reset");
        System.out.println("Reset the Chessboard");
        button.addActionListener((e) -> {
            chessboardComponent.getGameController().reset();
        });
        button.setLocation(HEIGHT + 800, HEIGHT / 10 + 174);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);
    }

    private void addSaveButton() {
        JButton button = new JButton("Save");
        button.setLocation(HEIGHT + 800, HEIGHT / 10 + 254);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {

            System.out.println("Save Game");
            String path = JOptionPane.showInputDialog("存档名");
            while (path.equals("") || path.endsWith(".txt")){
                if(path.equals(""))
                    JOptionPane.showMessageDialog(null, "存档名不能为空");
                if(path.endsWith(".txt"))
                    JOptionPane.showMessageDialog(null, "不需要加入后缀名");
                path = JOptionPane.showInputDialog("存档名");
            }
            chessboardComponent.getGameController().saveGame(path);
            new LoadFrame();
        });
    }

    private void addLoadButton() {
        JButton button = new JButton("Load");
        button.setLocation(HEIGHT + 800, HEIGHT / 10 + 334);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Load a Saved Game");
            boolean b = chessboardComponent.getGameController().loadGame();
            if (b) new LoadFrame();
        });
    }

    private void addRegretButton() {
        JButton button = new JButton("Regret");
        button.setLocation(HEIGHT + 800, HEIGHT / 10 + 414);
        button.setSize(180, 54);
        button.setFont(new Font("Rockwell", Font.BOLD, 20));
        add(button);

        button.addActionListener(e -> {
            System.out.println("Regret One Step");
            chessboardComponent.getGameController().regretOneStep();
        });
    }
}
