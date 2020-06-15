package com.forecast.springBoot; 


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication 
public class ForecastApi { 
	
	
	@Value("${prefix.latitude}")
	 private String latitude;
	
	@Value("${prefix.longitude}")
	 private String longitude;
	
	/*
	 * @Autowired SampleProperty sampleProp;
	 * 
	 * @Value("${prefix.stringProp1}") private String appTitle;
	 */
	
	
	public static void main(String args[]){ 
		
		//System.out.println(FrescoApi.appTitle);
		
		
		
		SpringApplication.run(ForecastApi.class,args);
	} 
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			
			Object obj = null;
			String paramForCmdLineLatitude=null;
			String paramForCmdLineLongitude=null;
			
			System.out.println(args.length+" outside");
			
			for (int i = 0; i < args.length; i++) {
				if(args[i].contains("latitude")) {
					paramForCmdLineLatitude=args[i].split(",")[0];
					paramForCmdLineLongitude=args[i].split(",")[1];
				}
			}
			
			
			//Quote quote = restTemplate.getForObject("https://api.weather.gov/points/39.7456,-97.0892", Quote.class);
		
			System.out.println("Latitude:-"+latitude);
			System.out.println("Longitude:-"+longitude);
			
			if(paramForCmdLineLatitude!=null)
				obj = new JSONParser().parse(restTemplate.getForObject("https://api.weather.gov/points/"+paramForCmdLineLatitude+","+paramForCmdLineLongitude, String.class));
			else
				obj = new JSONParser().parse(restTemplate.getForObject("https://api.weather.gov/points/"+latitude+","+longitude, String.class));
			
			JSONObject jo = (JSONObject) obj; 
			
			
			//Getting the Forecast API details
			 Map address = ((Map)jo.get("properties")); 
			Iterator<Map.Entry> itr1 = address.entrySet().iterator(); 
	        while (itr1.hasNext()) { 
	            Map.Entry pair = itr1.next(); 
	            if(pair.getKey().equals("forecast")) {
	          //  	System.out.println(pair.getKey() + " : " + pair.getValue()); 
	            	Object obj1 = new JSONParser().parse(restTemplate.getForObject(pair.getValue().toString(), String.class));
	            	JSONObject jo1 = (JSONObject) obj1;
	            	//System.out.println("------------------1");
	            	//System.out.println(jo1);
	            	Map periods = ((Map)jo1.get("properties"));
	            	//System.out.println("------------------");
	            	//System.out.println(periods.get("periods"));
	            	JSONArray dailyTempArr = (JSONArray)periods.get("periods");
//	            	System.out.println(dailyTempArr);
	            	StringBuilder strbuilder 
	                = new StringBuilder(); 
	            	
	            	strbuilder.append("--------------Five Day Temperature-----------------------");strbuilder.append("\n");
	            	strbuilder.append("S.No|  Name          |     Start Time           |     End Time                |  Temp  | Details ");strbuilder.append("\n");
	            	for (Iterator iterator = dailyTempArr.iterator(); iterator.hasNext();) {
	            		JSONObject dailyTempObj =  (JSONObject)iterator.next();
	            		//System.out.println(dailyTempObj);
	            		
	            		if(!dailyTempObj.get("name").toString().contains("Night")) {
	            			strbuilder.append(dailyTempObj.get("number")+"    |"+dailyTempObj.get("name")+"           | "+dailyTempObj.get("startTime")+" |  "+
	        	            		dailyTempObj.get("endTime")+"  |"+dailyTempObj.get("temperature")+"       |  "+dailyTempObj.get("detailedForecast"));	
	            		}else {
	            			strbuilder.append(dailyTempObj.get("number")+"    |"+dailyTempObj.get("name")+"      | "+dailyTempObj.get("startTime")+" |  "+
		    	            		dailyTempObj.get("endTime")+"  |"+dailyTempObj.get("temperature")+"       |  "+dailyTempObj.get("detailedForecast"));	
	            		}
	            		strbuilder.append("\n");
	            		
	            		
					}
	            	//System.out.println(strbuilder);
	            	File file = new File("output.txt");
	            	BufferedWriter writer = null;
	            	try {
	            	    writer = new BufferedWriter(new FileWriter(file));
	            	    writer.write(strbuilder.toString());
	            	} finally {
	            	    if (writer != null) writer.close();
	            	}
	            	
	            }
	            	
	            }
			
		};
	}
	
}