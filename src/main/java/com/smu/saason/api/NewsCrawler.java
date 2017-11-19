package com.smu.saason.api;

import java.io.IOException;
import java.nio.charset.Charset;
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

public class NewsCrawler {
	/* 주어진 시간 간격 마다
	 * 오늘의 유머 베스트오브베스트 첫 페이지 상의
	 * "오직 이미지가 포함된" (이미지가 없으면 X) 모든 게시글의 "첫 번째" 이미지가 .png 혹은 .jpg 확장자일 때
	 * 해당하는 이미지를 C:/Temp/ 폴더에 원본 이미지 파일 이름으로 저장
	 * 11.19 오후 7시 사진이 포함된 16개의 게시글에서 첫 번째 이미지만을 저장함
	 * 시작시간 18:46:51 ~ 끝시간 18:53:30 : 수행시간 6분 39초 (Thread.sleep으로 지정 > 추후 무한으로 실행 시 없애면 될듯)
	 * */
	private static int interval = 60000; /* 60000ms = 1min */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		ScheduledJob3 job = new ScheduledJob3();
		Timer jobScheduler = new Timer();
	
		jobScheduler.scheduleAtFixedRate(job, 1000, interval);
		try {
			Thread.sleep(400000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		jobScheduler.cancel();
		System.out.println("END DATE: " + job.getCurrentDate());
	}
}

class ScheduledJob3 extends TimerTask {
	public String getCurrentDate() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return date.format(new Date());
	}
	
	public void run() {
		System.out.println("START DATE: " + getCurrentDate());
		/* http://news.naver.com/main/list.nhn?mode=LS2D&mid=shm&sid1=105&sid2=283
		 *  dt-a > title / dt-a:href > link / dd-span.writing > press */
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("http://news.naver.com/main/list.nhn?mode=LS2D&mid=shm&sid1=105&sid2=283");
		
		try {
			httpClient.execute(httpGet, new BasicResponseHandler() {
				@Override
				public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
					HttpEntity entity = response.getEntity();
					ContentType contentType = ContentType.getOrDefault(entity);
					Charset charset = contentType.getCharset();
					String result = new String(super.handleResponse(response).getBytes(charset), "euc-kr");
					Document doc = Jsoup.parse(result);
					Elements rows = doc.select("table.container tbody tr td.content div.content div.list_body ul.type06_headline li");
//					String[] items = new String[] {"SinglePost"};
					
					for (Element row : rows) {
//						Iterator<Element> iterTotalElement = row.getElementsByTag("dl").iterator();
						Elements titles = row.select("dt a");
						Elements links = row.select("a[href]");
						Elements presses = row.select("dd span.writing");
						StringBuilder builder = new StringBuilder();
						
//						for (String item : items) {
//							builder.append(item + ": " + iterTotalElement.next().text() + "	\t");
//						}
						
						builder.append(titles.text() + "    \t");
						builder.append(links.attr("abs:href") + "    \t");
						builder.append(presses.text() + "    \t");
						
						System.out.println(builder.toString());
					}
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

