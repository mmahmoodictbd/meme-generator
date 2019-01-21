package com.chumbok.meme.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.chumbok.meme.util.FileUtils;

@Service
public class MemeService {

	private static final String MEME_SIGNETURE = "meme.Chumbok.com";
	private static final int TOP_TEXT_DEFAULT_Y = 40;

	@Value("${uploadedImagePath}")
	private String uploadedImagePath;

	@Autowired
	private ResourceLoader resourceLoader;

	public void uploadImage(String originalFileName, byte[] imageBytes)
			throws IOException {

		BufferedOutputStream stream = new BufferedOutputStream(
				new FileOutputStream(new File(uploadedImagePath + "/"
						+ originalFileName + System.currentTimeMillis())));
		stream.write(imageBytes);
		stream.close();
	}

	public List<String> getFileNames(int offset, int max) {

		List<File> images = FileUtils.readFilesFromDir(uploadedImagePath);

		List<String> urls = new ArrayList<String>();
		for (File imgFile : images) {
			urls.add(imgFile.getName());
		}

		int fromIndex = Math.max(0, offset * max);
		int toIndex = Math.min(urls.size(), (offset + 1) * max);

		return urls.subList(fromIndex, toIndex);
	}

	public BufferedImage buildMeme(String topText1, String topText2,
			String bottomText1, String bottomText2, String font, int fontSize,
			boolean bold, boolean addMemeSigneture, BufferedImage image) {

		BufferedImage img;
		Graphics2D g2d;

		if (!addMemeSigneture) {
			img = new BufferedImage(image.getWidth(), image.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			g2d = img.createGraphics();
			g2d.drawImage(image, 0, 0, null);
		} else {
			img = addMemeSigneture(image);
		}

		g2d = img.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
				RenderingHints.VALUE_STROKE_PURE);

		g2d.setFont(new Font(font, bold ? Font.BOLD : Font.PLAIN, fontSize));
		FontMetrics fm = g2d.getFontMetrics();

		int x = (int) (img.getWidth() - fm.stringWidth(topText1) - (img
				.getWidth() * 0.05)) / 2;
		int y = TOP_TEXT_DEFAULT_Y;

		drawString(g2d, topText1, x, y);

		if (topText2.trim().length() > 0) {
			x = (int) (img.getWidth() - fm.stringWidth(topText2) - (img
					.getWidth() * 0.05)) / 2;
			y += fm.getHeight();
			drawString(g2d, topText2, x, y);
		}

		if (bottomText2.trim().length() > 0) {
			x = (int) (img.getWidth() - fm.stringWidth(bottomText2) - (img
					.getWidth() * 0.05)) / 2;
			y = img.getHeight() - TOP_TEXT_DEFAULT_Y;
			drawString(g2d, bottomText2, x, y);
		}

		if (bottomText1.trim().length() > 0) {

			x = (int) (img.getWidth() - fm.stringWidth(bottomText1) - (img
					.getWidth() * 0.05)) / 2;

			if (bottomText2.trim().length() > 0) {
				y -= fm.getHeight();
			} else {
				y = img.getHeight() - TOP_TEXT_DEFAULT_Y;
			}

			drawString(g2d, bottomText1, x, y);
		}

		g2d.dispose();

		BufferedImage imageRGB = new BufferedImage(img.getWidth(),
				img.getHeight(), BufferedImage.TYPE_INT_RGB);
		imageRGB.setData(img.getData());

		return imageRGB;
	}

	private BufferedImage addMemeSigneture(BufferedImage image) {

		BufferedImage img = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(image, 0, 0, null);

		g2d.setFont(new Font("Courier", Font.BOLD, 15));

		FontMetrics fm = g2d.getFontMetrics();
		int x = img.getWidth() - fm.stringWidth(MEME_SIGNETURE) - 5;
		int y = img.getHeight() - 10;

		g2d.setColor(Color.black);
		g2d.drawString(MEME_SIGNETURE, ShiftWest(x, 1), ShiftNorth(y, 1));
		g2d.drawString(MEME_SIGNETURE, ShiftWest(x, 1), ShiftSouth(y, 1));
		g2d.drawString(MEME_SIGNETURE, ShiftEast(x, 1), ShiftNorth(y, 1));
		g2d.drawString(MEME_SIGNETURE, ShiftEast(x, 1), ShiftSouth(y, 1));

		g2d.setPaint(Color.white);
		g2d.drawString(MEME_SIGNETURE, x, y);

		Resource resource = resourceLoader
				.getResource("classpath:chumbok-dot-com-v1.0.png");
		try {
			File iconFile = resource.getFile();
			BufferedImage chumbokIcon = ImageIO.read(iconFile);
			g2d.drawImage(chumbokIcon, x - 20, y - 12, 17, 17, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		g2d.dispose();

		return img;

	}

	private void drawString(Graphics2D g2d, String text, int x, int y) {
		g2d.setColor(Color.black);
		g2d.drawString(text, ShiftWest(x, 1), ShiftNorth(y, 1));
		g2d.drawString(text, ShiftWest(x, 1), ShiftSouth(y, 1));
		g2d.drawString(text, ShiftEast(x, 1), ShiftNorth(y, 1));
		g2d.drawString(text, ShiftEast(x, 1), ShiftSouth(y, 1));

		g2d.setPaint(Color.white);
		g2d.drawString(text, x, y);
	}

	private int ShiftNorth(int p, int distance) {
		return (p - distance);
	}

	private int ShiftSouth(int p, int distance) {
		return (p + distance);
	}

	private int ShiftEast(int p, int distance) {
		return (p + distance);
	}

	private int ShiftWest(int p, int distance) {
		return (p - distance);
	}

	// http://www.javaworld.com/article/2077584/core-java/java-tip-81--jazz-up-the-standard-java-fonts.html

}
