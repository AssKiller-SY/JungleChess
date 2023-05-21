package controller;


import listener.GameListener;
import model.*;
import view.Animal.*;
import view.CellComponent;
import view.ChessGameFrame;
import view.ChessboardComponent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Controller is the connection between model and view,
 * when a Controller receive a request from a view, the Controller
 * analyzes and then hands over to the model for processing
 * [in this demo the request methods are onPlayerClickCell() and onPlayerClickChessPiece()]
 */
class AImove {
    ChessboardPoint src, dest;
    int value;
    public AImove(ChessboardPoint src, ChessboardPoint dest, int value) {
        this.src = src;
        this.dest = dest;
        this.value = value;
    }
}
public class GameController implements GameListener {

    private static int turn = 1;
    private final int[] dir_x = {1, 0, 0, -1, 4, -4, 0, 0};
    private final int[] dir_y = {0, 1, -1, 0, 0, 0, 3, -3};
    public ChessboardComponent view;
    private Chessboard model;
    private PlayerColor winner;
    private ArrayList<AImove> AImoves;
    public PlayerColor getCurrentPlayer() {
        return currentPlayer;
    }

    private PlayerColor currentPlayer;
    private ArrayList<ChessboardPoint> canReachPoint;
    // Record whether there is a selected piece before
    private ChessboardPoint selectedPoint;
    public int isAIPlaying = 0;

    public GameController(ChessboardComponent view, Chessboard model) {
        this.view = view;
        this.model = model;
        this.currentPlayer = PlayerColor.BLUE;
        view.registerController(this);
        view.initiateChessComponent(model);
        view.repaint();
    }

    private static String beginLetterAnimal(ChessPiece chess) {
        if (chess == null) return "*";
        else if (chess.getName().equals("Elephant")) return "E";
        else if (chess.getName().equals("Lion")) return "L";
        else if (chess.getName().equals("Tiger")) return "T";
        else if (chess.getName().equals("Leopard")) return "l";
        else if (chess.getName().equals("Wolf")) return "W";
        else if (chess.getName().equals("Dog")) return "D";
        else if (chess.getName().equals("Cat")) return "C";
        else if (chess.getName().equals("Rat")) return "R";
        else return "";
    }

    private boolean checkName(char c) {
        return c == 'E' || c == 'L' || c == 'T' || c == 'l' || c == 'W' || c == 'D' || c == 'C' || c == 'R' || c == '*';
    }



