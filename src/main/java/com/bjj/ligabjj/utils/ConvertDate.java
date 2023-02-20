package com.bjj.ligabjj.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import com.bjj.ligabjj.exceptions.ProjectException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertDate {
	
	public static Date formatDate(String format, String date) {
		
		log.debug("Format date: " + date);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			log.error("Format date invalid: " + date);
			throw new ProjectException(e);
		}
	}
	
	public static String formatDate(String format, Date date) {
		
		log.debug("Format date: " + date);
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	
	public static Timestamp localToTimeStamp(String dateStr) throws ParseException {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(simpleDateFormat.parse(dateStr));
		LocalDate date = LocalDate.of(calendar.get(Calendar.YEAR), (calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getMonthValue(), calendar.get(Calendar.DAY_OF_MONTH));
		return Timestamp.from(date.atStartOfDay().toInstant(ZoneOffset.UTC));
		
	}

}
