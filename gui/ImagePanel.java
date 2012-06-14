package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This JPanel is created when an image is required to be placed in a Panel
 * 
 * @author Chris McCaw
 * @version 1.0
 */
public class ImagePanel extends JPanel {

	private BufferedImage image;

	public ImagePanel(String imageLocation) {
		try {
			image = ImageIO.read(new File(new Object().getClass()
					.getResource(imageLocation).getPath()));
		} catch (IOException ex) {

		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);

	}

}
