package com.greenadine.clocksign;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TimeZone;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

import com.greenadine.clocksign.clocks.Clock;
import com.greenadine.clocksign.clocks.GameClock;
import com.greenadine.clocksign.clocks.PlayerTimeClock;
import com.greenadine.clocksign.clocks.RealClock;

public class ClockSaver {
	
	// Write out a .csv file containing all clocks in the world, or rather an
    // almost proper csv. The clock label is shoved on the end and special
    // csv formatting is ignored because it complicates the code and is (at
    // this point) unnecessary.
	public static void write(String filename, ArrayList<Clock> clocks) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(filename));
			ArrayList<Clock> clocklist = new ArrayList<Clock>();
            for (Clock clock : clocks) {
            	Block block = clock.getBlock();
            	
            	if (clock instanceof RealClock) {
            		saveClock(out, clocklist, clock, "SYSTEM");
            	} else if (clock instanceof GameClock) {
            		saveClock(out, clocklist, clock, "GAME");
            	} else if (clock instanceof PlayerTimeClock) {
            		saveClock(out, clocklist, clock, "PLAYER");
            	}
            	
                saveBlock(out, block);
                
                if (clock instanceof RealClock) {
            		out.write(((RealClock)clock).getTimezone().getDisplayName(false,TimeZone.SHORT) + ",");
            	} else if (clock instanceof GameClock) {
            		out.print(",");
            	} else if (clock instanceof PlayerTimeClock) {
            		out.print(",");
            	}
                
                out.write(clock.getFormat() + ",");
                out.write(clock.getLabel().replaceAll(",", " ") + "\n");
            }
            out.flush();
            out.close();
		} catch (Exception e) {
			//log.info("[" + getDescription().getName() + "] Cannot save wall clocks!");
			Log.warn("[ClockSign] ERROR: Could not save clocks to file!");
			e.printStackTrace();
		}
	}
	
	public static void saveClock(PrintWriter out, ArrayList<Clock> clocklist, Clock clock, String clocktype) {
		out.print(clocktype + ",");
		clocklist.add(clock);
	}
	
	public static void saveBlock(PrintWriter out, Block block) {
		out.write(block.getWorld().getName() + ",");
        out.write(block.getX() + ",");
        out.write(block.getY() + ",");
        out.write(block.getZ() + ",");
	}
	
	// We need a Server object because we derive the block location
    // based on coordinates that server provides
	@SuppressWarnings("resource")
	public static ArrayList<Clock> read(String filename, Server server) {
		ArrayList<Clock> clocks = new ArrayList<Clock>();
		
		try {
			Scanner in = new Scanner(new FileReader(filename));
            while (in.hasNextLine()) {
            	Clock clock;
                String[] line = in.nextLine().split(",");
                
                int x = Integer.parseInt(line[2]);
                int y = Integer.parseInt(line[3]);
                int z = Integer.parseInt(line[4]);
                Block b = server.getWorld(line[1]).getBlockAt(x, y, z);

                if (line[0].equals("SYSTEM")) {
                	RealClock realclock = new RealClock(b);
                	realclock.setTimeZone(line[5]);
                	clock = realclock;
                } else if (line[0].equals("GAME")) {
                	clock = new GameClock(b);
                } else if (line[0].equals("PLAYER")) {
                	clock = new PlayerTimeClock(b);
                } else {
                	continue;
                }
                
                clock.setFormat(line[6]);
                clock.setLabel(line[7]);

                clocks.add(clock);
            }
		} catch (Exception e) {
			Log.warn("[ClockSign] Could not load clocks!");
			e.printStackTrace();
		}
		
		return clocks;
	}
	
}
