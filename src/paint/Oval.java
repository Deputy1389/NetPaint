package paint;

//Sean Gallagher, Mark Hunter
//Alex's Section
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

@SuppressWarnings("serial")
public class Oval extends PaintObject {
	public Oval(Color color, Point point, Point point2) {
		super(color, point, point2);
	}

	// Draws an oval
	@Override
	public void draw(Graphics g, Image i) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(getColor());
		if (getPoint1().x >= getPoint2().x && getPoint1().y >= getPoint2().y)
			g2.fillOval(getPoint2().x, getPoint2().y, Math.abs(getPoint1().x - getPoint2().x),
					Math.abs(getPoint1().y - getPoint2().y));
		else if (getPoint1().x < getPoint2().x && getPoint1().y >= getPoint2().y)
			g2.fillOval(getPoint1().x, getPoint2().y, Math.abs(getPoint1().x - getPoint2().x),
					Math.abs(getPoint1().y - getPoint2().y));
		else if (getPoint1().x >= getPoint2().x && getPoint1().y < getPoint2().y)
			g2.fillOval(getPoint2().x, getPoint1().y, Math.abs(getPoint1().x - getPoint2().x),
					Math.abs(getPoint1().y - getPoint2().y));
		else
			g2.fillOval(getPoint1().x, getPoint1().y, Math.abs(getPoint1().x - getPoint2().x),
					Math.abs(getPoint1().y - getPoint2().y));
	}

}
