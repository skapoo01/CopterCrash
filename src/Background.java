/*
*	Purpose:	The background object models the backdrop of the Helicopter
*				Game
*	Filename:	Background.java
*	Author:		Siddharth Kapoor
*	Date:		April 18, 2018
*/

public class Background {

	private int xcord, ycord;

	public Background(int x, int y){
		this.xcord= x;
		this.ycord=y;	
	}

	public int getX(){
		return this.xcord;
	}

	public int getY(){
		return this.ycord;
	}

	public void shiftX(){
		this.xcord=xcord-10;
	}
}