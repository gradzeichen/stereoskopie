import helper.img.genmrp;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Stereoskopie extends JFrame implements ChangeListener, ActionListener {

	stview l, r;
	int x, y, w, h, ow, oh;
	public static double rt = 1;
	public static boolean keepratio = true;
	boolean on = true;
	public static boolean    nx    = true;
	public static boolean    nx2   = true;
	public JButton    tglrt = new JButton("Toggle Keep Ratio");
	public JButton    skip  = new JButton("Skip");
	public JButton    save  = new JButton("Save");
	public JButton    tbx   = new JButton("Position");
	public JTextField tfx   = new JTextField();
	public JTextField tfy   = new JTextField();
	public JTextField tfn   = new JTextField();
	public JTextField tfb   = new JTextField();
	public JTextField tfl   = new JTextField();
	public JTextField tfs   = new JTextField();
	public File exif;
	public static String exiftool = "exiftool";
	public static File in = null;
	public File in2       = null;
	public static String od = "";
	public static String odx = "";
	public static String odj = "jps/";
	public static String odt = "thumb/";
	public static String od2 = "thumb2d/";
	public static String odw = "wiki/";
	public static String stl = "Brandenburg Gate";
	public static String sts = "Berlin";
	public static String usr = "Gradzeichen";
	public static int aktiv = 0;

	public BufferedImage b = null, b2 = null;
	public static genmrp mrp = new genmrp();

	public static void main(String[] args) {
		if ( args.length > 1 ) System.exit(99);
		String pp = null;
		if ( args.length == 1 ) pp = args[0];
		getProps(pp);
		File inf = new File("."); //od);
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Left Eye Images directory");
		fc.setApproveButtonText("Use directory for left eye images");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(null);
		inf = fc.getSelectedFile();
		fc.setVisible(false);
		try {
			File[] vl = inf.listFiles();
			Arrays.sort(vl);
			int z = 0;
			try {
				if ( !(new File(odx)).exists() ) (new File(odx)).mkdir();
				if ( !(new File(odj)).exists() ) (new File(odj)).mkdir();
				if ( !(new File(odt)).exists() ) (new File(odt)).mkdir();
				if ( !(new File(od2)).exists() ) (new File(od2)).mkdir();
				if ( !(new File(odw)).exists() ) (new File(odw)).mkdir();
				FileWriter w = new FileWriter(odx + "stereo.protokoll", true);
				w.write(("=== Session: " + od + "  --  " + new Date() + " ===\n* " + stl + "\n* " + sts + "\n"));
				w.flush();
				w.close();
				w = new FileWriter(odx + "slideshow.wiki", true);
				w.write(("== Session: " + od + "  --  " + new Date() + " ==\n* " + stl + "\n* " + sts + "\n"));
				w.flush();
				w.close();
				w = new FileWriter(odx + "slideshow.js", true);
				w.write(("== Session: " + od + "  --  " + new Date() + " ==\n* " + stl + "\n* " + sts + "\n"));
				w.flush();
				w.close();
			}
			catch ( Exception e) {
				e.printStackTrace();
			}
			fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setDialogTitle("Right Eye Images directory");
			fc.setApproveButtonText("Use directory for right eye images");
			fc.showOpenDialog(null);
			inf = fc.getSelectedFile();
			fc.setVisible(false);
			File[] vr = inf.listFiles();
			Arrays.sort(vr);

			boolean we = true;
			int j = 0;
			for (int i = 0; i < Math.max(vl.length,vr.length); ) {
				while ( we ) {
					if (nx) {
						if ( i < vl.length && ( vl[i].getName().endsWith(".jpl") || vl[i].getName().endsWith(".jpg") ) ) {
							if ( j < vr.length && ( vr[j].getName().endsWith(".jpr") || vr[j].getName().endsWith(".jpg") ) ) {
								aktiv++;
								nx = false;
								new Stereoskopie(vl[i++], vr[j++], od);
							}
							else if ( j < vr.length ) j++;
							else i = vl.length;
						}
						else i++;
						we = false;
					} else Thread.sleep(100);
				}
				we = true;
			}
			while ( aktiv > 0 ) Thread.sleep(2000);
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public Stereoskopie(File inputfile, File inputfile2, String odir) {
		super("Stereoskopie " + inputfile.getName() + " -- " + odir);
		in = inputfile;
		in2 = inputfile2;
		od = odir;
		if ( !od.endsWith("/") ) od += "/";
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		d.width -= 20;
		d.height -= 100;
		mrp.setPara("2,0,0,0,https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/3d_glasses_inficolor.svg/50px-3d_glasses_inficolor.svg.png");
		setSize(d);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		tglrt.addActionListener(this);
		skip.addActionListener(this);
		save.addActionListener(this);
		tbx.addActionListener(this);
		tfl.setText(stl);
		tfs.setText(sts);
		JPanel p = new JPanel();
		ScrollPane s = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		getContentPane().add(s);
		p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
		s.add(p);
		iPanel lp = new iPanel(true, this);
		iPanel rp = new iPanel(false, this);
		l = new stview(true,d, this, lp);
		r = new stview(false,d, this, rp);
		p.add(l);
		p.add(r);

		File f = inputfile;
		File f2 = inputfile;
		try {
			b = ImageIO.read(f);
			b2 = ImageIO.read(f);
		}
		catch ( Exception e ) {
			e.printStackTrace();
			System.exit(0);
		}
		exif = getexif(f);
		x = 0;
		y = 0;
		w = b.getWidth();
		h = b.getHeight();
		rt = ((double)(((double)w) / ((double)h)));
		ow = w;
		oh = h;
		on = false;
		l.xsh.setValue(0);
		l.xsv.setValue(100000);
		r.xsh.setValue(100000);
		r.xsv.setValue(0);
		l.p.setPic(b);
		r.p.setPic(b2);

		// meins!
		w = 1920;
		h = 1080;
		keepratio = true;
		// ende meins!

		on = true;
		setVisible(true);
		p.repaint();
	}

	public void stateChanged(ChangeEvent e) {
		Object o = e.getSource();
		if ( o == tbx || ( on && ( o == l.xsh ||  o == l.xsv ||  o == r.xsh ||  o == r.xsv ) ) ) {
			on = false;
			x = ( l.xsh.getValue() * ow ) / 100000;
			y = ( ( 100000 - l.xsv.getValue() ) * oh ) / 100000;
			w = ( r.xsh.getValue() * ow ) / 100000;
			h = ( ( 100000 - r.xsv.getValue() ) * oh ) / 100000;
			String h1 = "get x = " + x + " y = " + y + " w = " + w + " h = " + h + " -> ";
			if ( x + w > ow ) w = ow - x;
			if ( y + h > oh ) h = oh - y;
			h1 += "sht x = " + x + " y = " + y + " w = " + w + " h = " + h + " -> ";
			if ( keepratio ) {
				if (o == r.xsh) h = (int) (w / rt);
				else if (o == r.xsv) w = (int) (h * rt);
				else {
					double r1 = (double)w/(double)h;
					double rd = rt - r1;
					if ( rd < -0.01 ) h = (int) (w / rt);
					else if ( rd > 0.01 ) w = (int) (h * rt);
				}
			}

			// meins!
			if ( keepratio ) {
				w = 1920;
				h = 1080;
			}
			// ende meins!

			h1 += "rto  w = " + w + " h = " + h + " ratio = " + ((double)w/(double)h) + " -> ";
			r.xsh.setValue((w*100000/ow));
			r.xsv.setValue(100000-(h*100000/oh));
			l.u.setT("x = " + x +" y = " + y +" w = " + w + " h  = " + h +" ow  = " + ow +" oh  = " + oh + " ratio = " + ( h != 0 ? (double)w/(double)h : "DbZ" ) );
			l.p.setView(x,y,w,h);
			r.p.setView(x,y,w,h);
			l.p.repaint();
			r.p.repaint();
			l.u.repaint();
			r.u.repaint();
			tfx.setText("" + x);
			tfy.setText("" + y);
			on = true;
		}
	}

	public void actionPerformed( ActionEvent e ) {
		Object o = e.getSource();
		if ( o == tglrt ) {
			keepratio = !keepratio;
			if ( keepratio ) tglrt.setText("Keeping Ratio");
			else tglrt.setText("Variable Ratio");
		}
		else if ( o == skip ) skip();
		else if ( o == save ) save();
		else if ( o == tbx  ) {
			try {
				x = Integer.parseInt(tfx.getText());
				y = Integer.parseInt(tfy.getText());
				on = false;
				l.xsh.setValue((x*100000/ow));
				l.xsv.setValue(100000-(y*100000/oh));
				stateChanged(new ChangeEvent(o));
			}
			catch ( Exception f ) {
				f.printStackTrace();
			}
		};
	}

	public void save() {
		setVisible(false);
		l     = null;
		r     = null;
		tglrt = null;
		skip  = null;
		save  = null;
		tbx   = null;
		tfx   = null;
		tfy   = null;
		removeAll();
		nx = true;
		BufferedImage bx = new BufferedImage(w*2,h,BufferedImage.TYPE_INT_RGB);
		Graphics g = bx.getGraphics();
		g.drawImage(b.getSubimage(x,y,w,h),0,0,null);
		g.drawImage(b2.getSubimage(x,y,w,h),w,0,null);
		b = null;
		// jps schreiben
		File fx = new File(odj + tfn.getText() + ".jps");
		try {
			ImageIO.write(bx,"jpg",fx);
				// thumb schreiben
			File t = File.createTempFile("stereothumb",".gif");
			byte[] ba = new byte[0]; // mrp.generate(fx.toString(),"-160,1,5"); /// !!!!!!! error
			if ( false ) {
				if (ba.length > 150000) System.out.println("preview file size " + ba.length + " " + t);
				FileOutputStream fo = new FileOutputStream(t);
				fo.write(ba);
				fo.flush();
				fo.close();
			}
			File p00 = new File(odx + "previewerr0");
			File p01 = new File(odx + "previewerr1");
			File p02 = new File(odx + "previewerr2");
			File p03 = new File(odx + "previewerr3");
			try {
				Vector<String> v = new Vector<String>();
				v.add(exiftool);
				v.add("-overwrite_original");
				v.add("-Copyright=" + usr + " at commons.wikimedia.org, cc-by-sa-4.0; glasses logo: Timothy Gu at commons.wikimedia.org, cc-by-sa-3.0");
				v.add("-DocumentName=" + tfb.getText());
				v.add("-Contact=meta.wikimedia.org/wiki/user:" + usr);
				v.add(t.getAbsolutePath());
				ProcessBuilder p = new ProcessBuilder( v );
				p.redirectError(p00);
				Process pp = p.start();
			}
			catch ( Exception e ) {
				e.printStackTrace();
				System.exit(32);
			}
			// ImageIO.write(new BufferedImage(32,18,BufferedImage.TYPE_INT_RGB),"gif",t);
			// exif lesen
			LineNumberReader r = new LineNumberReader(new FileReader(exif));
			String x   = r.readLine();
			String n   = null;
			String iso = null;
			String ex  = null;
			String fn  = null;
			String le  = null;
			while ( x != null ) {
				StringTokenizer tt = new StringTokenizer(x,":");
				n = tt.nextToken().trim();
				if      ( n.equals("ISO")                 ) iso = tt.nextToken().trim();
				else if ( n.equals("Shutter Speed Value") ) ex  = tt.nextToken().trim();
				else if ( n.equals("Aperture Value")      ) fn  = tt.nextToken().trim();
				else if ( n.equals("Focal Length")        ) le  = tt.nextToken().trim();
				x = r.readLine();
			}
			exif  = null;
			// exif schreiben
			String out = null;
			try {
				Vector<String> v = new Vector<String>();
				v.add(exiftool);
				v.add("-overwrite_original");
				v.add("-Make=Gradzeichen Cam");
				v.add("-Model=G Cam 1.0");
				v.add("-ProcessingSoftware=Stereoskopie.jar 0.1, Stereoskopie.apk 0.1, Exiftool, ImageMagick");
				v.add("-Copyright=" + usr + " at commons.wikimedia.org, cc-by-sa-4.0");
				v.add("-DocumentName=" + tfb.getText());
				v.add("-Keywords=Stereoskopie, " + tfl.getText());
				v.add("-ContentLocationName=" + tfs.getText());
				v.add("-CopyrightNotice=" + usr + " at commons.wikimedia.org, cc-by-sa-4.0");
				v.add("-Contact=meta.wikimedia.org/wiki/user:" + usr);
				v.add("-Caption-Abstract=Stereoscopic Photo by " + usr + ". Attribution: \"" + usr + " &mdash; CC-BY-SA-4.0\"");
				v.add("-ExposureTime=" + ex);
				v.add("-FNumber=" + fn);
				v.add("-FocalLength=" + le);
				v.add("-ISO=" + iso);
				v.add("-ObjectPreviewData<=" + t.toString());
				v.add(fx.getAbsolutePath());
				ProcessBuilder p = new ProcessBuilder( v );
				p.redirectError(p01);
				Process pp = p.start();
			}
			catch ( Exception e ) {
				e.printStackTrace();
				System.exit(32);
			}
			// wiki schreiben
			FileWriter w = new FileWriter(odw + tfn.getText() + ".wiki",true);
			w.write(("{{3D|p}}\n== {{int:filedesc}} ==\n{{Information\n|Description= {{en|" + tfb.getText() + "}}\n" +
					"|Source={{self}}\n" +
					"|Date=2016\n|Author=" + usr + "\n" +
					"|Permission= \n|other_versions=[[{{PAGETITLE}}.thumb.gif]]\n|Other_fields_1={{Information field|Stereoscopic content|...}}\n" +
					"|Other_fields={{Information field|...}}\n}}\n== {{int:license-header}} == \n" +
					"{{cc-by-sa-4.0}}\n[[Category:Stereoscopic photos by " + usr + "]]\n"));
			w.flush();
			w.close();
			// gif schreiben
			t = new File(odt + tfn.getText() + ".jps.thumb.gif");
			ba =  new byte[0]; // mrp.generate(fx.getAbsolutePath(),"-360,1,5"); // !!!!!! error
			FileOutputStream fo = new FileOutputStream(t);
			fo.write(ba);
			fo.flush();
			fo.close();
			try {
				Vector<String> v = new Vector<String>();
				v.add(exiftool);
				v.add("-overwrite_original");
				v.add("-Make=Gradzeichen Cam");
				v.add("-Model=G Cam 1.0");
				v.add("-ProcessingSoftware=Stereoskopie.jar 0.1, Stereoskopie.apk 0.1, Exiftool, ImageMagick");
				v.add("-DocumentName=" + tfb.getText());
				v.add("-Keywords=Stereoskopie, " + tfl.getText());
				v.add("-ContentLocationName=" + tfs.getText());
				v.add("-Contact=meta.wikimedia.org/wiki/user:" + usr);
				v.add("-Caption-Abstract=Stereoscopic Photo by " + usr + ". glasses logo: Timothy Gu at commons.wikimedia.org, cc-by-sa-3.0. Attribution: \"" + usr + " &mdash; CC-BY-SA-4.0\"");
				v.add("-ExposureTime=" + ex);
				v.add("-FNumber=" + fn);
				v.add("-FocalLength=" + le);
				v.add("-ISO=" + iso);
				v.add("-Copyright=" + usr + " at commons.wikimedia.org, cc-by-sa-4.0; glasses logo: Timothy Gu at commons.wikimedia.org, cc-by-sa-3.0");
				v.add("-Contact=meta.wikimedia.org/wiki/user:" + usr);
				v.add(t.getAbsolutePath());
				ProcessBuilder p = new ProcessBuilder( v );
				p.redirectError(p02);
				Process pp = p.start();
			}
			catch ( Exception e ) {
				e.printStackTrace();
				System.exit(32);
			}
			t = new File(od2 + tfn.getText() + ".jps.2dthumb.gif");
			ba = new byte[0]; //  mrp.generate(fx.getAbsolutePath(),"-360,0,0"); // !!!!!! error
			fo = new FileOutputStream(t);
			fo.write(ba);
			fo.flush();
			fo.close();
			try {
				Vector<String> v = new Vector<String>();
				v.add(exiftool);
				v.add("-overwrite_original");
				v.add("-Make=Gradzeichen Cam");
				v.add("-Model=G Cam 1.0");
				v.add("-ProcessingSoftware=Stereoskopie.jar 0.1, Stereoskopie.apk 0.1, Exiftool, ImageMagick");
				v.add("-DocumentName=" + tfb.getText());
				v.add("-Keywords=Stereoskopie, " + tfl.getText());
				v.add("-ContentLocationName=" + tfs.getText());
				v.add("-Contact=meta.wikimedia.org/wiki/user:" +usr);
				v.add("-Caption-Abstract=Stereoscopic Photo by " + usr + ". glasses logo: Timothy Gu at commons.wikimedia.org, cc-by-sa-3.0. Attribution: \"" + usr + " &mdash; CC-BY-SA-4.0\"");
				v.add("-ExposureTime=" + ex);
				v.add("-FNumber=" + fn);
				v.add("-FocalLength=" + le);
				v.add("-ISO=" + iso);
				v.add("-Copyright=" + usr + " at commons.wikimedia.org, cc-by-sa-4.0; glasses logo: Timothy Gu at commons.wikimedia.org, cc-by-sa-3.0");
				v.add("-Contact=meta.wikimedia.org/wiki/user:" + usr);
				v.add(t.getAbsolutePath());
				ProcessBuilder p = new ProcessBuilder( v );
				p.redirectError(p03);
				Process pp = p.start();
			}
			catch ( Exception e ) {
				e.printStackTrace();
				System.exit(32);
			}
			//ImageIO.write(new BufferedImage(320,180,BufferedImage.TYPE_INT_RGB),"gif",t);
			// slideshow schreiben
			int z = 0;
			while ( !nx2 ) {
				Thread.sleep(500);
				z++;
				if ( z > 1000 ) {
					z = 0;
					JOptionPane.showMessageDialog(null, "Cannot write slideshow and protocol!");
				}
			}
			nx2 = false;
			w = new FileWriter(odx + "slideshow.wiki",true);
			w.write(("* [[File:" + tfn.getText() + ".jps.jpg|20px]] | 60 | " + tfb.getText() + "\n"));
			w.flush();
			w.close();
			w = new FileWriter(odx + "slideshow.js",true);
			w.write(("* " + tfn.getText() + ".jps.jpg | 60 | " + tfb.getText() + "\n"));
			w.flush();
			w.close();
			// protokoll schreiben
			w = new FileWriter(odx + "stereo.protokoll",true);
			w.write(("* " + tfn.getText() + ".jps.jpg | " + tfb.getText() + " | " + new Date() + " | " + in + "\n"));
			w.flush();
			w.close();
			nx2 = true;
			String q = in2.getAbsolutePath();
			int d = q.lastIndexOf('/') +1;
			in2.renameTo(new File(q.substring(0,d) + "ready/" + q.substring(d)));
			tfn   = null;
			tfb   = null;
			tfl   = null;
			tfs   = null;
			fx  = null;
			w   = null;
			g   = null;
			t   = null;
			bx  = null;
			ba  = null;
			fo  = null;
			r   = null;
			x   = null;
			n   = null;
			iso = null;
			ex  = null;
			fn  = null;
			le  = null;
			out = null;
			in2 = null;
			if ( p00.length() > 0 )	JOptionPane.showMessageDialog(null, "Exif err 0 " + getc(p00));
			if ( p01.length() > 0 )	JOptionPane.showMessageDialog(null, "Exif err 1 " + getc(p01));
			if ( p02.length() > 0 )	JOptionPane.showMessageDialog(null, "Exif err 2 " + getc(p02));
			if ( p03.length() > 0 )	JOptionPane.showMessageDialog(null, "Exif err 3 " + getc(p03));

			removeAll();
			dispose();
			finalize();
			System.gc();
		}
		catch ( Throwable e ) {
			e.printStackTrace();
			System.exit(42);
		}
		aktiv--;
	}

	private String getc(File f) {
		String s = "";
		String t = "";
		try {
			LineNumberReader r = new LineNumberReader(new FileReader(f));
			t = r.readLine();
			while (t != null && t.length() > 0) {
				s += t;
				t = r.readLine();
			}
		}
		catch ( Exception e ) {
			s += e.getMessage();
		}
		return s;
	}

	public void skip() {
		// protokoll schreiben
		nx = true;
		setVisible(false);
		l     = null;
		r     = null;
		tglrt = null;
		skip  = null;
		save  = null;
		tbx   = null;
		tfx   = null;
		tfy   = null;
		exif  = null;
		removeAll();
		int z = 0;
		try {
			while (!nx2) {
				Thread.sleep(500);
				z++;
				if (z > 1000) {
					z = 0;
					JOptionPane.showMessageDialog(null, "Cannot write protocol!");
				}
			}
			nx2 = false;
			FileWriter w = new FileWriter(odx + "stereo.protokoll", true);
			w.write(("* " + tfn.getText() + ".jps | *** skipped! *** | " + new Date() + " | " + in2 + "\n"));
			w.flush();
			w.close();
			System.out.println("rename b " + in2.getAbsoluteFile());
			String q = in2.getAbsolutePath();
			int d = q.lastIndexOf('/') +1;
			System.out.println(q.substring(0,d) + "ready/skipped-" + q.substring(d));
			in2.renameTo(new File(q.substring(0,d) + "ready/skipped-" + q.substring(d)));
			System.out.println("renamd b " + in2.getAbsoluteFile());
			nx2 = true;
			w = null;
			tfn   = null;
			tfb   = null;
			tfl   = null;
			tfs   = null;
			in2 = null;
			removeAll();
			dispose();
			finalize();
			System.gc();
		}
		catch ( Throwable e) {
			e.printStackTrace();
		}
		nx2 = true;
		aktiv--;
	}

	public File getexif(File f) {
		File f1 = null;
		try {
			f1 = File.createTempFile("stereo-exif",".exif");
			ProcessBuilder p = new ProcessBuilder( exiftool, f.toString());
			p.redirectOutput(f1);
			Process pp = p.start();
		}
		catch ( Exception e ) {
			e.printStackTrace();
			System.exit(23);
		}
		return f1;
	}

	public static void getProps(String in ) {
		Properties p = new Properties();
		try {
			if ( in == null ) in = "stereo.properties";
			p.load(new FileInputStream(in));
		} catch (Exception e) {
			e.printStackTrace();
		}
		exiftool = p.getProperty("exiftool", exiftool);
		od       = p.getProperty("od",       od      );
		if ( od.equals("eingabe") ) {
			od = in.substring(0,in.lastIndexOf('/'));
		}
		odj      = p.getProperty("odj",      odj     );
		odt      = p.getProperty("odt",      odt     );
		odx      = p.getProperty("odx",      odx     );
		od2      = p.getProperty("od2",      od2     );
		odw      = p.getProperty("odw",      odw     );
		sts      = p.getProperty("sts",      sts     );
		stl      = p.getProperty("stl",      stl     );
		usr      = p.getProperty("usr",      usr     );
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class stview extends JPanel {

	public pic p;
	Dimension d;
	public iPanel u;
	JSlider xsh;
	JSlider xsv;
	Stereoskopie s;

	stview(boolean b, Dimension d1, Stereoskopie st, iPanel u1) {
		super();
		d = d1;
		s = st;
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		JPanel p0 = new JPanel();
		p0.setLayout(new BorderLayout());
		add(p0);
		d.width /= 2;
		p = new pic(b,d);
		Color c = Color.CYAN;
		if ( b ) c = Color.ORANGE;
		setBackground(c);
		p0.add(p,BorderLayout.CENTER);
		xsh = new JSlider(JSlider.HORIZONTAL);
		xsh.setMaximum(100000);
		xsh.setMinimum(0);
		p0.add(xsh,BorderLayout.NORTH);
		xsv = new JSlider(JSlider.VERTICAL);
		xsv.setMaximum(100000);
		xsv.setMinimum(0);
		p0.add(xsv,BorderLayout.WEST);
		setSize(d);
		u = u1;
		add(u);
		u.setT("init");
		xsh.addChangeListener(st);
		xsv.addChangeListener(st);
	}

	public Dimension getPreferredSize() {
		if ( u != null ) {
			d.width = s.getWidth()/2;
			d.height = u.getHeight() + 10;
		}
		return d;
	}
	public Dimension getSize() {
		if ( u != null ) {
			d.width = s.getWidth()/2;
			d.height = s.getHeight();
		}
		if ( d.width  > getMaximumSize().width  ) d.width  = getMaximumSize().width;
		if ( d.width  < getMinimumSize().width  ) d.width  = getMinimumSize().width;
		if ( d.height > getMaximumSize().height ) d.height = getMaximumSize().height;
		if ( d.height < getMinimumSize().height ) d.height = getMinimumSize().height;
		return d;
	}
	public Dimension getMinimumSize()   { return new Dimension(160,   90); }
	public Dimension getMaximumSize()   { return new Dimension(3840,2160); }

	public void paint(Graphics g) {
		super.paint(g);
		p.repaint();
		u.repaint();
	}
}

class pic extends JComponent {

	BufferedImage bi = null;
	int x = 0, y = 0;
	double w = 1920, h = 1080, xw = w, xh = h, wo = w, ho = h;
	boolean l;

	pic(boolean links, Dimension d) {
		super();
		l = links;
		w = d.width;
		h = w * 9 / 16;
		xw = w;
		xh = h;
	}

	public void setPic(BufferedImage i) {
		bi = i;
		x = 0;
		y = 0;
		w = i.getWidth();
		h = i.getHeight();
		xw = w;
		xh = h;
		wo = w;
		ho = h;
		repaint();
	}

	public void setView(int x1, int y1, int w1, int h1) {
		x = x1;
		y = y1;
		w = w1;
		h = h1;
		//repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		String s = "Rchts";
		Color  c = Color.BLUE;
		if ( bi != null && x < w && y < h && x >= 0 && y >= 0 && w + x <= wo && h + y <= ho ) {
			xw = getWidth();
			xh = xw / (w/h);
			g.drawImage(bi.getSubimage(x,y,(int)w,(int)h).getScaledInstance((int)xw,(int)xh,0),0,0,null);
		}
		else {
			if ( l ) {
				s = "Links";
				c = Color.RED;
			}
			g.setColor(c);
			g.fillRect(0,0,getWidth(),getHeight());
			g.setColor(Color.BLACK);
			g.drawString(s,getWidth()/2,getHeight()/2);
			g.drawString(( bi != null ? bi.toString(): "null" ),10,getHeight()/2 + 20);
			g.drawString("x = " + x + " y = " + y,10,getHeight()/2 + 40);
			g.drawString("w = " + w + " h = " + h,10,getHeight()/2 + 60);
			g.drawString("wo = " + wo + " ho = " + ho,10,getHeight()/2 + 80);
			g.drawString("xw = " + xw + " xh = " + xh,10,getHeight()/2 + 100);
			g.drawString("rt = " + Stereoskopie.rt + " ratio = " + (w/h),10,getHeight()/2 + 120);
		}
	}

	public Dimension getSize()          { return new Dimension ((int)xw,(int)xh); }
	public Dimension getPreferredSize() {
		return new Dimension ((int)xw,(int)xh);
	}
	public Dimension getMinimumSize()   { return new Dimension ((int)wo/5,(int)ho/5); }
	public Dimension getMaximumSize()   { return new Dimension ((int)wo,(int)ho); }
}

class iPanel extends JPanel {

	private String t = "leer";
	Color c = null;
	JTextField tf = new JTextField(t);
	JTextField ak = new JTextField(Stereoskopie.aktiv + " files active");

	public iPanel(boolean links, Stereoskopie sk) {
		super();
		c = Color.DARK_GRAY;
		if ( links ) {
			c = Color.CYAN;
			setLayout(new GridLayout(7,1)); //BoxLayout(this,BoxLayout.Y_AXIS));
			add(tf);
			tf.setSize(1000,20);
			add(sk.tglrt);
			add(sk.skip);
			add(sk.save);
			add(sk.tfx);
			add(sk.tfy);
			add(sk.tbx);
		}
		else {
			setLayout(new GridLayout(6,2)); //BoxLayout(this,BoxLayout.Y_AXIS));
			add(new JTextField("name"));
			add(sk.tfn);
			sk.tfn.setText("GCam_" + (int)((Math.random()*700000)+100000));
			add(new JTextField("desc"));
			add(sk.tfb);
			add(new JTextField("thema"));
			add(sk.tfl);
			add(new JTextField("city"));
			add(sk.tfs);
			add(ak);
		}

	}

	public void setT(String i ) {
		t = i;
		tf.setText(t);
		ak.setText("active files: " + Stereoskopie.aktiv);
		repaint();
	}

	public String getT(){ return t; }

	public void paint(Graphics g) {
		g.setColor(c);
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(Color.BLACK);
		g.drawString(getT(),5,25);
		g.drawString(new Date().toString(),5,45);
		g.drawString("rt: " + Stereoskopie.rt,5,65);
	}

	public Dimension getPreferredSize() { return new Dimension (640, 200);  }
	public Dimension getMinimumSize()   { return new Dimension (160, 200);  }
	public Dimension getMaximumSize()   { return new Dimension (1000,200); }
}

class vPanel extends JPanel {

	public vPanel() {

		//.

	}

}