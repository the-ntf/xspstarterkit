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

import java.io.IOException;
import java.io.Writer;

import lotus.domino.Document;
import lotus.domino.MIMEEntity;
import lotus.domino.NotesException;

public class MimeMessageGenerator {
    
    private static final String MULTIPART = "multipart"; //$NON-NLS-1$

    private Writer _writer;
    
    public MimeMessageGenerator(Writer writer) {
        _writer = writer;
    }
    
    public void toMime(Document document) throws NotesException, IOException {
        writeMimeOutput(document, "body", _writer); //$NON-NLS-1$
    }

    public void toMime(Document document, String itemName) throws NotesException, IOException {
        writeMimeOutput(document, itemName, _writer);
    }
    
    private void writeMimeOutput(Document document, String itemName, Writer writer)
            throws NotesException, IOException {

        MIMEEntity mimeEntity = null;
        MIMEEntity mChild = null;
        String contenttype = null;
        String headers = null;

        MimeEntityHelper helper = new MimeEntityHelper(document, itemName);
        mimeEntity = helper.getFirstMimeEntity();
        if (mimeEntity == null) {
            throw new NullPointerException(com.ibm.domino.commons.internal.DominoCommonsResourceHandler.getSpecialAudienceString("MimeMessageGenerator_InvalidItem")); // $NLX-MimeMessageGenerator_InvalidItem-1$
        }
        
        try {
            contenttype = mimeEntity.getContentType();
            headers = mimeEntity.getHeaders();
            // TODO: Is there a better way to output the headers?
            if (!headers.startsWith("MIME-Version:")) { //$NON-NLS-1$
                writer.write("MIME-Version: 1.0"); //$NON-NLS-1$
            }
            // Write MIMEEntity.
            writeMimeEntity(writer, mimeEntity);
            if (contenttype.startsWith(MULTIPART)) {
                mChild = mimeEntity.getFirstChildEntity();
                while (mChild != null) {
                    // Write MIMEEntity.
                    writeMimeEntity(writer, mChild);
                    MIMEEntity mChild2 = mChild.getFirstChildEntity();
                    if (mChild2 == null) {
                        mChild2 = mChild.getNextSibling();
                        if (mChild2 == null) {
                            mChild2 = mChild.getParentEntity();
                            if (mChild2 != null)
                                mChild2 = mChild2.getNextSibling();
                        }
                    }
                    mChild.recycle();
                    mChild = mChild2;
                }
            }
            writer.write(mimeEntity.getBoundaryEnd());
        } finally {
            helper.recycle();
        }
    }

    private void writeMimeEntity(Writer writer, MIMEEntity mimeEntity)
            throws NotesException, IOException {

        String headers;
        String content;
        int encoding;

        headers = mimeEntity.getHeaders();
        encoding = mimeEntity.getEncoding();
        if (encoding == MIMEEntity.ENC_IDENTITY_BINARY) {
            mimeEntity.encodeContent(MIMEEntity.ENC_BASE64);
            headers = mimeEntity.getHeaders();
        }
        content = mimeEntity.getBoundaryStart();
        writer.write(content);
        if (!content.endsWith("\n")) //$NON-NLS-1$
            writer.write("\n"); //$NON-NLS-1$
            // TODO: Is there a better way to output the headers?
        writer.write(headers);
        writer.write("\n\n"); //$NON-NLS-1$
        content = mimeEntity.getContentAsText();
        if (content != null && content.length() > 0)
            writer.write(content);
        writer.write(mimeEntity.getBoundaryEnd());
    }
}
