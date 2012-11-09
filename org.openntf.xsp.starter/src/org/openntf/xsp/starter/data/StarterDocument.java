/**
 * 
 */
package org.openntf.xsp.starter.data;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.xml.XMLException;
import com.ibm.jscript.InterpretException;
import com.ibm.jscript.std.ArrayObject;
import com.ibm.xsp.model.FileRowData;
import com.ibm.xsp.model.domino.DominoDocumentData;
import com.ibm.xsp.model.domino.DominoDocumentDataContainer;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;
import com.ibm.xsp.model.domino.wrapped.DominoDocument.FieldValueHolder;
import com.ibm.xsp.model.domino.wrapped.DominoRichTextItem;

/**
 * @author nfreeman
 * 
 */
public class StarterDocument {
	private DominoDocument documentDelegate_;
	private DominoDocumentData documentDelegateData_;

	public String getPersistenceFolder(String paramString) {
		return documentDelegate_.getPersistenceFolder(paramString);
	}

	public String getDBName() {
		return documentDelegate_.getDBName();
	}

	public String getComputeWithFormNamed() {
		return documentDelegate_.getComputeWithFormNamed();
	}

	public String getSaveLinksAs() {
		return documentDelegate_.getSaveLinksAs();
	}

	public void setAllowDeletedDocs(boolean paramBoolean) {
		documentDelegate_.setAllowDeletedDocs(paramBoolean);
	}

	public Document getDocument() {
		return documentDelegate_.getDocument();
	}

	public final Document getValidDocument() throws NotesException {
		return documentDelegate_.getValidDocument();
	}

	public void checkDocumentValidity() {
		documentDelegate_.checkDocumentValidity();
	}

	public void setDocument(Document paramDocument) {
		documentDelegate_.setDocument(paramDocument);
	}

	public Document getDocument(boolean paramBoolean) throws NotesException {
		return documentDelegate_.getDocument(paramBoolean);
	}

	public void restoreWrappedDocument() {
		documentDelegate_.restoreWrappedDocument();
	}

	public void recycle() throws NotesException {
		documentDelegate_.recycle();
	}

	public void discardWrappedObject() {
		documentDelegate_.discardWrappedObject();
	}

	public void closeMIMEEntities(boolean paramBoolean, String paramString) throws NotesException {
		documentDelegate_.closeMIMEEntities(paramBoolean, paramString);
	}

	public String getForm() throws NotesException {
		return documentDelegate_.getForm();
	}

	public void setEditable(boolean paramBoolean) {
		documentDelegate_.setEditable(paramBoolean);
	}

	public String getParentId() {
		return documentDelegate_.getParentId();
	}

	public Vector getItemValue(String paramString) throws NotesException {
		return documentDelegate_.getItemValue(paramString);
	}

	public ArrayObject getItemValueArray(String paramString) throws NotesException, InterpretException {
		return documentDelegate_.getItemValueArray(paramString);
	}

	public String getItemValueString(String paramString) throws NotesException {
		return documentDelegate_.getItemValueString(paramString);
	}

	public int getItemValueInteger(String paramString) throws NotesException {
		return documentDelegate_.getItemValueInteger(paramString);
	}

	public double getItemValueDouble(String paramString) throws NotesException {
		return documentDelegate_.getItemValueDouble(paramString);
	}

	public DateTime getItemValueDateTime(String paramString) throws NotesException {
		return documentDelegate_.getItemValueDateTime(paramString);
	}

	public Date getItemValueDate(String paramString) throws NotesException {
		return documentDelegate_.getItemValueDate(paramString);
	}

	public String getNoteID() throws NotesException {
		return documentDelegate_.getNoteID();
	}

	public String getDocumentId() {
		return documentDelegate_.getDocumentId();
	}

	public Database getParentDatabase() throws NotesException {
		return documentDelegate_.getParentDatabase();
	}

	public String getParentDatabaseFileName() throws NotesException {
		return documentDelegate_.getParentDatabaseFileName();
	}

	public boolean hasItem(String paramString) throws NotesException {
		return documentDelegate_.hasItem(paramString);
	}

	public Object getValue(Object paramObject) {
		return documentDelegate_.getValue(paramObject);
	}

	public Class getType(Object paramObject) {
		return documentDelegate_.getType(paramObject);
	}

	public Map<String, FieldValueHolder> getChangedFields() {
		return documentDelegate_.getChangedFields();
	}

	public List<FileRowData> getAttachmentList(String paramString) throws NotesException {
		return documentDelegate_.getAttachmentList(paramString);
	}

