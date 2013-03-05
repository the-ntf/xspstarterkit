package org.openntf.domino.klepto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.designer.domino.napi.NotesConstants;
import com.ibm.designer.domino.napi.NotesDatabase;
import com.ibm.designer.domino.napi.NotesDatetime;
import com.ibm.designer.domino.napi.NotesItem;
import com.ibm.designer.domino.napi.NotesNote;
import com.ibm.designer.domino.napi.NotesNoteItem;
import com.ibm.designer.domino.napi.NotesSession;
import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.model.DataObject;

/**
 * @author nfreeman
 * 
 */
public class DatabaseWatcher implements Serializable, DataObject {
	private static final long serialVersionUID = 1L;
	private transient NotesDatabase ndb_;
	private transient NotesDatetime lastWatch_;
	private Calendar lastWatchJava_;
	private final String ndbPath_;
	private String persistenceId;

	private final Map<String, NavigableSet<String>> fieldTokenMap_ = new HashMap<String, NavigableSet<String>>();

	private final NavigableMap<String, Integer> tokenFreqMap_ = new ConcurrentSkipListMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);

	public static void saveState(DatabaseWatcher watcher) {

	}

	public DatabaseWatcher(String path) {
		ndbPath_ = path;

	}

	static class DocSignerComparator implements Comparator<lotus.domino.Document> {
		@Override
		public int compare(lotus.domino.Document d1, lotus.domino.Document d2) {
			int result = 0;
			try {
				d1.getSigner().compareTo(d2.getSigner());
			} catch (NotesException ne) {
				ne.printStackTrace();

			}
			return result;
		}
	}

	private void _dumpTokenMap() {
		for (String key : getFieldTokenMap().keySet()) {
			NavigableSet<String> val = getFieldTokenMap().get(key);
			StringBuilder sb = new StringBuilder();
			for (String v : val) {
				sb.append(v);
				sb.append(", ");
			}
			System.out.println(key + ": " + sb.toString());

		}
	}

	public DocumentCollection findDeltaDocColl() throws Exception {
		DateTime dt = getLastWatchTime();
		DocumentCollection result = getDb().search("@True", dt);
		dt.recycle();

		return result;
	}

	public int[] findDeltaNoteids() throws Exception {
		return getNAPIDb().searchNOTEID(NotesConstants.NOTE_CLASS_DATA, getLastWatch());
	}

	public String getDbPath() {
		return ndbPath_;
	}

	public Database getDb() throws Exception {
		Session s = NotesContext.getCurrent().getCurrentSession();
		return s.getDatabase("", ndbPath_);
	}

	public Set<String> getFieldNames() {
		return fieldTokenMap_.keySet();
	}

	public Map<String, NavigableSet<String>> getFieldTokenMap() {
		return fieldTokenMap_;
	}

	public static final SortedSet<String> EMPTY_SORTEDSET = Collections.unmodifiableSortedSet(new ConcurrentSkipListSet<String>());

	public SortedSet<String> getFieldTokens(String fieldname) {
		if (!getFieldTokenMap().containsKey(fieldname)) {
			return EMPTY_SORTEDSET;
		} else {
			return getFieldTokenMap().get(fieldname);
		}
	}

	public String getFieldTokensTypeahead(String fieldname, String prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for (String token : getFieldTokensFromPrefix(fieldname, prefix)) {
			sb.append("<li>").append(token).append("</li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}

	public String getAllTokensTypeahead(String prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for (String token : getAllTokensFromPrefix(prefix)) {
			sb.append("<li>").append(token).append("</li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}

	public SortedSet<String> getFieldTokensFromPrefix(String fieldname, String prefix) {
		SortedSet<String> set = getFieldTokens(fieldname);
		if (!set.isEmpty()) {
			SortedSet<String> result = new TreeSet<String>();
			Set<String> matches = set.tailSet(prefix);
			for (String match : matches) {
				if (match.toLowerCase().startsWith(prefix.toLowerCase())) {
					result.add(match);
				} else {
					StringBuilder res = new StringBuilder();
					for (String s : result) {
						res.append(s).append(", ");
					}
					// System.out.println("Result: " + res.toString());
					return result;
				}
			}
			// System.out.println("Tailset end reached. " + result.size() + " matches returning...");
			return result;
		} else {
			// System.out.println("NO TOKENS FOUND IN FIELD " + fieldname + " for prefix " + prefix);
			return EMPTY_SORTEDSET;
		}
	}

	public SortedSet<String> getAllTokensFromPrefix(String prefix) {
		SortedSet<String> set = ((ConcurrentSkipListMap<String, Integer>) getTokenFreqMap()).keySet();
		if (!set.isEmpty()) {
			SortedSet<String> result = new TreeSet<String>();
			Set<String> matches = set.tailSet(prefix);
			for (String match : matches) {
				if (match.toLowerCase().startsWith(prefix.toLowerCase())) {
					result.add(match);
				} else {
					StringBuilder res = new StringBuilder();
					for (String s : result) {
						res.append(s).append(", ");
					}
					// System.out.println("Result: " + res.toString());
					return result;
				}
			}
			// System.out.println("Tailset end reached. " + result.size() + " matches returning...");
			return result;
		} else {
			// System.out.println("NO TOKENS FOUND for prefix " + prefix);
			return EMPTY_SORTEDSET;
		}
	}

	public NotesDatetime getLastWatch() throws Exception {
		if (lastWatch_ == null) {
			Calendar c = lastWatchJava_;
			if (c == null) {

				lastWatch_ = new NotesDatetime();
				lastWatch_.adjust(-50, 0, 0, 0, 0, 0);
			} else {
				lastWatch_ = new NotesDatetime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
						c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
			}
		}
		return lastWatch_;
	}

	public DateTime getLastWatchTime() throws Exception {
		Session s = NotesContext.getCurrent().getCurrentSession();
		if (lastWatchJava_ != null) {
			return s.createDateTime(lastWatchJava_);
		} else {
			return s.createDateTime(new Date(0));
		}
	}

	public NotesDatabase getNAPIDb() throws Exception {
		if (ndb_ == null || ndb_.isRecycled()) {
			NotesSession s = NotesContext.getCurrent().getNotesSession();
			ndb_ = s.getDatabaseByPath(ndbPath_);
		}
		return ndb_;
	}

	public int getTokenFreq(String token) {
		if (getTokenFreqMap().containsKey(token)) {
			return getTokenFreqMap().get(token);
		} else {
			return 0;
		}
	}

	public Map<String, Integer> getTokenFreqMap() {
		return tokenFreqMap_;
	}

	public NavigableSet<String> getTokenSet(String fieldName) {
		return fieldTokenMap_.get(fieldName);
	}

	public boolean isScanned() {
		return !getFieldTokenMap().isEmpty();
	}

	private boolean isSeptSet(Set<String> set) {
		for (String str : set) {
			if (KleptoUtils.isSept(str)) {
				continue;
			}
			return false;
		}
		return true;
	}

	public void scanDocs() {
		System.out.println("Scanning...");
		DocumentScanner scanner = new DocumentScanner(this);
		try {
			DocumentCollection coll = this.findDeltaDocColl();
			if (coll.getCount() > 0) {
				System.out.println("Beginning processing " + coll.getCount() + " documents through NLP tokenizer");
				lastWatchJava_ = new GregorianCalendar();
				int i = 0;
				Document doc = coll.getFirstDocument();
				Document ndoc;
				while (doc != null) {
					ndoc = coll.getNextDocument(doc);
					scanner.processDocument(doc);
					doc.recycle();
					doc = ndoc;
					if (++i % 1000 == 0) {
						System.out.println("Processed " + i + " documents through NLP tokenizer");
					}
				}
				System.out.println("Completed processing " + i + " documents through NLP tokenizer");
			}
			this.getDb().recycle();
			trimFieldTokenMap();
			trimTokenFreqMap();
			// _dumpTokenMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void scanNotes() {
		int[] noteids = new int[0];
		try {
			noteids = findDeltaNoteids();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (noteids.length > 0) {

			for (int i : noteids) {
				try {
					NotesNote note = getNAPIDb().openNote(i, NotesConstants.OPEN_NOVERIFYDEFAULT + NotesConstants.OPEN_NOOBJECTS);
					String[] items = note.getItemNames();
					for (String itemname : items) {
						NotesNoteItem item = note.getItem(itemname);
						if (!(item.isDatetime() || item.isNumber())) {
							System.out.println(itemname + ": " + NotesItem.getTypeString(item.getType()) + " ("
									+ item.getValue().getClass().getName() + ")");
						}
						item.recycle();
					}
					note.recycle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			getNAPIDb().recycle();
		} catch (Throwable recycleSucks) {
			recycleSucks.printStackTrace();
		}
	}

	private void trimFieldTokenMap() {
		Set<String> removeSet = new HashSet<String>();
		for (String key : getFieldTokenMap().keySet()) {
			NavigableSet<String> val = getFieldTokenMap().get(key);
			if (val.isEmpty())
				removeSet.add(key);
			if (isSeptSet(val)) {
				removeSet.add(key);
			}
		}
		for (String key : removeSet) {
			getFieldTokenMap().remove(key);
		}
	}

	private void trimTokenFreqMap() {
		Set<String> removeSet = new HashSet<String>();
		for (String key : getTokenFreqMap().keySet()) {

			if (key.isEmpty())
				removeSet.add(key);
			if (KleptoUtils.isSept(key)) {
				removeSet.add(key);
			}
		}
		for (String key : removeSet) {
			getTokenFreqMap().remove(key);
		}
	}

	public String getPersistenceId() {
		return persistenceId;
	}

	public void setPersistenceId(String persistenceId) {
		this.persistenceId = persistenceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.DataObject#getType(java.lang.Object)
	 */
	@Override
	public Class<?> getType(Object arg0) {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.DataObject#getValue(java.lang.Object)
	 */
	@Override
	public Object getValue(Object arg0) {
		if (arg0 instanceof String) {
			return getAllTokensTypeahead((String) arg0);
		} else {
			return getAllTokensTypeahead(arg0.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.DataObject#isReadOnly(java.lang.Object)
	 */
	@Override
	public boolean isReadOnly(Object arg0) {
		return true;
	}

	private final NavigableMap<String, Integer> selectionFreqMap_ = new ConcurrentSkipListMap<String, Integer>();

	private void mapSelection(String token) {
		if (selectionFreqMap_.containsKey(token)) {
			selectionFreqMap_.put(token, selectionFreqMap_.get(token) + 1);
		} else {
			selectionFreqMap_.put(token, Integer.valueOf(1));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.DataObject#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setValue(Object arg0, Object arg1) {
		String s1 = (String) arg0;
		String s2 = (String) arg1;
		if ("".equalsIgnoreCase(s1)) {
			mapSelection(s2);
		}

	}

}
