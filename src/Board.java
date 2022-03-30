import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
    private static final long serialVersionUID = 1L;
    private Point[][] points = new Point[][]{};
    private int size = 25;
    public int editType = 0;

    public Board(int length, int height) {
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    private void initialize(int length, int height) {
        points = new Point[length][height];

        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y] = new Point();
            }
        }

        for (int x = 0; x < points.length; ++x) {
            points[x][2].setNextRight(points[(x + 1) % points.length][3]);
            points[x][2].setNextLeft(points[(x + 1) % points.length][2]);

            points[x][2].setPrevRight(points[ x - 1 >= 0 ? x - 1: points.length - 1][3]);
            points[x][2].setPrevLeft(points[ x - 1 >= 0 ? x - 1: points.length - 1][2]);

            points[x][3].setNextRight(points[(x + 1) % points.length][3]);
            points[x][3].setNextLeft(points[(x + 1) % points.length][2]);

            points[x][3].setPrevRight(points[ x - 1 >= 0 ? x - 1: points.length - 1][3]);
            points[x][3].setPrevLeft(points[ x - 1 >= 0 ? x - 1: points.length - 1][2]);
        }

        for (int y: new int[]{0, 1, 4, 5})
            for (Point[] point : points)
                point[y].setType(5);
    }

    public void iteration() {

        for (int x = 0; x < points.length; x++) {
            points[x][2].move();
            points[x][3].move();
        }

        for (int x = 0; x < points.length; x++) {
            points[x][2].setMoved(false);
            points[x][3].setMoved(false);
        }

        this.repaint();
    }

    public void clear() {
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y].clear();
            }
        this.repaint();
    }


    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor(Color.GRAY);
        drawNetting(g, size);
    }

    private void drawNetting(Graphics g, int gridSpace) {
        Insets insets = getInsets();
        int firstX = insets.left;
        int firstY = insets.top;
        int lastX = this.getWidth() - insets.right;
        int lastY = this.getHeight() - insets.bottom;

        int x = firstX;
        while (x < lastX) {
            g.drawLine(x, firstY, x, lastY);
            x += gridSpace;
        }

        int y = firstY;
        while (y < lastY) {
            g.drawLine(firstX, y, lastX, y);
            y += gridSpace;
        }

        for (x = 0; x < points.length; ++x) {
            for (y = 0; y < points[x].length; ++y) {
                Color color = switch(points[x][y].getType())
                {
                    case 0 -> new Color(255, 255, 255);
                    case 1 -> new Color(230, 239, 19, 255);
                    case 2 -> new Color(52, 153, 175, 255);
                    case 3 -> new Color(183, 3, 3, 255);
                    case 5 -> new Color(32, 220, 11, 255);

                    default -> throw new IllegalStateException("Unexpected point type: " + points[x][y].getType());
                };

                g.setColor(color);

                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if(editType==0){
                points[x][y].clicked();
            }
            else {
                points[x][y].setType(editType);
            }
            this.repaint();
        }
    }

    public void componentResized(ComponentEvent e) {
        int dlugosc = (this.getWidth() / size) + 1;
        int wysokosc = (this.getHeight() / size) + 1;
        initialize(dlugosc, wysokosc);
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if(editType==0){
                points[x][y].clicked();
            }
            else {
                points[x][y].setType(editType);
            }
            this.repaint();
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

}