	public List<FileRowData> getEmbeddedImagesList(String paramString) throws NotesException {
		return documentDelegate_.getEmbeddedImagesList(paramString);
	}

	public DominoRichTextItem getRichTextItem(String paramString) {
		return documentDelegate_.getRichTextItem(paramString);
	}

	public void beforeSerializing() {
		documentDelegate_.beforeSerializing();
	}

	public void afterDeserializing(DominoDocumentDataContainer paramDominoDocumentDataContainer) {
		documentDelegate_.afterDeserializing(paramDominoDocumentDataContainer);
	}

	@Override
	public boolean equals(Object o) {
		return documentDelegate_.equals(o);
	}

	public Object getFieldAsJson(String paramString, boolean paramBoolean) throws NotesException, JsonException, IOException {
		return documentDelegate_.getFieldAsJson(paramString, paramBoolean);
	}

	public Object getFieldAsJson(String paramString, boolean paramBoolean, JsonFactory paramJsonFactory) throws NotesException,
			JsonException, IOException {
		return documentDelegate_.getFieldAsJson(paramString, paramBoolean, paramJsonFactory);
	}

	public Object getFieldAsJson(String paramString) throws NotesException, JsonException, IOException {
		return documentDelegate_.getFieldAsJson(paramString);
	}

	public Object getFieldAsJson(String paramString, JsonFactory paramJsonFactory) throws NotesException, JsonException, IOException {
		return documentDelegate_.getFieldAsJson(paramString, paramJsonFactory);
	}

	public Object getAttachmentAsJson(String paramString1, String paramString2, boolean paramBoolean) throws NotesException, JsonException,
			IOException {
		return documentDelegate_.getAttachmentAsJson(paramString1, paramString2, paramBoolean);
	}

	public Object getAttachmentAsJson(String paramString1, String paramString2) throws NotesException, JsonException, IOException {
		return documentDelegate_.getAttachmentAsJson(paramString1, paramString2);
	}

	public Object getAttachmentAsJson(String paramString1, String paramString2, boolean paramBoolean, JsonFactory paramJsonFactory)
			throws NotesException, JsonException, IOException {
		return documentDelegate_.getAttachmentAsJson(paramString1, paramString2, paramBoolean, paramJsonFactory);
	}

	public org.w3c.dom.Document getFieldAsXml(String paramString, boolean paramBoolean) throws NotesException, XMLException, IOException {
		return documentDelegate_.getFieldAsXml(paramString, paramBoolean);
	}

	public org.w3c.dom.Document getFieldAsXml(String paramString) throws NotesException, XMLException, IOException {
		return documentDelegate_.getFieldAsXml(paramString);
	}

	public org.w3c.dom.Document getAttachmentAsXML(String paramString1, String paramString2, boolean paramBoolean) throws NotesException,
			XMLException, IOException {
		return documentDelegate_.getAttachmentAsXML(paramString1, paramString2, paramBoolean);
	}

	@Override
	public int hashCode() {
		return documentDelegate_.hashCode();
	}

	public boolean isAllowDeletedDocs() {
		return documentDelegate_.isAllowDeletedDocs();
	}

	public boolean isEditable() {
		return documentDelegate_.isEditable();
	}

	public boolean isNewNote() throws NotesException {
		return documentDelegate_.isNewNote();
	}

	public boolean isResponse() throws NotesException {
		return documentDelegate_.isResponse();
	}

	public void removeItem(String paramString) throws NotesException {
		documentDelegate_.removeItem(paramString);
	}

	public void replaceItemValue(String paramString, Object paramObject) throws NotesException {
		documentDelegate_.replaceItemValue(paramString, paramObject);
	}

	public boolean save() throws NotesException {
		return documentDelegate_.save();
	}

	public void setValue(Object paramObject1, Object paramObject2) {
		documentDelegate_.setValue(paramObject1, paramObject2);
	}

	public boolean isReadOnly(Object paramObject) {
		return documentDelegate_.isReadOnly(paramObject);
	}

	public boolean removeAttachment(String paramString1, String paramString2) throws NotesException {
		return documentDelegate_.removeAttachment(paramString1, paramString2);
	}

	public void removeAllAttachments(String paramString) throws NotesException {
		documentDelegate_.removeAllAttachments(paramString);
	}

	public void setRichTextItem(String paramString, DominoRichTextItem paramDominoRichTextItem) {
		documentDelegate_.setRichTextItem(paramString, paramDominoRichTextItem);
	}

	@Override
	public String toString() {
		return documentDelegate_.toString();
	}

}
