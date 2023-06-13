import java.awt.Rectangle;

public class Wall extends Rectangle {
	boolean blockedUp = false;
	boolean blockedDown = false;
	boolean blockedRight = false;
	boolean blockedLeft = false;
	public Wall(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public boolean isBlockedUp() {
		return blockedUp;
	}
	public void setBlockedUp(boolean blockedUp) {
		this.blockedUp = blockedUp;
	}
	public boolean isBlockedDown() {
		return blockedDown;
	}
	public void setBlockedDown(boolean blockedDown) {
		this.blockedDown = blockedDown;
	}
	public boolean isBlockedRight() {
		return blockedRight;
	}
	public void setBlockedRight(boolean blockedRight) {
		this.blockedRight = blockedRight;
	}
	public boolean isBlockedLeft() {
		return blockedLeft;
	}
	public void setBlockedLeft(boolean blockedLeft) {
		this.blockedLeft = blockedLeft;
	}
	
	

}
