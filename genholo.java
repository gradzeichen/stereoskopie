package helper.img;

import helper.dreidabstract;
import helper.dreidinterface;
import helper.exp.dreidunexpectedexp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;

public class genholo extends dreidabstract {

	public void fillinfo() {
		prettyname = "Generate holo pyramid picture - naive Implementation";
		version = "0.0.1";
		isvideogen = false;
		gentype = dreidinterface.holo;
	}

	public genholo getInst() { genholo g = new genholo(); g.fillinfo(); return g; }

	public static void main(String[] args) {
		File f = new File(args[1]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			genholo g = new genholo();
			InputStream j = g.generate(args[0],"", 0);
			int a = 0, c = 0;
			a = j.read(b,c,b.length-c);
			while ( a > 0 ) {
				o.write(b,0,a);
				a = j.read(b,c,b.length-c);
			}
			o.flush();
			o.close();
			o.flush();
			o.close();
		}
		catch ( Exception e) {
			System.out.println("fehler " + new Date());
			e.printStackTrace();
			if ( b != null && b.length < 1000 ) System.out.println(new String(b));
		}
	}

	@Override
	public void doPara(String[] para) {}

	@Override
	public ByteArrayOutputStream doGen(Object oo, String[] para, int d) throws dreidunexpectedexp {
		BufferedImage bi = (BufferedImage) oo;
		int h = bi.getHeight();
		int w = bi.getWidth()/2;

		AffineTransform tx = AffineTransform.getScaleInstance(1 , -1 );
		tx.translate(0 , -h );
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage bb = bi.getSubimage(0 ,0,w,h);
		bb = op.filter(bb, null);
		BufferedImage bn = new BufferedImage(w+h,h+w,BufferedImage.TYPE_INT_RGB);
		Graphics g = bn.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,w+h,h+w);
		g.drawImage(bb,    0,     w,null);
		bb = bi.getSubimage(w ,0,w,h);
		tx = AffineTransform.getScaleInstance(-1 , 1 );
		tx.translate(-w , 0 );
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bb = op.filter(bb, null);
		tx = new AffineTransform();
		tx.rotate(Math.toRadians(90),(h+w)/2,(w+h)/2);
		((Graphics2D)g).setTransform(tx);
		g.drawImage(bb,0, 0,null);
		ByteArrayOutputStream bs = null;
		try  {
			bs = new ByteArrayOutputStream(w*100);
			ImageIO.write( bn,"jpg",bs);
			return bs;
		}
		catch  ( Exception e ) {
			throw new dreidunexpectedexp();
		}
	}
}
