package model;

import view.*;
import view.Animal.*;
import controller.GameController;

import java.util.ArrayList;
import static model.Constant.CHESSBOARD_COL_SIZE;
import static model.Constant.CHESSBOARD_ROW_SIZE;

/**
 * This class store the real chess information.
 * The Chessboard has 9*7 cells, and each cell has a position for chess
 */
public class Chessboard {
    private Cell[][] grid;
    public ArrayList<Step> steps;
    public void setBlueDead(ArrayList<ChessPiece> blueDead) {
        this.blueDead = blueDead;
    }

    public void setRedDead(ArrayList<ChessPiece> redDead) {
        this.redDead = redDead;
    }

    public ArrayList<ChessPiece> getBlueDead() {
        return blueDead;
    }

    public ArrayList<ChessPiece> getRedDead() {
        return redDead;
    }

    private ArrayList<ChessPiece> blueDead;
    private ArrayList<ChessPiece> redDead;

    public Chessboard() {
        this.grid =
                new Cell[Constant.CHESSBOARD_ROW_SIZE.getNum()][Constant.CHESSBOARD_COL_SIZE.getNum()];//19X19

        blueDead = new ArrayList<>();
        redDead = new ArrayList<>();
        steps = new ArrayList<>();

        initGrid();
        initPieces();
    }

