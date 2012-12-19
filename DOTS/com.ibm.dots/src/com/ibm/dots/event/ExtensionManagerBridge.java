/*
 * © Copyright IBM Corp. 2009,2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.dots.event;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * @author dtaieb
 *
 */
public class ExtensionManagerBridge {

	private static final String EM_EVENT_PREFIX = "[[event:"; // $NON-NLS-1$
	private static final String EM_NSFHOOKEVENT_PREFIX = "[[dbhookevent:"; // $NON-NLS-1$
	private static final String EM_EVENT_POSTFIX = "]];";

	private static final HashMap<Integer, Class<?> > eventsMap = new HashMap<Integer, Class<?>>();

	static{
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCLOSE,  NSFDbCloseEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEUPDATE , NSFNoteUpdateExtendedEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEUPDATEXTENDED , NSFNoteUpdateExtendedEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCREATE , NSFDbCreateEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBDELETE , NSFDbDeleteEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTECREATE , NSFNoteCreateEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEDELETE , NSFNoteDeleteEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_ADMINPPROCESSREQUEST , AdminPProcessRequestEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEOPEN , NSFNoteOpenEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTECLOSE , NSFNoteCloseEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEOPENBYUNID , NSFNoteOpenByUNIDEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_FTINDEX , FTIndexEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_FTSEARCH , FTSearchEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYKEY , NIFFindByKeyEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYNAME , NIFFindByNameEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFOPENNOTE , NIFOpenNoteEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFREADENTRIES , NIFReadEntriesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFUPDATECOLLECTION , NIFUpdateCollectionEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCOMPACT , NSFDbCompactEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBDELETENOTES , NSFDbDeleteNotesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFDBWRITEOBJECT , NSFDbWriteObjectEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NIFOPENCOLLECTION , NIFOpenCollectionEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NIFCLOSECOLLECTION , NIFCloseCollectionEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBRENAME , NSFDbRenameEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBREOPEN , NSFDbReopenEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBOPENEXTENDED, NSFDbOpenExtendedEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEOPENEXTENDED, NSFNoteOpenExtendedEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_TERMINATENSF, TerminateNSFEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEDECRYPT, NSFNoteDecryptEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFCONFLICTHANDLER, NSFConflictHandlerEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_MAILSENDNOTE, MailSendNoteEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_CLEARPASSWORD, ClearPasswordEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHFREETIMESEARCH, SCHFreeTimeSearchEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHRETRIEVE, SCHRetrieveEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_SCHSRVRETRIEVE, SCHSrvRetrieveEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCOMPACTEXTENDED, NSFDbCompactEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCOPYNOTE, NSFDbCopyNoteEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTECOPY, NSFNoteCopyEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEATTACHFILE, NSFNoteAttachFileEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEDETACHFILE, NSFNoteDetachFileEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEEXTRACTFILE, NSFNoteExtractFileEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEATTACHOLE2OBJECT, NSFNoteAttachOLE2ObjectEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEDELETEOLE2OBJECT, NSFNoteDeleteOLE2ObjectEvent.class);
		//      eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEEXTRACTOLE2OBJECT, NSFNoteExtractOLE2ObjectEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCOPY, NSFDbCopyEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCREATEANDCOPY, NSFDbCreateAndCopyEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCOPYACL, NSFDbCopyACLEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCOPYTEMPLATEACL, NSFDbCopyTemplateACLEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBCREATEACLFROMTEMPLATE, NSFDbCreateACLFromTemplateEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_FTDELETEINDEX, FTDeleteIndexEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_FTSEARCHEXT, FTSearchExtEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP, NameLookupEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEUPDATEMAILBOX, NSFNoteUpdateMailBoxEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_AGENTOPEN, AgentOpenEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_AGENTRUN, AgentRunEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_AGENTCLOSE, AgentCloseEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_SECAUTHENTICATION, SECAuthenticationEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP2, NameLookup2Event.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFADDTOFOLDER, NSFAddToFolderEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_ROUTERJOURNALMESSAGE, RouterJournalMessageEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_SMTPCONNECT, SMTPConnectEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_SMTPCOMMAND, SMTPCommandEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_SMTPMESSAGEACCEPT, SMTPMessageAcceptEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_SMTPDISCONNECT, SMTPDisconnectEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVECOPYNOTES, NSFArchiveCopyNotesEvent.class);
		//eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVEDELETENOTES, NSFArchiveDeleteNotesEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_MEDIARECOVERY_NOTE, MediaRecoveryEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTECIPHERDECRYPT, NSFNoteCipherDecryptEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFNOTECIPHEREXTRACTFILE, NSFNoteCipherExtractFileEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBNOTELOCK, NSFDbNoteLockEvent.class);
		eventsMap.put( IExtensionManagerEvent.EM_NSFDBNOTEUNLOCK, NSFDbNoteUnlockEvent.class);

		eventsMap.put( IExtensionManagerEvent.HOOK_EVENT_NOTE_UPDATE,NSFHookNoteUpdateEvent.class ); 
		eventsMap.put( IExtensionManagerEvent.HOOK_EVENT_NOTE_OPEN, NSFHookNoteOpenEvent.class );
	}

	/**
	 * 
	 */
	private ExtensionManagerBridge() {
	}

	/**
	 * @param commandBuffer
	 * @return
	 */
	public static IExtensionManagerEvent getEventFromCommand(String commandBuffer) {
		boolean bIsEvent = commandBuffer.startsWith( EM_EVENT_PREFIX );
		boolean bIsNSFHookEvent = commandBuffer.startsWith( EM_NSFHOOKEVENT_PREFIX);
		if ( !bIsEvent && !bIsNSFHookEvent ){
			return null;
		}

		String prefix = bIsEvent ? EM_EVENT_PREFIX : EM_NSFHOOKEVENT_PREFIX;
		commandBuffer = commandBuffer.substring( prefix.length() );
		int iIndex = commandBuffer.indexOf( EM_EVENT_POSTFIX );
		if ( iIndex < 0 ){
			return null;
		}

		try {
			int eventId = Integer.parseInt( commandBuffer.substring( 0, iIndex ).trim());
			return getEvent( eventId, commandBuffer.substring( iIndex + EM_EVENT_POSTFIX.length() ) );
		} catch (NumberFormatException e) {
			return  null;
		}
	}

	/**
	 * @param eventId
	 * @param eventBuffer
	 * @return
	 */
	private static IExtensionManagerEvent getEvent(int eventId, String eventBuffer) {
		AbstractEMEvent retEvent = null;
		Class<?> eventClass = eventsMap.get( eventId );
		if ( eventClass != null ){
			try{
				Constructor<?> constructor = null;
				try {
					constructor = eventClass.getConstructor();
					retEvent = (AbstractEMEvent)constructor.newInstance();
				} catch (Throwable t) {
					//Try with eventId
					constructor = eventClass.getConstructor( Integer.TYPE );
					retEvent = (AbstractEMEvent)constructor.newInstance( eventId );
				}               
			}catch( Throwable t ){
				t.printStackTrace();
			}
		}

		if ( retEvent != null && retEvent.parseEventBuffer( eventBuffer )){
			return retEvent;
		}
		return new UnknownEMEvent();
	}

	/**
	 * @param emEvent
	 * @return
	 */
	public static Class<?> getEMEventClass(IExtensionManagerEvent emEvent) {
		return getEMEventClass( emEvent.getEventId()  );
	}

	/**
	 * @param eventId
	 * @return
	 */
	public static Class<?> getEMEventClass(int eventId) {
		return eventsMap.get( eventId  );
	}

}
