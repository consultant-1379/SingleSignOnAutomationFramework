package com.ericsson.nms.sso.taf.getters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.testng.log4testng.Logger;
import com.ericsson.cifwk.taf.utils.FileFinder;

public class FileGetter  {
	
	static Logger log = Logger.getLogger(FileGetter.class);
	
	public static File getFileByName(String fileName) {
		log.info("Get file " + fileName);
		List<String> pathOfFiles = FileFinder.findFile(fileName);
		File wantedFile = null; 
		if (pathOfFiles.size() > 0) {
			String path = pathOfFiles.get(0);
			wantedFile = new File(path);
		} else {
			log.error("Cannot find the file: " + fileName, new FileNotFoundException());
		}
		return wantedFile;
	}

}
