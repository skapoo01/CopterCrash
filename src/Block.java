/*
*	Purpose:	The Block object models the surfaces that add up to become
*				the floor and ceiling of the helicopter's path
*	Filename:	Block.java
*	Author:		Siddharth Kapoor
*	Date:		April 18, 2018
*/

public class Block {
	private int height = 10;
	private int width = 50;
	private int xcord, ycord;

	public Block(int x, int y){
		this.xcord = x;
		this.ycord = y;
	}

	public int getHeight(){
		return this.height;
	}

	public int getWidth(){
		return this.width;
	}

	public int getX(){
		return this.xcord;
	}

	public int getY(){
		return this.ycord;
	}

	public void shiftX(){
		this.xcord = xcord - 10;
	}
}