import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import helper.dreidabstract;
import helper.dreidinterface;

abstract public class dreidhelper extends HttpServlet {

	static String HVERSION = "0.1a";

	static String dserver       = "http://lokalerserver.gradzeichen";
	static String api           = "http://api.gradzeichen";
	static String raw           = "http://raw.gradzeichen";
	static String locimg        = "http://img.gradzeichen/mediawiki/";

	// Properties to be overwritten in stereo.properties:

	static boolean dotest        = true;
	static boolean freeacc       = false;
	static boolean sldsdbg       = true;
	static boolean usetranscoded = true;

	static String sserver       = dserver     + ":8080/stereoskopie";
	static String aserver       = api         + "/mediawiki/api.php";

	static String  p1            = "anfang";
	static int     defaulttime   = 0;
	static String  nocomment     = "<!-- not defined -->";
	static String  iserver       = dserver;
	static String  slideprefix   = raw          + "/mediawiki/index.php?action=raw&title=";
	static String  imageprefix   = iserver     + "/mediawiki/";
	static String  picinfo       = imageprefix + "index.php/File:";
	static String  imgprf        = imageprefix + "images/thumb";
	static String  imgprf2       = imageprefix + "images";
	static String  imggen        = locimg      + "images";
	static String  bgimg         = "/4/4c/Stereoskopie-hintergrund.jpeg";
	static String  bgimg1        = "/c/cb/Stereoskopie-hintergrund-2.jpeg";
	static String  bgimg2        = "/a/a4/Stereoskopie-hintergrund-vrg.jpeg";
	static String  bgimg3        = "/3/3e/Stereoskopie-hintergrund-anaglyph.jpeg";
	static String  bgimg4        = "/2/2a/Stereoskopie-hintergrund-holo.jpeg";
	static String  bgimg5        = "/2/2a/Stereoskopie-hintergrund-poll.jpeg";
	static String  bgimg6        = "/2/2a/Stereoskopie-hintergrund-polr.jpeg";
	static String  bgimg7        = "/2/2a/Stereoskopie-hintergrund-prsm.jpeg";
	static String  imgch1        = "/a/a0/Xx.gif";
	static String  baustelle     = "https://upload.wikimedia.org/wikipedia/commons/b/b0/%D7%A4%D7%A8%D7%A1%D7%A4%D7%A7%D7%98%D7%99%D7%91%D7%94_-_%D7%94%D7%A1%D7%A2%D7%95%D7%93%D7%94_%D7%94%D7%90%D7%97%D7%A8%D7%95%D7%A0%D7%94.jpg";
	static String sizesprefix   = aserver + "?action=query&format=json&prop=imageinfo&iiprop=size|extmetadata&iimetadataversion=latest&titles=";
	static String transprefix   = "https://upload.wikimedia.org/wikipedia/commons/transcoded";
	static String license       = "MIT-Licence, cc-by-sa-4.0 for software and content by";
	static String notfound      = "File not found or not a stereoscopic file";
	static String notsupported  = "MPO format not supported, please convert to jps. Called method not supported, please use different method.";
	static String example       = "slideshow/user:G/1.js?section=Test&caption=Test+Slideshow";
	static String example1      = "G16_454597.jps.jpg?caption=Dem+Wahren+Schönen+Guten";
	static String sw            = "<a href=/stereoskopie/Stereoskopie.pdf>Manual</a> Software: <a href=/stereoskopie/Stereoskopie.jar>Desktop/Laptop</a> <a href=/stereoskopie/Stereoskopie.apk>Android</a> (TBA: iOS, WinMobile, Blackberry)<p>";
	static String vidinfo		= "Videos will only be displayed, if available online, use download otherwise.";
	static int    ersterMD      = 1;
	static int    zweiterMD     = 2;
	static long   statcycle     = 60 * 60 * 1000;
	static int    maxln         = 510;
	static int    maxny         = 49;
	static int    showli        = 5;
	static String generators    = "helper.img.gensbs:200,helper.img.genpol,helper.img.genholo:25,helper.img.genana," +
									"helper.img.genapp:25,helper.img.genmrp:25,helper.img.genttb,helper.img.gencrx,helper.vid.vidvr:5,helper.img.genlnt:25";
	static String scripts       = "alert('No Scripts found!');";
	// End of stereo.properties

	static int statcw = 0; static int statvr  = 0; static int statapp = 0; static int stattry = 0; static int statcrx  = 0; static int statdewp = 0; static int statfrwp = 0; static int    statless  = 0;
	static int statcm = 0; static int statwp  = 0; static int statpdf = 0; static int statana = 0; static int statttb  = 0; static int statzhwp = 0; static int statarwp = 0; static int    stathewp  = 0;
	static int statcx = 0; static int statmw  = 0; static int statcsv = 0; static int statdbg = 0; static int statget  = 0; static int stathome = 0; static int statviwp = 0; static int    statjpwp  = 0;
	static int statca = 0; static int statdl  = 0; static int statweb = 0; static int stathdr = 0; static int statnet  = 0; static int statcomm = 0; static int statwikt = 0; static int    statdata  = 0;
	static int statcp = 0; static int statde  = 0; static int statorg = 0; static int statmpo = 0; static int statnof  = 0; static int statvers = 0; static int statwkia = 0; static int    statmeta  = 0;
	static int statci = 0; static int statmr  = 0; static int statcom = 0; static int statfhd = 0; static int stathigh = 0; static int statkowp = 0; static int statbook = 0; static int    statother = 0;
	static int statct = 0; static int statlt  = 0; static int statpol = 0; static int statmid = 0; static int statholo = 0; static int statptwp = 0; static int statwcom = 0; static int    statslide = 0;
	static int statce = 0; static int statcs  = 0; static int statjar = 0; static int statapk = 0; static int stateswp = 0; static int statukwp = 0; static int statinfo = 0; static long   laststat  = Long.MIN_VALUE;
	static int statco = 0; static int stateu  = 0; static int statuhd = 0; static int statqhd = 0; static int statplwp = 0; static int statvoya = 0; static int statwiki = 0; static String anaglyph  = "";
	static int statcb = 0; static int statgen = 0; static int statpic = 0; static int statmax = 0; static int statsvwp = 0; static int statnews = 0; static int statworg = 0; static String unknown   = "";
	static int statcu = 0; static int statwde = 0; static int statsbs = 0; static int staterr = 0; static int statitwp = 0; static int statenwp = 0; static int statruwp = 0; static String sls       = "";
	static int statcl = 0; static int statvid = 0; static int statcmg = 0; static int statcmn = 0; static int statfawp = 0; static int statpost = 0; static int statcmdb = 0; static String res       = "";
	static int staten = 0; static int statset = 0; static int statsoc = 0; static int statsrc = 0; static int statsour = 0;

