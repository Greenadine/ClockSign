package com.greenadine.clocksign.clocks;

import java.util.GregorianCalendar;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class PlayerTimeClock extends Clock {
	
	public PlayerTimeClock(Block signBlock) {
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
		s.setLine(2, "Not working");
		
		s.setLine(3, "yet.");
		
		s.update();
	}
	
	
	public String getPlayerTime (Player player) {
		int hours = (int)((player.getPlayerTime() / 1000 + 6 ) % 24);
		int minutes = (int) (60 * (player.getPlayerTime() % 1000) / 1000);
		
		String cal = new GregorianCalendar(4012, 6, 11, hours, minutes, 0).toString(); // An arbitrary date
		
		return cal;
	}
	
}
