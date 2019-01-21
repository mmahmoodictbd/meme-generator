package com.chumbok.meme.controller;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chumbok.meme.service.MemeService;
import com.chumbok.meme.util.ImageUtils;

@Controller
public class MemeController {

	@Autowired
	private ServletContext context;

	@Autowired
	private MemeService memeService;

	@Value("${uploadedImagePath}")
	private String uploadedImagePath;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "redirect:images";
	}

	@RequestMapping(value = "/images", method = RequestMethod.GET)
	public ModelAndView showImages(
			@RequestParam(required = false, defaultValue = "0") int offset,
			@RequestParam(required = false, defaultValue = "20") int max,
			Model model) throws IOException {

		return new ModelAndView("images", "fileNames",
				memeService.getFileNames(offset, max));
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {

		List<String> messages = new ArrayList<String>();

		if (file.isEmpty()) {
			messages.add("No file selected for upload.");
		}

		String fileName = file.getOriginalFilename();

		String mimeType = context.getMimeType(fileName);
		if (mimeType == null || !mimeType.startsWith("image/")) {
			messages.add("Uploaded file is not an image file.");
		}

		try {
			memeService.uploadImage(fileName, file.getBytes());
			messages.add("You successfully uploaded " + fileName + "!");
		} catch (IOException e) {
			messages.add("You failed to upload " + fileName + " => "
					+ e.getMessage());
		}

		redirectAttributes.addFlashAttribute("messages", messages);

		return "redirect:images";
	}

	@RequestMapping(value = "/writeTextOnImage", method = RequestMethod.POST)
	public String writeText(
			@RequestParam("imgname") String imageFileName,
			@RequestParam(value = "toptext1", required = false, defaultValue = "") String topText1,
			@RequestParam(value = "toptext2", required = false, defaultValue = "") String topText2,
			@RequestParam(value = "bottomtext1", required = false, defaultValue = "") String bottomText1,
			@RequestParam(value = "bottomtext2", required = false, defaultValue = "") String bottomText2,
			@RequestParam(value = "font", required = false, defaultValue = "Impact") String font,
			@RequestParam(value = "fontSize", required = false, defaultValue = "30") int fontSize,
			@RequestParam(value = "isBold", required = false, defaultValue = "true") boolean isBold,
			RedirectAttributes redirectAttributes) throws IOException {

	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        for (Font font1 : fonts) {
            System.out.print(font1.getFontName() + " : ");
            System.out.println(font1.getFamily());
        }
        
		BufferedImage image = null;

		try {
			image = ImageIO.read(new URL("http://localhost:5000/image/"
					+ imageFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean addMemeSigneture = true;
		image = ImageUtils.getWidthScaledImage(image, 600);
		image = memeService.buildMeme(topText1, topText2, bottomText1,
				bottomText2, font, fontSize, isBold, addMemeSigneture, image);

		String b64 = ImageUtils.toBase64(image);
		redirectAttributes.addFlashAttribute("b64", b64);

		return "redirect:showImage?tmp=true&imageFileName=" + imageFileName;

	}

	@RequestMapping(value = "/showImage", method = RequestMethod.GET)
	public String showImages(
			@RequestParam(required = false) String imageFileName,
			@RequestParam(required = false, defaultValue = "0") boolean tmp,
			Model model, @ModelAttribute("b64") String b64) throws IOException {

		model.addAttribute("imgName", imageFileName);
		model.addAttribute("b64", b64);

		return "writeText";
	}

}
