package runner.application;

import java.util.ArrayList;

public class Background {
	
	private ImageResource img;
	private int maxWidth;
	private ArrayList<Double> xPos;
	public int n;
	private int last;
	
	public Background(ImageResource img, int maxWidth) {
		this.img = img;
		xPos = new ArrayList<Double>();
		n=0;
		last = 0;
		this.maxWidth = maxWidth;
		LoopImage();
	}
	
	// Return number of times to loop image to fill screen
	private void LoopImage() {
		
		n = 0;
		
		// Must have at least two instances of the image for smooth scrolling
		int totalWidth = 0;
		
		while(totalWidth < (maxWidth+img.getTexture().getImageWidth()) ) {
			xPos.add((double)totalWidth);
			totalWidth+=img.getTexture().getImageWidth();
			n++;
		}	
		
		last = n-1;
		
	}
	
	// Return stored image
	public ImageResource GetImg(){
		return img;
	}
	
	// Set which location should be at the tail end of the scrolling animation
	public void SetLast(int last) {
		this.last = last;
	}
	
	// Get the location of the X value that is last in the current scrolling loop
	public int GetLast() {
		return this.last;
	}
	
	// Return all of the Starting x positions
	public ArrayList<Double> GetX(){
		return xPos;
	}

}
