package helper.img;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import helper.dreidabstract;
import helper.dreidinterface;
import helper.exp.dreidgenexp;

public class gensbs extends dreidabstract {

	public void fillinfo() {
		prettyname = "Generate SBS 3DTV - naive Implementation";
		version = "0.0.1";
		isvideogen = false;
		gentype = dreidinterface.sbs;
	}

	public static void main(String[] args) {
		File f = new File(args[1]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			gensbs g = new gensbs();
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

	public gensbs getInst() { gensbs g = new gensbs(); g.fillinfo(); return g; }

	@Override
	public void doPara(String[] para) {}

	@Override
	public ByteArrayOutputStream doGen(Object oo, String[] para, int d) throws dreidgenexp {
		BufferedImage bi = (BufferedImage) oo;
		int h = 0;
		byte[] ba = null;
		int ec = -1;
		String es = null;
		try {
			ec = 20;
			h = bi.getHeight()*2;
			int w = bi.getWidth();
			Image i = bi.getScaledInstance(w,h,BufferedImage.SCALE_FAST);
			es = "scaled sbs " + w + " " + h + " ";
			es += i.toString() + " ";
			es += i.getHeight(null) + " ";
			es += i.getWidth(null);
			bi = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
			ec = 30;
			Graphics g = bi.getGraphics();
			ec = 40;
			g.drawImage(i, 0, 0,null);
			ec = 50;
			double r = (double)w/(double)h;
			int zw = w;
			int zh = h;
			int px = 0;
			int py = 0;
			int wh = w/2;
			double sn = 16d/9d;
			if ( r > sn + 0.01 ) {
				zh = (int)(zw * sn);
				py = ((zh/2) - (h/2))/2;
			}
			else if ( r < sn - 0.01 ) {
				zw = (int) (zh * sn);
				px = ((zw/2) - (wh))/2;
			}
			es += " werte wh " + wh + " px " + px + " py " + py + " zh " + zh + " zw " + zw + " w " + w + " h " + h + " sn " + sn;
			BufferedImage bn = new BufferedImage(zw,zh,BufferedImage.TYPE_INT_RGB);
			g = bn.getGraphics();
			g.setColor(Color.BLACK);
			ec = 60;
			g.fillRect(0,0,zw,zh);
			g.drawImage(bi.getSubimage(0  ,0,wh,h),    px,     py,null);
			g.drawImage(bi.getSubimage(wh,0,wh,h),px + (zw/2), py,null);
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
			dreidgenexp g = new dreidgenexp();
			g.reason = bs.toString();
			throw g;
		}
	}
}
