package com.greenadine.clocksign.clocks;

import java.util.Date;
import java.util.TimeZone;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class RealClock extends Clock {

	protected TimeZone m_timezone = TimeZone.getDefault();
	
	public RealClock(Block signBlock) {
		super(signBlock);
	}
	
	public void update() {
		if (!isValid()) return;
		
		Sign s = sign();
		
		s.setLine(0, "");
		if (!m_label.isEmpty()) {
			s.setLine(1, m_label);
		} else {
			s.setLine(1, m_formatter.getTimeZone().getDisplayName(false, TimeZone.SHORT));
		}
		s.setLine(2, m_formatter.format(new Date()));
		s.setLine(3, "");
		
		s.update();
	}
	
	@Override
	public void setFormat(String format) {
		super.setFormat(format);
		m_formatter.setTimeZone(m_timezone);
		//m_format = m_formatter.toPattern();
	}
	
	// TimeZone methods
	public void setTimeZone(String timezone) {
		m_timezone = TimeZone.getTimeZone(timezone);
		m_formatter.setTimeZone(m_timezone);
	}
	
	public TimeZone getTimezone() {
		return m_timezone;
	}
	
}

