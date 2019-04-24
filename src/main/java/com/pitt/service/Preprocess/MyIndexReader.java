package com.pitt.service.Preprocess;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeMap;

public class MyIndexReader {
	private Directory directory;
	private DirectoryReader ireader;

	private IndexSearcher isearcher;
	private static String textIndex =  "/data/aitext";
	private static String codeIndex =  "/data/aicode";
//

	public MyIndexReader(String dataType) throws IOException {
		Resource resource;
		if (dataType.equals("text")) {
			resource = new ClassPathResource(textIndex);
		} else {
			resource = new ClassPathResource(codeIndex);
		}
		String target = resource.getFile().getPath();
		System.out.println(target);
		directory = FSDirectory.open(Paths.get(target));
		ireader = DirectoryReader.open(directory);
		isearcher = new IndexSearcher(ireader);
	}

	public TreeMap<Integer, Float> search(String query, int TopN) {
		TreeMap<Integer, Float> socres = new TreeMap<>();
		try{
			Query theQ = new QueryParser("CONTENT", new WhitespaceAnalyzer()).parse(query);
			TopFieldDocs topFieldDocs = isearcher.search(theQ, TopN, Sort.RELEVANCE, true, true);
			float maxRef = topFieldDocs.getMaxScore();
			ScoreDoc[] scoreDoc = topFieldDocs.scoreDocs;
			for (ScoreDoc score : scoreDoc) {
				socres.put(Integer.valueOf(ireader.document(score.doc).get("DOCNO")), score.score / maxRef);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return socres;
	}
}