    public void cancelLighten() {
        canReachPoint = null;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                view.gridComponents[i][j].setCanReach(false);
            }
        }
    }

    private void announceWinner() {
        if (winner == PlayerColor.BLUE) {
            JOptionPane.showMessageDialog(view, "BLUE Win !");
        } else {
            JOptionPane.showMessageDialog(view, "RED Win !");
        }
    }


    // after a valid move swap the player
    private void swapColor() {

        if(currentPlayer == PlayerColor.BLUE)
            currentPlayer = PlayerColor.RED;
        else
            currentPlayer = PlayerColor.BLUE;

        if (currentPlayer == PlayerColor.BLUE)
            view.turnLabel.setText("Turn " + (++turn) + ": BLUE");
        else
            view.turnLabel.setText("Turn " + turn + ": RED");
    }

    private boolean checkWin() {
        if (model.getGrid()[0][3].getPiece() != null || model.getRedDead().size() == 8) {
            this.winner = PlayerColor.BLUE;
        }
        if (model.getGrid()[8][3].getPiece() != null || model.getBlueDead().size() == 8) {
            this.winner = PlayerColor.RED;
        }
        return false;
    }

    private void twiceSelect(CellComponent component) {
        selectedPoint = null;
        cancelLighten();
        view.repaint();
        component.revalidate();
    }

    //点击非棋子的格子
    @Override
    public void onPlayerClickCell(ChessboardPoint point, CellComponent component) {
        if (selectedPoint != null && model.isValidMove(selectedPoint, point)) {
            model.moveChessPiece(selectedPoint, point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));
            twiceSelect(component);
            swapColor();
            settimeLable90sCountDown();
            checkWin();
            if (winner != null) {
                announceWinner();
                reset();
            }

            if(isAIPlaying == 1) {
                //System.out.println("test");
                AIEasy();
            }
            if(isAIPlaying == 2) {
                AINormal();
            }
        }
    }

    private void AIEasy() {
        AImoves = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (model.getGrid()[i][j].getPiece() != null && model.getGrid()[i][j].getPiece().getOwner() == currentPlayer) {
                            ChessboardPoint src = new ChessboardPoint(i,j);
                            canReachPoint = getCanReachPoint(src);
                            if(canReachPoint.size() == 0) continue;
                            for (ChessboardPoint dest : canReachPoint) {
                                AImoves.add(new AImove(src,dest,0));
                            }
                        }
                    }
                }
                Random random = new Random();
                int randomMove = random.nextInt(AImoves.size());
                ChessboardPoint src = AImoves.get(randomMove).src;
                ChessboardPoint dest = AImoves.get(randomMove).dest;
                if(model.isValidMove(src,dest)) {
                    model.moveChessPiece(src, dest);
                    view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                } else {
                    model.captureChessPiece(src, dest);
                    view.removeChessComponentAtGrid(dest);
                    view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                }
                canReachPoint = null;
                cancelLighten();
                swapColor();
                view.repaint();
                view.gridComponents[dest.getRow()][dest.getCol()].revalidate();
                checkWin();
                if (winner != null){
                    announceWinner();
                    reset();
                }
            }
        });
        thread.start();
    }

    private void AINormal() {
        //System.out.println(turn);
        AImoves = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ChessboardPoint dens = new ChessboardPoint(8,3);
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (model.getGrid()[i][j].getPiece() != null
                                && model.getGrid()[i][j].getPiece().getOwner() == currentPlayer) {
                            ChessboardPoint src = new ChessboardPoint(i,j);
                            canReachPoint = getCanReachPoint(src);
                            if(canReachPoint.size() == 0) continue;
                            for (ChessboardPoint dest : canReachPoint) {
                                int value = 0;

                                for (int k = 0; k < 9; k++) {
                                    for (int l = 0; l < 7; l++) {
                                        if (model.getGrid()[k][l].getPiece() != null
                                                && model.getGrid()[k][l].getPiece().getOwner() != currentPlayer){
                                            if(model.isValidCapture(new ChessboardPoint(k,l),src) &&
                                                    !model.isValidCapture(new ChessboardPoint(k,l),dest))
                                                value += 19*model.getGrid()[src.getRow()][src.getCol()].getPiece().getRank();
                                        }
                                    }
                                }

                                if(model.isValidMove(src,dest)) {

                                    if(model.calculateDistance(src,dens) > model.calculateDistance(dest,dens)) {
                                        value += 17 - model.calculateDistance(dest,dens);
                                    } else {
                                        value -= 17 - model.calculateDistance(dest,dens);
                                    }
                                    model.moveChessPiece(src,dest);
                                    ChessPiece AIchess = model.getGrid()[dest.getRow()][dest.getCol()].getPiece();
                                    for (int k = 0; k < 8; k++) {
                                        int xs = dest.getRow();
                                        int ys = dest.getCol();
                                        int xe = xs+dir_x[k];
                                        int ye = ys+dir_y[k];

                                        if(xe<0||ye<0||xe>8||ye>6) continue;

                                        if(model.getGrid()[xe][ye].getPiece() != null
                                                && model.getGrid()[xe][ye].getPiece().getOwner() != currentPlayer) {
                                            ChessPiece chessPiece = model.getGrid()[xe][ye].getPiece();

                                            if(model.isValidCapture(new ChessboardPoint(xe,ye),dest)) {
                                                value -= AIchess.getRank()*17;
                                                //System.out.println(AIchess.getName()+" "+chessPiece.getName());
                                            }
                                        }
                                    }
                                    model.moveChessPiece(dest,src);
                                } else {
                                    value += 16*model.getGrid()[dest.getRow()][dest.getCol()].getPiece().getRank();
                                }
                                AImoves.add(new AImove(src,dest,value));
                            }
                        }
                    }
                }
                Collections.sort(AImoves, new Comparator<AImove>() {
                    @Override
                    public int compare(AImove o1, AImove o2) {
                        return o2.value - o1.value;
                    }
                });

                ChessboardPoint src = AImoves.get(0).src;
                ChessboardPoint dest = AImoves.get(0).dest;
                if(model.isValidMove(src,dest)) {
                    model.moveChessPiece(src, dest);
                    view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                } else {
                    model.captureChessPiece(src, dest);
                    view.removeChessComponentAtGrid(dest);
                    view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                }
                canReachPoint = null;
                cancelLighten();
                swapColor();
                view.repaint();
                view.gridComponents[dest.getRow()][dest.getCol()].revalidate();
                checkWin();
                if (winner != null){
                    announceWinner();
                    reset();
                }
            }
        });
        thread.start();
    }
    //点击有棋子的格子
    @Override
    public void onPlayerClickChessPiece(ChessboardPoint point, AnimalChessComponent component) {
        //第一次选了个棋子
        if (selectedPoint == null) {
            if (model.getChessPieceOwner(point).equals(currentPlayer)) {
                canReachPoint = getCanReachPoint(point);
                selectedPoint = point;
                component.setSelected(true);
                component.revalidate();
                component.repaint();
                view.repaint();
                view.revalidate();
            }
        }
        //连续选了同一个棋子
        else if (selectedPoint.equals(point)) {
            selectedPoint = null;
            cancelLighten();
            component.setSelected(false);
            component.repaint();
            component.revalidate();
            view.repaint();
            view.revalidate();
        } else if (model.isValidCapture(selectedPoint, point)) {
            model.captureChessPiece(selectedPoint, point);
            view.removeChessComponentAtGrid(point);
            view.setChessComponentAtGrid(point, view.removeChessComponentAtGrid(selectedPoint));

            selectedPoint = null;
            cancelLighten();
            swapColor();

            view.repaint();
            view.revalidate();
            component.revalidate();

            settimeLable90sCountDown();

            checkWin();
            if (winner != null) {
                announceWinner();
                reset();
            }

            if(isAIPlaying == 1) {
                AIEasy();
            }
            if(isAIPlaying == 2) {
                AINormal();
            }
            // TODO: Implement capture function
        }
    }

    //找出所有能走的点
    public ArrayList<ChessboardPoint> getCanReachPoint(ChessboardPoint src) {
        ArrayList<ChessboardPoint> list = new ArrayList<>();
        int xs = src.getRow();
        int ys = src.getCol();
        for (int i = 0; i < 8; i++) {
            int xe = xs + dir_x[i];
            int ye = ys + dir_y[i];
            if (xe < 0 || ye < 0 || xe > 8 || ye > 6) continue;
            //System.out.println("testCanReach:"+xs+" "+ys+" "+xe+" "+ye);
            ChessboardPoint dest = new ChessboardPoint(xe, ye);
            if (model.isValidMove(src, dest) || model.isValidCapture(src, dest)) {
                view.gridComponents[xe][ye].setCanReach(true);
                list.add(dest);
            }
        }
        //System.out.println("test1end");
        return list;
    }

    public void reset() {
        model.initGrid();
        model.initPieces();
        model.steps = new ArrayList<>();
        model.setBlueDead(new ArrayList<>());
        model.setRedDead(new ArrayList<>());

        view.removeChessComponent();
        view.initiateChessComponent(model);
        view.turnLabel.setText("Turn 1: BLUE");

        settimeLable90sCountDown();

        currentPlayer = PlayerColor.BLUE;
        selectedPoint = null;
        cancelLighten();

        view.repaint();
        view.revalidate();
        winner = null;

    }

    public void settimeLable90sCountDown() {
        final int[] secondsLeft = {90};
        view.timeLabel.setText("Time: " + secondsLeft[0] + "s");
        ChessGameFrame.timer.stop();
        ChessGameFrame.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsLeft[0]--;
                view.timeLabel.setText("Time: " + secondsLeft[0] + "s");
                if (secondsLeft[0] == 0) {
                    ((Timer) e.getSource()).stop();
                    view.timeLabel.setText("Time: " + secondsLeft[0] + "s");
                }
            }
        });
        ChessGameFrame.timer.start();
    }

    public void saveGame(String fileName) {

        String filepath = "Save\\" + fileName + ".txt";
        File file = new File(filepath);

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filepath);
            fileWriter.write(model.steps.size() + "\n");

            for (Step step : model.steps) {
                fileWriter.write(step.toString() + "\n");
            }

            if (currentPlayer == PlayerColor.BLUE) {
                fileWriter.write("b");
            } else {
                fileWriter.write("r");
            }

            fileWriter.write("\n");

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    ChessPiece chess = model.getGrid()[i][j].getPiece();
                    fileWriter.write(beginLetterAnimal(chess) + " ");
                }
                fileWriter.write("\n");
            }

            System.out.println("存档已保存");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean loadGame() {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("Save"));
        chooser.showOpenDialog(view);
        File file = chooser.getSelectedFile();

        if (!file.getName().endsWith(".txt")) {
            JOptionPane.showMessageDialog(null, "文件后缀名错误\n继续当前游戏",
                    "存档不合法", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            ArrayList<String> StepInfo = new ArrayList<>();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);

            //System.out.println("test1");

            while(true) {
                String stepInfo = reader.readLine();
                if( stepInfo == null || stepInfo.equals("")) break;
                StepInfo.add(stepInfo);
                //System.out.println("test:"+stepInfo);
            }

            //System.out.println("test2");

            int StepCount = Integer.parseInt(StepInfo.get(0));
            //System.out.println("test3");
            for (int i = 1; i <= StepCount+1; i++) {
                String stepInfo = StepInfo.get(i);
                if ((i & 1) == 0 && stepInfo.charAt(0) == 'b') {
                    JOptionPane.showMessageDialog(null, "当前玩家回合错误\n继续游戏",
                            "存档不合法", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if ((i & 1) == 1 && stepInfo.charAt(0) == 'r') {
                    JOptionPane.showMessageDialog(null, "当前玩家回合错误\n继续游戏",
                            "存档不合法", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            try {

                for (int i = StepCount + 2; i < StepCount + 11; i++) {
                    String stepInfo = StepInfo.get(i);
                    if (stepInfo.length() != 14) {
                        JOptionPane.showMessageDialog(null, "棋盘列数错误\n继续游戏",
                                "存档不合法", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    for (int j = 0; j < stepInfo.length(); j++) {
                        if (!checkName(stepInfo.charAt(j))) {
                            if(((j & 1) == 1 && stepInfo.charAt(j) == ' ')) continue;
                            JOptionPane.showMessageDialog(null, "棋子名称错误\n继续游戏",
                                    "存档不合法", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "棋盘行数错误\n继续游戏",
                        "存档不合法", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            reset();
            for (int i = 1; i < StepCount+1; i++) {
                String stepInfo = StepInfo.get(i);
                int xs = Integer.parseInt(""+stepInfo.charAt(8));
                int ys = Integer.parseInt(""+stepInfo.charAt(10));
                int xe = Integer.parseInt(""+stepInfo.charAt(17));
                int ye = Integer.parseInt(""+stepInfo.charAt(19));

                ChessboardPoint src = new ChessboardPoint(xs, ys);
                ChessboardPoint dest = new ChessboardPoint(xe, ye);

                boolean isMove = stepInfo.charAt(23) == 'n';
                //System.out.println("testStepCharAt "+step.charAt(14)+" "+isCapture);

                if (isMove) {
                    if (!model.isValidMove(src, dest)) {
                        JOptionPane.showMessageDialog(null, "移动不符合规则\n重新开始",
                                "存档不合法", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    model.moveChessPiece(src, dest);
                    view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                    selectedPoint = null;
                    swapColor();
                    view.repaint();

                } else {
                    if (!model.isValidCapture(src, dest)) {
                        JOptionPane.showMessageDialog(null, "攻击不符合规则\n重新开始",
                                "存档不合法", JOptionPane.ERROR_MESSAGE);
                        reset();
                        return false;
                    }
                    model.captureChessPiece(src, dest);
                    view.removeChessComponentAtGrid(dest);
                    view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                    swapColor();
                    view.repaint();
                    view.revalidate();
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "无此存档\n已重新开始",
                    "错误", JOptionPane.ERROR_MESSAGE);
            reset();
        }
        return true;
    }

    public void regretOneStep() {
        try {
            if (model.steps.size() == 0) {
                JOptionPane.showMessageDialog(null, "没有上一步",
                        "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Step lastStep = model.steps.get(model.steps.size() - 1);

            model.steps.remove(model.steps.size() - 1);
            ChessboardPoint src = lastStep.dest;
            ChessboardPoint dest = lastStep.src;

            if (lastStep.eaten == null) {
                model.moveChessPiece(src, dest);
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                selectedPoint = null;
                swapColor();
                view.repaint();
                settimeLable90sCountDown();
                if(lastStep.color == PlayerColor.BLUE){
                    turn-=2;
                    view.turnLabel.setText("Turn " + turn + ": BLUE");
                }
            } else {
                model.moveChessPiece(src, dest);
                view.setChessComponentAtGrid(dest, view.removeChessComponentAtGrid(src));
                AnimalChessComponent chessComponent = new AnimalChessComponent();
                ChessPiece chessPiece = lastStep.eaten;
                int CHESS_SIZE = 72;
                switch (lastStep.eaten.getName()) {
                    case "Elephant":
                        chessComponent = new ElephantChessComponent(chessPiece.getOwner(), CHESS_SIZE);
                        break;
                    case "Lion":
                        chessComponent = new LionChessComponent(chessPiece.getOwner(), CHESS_SIZE);
                        break;
                    case "Tiger":
                        chessComponent = new TigerChessComponent(chessPiece.getOwner(), CHESS_SIZE);
                        break;
                    case "Leopard":
                        chessComponent = new LeopardChessComponent(chessPiece.getOwner(), CHESS_SIZE);
                        break;
                    case "Wolf":
                        chessComponent = new WolfChessComponent(chessPiece.getOwner(), CHESS_SIZE);
                        break;
                    case "Dog":
                        chessComponent = new DogChessComponent(chessPiece.getOwner(), CHESS_SIZE);
                        break;
                    case "Cat":
                        chessComponent = new CatChessComponent(chessPiece.getOwner(), CHESS_SIZE);
                        break;
                    case "Rat":
                        chessComponent = new RatChessComponent(chessPiece.getOwner(), CHESS_SIZE);
                        break;
                }
                view.setChessComponentAtGrid(src, chessComponent);
                model.setChessPiece(src, lastStep.eaten);
                swapColor();
                if(lastStep.color == PlayerColor.BLUE){
                    turn-=2;
                    view.turnLabel.setText("Turn " + turn + ": BLUE");
                }
                view.repaint();
                view.revalidate();
            }
            model.steps.remove(model.steps.size() - 1);
        } catch (Exception ex) {}
    }

    public void playback() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Step> stepsCopy = model.steps;
                reset();
                for (Step step : stepsCopy) {
                    try {
                        Thread.sleep(1000);
                        //System.out.println("TESTplayback");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(step);
                    if(step.eaten == null) {
                        model.moveChessPiece(step.src, step.dest);
                        view.setChessComponentAtGrid(step.dest, view.removeChessComponentAtGrid(step.src));
                        view.repaint();
                        swapColor();
                    } else {
                        model.captureChessPiece(step.src, step.dest);
                        view.removeChessComponentAtGrid(step.dest);
                        view.setChessComponentAtGrid(step.dest, view.removeChessComponentAtGrid(step.src));
                        view.repaint();
                        view.revalidate();
                        swapColor();
                    }
                }
            }
        });
        thread.start();
    }
}
