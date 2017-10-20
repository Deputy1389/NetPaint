package paint;

//Sean Gallagher, Mark Hunter
//Alex's Section
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

@SuppressWarnings("serial")
public class Pic extends PaintObject {
	public Pic(Point point, Point point2) {
		super(null, point, point2);
	}

	// Draws the image
	@Override
	public void draw(Graphics g, Image i) {
		Graphics2D g2 = (Graphics2D) g;
		if (getPoint1().x >= getPoint2().x && getPoint1().y >= getPoint2().y)
			g2.drawImage(i, getPoint2().x, getPoint2().y, Math.abs(getPoint1().x - getPoint2().x),
					Math.abs(getPoint1().y - getPoint2().y), null);
		else if (getPoint1().x < getPoint2().x && getPoint1().y >= getPoint2().y)
			g2.drawImage(i, getPoint1().x, getPoint2().y, Math.abs(getPoint1().x - getPoint2().x),
					Math.abs(getPoint1().y - getPoint2().y), null);
		else if (getPoint1().x >= getPoint2().x && getPoint1().y < getPoint2().y)
			g2.drawImage(i, getPoint2().x, getPoint1().y, Math.abs(getPoint1().x - getPoint2().x),
					Math.abs(getPoint1().y - getPoint2().y), null);
		else
			g2.drawImage(i, getPoint1().x, getPoint1().y, Math.abs(getPoint1().x - getPoint2().x),
					Math.abs(getPoint1().y - getPoint2().y), null);
	}

}