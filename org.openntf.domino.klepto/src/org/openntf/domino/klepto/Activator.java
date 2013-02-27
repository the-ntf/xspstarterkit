package org.openntf.domino.klepto;

import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin {
	public static final String PLUGIN_ID = Activator.class.getPackage().getName();
	public static final boolean _debug = false;

	public static Activator instance;

	private static String version;

	public static Activator getDefault() {
		return instance;
	}

	public static SentenceDetector getSentenceDetector() {
		if (getDefault().sentenceDetector_ == null) {
			getDefault().trainSentenceDetector();
		}
		return getDefault().sentenceDetector_;
	}

	public static Tokenizer getTokenizer() {
		if (getDefault().tokenizer_ == null) {
			getDefault().trainTokenParser();
		}
		return getDefault().tokenizer_;
	}

	public static String getVersion() {
		if (version == null) {
			version = (String) instance.getBundle().getHeaders().get("Bundle-Version");
		}
		return version;
	}

	private SentenceDetector sentenceDetector_;

	private Tokenizer tokenizer_;

	public Activator() {
		instance = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// try {
		// AdapterLogger.NOTESCONTEXT.setLogLevel(Level.FINEST.intValue());
		// Thread t = new NotesThread(new SessionTester());
		// t.run();
		// } catch (Throwable t) {
		// t.printStackTrace();
		// }
	}

	// private static class SessionTester implements Runnable {
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Runnable#run()
	// */
	// @Override
	// public void run() {
	// try {
	// NotesContext nc = NotesContext.getCurrentUnchecked();
	// if (nc == null) {
	// System.out.println("NotesContext is null");
	// Session s = NotesFactory.createTrustedSession();
	// s.setConvertMime(false);
	// } else {
	// System.out.println("NotesContext is valid");
	// NotesSession s = nc.getNotesSession();
	//
	// NotesDatabase ndb = s.getDatabase("", "names.nsf");
	// System.out.println("SUCCESS! " + ndb.getDatabasePath());
	// s.recycle();
	// // s.setConvertMime(false);
	// }
	// } catch (Throwable t) {
	// KleptoUtils.handleException(t);
	// }
	//
	// }
	//
	// }

	private void trainSentenceDetector() {
		try {
			System.out.println("Beginning sentence training...");
			SentenceModel sentenceModel = AccessController.doPrivileged(new PrivilegedAction<SentenceModel>() {
				@Override
				public SentenceModel run() {
					try {

						Bundle bundle = getBundle();
						InputStream modelIn = bundle.getEntry("resources/en/en-sent.bin").openStream();
						SentenceModel result = new SentenceModel(modelIn);
						modelIn.close();
						return result;
					} catch (Exception e) {
						KleptoUtils.handleException(e);
					}
					return null;
				}
			});
			sentenceDetector_ = new SentenceDetectorME(sentenceModel);
			System.out.println("Completed sentence training.");
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	private void trainTokenParser() {
		try {
			System.out.println("Beginning token training...");

			TokenizerModel tokenModel = AccessController.doPrivileged(new PrivilegedAction<TokenizerModel>() {
				@Override
				public TokenizerModel run() {
					try {

						Bundle bundle = getBundle();
						InputStream modelIn = bundle.getEntry("resources/en/en-token.bin").openStream();
						TokenizerModel result = new TokenizerModel(modelIn);
						modelIn.close();
						return result;
					} catch (Exception e) {
						KleptoUtils.handleException(e);
					}
					return null;
				}
			});

			tokenizer_ = new TokenizerME(tokenModel);
			System.out.println("Completed token training.");
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

}
