package com.smu.saason.api;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smu.saason.bean.Posts;
import com.smu.saason.dao.PostsDAO;

// 키 중복 에러!!! 시간마다 객체가 생성되나보다...ㅠㅠ

@Component
public class ScheduledCrawlingJob {
	@Autowired
	PostsDAO postDAO;
	ScheduledJobs2 job = new ScheduledJobs2();

	@Scheduled(cron ="0 0/5 * * * *")
	public void crawl() throws SQLException {
		System.out.println("Test");
		job.run();
		ArrayList<Posts> postList = job.getPostList();
		for (Posts post : postList) {
			postDAO.insert(post);
		}
	}

}

class ScheduledJobs2 extends TimerTask {
	private static String newsProvider = "http://news.naver.com/main/list.nhn?mode=LS2D&mid=shm&sid1=105&sid2=283";
	private static int logFileNo = 1;
	private static int postId = 1;
	private ArrayList<Posts> postList = new ArrayList<Posts>();
	
	public ArrayList<Posts> getPostList() {
		return this.postList;
	}
	
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
						
						java.util.Date dt = new java.util.Date();

						java.text.SimpleDateFormat sdf = 
						     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						String currentTime = sdf.format(dt);
						Posts post = new Posts(postId, titles.text(), links.attr("abs:href"), 1, 2, currentTime, currentTime);
						postList.add(post);
						
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
						
						java.util.Date dt = new java.util.Date();

						java.text.SimpleDateFormat sdf = 
						     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						String currentTime = sdf.format(dt);
						Posts post = new Posts(postId, titles.text(), links.attr("abs:href"), 1, 2, currentTime, currentTime);
						postList.add(post);
						
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
