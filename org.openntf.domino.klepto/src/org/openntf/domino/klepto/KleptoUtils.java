/**
 * 
 */
package org.openntf.domino.klepto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import lotus.domino.ACL;
import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.DbDirectory;
import lotus.domino.Document;
import lotus.domino.Form;
import lotus.domino.Item;
import lotus.domino.NoteCollection;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewColumn;
import lotus.domino.ViewEntry;
import lotus.domino.ViewNavigator;
import lotus.notes.NotesThread;
import lotus.notes.addins.DominoServer;

/**
 * @author nfreeman
 * 
 */
public class KleptoUtils {

	public static void demoViewDangers(Database db) throws NotesException {
		@SuppressWarnings("unchecked")
		Vector<View> views = db.getViews();
		for (View view : views) {
			String name = view.getName();
			view.setAutoUpdate(false);
			if ("isCalendar".equalsIgnoreCase(name)) {
				view.isCalendar();
			} else if ("isCategorized".equalsIgnoreCase(name)) {
				view.isCategorized();
			} else if ("isDefault".equalsIgnoreCase(name)) {
				view.isDefaultView();
			} else if ("isCalendar".equalsIgnoreCase(name)) {
				view.isCalendar();
			} else if ("isFolder".equalsIgnoreCase(name)) {
				view.isFolder();
			} else if ("isHierarchical".equalsIgnoreCase(name)) {
				view.isHierarchical();
			} else if ("isPrivate".equalsIgnoreCase(name)) {
				view.isPrivate();
			} else if ("isProhibitDesignRefresh".equalsIgnoreCase(name)) {
				view.isProhibitDesignRefresh();
			} else if ("isProtectReaders".equalsIgnoreCase(name)) {
				view.isProtectReaders();
			} else if ("isQueryView".equalsIgnoreCase(name)) {
				view.isQueryView();
			}
			view.recycle();
		}
	}

	public static List<String> getAllRoles(Database db) throws NotesException {
		List<String> result = new ArrayList<String>();
		ACL acl = db.getACL();
		Vector<?> roles = acl.getRoles();
		if (!roles.isEmpty()) {
			for (Object role : roles) {
				result.add("[" + (String) role + "]");
			}
		}
		acl.recycle();
		return result;
	}

