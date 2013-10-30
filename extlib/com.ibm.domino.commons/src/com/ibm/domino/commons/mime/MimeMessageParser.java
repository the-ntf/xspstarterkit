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






package com.ibm.domino.commons.mime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.MIMEHeader;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.Stream;

import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.field.address.Address;
import org.apache.james.mime4j.field.address.AddressList;
import org.apache.james.mime4j.message.BinaryBody;
import org.apache.james.mime4j.message.Body;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.Entity;
import org.apache.james.mime4j.message.Header;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.message.Multipart;
import org.apache.james.mime4j.message.TextBody;
import org.apache.james.mime4j.parser.Field;

public class MimeMessageParser {
    
    private InputStream _is;
    
    public MimeMessageParser(InputStream is) {
        _is = is;
    }
    
    public void fromMime(Document document) throws MimeIOException, IOException, NotesException {
        Message mimeMsg = new Message(_is);
        
        document.replaceItemValue("Form", "Memo"); //$NON-NLS-1$ //$NON-NLS-2$
        
        String subject = mimeMsg.getSubject();
        document.replaceItemValue("Subject", subject); //$NON-NLS-1$
        
        AddressList to = mimeMsg.getTo();
        writeAddresses(document, "SendTo", to); //$NON-NLS-1$

        AddressList cc = mimeMsg.getCc();
        writeAddresses(document, "CopyTo", cc); //$NON-NLS-1$
        
        writeEntity(document, null, mimeMsg);  
    }
    
    private static void writeEntity(Document document, MIMEEntity parent, Entity entity) throws IOException, NotesException {  
        MIMEEntity notesEntity = null;
        if ( parent == null ) {
            notesEntity = document.createMIMEEntity();
        }
        else {
            notesEntity = parent.createChildEntity();
        }
        
        String mediaType = entity.getMimeType();
        
        Body body = entity.getBody();
        if ( body instanceof Multipart ) {

            // Set the content type
            
            MIMEHeader notesHeader = notesEntity.createHeader("Content-Type"); //$NON-NLS-1$
            notesHeader.setHeaderVal(mediaType);
            
            Multipart multipart = (Multipart)body;
            for (BodyPart part : multipart.getBodyParts()) {  
                writeEntity(document, notesEntity, part);  
            }
        }
        else {
            Header header = entity.getHeader();
            
            // Handle content disposition
            String filename = entity.getFilename();
            String disposition = entity.getDispositionType();
            if ( disposition != null ) {
                MIMEHeader notesHeader = notesEntity.createHeader("Content-Disposition"); //$NON-NLS-1$
                String value = disposition;
                if ( filename != null ) {
                    value += "; filename=\"" + filename + "\""; //$NON-NLS-1$ //$NON-NLS-2$
                }
                notesHeader.setHeaderVal(value);
            }
            
            // Handle content ID
            Field id = header.getField("Content-ID"); //$NON-NLS-1$
            if ( id != null ) {
                MIMEHeader notesHeader = notesEntity.createHeader("Content-ID"); //$NON-NLS-1$
                notesHeader.setHeaderVal(id.getBody());
            }

            // Write a simple part's content
            
            Session session = document.getParentDatabase().getParent();
            
            if ( body instanceof TextBody) {
                Reader reader = ((TextBody)body).getReader();
                Stream stream = session.createStream();
                readerToNotesStream(stream, reader);
                
                // TODO: Handle character set
                
                notesEntity.setContentFromText(stream, mediaType, MIMEEntity.ENC_NONE);
            }
            else if ( body instanceof BinaryBody ) {
                InputStream is = ((BinaryBody)body).getInputStream();
                Stream stream = session.createStream();
                inputStreamToNotesStream(stream, is);

                notesEntity.setContentFromBytes(stream, mediaType, MIMEEntity.ENC_NONE);
            }
            else {
                // TODO: Is this an error condition?
            }
        }
    }
    
    private static void writeAddresses(Document document, String itemName, AddressList addresses) throws NotesException {
        
        if ( addresses != null && addresses.size() > 0 ) {
            String value = ""; //$NON-NLS-1$
            for ( int i = 0; i < addresses.size(); i++ ) {
                if ( i > 0 ) {
                    value += ", "; //$NON-NLS-1$
                }
                Address address = addresses.get(i);
                value += address.toString();
            }
            
            document.replaceItemValue(itemName, value);
        }
    }

    private static void readerToNotesStream(Stream stream, Reader reader) throws IOException, NotesException {
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        while ( line != null ) {
            stream.writeText(line);
            stream.writeText("\r\n"); //$NON-NLS-1$
            line = br.readLine();
        }
    }
    
    private static void inputStreamToNotesStream(Stream stream, InputStream is) throws IOException, NotesException {
        
        int ch = is.read();
        while ( ch != -1 ) {
            byte b[] = new byte[1];
            b[0] = (byte)ch;
            stream.write(b);
            ch = is.read();
        }
    }
}
