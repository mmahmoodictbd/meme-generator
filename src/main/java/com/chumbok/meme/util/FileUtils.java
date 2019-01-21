package com.chumbok.meme.util;

import java.io.File;
import java.util.List;

import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileUtils {

	private FileUtils() {
	}

	public static List<File> readFilesFromDir(String dirPath) {

		File dir = new File(dirPath);

		List<File> files = (List<File>) org.apache.commons.io.FileUtils
				.listFiles(dir, TrueFileFilter.INSTANCE,
						TrueFileFilter.INSTANCE);

		return files;

	}
}
