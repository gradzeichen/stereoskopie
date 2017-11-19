package helper.img;

import helper.dreidinterface;
import helper.exp.dreidunexpectedexp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Date;

public class genapp implements dreidinterface {

	public static void main(String[] args) {
		File f = new File(args[1]);
		byte[] b = null;
		try {
			FileOutputStream o = new FileOutputStream(f);
			genapp g = new genapp();
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
			System.out.println("fehler "  + new Date());
			e.printStackTrace();
			if ( b != null && b.length < 1000 ) System.out.println(new String(b));
		}
	}

	public genapp getInst() { return new genapp(); }

	@Override
	public String setPara(String para) {
		return para;
	}

	@Override
	public InputStream generate(String file, String para, int d) throws dreidunexpectedexp {
		URL u = null;
		byte[] ba = new byte[0];
		try {
			InputStream in;
			if ( file.startsWith("http") ) {
				u = new URL(file + para);
				in = u.openStream();
			}
			else in = new FileInputStream(new File(file + para));
			return in;
		}
		catch  ( Exception e ) {
			throw new dreidunexpectedexp();
		}
	}

	@Override
	public int getInfo() {
		return video - video + app;
	}
}
