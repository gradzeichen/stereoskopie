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

public class genpol extends dreidabstract {

	public void fillinfo() {
		prettyname = "Generate polarized - naive Implementation";
		version = "0.0.1";
		isvideogen = false;
		gentype = dreidinterface.pol;
	}

	public static void main(String[] args) {
		File f = new File(args[1]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			genpol g = new genpol();
			InputStream j = g.generate(args[0],"links", 0);
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
			System.out.println("fehler "  + new Date());
			e.printStackTrace();
			if ( b != null && b.length < 1000 ) System.out.println(new String(b));
		}
	}

	public genpol getInst() { genpol g = new genpol(); g.fillinfo(); return g; }

	@Override
	public void doPara(String[] para) {}

	@Override
	public ByteArrayOutputStream doGen(Object oo, String[] para, int d) throws dreidunexpectedexp {
		BufferedImage bi = (BufferedImage) oo;
		int h = 0;
		h = bi.getHeight();
		int w = bi.getWidth();
		BufferedImage bn = new BufferedImage(w/2,h,BufferedImage.TYPE_INT_RGB);
		Graphics g = bn.getGraphics();
		g.drawImage(bi.getSubimage(( para[0].equals("links") ? 0 : w/2)  ,0,w/2,h),    0,     0,null);
		ByteArrayOutputStream bs = null;
		bs = new ByteArrayOutputStream(w*100);
		try {
			ImageIO.write(bn, "jpg", bs);
		}
		catch ( Exception t ) {
			throw new dreidunexpectedexp();
		}
		return bs;
	}
}
