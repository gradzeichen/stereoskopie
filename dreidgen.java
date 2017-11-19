abstract public class dreidgen extends dreidhelper {

	static String GVERSION = "0.1";

	String vr   (String md, String s, int dur)           {
		int tp = tpe(s);
		if ( tp == 0 ) return sserver + "/forward/jps/" + md + s;
		if ( tp == 1 ) return sserver + "/gen/vr/" + md + s + "?d=" + dur; // stream/sbs2vr/
		if ( tp == 3 ) return sserver + "/forward/vid/" + md + s;
		//return imgprf2 + "/" + md + s;
		return pic(null);
	}
	String sbs  (String md, String s, int dur)           {
		int tp = tpe(s);
		if ( tp == 0 ) return sserver + "/gen/sbs/" + md + s;
		if ( tp == 1 ) return sserver + "/forward/vid/" + md + s;
		if ( tp == 3 ) return sserver + "/gen/sbs/" + md + s+ "?d=" + dur;
		return pic(null);
	}
	String ttb  (String md, String s, int dur)           {
		int tp = tpe(s);
		if ( tp == 0 ) return sserver + "/gen/tab/" + md + s;
		if ( tp == 2 ) return sserver + "/forward/vid/" + md + s;
		if ( tp == 3 ) return sserver + "/gen/tab/" + md + s+ "?d=" + dur;
		return pic(null);
	}
	String crx  (String md, String s, int dur)           { return sserver + "/gen/crx/" + md + s;    }
	String lnt  (String md, String s, String c, int dur) {
		return sserver + "/gen/lenticular/" + dec.format(lenticularsiz) + "/" + dec.format(uhd) + md + s;
	}
	String morph(String md, String s, int dur)           { return sserver + "/gen/morph/" + md + s;  }
	String pol  (String md, String s, String c, int dur) {
		return sserver + "/gen/pol/" + c + "/" + md + s;
	}
	String hp   (String md, String s, int dur)           { return sserver + "/gen/holo/" + md + s;   }
	String dl   (String md, String s, int dur)           {
		int i = s.indexOf(".jps");
		if (i > 0) s = s.substring(0, i) + ".jps?post=" + s.substring(i + 4);
		return sserver + "/gen/dl/" + md + s;
	}
	String app  (String md, String s, int dur)           {
		int i = s.indexOf(".jps");
		if (i > 0) s = s.substring(0, i) + ".jps?post=" + s.substring(i + 4);
		return sserver + "/gen/app/" + md + s;
	}
	String anag (String md, String s, String c, int dur) {
		return sserver + "/gen/ana/" + c.substring(9, 14) + "/" + c.substring(16, 21) + md + s;
	}
	int    tpe  (String in)                     {
		int tp = 4;
		if      ( in.indexOf(".jps")        > 0 ) tp = 0;
		else if ( in.indexOf(".sbs.")       > 0 ) tp = 1;
		else if ( in.indexOf(".tab.")       > 0 ) tp = 2;
		else if ( in.indexOf(".vrglasses.") > 0 ) tp = 3;
		else if ( in.indexOf(".cardboard.") > 0 ) tp = 3;
		return tp;
	}
}
