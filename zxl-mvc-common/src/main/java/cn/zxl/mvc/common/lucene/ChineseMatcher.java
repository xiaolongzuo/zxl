package cn.zxl.mvc.common.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class ChineseMatcher implements InitializingBean {

	private SmartChineseAnalyzer smartChineseAnalyzer = new SmartChineseAnalyzer(Version.LUCENE_47);
	
	private List<String> smallWeightWords;
	
	private double smallWeight = 0.3D;
	
	public void setSmallWeightWordsResource(Resource resource) {
		try {
			if (smallWeightWords == null) {
				smallWeightWords = Collections.emptyList();
			}
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				smallWeightWords.add(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setSmallWeightWords(List<String> smallWeightWords) {
		this.smallWeightWords = smallWeightWords;
	}

	public void setSmallWeight(double smallWeight) {
		this.smallWeight = smallWeight;
	}

	public void afterPropertiesSet() throws Exception {
		if (smallWeightWords == null) {
			smallWeightWords = Collections.emptyList();
		}
	}
	
	public boolean match(String text1,String text2,double maxUnmatchRate) {
		if (text1 == null || text2 == null) throw new NullPointerException("text1 and text2 can't be null!");
		if (text1.equals(text2)) return true;
		return twoWayMatch(text1, text2) <= maxUnmatchRate;
	}
	
	public double twoWayMatch(String text1,String text2) {
		return (oneWayMatch(text1, text2) + oneWayMatch(text2, text1));
	}
	
	public double oneWayMatch(String text1,String text2) {
		try {
			Set<String> set = new HashSet<String>(10);
			TokenStream tokenStream = smartChineseAnalyzer.tokenStream("field", text1);
			CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
			tokenStream.reset();
			while (tokenStream.incrementToken()) {
				set.add(charTermAttribute.toString());
			}
			int originalCount = set.size();
			tokenStream.end();
			tokenStream.close();
			tokenStream = smartChineseAnalyzer.tokenStream("field", text2);
			charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
			tokenStream.reset();
			int smallWeightWordsCount = 0;
			int denominator = 0;
			while (tokenStream.incrementToken()) {
				denominator++;
				String word = charTermAttribute.toString();
				int tempSize = set.size();
				set.add(word);
				if (tempSize + 1 == set.size() && smallWeightWords.contains(word)) {
					smallWeightWordsCount++;
				}
			}
			int numerator = set.size() - originalCount;
			double unmatchRate = (smallWeightWordsCount * smallWeight + numerator - ((double)smallWeightWordsCount))/denominator;
			tokenStream.end();
			tokenStream.close();
			return unmatchRate;
		} catch (IOException e) {
			return 1D;
		}
		
    }
	
}
