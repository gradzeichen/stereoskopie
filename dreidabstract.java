package helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import helper.exp.dreidexception;
import helper.exp.dreidgenexp;
import helper.exp.dreidunexpectedexp;

/**
 * Created by g on 05.17.
 */
public abstract class dreidabstract implements dreidinterface, Runnable {

	public boolean isvideogen; // = false;
	public int     gentype;    // = dreidinterface.vr;
	public String  prettyname; // = "3D Abstract";
	public String  version;    // = "0.0.1";
	public int     waittime = 90;
	public int	   cahun    = -1;

	public abstract void doPara(String[] para) throws dreidexception;

	public abstract Object doGen(Object o, String para[], int duration) throws dreidexception;

	public abstract void fillinfo();

	public String paraStr(String[] para) {
		String s = "";
		if ( para == null || para.length == 0 ) return s;
		for ( int i = 0; i < para.length; i++ ) s += "." + para[i];
		return s;
	}

	final static String abstversion = "0.1";
	private InputStream     o       = null;
	private String          s       = null;
	private File            f       = null;
	private BufferedImage   bi      = null;
	private boolean         ready   = false;
	private cach            ca      = null;
	static int              stdhun  = 100;
	private static Vector<String>  cn = new Vector<String>();
	private static Vector<cach>    cc = new Vector<cach>();
	public static String convert    = "convert";
	public static String exiftool   = "exiftool";
	public static String ffmpeg     = "ffmpeg";
 	public static String tmpdir     = "/tmp/";
	public static String composite  = "composite";
	public static String mktags     = "MKtags";

