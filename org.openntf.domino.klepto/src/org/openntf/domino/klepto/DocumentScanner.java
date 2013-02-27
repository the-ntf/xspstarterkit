/**
 * 
 */
package org.openntf.domino.klepto;

import java.util.Map;
import java.util.NavigableSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.RichTextItem;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.tokenize.Tokenizer;

/**
 * @author nfreeman
 * 
 */
public class DocumentScanner {

	private DatabaseWatcher parentWatcher_;

	/**
	 * 
	 */
	private DocumentScanner() {

	}

	public DocumentScanner(DatabaseWatcher parent) {
		setParent(parent);
	}

	public DatabaseWatcher getParent() {
		return parentWatcher_;
	}

	@SuppressWarnings("unchecked")
	public void processDocument(Document doc) {
		Vector<Item> items = null;
		try {
			items = doc.getItems();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (items != null) {
			Map<String, NavigableSet<String>> tmap = getParent().getFieldTokenMap();
			Map<String, Integer> tfmap = getParent().getTokenFreqMap();
			SentenceDetector sd = Activator.getSentenceDetector();
			Tokenizer tokenizer = Activator.getTokenizer();
			for (Item item : items) {
				String value = null;
				try {
					switch (item.getType()) {
					case Item.TEXT:
						value = item.getValueString();
						break;
					case Item.RICHTEXT:
						value = ((RichTextItem) item).getUnformattedText();
						break;
					default:

					}
					if (value != null && value.length() > 0 && !KleptoUtils.isNumber(value)) {
						// System.out.println("Non-null value found in " + item.getName() + ". Tokenizing: " + value);
						NavigableSet<String> tokenSet = null;

						if (!tmap.containsKey(item.getName())) {
							tokenSet = new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER);
							tmap.put(item.getName(), tokenSet);
						} else {
							tokenSet = tmap.get(item.getName());
						}
						synchronized (sd) {
							String[] sentences = sd.sentDetect(value);
							for (String sentence : sentences) {
								synchronized (tokenizer) {
									String[] tokens = tokenizer.tokenize(sentence);
									for (String token : tokens) {
										token = token.trim();
										if (token.length() > 2) {
											// char[] chars = token.toCharArray();
											// if (!Character.isLetterOrDigit(chars[0])) {
											// System.out.println("First character for token " + token + " is " + chars[0]);
											// }
											// if (!Character.isLetterOrDigit(chars[chars.length - 1])) {
											// System.out.println("Last character for token " + token + " is " + chars[chars.length - 1]);
											// }
											tokenSet.add(token);
											if (tfmap.containsKey(token)) {
												tfmap.put(token, tfmap.get(token) + 1);
											} else {
												tfmap.put(token, 1);
											}
										}
									}
								}
							}
						}
					}
					item.recycle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setParent(DatabaseWatcher parent) {
		parentWatcher_ = parent;
	}

}
