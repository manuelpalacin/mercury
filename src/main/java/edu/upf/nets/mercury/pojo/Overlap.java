package edu.upf.nets.mercury.pojo;

import java.util.ArrayList;
import java.util.List;

public class Overlap {
	
	private int number = 0;
	private int numberWeak = 0;
	private List<String> wordList;
	private List<String> wordListWeak;
	private float percentage0 = 0;
	private float percentage1 = 0;
	private float percentageWeak0 = 0;
	private float percentageWeak1 = 0;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getNumberWeak() {
		return numberWeak;
	}
	public void setWordListWeak(List<String> wordListWeak) {
		this.wordListWeak = wordListWeak;
	}
	public List<String> getWordList() {
		if(wordList==null)
			wordList = new ArrayList<String>();
		return wordList;
	}
	public void setNumberWeak(int numberWeak) {
		this.numberWeak = numberWeak;
	}
	public List<String> getWordListWeak() {
		if (wordListWeak == null)
			wordListWeak = new ArrayList<String>();
		return wordListWeak;
	}
	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}
	public float getPercentage0() {
		return percentage0;
	}
	public void setPercentage0(float percentage0) {
		this.percentage0 = percentage0;
	}
	public float getPercentage1() {
		return percentage1;
	}
	public void setPercentage1(float percentage1) {
		this.percentage1 = percentage1;
	}
	public float getPercentageWeak0() {
		return percentageWeak0;
	}
	public void setPercentageWeak0(float percentageWeak0) {
		this.percentageWeak0 = percentageWeak0;
	}
	public float getPercentageWeak1() {
		return percentageWeak1;
	}
	public void setPercentageWeak1(float percentageWeak1) {
		this.percentageWeak1 = percentageWeak1;
	}
	
	

}
