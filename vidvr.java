package helper.vid;

import java.io.*;
import java.net.URL;
import java.util.Date;

import helper.exp.dreidexception;
import helper.dreidabstract;
import helper.exp.dreidgenexp;

public class vidvr extends dreidabstract {

	public void fillinfo() {
		isvideogen = true;  // = false;
		gentype = vr;    // = dreidinterface.vr;
		prettyname = "Simple SBS to Cardboard Video Converter"; // = "3D Abstract";
		version = "0.1"; // = "0.0.1";
		waittime = 90;
	}

	public vidvr getInst() { vidvr g = new vidvr(); g.fillinfo(); return g; }

	public static void main(String[] args) {
		File f = new File(args[1]);
		try {
			FileOutputStream o = new FileOutputStream(f);
			byte[] b = new byte[20000];
			vidvr v = new vidvr();
			v.fillinfo();
			InputStream j = v.generate(args[0],null, 100);
			int a = 0, c = 0;
			System.out.println("main genrtd a " + a + " " + new Date());
			a = j.read(b,c,b.length-c);
			System.out.println("main genrtd b " + a + " " + new Date());
			while ( a > 0 ) {
				o.write(b,0,a);
				System.out.println("main fout " + a + " " + new Date());
				a = j.read(b,c,b.length-c);
			}
			System.out.println("main genrtd c " + a + " " + new Date());
			o.flush();
			o.close();
			System.out.println("main genrtd d " + a + " " + new Date());
		}
		catch ( Exception e) {
			System.out.println("fehler" + new Date());
			e.printStackTrace();
		}
	}

	public void doPara(String[] para) throws dreidexception {
		if ( para != null && para.length > 0 ) ffmpeg = para[0];
	};

	public File doGen(Object in, String para[], int dur) throws dreidexception {
		try {
			waittime = dur;
			URL i = new URL(in.toString());
			File f3 = File.createTempFile("stereogenvr-", ".webm");
			ProcessBuilder p = new ProcessBuilder(ffmpeg, "-i", in.toString(), "-codec", "copy", "-aspect", "32:9", f3.getAbsoluteFile().toString());
			// if ( tmpdir != null ) p.environment().put("MAGICK_TMPDIR",tmpdir);
			p.redirectOutput(f3);
			p.start();
			return f3;
		}
		catch ( Exception e ) {
			throw new dreidgenexp();
		}
	}
}
