package com.greenadine.clocksign.clocks;

import java.util.Date;
import java.util.GregorianCalendar;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class GameClock extends Clock {
	
	public GameClock(Block signBlock) {
		super(signBlock);
	}
	
	public void update() {
		if (!isValid()) return;
		
		Sign s = sign();
		
		s.setLine(0, "");
		if (!m_label.isEmpty()) {
			s.setLine(1, m_label);
		} else {
			s.setLine(1, "Time");
		}
		
		s.setLine(2, m_formatter.format(getGameTime()));
		
		s.setLine(3, "");
		
		s.update();
	}
	
	public Date getGameTime() {
		int hours = (int)((m_signBlock.getWorld().getTime() / 1000 + 6) % 24); // 0 time is 6am
		int minutes = (int) (60 * (m_signBlock.getWorld().getTime() % 1000) / 1000);
		
		GregorianCalendar cal = new GregorianCalendar(4012, 6, 11, hours, minutes, 0); // An arbitrary date
		
		return cal.getTime();
		
	}
}

