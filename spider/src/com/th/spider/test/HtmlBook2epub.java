package com.th.spider.test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.adobe.dp.epub.io.OCFContainerWriter;
import com.adobe.dp.epub.ncx.TOCEntry;
import com.adobe.dp.epub.opf.NCXResource;
import com.adobe.dp.epub.opf.OPSResource;
import com.adobe.dp.epub.opf.Publication;
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
public class HtmlBook2epub {
	
	public static String epubDir = "D:/epub/";

	/**
	 * 根据小说id生成对应的epub文件
	 * 
	 * @param HtmlBookId
	 * @return
	 * @throws Exception
	 */
	
	public static boolean makeEpub(String name,String url)throws Exception {
		String bookCatalogUrl = url;
		String dir = "OEBPS";
		
		Publication epub = new Publication(dir);
		epub.addDCMetadata("title", name);
		epub.addDCMetadata("identifier",name);
		epub.addDCMetadata("source", bookCatalogUrl);
		epub.addDCMetadata("language", "zh");
		epub.addDCMetadata("rights", "liangzhen");
		
		addChapter(epub, name);// 添加章节
		
		String str = url + name;
		String fileName = str.substring(str.lastIndexOf("/"));
		
		File outFile = new File(epubDir,fileName + ".epub");
		OutputStream out = new FileOutputStream(outFile);
		OCFContainerWriter container = new OCFContainerWriter(out);
		epub.serialize(container);
//		checkEpub(outFile.getAbsolutePath());
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
			report.warning(null, 0, "filename does not include '.epub' suffix");

		EpubCheck check = new EpubCheck(new File(epubName), report);
		if (check.validate())
			System.out.println("No errors or warnings detected");
		else {
			System.err.println("\nCheck finished with warnings or errors!\n");
		}
	}

	
	

	/**
	 * 根据章节id添加章节
	 * 
	 * @param epub
	 * @param chapterUrl
	 *            章节url
	 * @throws Exception
	 */
	public static void addChapter(Publication epub, String name) throws Exception {
		String chapterHtml = Exmaple3.getContent(name);
		addChapter(epub, name, chapterHtml);
	}

	/**
	 * 根据章节内容添加章节
	 * 
	 * @param epub
	 * @param chapterId
	 * @param title
	 * @param texts
	 */
	public static void addChapter(Publication epub, String name, String chapterHtml) {
		
		NCXResource toc = epub.getTOC();
		TOCEntry rootTOCEntry = toc.getRootTOCEntry();
		String chapterFile = epub.getContentFolder() + "/" + name+ ".html";
		OPSResource chapter1 = epub.createOPSResource(chapterFile);
		epub.addToSpine(chapter1);
		
		OPSDocument chapter1Doc = chapter1.getDocument();
		TOCEntry chapter1TOCEntry = toc.createTOCEntry(name, chapter1Doc.getRootXRef());
		rootTOCEntry.add(chapter1TOCEntry);
		Element body1 = chapter1Doc.getBody();
		body1.add(chapterHtml);

	}





}