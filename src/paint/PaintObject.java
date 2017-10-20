package paint;

//Sean Gallagher, Mark Hunter
//Alex's Section
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class PaintObject implements Serializable {
	private Point point1, point2;
	private Color color;

	abstract public void draw(Graphics g, Image i);

	public PaintObject(Color color, Point point, Point point2) {
		this.point1 = point;
		this.point2 = point2;
		this.color = color;
	}

	protected Point getPoint1() {
		return this.point1;
	}

	protected Point getPoint2() {
		return this.point2;
	}

	protected Color getColor() {
		return this.color;
	}
}