	public final static void init(String im, String co, String tm, String et, String ff, String mk) {
		if ( im != null ) convert   = im;
		if ( co != null ) composite = co;
		if ( tm != null ) tmpdir    = tm;
		if ( et != null ) exiftool  = et;
		if ( ff != null ) ffmpeg    = ff;
		if ( mk != null ) mktags    = mk;
	}
	@Override
	public final String setPara(String para) {
		if ( para == null || para.length() == 0 ) {
			makeCache(stdhun);
			return "";
		}
		StringTokenizer t = new StringTokenizer(para," ,;:/");
		String s = t.nextToken();
		Vector<String> v = new Vector<String>();
		try {
			int i = Integer.parseInt(s);
			if ( i >= 0 ) makeCache(i);
			while ( t.hasMoreTokens() ) v.add(t.nextToken());
		}
		catch ( Exception e ) {
			v.add(s);
		}
		String[] o = new String[v.size()];
		for ( int i = 0; i < o.length; i++ ) o[i] = v.get(i);
		try {
			doPara(o);
			return para;
		}
		catch ( Exception e ) {
			// besser!
			return e.toString();
		}
	}
	@Override
	public final InputStream generate(String file, String para, int duration) throws dreidgenexp {
		String[] p = new String[0];
		if ( para != null && para.length() > 0 ) {
			StringTokenizer t = new StringTokenizer(para, " ,;:/");
			Vector<String> v = new Vector<String>();
			while (t.hasMoreTokens()) v.add(t.nextToken());
			String[] o = new String[v.size()];
			for (int i = 0; i < o.length; i++) o[i] = v.get(i);
		}
		byte[] b = getCache(file,p);
		try {
			if ( b != null ) {
				ByteArrayInputStream o = new ByteArrayInputStream(b);
				System.out.println("from cache");
				return o;
			}
			else {
				Object m = null;
				if ( isvideogen ) m = file;
				else m = getImage(file);
				System.out.println("dogen " + new Date());
				m = doGen(m, p, duration);
				s = file + paraStr(p);
				System.out.println("done gen " + new Date());
				if ( m.getClass() == File.class ) {
					f = (File)m;
					int cc = 0;
					while ( f.length() == 0 && cc < 5000 ) { try { Thread.sleep(100); cc++; } catch ( Exception e ) {};  };
					o = new FileInputStream(f);
					System.out.println("out:: file: " + o);
					new Thread(this).start();
					System.out.println("run " + new Date());
					return new FileInputStream(f);
				}
				else if ( m.getClass() == BufferedImage.class ) {
					bi = (BufferedImage) m;
					System.out.println("out:: image: " + bi.toString());
					new Thread(this).start();
					return getos();
				}
				else if ( m.getClass() == ByteArrayOutputStream.class ) {
					byte[] ba = ((ByteArrayOutputStream)m).toByteArray();
					o = new ByteArrayInputStream(ba);
					System.out.println("out:: byte: " + ba.toString() + " length " + ba.length);
					new Thread(this).start();
					return new ByteArrayInputStream(ba);
				}
				else {
					dreidunexpectedexp e = new dreidunexpectedexp();
					System.out.println("out:: exp: " + m.getClass());
					e.reason = "unexpected class returned: " + m.getClass();
					throw e;
				}
			}
		}
		catch ( Exception e ) {
			// besser
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(bs));
			dreidgenexp g = new dreidgenexp();
			g.reason = bs.toString();
			System.out.println(e + ":: " + g.reason);
			e.printStackTrace();
			throw g;
		}
	}
	@Override
	public final int getInfo() {
		if ( prettyname == null ) fillinfo();
		if ( isvideogen ) return dreidinterface.video + gentype;
		return gentype;
	}
	private final BufferedImage getImage(String file) throws dreidgenexp {
		BufferedImage bi = null;
		try {
			if (file.startsWith("http")) {
				URL u = new URL(file);
				bi = ImageIO.read(u);
			}
			else
				bi = ImageIO.read((new File(file)));
		}
		catch ( Exception e ) {
			// besser!
			// e.printStackTrace();
			helper.exp.dreidgenexp g = new dreidgenexp();
			g.reason = "Generate: File not found: " + file;
			System.out.println(g.reason);
			throw g;
		}
		return bi;
	}
	public final void makeCache(int i) {
		fillinfo();
		int ind = -1;
		for ( int ii = 0; ii < cn.size(); ii++ ) {
			if ( cn.get(ii).equals(prettyname) )
			{
				ind = ii;
			}
		}
		if ( ind == -1 ) {
			cn.add(prettyname);
			cc.add(new cach());
			ind = cn.size() - 1;
		}
		ca = cc.get(ind);
		if  ( i < 1 ) ca = null;
		else if ( ca == null || ca.hun != i  ) {
			ca = new cach();
;			ca.hun = i;
			ca.cache = new byte[ca.hun][];
			ca.names = new String[ca.hun];
			ca.next  = 0;
		}
		cc.set(ind,ca);
		if ( cahun == -1 ) cahun = i;
		System.out.println("Cache made #" + ind + " " + prettyname);
	}
	private final byte[] getCache(String file, String[] para) {
		if ( ca == null )
			for ( int ii = 0; ii < cn.size(); ii++ ) {
				if ( cn.get(ii).equals(prettyname) )
				{
					ca = cc.get(ii);
					break;
				}
			}
		System.out.println("search cache for " + file + " " + ( ca != null ? ca.next + "/" + ca.hun : "ca is null" ) );
		if ( ca != null ) for ( int i = 0; i < ca.hun; i++ ) if ( ca.names[i] != null && ca.names[i].equals(file + paraStr(para)) ) return ca.cache[i];
		System.out.println("cache miss");
		return null;
	}
	private final InputStream getos() {
		while ( !ready ) {
			try {
				Thread.sleep(200);
			}
			catch ( Exception e ) {
				System.out.println("dreidabstract: sleep exp " + new Date());
			}
		}
		return o;
	}
	public final void run() {
		if ( o != null ) {
			if ( ca != null && ca.hun > 0) {
				byte[] b = new byte[1000000], b1 = new byte[0];
				int a = 0, c = 0;
				try {
					a = o.read(b, c, b.length - c);
					while (a > 0) {
						c += a;
						if (c >= b.length) {
							b1 = new byte[c + 1000000];
							for (a = 0; a < c; a++) b1[a] = b[a];
							b = b1;
							b1 = null;
						}
						a = o.read(b, c, b.length - c);
					}
					b1 = new byte[c];
					for (a = 0; a < c; a++) b1[a] = b[a];
					ca.cache[ca.next] = b1;
					ca.names[ca.next++] = s;
					if (ca.next >= ca.hun) ca.next = 0;
					System.out.println("cached: " + s + " length " + b1.length + " next " + ca.next);
				} catch (Exception e) {
					System.out.println("cache write: abstract.run " + e + " " + new Date());
					e.printStackTrace();
				}
			}

			if ( f != null )
				while ( !ready ) {
					try {
						Thread.sleep(waittime * 1000);
						f.delete();
						ready = true;
					}
					catch ( Exception e ) {
						System.out.println("dreidabstract: sleep exp " + new Date());
					}
			}
		}
		else {
			ByteArrayOutputStream bs = null;
			byte[] ba = null;
			try  {
				bs = new ByteArrayOutputStream(bi.getWidth()*100);
				ImageIO.write( bi,"jpg",bs);
				ba = bs.toByteArray();
				o = new ByteArrayInputStream(ba);
			}
			catch  ( Exception e ) {
				System.out.println("cache write: abstract.run bi " + e + " " + new Date());
				e.printStackTrace();
			}
			ready = true;
			if ( ca != null && ca.hun > 0 ) {
				ca.cache[ca.next] = ba;
				ca.names[ca.next++] = s;
				if ( ca.next >= ca.hun ) ca.next = 0;
			}
		}
	}
}
class cach {
	int      hun     = dreidabstract.stdhun;
	byte[][] cache   = null;
	String[] names   = null;
	int      next    = 0;

}