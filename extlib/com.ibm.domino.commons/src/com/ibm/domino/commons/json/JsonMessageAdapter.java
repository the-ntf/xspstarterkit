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

import static com.ibm.domino.commons.json.JsonConstants.CC_PROP;
import static com.ibm.domino.commons.json.JsonConstants.CONTENT_PROP;
import static com.ibm.domino.commons.json.JsonConstants.DATE_PROP;
import static com.ibm.domino.commons.json.JsonConstants.DISTINGUISHED_NAME_PROP;
import static com.ibm.domino.commons.json.JsonConstants.EMAIL_PROP;
import static com.ibm.domino.commons.json.JsonConstants.FROM_PROP;
import static com.ibm.domino.commons.json.JsonConstants.HREF_PROP;
import static com.ibm.domino.commons.json.JsonConstants.SUBJECT_PROP;
import static com.ibm.domino.commons.json.JsonConstants.TO_PROP;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.Name;
import lotus.domino.NotesException;
import lotus.domino.Session;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.domino.commons.internal.Logger;
import com.ibm.domino.commons.json.JsonMimeEntityAdapter.ParserContext;
import com.ibm.domino.commons.mime.MimeEntityHelper;
import com.ibm.domino.commons.names.Person;

public class JsonMessageAdapter implements JsonObject {

    private static final String SENDTO_ITEM = "SendTo"; // $NON-NLS-1$
    private static final String COPYTO_ITEM = "CopyTo"; // $NON-NLS-1$
    private static final String FROM_ITEM = "From"; // $NON-NLS-1$
    private static final String POSTEDDATE_ITEM = "PostedDate"; // $NON-NLS-1$
    private static final String SUBJECT_ITEM = "Subject"; // $NON-NLS-1$
    private static final String BODY_ITEM = "Body"; // $NON-NLS-1$
    private static final String INET_PREFIX = "INet"; // $NON-NLS-1$
    
    private static SimpleDateFormat ISO8601 = getUtcFormatter();
    
    private Document _document = null;
    private String _url = null;
    private String[] _propertyNames = null;
    private ParserContext _context = null;
    
    /**
     * Constructor used when generating JSON output.
     * 
     * @param document
     * @param url
     */
    public JsonMessageAdapter(Document document, String url) {
        _document = document;
        _url = url;
    }
    
    /**
     * Constructor used when parsing JSON input.
     * 
     * @param document
     * @param context
     */
    public JsonMessageAdapter(Document document, ParserContext context) {
        _document = document;
        _context = context;
    }

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
                
                try {
                    String from = _document.getItemValueString(FROM_ITEM);
                    if ( StringUtil.isNotEmpty(from) ) {
                        properties.add(FROM_PROP);
                    }

                    Vector<Object> items = _document.getItemValue(SENDTO_ITEM);
                    if ( items != null ) {
                        Iterator<Object> iterator = items.iterator();
                        if ( iterator.hasNext() ) {
                            properties.add(TO_PROP);
                        }
                    }

                    items = _document.getItemValue(COPYTO_ITEM);
                    if ( items != null ) {
                        Iterator<Object> iterator = items.iterator();
                        if ( iterator.hasNext() ) {
                            properties.add(CC_PROP);
                        }
                    }

                    String subject = _document.getItemValueString(SUBJECT_ITEM);
                    if ( StringUtil.isNotEmpty(subject) ) {
                        properties.add(SUBJECT_PROP);
                    }
                    
                    properties.add(DATE_PROP);
                    properties.add(HREF_PROP);
                    properties.add(CONTENT_PROP);
                }
                catch(NotesException e) {
                    // Ignore
                    Logger.get().warnp(this, "getProperties",//$NON-NLS-1$
                            e, com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getLoggingString("warn.JsonMessageAdapter_UnhandledExceptionInGetProperties"));// $NLW-JsonMessageAdapter_UnhandledExceptionInGetProperties-1$
                }

                // Convert to array
                
                String[] array = new String[properties.size()];
                Iterator<String> iterator = properties.iterator();
                for ( int i = 0; iterator.hasNext(); i++ ) {
                    array[i] = iterator.next();
                }
                
                // Cache the array for next time
                _propertyNames = array;
                
