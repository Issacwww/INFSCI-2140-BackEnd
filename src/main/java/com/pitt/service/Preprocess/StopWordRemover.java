package com.pitt.service.Preprocess;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StopWordRemover {
	private Set<String> v1 = new HashSet<>();
	private final Pattern p = Pattern.compile("how can i|how do i|how should i|how would you|what is the|how to use|is it possible|is there a|best way to|not able to|the difference between|how to");
	StopWordRemover() {
		try {
			Resource resource = new ClassPathResource("data/stopword.txt");
			System.out.println("stopword: " + resource.getFile().getPath());
//			String stopword = resource.getFile().getPath()+"//data//stopword.txt";
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(resource.getFile())));
			String str;
			while((str = bufferedReader.readLine())!=null)//using the readLine to read a Line of the content
				v1.add(str);
			bufferedReader.close();
		}catch (IOException e){
			e.printStackTrace();
		}

	}

	boolean isStopWord(String word) {
		for(String s: v1)
			if (s.equals(word))
				return true;
		return false;
	}

	String removeTriGram(String query) {
		//todo: remove the trigram
		Matcher m = p.matcher(query);
		StringBuffer removed = new StringBuffer();
		while (m.find())
			m.appendReplacement(removed,"");
		m.appendTail(removed);

		return removed.toString().trim();
	}

}
