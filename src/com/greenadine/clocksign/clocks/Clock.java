package com.greenadine.clocksign.clocks;

import java.text.SimpleDateFormat;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.greenadine.clocksign.ClockSign;

public abstract class Clock {
	protected static Block m_signBlock = null;
	protected String m_format = ClockSign.defaultTimeFormat;
	protected SimpleDateFormat m_formatter = new SimpleDateFormat(m_format);
	protected String m_label = "Time";
	
	public Clock(Block signBlock) {
		m_signBlock = signBlock;
	}
	
	public Block getBlock() {
		return m_signBlock;
	}
	
	// Updates the text of the sign.
	public abstract void update();
	
    //////
	// Format methods
	public void setFormat(String format) {
		if (!format.isEmpty()) {
			m_formatter = new SimpleDateFormat(format);
			m_format = m_formatter.toPattern();
		}
	}
	
	public String getFormat() {
		return m_format;
	}
	
	public SimpleDateFormat formatter() {
		return m_formatter;
	}
	
	//////
	// Label methods
	public void setLabel(String label) {
		if (!label.isEmpty()) {
			m_label = label;
		}
	}
	
	public void setDate(String date) {
		if(!date.isEmpty()) {
			m_formatter = new SimpleDateFormat(date);
			m_format = m_formatter.toPattern();
		}
	}
	
	public String getLabel() {
		return m_label;
	}
	
	// Check if this clock is still associated with a sign.
	public boolean isValid() {
		return m_signBlock.getType() == Material.WALL_SIGN || m_signBlock.getType() == Material.SIGN_POST;
	}
	
	public boolean isLoaded() {
		return m_signBlock.getChunk().isLoaded();
	}
	
	// Convenience method
	public Sign sign() {
		return (Sign)m_signBlock.getState();
	}
	
	public String getDate() {
		return sign().getLine(2);
	}
}
