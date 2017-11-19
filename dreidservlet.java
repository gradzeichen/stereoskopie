import helper.dreidabstract;
import helper.dreidinterface;
import helper.exp.dreidexception;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.DeflaterOutputStream;
import helper.exp.dreidzweidexp;

public class dreidservlet extends dreidgen {

	static final String VERSION = "0.0.8a";

	public             dreidservlet() {
		this.enc = "u";
		this.time = 0L;
		laststat = System.currentTimeMillis();
	}
	public  void       doGet(HttpServletRequest Request, HttpServletResponse Response) throws ServletException, IOException {
		this.time = System.currentTimeMillis();
		if (p == null || Request.getParameter("getprops") != null ) getProps();
		statget++;
		String re = Request.getHeader("Referer");
		if (re == null || re.indexOf("wmflabs")      > -1) {}
		else if (re.indexOf("en.wikipedia.org")      >= 0) statenwp++;
		else if (re.indexOf("de.wikipedia.org")      >= 0) statdewp++;
		else if (re.indexOf("fr.wikipedia.org")      >= 0) statfrwp++;
		else if (re.indexOf("it.wikipedia.org")      >= 0) statitwp++;
		else if (re.indexOf("es.wikipedia.org")      >= 0) stateswp++;
		else if (re.indexOf("pl.wikipedia.org")      >= 0) statplwp++;
		else if (re.indexOf("vi.wikipedia.org")      >= 0) statviwp++;
		else if (re.indexOf("jp.wikipedia.org")      >= 0) statjpwp++;
		else if (re.indexOf("ko.wikipedia.org")      >= 0) statkowp++;
		else if (re.indexOf("ru.wikipedia.org")      >= 0) statruwp++;
		else if (re.indexOf("zh.wikipedia.org")      >= 0) statzhwp++;
		else if (re.indexOf("ar.wikipedia.org")      >= 0) statarwp++;
		else if (re.indexOf("he.wikipedia.org")      >= 0) stathewp++;
		else if (re.indexOf("pt.wikipedia.org")      >= 0) statptwp++;
		else if (re.indexOf("uk.wikipedia.org")      >= 0) statukwp++;
		else if (re.indexOf("fa.wikipedia.org")      >= 0) statfawp++;
		else if (re.indexOf("sv.wikipedia.org")      >= 0) statsvwp++;
		else if (re.indexOf(".wikipedia.org")        >= 0) statwp++;
		else if (re.indexOf("meta.wikimedia.org")    >= 0) statmeta++;
		else if (re.indexOf("commons.wikimedia.org") >= 0) statcomm++;
		else if (re.indexOf("wiktionary.org")        >= 0) statwikt++;
		else if (re.indexOf("wikidata.org")          >= 0) statdata++;
		else if (re.indexOf("wikivoyage.org")        >= 0) statvoya++;
		else if (re.indexOf("wikinews.org")          >= 0) statnews++;
		else if (re.indexOf("wikibooks.org")         >= 0) statbook++;
		else if (re.indexOf("wikisource.org")        >= 0) statsour++;
		else if (re.indexOf("wikiversity.org")       >= 0) statvers++;
		else if (re.indexOf("mediawiki.org")         >= 0) statmw++;
		else if (re.indexOf("wikia.com")             >= 0) statwkia++;
		else if (re.indexOf("wiki")                  >= 0) {
			if      (re.indexOf(".org")              >= 0) statworg++;
			else if (re.indexOf(".com")              >= 0) statwcom++;
			else if (re.indexOf(".de")               >= 0) statwde++;
			else                                           statwiki++;
		}
		else if (re.indexOf(".org")                  >= 0) statorg++;
		else if (re.indexOf("google")                >= 0 ||
				 re.indexOf("bing")                  >= 0 ||
				 re.indexOf("yahoo")                 >= 0 ||
				 re.indexOf("duckduck")              >= 0) statsrc++;
		else if (re.indexOf("linkedin")              >= 0 ||
				 re.indexOf("pinterest")             >= 0 ||
				 re.indexOf("twitter")               >= 0 ||
				 re.indexOf("facebook")              >= 0) statsoc++;
		else if (re.indexOf(".de")                   >= 0 ||
				 re.indexOf(".at")                   >= 0 ||
				 re.indexOf(".ch")                   >= 0 ||
				 re.indexOf(".lu")                   >= 0) statde++;
		else if (re.indexOf(".es")                   >= 0 ||
				 re.indexOf(".fr")                   >= 0 ||
				 re.indexOf(".eu")                   >= 0 ||
				 re.indexOf(".nl")                   >= 0 ||
				 re.indexOf(".int")                  >= 0 ||
				 re.indexOf(".pl")                   >= 0 ||
				 re.indexOf(".pt")                   >= 0 ||
				 re.indexOf(".be")                   >= 0 ||
				 re.indexOf(".it")                   >= 0) stateu++;
		else if (re.indexOf(".net")                  >= 0 ||
				 re.indexOf(".edu")                  >= 0) statnet++;
		else if (re.indexOf(".us")                   >= 0 ||
				 re.indexOf(".uk")                   >= 0 ||
				 re.indexOf(".ie")                   >= 0 ||
				 re.indexOf(".au")                   >= 0 ||
 				 re.indexOf(".ca")                   >= 0) staten++;
		else if (re.indexOf(".com")                  >= 0) statcom++;
		else                                               statweb++;
		re = Request.getHeader("Client");
		if (re == null )                          statcu++;
		else {
			re = re.toLowerCase();
			if      (re.indexOf("mac")      >= 0) statcm++;
			else if (re.indexOf("linux")    >= 0) statcx++;
			else if (re.indexOf("iphone")   >= 0) statci++;
			else if (re.indexOf("win")      >= 0) {
				if (re.indexOf("mob")       >= 0) statco++;
				else                              statcw++;
			} else if (re.indexOf("tablet") >= 0) statct++;
			else if (re.indexOf("android")  >= 0) statca++;
			else if (re.indexOf("pad")      >= 0) statcp++;
			else if (re.indexOf("berry")    >= 0) statcb++;
			else if (re.indexOf("book")     >= 0) statce++;
			else {
				statcu++;
				if ( unknown.indexOf(re)    <  0) unknown += re + "";
			}
		}
		Request.setCharacterEncoding("UTF-8");
		String str1 = Request.getHeader("Accept-Encoding");
		if (str1 == null) str1 = "";
		String str2 = Request.getPathInfo();
		if (str2 == null) str2 = "";
		String str3 = str2;
		int l = str2.lastIndexOf('/');
		if (l >= 0) str2 = str2.substring(l + 1);
		l = str2.lastIndexOf(':');
		if (l >= 0) str2 = str2.substring(l + 1);
		String setC = Request.getParameter("setc");
		String setL = Request.getParameter("setl");
		boolean setan = (Request.getParameter("analeft") != null || Request.getParameter("anaright") != null || Request.getParameter("analeftc") != null || Request.getParameter("anarightc") != null);
		String setR = Request.getParameter("setr");
		String cha = Request.getParameter("section");
		String cap = Request.getParameter("caption");
		String setW = Request.getParameter("setw");
		String lr = Request.getParameter("lr");
		if (cha == null) cha = Request.getParameter("s");
		if (cap == null) cap = Request.getParameter("c");
		int duration = 0;
		String dd = Request.getParameter("duration");
		if ( dd == null ) dd = Request.getParameter("dur");
		if ( dd == null ) dd = Request.getParameter("d");
		try { duration = Integer.parseInt(dd); } catch ( Exception w ) {};
		dbg = Request.getParameter("debug");
		svstream sv = getStream(Response, str1);
		PrintStream lPS = sv.p;
		Object lO = sv.o;
		Cookie[] coo = Request.getCookies();
		mc = "";
		uhd = 720;
		String ew = null;
		Cookie wc = null, rc = null, dc = null, sc = null, lc = null;
		for (int i = 0; coo != null && i < coo.length; i++) {
			if (coo[i].getName().equals("stereowindow")) { wc = coo[i]; ew = coo[i].getValue(); }
			if (coo[i].getName().equals("stereocookie")) { sc = coo[i]; mc = coo[i].getValue(); }
			if (coo[i].getName().equals("stereolent"  )) { lc = coo[i]; }
			if (coo[i].getName().equals("stereores"))
				try {
					rc = coo[i];
					uhd = Integer.parseInt(coo[i].getValue());
				} catch (Exception e) {
					uhd = 360;
				}
			if (coo[i].getName().equals("stereodbg")) {
				dc = coo[i];
				String v = coo[i].getValue();
				if ( v == null ) v = "";
				if ( v.equals("true") && (dbg == null || ( dbg.length() > 0 && ("nNfF-").indexOf(dbg.charAt(0)) == -1))) {
					debug = true;
					if ( dc == null ) dc = new Cookie("stereodbg","true"); else dc.setValue("true");
					Response.addCookie(dc);
				}
				else if (dbg != null && ( dbg.length() > 0 && ("nNfF-").indexOf(dbg.charAt(0)) > -1) ) {
					if ( dc == null ) dc = new Cookie("stereodbg","false"); else dc.setValue("false");
					Response.addCookie(dc);
					debug = false;
				}
				else if (coo[i].getValue().equals("true")) debug = true;
			}
		}
		if (dbg != null && (dbg.length() == 0 || ("nNfF-").indexOf(dbg.charAt(0)) == -1)) {
			debug = true;
			if ( dc == null ) dc = new Cookie("stereodbg","true"); else dc.setValue("true");
			Response.addCookie(dc);
		}
		else debug = false;
		dbg = "start";
		if (debug) statdbg++;
		if (setC != null) {
			// Cookie neu schreiben
			if ( sc == null ) sc = new Cookie("stereocookie",setC); else sc.setValue(setC);
			Response.addCookie(sc);
			mc = setC;
			if      (setC.startsWith("ca")   ) statvr++;
			else if (setC.startsWith("v")    ) statvr++;
			else if (setC.indexOf("app") >= 0) statapp++;
			else if (setC.startsWith("cr")   ) statcrx++;
			else if (setC.startsWith("s")    ) statsbs++;
			else if (setC.startsWith("pr")   ) statttb++;
			else if (setC.startsWith("p")    ) statpol++;
			else if (setC.startsWith("d")    ) statdl++;
			else if (setC.startsWith("h")    ) statholo++;
			else if (setC.startsWith("a")    ) statana++;
			else if (setC.startsWith("m")    ) statmr++;
			else if (setC.startsWith("l")    ) statlt++;
		}
		if (setW != null) {
			if ( setW.length() == 0 || setW.equals("false") ) {
				if ( wc == null ) wc = new Cookie("stereowindow","false"); else wc.setValue("false");
				ew = null;
			}
			else {
				if ( wc == null ) wc = new Cookie("stereowindow","true"); else wc.setValue("true");
				ew = "links";
			}
			// Cookie neu schreiben
			Response.addCookie(wc);
		}
		if (setL != null) {
			if ( setL.length() == 0 ) {
				if ( lc == null ) lc = new Cookie("stereolent","1"); else lc.setValue("1");
			}
			else {
				try {
					lenticularsiz = Integer.parseInt(setL);
				} catch (Exception e) {
					lenticularsiz = 1;
				};
			}
			// Cookie neu schreiben
			if ( lc == null ) lc = new Cookie("stereolent","" + lenticularsiz); else lc.setValue("" + lenticularsiz);
			Response.addCookie(lc);
		}
		if (setR != null) {
			if ( rc == null ) rc = new Cookie("stereores",setR); else rc.setValue(setR);
			Response.addCookie(rc);
			try {
				uhd = Integer.parseInt(setR);
				if (uhd == 3840) statuhd++;
				else if (uhd == 1920) statfhd++;
				else if (uhd == 1280) stathdr++;
				else if (uhd == 2560) statqhd++;
				else {
					if (uhd < 1280) statless++;
					else if (uhd > 3840) statmax++;
					else if (uhd > 1920) stathigh++;
					else statmid++;
					res += uhd+ "\n";
				}
			} catch (Exception e) {
				if ( uhd > 0 ) uhd = -uhd;
				else uhd = -2;
			};
		}
		if (setan) {
			String a = Request.getParameter("analeft");
			String b = Request.getParameter("anaright");
			if (a != null && a.length() == 6 && b != null && b.length() == 6) {
				mc = "anaglyph," + a + "," + b;
			} else {
				a = Request.getParameter("analeftc");
				b = Request.getParameter("anarightc");
				mc = "anaglyph," + a.substring(a.length() - 6) + "," + b.substring(b.length() - 6);
			}
			if ( sc == null ) sc = new Cookie("stereocookie",mc); else sc.setValue(mc);
			Response.addCookie(sc);
			anaglyph += a + b + "\n";
		}
		if (debug) debug(Request, str2);
		String post = Request.getParameter("post");
		if (post == null) post = "";
		if ( !( ( checkAuth(Request.getHeader("Authorization")) ) || freeacc ) ) {
			Response.setStatus(401);
			Response.setHeader("WWW-Authenticate", "Basic realm=\"project #0023\"");
			Response.setHeader("Location",baustelle);
		}
		else if ( ew != null && ew.equals("true") && ( lr == null || ( !lr.equals("links") && !lr.equals("rchts") ) ) ) {
			// umleiten auf linkes fenster
			Response.setStatus(307);
			String q = Request.getQueryString();
			if ( q == null ) q = "";
			else q += "&";
			q = q.replace("lr=","rl=");
			Response.setHeader("Location", sserver + str3 + "?" + q + "lr=links");
			Response.setHeader("Window-target","links");
		}
		else if ( dotest && ( !str2.equals("test") || !debug ) ) Response.sendRedirect(baustelle);
		else doS(str3, str2, lPS, Response, setR, cha, cap, post, duration);
		lPS.flush();
		if (laststat + statcycle < System.currentTimeMillis()) {
			stat();
			System.out.println("stat aufgerufen" + new Date());
		}
		//else /*System.out.println("aufruf durch " + new Date()*/);
		if (lO == null) return;
		((DeflaterOutputStream) lO).finish();
	}
	private void       doS(String str3, String str2, PrintStream lPS, HttpServletResponse Response, String setR, String cha, String cap, String p, int duration) {
		if ( ( str3.toLowerCase().startsWith("/slideshow/") || str3.toLowerCase().startsWith("/s/") ) && mc.length() > 0) {
			// slideshow ausführen
			dbg += " slds ";
			statslide++;
			slshow(Response, lPS, str3, cha);
		}
		else if ( str3.toLowerCase().startsWith("/forward/jps/") ) {
			String img = str3.substring(str3.indexOf("/jps/") + 4);
			Response.setStatus(307);
			Response.setHeader("Location", imgprf2 + img);
			img = img.substring(0,img.indexOf(".jps")) + ".jps";
			Response.setHeader("Content-Location",img);
		}
		else if ( str3.toLowerCase().startsWith("/forward/vid/") ) {
			String img = str3.substring(str3.indexOf("/vid/") + 4);
			Response.setStatus(307);
			Response.setHeader("Location", imgprf2 + img);
		}
		else if ( str3.toLowerCase().startsWith("/conv") ) {
			try {
				String img = str3.substring(str3.indexOf("/conv/") + 6);
				img = img.substring(img.lastIndexOf('/') + 1, img.lastIndexOf("."));
				if (str3.toLowerCase().startsWith("/conv/len/")) {
					try {
						String para = "";
						// anzahl einzelbilder, ziel: jps oder len, insets, auswahl bildnummern
						String f = imggen + str3.substring(8);
						statcl++;
						if (cnvlnt == null) err502(Response, lPS, "conversion of lenticular module not loadad.", null);
						else
							jpeg(Response, lPS, cnvlnt.getInst().generate(f, para, 0), img, "lenticular" + "." + para); // bzw. lossless
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (str3.toLowerCase().startsWith("/conv/svg/")) {
					String para = "";
					// ein oder mehrere svgs, tokennamen
					statcs++;
					if (cnvsvg == null) err502(Response, lPS, "conversion of svg module not loadad.", null);
					else jpeg(Response, lPS, cnvsvg.getInst().generate(imggen + str3.substring(14), para, 0), img, "svg2jps");
				} else {
					html(Response, lPS, false, str2, info("Unsupported type of convertation: " + str2), null);
				}
			}
			catch ( dreidexception t ) {
				html(Response, lPS, false, str2, info(t.reason), null);
			}
		} else if (str3.toLowerCase().startsWith("/gen/")) {
			try {
				statgen++;
				String img = str3.substring(str3.indexOf("/gen/") + 5);
				if ( img.indexOf(".jps") > 0 ) img = img.substring(img.lastIndexOf('/') + 1, img.indexOf(".jps"));
				if (!debug) {
					Date heute = new Date( ( new Date().getTime() / 1000 ) * 1000 );
					Calendar ca = new GregorianCalendar();
					ca.setTime(heute);
					ca.set(Calendar.MINUTE,1);
					ca.set(Calendar.SECOND,1);
					long last = 0, exp = System.currentTimeMillis();
					if (str3.indexOf("/gen/zip") == 0)
						last = ca.getTime().getTime();
					else if (str3.indexOf("/gen/mor") == 0) {
						ca.set(Calendar.MONTH,1);
						ca.set(Calendar.DATE,1);
						last = ca.getTime().getTime();
						exp += 365 * 60 * 60 * 1000;
					} else if (str3.indexOf("/gen/ana") == 0 || str3.indexOf("/gen/len") == 0) {
						ca.set(Calendar.DATE,1);
						last = ca.getTime().getTime();
						exp += 30 * 60 * 60 * 1000;
					} else {
						ca.set(Calendar.HOUR,1);
						last = ca.getTime().getTime() - (5 * 60 * 60 * 1000);
						exp += 5 * 60 * 60 * 1000;
					}
					Response.setDateHeader("Last-Modified", last);
					Response.setDateHeader("Expires", exp);
				} else Response.setDateHeader("Expires", System.currentTimeMillis());
				int tp = tpe(str3);
				if (str3.toLowerCase().startsWith("/gen/sbs/")) {
					try {
						String f = imggen + str3.substring(8);
						if ( sbspic == null ) throw new Exception("no sbs");
						dreidinterface ab = sbspic.getInst();
						if ( ab == null ) throw new Exception("no instance");
						InputStream iw = ab.generate(f,null, duration);
						if ( iw == null ) throw new Exception("no inputstream");
						System.out.println("sbs " + iw);
						jpeg(Response, lPS, iw, img, "sbs");
					} catch ( dreidexception e) {
						System.out.println(e.reason);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				} else if (str3.toLowerCase().startsWith("/gen/pol/")) {
					jpeg(Response, lPS, polpic.getInst().generate(imggen + str3.substring(14), "links", 0), img, "pol");
				} else if (str3.toLowerCase().startsWith("/gen/app/")) {
					String ty = "video/webm";
					if (tp == 0) ty = "image/x-jps";
					else if (tp == 1) ty = "video/x-sbs-webm";
					else if (tp == 2) ty = "video/x-tab-webm";
					else if (tp == 3) ty = "video/x-vrglasses-webm";
					webm(Response, lPS, appdld.getInst().generate(imggen + str3.substring(8), p, duration), ty, null);
				} else if (str3.toLowerCase().startsWith("/gen/vr/")) {
					String ty = "video/webm";
					webm(Response, lPS, vrgvid.getInst().generate(imggen + str3.substring(7), p, duration), ty, null);
				} else if (str3.toLowerCase().startsWith("/gen/d")) {
					int i = 7;
					if (str3.toLowerCase().startsWith("/gen/do")) i += 6;
					webm(Response, lPS, appdld.getInst().generate(imggen + str3.substring(i), p, duration), "binary/download", str2);
				} else if (str3.toLowerCase().startsWith("/gen/holo/")) {
					jpeg(Response, lPS, holpic.getInst().generate(imggen + str3.substring(9), null, 0), img, "holo");
				} else if (str3.toLowerCase().startsWith("/gen/cr")) {
					jpeg(Response, lPS, crxpic.getInst().generate(imggen + str3.substring(10), null, 0), img, "cross");
				} else if (str3.toLowerCase().startsWith("/gen/tt") || str3.toLowerCase().startsWith("/gen/pr")) {
					jpeg(Response, lPS, ttbpic.getInst().generate(imggen + str3.substring(8), null, 0), img, "prisma");
				} else if (str3.toLowerCase().startsWith("/gen/ana/")) {
					jpeg(Response, lPS, anapic.getInst().generate(imggen + str3.substring(24), str3.substring(8, 22), 0), img, "anaglyph." + str3.substring(24) + "." + str3.substring(8, 22));
				} else if (str3.toLowerCase().startsWith("/gen/lenticular")) {
					lossless(Response, lPS, ltcpic.getInst().generate(imggen + str3.substring(27), str3.substring(16, 27), 0), false, img);
				} else if (str3.toLowerCase().startsWith("/gen/morph")) {
					lossless(Response, lPS, mrppic.getInst().generate(imggen + str3.substring(10), null, 0), true, img);
				} else if (str3.toLowerCase().startsWith("/gen/zip/")) {
					zip(Response, lPS, str3, cha, cap);
				} else {
					html(Response, lPS, false, str2, info(notsupported + ": " + str2), null);
				}
			}
			catch ( dreidexception z ) {
				html(Response, lPS, false, str2, info(z.reason), null);
			}
			catch ( Exception z ) {
				z.printStackTrace();
				html(Response, lPS, false, str2, info("Gen-Error: " + z), null);
			}
		} else if (str3.toLowerCase().startsWith("/anachooser/")) {
			// anaglyph farben wählen
			if ( str3.indexOf("/slideshow/") > -1  || str3.indexOf("/s/") > -1 ) str2 = "/slideshow/" + str2;
			html(Response, lPS, false, str2, anach(str2,cha), null ); //nocomment);
		} else if (str3.toLowerCase().startsWith("/stereoskopie.pdf")) {
			// pdf downloaden
			statpdf++;
			Response.setContentType("text/pdf");
			file(lPS, "pdf");
		} else if (str2.equals("setR")) {
			try {
				uhd = Integer.parseInt(setR);
			} catch (Exception e) {
				// e
			}
			html(Response, lPS, false, "Screen Resolution", getUhd(), null ); // nocomment);
			dbg += "res is set!";
		} else if (str2.equals("test") && debug && dotest ) {
			html(Response, lPS, false, "Test " + new Date(), test(), null ); // nocomment);
			dbg += "tested";
		} else if (str3.toLowerCase().equals("/stereoskopie.apk")) {
			// App downloaden
			statapk++;
			Response.setContentType("binary/download");
			file(lPS, "apk");
		} else if (str3.toLowerCase().equals("/stereoskopie.jar")) {
			// Desktopversion downloaden
			statjar++;
			Response.setContentType("binary/download");
			file(lPS, "jar");
		} else if (str3.toLowerCase().equals("/stereoskopie.csv")) {
			// statistik downloaden
			statcsv++;
			Response.setContentType("text/csv");
			int now = 0;
			if (p.equals("now")) now = 1;
			else if ( p.startsWith("res") ) now = 2;
			else if ( p.startsWith("ana") ) now = 3;
			else if ( p.startsWith("writenow") ) now = -1;
			lPS.println(csv(now));
		} else if (str2.indexOf('.') < 0 && !str2.toLowerCase().equals("slideshow")) {
			// startseite anzeigen
			statslide++;
			html(Response, lPS, false, str2, info(null), null ); // nocomment);
		} else if (mc == null || mc.equals("")) {
			// noch kein Cookie gesetzt
			statset++;
			html(Response, lPS, false, str2, setA(str2, cha, cap), null ); // nocomment);
		} else if (str2.toLowerCase().indexOf(".jps") > 0 || str2.toLowerCase().indexOf(".sbs.") > 0 || str2.toLowerCase().indexOf(".tab.") > 0 || str2.toLowerCase().indexOf(".cardboard.") > 0 ||
				str2.toLowerCase().indexOf(".vrglasses.") > 0 || str2.toLowerCase().indexOf(".holopyramid.") > 0 || str2.toLowerCase().indexOf(".anaglyph.") > 0) {
			// pic ode video anzeigen
			String[][] slds = null;
			int ind = 0;
			if (str2.toLowerCase().indexOf(".jps") > 0) {
				slds = new String[1][11];
				slds[0][0] = str2;
				slds[0][1] = "0";
				slds[0][2] = cap;
 				try {
 					slds = getsizes(slds);
					statpic++;
				}
				catch ( Exception e ) {
 					statnof++;
 					err502(Response,lPS,e.toString(),str2.getBytes());
				}
			}
			else {
				// video wählen!!!!!
				int anz = 1;
				slds = new String[anz][11];
				slds[0][0] = str2;
				slds[0][1] = "0";
				slds[0][2] = cap;
				try {
					slds = getsizes(slds);
					statvid++;
				}
				catch ( Exception e ) {
					statnof++;
					err502(Response,lPS,e.toString(),str2.getBytes());
				}
			}
			html(Response, lPS, true, str2, disp(str2, mc, lr, duration), slds[ind] ); //cap);
		} else if (str2.toLowerCase().indexOf(".mpo") > 0) {
			statmpo++;
			html(Response, lPS, false, str2, info(notsupported + ": " + str2), null);
		} else {
			if (str2.equals("info")) statinfo++;
			else if (str3.toLowerCase().startsWith("/stereoskopie.")) stattry++;
			else if (str2.length() < 3 || str3.toLowerCase().startsWith("/index.")) stathome++;
			else statother++;
			html(Response, lPS, false, str2, info(notfound + ": " + str2), null);
		}
	}
	private void       slshow(HttpServletResponse Res, PrintStream ps, String p, String cha) {
		String[][] slds = null;
		try {
			int i = p.indexOf('/',1) + 1;
			p = p.substring(i);
			String dt = new Date() + "";
			slds = getSlides(p, mwurl(cha,false));
			sls += p + "#" +  mwurl(cha,false) + "\n";
			System.out.println("slshow slides. " + p + " --- " + cha + " --- " + slds.length + "  ---  " + new Date());
			getsizes(slds);
			int nof = 0;
			if ( slds == null || slds.length == 0 ) err502(Res, ps, "No slides found in " + p, null);
			else if ( mc.indexOf("app") > -1 ){
				Res.setContentType("text/x-stereo-slideshow-properties");
				ps.println("version=1.0");
				ps.println("info.show="    + p);
				ps.println("info.section=" + cha);
				ps.println("info.caption=");
				ps.println("info.mod" + dt);
				ps.println("info.srvd=" + sserver);
				ps.println("info.imgs=" + iserver);
				ps.println("count=" + slds.length);
				String d = null;
				int ij = 100000;
				for ( int ii = 0; ii < slds.length; ii++ ) {
					d = disp(slds[ii][0], mc, "links", getSecs(slds[ii][1]));
					ps.println("url." + ij + "=" + d);
					ps.println("dur." + ij + "=" + slds[ii][1]);
					ps.println("cap." + ij + "=" + inline(slds[ii][2]));
					ps.println("wid." + ij + "=" + slds[ii][3]);
					ps.println("hgt." + ij + "=" + slds[ii][4]);
					ps.println("len." + ij + "=" + slds[ii][5]);
					ps.println("art." + ij + "=" + inline(slds[ii][6]));
					ps.println("lic." + ij + "=" + slds[ii][7]);
					ps.println("lur." + ij + "=" + slds[ii][8]);
					ps.println("att." + ij + "=" + inline(slds[ii][9]));
					ps.println("mty." + ij + "=" + slds[ii][10]);
					if ( slds[ii][10].startsWith("notf") ) nof++;
					ps.println("dsu." + ij++ + "=" + picinfo + d.substring(d.lastIndexOf('/') + 1));
				}
				ps.println("info.ready" + new Date());
				ps.println("status=transmitted");
			}
			else {
				Res.setContentType("text/html; charset=UTF-8");
				// prefetch via link für alle medien
				// help via link
				if ( cha == null ) cha = "All Chapters";
				String d = "";
				String urls  = "const urls = [\n";
				String dur   = "const dur  = [\n";
				String cap   = "const cap  = [\n";
				String wid   = "const wid  = [\n";
				String heig  = "const heig = [\n";
				String len   = "const len  = [\n";
				String art   = "const art  = [\n";
				String li    = "const li   = [\n";
				String lu    = "const lu   = [\n";
				String att   = "const att  = [\n";
				String du    = "const du   = [\n";
				String mt    = "const mt   = [\n";
				String sx    = null;
				for ( i = 0; i < slds.length; i++) {
					d     = disp(slds[i][0], mc, "links", getSecs(slds[i][1]));
					Res.setHeader("Prefetch",d);
					if (i > 0) sx = ", // " + ( i + 999 ) + "\n"; else sx = "";
					du    += sx + "'" + picinfo + d.substring(d.lastIndexOf('/') + 1) + "'";
					urls  += sx + "'" + d + "'";
					dur   += sx + "'" + getSecs(slds[i][ 1]) + "'";
					cap   += sx + "'" + getCom( slds[i][ 2]) + "'";
					wid   += sx + "'" + slds[i][ 3] + "'";
					heig  += sx + "'" + slds[i][ 4] + "'";
					art   += sx + "'" + slds[i][ 5] + "'";
					li    += sx + "'" + slds[i][ 6] + "'";
					lu    += sx + "'" + slds[i][ 7] + "'";
					len   += sx + "'" + slds[i][ 8] + "'";
					att   += sx + "'" + slds[i][ 9] + "'";
					mt    += sx + "'" + slds[i][10] + "'";
				}
				ps.println(head("Slideshow: " + p + ": " + cha) + bg(null) +
						"\n<script>\n" +
						urls + "];\n" +
						dur  + "];\n" +
						cap  + "];\n" +
						wid  + "];\n" +
						heig + "];\n" +
						len  + "];\n" +
						art  + "];\n" +
						li   + "];\n" +
						lu   + "];\n" +
						att  + "];\n" +
						du   + "];\n" +
						mt   + "];\n" +
						"const dbg = " + sldsdbg + ";\n" +
						"const bgimg = '" + pic(null) + "';\n" +
						"startslideshow(" + slds.length + "," + nof +");\n" +
						"</script>\n" +
						( debug ? "<pre>" + urls + "\n\n" + art + "\n\n" + mt + "\n</pre>\n" : "" ) +
						"<noscript>Sorry, without Javascript this cannot work.</noscript>" +
						ft(true));
				System.out.println("slshow head. " + slds.length + "  --- " + p + " --- " + cha + "  " + new Date());
			}
		}
		catch (Exception e) {
			byte[] b = e.toString().getBytes();
			try{ b = e.toString().getBytes("ISO_8859_1"); } catch ( Exception f ) {};
			err502(Res,ps, "Slideshow load error:", b);
		}
	}
	private void       html(HttpServletResponse Response, PrintStream lPS, boolean media, String t, String c, String[] com) {
		Response.setContentType("text/html; charset=UTF-8");
		if ( t == null ) t = "<!-- Empty -->";
		if ( !media ) {
			Response.setDateHeader("Last-Modified",System.currentTimeMillis());
			Response.setDateHeader("Expires",System.currentTimeMillis());
		}

		System.out.println("html: " + c + " -- " + ( com != null ? com[10] : "" ));

		if ( com == null || com[10].equals("pic") )
			lPS.println(head(t) + bg((media ? c : null)) + (media ? med(t, com) + (debug ? "<p>File: " + c : "") : c) + ft(media));
		else
			lPS.println(head(t) + bg((null)) + (media ? med(t, com) + (debug ? "<p>File: " + c : "") : c) + "<video autoplay " +
					"loop style=\"position: fixed; top: 50%; left: 50%; min-width: 100%; min-height: 100%; width: auto; height: auto; z-index: -100; " +
					"-ms-transform: translateX(-50%) translateY(-50%); -moz-transform: translateX(-50%) translateY(-50%); -webkit-transform: translateX(-50%) translateY(-50%); " +
					"transform: translateX(-50%) translateY(-50%);\"><source src=\"" + c + "\" type=video/webm></video>" + ft(media));
	}
	private void       zip(HttpServletResponse Res, PrintStream lPS, String path, String sect, String capt) {
		Res.setContentType("binary/download");
		// zip-suffix entfernen
		// datei oder slieeshow?
		// getslides
		// make zip
		// write
		// txt/pdf
		// txt/slides
		// sbs/vid
		// vrg/webm
		// hlp/webm
		// ana/123456123456/webm
		// img/jps
		// readmie schreiben
	}
	private String     disp(String u, String c, String lr, int dur) {
		String s = getmd(u); //http://localhost/mediawiki/index.php/File:";
		if (c.startsWith("anaglyph")) s = anag(s, u, c, dur);
		else if (c.startsWith("lenticular")) s = lnt(s, u, c, dur);
		else if (c.startsWith("cr")) s = crx(s, u, dur);
		else if (c.startsWith("t") || c.startsWith("p") ) s = ttb(s, u, dur);
		else if (c.equals("vrglasses") || c.equals("cardboard")) s = vr(s, u, dur);
		else if (c.equals("sbs") || c.equals("3dtv")) s = sbs(s, u, dur);
		else if (c.equals("pol")) s = pol(s, u, lr, dur);
		else if (c.equals("morph")) s = morph(s, u, dur);
		else if (c.equals("useapp")) s = app(s, u, dur);
		else if (c.equals("holopyramid")) s = hp(s, u, dur);
		else             /*   "download" */ s = dl(s, u, dur);
		return s;
	}
	private String     test() {
		String[][] r = new String[2][10];
		r[0][0] = "ww.webm";
		r[1][0] = "ww.jpg";
		try {
			r = getsizes(r);
		}
		catch ( Exception e ) {
			return e.toString();
		}
		String s = "<ul>";
		s += "<li>" + r[0][0]; s += "<li>" + r[0][1]; s += "<li>" + r[0][2]; s += "<li>" + r[0][3]; s += "<li>" + r[0][4];
		s += "<li>" + r[0][5]; s += "<li>" + r[0][6]; s += "<li>" + r[0][7]; s += "<li>" + r[0][8]; s += "<li>" + r[0][9];
		s += "<li>" + r[1][0]; s += "<li>" + r[1][1]; s += "<li>" + r[1][2]; s += "<li>" + r[1][3]; s += "<li>" + r[1][4];
		s += "<li>" + r[1][5]; s += "<li>" + r[1][6]; s += "<li>" + r[1][7]; s += "<li>" + r[1][8]; s += "<li>" + r[1][9];
		return s + "</ul>";
	}
	private String[][] getsizes(String[][] f) throws IOException, dreidzweidexp {
		// lese von json...
		if ( f == null || f.length == 0 ) {
			System.out.println("Sizes mit leerer Eingabe!");
			return f;
		}
		int splen = sizesprefix.length();
		int ak = 0;
		int ul = 0;
		String titlelist = "";
		String ee = "", ex = "";
		boolean we = true;
		int k0 = 0;
		for (int k = 0; k < f.length; ) {
			if ( ak < maxny && ul + f[k][0].length() + 6 < maxln ) {
				titlelist += mwurl("File:" + f[k][0], true) + "|";
				f[k][10] = "undef";
				f[k][5] = "id #" + (k +  1000);
				ul += f[k++][0].length() + 6;
				ak++;
				we = true;
			}
			else {
				we = false;
			}
			if ( !we || k == f.length ) {
				ak = 0;
				ul = splen;
				InputStream is = new URL(sizesprefix + titlelist).openStream();
				titlelist = "";
				String l = "";
				if (is != null) {
					LineNumberReader n = new LineNumberReader(new InputStreamReader(is, Charset.forName("UTF-8")));
					while (n.ready()) l += n.readLine() + "\n";
					JSONObject json = new JSONObject(l);
					String title = "", ttl = "";
					double dur = -2;
					int dul = -2, sta = -2, stl = -2;
					boolean pic = true;
					JSONObject jo = json.getJSONObject("query"), oe = null;
					jo = jo.getJSONObject("pages");
					Iterator je = jo.keys(), jj = null, ji = null;
					int q = -2;
					JSONArray oa = null;
					JSONObject oo = null, oc = null, od = null, of = null;
					String nn = null;
					while (je.hasNext()) {
						System.out.println("next");
						try {
							ex = "";
							oe = jo.getJSONObject((String) je.next());
							sta = -3;
							title = oe.getString("title");
							q = title.lastIndexOf(':');
							if (q > 0) title = title.substring(q + 1);
							title = mwurl(title, true);
							ex = "1";
							for (int i = k0; i < k; i++) {
								stl = eq(mwurl(f[i][0],true), title);
								if ( f[i][6] == null ) f[i][6] = "i " + i + " k0 " + k0 + " k " + k;
								ex = "2";
								if ( ( stl > 0 && stl > sta ) || ( stl == 0 && sta < 0 ) ) {
									System.out.println("stl " + title + " " + i + " stl " + stl + " sta " + sta);
									ex = "3 ";
									long ll = -33;
									try { ll = oe.getLong("pageid"); } catch ( Exception e ) { ex += e + " i= " + i + " k= " + k; };
									if ( ll > 0 ) {
										sta = stl;
										if ( title.indexOf(".jps") > -1 || ( dotest && title.indexOf(".jpg") > -1 ) ) {
											pic = true;
											f[i][10] = "pic";
											if      ( dotest )                             f[i][10] += " test";
										}
										else {
											pic = false;
											if      ( title.indexOf(".cardboard.")  > -1 ) f[i][10] = "cardboard";
											else if ( title.indexOf(".vrglasses.")  > -1 ) f[i][10] = "vrglasses";
											else if ( title.indexOf(".sbs.")        > -1 ) f[i][10] = "sbs";
											else if ( title.indexOf(".tab.")        > -1 ) f[i][10] = "tab";
											else if ( title.indexOf(".anaglyph.")   > -1 ) f[i][10] = "anaglyph";
											else if ( title.indexOf(".holo")        > -1 ) f[i][10] = "holo";
											else if ( title.indexOf(".morph")       > -1 ) f[i][10] = "morph";
											else if ( title.indexOf(".lenti")       > -1 ) f[i][10] = "lenticular";
											else if ( dotest )                             f[i][10] = "novid";
											else throw new dreidzweidexp(title + " not a stereoscopic file");
											if      ( dotest )                             f[i][10] += " test";
										}
										ex += "4 " + pic;
										oa = oe.getJSONArray("imageinfo");
										try {
											for (int y = 0; y < oa.length(); y++) {
												oo = oa.getJSONObject(y);
												jj = oo.keys();
												while (jj.hasNext()) {
													nn = (String) jj.next();
													if (nn.equals("width"))  f[i][3] = oo.getInt(nn) + "";
													if (nn.equals("height")) f[i][4] = oo.getInt(nn) + "";
													if (nn.equals("size"))   f[i][8] = oo.getInt(nn) + "";
													ex += "x";
													if (!pic && nn.equals("duration"))
														dur = oo.getBigDecimal(nn).doubleValue();
													if (nn.equals("extmetadata")) {
														od = oo.getJSONObject(nn);
														ji = od.keys();
														while ( ji.hasNext() ) {
															nn = (String) ji.next();
															of = (JSONObject) od.get(nn);
															if ( nn.equals("Artist") ) {
																try {
																	f[i][5] = getCom(of.getString("value"));
																} catch ( NullPointerException e) {
																	f[i][5] = "NPE in Artist: " + e + " -> " + f[i][0];
																} catch (Exception e) {
																	ee += " art " + e;
																}
															}
															else if ( nn.equals("LicenseShortName") ) {
																try {
																	f[i][6] = getCom(of.getString("value"));
																} catch ( NullPointerException e) {
																	f[i][6] = "NPE in LicenceShortName: " + e + " -> " + f[i][0];
																} catch (Exception e) {
																	ee += " lst " + e;
																}
															}
															else if ( nn.equals("LicenseUrl") ) {
																try {
																	f[i][7] = of.getString("value");
																} catch ( NullPointerException e) {
																	f[i][7] = "NPE in LicenceUrl: " + e + " -> " + f[i][0];
																} catch (Exception e) {
																	ee += " lur " + e;
																}
															}
															else if ( nn.equals("ImageDescription") ) {
																try {
																	ttl = getCom(of.getString("value"));
																} catch ( NullPointerException e) {
																	ttl = null;
																} catch (Exception e) {
																	ee += " imd " + e;
																}
															}
															else if ( nn.equals("Attribution") ) {
																try {
																	f[i][9] = getCom(of.getString("value"));
																} catch ( NullPointerException e) {
																	f[i][9] = "NPE in Attribution: " + e + " -> " + f[i][0];
																} catch (Exception e) {
																	ee += " atr " + e;
																}
															}
														}
													}
												}
											}
										} catch (Exception e) {
											ee += " imif " + e;
										}
										ex += " 6";
										if (!pic) {
											try {
												dul = Integer.parseInt(f[i][i]);
												ex += " 7";
											} catch (Exception e) {
												dul = -3;
											}
											ex += " 8";
											if (dul < 3) f[i][1] = "" + (int) dur;
											else if (dul < dur) f[i][1] = "-" + dul;
											ex += " 9";
										}
										if (f[i][2] == null || f[i][2].length() < 4) f[i][2] = ttl;
									} else if (sta == -1) {
										ex += " 10";
										f[i][3] = "-1";
										f[i][4] = "-1";
										f[k][5] = "No file found " + ( k + 1000 );
										f[i][8] = "-1";
										f[i][10] = "nofile";
									}
									ex += " 10";
								}
								ex += " 11";
							}
							ex += " 12";
						} catch (Exception e) {
							ee += e + " " + ex + " ---- ";
						}
					}
				}
				k0 = k;
			}
		}
		if ( ee.length() > 0 ) throw new IOException(ee);
		return f;
	}
	private String[][] getSlides(String u, String cha) throws IOException {
		// urlreader
		InputStream is = null;
		try {
			is = new URL(slideprefix + u).openStream();
		} catch (Exception e) {
			throw new IOException("Slideshow " + slideprefix + u + " not found.");
		}
		if (is != null) {
			try {
				String[] d;
				LineNumberReader f = new LineNumberReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				d ch = new d(true, -1);
				if (cha != null && cha.length() > 0) ch = new d(false, -2);
				else cha = "stop";
				String l = f.readLine();
				Vector<String[]> v = new Vector<String[]>();
				while (l != null) {
					if (l.length() > 4) {
						if (l.startsWith("=") && l.trim().endsWith("=")) {
							ch = e(new s(l), new s(cha), ch);
						}
						if (ch.b) {
							d = parseSlide(l);
							if (d.length > 0) {
								v.add(d);
							}
						}
					}
					l = f.readLine();
				}
				String[][] r = new String[v.size()][11];
				for (int i = 0; i < v.size(); i++) { r[i][0] = v.get(i)[0]; r[i][1] = v.get(i)[1]; r[i][2] = v.get(i)[2]; dbg += "<br>in array: " + i + " " + r[i][0]; }
				r = getsizes(r);
				return r;
			} catch (Exception e) {
				throw new IOException("Slideshow " + slideprefix + u + " read error: " + e);
			}
		}
		System.out.println("leere show " + new Date());
		return null;
	}
	private String     med(String pic, String[] com) {
		if ( pic == null ) return "<a onclick=history.back(); href=#>Back</a>";
		if ( com == null ) return "<a href=" + picinfo + pic + ">" + pic.substring(pic.lastIndexOf('/') + 1) + "</a> <a onclick=history.back(); href=#>Back</a>";
		return "<div id=fscreen><a href=\"javascript:flscn(); \">Activate Fullscreen mode</a><br></div>" +
				"<div id=pinfo>Author: <a href=javascript:alert('" + com[9] + "');>" + com[5] + "</a> License: <a href=" +
				com[7] + ">" + com[6] + "</a><br><a href=" + picinfo + pic + ">" + getCom(com[2]) + "</a></div>" + com[0] +
				"script...<script>vanish();</script>scripted";
	}
	public  String     getServletInfo () {
		String s = "Stereoskopie Version " + VERSION + " (" + (System.currentTimeMillis() - this.time) + this.enc + getLastModified(null) + ")";
		return s;
	}
}
