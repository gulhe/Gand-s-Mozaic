package org.gulhe.scatter.pompage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageParser {
	static List<String> getLinksToFilmPagesHref(String url)
			throws IOException {
		ArrayList<String> ret = new ArrayList<>();
		Elements linkNodes = getLinkNodes(url);
		for (Iterator<Element> iterator = linkNodes.iterator(); iterator
				.hasNext();) {
			Element element = (Element) iterator.next();
			String href = element.attr("href");
			ret.add("http://www.imdb.com" + href);
		}
		return ret;
	}

	static Elements getImageNode(String textContent) throws IOException {
		Document page = Jsoup.connect(textContent).get();
		return page.select("#img_primary .image img");
	}

	static Elements getLinkNodes(String textContent) throws IOException {
		System.out.println("getting : " + textContent);
		Document page = Jsoup.parse(new URL(textContent), 100000);
		Elements select = page.select("#main .lister-item-image .title a");
		return select;
	}

	static ArrayList<String> getUrlsOfAllMoviePages(String baseUrl,
			int pageAmount) {
		ArrayList<String> allHrefs = new ArrayList<>();

		for (int i = 1; i < pageAmount+1; i++) {
			try {
				allHrefs.addAll(PageParser.getLinksToFilmPagesHref(baseUrl + i));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return allHrefs;
	}

}
