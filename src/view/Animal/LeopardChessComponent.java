package view.Animal;


import model.PlayerColor;

import javax.swing.*;
import java.awt.*;

/**
 * This is the equivalent of the ChessPiece class,
 * but this class only cares how to draw Chess on ChessboardComponent
 */
public class LeopardChessComponent extends AnimalChessComponent {

    private boolean selected;
    private int size;

    public LeopardChessComponent(PlayerColor owner, int size) {
        this.owner = owner;
        this.selected = false;
        this.size = size;
        setSize(size / 2, size / 2);
        setLocation(0, 0);
        setVisible(true);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ImageIcon pic;
        if (owner == PlayerColor.RED) {
            pic = new ImageIcon("resource/chess/redLeopard.png");
        } else {
            pic = new ImageIcon("resource/chess/blueLeopard.png");
        }
        Image image = pic.getImage();
        g.drawImage(image, 5, 5, size-10, size-10, null);
    }
}
