package net.bberger.packetlogger.text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import sivantoledo.ax25.Afsk1200Modulator;
import sivantoledo.ax25.Afsk1200MultiDemodulator;
import sivantoledo.ax25.Packet;
import sivantoledo.ax25.PacketDemodulator;
import sivantoledo.ax25.PacketHandler;
import sivantoledo.sampledsound.Soundcard;


public class PacketLogger implements PacketHandler {
	static final int RATE = 48000;
	static final int FILTER_LENGTH = 32;
	static final int BUFFER_SIZE = 100;
	static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT+0");
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") {{
		setTimeZone(TIME_ZONE);
	}};
	
//	public PacketLogger(String input, PacketHandler packetHandler)
	
	public void handlePacket(byte[] bytes) {
		Packet packet = new Packet(bytes);
		packet.parse();
		
		System.out.print(DATE_FORMAT.format(new Date()));
		System.out.print('\t');
		System.out.print(packet.source);
		System.out.print('\t');
		System.out.print(packet.destination);
		System.out.print('\t');
		String[] path = packet.path;
		
		if (path != null) {
			boolean first = true;
			for (String repeater : path) {
				if (!first) {
					System.out.print(',');
				}

				first = false;
				System.out.print(repeater);
			}
		}

		if (packet.payload != null) {
			System.out.print('\t');
			System.out.println(new String(packet.payload));
		}
	}
		
	public static void main(String args[]) {
		try {
			String input = System.getProperty("input");

			PacketHandler packetHandler = new PacketLogger();

			// TODO move all this into the PacketLogger class
			PacketDemodulator multi = new Afsk1200MultiDemodulator(RATE, packetHandler);
			Afsk1200Modulator mod = new Afsk1200Modulator(RATE);
			
			Soundcard sc = new Soundcard(RATE,input, null,BUFFER_SIZE, multi,mod);
		    sc.receive();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
