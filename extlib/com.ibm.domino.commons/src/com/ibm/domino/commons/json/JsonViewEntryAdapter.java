/* ***************************************************************** */
/* Licensed Materials - Property of IBM                              */
/*                                                                   */
/* Copyright IBM Corp. 1985, 2013 All Rights Reserved                */
/*                                                                   */
/* US Government Users Restricted Rights - Use, duplication or       */
/* disclosure restricted by GSA ADP Schedule Contract with           */
/* IBM Corp.                                                         */
/*                                                                   */
/* ***************************************************************** */






package com.ibm.domino.commons.json;

import static com.ibm.domino.commons.json.JsonConstants.FROM_PROP;
import static com.ibm.domino.commons.json.JsonConstants.HREF_PROP;
import static com.ibm.domino.commons.json.JsonConstants.TO_PROP;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.NotesException;
import lotus.domino.ViewEntry;

import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.domino.commons.internal.Logger;
import com.ibm.domino.commons.names.Person;

/**
 * Adapts a ViewEntry to a JsonObject.
 * 
 * <p>Rather than using the raw column names for each view entry, this class
 * lets you specify the JSON properties for selected column indexes.  When you 
 * construct a <code>JsonViewEntryAdapter</code> for output, you supply a 
 * <code>Map</code> of JSON property names to columns.
 * 
 * <p>This class currently does NOT handle parsing of view entries.
 */
public class JsonViewEntryAdapter implements JsonObject {
    
    private static SimpleDateFormat ISO8601 = getUtcFormatter();
    
    private ViewEntry _entry = null;
    private String[] _propertyNames = null;
    private Vector<?> _values = null;
    private URI _baseUri = null;
    private Map<String, Column> _columns = null;
    
    public static class Column {
        
        private int _index;
        private String _displayName;
        private String _programmaticName;
        
        public Column(int index, String displayName, String programmaticName) {
            _index = index;
            _displayName = displayName;
            _programmaticName = programmaticName;
        }
        
        public int getIndex() {
            return _index;
        }

        public String getDisplayName() {
            return _displayName;
        }

        public String getProgrammaticName() {
            return _programmaticName;
        }

    }

    /**
     * Constructor used when generating JSON output.
     * 
     * @param entry The view entry.
     * @param baseUrl The base URL (for the href property)
     * @param columns A map of display names and columns.
     */
    public JsonViewEntryAdapter(ViewEntry entry, URI baseUri, Map<String, Column> columns) {
        _entry = entry;
        _baseUri = baseUri;
        _columns = columns;
        
        try {
            _values = entry.getColumnValues();
        }
        catch (NotesException e) {
            // Shouldn't happen
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.util.io.json.JsonObject#getJsonProperties()
     */
    public Iterator<String> getJsonProperties() {
        return new Iterator<String>() {
            
            private int _index = 0;
            
            public boolean hasNext() {
                String properties[] = getProperties();
                return _index < properties.length ;
            }

            public String next() {
                String properties[] = getProperties();
                return properties[_index++];
            }

            public void remove() {
                // The JSON IO classes shouldn't call remove
            }

            private String[] getProperties() {
                if ( _propertyNames != null ) {
                    return _propertyNames;
                }

                List<String> properties = new ArrayList<String>();
                
                // Add href property
                
                properties.add(HREF_PROP);
                
                // Add all column properties
                
                Set<String> keys = _columns.keySet();
                Iterator<String> iterator = keys.iterator();
                while ( iterator.hasNext() ) {
                    properties.add(iterator.next());
                }
                
                // Convert to array
                
                String[] array = new String[properties.size()];
                iterator = properties.iterator();
                for ( int i = 0; iterator.hasNext(); i++ ) {
                    array[i] = iterator.next();
                }
                
                // Cache the array for next time
                _propertyNames = array;
                
                return _propertyNames;
            }
        };
    }
    
    private boolean isPersonColumn(String propertyName) {
        boolean person = false;
        
        if ( TO_PROP.equals(propertyName) || FROM_PROP.equals(propertyName) ) {
            person = true;
        }
        
        return person;
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.util.io.json.JsonObject#getJsonProperty(java.lang.String)
     */
    public Object getJsonProperty(String property) {
        if ( _values == null ) {
            return null;
        }

        int columnIndex = getColumnIndex(property);
        if ( columnIndex != -1 ) {
            Object value = _values.get(columnIndex);
            if ( value instanceof String ) {
                if ( isPersonColumn(property) ) {
                    return new JsonPersonAdapter(new Person((String)value, null, null));
                }
                else {
                    String valueAsString = (String)value;
                    return valueAsString;
                }
            }
            else if ( value instanceof DateTime ) {
                try {
                    Date date = ((DateTime)value).toJavaDate();
                    return ISO8601.format(date);
                } catch (NotesException e) {
                    Logger.get().warnp(this, "getJsonProperty",//$NON-NLS-1$
                            e, com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getLoggingString("warn.JsonViewEntryAdapter_UnhandledExceptionConvertingToDate")); // $NLW-JsonViewEntryAdapter_UnhandledExceptionConvertingToDate-1$
                }
            }
            else if ( value instanceof Date ) {
                return ISO8601.format(value);
            }
        }
        else if ( HREF_PROP.equals(property) ) {
            String unid = null;
            
            try {
                unid = _entry.getUniversalID(); 
            }
            catch (NotesException e) {
                Logger.get().warnp(this, "getJsonProperty",//$NON-NLS-1$
                        e, com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getLoggingString("warn.JsonViewEntryAdapter_UnhandledExceptionGettingUniversalID")); // $NLW-JsonViewEntryAdapter_UnhandledExceptionGettingUniversalID-1$
            }
            
            return _baseUri.toString() + "/" + unid;
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.ibm.commons.util.io.json.JsonObject#putJsonProperty(java.lang.String, java.lang.Object)
     */
    public void putJsonProperty(String property, Object value) {
        // This method is called when converting JSON to a view entry
        
    }

    private int getColumnIndex(String columnName) {
        int index = -1;

        Column column = _columns.get(columnName);
        if ( column != null ) {
            index = column.getIndex();
        }
        
        return index;
    }
    
    private static SimpleDateFormat getUtcFormatter() {
        TimeZone tz = TimeZone.getTimeZone("UTC"); // $NON-NLS-1$
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //$NON-NLS-1$
        formatter.setTimeZone(tz);
        return formatter;
    }
}
