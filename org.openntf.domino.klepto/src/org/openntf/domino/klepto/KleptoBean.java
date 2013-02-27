/**
 * 
 */
package org.openntf.domino.klepto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Session;

import com.ibm.domino.xsp.module.nsf.NotesContext;

/**
 * @author nfreeman
 * 
 */
public class KleptoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, DatabaseWatcher> watchers_;
	private String kleptoPath_;

	public KleptoBean() {
		System.out.println("New KleptoBean constructed!");
		try {
			Database kleptoBase = NotesContext.getCurrent().getCurrentDatabase();
			kleptoPath_ = kleptoBase.getFilePath();
		} catch (Throwable t) {
			KleptoUtils.handleException(t);
		}
	}

	public DatabaseWatcher addWatcherTarget(String filepath) {
		if (!getWatchers().containsKey(filepath)) {
			System.out.println("Watcher for path " + filepath + " not found. Creating new one...");
			Object o = MIMEBeanUtils
					.restoreState2(NotesContext.getCurrent().getSessionAsSigner(), getKleptoPath(), filepath, "watcherData");
			if (o == null) {
				getWatchers().put(filepath, new DatabaseWatcher(filepath));
			} else {
				DatabaseWatcher dw = (DatabaseWatcher) o;
				getWatchers().put(filepath, dw);
				dw.scanDocs();
			}

		}
		return getWatchers().get(filepath);
	}

	public DatabaseWatcher get(String name) {
		if (getWatchers().containsKey(name)) {
			return getWatchers().get(name);
		} else {
			Object o = MIMEBeanUtils.restoreState2(NotesContext.getCurrent().getSessionAsSigner(), getKleptoPath(), name, "watcherData");
			if (o == null) {
				System.out.println("Could not find " + name + " in watchers.");
				return null;
			} else {
				DatabaseWatcher dw = (DatabaseWatcher) o;
				getWatchers().put(name, dw);
				dw.scanDocs();
				return dw;
			}
		}
	}

	private Database getKleptoDb() throws lotus.domino.NotesException {
		Session s = NotesContext.getCurrent().getSessionAsSigner();
		Database db = s.getDatabase("", getKleptoPath());
		return db;
	}

	private String getKleptoPath() {
		return kleptoPath_;
	}

	public void write() {
		System.out.println("Beginning watcher serialization");
		for (DatabaseWatcher watcher : getWatchers().values()) {
			String persistPath = getKleptoPath();
			// String unid = null;
			// if (watcher.getPersistenceId() == null) {
			// try {
			// Document doc = getKleptoDb().createDocument();
			// doc.replaceItemValue("filepath", watcher.getDbPath());
			// doc.replaceItemValue("form", "WatcherHolder");
			// doc.save();
			// unid = doc.getUniversalID();
			// doc.recycle();
			// watcher.setPersistenceId(unid);
			// } catch (Throwable t) {
			// KleptoUtils.handleException(t);
			// }
			// } else {
			// unid = watcher.getPersistenceId();
			// }
			// System.out.println("writing persistence for " + watcher.getDbPath() + " to unid " + unid);
			MIMEBeanUtils.saveState2(watcher, NotesContext.getCurrent().getSessionAsSigner(), persistPath, watcher.getDbPath(),
					"watcherData");

			// DatabaseWatcher clone = (DatabaseWatcher) MIMEBeanUtils.restoreState2(NotesContext.getCurrent().getSessionAsSigner(),
			// persistPath, watcher.getDbPath(), "watcherData");
			// StringBuilder sb = new StringBuilder();
			// for (String name : clone.getFieldNames()) {
			// sb.append(name).append(", ");
			// }
			// System.out.println(sb.toString());
		}
	}

	public void testSerialization() {
		String persistPath = getKleptoPath();
		String unid = null;
		try {
			Document doc = getKleptoDb().createDocument();
			doc.replaceItemValue("form", "WatcherHolder");
			doc.save();
			unid = doc.getUniversalID();
			doc.recycle();
		} catch (Throwable t) {
			KleptoUtils.handleException(t);
		}
		MIMEBeanUtils.testSave(NotesContext.getCurrent().getSessionAsSigner(), persistPath, unid, "watcherData");
		MIMEBeanUtils.testRestore(NotesContext.getCurrent().getSessionAsSigner(), persistPath, unid, "watcherData");
	}

	public Map<String, DatabaseWatcher> getWatchers() {
		if (watchers_ == null) {
			System.out.println("Klepto watchers is null. Generating new map...");
			watchers_ = new HashMap<String, DatabaseWatcher>();
		}
		return watchers_;
	}
}
