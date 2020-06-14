package com.forecast.springBoot; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
@RestController
@EnableAutoConfiguration
public class ForecastCourseController {
	
	@Autowired
	SampleProperty sampleProp;
	
    @RequestMapping("/fresco")
    public String test() {
        return "Hi! Welcome to Fresco World";
    }
}