/**
 * 
 */
package com.ibm.dots.thread;

import java.util.LinkedHashMap;
import java.util.Map;

import lotus.domino.Database;
import lotus.domino.NotesException;
import lotus.domino.Session;

/**
 * @author nfreeman
 * 
 */
public class TrustedLocalCachedSession extends TrustedLocalSession {
	static class DbMap extends LinkedHashMap<String, Database> {
		DbMap(int size) {
			super(size, 1.0f);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
		 */
		@Override
		protected boolean removeEldestEntry(java.util.Map.Entry<String, Database> paramEntry) {
			try {
				paramEntry.getValue().recycle();
			} catch (Throwable whoCares) {

			}
			return super.removeEldestEntry(paramEntry);
		}
	}

	private DbMap dbMap_; // no need to synchronize becase this is always thread local.
	private final int cacheSize_;

	public TrustedLocalCachedSession(int cacheSize) {
		cacheSize_ = cacheSize;
	}

	private DbMap getDbMap() {
		if (dbMap_ == null) {
			dbMap_ = new DbMap(cacheSize_);
		}
		return dbMap_;
	}

	public Database getDatabase(String filepath) {
		Database result = null;
		Map<String, Database> dbMap = getDbMap();
		if (!dbMap.containsKey(filepath)) {
			Session s = get();
			try {
				result = s.getDatabase("", filepath);
				dbMap.put(filepath, result);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			result = dbMap.get(filepath);
			try {
				result.isOpen();
			} catch (NotesException recycleSucks) {
				Session s = get();
				try {
					result = s.getDatabase("", filepath);
					dbMap.put(filepath, result);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.dots.thread.TrustedLocalSession#clear()
	 */
	@Override
	public void clear() {
		super.clear(); // no need
		getDbMap().clear();
	}

}
