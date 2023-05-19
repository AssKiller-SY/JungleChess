package view.Animal;

import model.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AnimalChessComponent extends JComponent {
    public boolean selected;
    int size;
    public PlayerColor owner;
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean flag) {this.selected=flag;}

    public AnimalChessComponent(){};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //高亮，如果被点击了
        if (isSelected()) {
            Graphics2D g2d = (Graphics2D) g;
            if(owner == PlayerColor.BLUE) {
                g2d.setColor(Color.BLUE);
            } else {
                g2d.setColor(Color.RED);
            }
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(1, 1, getWidth() - 1, getHeight() - 1, size / 4, size / 4);
            g2d.fill(roundedRectangle);
        }
    }
}
