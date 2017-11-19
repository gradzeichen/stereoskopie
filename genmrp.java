package helper.img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import helper.dreidabstract;
import helper.dreidinterface;

/**
 * Created by g on 05.17.
 */

public class genmrp extends dreidabstract {

	public void fillinfo() {
		prettyname = "Generate morphed picture - naive Implementation";
		version = "0.0.1";
		isvideogen = false;
		gentype = dreidinterface.morph;
	}

	private static int           ssteps = 1;
	private static int           sdelay = 5;
	private static int           sthumb = 0;
	private static String        water  = null;
	private static BufferedImage wateri = null;
	private int                  steps  = 1;
	private int                  delay  = 5;
	private int                  thumb  = 0;

	public static void main(String[] args) {
		File f = new File(args[1]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			genmrp g = new genmrp();
			InputStream j = g.generate(args[0],"", 0);
			int a = 0, c = 0;
			a = j.read(b,c,b.length-c);
			while ( a > 0 ) {
				o.write(b,0,a);
				a = j.read(b,c,b.length-c);
			}
			o.flush();
			o.close();
		}
		catch ( Exception e) {
			System.out.println("fehler" + new Date());
			e.printStackTrace();
			if ( b != null && b.length < 1000 ) System.out.println(new String(b));
		}
	}

	public genmrp getInst() { genmrp g = new genmrp(); g.fillinfo(); return g; }

	@Override
	public void doPara(String[] para) {
		thumb = sthumb;
		delay = sdelay;
		steps = ssteps;
		try {
			thumb   = Integer.parseInt(para[0]);
			steps   = Integer.parseInt(para[1]);
			delay   = Integer.parseInt(para[2]);
			water   = para[3];
			wateri  = null;
			tmpdir  = para[4];
			convert = para[5];
		}
		catch ( Exception e ) {}
	}

	@Override
	public ByteArrayOutputStream doGen(Object oo, String[] para, int d) {
		BufferedImage bi = (BufferedImage)oo;
		int h = 0;
		byte[] ba = null;
		int ec = -1;
		String es = null;
		File f1 = null;
		File f2 = null;
		File f3 = null;
		try {
			if ( para != null ) {
				int pl = para.length;
				try {
					if (pl > 0) thumb = Integer.parseInt(para[0]);
					if (pl > 1) steps = Integer.parseInt(para[1]);
					if (pl > 2) delay = Integer.parseInt(para[2]);
				}
				catch ( Exception e ) {}
				if ( pl > 3 ) {
					String water1 = para[3];
					if (water != null && water1 != null && water1.length() > 5 && !water.equals(water1)) {
						wateri = null;
					}
				}
				if ( pl > 4 ) tmpdir = para[4];
				if ( pl > 5 ) convert = para[5];
			}
			h = bi.getHeight();
			int w = bi.getWidth()/2;
			BufferedImage bn = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
			Graphics g = bn.getGraphics();
			f1 = File.createTempFile("genmrptmpfil1-",".png");
			f2 = File.createTempFile("genmrptmpfil2-",".png");
			f3 = File.createTempFile("genmrptmpfil2-",".gif");
			ec = 40;
			g.drawImage(bi.getSubimage(0,0,w,h),0, 0,null);
			g = bi.getGraphics();
			BufferedImage bx = bi;
			bi = bx.getSubimage(w,0, w,h);
			if ( thumb != 0 ) {
				int w1 = thumb;
				if ( w1 < 0 ) w1 *= -1;
				int h1 = bi.getHeight();
				h1 = (int)((double)w1 / (double)( (double)bi.getWidth() / (double)h1 ));
				Image im = bi.getScaledInstance(w1,h1,0);
				bi = new BufferedImage(w1,h1,BufferedImage.TYPE_INT_RGB);
				g = bi.getGraphics();
				g.drawImage(im,0,0,null);
				im = bn.getScaledInstance(w1,h1,0);
				bn = new BufferedImage(w1,h1,BufferedImage.TYPE_INT_RGB);
				g = bn.getGraphics();
				g.drawImage(im,0,0,null);
			}
			if ( thumb < 0 ) {
				if ( wateri == null ) {
					if ( water.startsWith("http") ) {
						URL u = new URL(water);
						wateri = ImageIO.read(u);
					}
					else wateri = ImageIO.read((new File(water)));
				}
				g = bi.getGraphics();
				g.drawImage(wateri,0,bi.getHeight()-wateri.getHeight(),null);
				g = bn.getGraphics();
				g.drawImage(wateri,0,bn.getHeight()-wateri.getHeight(),null);
			}
			ec = 60;
			ec = 80;
			ImageIO.write( bi,"png",new FileOutputStream(f2));
			FileInputStream fi = null;	
			if ( thumb >= 0 || steps != 0 || delay != 0  ) {
				ImageIO.write( bn,"png",new FileOutputStream(f1));
				ProcessBuilder p = new ProcessBuilder( convert, f1.getAbsoluteFile().toString(),  f2.getAbsoluteFile().toString(), "-morph", "" + steps, "-loop", "0", "-delay", "" + delay, f3.getAbsoluteFile().toString());
				if ( tmpdir != null ) p.environment().put("MAGICK_TMPDIR",tmpdir);
				Process pp = p.start();
				boolean bln = true;
				while ( bln ) {
					try {
						pp.exitValue();
						bln = false;
					}
					catch ( Exception e ) {
						Thread.sleep(200);
					}
				}
				ba = new byte[(int)f3.length()];
				fi = new FileInputStream(f3);
			}
			else {
				ImageIO.write( bn,"gif",new FileOutputStream(f1));
				ba = new byte[(int)f1.length()];
				fi = new FileInputStream(f1);
			}
			int i = 0, j = 0;
			while ( j < ba.length ) {
				i = fi.read(ba,j,ba.length-j);
				j += i;
			}
			ec = 90;
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			bs.write(ba);
			return bs;
		}
		catch ( Throwable e ) {
			StackTraceElement[] f = e.getStackTrace();
			int v = -3;
			if ( f.length > 0 )	v = f[0].getLineNumber();
			e.printStackTrace();
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			try { bs.write((e.toString() + " - " + oo + " - " + para + " line " + v + " ec " + ec + " es " + es).getBytes()); } catch ( Exception t ) {}
			return bs;
		}
		finally {
			if ( f1 != null ) f1.delete();
			if ( f2 != null ) f2.delete();
			if ( f3 != null ) f3.delete();
		}
	}
}
