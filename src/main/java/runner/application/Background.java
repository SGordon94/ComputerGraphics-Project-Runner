package runner.application;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Background {
	
	private ImageResource img;
	private Point2D.Double position;
	private int maxWidth;
	private ArrayList<Double> xPos;
	public int n;
	private int last;
	
	public Background(ImageResource img, int maxWidth) {
		this.img = img;
		xPos = new ArrayList<Double>();
		n=0;
		last = 0;
	//this.position = position;
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
	
	public ImageResource GetImg(){
		return img;
	}
	
	public void SetLast(int last) {
		this.last = last;
	}
	
	public int GetLast() {
		return this.last;
	}
	
	public ArrayList<Double> GetX(){
		return xPos;
	}

}
