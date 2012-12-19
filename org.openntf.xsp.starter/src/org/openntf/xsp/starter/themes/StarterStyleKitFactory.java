/**
 * 
 */
package org.openntf.xsp.starter.themes;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.openntf.xsp.starter.Activator;

import com.ibm.xsp.stylekit.StyleKit;
import com.ibm.xsp.stylekit.StyleKitFactory;

/**
 * @author nfreeman
 * 
 */
public class StarterStyleKitFactory implements StyleKitFactory {
	private final Map<String, String> _themes = new HashMap<String, String>();
	private final Map<String, StyleKit> _kits = new HashMap<String, StyleKit>();
	private final static boolean _debug = Activator.isDebug();
	static {
		if (_debug)
			System.out.println(StarterStyleKitFactory.class.getName() + " loaded");
	}

	public StarterStyleKitFactory() {

		_themes.put("starter", "/resources/themes/starter.theme");

		if (_debug)
			System.out.println(getClass().getName() + " constructed");
	}

	public void addStyleKit(String arg0, StyleKit arg1) {
		_kits.put(arg0, arg1);
	}

	public StyleKit getStyleKit(FacesContext arg0, String arg1) {
		return _kits.get(arg0);
	}

	public Iterator<String> getStyleKitIds() {
		return _kits.keySet().iterator();
	}

	public InputStream getThemeAsStream(String themeId, int scope) {
		if (_debug)
			System.out.println(getClass().getName() + " : Theme Scope : " + scope);
		if (_debug)
			System.out.println(getClass().getName() + " : Theme ID : " + themeId);
		InputStream result = null;
		// if(scope==StyleKitFactory.STYLEKIT_APPLICATION) { //8.5.3 only?
		if (_themes.containsKey(themeId)) {
			if (_debug)
				System.out.println(getClass().getName() + " : Loading theme : " + themeId);
			result = getThemeFromBundle(_themes.get(themeId));
		}
		// }
		return result;
	}

	public InputStream getThemeFragmentAsStream(String themeId, int scope) {
		if (_debug)
			System.out.println(getClass().getName() + " : ThemeFragment Scope : " + scope);
		if (_debug)
			System.out.println(getClass().getName() + " : ThemeFragment ID : " + themeId);
		return null;
	}

	private InputStream getThemeFromBundle(final String fileName) {
		ClassLoader cl = getClass().getClassLoader();
		return cl.getResourceAsStream(fileName);
	}
}