	public static int[] getBuiltViewIds(Database db) throws NotesException {
		int[] result;
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllIndexElements(true);
		nc.setSelectionFormula("@IsAvailable($Collection)");
		nc.buildCollection();
		result = nc.getNoteIDs();
		nc.recycle();
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Set<Object> getCategorizedValues(View view) throws NotesException {
		Set<Object> result = new HashSet<Object>();
		if (isIndexed(view))
			for (ViewColumn column : (Vector<ViewColumn>) view.getColumns()) {
				if (column.isCategory()) {
					int pos = column.getPosition() - 1;
					ViewNavigator nav = view.createViewNav();
					ViewEntry entry = nav.getFirst();
					ViewEntry nextEntry;
					while (entry != null) {
						if (entry.isCategory()) {
							nextEntry = nav.getNextSibling(entry);
							Vector vals = entry.getColumnValues();
							if (vals.size() > pos && vals.get(pos) != null) {
								result.add(vals.get(pos));
							}
						} else {
							nextEntry = nav.getNext(entry);
						}
						entry.recycle();
						entry = nextEntry;
					}
					nav.recycle();
				}
				column.recycle();
			}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Set<Object>> getCategorizedViewContents(Database db) throws NotesException {
		Map<String, Set<Object>> result = new HashMap<String, Set<Object>>();
		for (View view : (Vector<View>) db.getViews()) {
			String viewName = view.getName();
			result.put(viewName, getCategorizedValues(view));
		}
		return result;
	}

	public static int[] getDataIds(Database db) throws NotesException {
		int[] result;
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		result = nc.getNoteIDs();
		nc.recycle();
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Set<String> getEveryAuthor(Database db) throws NotesException {
		Set<String> result = new HashSet<String>();
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		while (nid != null) {
			Document doc = db.getDocumentByID(nid);
			for (Item item : (Vector<Item>) doc.getItems()) {
				if (item.isAuthors()) {
					result.addAll(item.getValues());
				}
				item.recycle();
			}
			doc.recycle();
			nid = nc.getNextNoteID(nid);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, Integer>> getEveryEditorCountByForm(Database db) throws NotesException {
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		while (nid != null) {
			Document doc = db.getDocumentByID(nid);
			String form = doc.getItemValueString("form");
			Item item = doc.getFirstItem("$UpdatedBy");
			for (String name : (Vector<String>) item.getValues()) {
				if (result.containsKey(name)) {
					Map<String, Integer> formMap = result.get(name);
					if (formMap.containsKey(form)) {
						formMap.put(form, formMap.get(form) + 1);
					} else {
						formMap.put(form, 1);
					}
				} else {
					Map<String, Integer> formMap = new HashMap<String, Integer>();
					formMap.put(form, 1);
					result.put(name, formMap);
				}
			}
			item.recycle();
			doc.recycle();
			nid = nc.getNextNoteID(nid);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, Integer>> getEveryEditorCountByFormWithGroups(Database db) throws NotesException {
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		while (nid != null) {
			Document doc = db.getDocumentByID(nid);
			String form = doc.getItemValueString("form");
			Item item = doc.getFirstItem("$UpdatedBy");
			for (String name : (Vector<String>) item.getValues()) {
				Collection<String> allNames = (Collection<String>) getServerNames(name);
				for (String groupname : allNames) {
					if (result.containsKey(groupname)) {
						Map<String, Integer> formMap = result.get(groupname);
						if (formMap.containsKey(form)) {
							formMap.put(form, formMap.get(form) + 1);
						} else {
							formMap.put(form, 1);
						}
					} else {
						Map<String, Integer> formMap = new HashMap<String, Integer>();
						formMap.put(form, 1);
						result.put(groupname, formMap);
					}
				}
			}
			item.recycle();
			doc.recycle();
			nid = nc.getNextNoteID(nid);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Set<String> getEveryName(Database db) throws NotesException {
		Set<String> result = new HashSet<String>();
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		while (nid != null) {
			Document doc = db.getDocumentByID(nid);
			for (Item item : (Vector<Item>) doc.getItems()) {
				if (item.isNames()) {
					result.addAll(item.getValues());
				}
				item.recycle();
			}
			doc.recycle();
			nid = nc.getNextNoteID(nid);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Integer> getEveryNameCount(Database db) throws NotesException {
		Map<String, Integer> result = new HashMap<String, Integer>();
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		while (nid != null) {
			Document doc = db.getDocumentByID(nid);
			for (Item item : (Vector<Item>) doc.getItems()) {
				if (item.isNames()) {
					for (String name : (Vector<String>) item.getValues()) {
						if (result.containsKey(name)) {
							result.put(name, result.get(name) + 1);
						} else {
							result.put(name, 1);
						}
					}
				}
				item.recycle();
			}
			doc.recycle();
			nid = nc.getNextNoteID(nid);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, Integer>> getEveryNameCountByForm(Database db) throws NotesException {
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		while (nid != null) {
			Document doc = db.getDocumentByID(nid);
			String form = doc.getItemValueString("form");
			for (Item item : (Vector<Item>) doc.getItems()) {
				if (item.isNames()) {
					for (String name : (Vector<String>) item.getValues()) {
						if (result.containsKey(name)) {
							Map<String, Integer> formMap = result.get(name);
							if (formMap.containsKey(form)) {
								formMap.put(form, formMap.get(form) + 1);
							} else {
								formMap.put(form, 1);
							}
						} else {
							Map<String, Integer> formMap = new HashMap<String, Integer>();
							formMap.put(form, 1);
							result.put(name, formMap);
						}
					}
				}
				item.recycle();
			}
			doc.recycle();
			nid = nc.getNextNoteID(nid);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Set<String> getEveryReader(Database db) throws NotesException {
		Set<String> result = new HashSet<String>();
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		while (nid != null) {
			Document doc = db.getDocumentByID(nid);
			for (Item item : (Vector<Item>) doc.getItems()) {
				if (item.isReaders()) {
					result.addAll(item.getValues());
				}
				item.recycle();
			}
			doc.recycle();
			nid = nc.getNextNoteID(nid);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Set<Object>> getFieldValues(Database db) throws NotesException {
		Map<String, Set<Object>> result = new HashMap<String, Set<Object>>();
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.buildCollection();
		String nid = nc.getFirstNoteID();
		while (nid != null) {
			Document doc = db.getDocumentByID(nid);
			doc.setPreferJavaDates(true);
			for (Item item : (Vector<Item>) doc.getItems()) {
				String name = item.getName();

				if (result.containsKey(name)) {
					result.get(name).addAll(item.getValues());
				} else {
					result.put(name, new HashSet<Object>(item.getValues()));
				}

				item.recycle();
			}
			doc.recycle();
			nid = nc.getNextNoteID(nid);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Collection<String>> getFormFields(Database db) throws NotesException {
		Map<String, Collection<String>> result = new HashMap<String, Collection<String>>();
		for (Form form : (Vector<Form>) db.getForms()) {
			result.put(form.getName(), form.getFields());
		}
		return result;
	}

	public static int getFormInstanceCount(Database db, String form) throws NotesException {
		int result;
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDataNotes(true);
		nc.setSelectionFormula("Form=\"" + form + "\"");
		nc.buildCollection();
		result = nc.getCount();
		nc.recycle();
		return result;
	}

	public static int[] getInheritedDesignElements(Database db) throws NotesException {
		int[] result;
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllDesignElements(true);
		nc.setSelectionFormula("@IsAvailable($Class)");
		nc.buildCollection();
		result = nc.getNoteIDs();
		nc.recycle();
		return result;
	}

	public static int[] getNaughtyViewIds(Database db) throws NotesException {
		int[] result;
		NoteCollection nc = db.createNoteCollection(false);
		nc.selectAllIndexElements(true);
		nc.setSelectionFormula("@IsAvailable($Collection) & @IsAvailable($FormulaTV)");
		nc.buildCollection();
		result = nc.getNoteIDs();
		nc.recycle();
		return result;
	}

	public static Collection<?> getServerNames(String searchName) throws NotesException {
		DominoServer server = new DominoServer(); // UNDOCUMENTED! UNSUPPORTED! UNAFRAID!
		return server.getNamesList(searchName);
	}

	public static Map<String, List<String>> getTemplateMap(DbDirectory dir) throws NotesException {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		Database curDb = dir.getFirstDatabase(DbDirectory.DATABASE);
		while (curDb != null) {
			if (curDb.open()) {
				String templateName = curDb.getTemplateName();
				String filepath = curDb.getFilePath();
				if (!result.containsKey(templateName)) {
					List<String> curList = new ArrayList<String>();
					curList.add(filepath);
					result.put(templateName, curList);
				} else {
					List<String> curList = result.get(templateName);
					curList.add(filepath);
				}
			}

			curDb.recycle();
		}

		return result;
	}

	public static Session getTrustedSession() throws NotesException {
		NotesThread.sinitThread();
		return NotesFactory.createTrustedSession(); // only available in Java, and only if the .jar is loaded from the local OS
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Collection<String>> getViewColumns(Database db) throws NotesException {
		Map<String, Collection<String>> result = new HashMap<String, Collection<String>>();
		for (View view : (Vector<View>) db.getViews()) {
			result.put(view.getName(), view.getColumnNames());
		}
		return result;
	}

	public static void handleException(Throwable t) {
		String mess = t.getMessage();
		if (mess != null && mess.length() > 0) {
			// KleptoUtils.log("Message: " + mess);
		}
		t.printStackTrace();
	}

	public static void incinerate(Object... args) {
		for (Object o : args) {
			if (o != null) {
				if (o instanceof Base) {
					try {
						((Base) o).recycle();
					} catch (Throwable t) {
						// who cares?
					}
				} else if (o instanceof Collection) {
					if (o instanceof Map) {
						Set<Map.Entry> entries = ((Map) o).entrySet();
						for (Map.Entry<?, ?> entry : entries) {
							KleptoUtils.incinerate(entry.getKey(), entry.getValue());
						}
					} else {
						Iterator i = ((Collection) o).iterator();
						while (i.hasNext()) {
							Object obj = i.next();
							KleptoUtils.incinerate(obj);
						}
					}
				} else if (o.getClass().isArray()) {
					try {
						Object[] objs = (Object[]) o;
						for (Object ao : objs) {
							KleptoUtils.incinerate(ao);
						}
					} catch (Throwable t) {
						// who cares?
					}
				}
			}
		}
	}

	public static boolean isHex(String value) {
		String chk = value.trim().toLowerCase();
		for (int i = 0; i < chk.length(); i++) {
			char c = chk.charAt(i);
			boolean isHexDigit = Character.isDigit(c) || Character.isWhitespace(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd'
					|| c == 'e' || c == 'f' || Character.getType(c) == 15;

			if (!isHexDigit) {
				// System.out.println("Not a hex at position " + i + " (" + Character.getType(c) + ") " + ": " + String.valueOf(value));
				return false;
			}

		}
		return true;
	}

	public static boolean isIndexed(View view) throws NotesException {
		boolean result = false;
		String unid = view.getUniversalID();
		Document doc = view.getParent().getDocumentByUNID(unid);
		result = doc.hasItem("$Collection");
		doc.recycle();
		return result;
	}

	public static boolean isNumber(String value) {
		boolean seenDot = false;
		boolean seenExp = false;
		boolean justSeenExp = false;
		boolean seenDigit = false;
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c >= '0' && c <= '9') {
				seenDigit = true;
				continue;
			}
			if ((c == '-' || c == '+') && (i == 0 || justSeenExp)) {
				continue;
			}
			if (c == '.' && !seenDot) {
				seenDot = true;
				continue;
			}
			justSeenExp = false;
			if ((c == 'e' || c == 'E') && !seenExp) {
				seenExp = true;
				justSeenExp = true;
				continue;
			}
			return false;
		}
		if (!seenDigit) {
			return false;
		}
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isSept(String value) {
		String chk = value.trim().toLowerCase();
		for (int i = 0; i < chk.length(); i++) {
			char c = chk.charAt(i);
			boolean isSeptDigit = Character.isDigit(c) || Character.isWhitespace(c) || c == 'a' || c == 'b' || c == 'c' || c == 'd'
					|| c == 'e' || c == 'f' || c == 'g' || Character.getType(c) == 15;

			if (!isSeptDigit) {
				// System.out.println("Not a hex at position " + i + " (" + Character.getType(c) + ") " + ": " + String.valueOf(value));
				return false;
			}

		}
		return true;
	}

	public static Date toJavaDateSafe(DateTime dt) {
		Date date = null;
		if (dt != null) {
			try {
				date = dt.toJavaDate();
			} catch (NotesException ne) {
				// do nothing
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				try {
					dt.recycle();
				} catch (NotesException recycleSucks) {
					// what would you even do?
				}
			}
		}
		return date;
	}

	public KleptoUtils() {
	}

}
