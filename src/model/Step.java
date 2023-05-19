package model;

import view.Animal.AnimalChessComponent;

public class Step {
    public ChessboardPoint src;
    public ChessboardPoint dest;
    public PlayerColor color;
    public ChessPiece eaten;
    //public AnimalChessComponent eatenComponent;


    public Step(ChessboardPoint src, ChessboardPoint dest, PlayerColor color) {
        this.src = src;
        this.dest = dest;
        this.color = color;
        eaten = null;
    }

    public Step(ChessboardPoint src, ChessboardPoint dest, PlayerColor color, ChessPiece eaten ) {
        this.src = src;
        this.dest = dest;
        this.color = color;
        this.eaten = eaten;
        //this.eatenComponent = eatenComponent;
    }

    @Override
    public String toString() {
        int xs = src.getRow();
        int ys = src.getCol();
        int xe = dest.getRow();
        int ye = dest.getCol();
        if (eaten == null) {
            if(color == PlayerColor.BLUE) {
                return String.format("b from (%d,%d) to (%d,%d), no capture",xs,ys,xe,ye);
            } else {
                return String.format("r from (%d,%d) to (%d,%d), no capture",xs,ys,xe,ye);
            }
        } else {
            if(color == PlayerColor.BLUE) {
                return String.format("b from (%d,%d) to (%d,%d), eat %s",xs,ys,xe,ye,eaten.getName());
            } else {
                return String.format("r from (%d,%d) to (%d,%d), eat %s",xs,ys,xe,ye,eaten.getName());
            }
        }
    }
}
