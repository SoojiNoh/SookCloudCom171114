package com.smu.saason.api;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;

public class WebCrawler {
	/* 주어진 시간 간격 마다
	 * 오늘의 유머 베스트오브베스트 첫 페이지 상의
	 * "오직 이미지가 포함된" (이미지가 없으면 X) 모든 게시글의 "첫 번째" 이미지가 .png 혹은 .jpg 확장자일 때
	 * 해당하는 이미지를 C:/Temp/ 폴더에 원본 이미지 파일 이름으로 저장
	 * 11.19 오후 7시 사진이 포함된 16개의 게시글에서 첫 번째 이미지만을 저장함
	 * 시작시간 18:46:51 ~ 끝시간 18:53:30 : 수행시간 6분 39초 (Thread.sleep으로 지정 > 추후 무한으로 실행 시 없애면 될듯)
	 * */
	private static int interval = 60000; /* 60000ms = 1min */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		ScheduledJob2 job = new ScheduledJob2();
		Timer jobScheduler = new Timer();
	
		jobScheduler.scheduleAtFixedRate(job, 1000, interval);
		try {
			Thread.sleep(400000); /* 400000ms = 6min 39sec */
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		jobScheduler.cancel();
		System.out.println("END DATE: " + job.getCurrentDate());
	}
}

class ScheduledJob2 extends TimerTask {
	public String getCurrentDate() {
		SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return date.format(new Date());
	}
	
	public void run() {
		System.out.println("START DATE: " + getCurrentDate());
		// http://news.naver.com/main/list.nhn?mode=LS2D&mid=shm&sid1=105&sid2=283
		HttpPost httpPost = new HttpPost("http://www.todayhumor.co.kr/board/list.php?table=bestofbest");
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		ContentType contentType = ContentType.getOrDefault(entity);
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(entity.getContent()));
		} catch (UnsupportedOperationException | IOException e) {
			e.printStackTrace();
		}
		
		StringBuffer buffer = new StringBuffer();
		String line = "";
		
		try {
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String totalString = buffer.toString();
		String subString = totalString.substring(totalString.indexOf("bestofbest&no")+14, totalString.indexOf("bestofbest&no")+20);
		int number = Integer.parseInt(subString);
		int startNumber = Integer.parseInt(subString);
		
		for (int i = 20 ; i > 0 ; i--) {
			number--;
			startNumber--;
			
			System.out.println("NUMBER: " + number);
			HttpPost httpPost2 = new HttpPost("http://www.todayhumor.co.kr/board/view.php?table=bestofbest&no="+number + "&s_no="+startNumber + "&page=1");
			HttpClient httpClient2 = HttpClientBuilder.create().build();
			HttpResponse response2 = null;
			try {
				response2 = httpClient2.execute(httpPost2);
			} catch (IOException e) {
				e.printStackTrace();
			}
			HttpEntity entity2 = response2.getEntity();
			ContentType contentType2 = ContentType.getOrDefault(entity2);
			Charset charset = contentType.getCharset();
			
			BufferedReader reader2 = null;
			
			try {
				reader2 = new BufferedReader(new InputStreamReader(entity2.getContent()));
			} catch (UnsupportedOperationException | IOException e) {
				e.printStackTrace();
			}
			
			StringBuffer buffer2 = new StringBuffer();
			
			String line2 = null;
			try {
				while ((line2 = reader2.readLine()) != null) {
					buffer2.append(line2 + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println(buffer.toString());
			
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
				String temp = buffer2.toString();
				out.write(temp);
				out.newLine();
				out.close();
			} catch (IOException e) {
				System.err.println(e);
				System.exit(1);
			}
			
			String s1 = buffer2.toString();
			String s2 = "";
			String s3 = "";
			
			try {
				s2 = s1.substring(s1.indexOf("http://thimg.todayhumor.co.kr/upfile"), s1.indexOf("http://thimg.todayhumor.co.kr/upfile")+200);
				if (s2.indexOf(".png") > -1) {
					s3 = s2.substring(s2.indexOf("http://thimg.todayhumor.co.kr/upfile"), s2.indexOf(".png")) + ".png";
				}
				else if (s2.indexOf(".jpg") > -1) {
					s3 = s2.substring(s2.indexOf("http://thimg.todayhumor.co.kr/upfile"), s2.indexOf(".jpg")) + ".jpg";
				}
			} catch (Exception e) {
				System.out.println("1: NO IMAGE");
			}
			
			try {
				String imgUrl = s3;
				URL url = new URL(imgUrl);
				String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
				String ext = imgUrl.substring(imgUrl.lastIndexOf('.') + 1, imgUrl.length());
				BufferedImage img = ImageIO.read(url);
				
				ImageIO.write(img,  ext,  new File("C://Temp//" + fileName));
				System.out.println("-------------------------------------------------------------------------");
			} catch (Exception e) {
				System.out.println("2: NO IMAGE");
			}
		}
		
	}
}

