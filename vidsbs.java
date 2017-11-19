package helper.vid;

import java.io.*;
import java.net.URL;
import java.util.Date;

import helper.exp.dreidexception;
import helper.dreidabstract;
import helper.exp.dreidgenexp;

public class vidsbs extends dreidabstract {

	public void fillinfo() {
		isvideogen = true;  // = false;
		gentype = sbs;   // = dreidinterface.vr;
		prettyname = "Simple Cardboard to sbs Video Converter"; // = "3D Abstract";
		version = "0.1"; // = "0.0.1";
		waittime = 300;
	}

	public vidsbs getInst() { vidsbs g = new vidsbs(); g.fillinfo(); return g; }

	public static void main(String[] args) {
		File f = new File(args[1]);
		try {
			FileOutputStream o = new FileOutputStream(f);
			byte[] b = new byte[20000];
			vidsbs v = new vidsbs();
			InputStream j = v.generate(args[0],null, 100);
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
		}
	}

	public void doPara(String[] para) throws dreidexception {
		if ( para != null && para.length > 0 ) ffmpeg = para[0];
	};

	public File doGen(Object in, String para[], int dur) throws dreidexception {
		try {
			waittime = dur;
			URL i = new URL(in.toString());
			File f3 = File.createTempFile("stereogensbs-", ".webm");
			ProcessBuilder p = new ProcessBuilder(ffmpeg, "-i", in.toString(), "-codec", "copy", "-aspect", "16:9", f3.getAbsoluteFile().toString());
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