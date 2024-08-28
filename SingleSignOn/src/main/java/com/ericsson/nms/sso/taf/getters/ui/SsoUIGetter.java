package com.ericsson.nms.sso.taf.getters.ui;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import com.ericsson.cifwk.taf.utils.FileFinder;

import com.ericsson.cifwk.taf.RestGetter;

public class SsoUIGetter implements RestGetter {

	static Logger log = Logger.getLogger(SsoUIGetter.class);
	
	public static final String citrixCannotSaveErrorImage = "citrixCannotSaveError.jpg";
	public static final String citrixCannotSaveOkButtonImage = "citrixCannotSaveOkButton.jpg";
	public static final String citrixLoginLogoImage = "citrixLoginLogo.jpg";
	public static final String citrixLoginOKButton = "citrixLoginOKButton.jpg";
	public static final String citrixInvalidLoginWindow = "citrixInvalidLoginWindow.jpg";
	public static final String citrixInvalidLoginOKButton = "citrixInvalidLoginWindowOKButton.jpg";
	public static final String citrixLoginCancelButton = "citrixLoginCancelButton.jpg";
	
	public static final String citrixAppExit = "citrixAppExit.jpg";
	public static final String citrixAppExitOkButton = "citrixAppExitOkButton.jpg";

	public static final String citrixExistingSession = "citrixConnectToExistingSession.jpg";
	public static final String citrixNewSessionButton = "citrixNewSession.jpg";
			
	public File getImageFile(String name) {
		List<String> list =FileFinder.findFile(name);
		if(list.isEmpty()) {
			log.error("No File found:"+name);
			return null;
		}
		return new File(list.get(0));
	}
}