    public void initGrid() {
        for (int i = 0; i < Constant.CHESSBOARD_ROW_SIZE.getNum(); i++) {
            for (int j = 0; j < Constant.CHESSBOARD_COL_SIZE.getNum(); j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public void initPieces() {
        grid[6][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Elephant", 8));
        grid[2][6].setPiece(new ChessPiece(PlayerColor.RED, "Elephant", 8));
        grid[8][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Lion", 7));
        grid[0][0].setPiece(new ChessPiece(PlayerColor.RED, "Lion", 7));
        grid[8][0].setPiece(new ChessPiece(PlayerColor.BLUE, "Tiger", 6));
        grid[0][6].setPiece(new ChessPiece(PlayerColor.RED, "Tiger", 6));
        grid[6][4].setPiece(new ChessPiece(PlayerColor.BLUE, "Leopard", 5));
        grid[2][2].setPiece(new ChessPiece(PlayerColor.RED, "Leopard", 5));
        grid[6][2].setPiece(new ChessPiece(PlayerColor.BLUE, "Wolf", 4));
        grid[2][4].setPiece(new ChessPiece(PlayerColor.RED, "Wolf", 4));
        grid[7][5].setPiece(new ChessPiece(PlayerColor.BLUE, "Dog", 3));
        grid[1][1].setPiece(new ChessPiece(PlayerColor.RED, "Dog", 3));
        grid[7][1].setPiece(new ChessPiece(PlayerColor.BLUE, "Cat", 2));
        grid[1][5].setPiece(new ChessPiece(PlayerColor.RED, "Cat", 2));
        grid[6][6].setPiece(new ChessPiece(PlayerColor.BLUE, "Rat", 1));
        grid[2][0].setPiece(new ChessPiece(PlayerColor.RED, "Rat", 1));
    }


    private ChessPiece getChessPieceAt(ChessboardPoint point) {
        return getGridAt(point).getPiece();
    }

    private Cell getGridAt(ChessboardPoint point) {
        return grid[point.getRow()][point.getCol()];
    }

    private int calculateDistance(ChessboardPoint src, ChessboardPoint dest) {
        return Math.abs(src.getRow() - dest.getRow()) + Math.abs(src.getCol() - dest.getCol());
    }

    private ChessPiece removeChessPiece(ChessboardPoint point) {
        ChessPiece chessPiece = getChessPieceAt(point);
        getGridAt(point).removePiece();
        return chessPiece;
    }

    public void setChessPiece(ChessboardPoint point, ChessPiece chessPiece) {
        getGridAt(point).setPiece(chessPiece);
    }

    public void moveChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidMove(src, dest)) {
            throw new IllegalArgumentException("Illegal chess move!");
        }
        ChessPiece chessPiece = removeChessPiece(src);
        setChessPiece(dest, chessPiece);
        steps.add(new Step(src, dest, chessPiece.getOwner()));
    }

    public void captureChessPiece(ChessboardPoint src, ChessboardPoint dest) {
        if (!isValidCapture(src, dest)) {
            throw new IllegalArgumentException("Illegal chess capture!");
        }
        ChessPiece attacker = removeChessPiece(src);
        ChessPiece defender = removeChessPiece(dest);
        setChessPiece(dest, attacker);
        if (attacker.getOwner() == PlayerColor.BLUE){
            System.out.println(String.format("Blue %s eat Red %s",attacker.getName(),defender.getName()));
            redDead.add(defender);
        } else {
            System.out.println(String.format("Red %s eat Blue %s",attacker.getName(),defender.getName()));
            blueDead.add(defender);
        }
        steps.add(new Step(src, dest, attacker.getOwner(), defender));
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public PlayerColor getChessPieceOwner(ChessboardPoint point) {
        return getGridAt(point).getPiece().getOwner();
    }

    public boolean isValidMove(ChessboardPoint src, ChessboardPoint dest) {

        //起点
        ChessPiece st = getChessPieceAt(src);
        int xs = src.getRow(), ys = src.getCol();

        //终点
        ChessPiece ed = getChessPieceAt(dest);
        int xe = dest.getRow(), ye = dest.getCol();

        //判断起点终点有无棋子
        if (st == null || ed != null) {return false;}
        //判断是否走入自己的巢穴
        if(st.getOwner() == PlayerColor.BLUE) {
            //System.out.println("test"+st.getName()+" "+st.getOwner()+" "+xe+" "+ye);
            if(xe == 8 && ye == 3) return false;
        }
        if(st.getOwner() == PlayerColor.RED) {
            if(xe == 0 && ye == 3) return false;
        }

        boolean legalDistance = calculateDistance(src, dest)==1;
        if (st.getName().equals("Elephant")){
            return  legalDistance && !isRiver(dest);
        }
        if (st.getName().equals("Lion")){
            return (legalDistance && !isRiver(dest)) || canJumpRiver1(src, dest);
        }
        if (st.getName().equals("Tiger")){
            return (legalDistance && !isRiver(dest)) || canJumpRiver1(src, dest);
        }
        if (st.getName().equals("Leopard")){
            return legalDistance && !isRiver(dest);
        }
        if (st.getName().equals("Wolf")){
            return legalDistance && !isRiver(dest);
        }
        if (st.getName().equals("Dog")){
            return legalDistance && !isRiver(dest);
        }
        if (st.getName().equals("Cat")){
            return legalDistance && !isRiver(dest);
        }
        if (st.getName().equals("Rat")){
            return legalDistance;
        }
        return false;
    }


    public boolean isValidCapture(ChessboardPoint src, ChessboardPoint dest) {
        // TODO:Fix this method
        ChessPiece attacker = getChessPieceAt(src);
        ChessPiece defender = getChessPieceAt(dest);

        if(attacker == null || defender == null) {return false;}
        if(attacker.getOwner() == defender.getOwner()) {return false;}

        int rank = defender.getRank();
        if(isTrap(defender, dest)) rank = 1;


        boolean leagalDistance = calculateDistance(src, dest) == 1;

        if (attacker.getName().equals("Elephant")) {
            return leagalDistance && !isRiver(dest) && rank != 1;
        }
        if (attacker.getName().equals("Lion")) {

            return ((leagalDistance && !isRiver(dest)) || canJumpRiver1(src,dest)) && rank <= 7;
        }
        if (attacker.getName().equals("Tiger")) {
            //System.out.println("isValidCapture"+src.getRow()+" "+src.getCol()+" "+dest.getRow()+" "+dest.getCol());
            return ((leagalDistance && !isRiver(dest)) || canJumpRiver1(src,dest)) && rank <= 6;
        }
        if (attacker.getName().equals("Leopard")) {
            return leagalDistance && !isRiver(dest) && rank <=5;
        }
        if (attacker.getName().equals("Wolf")) {
            return leagalDistance && !isRiver(dest) && rank <=4;
        }
        if (attacker.getName().equals("Dog")) {
            return leagalDistance && !isRiver(dest) && rank <=3;
        }
        if (attacker.getName().equals("Cat")) {
            return leagalDistance && !isRiver(dest) && rank <=2;
        }
        if (attacker.getName().equals("Rat")) {
            return leagalDistance &&
                    (isRiver(src)&&isRiver(dest) || !isRiver(src)&&!isRiver(dest)) &&
                        (rank ==1 || rank == 8);
        }

        return false;
    }

    private  boolean isTrap(ChessPiece chess, ChessboardPoint point) {
        if(chess.getOwner() == PlayerColor.BLUE) {
            return (point.getRow() == 1 && point.getCol() == 3)
                    || (point.getRow() == 0 && point.getCol() == 2)
                        || (point.getRow() == 0 && point.getCol() == 4);
        } else {
            return (point.getRow() == 7 && point.getCol() == 3)
                    || (point.getRow() == 8 && point.getCol() == 2)
                        || (point.getRow() == 8 && point.getCol() == 4);
        }
    }
    private boolean isRiver(ChessboardPoint point) {
        int col = point.getCol();
        int row = point.getRow();
        return row >= 3 && row <= 5 && (col >= 1 && col <= 2 || col <= 5 && col >= 4);
    }

    private boolean canJumpRiver1(ChessboardPoint src, ChessboardPoint dest) {
        //起始位置
        int xs = src.getRow(), ys = src.getCol();
        //目标位置
        int xe = dest.getRow(), ye = dest.getCol();
        // System.out.println("testJump1"+xs+" "+ys+" "+xe+" "+ye);
        // if(canJumpRiver2(xs, ys, xe, ye) || canJumpRiver2(xe, ye, xs, ys)) System.out.println("testjunpriver1");
        return canJumpRiver2(xs, ys, xe, ye) || canJumpRiver2(xe, ye, xs, ys);
    }

    private boolean canJumpRiver2(int xs, int ys, int xe, int ye) {
        if (xs == 3 && ys == 0 && xe == 3 && ye == 3) {
            if (getChessPieceAt(new ChessboardPoint(3, 1)) == null &&
                    getChessPieceAt(new ChessboardPoint(3, 2)) == null) return true;
        }
        if (xs == 4 && ys == 0 && xe == 4 && ye == 3) {
            if (getChessPieceAt(new ChessboardPoint(4, 1)) == null &&
                    getChessPieceAt(new ChessboardPoint(4, 2)) == null) return true;
        }
        if (xs == 5 && ys == 0 && xe == 5 && ye == 3) {
            if (getChessPieceAt(new ChessboardPoint(5, 1)) == null &&
                    getChessPieceAt(new ChessboardPoint(5, 2)) == null) return true;
        }
        if (xs == 3 && ys == 3 && xe == 3 && ye == 6) {
            if (getChessPieceAt(new ChessboardPoint(3, 4)) == null &&
                    getChessPieceAt(new ChessboardPoint(3, 5)) == null) return true;
        }
        if (xs == 4 && ys == 3 && xe == 4 && ye == 6) {
            if (getChessPieceAt(new ChessboardPoint(4, 4)) == null &&
                    getChessPieceAt(new ChessboardPoint(4, 5)) == null) return true;
        }
        if (xs == 5 && ys == 3 && xe == 5 && ye == 6) {
            if (getChessPieceAt(new ChessboardPoint(5, 4)) == null &&
                    getChessPieceAt(new ChessboardPoint(5, 5)) == null) return true;
        }

        if (xs == 2 && ys == 1 && xe == 6 && ye == 1) {
            if (getChessPieceAt(new ChessboardPoint(3, 1)) == null &&
                    getChessPieceAt(new ChessboardPoint(4, 1)) == null &&
                    getChessPieceAt(new ChessboardPoint(5, 1)) == null) return true;
        }
        if (xs == 2 && ys == 2 && xe == 6 && ye == 2) {
            if (getChessPieceAt(new ChessboardPoint(3, 2)) == null &&
                    getChessPieceAt(new ChessboardPoint(4, 2)) == null &&
                    getChessPieceAt(new ChessboardPoint(5, 2)) == null) return true;
        }
        if (xs == 2 && ys == 4 && xe == 6 && ye == 4) {
            if (getChessPieceAt(new ChessboardPoint(3, 4)) == null &&
                    getChessPieceAt(new ChessboardPoint(4, 4)) == null &&
                    getChessPieceAt(new ChessboardPoint(5, 4)) == null) return true;
        }
        if (xs == 2 && ys == 5 && xe == 6 && ye == 5) {
            if (getChessPieceAt(new ChessboardPoint(3, 5)) == null &&
                    getChessPieceAt(new ChessboardPoint(4, 5)) == null &&
                    getChessPieceAt(new ChessboardPoint(5, 5)) == null) return true;
        }
        return false;
    }
}
