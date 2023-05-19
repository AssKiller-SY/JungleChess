package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * This is the equivalent of the Cell class,
 * but this class only cares how to draw Cells on ChessboardComponent
 */
/*
这段代码是一个 Java 类，继承自 `JPanel` 类，用于在 GUI
界面中显示一个颜色块（CellComponent）。这个颜色块可以设置背景颜色、位置和大小。

在类中，定义了一个名为 `background` 的私有成员变量，表示颜色块的背景颜色。
在类的构造函数中，接收了三个参数：背景颜色、位置和大小，并进行了相应的设置。
其中，使用了 `setLayout()` 方法设置了布局管理器为 `GridLayout`，并使用了 `setLocation()` 和 `setSize()` 方法设置了颜色块的位置和大小。

在类中，重写了 `paintComponent()` 方法，用于绘制颜色块的背景。在方法中，
首先调用了 `super.paintComponents(g)` 方法，以绘制 JPanel 的默认组件。接着，使用 `g.setColor()` 方法设置了绘图上下文的颜色为背景颜色，并使用 `g.fillRect()` 方法绘制了一个填充矩形，表示颜色块的背景。其中，使用了 `getWidth()` 和 `getHeight()` 方法获取颜色块的宽度和高度，并使用 `-1` 进行了微调，以确保颜色块的边框不会被覆盖。

整个类的作用是用于在 GUI 界面中显示一个颜色块，可以根据需要设置颜色、位置和大小。
在 `paintComponent()` 方法中，使用了 Graphics 类的方法进行绘制，以实现颜色块的绘制。
 */
public class CellComponent extends JPanel {
    private Color background;

    public boolean getCanReach() {
        return canReach;
    }

    public void setCanReach(boolean canReach) {
        this.canReach = canReach;
    }
    private int size;
    private boolean canReach;

    public CellComponent(Color background, Point location, int size) {
        setLayout(new GridLayout(1, 1));
        setLocation(location);
        setSize(size, size);
        this.background = background;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.setColor(background);
        g.fillRect(1, 1, this.getWidth() - 1, this.getHeight() - 1);


        if (canReach) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.green);
            Shape roundedRectangle = new RoundRectangle2D.Double
                    (1, 1, this.getWidth() - 1, this.getHeight() - 1, size / 4, size / 4);
            g2d.fill(roundedRectangle); }
    }
}
