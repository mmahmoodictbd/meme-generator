package com.chumbok.meme.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtils {

	private ImageUtils() {
		throw new IllegalStateException(
				"ImageUtils can not be initiated. All methods are static.");
	}

	public static BufferedImage read(String url) {

		URL imgUrl = null;
		try {
			imgUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		if (imgUrl == null) {
			try {
				BufferedImage in = ImageIO.read(imgUrl);
				return in;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static BufferedImage getWidthScaledImage(BufferedImage image,
			int width) throws IOException {

		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		int height = (int) (((double) imageHeight / imageWidth) * width);

		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;

		AffineTransform scaleTransform = AffineTransform.getScaleInstance(
				scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(
				scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		return bilinearScaleOp.filter(image, new BufferedImage(width, height,
				image.getType()));
	}

	public static String toBase64(BufferedImage image) {

		byte[] imageInByteArray = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, "png", baos);
			baos.flush();
			imageInByteArray = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return javax.xml.bind.DatatypeConverter
				.printBase64Binary(imageInByteArray);
	}
}
