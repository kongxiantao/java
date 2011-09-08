package com.th.spider.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.adobe.dp.epub.io.OCFContainerWriter;
import com.adobe.dp.epub.io.StringDataSource;
import com.adobe.dp.epub.ncx.TOCEntry;
import com.adobe.dp.epub.opf.NCXResource;
import com.adobe.dp.epub.opf.OPSResource;
import com.adobe.dp.epub.opf.Publication;
import com.adobe.dp.epub.opf.Resource;
import com.adobe.dp.epub.ops.Element;
import com.adobe.dp.epub.ops.OPSDocument;
import com.adobe.epubcheck.api.EpubCheck;
import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.util.DefaultReportImpl;

/**
 * 从网上抓取网页下来，打包成epub 书目录url： 参考
 * http://code.google.com/p/epub-tools/wiki/HelloEPUB2
 * http://code.google.com/p/epub-tools/w/list
 * 
 * @author
 * 
 */
public class Exmaple5 {

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String bookId = "618119";
		makeEpub(bookId, "D:/temp");
		System.out.println("over");
	}

	/**
	 * 根据小说id生成对应的epub文件
	 * 
	 * @param HtmlBookId
	 * @return
	 * @throws Exception
	 */
	public static boolean makeEpub(String HtmlBookId, String epubDir)
			throws Exception {
		String bookCatalogUrl = "http://book.com/?bookid=" + HtmlBookId;
		String bookCatalogHtml = downloadUrlContent(bookCatalogUrl);
		String bookTitle = Exmaple5.getBookTitle(bookCatalogHtml);
		String bookAuthor = Exmaple5.getBookAuthor(bookCatalogHtml);
		String dir = "OEBPS";
		Publication epub = new Publication(dir);
		// see http://www.idpf.org/2007/opf/OPF_2.0_final_spec.html#Section2.1
		// <title>: 题名 <creator> ：责任者 <subject> ：主题词或关键词 <description> ：内容描述
		// <contributor> ：贡献者或其它次要责任者 <date> ：日期 <type> ：类型 <format> ：格式
		// <identifier> ：标识符 <source> ：来源 <language> ：语种 <relation> ：相关信息
		// <coverage> ：履盖范围 <rights> ：权限描述
		epub.addDCMetadata("title", bookTitle);// 添加标题
		epub.addDCMetadata("creator", bookAuthor);// 添加书作者
		addIntro(epub, HtmlBookId);// 添加简介
		epub.addDCMetadata("publisher", "lizongbo");
		epub.addDCMetadata("contributor", "lizongbo");
		// /epub.addDCMetadata("date", "");
		// /epub.addDCMetadata("type", "");
		// /epub.addDCMetadata("format", "lizongbo");
		epub.addDCMetadata("identifier", "Htmlbook_" + HtmlBookId);
		epub.addDCMetadata("source", bookCatalogUrl);
		epub.addDCMetadata("language", "zh");
		// /epub.addDCMetadata("ralation", "");
		// /epub.addDCMetadata("coverage", "无");
		epub.addDCMetadata("rights", "本书由lizongbo整理网页生成");
		epub.addMetadata(null, "cover", "cover-image");// 添加蜂蜜图片的id
		String[] chapterIds = getChapterIds(bookCatalogHtml, HtmlBookId);
//		addCoverImg(epub, HtmlBookId);// 添加封面和缩略图
		for (int i = 0; i < 3000 && i < chapterIds.length; i++) {
			addChapter(epub, HtmlBookId, chapterIds[i]);// 添加章节
		}
		File outFile = new File(epubDir, "Htmlbook_" + HtmlBookId + ".epub");
		OutputStream out = new FileOutputStream(outFile);
		OCFContainerWriter container = new OCFContainerWriter(out);
		epub.serialize(container);
		//checkEpub(outFile.getAbsolutePath());
		return false;

	}

	/**
	 * 检查 epub书格式是否ok
	 * 
	 * @param epubName
	 */
	public static void checkEpub(String epubName) {
		Report report = new DefaultReportImpl(epubName);
		if (!epubName.endsWith(".epub"))
			report.warning(null, 0, "filename does not include  '.epub' suffix");

		EpubCheck check = new EpubCheck(new File(epubName), report);
		if (check.validate())
			System.out.println("No errors or warnings detected");
		else {
			System.err.println("\nCheck finished with warnings or errors!\n");
		}
	}

	/**
	 * 添加封面图片和缩略图
	 * 
	 * @param epub
	 * @param HtmlBookId
	 * @throws Exception
	 */
	/*public static void addCoverImg(Publication epub, String HtmlBookId)
			throws Exception {
		String bookUrl = "http://book.com/index_" + HtmlBookId + ".htm";
		String bookHtml = downloadUrlContent(bookUrl);
		String coverImgUrl = getCoverImgUrl(bookHtml);
		BitmapImageResource coverImg = epub.createBitmapImageResource(
				epub.getContentFolder() + "/images/cover.jpg", "image/jpeg",
				new ImgFileUrlDataSource(coverImgUrl));
		coverImg.setId("cover-image");
		// 还需要把封面图片转成缩略图 thumb.png
		BufferedDataSource thumbDs = new BufferedDataSource();
		BufferedImage bi = ImageIO.read(new URL(coverImgUrl));// 读到原图
		BufferedImage tag = null;
		tag = new BufferedImage(54, 75, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(bi, 0, 0, 54, 75, null); // 绘制缩小后的图
		ImageIO.write(tag, "png", thumbDs.getOutputStream());
		BitmapImageResource thumbImg = epub.createBitmapImageResource(
				epub.getContentFolder() + "/images/thumb.png", "image/png",
				thumbDs);
	}*/

	/**
	 * 根据章节id添加章节
	 * 
	 * @param epub
	 * @param chapterUrl
	 *            章节url
	 * @throws Exception
	 */
	public static void addChapter(Publication epub, String HtmlBookId,
			String chapterId) throws Exception {
		String chapterUrl = "http://book.com/book/chapter_" + HtmlBookId + "_"
				+ chapterId + ".html";
		String chapterHtml = downloadUrlContent(chapterUrl);
		String chapterTitle = Exmaple5.getChapterTitle(chapterHtml);
		String chapterText = Exmaple5.getChapterText(chapterHtml);
		chapterText = chapterText.replaceAll("</p><p>", "\n");
		chapterText = chapterText.replaceAll("<p>", "");
		chapterText = html2txt(chapterText.replaceAll("</p>", "")).trim();
		String chapterTextArr[] = chapterText.split("\n");
		addChapter(epub, HtmlBookId, chapterId, chapterTitle, chapterTextArr);

	}

	/**
	 * 根据章节内容添加章节
	 * 
	 * @param epub
	 * @param chapterId
	 * @param title
	 * @param texts
	 */
	public static void addChapter(Publication epub, String HtmlBookId,
		String chapterId, String title, String[] texts) {
		if (texts == null || texts.length < 1) {
		System.out.println("warn: " + HtmlBookId + "|" + chapterId + "|"
		+ title + " texts is empty");
		return;
		}
		if (title == null || title.length() < 1) {
		System.out.println("warn: " + HtmlBookId + "|" + chapterId + "|"
		+ title + " title is empty");
		return;
		}
		NCXResource toc = epub.getTOC();
		TOCEntry rootTOCEntry = toc.getRootTOCEntry();
		String chapterFile = epub.getContentFolder() + "/" + chapterId
		+ ".html";
		System.out.println("addChapter " + chapterFile + "|" + chapterId + "|"
		+ title);
		OPSResource chapter1 = epub.createOPSResource(chapterFile);
		epub.addToSpine(chapter1);
		OPSDocument chapter1Doc = chapter1.getDocument();
		TOCEntry chapter1TOCEntry = toc.createTOCEntry(title, chapter1Doc
		.getRootXRef());
		rootTOCEntry.add(chapter1TOCEntry);
		Element body1 = chapter1Doc.getBody();
		Element header1 = chapter1Doc.createElement("h1");
		header1.add(title);
		body1.add(header1);
		{// 添加原文来源：
		String chapterUrl = "http://book.com/book/chapter_"
		+ HtmlBookId + "_" + chapterId + ".html";
		Element paragraph1 = chapter1Doc.createElement("p");
		paragraph1.add("原文来源：" + chapterUrl);
		body1.add(paragraph1);
		}
		for (int i = 0; texts != null && i < texts.length; i++) {
		Element paragraph1 = chapter1Doc.createElement("p");
		paragraph1.add(texts[i]);
		body1.add(paragraph1);
		}

	}

	/**
	 * 添加小说简介
	 * 
	 * @param epub
	 * @param HtmlBookId
	 * @throws Exception
	 */
	public static void addIntro(Publication epub, String HtmlBookId)
			throws Exception {
		String bookUrl = "http://book.com/book/index_" + HtmlBookId + ".html";
		String bookHtml = downloadUrlContent(bookUrl);
		String startText = "<div >";
		String endText = "</div>";
		String intro = getStringLastBetween(bookHtml, startText, endText);
		intro = intro.replaceAll("<p>", "");
		intro = html2txt(intro.replaceAll("</p>", ""));
		intro = intro + "\n来源：" + bookUrl;
		epub.addDCMetadata("description", intro);
		Resource introRes = epub.createResource("intro.txt", "text/plain",
				new StringDataSource(intro));
		startText = "<div class=\"linkOther\">";
		endText = "</div>";
		String keywords = getStringLastBetween(bookHtml, startText, endText);
		keywords = html2txt(keywords);
		String ks[] = keywords.split("\n");
		for (String s : ks) {
			if (s != null && s.trim().length() > 0) {
				epub.addDCMetadata("subject", s);// 支持多个关键字
			}
		}

	}

	/**
	 * 用GB2312下载网页内容
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	public static String downloadUrlContent(String urlStr) throws Exception {
		return downloadUrlContent(urlStr, "GB2312");
	}

	/**
	 * 根据章节内内容获得章节标题
	 * 
	 * @param chapterHtml
	 * @return
	 */
	public static String getChapterTitle(String chapterHtml) {
		String startText = "<h1>";
		String endText = "</h1>";
		return getStringLastBetween(chapterHtml, startText, endText);
	}

	/**
	 * 根据章节内容获取小说内容的html
	 * 
	 * @param chapterHtml
	 * @return
	 */
	public static String getChapterText(String chapterHtml) {
		String startText = "<div>";
		String endText = "</div>";
		return getStringLastBetween(chapterHtml, startText, endText);
	}

	/**
	 * 根据目录列表网页内容获取小说标题
	 * 
	 * @param bookHtml
	 * @return
	 */
	public static String getBookTitle(String bookHtml) {
		String startText = "<title>";
		String endText = "</title>";
		String title = getStringLastBetween(bookHtml, startText, endText);
		if (title.contains("_")) {
			title = title.substring(0, title.indexOf("_"));
		}
		return html2txt(title);
	}

	/**
	 * 根据目录列表网页内容获取作者名称
	 * 
	 * @param bookHtml
	 * @return
	 */
	public static String getBookAuthor(String bookHtml) {
		String startText = "<h1>";
		String endText = "</h1>";
		String title = getStringLastBetween(bookHtml, startText, endText);
		startText = "<span>";
		endText = "</span>";
		title = getStringLastBetween(title, startText, endText);
		System.out.println("getBookAuthor==" + title);
		return title.length() > 0 ? html2txt(title) : "无名";
	}

	/**
	 * 根据目录页面网页内容获得章节Id
	 * 
	 * @param bookHtml
	 * @return
	 */
	public static String[] getChapterIds(String bookHtml, String HtmlBookId) {
		java.util.List<String> chapterList = new ArrayList<String>();
		String startText = "<a href=\"c_" + HtmlBookId + "_";
		String endText = ".html\"";
		String chapterId = null;
		while ((chapterId = getStringBetween(bookHtml, startText, endText))
				.length() > 0) {
			System.out.println("chapterId==" + chapterId);
			chapterList.add(chapterId);
			bookHtml = bookHtml.substring(bookHtml.indexOf(startText)
					+ startText.length());
		}

		return chapterList.toArray(new String[0]);
	}

	/**
	 * 根据小说首页html，提取封面图片路径
	 * 
	 * @param bookHtml
	 * @return
	 */
	public static String getCoverImgUrl(String bookHtml) {
		String startText = "http://book.com/cover";
		String endText = ".jpg";
		String url = getStringBetween(bookHtml, startText, endText);
		url = startText + url + endText;
		System.out.println("getCoverImgUrl==" + url);
		return url;
	}

	/**
	 * 获取文本中最后一次出现在两个字符串之间的文字，不包含开头和结尾的字符串
	 * 
	 * @param src
	 * @param startText
	 * @param endText
	 * @return
	 */
	public static String getStringLastBetween(String src, String startText,
			String endText) {
		if (src != null && src.contains(startText)) {
			int startIndex = src.lastIndexOf(startText);
			int endIndex = src.indexOf(endText, startIndex);
			if (endIndex > startIndex) {
				return src.substring(startIndex + startText.length(), endIndex);

			}
		}
		return "";

	}

	/**
	 * 获取文本中第一次出现在两个字符串之间的文字，不包含开头和结尾的字符串
	 * 
	 * @param src
	 * @param startText
	 * @param endText
	 * @return
	 */
	public static String getStringBetween(String src, String startText,
			String endText) {
		if (src != null && src.contains(startText)) {
			int startIndex = src.indexOf(startText);
			int endIndex = src.indexOf(endText, startIndex);
			if (endIndex > startIndex) {
				return src.substring(startIndex + startText.length(), endIndex);

			}
		}
		return "";

	}

	/**
	 * 用指定编码下载网页内容
	 * 
	 * @param urlStr
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String downloadUrlContent(String urlStr, String encoding)throws Exception {
		URL url = new URL(urlStr);
		URLConnection urlc = url.openConnection();
		urlc
		.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
		urlc
		.setRequestProperty("Accept",
		"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		urlc.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
		urlc.setRequestProperty("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
		urlc.setConnectTimeout(5000);
		urlc.connect();
		StringBuilder sb = new StringBuilder(4096);
		BufferedReader in = new BufferedReader(new InputStreamReader(urlc
		.getInputStream(), encoding));
		String line;
		while ((line = in.readLine()) != null) {
		sb.append(line).append('\n');
		}
		in.close();
		System.out.println(urlStr);
		return sb.toString().trim();
	}

	/**
	 * 提取html的文本内容
	 */
	public static String html2txt(String s) {
		if (s != null) {
			return s.replaceAll("<.*?>", "");
		}
		return "";
	}
}