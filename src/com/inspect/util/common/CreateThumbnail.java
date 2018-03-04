package com.inspect.util.common;

import java.awt.Graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CreateThumbnail {

	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	public static final String IMAGE_JPEG = "jpeg";
	public static final String IMAGE_JPG = "jpg";
	public static final String IMAGE_PNG = "png";

	private ImageIcon image;
	private ImageIcon thumb;

	public CreateThumbnail(Image image) {
		this.image = new ImageIcon(image);
	}

	public CreateThumbnail(String fileName) {
		image = new ImageIcon(fileName);
	}

	public Image getThumbnail(int size, int dir) {
		if(image.getIconHeight() < size){
			size = image.getIconHeight();
		}
		if (dir == HORIZONTAL) {
			thumb = new ImageIcon(image.getImage().getScaledInstance(size, -1,
					Image.SCALE_SMOOTH));
		} else {
			thumb = new ImageIcon(image.getImage().getScaledInstance(-1, size,
					Image.SCALE_SMOOTH));
		}
		return thumb.getImage();
	}

	public Image getThumbnail(int size, int dir, int scale) {
		if(image.getIconHeight() < size){
			size = image.getIconHeight();
		}
		if (dir == HORIZONTAL) {
			thumb = new ImageIcon(image.getImage().getScaledInstance(size, -1,
					scale));
		} else {
			thumb = new ImageIcon(image.getImage().getScaledInstance(-1, size,
					scale));
		}
		return thumb.getImage();
	}

	public void saveThumbnail(File file, String imageType) {
		if (thumb != null) {
			BufferedImage bi = new BufferedImage(thumb.getIconWidth(),
					thumb.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();
			g.drawImage(thumb.getImage(), 0, 0, null);
			try {
				ImageIO.write(bi, imageType, file);
			} catch (IOException ioe) {
				System.out.println("Error occured saving thumbnail");
			}
		} else {
			System.out.println("Thumbnail has not yet been created");
		}
	}

	public static void main(String[] args) {
		CreateThumbnail ct = new CreateThumbnail("D:/upload/11.png");
		ct.getThumbnail(80, CreateThumbnail.HORIZONTAL);
		ct.saveThumbnail(new File("D:/upload/11_22.png"), CreateThumbnail.IMAGE_JPEG);
	}

}
