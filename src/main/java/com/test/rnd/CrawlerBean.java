package com.test.rnd;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@ManagedBean(name = "crawlerBean")
@RequestScoped
public class CrawlerBean {

	private static final String A_HREF = "a[href]";
	private static final String ABS_HREF = "abs:href";
	private static final String LAST_SLASH_FILTER = "^(.*)\\$";
	private static final String BABYLONHEALTH_COM = "^.*?babylonhealth.com.*$";
	private static final String NO_EMAIL_NO_URL_DATA = "((?!(\\?|@)).)*$";
	private static final String NO_ZIP_NO_PDF = "((?!(.zip|.pdf)).)*$";

	Map<String, Set<String>> urlMap = new HashMap<String, Set<String>>();

	@PostConstruct
	private void init() {
		getLinks("https://babylonhealth.com");
		// urlMap.put("a", Arrays.asList("ss", "fdf"));
		// urlMap.put("b", Arrays.asList("ddd", "vvv"));
	}

	private void getLinks(String url) {
		urlMap.put(url, new HashSet<String>());
		try {
			Document document = Jsoup.connect(url).get();
			Elements linksOnPage = document.select(A_HREF);
			for (Element page : linksOnPage) {
				String childURL = page.attr(ABS_HREF);

				// Removes last '\' character to avoid duplicate
				childURL = childURL.replaceAll(LAST_SLASH_FILTER, "");

				// Filters
				boolean matchBabylonhealthURL = childURL.matches(BABYLONHEALTH_COM);
				boolean noEmailOrURLdata = childURL.matches(NO_EMAIL_NO_URL_DATA);
				boolean noPdfOrZip = childURL.matches(NO_ZIP_NO_PDF);
				if (matchBabylonhealthURL && noEmailOrURLdata && noPdfOrZip) {
					urlMap.get(url).add(childURL);
					if (!urlMap.containsKey(childURL)) {

						System.out.println(page.attr(ABS_HREF));
						getLinks(page.attr(ABS_HREF));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage() + " " + url);
			// Ignore if the site/page is not available for any reason.
			urlMap.remove(url);
		}
	}

	// Main method, Ignore
	public static void main(String[] args) {
		String url = "https://babylonhealth.com";
		CrawlerBean bean = new CrawlerBean();
		bean.getLinks(url);
		System.out.println(bean.urlMap);
	}

	public Map<String, Set<String>> getUrlMap() {
		// Just to return sorted by keys for UI experience.
		// It will not be preferred in real environment
		return new TreeMap<String, Set<String>>(urlMap);
	}

	public void setUrlMap(Map<String, Set<String>> urlMap) {
		this.urlMap = urlMap;
	}

}