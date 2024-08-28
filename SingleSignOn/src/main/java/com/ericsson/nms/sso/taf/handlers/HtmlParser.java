package com.ericsson.nms.sso.taf.handlers;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlParser {

	static Logger log = Logger.getLogger(HtmlParser.class);
	
	public static String getHTMLTitle(String response) {
		Document doc = Jsoup.parse(response);
		String title= doc.title();
		log.debug("Page title:" +title);
		return title;
	}
}
