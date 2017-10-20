package paint;

//Sean Gallagher, Mark Hunter
//Alex's Section
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

@SuppressWarnings("serial")
public class Line extends PaintObject {
	public Line(Color color, Point point, Point point2) {
		super(color, point, point2);
	}

	// Draws a line
	@Override
	public void draw(Graphics g, Image i) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getColor());
		g2.drawLine(getPoint1().x, getPoint1().y, getPoint2().x, getPoint2().y);
	}

}