	private static String[] 	  aza = null;
	String				          enc;
	long				          time;
	boolean				          debug;
	String			  	          dbg;
	String				          lr;
	String				          mc;
	int					          uhd;
	int                           lenticularsiz = 1;
	static Properties	          p	        	= null;
	final static char	          g				= '=';
	final static DecimalFormat    dec 			= new DecimalFormat("00000");
	final static SimpleDateFormat dat 			= new SimpleDateFormat("YY-MM-DD_HH-mm-SS_");
	static dreidinterface sbspic = null; static dreidinterface polpic = null; static dreidinterface anapic = null; static dreidinterface holpic = null;
	static dreidinterface sbsvid = null; static dreidinterface mrppic = null; static dreidinterface crxpic = null; static dreidinterface cnvlnt = null;
	static dreidinterface vrgvid = null; static dreidinterface polvid = null; static dreidinterface anavid = null; static dreidinterface holvid = null;
	static dreidinterface appdld = null; static dreidinterface ltcpic = null; static dreidinterface ttbpic = null; static dreidinterface cnvsvg = null;

	void          jpeg           (HttpServletResponse Res, PrintStream ps, InputStream b, String colo, String t) {
		byte[] bx = new byte[100000];
		try {
			if (b == null ) {
				err502(Res, ps, "Image Content not found", null);
			} else {
				Res.setContentType("image/jpeg");
				Res.setHeader("Content-Location", colo + "." + ( t.equals("svg2jps") ? "svg.jps" : t + ".jpg") );
				System.out.println("do jpeg: " + b);
				int a = b.read(bx);
				int r = 50, s = 0;
				while ( a < 0 && s < 50 ) {
					try {
						Thread.sleep(r);
						ps.print("");
						System.out.print(" -> " + a);
						a = b.read(bx);
						if ( r < 10000 ) r += 100;
						else {
							r = 5000;
							s++;
						}
					} catch ( IOException io ) {
						a = 0;
					} catch ( Exception ee ) {
					}
				}
				while ( a >= 0 ) {
					System.out.print(a + " ");
					ps.write(bx,0,a);
					a = b.read(bx);
				}
				System.out.println("done");
			}
		} catch (Exception e) {
			err502(Res, ps, e.toString(), bx);
		}
	}
	void          lossless       (HttpServletResponse Res, PrintStream ps, InputStream b, boolean gif, String colo) {
		byte[] bx = new byte[5000];
		try {
			if (b == null ) {
				err502(Res, ps, "Image Content not found", null);
			} else {
				Res.setContentType("image/" + ( gif ? "gif" : "png" ) );
				Res.setHeader("Content-Location", colo + ( gif ? ".gif" : ".png") );
				int a = b.read(bx);
				while ( a > 0 ) {
					ps.write(bx, 0, a);
					a = b.read(bx);
				}
			}
		} catch (Exception e) {
			err502(Res, ps, e.toString(), bx);
		}
	}
	void          webm           (HttpServletResponse Res, PrintStream ps, InputStream b, String ty, String filename) {
		byte[] bx = new byte[5000];
		try {
			if (b == null ) {
				err502(Res, ps, "Video Content not found", null);
			} else {
				Res.setContentType(ty);
				if ( filename != null ) Res.setHeader("Content-Location","3d-" + dat.format(new Date()) + filename);
				int a = b.read(bx);
				while ( a > 0 ) {
					ps.write(bx, 0, a);
					a = b.read(bx);
				}
			}
		} catch (Exception e) {
			ps.println(e.toString());
		}
	}
	String        info           (String p) {
		return "\"Stereoskopie\" is a means to display stereoscopic content from Wikimedia projects.<p>" +
				"The application provides the content in formats suitiable for 3DTV, 3Dready, " +
				"cardboard, VRglasses, morphed/prisma/crossed view, polarialisation based/anaglyphic/lenticular systems,  and " +
				"holographic pyramids. <a href=" + sserver + "/conf.html?setc=&>(Re)configure display options</a><p>" +
				"Licence: " + license + " <a href=https://meta.wikimedia.org/wiki/user:G style=\"font-family: UnifrakturMaguntia;\">Gradzeichen</a>." +
				"<p>Sources: <a href=https://www.github.com/gradzeichen/stereoskopie>available at Github</a> Examples: <a href=" + sserver + "/" + example + ">Slideshow</a>, <a href=" +
				sserver + "/" + example1 + ">Photo</a><p>" + sw + (p != null ? "<p>" + p : "");
	}
	String        setA           (String p, String cha, String cap) {
		p = sserver + "/" + p +"?";
		if ( cha != null ) p += "section=" + cha + "&";
		if ( cap != null ) p += "caption=" + cap + "&";
		String s = "You are about to watch stereoscopic content on Wikimedia.<p>Please choose your viewing option <small>(will be saved to a cookie)</small>:<span style=font-size: 1em;>" +
				"<br><img src=" + imgprf2 + imgch1 + " alt=\"Select method: sbs, cardboard, pol, anaglyph, download, holographic pyramid\" width=1380 height=240 usemap=\"#methodselection\">" +
				"<map name=\"methodselection\">" +
				"<area shape=\"rect\" coords=\"0,0,230,240\" href=" + p + "setc=sbs title=\"sbs, 3dtv\">" +
				"<area shape=\"rect\" coords=\"230,0,460,240\" href=" + p + "setc=cardboard title=\"vrglasses, cardboard\">" +
				"<area shape=\"rect\" coords=\"460,0,690,240\" href=" + p + "setc=pol title=\"polarized\">" +
				"<area shape=\"rect\" coords=\"690,0,920,240\" href=";
		if ( p.indexOf("/slideshow/") > -1 || p.indexOf("/s/") > -1 ) s += sserver + "/anachooser/slideshow" + p.substring(p.lastIndexOf('/'));
		else s += sserver + "/anachooser" + p.substring(p.lastIndexOf('/'));
		s += "setc=anaglyph title=\"anaglyph, prisma, morph, lenticular\">" +
				"<area shape=\"rect\" coords=\"920,0,1150,240\" href=" + p + "setc=download title=\"download, 3dready\">" +
				"<area shape=\"rect\" coords=\"1150,0,1380,240\" href=" + p + "setc=holopyramid title=\"holographic pyramid\">";
		return s + "</map><br>Set display: <a href=setR?setr=3840>UHD 4K</a> <a href=setR?setr=1920>FullHD</a> <a href=setR?setr=1280>HDready</a> " +
				"<a href=" + p + "setc=useapp>Local 3D app</a> <a href=" + p + "setw=links target=links>Use own window</a></span><p><a href=" + sserver + "/info>Info</a> " + sw;
	}
	String        anach          (String p, String cha) {
		return "<form method=get action=" + sserver + "/" + p + "><input type=hidden name=section>" + cha + "</input><table border=0><tr><td>Select the colors of your 3D glasses:<p>" +
				"<div style=\"font-size: 0.5em;\">Enter RGB as hex values</div>Left: " +
				"<input type=text name=analeft list=left><datalist id=left>" +
				"<option value=FF0000>Red</option><option value=00FF00>Green</option>" +
				"<option value=0000FF>Blue</option><option value=FF0000>Yellow</option><option>F0F000</option></datalist></input>" +
				" Right: <input type=text name=anaright list=right><datalist id=right><option value=FF0000>Red</option>" +
				"<option value=00FF00>Green</option><option value=0000FF>Blue</option><option value=FFFF00>Yellow</option><option>F0F000</option>" +
				"</datalist></input><p><div style=\"font-size: 0.5em;\">Or choose from color chooser</div>Left: <input type=color name=analeftc>" +
				" Right: <input type=color name=anarightc><td>Lenticular settings:<p>Column width:  <input type=text name=setL value=" + lenticularsiz +
				" list=lnt><datalist id=lnt><option value=1>Min</option><option>2</option><option>4</option><option value=384>Max</option></datalist></input>" +
				"<br>Picture width: <input type=text name=setR value=" + uhd + " list=setr><datalist id=setr><option value=160>min</option><option value=1280>HDready</option>" +
				"<option value=1920>FullHD</option><option value=2560>QHD 2K</option><option value=3840>UHD 4K</option><option value=10000>max</option></datalist></input>" +
				"<tr><td colspan=2><br>Download settings:<br><input type=checkbox>Short</input> <input type=checkbox>Licence texts</input> <input type=checkbox>Slide def</input> " +
				"<input type=checkbox>extmetadata</input><br><input type=checkbox>sbs/tab</input> <input type=checkbox>vr/card/jps</input> " +
				"<input type=checkbox>holo</input> <input type=checkbox>anaglyph</input> <input type=checkbox>lenticular</input> <input type=checkbox>morph</input><br>" +
				"Once: <input type=checkbox>Desktop/Laptop</input> <input type=checkbox>Android</input> <input type=checkbox>Manual</input>" +
				"<tr><td>Mode: <input type=text name=setA value=\"" + mc + "\" list=setc><datalist id=setc><option>vrglasses</option><option>sbs</option><option>pol</option>" +
				"<option>holopyramid</option><option>download</option><option>anaglyph</option><option>morph</option><option>lenticular</option><option value=cross>crossed eyes</option value=tab><option>prisma/tab</option><option>app</option></datalist></input>" +
				"<td><input type=submit value=\"Apply Settings\"></table></form><p>" + vidinfo;
	}
	String        mwurl          (String in, boolean enc) {
		if (in == null) return "";
		if ( enc ) in = in.trim().replace("%20", "_").replace(" ", "_").replace("+", "_");
		else       in = in.trim().replace("%20", " ").replace("_", " ").replace("+", " ");
		int i = in.indexOf('%');
		String a, b, c;
		boolean u = false;
		String in2 = in;
		while ( i > -1) {
			a = in.substring(0,i);
			b = in.substring(i + 1, i + 3);
			c = in.substring(i + 4);
			u = true;
			in = a + Integer.parseInt(c,16) + c;
		}
		if ( u ) {
			byte[] bx = new byte[in.length()];
			for ( int j = 0; j < bx.length; j++) bx[j] = (byte)in.charAt(j);
			try {
				in = new String(bx,"UTF-8");
			}
			catch ( Exception e ) {
				in = in2;
			}
			int ii = in.indexOf(0);
			if ( ii >= 0 ) in = in.substring(0,ii);
		}
		return in;
	}
	String        getUhd         () {
		return "Display set to " + (uhd == 1280 ? "HDready" : "" + (uhd == 1920 ? "FullHD" : (uhd == 3840 ? "" + "UHD 4K" : (uhd == 2960 ? "" + "QHD 2K" : uhd))))
				+ "  Mode.<p><a href=# onclick=history.back(); >Back</a><p>";
	};
	void          debug          (HttpServletRequest r, String str2) {
		Cookie[] c = r.getCookies();
		for ( int i = 0; i < c.length; i++ ) dbg += "<br>" + c[i].getName() + ": " + c[i].getValue();
		dbg += "<br>p1=" + p1 + "<br>";
	}
	String        getmd          (String f) {
		String s = "12345";
		byte[] b = s.getBytes();
		String h = null;
		int i = -1;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			b = md.digest(f.getBytes());
			s = "";
			for (i = 0; i < zweiterMD; i++) {
				h = "00" + Integer.toHexString(b[i]);
				s += h.substring(h.length() - 2);
			}
		} catch (Exception e) {
			s += "--fehler--" + h + "--" + b.length + "--" + b[i];
			e.printStackTrace();
		}
		if (s.length() < zweiterMD) return "fehler-" + s.length() + "-" + b.length + "/";
		if (s.startsWith("--")) return s + "/";
		return s.substring(0, ersterMD) + ( ersterMD > 0 ? "/" : "") + s.substring(0, zweiterMD) + ( zweiterMD > 0 ? "/" : "");
	}
	int           eq             (String a, String b) {
		int i = 0;
		String a1 = "", b1 = "";
		i = a.lastIndexOf(':');
		if ( i > -1 ) a = a.substring(i+1);
		i = b.lastIndexOf(':');
		if ( i > -1 ) b = b.substring(i+1);
		if ( a.indexOf(".jps") > -1 && a.equals(b) ) return 9;
		if ( a.indexOf(".mpo") > -1 && a.equals(b) ) return 8;
		i = a.lastIndexOf('.');
		if ( i > -1 ) a = a.substring(0,i);
		i = b.lastIndexOf('.');
		if ( i > -1 ) b = b.substring(0,i);
		i = a.lastIndexOf('.');
		if ( i > -1 ) {
			a1 =a.substring(i).toLowerCase();
			a = a.substring(0,i);
		}
		i = b.lastIndexOf('.');
		if ( i > -1 ) {
			b1 =b.substring(i).toLowerCase();
			b = b.substring(0,i);
		}
		if ( a.equals(b) ) {
			if ( a1.equals(b1)          ) return 7;
			if ( b1.indexOf("tab") > -1 ) return 6;
			if ( b1.indexOf("sbs") > -1 ) return 3;
			if ( b1.indexOf("vr")  > -1 ) return 5;
			if ( b1.indexOf("ca")  > -1 ) return 4;
			return 0;
		}
		if ( a.equalsIgnoreCase(b) ) {
			if ( a1.equals(b1) || b1.indexOf("vr") > -1 || b1.indexOf("ca") > -1 || b1.indexOf("tab") > -1 ) return 2;
			return 1;
		}
		return -10;
	}
	d             e              (s z, s t, d c) {
		int a = 0, e = 0, i, y = z.l();
		for (i = 0; i < y; )
			if (z.c(i++) == g) a++;
			else break;
		for (i = y; i > 0; )
			if (z.c(--i) == g) e++;
			else break;
		if (a == e && (a == c.i || c.i < 0) && z.s(a, y - e).t().e(t)) {
			if (c.i == -2) {
				c.i = a;
				c.b = true;
			} else c.b = !c.b;
		} else if (c.b && a <= c.i) c.b = false;
		return c;
	}
	String[]      parseSlide     (String l) {
		int i = l.indexOf(']');
		StringTokenizer t = null;
		String e = null;
		if ( i > 0 ) {
			e = parseFile(l.substring(0,i));
			t = new StringTokenizer(l.substring(i), "|");
		}
		else {
			t = new StringTokenizer(l, "|");
			e = t.nextToken();
			e = parseFile(e);
		}
		if (e != null) {
			int z = defaulttime;
			String c = nocomment;
			if (t.hasMoreTokens()) z = getSecs(t.nextToken());
			if (t.hasMoreTokens()) c = getCom(t.nextToken());
			String[] s = {e, "" + z, c};
			return s;
		}
		return new String[0];
	}
	int           getSecs        (String in) {
		String s = "0";
		char c;
		int j = defaulttime;
		for (int i = 0; i < in.length(); i++) {
			c = in.charAt(i);
			if (Character.isDigit(c)) s += c;
			else if ( c == '.' || c == ',' ) i = in.length();
		}
		try {
			j = Integer.parseInt(s);
		}
		catch ( Exception e ) { j = -j; };
		return j;
	}
	String        getCom         (String in) {
		if (in == null) return nocomment;
		int i = Integer.MAX_VALUE, j = 0;
		while ( i > 0 ) {
			i = in.toLowerCase().indexOf("<a ");
			j = in.indexOf('>',i+1);
			if ( i > -1 ) in = in.substring(0,i) + in.substring(j+1);
		}
		return in.replace("[", "").replace("]", "").replace("</a>","")
				.replace("<", "&lt;").replace("http","h++p")
				.replace("&lt;span ", "<span ").replace("&lt;/span>", "</span>")
				.replace("&lt;!--", "<!--").trim();
	}
	String        parseFile      (String i) {
		i = i.trim();
		boolean bn = true;
		if (i.indexOf(".jps") > -1 || i.indexOf(".cardboard.") > -1 || i.indexOf(".vrglasses") > -1 || i.indexOf(".sbs.") > -1 || i.indexOf(".holopyramid.") > -1 || i.indexOf(".anaglyph.") > -1 || i.indexOf(".morph.") > -1 || i.indexOf(".tab.") > -1 ) {
			int a = i.indexOf("[[");
			if (a > -1) {
				int b = i.indexOf('|');
				if (b > 0) {
					i = i.substring(a + 2, b);
					bn = false;
				}
			}
			a = i.lastIndexOf(':');
			if (a > -1) {
				i = i.substring(a + 1);
				bn = false;
			}
			a = i.lastIndexOf('/');
			if (a > -1) {
				i = i.substring(a + 1);
				bn = false;
			}
			a = 0;
			char c;
			while (bn) {
				c = i.charAt(a);
				if (c == '*' || c == '#' || c == ' ' || c == '\t' || c == ';' || c == '/') a++;
				else bn = false;
			}
			return i.substring(a);
		}
		return null;
	}
	public void   doPost         (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		statpost++;
		doGet(req, res);
	}
	public long   getLastModified(HttpServletRequest Request) {
		if (Request != null && Request.getPathInfo() != null && Request.getPathInfo().startsWith("/gen/")) {
			statcmdb++;
			String d = Request.getParameter("debug");
			if ( d != null ) return System.currentTimeMillis();
			Cookie[] c = Request.getCookies();
			if ( c != null ) for ( int i = 0; i < c.length; i++ ) if ( c[i].getName().equals("stereodbg") && c[i].getValue().equals("true") ) return System.currentTimeMillis();
			statcmdb--; statcmg++;
			Date heute = new Date( ( new Date().getTime() / 1000 ) * 1000 );
			Calendar ca = new GregorianCalendar();
			ca.setTime(heute);
			ca.set(Calendar.MINUTE,1);
			ca.set(Calendar.SECOND,1);
			if ( Request.getPathInfo().startsWith("/gen/zip") ) return ca.getTime().getTime();
			ca.set(Calendar.YEAR,ca.get(Calendar.YEAR)-1);
			ca.set(Calendar.MONTH,1);
			ca.set(Calendar.DATE,1);
			ca.set(Calendar.HOUR,1);
			ca.set(Calendar.MINUTE,1);
			ca.set(Calendar.SECOND,1);
			if ( Request.getPathInfo().startsWith("/gen/mor") ) return ca.getTime().getTime();
			ca.setTime(heute);
			ca.set(Calendar.DATE,1);
			ca.set(Calendar.HOUR,1);
			ca.set(Calendar.MINUTE,1);
			ca.set(Calendar.SECOND,1);
			if ( Request.getPathInfo().startsWith("/gen/ana") || Request.getPathInfo().startsWith("/gen/len") ) return ca.getTime().getTime();
			ca.setTime(heute);
			ca.set(Calendar.HOUR,1);
			ca.set(Calendar.MINUTE,1);
			ca.set(Calendar.SECOND,1);
			return (ca.getTime().getTime() - (5 * 60 * 60 * 1000));
		}
		if ( Request != null ) statcmn++;
		return System.currentTimeMillis();
	}
	void          getProps       () {
		p = new Properties();
		try {
			p1 = getServletContext().getRealPath("") + "/stereo.properties";
			p.load(new FileInputStream(p1));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(p1);
			System.err.println(HVERSION);
		}
		p1 = p.getProperty("testprop", p1);
		slideprefix = p.getProperty("slideprefix", slideprefix);
		nocomment = p.getProperty("nocomment", nocomment);
		try {
			String x = p.getProperty("dotest",      "" + dotest );
			if ( x.equalsIgnoreCase("true") ) dotest = true; else dotest = false;
			x = p.getProperty("freeacc",      "" + freeacc );
			if ( x.equalsIgnoreCase("true") ) freeacc = true; else freeacc = false;
			x = p.getProperty("sldsdbg",      "" + sldsdbg );
			if ( x.equalsIgnoreCase("true") ) sldsdbg = true; else sldsdbg = false;
			x = p.getProperty("usetranscoded",      "" + usetranscoded );
			if ( x.equalsIgnoreCase("true") ) usetranscoded = true; else usetranscoded = false;
			defaulttime = Integer.parseInt(p.getProperty("defaulttime", "" + defaulttime));
			ersterMD    = Integer.parseInt(p.getProperty("mdeins",      "" + ersterMD));
			zweiterMD   = Integer.parseInt(p.getProperty("mdzwei",      "" + zweiterMD));
			maxln       = Integer.parseInt(p.getProperty("maxln",      "" + maxln ));
			maxny       = Integer.parseInt(p.getProperty("maxny",      "" + maxny ));
			showli      = Integer.parseInt(p.getProperty("showli",      "" + showli ));
			statcycle   = Integer.parseInt(p.getProperty("statcycle",   "" + statcycle ));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String az = p.getProperty("auth.anz", "-2");
		int azi = -1;
		int azj = -5;
		try {
			azi = Integer.parseInt(az);
			azj = -4;
			aza = new String[azi];
			azj = -3;
			for ( azj = 0; azj < azi; azj++ ) aza[azj] = p.getProperty("auth." + azj);
		} catch ( Exception e ) { System.out.println("auth laden gescheitert: " + azi + " " + azj + " " + new Date() ); };
		generators = p.getProperty("generators", generators);
		StringTokenizer t = new StringTokenizer(generators, ",");
		while (t.hasMoreTokens()) {
			String s = t.nextToken();
			StringTokenizer u = new StringTokenizer(s, ":");
			s = u.nextToken();
			String p = null;
			if (u.hasMoreTokens()) p = u.nextToken();
			try {
				Class c = Class.forName(s);
				dreidinterface d = (dreidinterface) c.newInstance();
				switch (d.getInfo()) {
					case dreidinterface.sbs:
						sbspic = d;
						System.out.print("is sbs ");
						break;
					case dreidinterface.pol:
						polpic = d;
						System.out.print("is pol ");
						break;
					case dreidinterface.ana:
						anapic = d;
						System.out.print("is ana ");
						break;
					case dreidinterface.app:
						appdld = d;
						System.out.print("is app ");
						break;
					case dreidinterface.holo:
						holpic = d;
						System.out.print("is holo ");
						break;
					case dreidinterface.morph:
						mrppic = d;
						System.out.print("is morph ");
						break;
					case dreidinterface.lent:
						ltcpic = d;
						System.out.print("is lent ");
						break;
					case dreidinterface.crx:
						crxpic = d;
						System.out.print("is crx ");
						break;
					case dreidinterface.ttb:
						ttbpic = d;
						System.out.print("is ttb ");
						break;
					case dreidinterface.sbs + dreidinterface.video:
						sbsvid = d;
						System.out.print("is sbs vid ");
						break;
					case dreidinterface.vr + dreidinterface.video:
						vrgvid = d;
						System.out.print("is vr vid ");
						break;
					case dreidinterface.pol + dreidinterface.video:
						polvid = d;
						System.out.print("is pol vid ");
						break;
					case dreidinterface.ana + dreidinterface.video:
						anavid = d;
						System.out.print("is ana vid ");
						break;
					case dreidinterface.holo + dreidinterface.video:
						holvid = d;
						System.out.print("is holo vid ");
						break;
					default:
						System.out.println("is not recognized " + d.getInfo());
				}
				System.out.println("got " + d);
				d.setPara(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		sserver      = p.getProperty("sserver",      sserver);
		aserver      = p.getProperty("aserver",      aserver);
		iserver      = p.getProperty("iserver",      iserver);
		imageprefix  = p.getProperty("imageprefix",  imageprefix);
		picinfo      = p.getProperty("picinfo",      picinfo);
		imgprf       = p.getProperty("imgprf",       imgprf);
		imgprf2      = p.getProperty("imgprf2",      imgprf2);
		imggen       = p.getProperty("imggen",       imggen);
		bgimg        = p.getProperty("bgimg",        bgimg);
		bgimg1       = p.getProperty("bgimg1",       bgimg1);
		bgimg2       = p.getProperty("bgimg2",       bgimg2);
		bgimg3       = p.getProperty("bgimg3",       bgimg3);
		bgimg4       = p.getProperty("bgimg4",       bgimg4);
		bgimg5       = p.getProperty("bgimg5",       bgimg5);
		bgimg6       = p.getProperty("bgimg6",       bgimg6);
		bgimg7       = p.getProperty("bgimg7",       bgimg7);
		imgch1       = p.getProperty("imgch1",       imgch1);
		baustelle    = p.getProperty("baustelle",    baustelle);
		sizesprefix  = p.getProperty("sizesprefix",  sizesprefix);
		transprefix  = p.getProperty("transprefix",  transprefix);
		license      = p.getProperty("license",      license);
		notfound     = p.getProperty("notfound",     notfound);
		notsupported = p.getProperty("notsupported", notsupported);
		example      = p.getProperty("example",      example);
		example1     = p.getProperty("example1",     example1);
		sw           = p.getProperty("sw",           sw);
		vidinfo      = p.getProperty("vidinfo",      vidinfo);
		dreidabstract.init( p.getProperty("convert", 	dreidabstract.convert ),
							p.getProperty("composite", dreidabstract.composite ),
							p.getProperty("tmpdir", 	dreidabstract.tmpdir ),
							p.getProperty("exiftool", 	dreidabstract.exiftool ),
							p.getProperty("ffmpeg", 	dreidabstract.ffmpeg ),
							p.getProperty("mktags", 	dreidabstract.mktags ) );
		try {
			LineNumberReader r = new LineNumberReader(new FileReader(getServletContext().getRealPath("") + "/stereo.js"));
			String tt = r.readLine();
			scripts = "const showli = " + showli + ";\n";
			int ii = 10000;
			while (tt != null /*&& !tt.equals("")*/) {
				scripts += tt + " // " + ++ii + "\n";
				tt = r.readLine();
			}
		} catch (Exception e) {
			System.out.println("Scripts lesen gescheitert  " + new Date());
			e.printStackTrace();
			System.err.println(HVERSION);
		}
	}
	String        head           (String t) {
		String s = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n<HTML><HEAD><TITLE>Stereo" +
				(mc.startsWith("p") ? " " : "skopie ")  +
				( lr == null || lr.length() == 0 ? "" : ( lr.equals("rchts") ? "(RIGHT)" : ( mc.startsWith("p") ? "(LEFT)" : "" ) ) ) + "[" + t + "]</TITLE>\n";
		return s + "<script language=javascript>" + scripts + "</script><style type=\"text/css\">" +
				"@font-face { font-family: 'UnifrakturMaguntia'; src: url('https://bits.wikimedia.org/static-current/extensions/UniversalLanguageSelector/data/fontrepo/fonts/UnifrakturMaguntia/UnifrakturMaguntia.woff') format('woff'); }" +
				"</style></HEAD>";
	}
	String        bg             (String pic) {
		return "<body style=\"" + (debug ? "" : "background-image: url(" + pic(pic) + "); ") + "" +
				( mc.startsWith("holo") ? ( mc.indexOf("upright") > -1 ? "background-size: cover; width: 100%; height: " + uhd + "; " : "width: " + uhd + "; height: " + uhd + "; " ) : "background-size: cover; width: 100%; height: 100%; " ) +
				"background-color: black; background-repeat: no-repeat; background-position: top; color: lightblue; border: none;\"><div style=\"font-size: 2em;\">";
	}
	String        pic            (String pic) {
		if ( pic == null || !pic.startsWith("http") ) {
			if ( mc == null || mc.startsWith("vr") || mc.startsWith("card") ) return imgprf2 + bgimg2;
			else if ( mc.startsWith("sbs") ) return imgprf2 + bgimg1;
			else if ( mc.startsWith("pol") ) {
				if ( lr != null && lr.equals("rechts") ) return imgprf2 + bgimg6;
				return imgprf2 + bgimg5;
			}
			else if ( mc.startsWith("holo") ) return imgprf2 + bgimg4;
			else if ( mc.startsWith("ana") ) return imgprf2 + bgimg3;
			else return imgprf2 + bgimg;
		}
		else return pic;
	}
	String        ft             (boolean med) {
		String s = (med ? "" : "<div style=\"color: gray; font-size: 0.5em;\">" + getServletInfo() +
				"<br>Background: Eakins: The Fairman Rogers Four-in-hand (A May Morning in the Park)</div>") +
				(debug ? "<p>" + dbg : "") + "</div></body></HTML>";
		return s;
	}
	String        inline         (String in) {
		return in.replace("\n","<br>").replace("'","&quote;").replace("\"","&dquote;");
	}
	svstream      getStream      (HttpServletResponse Response, String str1) throws IOException {
		Object lO = null;
		PrintStream lPS = null;
		if (str1.startsWith("gzip")) {
			Response.setHeader("Content-Encoding", "gzip");
			lO = new GZIPOutputStream(Response.getOutputStream());
			lPS = new PrintStream((OutputStream) lO, false, "UTF-8");
			this.enc = "g";
		} else if (str1.indexOf("deflate") > -1) {
			Response.setHeader("Content-Encoding", "deflate");
			lO = new DeflaterOutputStream(Response.getOutputStream());
			lPS = new PrintStream((OutputStream) lO, false, "UTF-8");
			this.enc = "d";
		} else {
			lPS = new PrintStream(Response.getOutputStream(), false, "UTF-8");
			this.enc = "n";
		}
		return new svstream(lPS, lO);
	}
	void          stat           () {
		String s = System.currentTimeMillis() + "";
		System.out.println("Schreibe stat" + new Date());
		try {
			FileWriter p = new FileWriter(getServletContext().getRealPath("") + "/stereo.stats",true);
			if (statenwp  > 0) { s += "en" + statenwp;  statenwp  = 0; }
			if (statpost  > 0) { s += "cy" + statpost;  statpost  = 0; }
			if (statcmdb  > 0) { s += "ck" + statcmdb;  statcmdb  = 0; }
			if (statcmg   > 0) { s += "cc" + statcmg;   statcmg   = 0; }
			if (statcmn   > 0) { s += "cz" + statcmn;   statcmn   = 0; }
			if (statget   > 0) { s += "cw" + statget;   statget   = 0; }
			if (statdewp  > 0) { s += "de" + statdewp;  statdewp  = 0; }
			if (statfrwp  > 0) { s += "fr" + statfrwp;  statfrwp  = 0; }
			if (statitwp  > 0) { s += "it" + statitwp;  statitwp  = 0; }
			if (stateswp  > 0) { s += "es" + stateswp;  stateswp  = 0; }
			if (statplwp  > 0) { s += "pl" + statplwp;  statplwp  = 0; }
			if (statviwp  > 0) { s += "vi" + statviwp;  statviwp  = 0; }
			if (statjpwp  > 0) { s += "jp" + statjpwp;  statjpwp  = 0; }
			if (statkowp  > 0) { s += "ko" + statkowp;  statkowp  = 0; }
			if (statruwp  > 0) { s += "ru" + statruwp;  statruwp  = 0; }
			if (statzhwp  > 0) { s += "zh" + statzhwp;  statzhwp  = 0; }
			if (statarwp  > 0) { s += "ar" + statarwp;  statarwp  = 0; }
			if (stathewp  > 0) { s += "he" + stathewp;  stathewp  = 0; }
			if (statsvwp  > 0) { s += "sv" + statsvwp;  statsvwp  = 0; }
			if (statptwp  > 0) { s += "pt" + statptwp;  statptwp  = 0; }
			if (statukwp  > 0) { s += "uk" + statukwp;  statukwp  = 0; }
			if (statfawp  > 0) { s += "fa" + statfawp;  statfawp  = 0; }
			if (statwp    > 0) { s += "wp" + statwp;    statwp    = 0; }
			if (statmeta  > 0) { s += "me" + statmeta;  statmeta  = 0; }
			if (statcomm  > 0) { s += "cm" + statcomm;  statcomm  = 0; }
			if (statwikt  > 0) { s += "wt" + statwikt;  statwikt  = 0; }
			if (statdata  > 0) { s += "dt" + statdata;  statdata  = 0; }
			if (statvoya  > 0) { s += "vy" + statvoya;  statvoya  = 0; }
			if (statnews  > 0) { s += "nw" + statnews;  statnews  = 0; }
			if (statbook  > 0) { s += "bk" + statbook;  statbook  = 0; }
			if (statsour  > 0) { s += "sr" + statsour;  statsour  = 0; }
			if (statvers  > 0) { s += "vk" + statvers;  statvers  = 0; }
			if (statmw    > 0) { s += "mw" + statmw;    statmw    = 0; }
			if (statwkia  > 0) { s += "wa" + statwkia;  statwkia  = 0; }
			if (statworg  > 0) { s += "wo" + statworg;  statworg  = 0; }
			if (statwcom  > 0) { s += "wc" + statwcom;  statwcom  = 0; }
			if (statwde   > 0) { s += "wd" + statwde;   statwde   = 0; }
			if (statwiki  > 0) { s += "wi" + statwiki;  statwiki  = 0; }
			if (statcom   > 0) { s += "cf" + statcom;   statcom   = 0; }
			if (statorg   > 0) { s += "or" + statorg;   statorg   = 0; }
			if (statde    > 0) { s += "dd" + statde;    statde    = 0; }
			if (statweb   > 0) { s += "we" + statweb;   statweb   = 0; }
			if (statvr    > 0) { s += "vr" + statvr;    statvr    = 0; }
			if (statsbs   > 0) { s += "sb" + statsbs;   statsbs   = 0; }
			if (statpol   > 0) { s += "po" + statpol;   statpol   = 0; }
			if (statdl    > 0) { s += "dl" + statdl;    statdl    = 0; }
			if (statholo  > 0) { s += "ho" + statholo;  statholo  = 0; }
			if (statana   > 0) { s += "an" + statana;   statana   = 0; }
			if (statpic   > 0) { s += "pi" + statpic;   statpic   = 0; }
			if (statvid   > 0) { s += "vj" + statvid;   statvid   = 0; }
			if (statslide > 0) { s += "sl" + statslide; statslide = 0; }
			if (statjar   > 0) { s += "jr" + statjar;   statjar   = 0; }
			if (statapk   > 0) { s += "ak" + statapk;   statapk   = 0; }
			if (statpdf   > 0) { s += "pd" + statpdf;   statpdf   = 0; }
			if (statother > 0) { s += "ot" + statother; statother = 0; }
			if (statapp   > 0) { s += "ap" + statapp;   statapp   = 0; }
			if (statmpo   > 0) { s += "mp" + statmpo;   statmpo   = 0; }
			if (statgen   > 0) { s += "ge" + statgen;   statgen   = 0; }
			if (statset   > 0) { s += "se" + statset;   statset   = 0; }
			if (statinfo  > 0) { s += "in" + statinfo;  statinfo  = 0; }
			if (statuhd   > 0) { s += "uh" + statuhd;   statuhd   = 0; }
			if (statqhd   > 0) { s += "qh" + statqhd;   statqhd   = 0; }
			if (statfhd   > 0) { s += "fh" + statfhd;   statfhd   = 0; }
			if (stathdr   > 0) { s += "hd" + stathdr;   stathdr   = 0; }
			if (statless  > 0) { s += "ls" + statless;  statless  = 0; }
			if (statmid   > 0) { s += "md" + statmid;   statmid   = 0; }
			if (stathigh  > 0) { s += "hi" + stathigh;  stathigh  = 0; }
			if (statmax   > 0) { s += "mx" + statmax;   statmax   = 0; }
			if (stattry   > 0) { s += "tr" + stattry;   stattry   = 0; }
			if (stathome  > 0) { s += "hm" + stathome;  stathome  = 0; }
			if (statcsv   > 0) { s += "cu" + statcsv;   statcsv   = 0; }
			if (staterr   > 0) { s += "er" + staterr;   staterr   = 0; }
			if (statdbg   > 0) { s += "db" + statdbg;   statdbg   = 0; }
			if (statnof   > 0) { s += "nf" + statnof;   statnof   = 0; }
			if (statcw    > 0) { s += "cq" + statcw;    statcw    = 0; } //win
			if (statcm    > 0) { s += "ca" + statcm;    statcm    = 0; } //mac
			if (statcx    > 0) { s += "cv" + statcx;    statcx    = 0; } //linux
			if (statca    > 0) { s += "cd" + statca;    statca    = 0; } //android
			if (statci    > 0) { s += "ci" + statci;    statci    = 0; } //iphone
			if (statcp    > 0) { s += "cp" + statcp;    statcp    = 0; } //ipad
			if (statct    > 0) { s += "ct" + statct;    statct    = 0; } //tablet
			if (statce    > 0) { s += "ce" + statce;    statce    = 0; } //ebook
			if (statco    > 0) { s += "cr" + statco;    statco    = 0; } //winmobile
			if (statcb    > 0) { s += "cb" + statcb;    statcb    = 0; } //blackberry
			if (statcu    > 0) { s += "cn" + statcu;    statcu    = 0; } //unknown
			if (statmr    > 0) { s += "mr" + statmr;    statmr    = 0; } //morph
			if (statlt    > 0) { s += "lt" + statlt;    statlt    = 0; } //lent
			if (statcrx   > 0) { s += "cx" + statcrx;   statcrx   = 0; } //crossed eye
			if (statttb   > 0) { s += "tb" + statttb;   statttb   = 0; } //prisma & ttb
			if (statcl    > 0) { s += "cl" + statcl;    statcl    = 0; } //conv lenticular
			if (statcs    > 0) { s += "cg" + statcs;    statcs    = 0; } //conf svg
			if (stateu    > 0) { s += "eu" + stateu;    stateu    = 0; } //fr,es,it
			if (staten    > 0) { s += "ex" + staten;    staten    = 0; } //us,uk
			if (statnet   > 0) { s += "nt" + statnet;   statnet   = 0; } //net,edu
			if (statsrc   > 0) { s += "sc" + statsrc;   statsrc   = 0; } //ggl,bing
			if (statsoc   > 0) { s += "cs" + statsoc;   statsoc   = 0; } //fb,tw

			s += "\n";
			p.write(s);
			p.flush();
			p.close();
			p = new FileWriter(getServletContext().getRealPath("") + "/stereo.res.stats",true);
			p.write(res);
			p.flush();
			p.close();
			p = new FileWriter(getServletContext().getRealPath("") + "/stereo.ana.stats",true);
			p.write(anaglyph);
			p.flush();
			p.close();
			p = new FileWriter(getServletContext().getRealPath("") + "/stereo.unk.stats",true);
			p.write(unknown);
			p.flush();
			p.close();
			p = new FileWriter(getServletContext().getRealPath("") + "/stereo.sls.stats",true);
			p.write(sls);
			p.flush();
			p.close();
		} catch (Exception e) {
			System.out.println("Stat schreiben gescheitert  " + new Date());
			e.printStackTrace();
			System.err.println(HVERSION);
		}
		anaglyph = "";
		res      = "";
		unknown  = "";
		sls      = "";
		laststat = System.currentTimeMillis();
		System.out.println("stat geschrieben" + new Date() + " nächste: " + new Date(laststat + statcycle));
	}
	String        csv            (int n) {
		String s;
		if ( n < 0 ) stat();
		if ( n < 2 ) s = "Datum,en.wikipedia.org,de.wikipedia.org,wikipedia,wm,uhd,qhd,fhd,hdr,android,desktop,pic,vid,vr,sbs,pol,holo,anaglyph,morph.lenticular,cross,prisma,app,slideshow,errs\n";
		else if ( n == 2 ) s = "Resolution\n";
		else s = "Colors\n";
		if ( n == 1 ) {
			s += new Date(laststat) + "," + statenwp + "," + statdewp + "," +
					(statwp + statjpwp + statfrwp + statitwp + stateswp + statplwp + statkowp + statruwp + statzhwp + statarwp + stathewp + statsvwp + statptwp + statukwp + statfawp + statviwp) + "," +
					(statwikt + statbook + statmeta + statcomm + statdata + statvoya + statnews + statsour + statvers + statmw + statworg) + "," +
					statuhd + "," + statqhd + "," + statfhd + "," + stathdr + "," + statapk + "," + statjar + "," + statpic + "," +
					statvid + "," + statvr + "," + statsbs + "," + statpol + "," + statholo + "," + statana + "," + statmr + "," + statlt + "," + statcrx + "," + statttb +  "," + statapp + "," + statslide + "," + staterr + "\n";
		} else {
			// datei lesen
			try {
				if ( n == 0 ) {
					LineNumberReader r = new LineNumberReader(new FileReader(getServletContext().getRealPath("") + "/stereo.stats"));
					String t = r.readLine();
					Vector<st> z = null;
					while (t != null && !t.equals("")) {
						z = st.line(t);
						s += st.cnt(z, "zeit") + "," + st.cnt(z, "en") + "," + st.cnt(z, "de") + "," + st.cnt(z, "wp,jp,fr,it,es,pl,ko,ru,zh,ar,he,sv,pt,uk,fa,vi") + "," +
								st.cnt(z, "wt,bk,mt,cm,dt,vy,nw,so,vr,mw,wo") + "," + st.cnt(z, "uh") + "," + st.cnt(z, "qh") + "," + st.cnt(z, "fh") + "," + st.cnt(z, "hr") + "," +
								st.cnt(z, "ap") + "," + st.cnt(z, "jr") + "," + st.cnt(z, "pi") + "," + st.cnt(z, "vd") + "," + st.cnt(z, "vr") + "," + st.cnt(z, "sb") + "," +
								st.cnt(z, "po") + "," + st.cnt(z, "ho") + "," + st.cnt(z, "an") + "," + st.cnt(z, "mr") + "," + st.cnt(z, "lt") + "," + st.cnt(z, "cx") + "," +
								st.cnt(z, "tb") + "," + st.cnt(z, "ap") + "," + st.cnt(z, "sl") + "," + st.cnt(z, "er") + "\n";
						t = r.readLine();
					}
				}
				else {
					LineNumberReader r;
					if ( n == 2 )  r = new LineNumberReader(new FileReader(getServletContext().getRealPath("") + "stereo.res.stats"));
					else r = new LineNumberReader(new FileReader(getServletContext().getRealPath("") + "stereo.ana.stats"));
					String t = r.readLine();
					while ( t != null ) {
						s += t;
						t = r.readLine();
					}
				}
			} catch (Exception e) {
				System.out.println("Stat lesen gescheitert  " + new Date());
				e.printStackTrace();
			}
		}
		System.out.println("stat gelesen " + new Date() + s);
		return s;
	}
	void          file           (PrintStream p, String ext) {
		byte[] s = new byte[1000000];
		int l = 0;
		try {
			FileInputStream in = new FileInputStream(new File(getServletContext().getRealPath("") + "stereo." + ext));
			l = in.read(s);
			while ( l > 0 ) {
				p.write(s,0,l);
				l = in.read(s);
			}
		} catch (Exception e) {
			System.out.println("Datei lesen/schreiben gescheitert  " + new Date());
			e.printStackTrace();
		}
	}
	boolean       checkAuth      (String auth) {
		if ( auth != null && aza != null ) for ( int i = 0; i < aza.length; i++ ) if ( auth.equals(aza[i]) ) return true;
		return false;
	}
	void          err502         (HttpServletResponse r, PrintStream o, String s, byte[] b) {
		staterr++;
		r.setStatus(502);
		r.setContentType("text/html");
		String t = "";
		try {
			int i = b.length;
			if ( i > 2000 ) i = 2000;
			int c = 0;
			for ( int j = 0; j < i; j++ ) if ( b[j] == 0 || (b[j] > 127 && b[j] < 160 ) ) { b[j] += 32; c++; }
			t = getCom(new String(b,0,i, "ISO_8859_1") + ( c > 0 ? " -> " + c + " changed! " : "") );
		} catch (Exception e) {
			t = "Fehler im Fehler: " + ( b == null ? "b is null" : e + " b.length = " + b.length );
		}
		o.println(head("502: " + s + " " + System.currentTimeMillis()) + bg(null) + "<h1>" + s + "</h1>" +
				( b != null ? b.length : "" ) + "<h3>" + t + "</h3>" + new Date() + ft(false));
	}
}
class svstream {
	PrintStream p;
	Object o;

	svstream (PrintStream q, Object r) {
		p = q;
		o = r;
	}
}
class d {
	boolean b;
	int i;

	public d(boolean c, int j) {
		b = c;
		i = j;
	};
}
class s {
	String u;

	public s(String t) {
		u = t;
	}
	public s t() {
		return new s(u.trim());
	}
	public char c(int i) {
		return u.charAt(i);
	}
	public boolean e(s v) {
		return u.equals(v.u);
	}
	public int l() {
		return u.length();
	}
	public s s(int a, int b) {
		return new s(u.substring(a,b));
	}
}
class st {
	String n;
	long   i;

	public st(String m, long k) {
		n = m;
		i = k;
	}

	static long cnt(Vector<st> v, String x) {
		long l = 0;
		st n = null;
		for ( int i = 0; i < v.size(); i++ ) {
			n = v.get(i);
			if ( x.indexOf(n.n) > -1 ) l += n.i;
		}
		return l;
	}

	static Vector<st> line(String s) {
		Vector<st> v = new Vector<st>();
		st n = new st("zeit",0);
		int l = s.length();
		int i = nx(s,0,l,n);
		v.add(n);
		while ( i < l ) {
			n = new st(s.substring(i,i+1),0);
			i = nx(s,i,l,n);
			v.add(n);
		}
		return v;
	}

	static int nx(String s, int p, int e, st n) {
		long l = 0;
		String t = "0";
		char c;
		while ( p < e ) {
			c = s.charAt(p++);
			if ( Character.isDigit(c) ) t += c;
			else e = -1;
		}
		try {
			l = Long.parseLong(t);
		}
		catch ( Exception f ) {}
		n.i = l;
		return p;
	}
}