                return _propertyNames;
            }
        };
    }

    public Object getJsonProperty(String property) {
        Object value = null;
        
        try {
            if ( FROM_PROP.equals(property) ) {
                String from = _document.getItemValueString(FROM_ITEM);
                if ( StringUtil.isNotEmpty(from) ) {
                    String email = _document.getItemValueString(INET_PREFIX + FROM_ITEM);
                    if ( StringUtil.isEmpty(email) ) {
                        email = null;
                    }
                    
                    Session session = _document.getParentDatabase().getParent();
                    Name name = session.createName(from);
                    Person person = new Person(name.getCommon(), name.getAbbreviated(), email);
                    value = new JsonPersonAdapter(person);
                }
            }
            else if ( SUBJECT_PROP.equals(property) ) {
                String subject = _document.getItemValueString(SUBJECT_ITEM);
                if ( subject != null ) {
                    value = subject;
                }
            }
            else if ( DATE_PROP.equals(property) ) {
                DateTime dt = null;
                
                if ( _document.hasItem(POSTEDDATE_ITEM)) {
                    Vector values = _document.getItemValueDateTimeArray(POSTEDDATE_ITEM);
                    if ( values != null && values.size() > 0 ) {
                        if ( values.get(0) instanceof DateTime ) {
                            dt = (DateTime)values.get(0);
                        }
                    }
                }
                
                if ( dt == null ) {
                    dt = _document.getLastModified();
                }
                
                Date date = dt.toJavaDate();
                value = ISO8601.format(date);
            }
            else if ( HREF_PROP.equals(property) ) {
                value = _url;
            }
            else if ( TO_PROP.equals(property) ) {
                value = getAddressList(SENDTO_ITEM);
            }
            else if ( CC_PROP.equals(property) ) {
                value = getAddressList(COPYTO_ITEM);
            }
            else if ( CONTENT_PROP.equals(property) ) {
                List<JsonMimeEntityAdapter> adapters = new ArrayList<JsonMimeEntityAdapter>();
                MimeEntityHelper helper = new MimeEntityHelper(_document, BODY_ITEM);
                MIMEEntity entity = helper.getFirstMimeEntity();
                if ( entity != null ) {
                    JsonMimeEntityAdapter.addEntityAdapter(adapters, entity);
                }
                value = adapters;
            }
        }
        catch (NotesException e) {
            Logger.get().warnp(this, "getJsonProperty",//$NON-NLS-1$
                    e, com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getLoggingString("warn.JsonMessageAdapter_UnhandledExceptionInGetJsonProperty"));// $NLW-JsonMessageAdapter_UnhandledExceptionInGetJsonProperty-1$
            // default to null
        }

        return value;
    }

    public void putJsonProperty(String property, Object value) {
        // This method is called when converting JSON to a message
        
        try {
            if ( TO_PROP.equals(property) ) {
                putAddressList(SENDTO_ITEM, value);
            }
            else if ( CC_PROP.equals(property) ) {
                putAddressList(COPYTO_ITEM, value);
            }
            else if ( SUBJECT_PROP.equals(property) ) {
                _document.replaceItemValue(SUBJECT_ITEM, value);
            }
            else if ( CONTENT_PROP.equals(property) ) {
                
                // Flush the last MIME entity to the document
                
                JsonMimeEntityAdapter entityAdapter = _context.getCurrentEntityAdapter();
                if ( entityAdapter != null ) {
                    entityAdapter.flushJsonProperties(true);
                }
            }
        }
        catch (NotesException e) {
            // Ignore !?!
        }
        catch (JsonException e) {
            // Ignore !?!
        }
    }

    private static SimpleDateFormat getUtcFormatter() {
        TimeZone tz = TimeZone.getTimeZone("UTC"); // $NON-NLS-1$
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); //$NON-NLS-1$
        formatter.setTimeZone(tz);
        return formatter;
    }
    
    /**
     * Creates a list of JsonPersonAdapters from a list of Notes addresses
     * 
     * <p>This method may get data from items other than <code>itemName</code>.  For example, 
     * if the item name is SendTo, this method reads the INetSendTo item for corresponding 
     * internet addresses.
     * 
     * @param itemName
     * @return
     * @throws NotesException
     */
    private List<JsonPersonAdapter> getAddressList(String itemName) throws NotesException {
        List<JsonPersonAdapter> list = new ArrayList<JsonPersonAdapter>();
        Vector<Object> items = _document.getItemValue(itemName);
        Vector<Object> inetItems = _document.getItemValue(INET_PREFIX + itemName);
        Session session = _document.getParentDatabase().getParent();
        
        Iterator<Object> inetIterator = null;
        if ( inetItems != null ) {
            inetIterator = inetItems.iterator();
        }
        
        if ( items != null ) {
            Iterator<Object> iterator = items.iterator();
            while ( iterator.hasNext() ) {
                Object value = iterator.next();
                
                Object inetValue = null;
                if ( inetIterator != null && inetIterator.hasNext() ) {
                    inetValue = inetIterator.next();
                }
                
                if ( value instanceof String ) {
                    String displayName = null;
                    String distinguishedName = null;
                    String emailAddress = null;
                    Name name = session.createName((String)value);

                    if ( name.isHierarchical() ) {
                        displayName = name.getCommon();
                        distinguishedName = name.getAbbreviated();
                        if ( inetValue instanceof String ) {
                            emailAddress = (String)inetValue;
                        }
                        else {
                            emailAddress = name.getAddr821();
                        }
                    }
                    else {
                        displayName = trimCommonName(name.getCommon());
                        emailAddress = name.getAddr821();
                    }
                    
                    Person person = new Person(displayName, distinguishedName, emailAddress);
                    list.add(new JsonPersonAdapter(person));
                }
            }
        }
        
        return list;
    }
    
    private void putAddressList(String itemName, Object list) throws NotesException {
        Vector<Object> values = new Vector<Object>();
        
        if ( list instanceof List ) {
            Iterator<Object> iterator = ((List)list).iterator();
            while (iterator.hasNext()) {
                Object value = iterator.next();
                if ( value instanceof JsonPersonAdapter ) {
                    JsonPersonAdapter adapter = (JsonPersonAdapter)value;
                    String email = (String)adapter.getJsonProperty(EMAIL_PROP);
                    String distinguishedName = (String)adapter.getJsonProperty(DISTINGUISHED_NAME_PROP);
                    if ( StringUtil.isNotEmpty(distinguishedName) ) {
                        values.add(distinguishedName);
                    }
                    else if ( StringUtil.isNotEmpty(email) ) {
                        values.add(email);
                    }
                }
            }
            _document.replaceItemValue(itemName, values);
        }
    }
    
    /**
     * Removes leading and trailing quotes and/or whitespace.
     * 
     * @param name
     * @return
     */
    private String trimCommonName(String name) {
        name = name.replaceAll("(^\")|(\"$)", ""); // $NON-NLS-1$
        name = name.trim();
        return name;
    }
    
    /**
     * Removes "@{domain}" from a distinguished name.
     * 
     * @param name
     * @return
     */
//    private String trimDistinguishedName(String name) {
//        String token[] = name.split("@");
//        if ( token != null && token[0] != null ) {
//            name = token[0];
//        }
//        
//        return name;
//    }
    
}
