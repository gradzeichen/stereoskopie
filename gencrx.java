package helper.img;

import helper.dreidabstract;
import helper.dreidinterface;
import helper.exp.dreidunexpectedexp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class gencrx extends dreidabstract {

	public void fillinfo() {
		isvideogen = false;
		gentype = crx;
		prettyname = "Simple crossed eye conversion";
		version = "0.1";
	}

	public gencrx getInst() { gencrx g = new gencrx(); g.fillinfo(); return g; }

	public static void main(String[] args) {
		File f = new File(args[1]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			gencrx g = new gencrx();
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
	public void doPara(String[] para) {}

	@Override
	public Object doGen(Object oo, String[] para, int d) throws dreidunexpectedexp {
		int h = 0;
		int ec = -1;
		String es = null;
		BufferedImage bi = null;
		try {
			ec = 20;
			bi = (BufferedImage)oo;
			h = bi.getHeight();
			int w = bi.getWidth();
			BufferedImage bn = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
			ec = 30;
			Graphics g = bi.getGraphics();
			ec = 40;
			g.drawImage(bi.getSubimage(0,0,w/2,h), w/2, 0,null);
			g.drawImage(bi.getSubimage(w/2  ,0,w/2,h),    0,     0,null);
			ec = 70;
			return bn;
		}
		catch ( Throwable e ) {
			StackTraceElement[] f = e.getStackTrace();
			int v = -3;
			if ( f.length > 0 )	v = f[0].getLineNumber();
			dreidunexpectedexp u = new dreidunexpectedexp();
			u.reason = e.toString() + " - " + bi + " - " + para + " line " + v + " ec " + ec + " es " + es;
			throw u;
		}
	}
}
