package helper.img;

import helper.dreidabstract;
import helper.dreidinterface;
import helper.exp.dreidunexpectedexp;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;

public class genana extends dreidabstract {

	public void fillinfo() {
		prettyname = "Generate an Anaglyph - naive Implementation";
		version = "0.0.1";
		isvideogen = false;
		gentype = dreidinterface.ana;
	}

	private static int scolor  = 10;
	private static int stint   = 10;
	private int color  = scolor;
	private int tint   = stint;

	public genana getInst() { genana g = new genana(); g.fillinfo(); return g; }

	public static void main(String[] args) {
		File f = new File(args[3]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			genana g = new genana();
			g.setPara(args[0] + "," + args[1]);
			InputStream j = g.generate(args[0],"FF0000,0000FF", 0);
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
			System.out.println("fehler " + new Date());
			e.printStackTrace();
		}
	}

	@Override
	public void doPara(String[] para) {
		try {
			scolor     = Integer.parseInt(para[0]);
			stint      = Integer.parseInt(para[1]);
		}
		catch ( Exception e ) {}
	}

	public File doGen(Object oo, String[] para, int d) throws dreidunexpectedexp {
		int h = 0;
		byte[] ba = null;
		int ec = -1;
		String es = null;
		File f1 = null;
		File f2 = null;
		File f3 = null;
		File f4 = null;
		File f5 = null;
		color = scolor;
		tint  = stint;
		try {
			BufferedImage bi = (BufferedImage)oo;
			String pa1 = "123456";
			String pa2 = "654321";
			try {
				pa1      = para[0];
				pa2      = para[1];
				color   = Integer.parseInt(para[2]);
				tint    = Integer.parseInt(para[3]);
			}
			catch ( Exception e ) {}
			h = bi.getHeight();
			int w = bi.getWidth()/2;
			BufferedImage bn = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
			Graphics g = bn.getGraphics();
			f1 = File.createTempFile("genanatmpfil1-",".png");
			f2 = File.createTempFile("genanatmpfil2-",".png");
			f3 = File.createTempFile("genanatmpfil3-",".png");
			f4 = File.createTempFile("genanatmpfil4-",".png");
			f5 = File.createTempFile("genanatmpfil5-",".png");
			System.out.println(f3.getAbsoluteFile());
			ec = 40;
			g.drawImage(bi.getSubimage(0,0,w,h),0, 0,null);
			g = bi.getGraphics();
			BufferedImage bx = bi;
			bi = bx.getSubimage(w,0, w,h);
			ec = 60;
			ImageIO.write( bn,"png",new FileOutputStream(f1));
			ec = 80;
			ImageIO.write( bi,"png",new FileOutputStream(f2));
			ProcessBuilder p = new ProcessBuilder( convert, f1.getAbsoluteFile().toString(), "-fill", "#" + pa1, "-colorize", "" + color, "-tint", "" + tint, f3.getAbsoluteFile().toString());
			if ( tmpdir != null ) p.environment().put("MAGICK_TMPDIR",tmpdir);
			Process pp = p.start();
			ProcessBuilder p1 = new ProcessBuilder( convert, f2.getAbsoluteFile().toString(), "-fill", "#" + pa2, "-colorize", "" + color, "-tint", "" + tint, f4.getAbsoluteFile().toString());
			if ( tmpdir != null ) p1.environment().put("MAGICK_TMPDIR",tmpdir);
			Process p2 = p1.start();
			boolean bln = true;
			while ( bln ) {
				try {
					pp.exitValue();
					p2.exitValue();
					bln = false;
				}
				catch ( Exception e ) {
					Thread.sleep(100);
				}
			}
			p = new ProcessBuilder( composite, f3.getAbsoluteFile().toString(),  f4.getAbsoluteFile().toString(), "-blend", "50", f5.getAbsoluteFile().toString());
			if ( tmpdir != null ) p.environment().put("MAGICK_TMPDIR",tmpdir);
			pp = p.start();
			return f5;
		}
		catch ( Throwable e ) {
			StackTraceElement[] f = e.getStackTrace();
			int v = -3;
			if ( f.length > 0 )	v = f[0].getLineNumber();
			e.printStackTrace();
			String s = e.toString() + " - " + para + " line " + v + " ec " + ec + " es " + es;
			dreidunexpectedexp w = new dreidunexpectedexp();
			w.reason = s;
			throw w;
		}
		finally {
			if ( f1 != null ) f1.delete();
			if ( f2 != null ) f2.delete();
			if ( f3 != null ) f3.delete();
			if ( f4 != null ) f4.delete();
			if ( f5 != null ) f5.delete();
		}
	}
}