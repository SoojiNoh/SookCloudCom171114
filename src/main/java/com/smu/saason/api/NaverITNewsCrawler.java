package com.smu.saason.api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.smu.saason.bean.Posts;

public class NaverITNewsCrawler {
	/*
	 * BasicResponseHandler를 ScheduledJob 클래스 안에 내부 클래스로 작성
	 */
	
	/* 
	 * executionTime동안 주어진 interval마다 (시간 단위는 ms이며 60000ms = 1min)
	 * newsProvider로 지정된 url에서 (현재 네이버 뉴스 IT 분야)
	 * 뉴스의 제목, 언론사, 링크 참조
	 */
	
	/*
	 * TODO
	 * (1) 크롤링한 뉴스를 DB에 저장 > Null Pointer Error! (Autowired annotation)
	 * posts : (id, title, url, keyword_id, provider_id)
	 * (2) 유저가 원하는 키워드에 해당하는 뉴스 알림 -> 알림하는 소스 하나 더 만들기
	 */

	private static int interval = 60000 * 60; 
	private static int executionTime = 60000 * 360;
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		ScheduledJob job = new ScheduledJob();
		Timer jobScheduler = new Timer();
	
		jobScheduler.scheduleAtFixedRate(job, 1000, interval);
		try {
			Thread.sleep(executionTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		jobScheduler.cancel();
		System.out.println("END DATE: " + job.getCurrentDate());
	}
}

class ScheduledJob extends TimerTask {
	private static String newsProvider = "http://news.naver.com/main/list.nhn?mode=LS2D&mid=shm&sid1=105&sid2=283";
	private static int logFileNo = 1;
	private static int postId = 1;
	// @Autowired private PostsDAO postDAO; // TODO
	
	public String getCurrentDate() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return date.format(new Date());
	}
	
	public void run() {
		System.out.println("START DATE: " + getCurrentDate());
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(newsProvider);
		
		try {
			httpClient.execute(httpGet, new BasicResponseHandler() {
				@Override
				public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
					HttpEntity entity = response.getEntity();
					ContentType contentType = ContentType.getOrDefault(entity);
					Charset charset = contentType.getCharset();
					String result = new String(super.handleResponse(response).getBytes(charset), "euc-kr");
					Document doc = Jsoup.parse(result);
					Elements headlineRows = doc.select("table.container tbody tr td.content div.content div.list_body ul.type06_headline li");
					Elements generalRows = doc.select("table.container tbody tr td.content div.content div.list_body ul.type06 li");
					BufferedWriter logFileWriter = new BufferedWriter(new FileWriter(logFileNo + ".txt"));
					
					/*  <dt>-<a> title
					 *  <dt>-<a:href> link
					 *  <dd>-<span.writing> press */
					
					System.out.println("[HEADLINE]");
					for (Element row : headlineRows) {
						Elements titles = row.select("dt a");
						Elements presses = row.select("dd span.writing");
						Elements links = row.select("a[href]");
						StringBuilder builder = new StringBuilder();
						
						// append to local builder
						System.out.println("> POSTID " + postId);
						builder.append("<TITLE>" + titles.text() + "\n");
						builder.append("<PRESS>" + presses.text() + "\n");
						builder.append("<LINK>" + links.attr("abs:href"));
						
						// insert to DB
//						Posts post = new Posts(postId, titles.text(), links.attr("abs:href"), 1, 2, System.currentTimeMillis());
//						try {
//							postDAO.insert(post);
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
						
						System.out.println(builder.toString());
						System.out.println("---------------------------------");
						
						// append to logfile
						logFileWriter.append(builder.toString());
						logFileWriter.newLine();
						
						postId++;
					}

					System.out.println("[GENERAL]");
					for (Element row : generalRows) {
						Elements titles = row.select("dt a");
						Elements presses = row.select("dd span.writing");
						Elements links = row.select("a[href]");
						StringBuilder builder = new StringBuilder();
						
						// append to local builder
						System.out.println("> POSTID " + postId);
						builder.append("<TITLE>" + titles.text() + "\n");
						builder.append("<PRESS>" + presses.text() + "\n");
						builder.append("<LINK>" + links.attr("abs:href"));
						
						// insert to DB
//						Posts post = new Posts(postId, titles.text(), links.attr("abs:href"), 1, 2, System.currentTimeMillis());
//						try {
//							postDAO.insert(post);
//						} catch (SQLException e) {
//							e.printStackTrace();
//						}
						
						System.out.println(builder.toString());
						System.out.println("---------------------------------");
						
						// append to logfile
						logFileWriter.append(builder.toString());
						logFileWriter.newLine();
						
						postId++;
					}
					
					logFileWriter.close();
					logFileNo++;
					
					return result;
				}
			});
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

