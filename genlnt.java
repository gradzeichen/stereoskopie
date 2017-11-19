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
import java.util.StringTokenizer;

public class genlnt extends dreidabstract {

	public void fillinfo() {
		prettyname = "Generate lenticular - naive Implementation";
		version = "0.0.1";
		isvideogen = false;
		gentype = dreidinterface.lent;
	}

	public static void main(String[] args) {
		File f = new File(args[1]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			genlnt g = new genlnt();
			InputStream j = g.generate(args[0],"30,2000", 0);
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

	public genlnt getInst() { genlnt g = new genlnt(); g.fillinfo(); return g; }

	@Override
	public void doPara(String[] para) {}

	@Override
	public ByteArrayOutputStream doGen(Object oo, String[] para, int dur) {
		BufferedImage bi = (BufferedImage) oo;
		int h = 0, cols = 1, wid = 1280, hi = 720;
		byte[] ba = null;
		int ec = -1;
		String es = null;
		try {
			cols = Integer.parseInt(para[0]);
			wid  = Integer.parseInt(para[1]);
		}
		catch ( Throwable t ) {}
		try {
			ec = 20;
			h = bi.getHeight();
			int wid2 = bi.getWidth();
			double d = (double)h/(double)wid2;
			hi = ((int)(d*h));
			ec = 30;
			wid2 = wid * 2;
			Image im = bi.getScaledInstance(wid2,hi,0);
			ec = 40;
			bi = new BufferedImage(wid2,hi,BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();
			g.drawImage(im,0,0,null);
			BufferedImage bn = new BufferedImage(wid,hi,BufferedImage.TYPE_INT_RGB);
			g = bn.getGraphics();
			ec = 50;
			h = cols * 2;
			g.drawImage(bi.getSubimage(0,0,wid,hi), 0,0,null);
			ec = 55;
			int j = 0;
			BufferedImage bv;
			for ( int i = wid; i + cols <= wid2; i += h ) {
				bv = bi.getSubimage(i, 0, cols, hi);
				ec++;
				g.drawImage(bv, j + cols, 0, null);
				j += h;
				ec++;
			}
			ec = 70;
			ByteArrayOutputStream bs = null;
			bs = new ByteArrayOutputStream(wid*100);
			ec = 80;
			ImageIO.write( bn,"png",bs);
			return bs;
		}
		catch ( Throwable e ) {
			StackTraceElement[] f = e.getStackTrace();
			int v = -3;
			if ( f.length > 0 )	v = f[0].getLineNumber();
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			try { bs.write((e.toString() + " - " + oo + " - " + para + " line " + v + " ec " + ec + " es " + es).getBytes()); } catch ( Exception t ) { }
			return bs;
		}
	}
}
