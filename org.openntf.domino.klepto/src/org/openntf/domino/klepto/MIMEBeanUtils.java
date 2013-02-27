/**
 * 
 */
package org.openntf.domino.klepto;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.domino.Stream;

import com.ibm.designer.domino.napi.NotesDatabase;
import com.ibm.designer.domino.napi.NotesSession;
import com.ibm.designer.domino.napi.dxl.DXLImporter;

/**
 * @author nfreeman
 * 
 */
public class MIMEBeanUtils {
	private static String UNID_PLACEHOLDER = "unid='@@@@@@@@@@@@@@@@@@@@'";
	private static String DXL_HEADER;
	private static String DXL_FOOTER;
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0'?>").append(System.getProperty("line.separator"));
		sb.append("<document xmlns='http://www.lotus.com/dxl'").append(System.getProperty("line.separator"));
		sb.append("<noteinfo ").append(UNID_PLACEHOLDER).append(">").append(System.getProperty("line.separator"));
		sb.append("<item name='KleptoData'>");
		sb.append("<rawitemdata type='19'>");
		DXL_HEADER = sb.toString();
		StringBuilder sbFoot = new StringBuilder();
		sbFoot.append("</rawitemdata></item></document>");
		DXL_FOOTER = sbFoot.toString();
	}

	static public class BeanReader implements Runnable {
		private final String filepath_;
		private final String unid_;
		private final String itemName_;
		private final PipedOutputStream os_;

		public BeanReader(PipedOutputStream os, String filepath, String unid, String itemName) {
			filepath_ = filepath;
			unid_ = unid;
			itemName_ = itemName;
			os_ = os;
		}

		@Override
		public void run() {
			try {
				Session s = NotesFactory.createTrustedSession();
				s.setConvertMIME(false);
				Database db = s.getDatabase("", filepath_);
				Document doc = db.getDocumentByUNID(unid_);

				Stream mimeStream = s.createStream();
				MIMEEntity entity = doc.getMIMEEntity(itemName_);
				entity.getContentAsBytes(mimeStream);
				mimeStream.getContents(os_);

				doc.save();
				mimeStream.recycle();
				entity.recycle();
				doc.recycle();
				s.setConvertMIME(true);
				s.recycle();
			} catch (Throwable t) {
				handleException(t);
			}
		}

	}

	static public class Deserializer implements Runnable {
		private final PipedInputStream is_;
		private GZIPInputStream gStream;
		private ObjectInputStream objectStream;
		private Serializable restored;
		private boolean ready = false;

		public Deserializer(PipedInputStream is) {
			is_ = is;
		}

		@Override
		public void run() {
			try {
				// System.out.println("Running deserializer!!");
				gStream = new GZIPInputStream(is_);
				objectStream = new ObjectInputStream(gStream);
				restored = (Serializable) objectStream.readObject();
				// if (restored != null) {
				// System.out.println("Deserialized a " + restored.getClass().getName());
				// } else {
				// System.out.println("Deserializer didn't restore anything");
				// }
				ready = true;
			} catch (Throwable t) {
				handleException(t);
			}
		}

		public Serializable getRestored() {
			return restored;
		}

		public boolean isReady() {
			return ready;
		}
	}

	static public class Serializer implements Runnable {
		private final Serializable target_;
		private final PipedOutputStream os_;
		private GZIPOutputStream gStream;
		private ObjectOutputStream objectStream;

		public Serializer(PipedOutputStream os, Serializable target) {
			target_ = target;
			os_ = os;
		}

		@Override
		public void run() {
			try {
				gStream = new GZIPOutputStream(os_);
				objectStream = new ObjectOutputStream(gStream);
				objectStream.writeObject(target_);
				objectStream.flush();
				objectStream.close();
			} catch (Throwable t) {
				handleException(t);
			}
		}

	}

	static public class BeanWriter implements Runnable {
		private final String filepath_;
		private final String unid_;
		private final String itemName_;
		private final PipedInputStream is_;
		private boolean ready_ = false;

		public BeanWriter(PipedInputStream is, String filepath, String unid, String itemName) {
			filepath_ = filepath;
			unid_ = unid;
			itemName_ = itemName;
			is_ = is;
			System.out.println("BeanWriter constructor for " + filepath + ": " + unid);
		}

		public boolean isReady() {
			return ready_;
		}

		@Override
		public void run() {
			System.out.println("BeanWriter running...");
			try {

				NotesSession.notesInitThread();
				NotesSession s = new NotesSession();
				NotesDatabase db = s.getDatabaseByPath(filepath_);
				DXLImporter importer = new DXLImporter(db);

				if (s != null) {
					// lotus.domino.local.Session s1;
					//
					// s.setConvertMIME(false);
					// Database db = s.getDatabase("", filepath_);
					// Document doc = db.getDocumentByUNID(unid_);
					// if (cleanPriorContents(doc)) {
					// doc = db.getDocumentByUNID(unid_);
					// }
					// Stream mimeStream = s.createStream();
					// MIMEEntity entity = doc.createMIMEEntity(itemName_);
					// ready_ = true;
					// mimeStream.setContents(is_);
					// entity.setContentFromBytes(mimeStream, "application/gzip-compressed", MIMEEntity.ENC_IDENTITY_BINARY);
					//
					// is_.close();
					// doc.save();
					// mimeStream.recycle();
					// entity.recycle();
					// doc.recycle();
					// s.setConvertMIME(true);
					// s.recycle();
				}// NotesThread.stermThread();
			} catch (Throwable t) {
				handleException(t);
			}
			System.out.println("BeanWriter complete.");
		}

	}

	public static void handleException(Throwable t) {
		t.printStackTrace(); // TODO replace with preferred handling
	}

	private static boolean cleanPriorContents(Document doc, String itemName) throws lotus.domino.NotesException {
		boolean cleaned = false;
		MIMEEntity previousState = doc.getMIMEEntity(itemName);
		if (previousState != null) {
			previousState.remove();
			doc.save();
			previousState.recycle();
			doc.recycle();
			cleaned = true;
		}
		return cleaned;
	}

	// public static Serializable restoreState(String filepath, String unid, String itemName) {
	// Serializable result = null;
	// try {
	// int BUFFER = 2048;
	// PipedInputStream sink = new PipedInputStream(BUFFER);
	// PipedOutputStream source = new PipedOutputStream(sink);
	//
	// BeanReader reader = new BeanReader(source, filepath, unid, itemName);
	// NotesThread runner = new NotesThread(reader);
	// runner.start();
	// // runner.join();
	//
	// GZIPInputStream gStream = new GZIPInputStream(sink);
	// ObjectInputStream objectStream = new ObjectInputStream(gStream);
	// Serializable restored = (Serializable) objectStream.readObject();
	//
	// result = restored;
	// } catch (Exception e) {
	// KleptoUtils.handleException(e);
	// }
	// return result;
	// }

	// public static void saveState(final Serializable object, String filepath, String unid, String itemName) {
	// System.out.println("Beginning save state...");
	// try {
	// int BUFFER = 2048;
	// PipedInputStream sink = new PipedInputStream(BUFFER);
	// PipedOutputStream source = new PipedOutputStream(sink);
	// GZIPOutputStream gStream = new GZIPOutputStream(source);
	// ObjectOutputStream objectStream = new ObjectOutputStream(gStream);
	//
	// BeanWriter writer = new BeanWriter(sink, filepath, unid, itemName);
	// Thread runner = new Thread(writer);
	// System.out.println("Starting writer...");
	// runner.start();
	// int count = 0;
	// while (!writer.isReady() && count < 10) {
	// Thread.sleep(100);
	// count++;
	//
	// }
	//
	// if (writer.isReady()) {
	// System.out.println("Beginning write of object...");
	// objectStream.writeObject(object);
	// objectStream.flush();
	// System.out.println("Completed object write...");
	// } else {
	// System.out.println("Writer never readied. Aborting...");
	// }
	// objectStream.close();
	// } catch (Throwable t) {
	// KleptoUtils.handleException(t);
	// }
	// }

	private static final HashMap<String, String> TEST_MAP = new HashMap<String, String>();

	public static void testRestore(final Session s, String filepath, String unid, String itemName) {
		System.out.println("Testing restore state...");
		Serializable result = null;
		try {
			int BUFFER = 2048;
			s.setConvertMIME(false);
			PipedOutputStream source = new PipedOutputStream();
			PipedInputStream sink = new PipedInputStream(source, BUFFER);

			Deserializer deserializer = new Deserializer(sink);
			System.out.println("Starting deserializer...");
			Thread runner = new Thread(deserializer);
			runner.start();
			System.out.println("Deserializer thread running");

			Database db = s.getDatabase("", filepath);
			Document doc = db.getDocumentByUNID(unid);

			Stream mimeStream = s.createStream();
			MIMEEntity entity = doc.getMIMEEntity(itemName);
			System.out.println("Got MIME Entity");
			entity.getContentAsBytes(mimeStream);
			System.out.println("Entity contents fed to stream");
			mimeStream.getContents(source);
			System.out.println("Stream bound to pipe");

			int count = 0;
			while (!deserializer.isReady()) {
				if (++count % 100 == 0) {
					System.out.println("Deserializer isn't ready yet. Waiting...");
				}
				// Thread.yield();
				Thread.sleep(100);
				if (count > 10000) {
					break;
				}
			}
			if (deserializer.isReady()) {

				result = deserializer.getRestored();
				if (result != null) {
					System.out.println("Deserialized object is " + result.getClass().getName());
				} else {
					System.out.println("result was NULL!");
				}
			} else {
				System.out.println("Deserializer wasn't ready in time. :-(");
			}

			mimeStream.recycle();
			entity.recycle();
			doc.recycle();
			db.recycle();
			s.setConvertMIME(true);
		} catch (Exception e) {
			KleptoUtils.handleException(e);
		}
		if (result instanceof HashMap<?, ?>) {
			HashMap<String, String> map = (HashMap<String, String>) result;
			for (String key : map.keySet()) {
				System.out.println(key + ": " + map.get(key));
			}
		}

	}

	public static void testSave(final Session s, String filepath, String unid, String itemName) {
		System.out.println("Testing save state...");
		TEST_MAP.put("This", "Me");
		TEST_MAP.put("That", "Myself");
		TEST_MAP.put("The Other", "I");
		TEST_MAP.put("This2", "Me");
		TEST_MAP.put("That2", "Myself");
		TEST_MAP.put("The Other2", "I");
		TEST_MAP.put("This3", "Me");
		TEST_MAP.put("That3", "Myself");
		TEST_MAP.put("The Other3", "I");
		TEST_MAP.put("This4", "Me");
		TEST_MAP.put("That4", "Myself");
		TEST_MAP.put("The Other4", "I");
		TEST_MAP.put("This5", "Me");
		TEST_MAP.put("That5", "Myself");
		TEST_MAP.put("The Other5", "I");
		TEST_MAP.put("This6", "Me");
		TEST_MAP.put("That6", "Myself");
		TEST_MAP.put("The Other6", "I");
		TEST_MAP.put("This7", "Me");
		TEST_MAP.put("That7", "Myself");
		TEST_MAP.put("The Other7", "I");
		TEST_MAP.put("This8", "Me");
		TEST_MAP.put("That8", "Myself");
		TEST_MAP.put("The Other8", "I");
		TEST_MAP.put("This9", "Me");
		TEST_MAP.put("That9", "Myself");
		TEST_MAP.put("The Other9", "I");
		try {
			int BUFFER = 2048;
			s.setConvertMIME(false);
			Database db = s.getDatabase("", filepath);
			Document doc = db.getDocumentByUNID(unid);
			if (cleanPriorContents(doc, itemName)) {
				doc = db.getDocumentByUNID(unid);
			}
			MIMEEntity entity = doc.createMIMEEntity(itemName);
			Stream mimeStream = s.createStream();

			PipedOutputStream source = new PipedOutputStream();
			PipedInputStream sink = new PipedInputStream(source, 128);
			BufferedInputStream bis = new BufferedInputStream(sink);

			Serializer serializer = new Serializer(source, TEST_MAP);

			Thread runner = new Thread(serializer);
			System.out.println("Starting serializer...");
			runner.start();
			System.out.println("Serializer running.");

			System.out.println("Setting sink to mimestream.");
			mimeStream.setContents(bis);
			System.out.println("Setting entity contents...");
			if (mimeStream.getBytes() != 0) {
				entity.setContentFromBytes(mimeStream, "application/gzip-compressed", MIMEEntity.ENC_IDENTITY_BINARY);
				System.out.println("Bytes set from stream contents.");
			}
			sink.close();

			doc.save();
			mimeStream.recycle();
			entity.recycle();
			doc.recycle();
			s.setConvertMIME(true);
		} catch (Throwable t) {
			KleptoUtils.handleException(t);
		}
	}

	public static Serializable restoreState2(final Session s, String filepath, String key, String itemName) {
		Serializable result = null;
		try {
			int BUFFER = 2048;
			s.setConvertMIME(false);
			PipedOutputStream source = new PipedOutputStream();
			PipedInputStream sink = new PipedInputStream(source, BUFFER);

			Deserializer deserializer = new Deserializer(sink);
			Thread runner = new Thread(deserializer);
			runner.start();

			Database db = s.getDatabase("", filepath);
			String unid = md5(key);
			Document doc = null;
			try {
				doc = db.getDocumentByUNID(unid);
			} catch (NotesException ne) {
				return null;
			}
			Stream mimeStream = s.createStream();
			MIMEEntity entity = doc.getMIMEEntity(itemName);
			entity.getContentAsBytes(mimeStream);
			mimeStream.getContents(source);

			mimeStream.recycle();
			entity.recycle();
			doc.recycle();
			db.recycle();
			s.setConvertMIME(true);
			int count = 0;
			while (!deserializer.isReady()) {
				if (++count % 100 == 0) {
					System.out.println("Deserializer isn't ready yet. Waiting...");
				}
				// Thread.yield();
				Thread.sleep(100);
				if (count > 1000) {
					break;
				}
			}

			result = deserializer.getRestored();
		} catch (Exception e) {
			KleptoUtils.handleException(e);
		}
		return result;
	}

	public static void saveState2(final Serializable object, final Session s, String filepath, String key, String itemName) {
		// System.out.println("Beginning save state...");
		try {
			int BUFFER = 2048;
			s.setConvertMIME(false);
			Database db = s.getDatabase("", filepath);
			String unid = md5(key);
			Document doc = null;
			try {
				doc = db.getDocumentByUNID(unid);
			} catch (NotesException ne) {
				doc = db.createDocument();
				doc.setUniversalID(unid);
			}
			if (cleanPriorContents(doc, itemName)) {
				doc = db.getDocumentByUNID(unid);
			}
			MIMEEntity entity = doc.createMIMEEntity(itemName);
			Stream mimeStream = s.createStream();

			PipedOutputStream source = new PipedOutputStream();
			PipedInputStream sink = new PipedInputStream(source, BUFFER);

			Serializer serializer = new Serializer(source, object);

			Thread runner = new Thread(serializer);
			// System.out.println("Starting serializer...");
			runner.start();
			// System.out.println("Serializer running.");

			// System.out.println("Setting sink to mimestream.");
			mimeStream.setContents(sink);

			entity.setContentFromBytes(mimeStream, "application/gzip-compressed", MIMEEntity.ENC_IDENTITY_BINARY);
			// System.out.println("Bytes set from stream contents.");

			sink.close();

			if (!doc.hasItem("key")) {
				doc.replaceItemValue("key", key);
				doc.replaceItemValue("form", "WatcherHolder");
			}
			doc.save();
			mimeStream.recycle();
			entity.recycle();
			doc.recycle();
			s.setConvertMIME(true);
		} catch (Throwable t) {
			KleptoUtils.handleException(t);
		}
	}

	public static String checksum(byte[] bytes, String alg) {
		String hashed = "";
		byte[] defaultBytes = bytes;
		try {
			MessageDigest algorithm = MessageDigest.getInstance(alg);
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (byte element : messageDigest) {
				String hex = Integer.toHexString(0xFF & element);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

			hashed = hexString.toString();
		} catch (Throwable t) {
			handleException(t);
		}
		return hashed;
	}

	public static String checksum(Serializable object, String algorithm) {
		String result = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(object);
			result = checksum(baos.toByteArray(), algorithm);
			out.close();
		} catch (Throwable t) {
			handleException(t);
		}
		return result;
	}

	public static String md5(Serializable object) {
		return checksum(object, "MD5");
	}

}
