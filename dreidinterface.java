package helper;

import helper.exp.dreidexception;
import java.io.InputStream;

public interface dreidinterface {

	public final static int video =    1; public final static int sbs   =    2; public final static int pol   =    4;
	public final static int ana   =    8; public final static int holo  =   16; public final static int vr    =   32;
	public final static int app   =   64; public final static int morph =  128; public final static int lent  =  256;
	public final static int crx   =  512; public final static int ttb   = 1024; public final static int svg   = 2048;
	public final static int clent = 4096; public final static int frttb = 8192;

	abstract dreidinterface getInst();

	abstract String setPara(String para);

	abstract InputStream generate(String file, String para, int duration) throws dreidexception;

	abstract int getInfo();
}