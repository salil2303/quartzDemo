package com.spring.quartz.quartzDemo.jobs;

import javax.validation.constraints.NotNull;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class ScheduleRequest {
    
    @NotNull
    private LocalDateTime dateTime;

    @NotNull
    private ZoneId timeZone;
	
}