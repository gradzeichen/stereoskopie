package helper.img;

import helper.dreidabstract;
import helper.dreidinterface;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class genttb extends dreidabstract {

	public void fillinfo() {
		isvideogen = false;
		gentype = ttb;
		prettyname = "Simple top and bottom conversion";
		version = "0.1";
	}

	public void doPara(String[] p) {};

	public genttb getInst() { genttb g = new genttb(); g.fillinfo(); return g; }

	public static void main(String[] args) {
		File f = new File(args[1]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			genttb g = new genttb();
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

	@Override
	public ByteArrayOutputStream doGen(Object oo, String[] para, int d) {
		BufferedImage bi = (BufferedImage) oo;
		int h = 0;
		byte[] ba = null;
		int ec = -1;
		String es = null;
		try {
			ec = 20;
			h = bi.getHeight();
			int w = bi.getWidth();
			BufferedImage bn = new BufferedImage(w/2,h*2,BufferedImage.TYPE_INT_RGB);
			ec = 30;
			Graphics g = bi.getGraphics();
			ec = 40;
			g.drawImage(bi.getSubimage(0,0,w/2,h), 0, 0,null);
			g.drawImage(bi.getSubimage(w/2  ,0,w/2,h),    0,     h,null);
			ec = 70;
			ByteArrayOutputStream bs = null;
			bs = new ByteArrayOutputStream(w*100);
			ec = 80;
			ImageIO.write( bn,"jpg",bs);
			return bs;
		}
		catch ( Throwable e ) {
			StackTraceElement[] f = e.getStackTrace();
			int v = -3;
			if ( f.length > 0 )	v = f[0].getLineNumber();
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			try { bs.write((e.toString() + " - " + oo + " line " + v + " ec " + ec + " es " + es).getBytes()); } catch ( Exception t ) {};
			return bs;
		}
	}
}
