package runner.application;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageResource {

    private Texture texture = null; //texture to be drawn
    private BufferedImage image = null; //image to read into texture

    public ImageResource(String path) {
        URL url = ImageResource.class.getResource(path); //get path to image
        try {
            //read image
            image = ImageIO.read(url);
        } 
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        if (image != null) {
            image.flush(); //to fix memory leak
        }
    }

    public Texture getTexture() {
        //return the image as a texture
        if (image == null) return null;
        if (texture == null) {
            texture = AWTTextureIO.newTexture(Application.getProfile(), image, true);
        }
        return texture;
    }
}