/**
 * 
 */
package com.ibm.dots.tasklet.events;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ibm.dots.event.AdminPProcessRequestEvent;
import com.ibm.dots.event.AgentOpenEvent;
import com.ibm.dots.event.ClearPasswordEvent;
import com.ibm.dots.event.FTDeleteIndexEvent;
import com.ibm.dots.event.FTIndexEvent;
import com.ibm.dots.event.FTSearchEvent;
import com.ibm.dots.event.IExtensionManagerEvent;
import com.ibm.dots.event.MailSendNoteEvent;
import com.ibm.dots.event.MediaRecoveryEvent;
import com.ibm.dots.event.NIFOpenCollectionEvent;
import com.ibm.dots.event.NSFAddToFolderEvent;
import com.ibm.dots.event.NSFConflictHandlerEvent;
import com.ibm.dots.event.NSFDbCloseEvent;
import com.ibm.dots.event.NSFDbCompactEvent;
import com.ibm.dots.event.NSFDbCopyACLEvent;
import com.ibm.dots.event.NSFDbCopyEvent;
import com.ibm.dots.event.NSFDbCopyNoteEvent;
import com.ibm.dots.event.NSFDbCopyTemplateACLEvent;
import com.ibm.dots.event.NSFDbCreateACLFromTemplateEvent;
import com.ibm.dots.event.NSFDbCreateAndCopyEvent;
import com.ibm.dots.event.NSFDbCreateEvent;
import com.ibm.dots.event.NSFDbDeleteEvent;
import com.ibm.dots.event.NSFDbDeleteNotesEvent;
import com.ibm.dots.event.NSFDbNoteLockEvent;
import com.ibm.dots.event.NSFDbNoteUnlockEvent;
import com.ibm.dots.event.NSFDbOpenExtendedEvent;
import com.ibm.dots.event.NSFDbRenameEvent;
import com.ibm.dots.event.NSFDbReopenEvent;
import com.ibm.dots.event.NSFHookNoteOpenEvent;
import com.ibm.dots.event.NSFHookNoteUpdateEvent;
import com.ibm.dots.event.NSFNoteAttachFileEvent;
import com.ibm.dots.event.NSFNoteCipherDecryptEvent;
import com.ibm.dots.event.NSFNoteCipherExtractFileEvent;
import com.ibm.dots.event.NSFNoteCloseEvent;
import com.ibm.dots.event.NSFNoteCopyEvent;
import com.ibm.dots.event.NSFNoteCreateEvent;
import com.ibm.dots.event.NSFNoteDecryptEvent;
import com.ibm.dots.event.NSFNoteDeleteEvent;
import com.ibm.dots.event.NSFNoteDetachFileEvent;
import com.ibm.dots.event.NSFNoteExtractFileEvent;
import com.ibm.dots.event.NSFNoteOpenByUNIDEvent;
import com.ibm.dots.event.NSFNoteOpenEvent;
import com.ibm.dots.event.NSFNoteOpenExtendedEvent;
import com.ibm.dots.event.NSFNoteUpdateExtendedEvent;
import com.ibm.dots.event.NSFNoteUpdateMailBoxEvent;
import com.ibm.dots.event.RouterJournalMessageEvent;
import com.ibm.dots.event.SECAuthenticationEvent;
import com.ibm.dots.event.SMTPCommandEvent;
import com.ibm.dots.event.SMTPConnectEvent;
import com.ibm.dots.event.SMTPDisconnectEvent;
import com.ibm.dots.event.SMTPMessageAcceptEvent;
import com.ibm.dots.event.TerminateNSFEvent;
import com.ibm.dots.tasklet.AbstractTasklet;
import com.ibm.dots.tasklet.TaskletManager;

/**
 * @author nfreeman
 * 
 */
public enum DotsEventFactory {
	INSTANCE;

	private static final String EM_EVENT_PREFIX = "[[event:"; // $NON-NLS-1$
	private static final int EM_EVENT_PREFIX_LEN = EM_EVENT_PREFIX.length();
	private static final String EM_NSFHOOKEVENT_PREFIX = "[[dbhookevent:"; // $NON-NLS-1$
	private static final String EM_EVENT_POSTFIX = "]];";

	private static final HashMap<Integer, IExtensionManagerEvent> eventsMap = new HashMap<Integer, IExtensionManagerEvent>();

	static {
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCLOSE, new NSFDbCloseEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEUPDATE, new NSFNoteUpdateExtendedEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEUPDATEXTENDED, new NSFNoteUpdateExtendedEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCREATE, new NSFDbCreateEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBDELETE, new NSFDbDeleteEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTECREATE, new NSFNoteCreateEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEDELETE, new NSFNoteDeleteEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEOPEN, new NSFNoteOpenEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTECLOSE, new NSFNoteCloseEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEOPENBYUNID, new NSFNoteOpenByUNIDEvent());
		eventsMap.put(IExtensionManagerEvent.EM_FTINDEX, new FTIndexEvent());
		eventsMap.put(IExtensionManagerEvent.EM_FTSEARCH, new FTSearchEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCOMPACT, new NSFDbCompactEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBDELETENOTES, new NSFDbDeleteNotesEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NIFOPENCOLLECTION, new NIFOpenCollectionEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCOMPACTEXTENDED, new NSFDbCompactEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEOPENEXTENDED, new NSFNoteOpenExtendedEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEDECRYPT, new NSFNoteDecryptEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBNOTELOCK, new NSFDbNoteLockEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBNOTEUNLOCK, new NSFDbNoteUnlockEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCOPYNOTE, new NSFDbCopyNoteEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTECOPY, new NSFNoteCopyEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFCONFLICTHANDLER, new NSFConflictHandlerEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEATTACHFILE, new NSFNoteAttachFileEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEDETACHFILE, new NSFNoteDetachFileEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEEXTRACTFILE, new NSFNoteExtractFileEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTECIPHERDECRYPT, new NSFNoteCipherDecryptEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTECIPHEREXTRACTFILE, new NSFNoteCipherExtractFileEvent());
		eventsMap.put(IExtensionManagerEvent.EM_ADMINPPROCESSREQUEST, new AdminPProcessRequestEvent());
		eventsMap.put(IExtensionManagerEvent.EM_CLEARPASSWORD, new ClearPasswordEvent());
		eventsMap.put(IExtensionManagerEvent.EM_MEDIARECOVERY_NOTE, new MediaRecoveryEvent());
		eventsMap.put(IExtensionManagerEvent.EM_FTDELETEINDEX, new FTDeleteIndexEvent());
		eventsMap.put(IExtensionManagerEvent.EM_AGENTOPEN, new AgentOpenEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFADDTOFOLDER, new NSFAddToFolderEvent());
		eventsMap.put(IExtensionManagerEvent.EM_MAILSENDNOTE, new MailSendNoteEvent());

		eventsMap.put(IExtensionManagerEvent.EM_NSFDBRENAME, new NSFDbRenameEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBREOPEN, new NSFDbReopenEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBOPENEXTENDED, new NSFDbOpenExtendedEvent());
		eventsMap.put(IExtensionManagerEvent.EM_TERMINATENSF, new TerminateNSFEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCOPY, new NSFDbCopyEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCREATEANDCOPY, new NSFDbCreateAndCopyEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCOPYACL, new NSFDbCopyACLEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCOPYTEMPLATEACL, new NSFDbCopyTemplateACLEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFDBCREATEACLFROMTEMPLATE, new NSFDbCreateACLFromTemplateEvent());
		eventsMap.put(IExtensionManagerEvent.EM_NSFNOTEUPDATEMAILBOX, new NSFNoteUpdateMailBoxEvent());
		eventsMap.put(IExtensionManagerEvent.EM_SECAUTHENTICATION, new SECAuthenticationEvent());
		eventsMap.put(IExtensionManagerEvent.EM_ROUTERJOURNALMESSAGE, new RouterJournalMessageEvent());
		eventsMap.put(IExtensionManagerEvent.EM_SMTPCONNECT, new SMTPConnectEvent());
		eventsMap.put(IExtensionManagerEvent.EM_SMTPCOMMAND, new SMTPCommandEvent());
		eventsMap.put(IExtensionManagerEvent.EM_SMTPMESSAGEACCEPT, new SMTPMessageAcceptEvent());
		eventsMap.put(IExtensionManagerEvent.EM_SMTPDISCONNECT, new SMTPDisconnectEvent());
		eventsMap.put(IExtensionManagerEvent.HOOK_EVENT_NOTE_UPDATE, new NSFHookNoteUpdateEvent());
		eventsMap.put(IExtensionManagerEvent.HOOK_EVENT_NOTE_OPEN, new NSFHookNoteOpenEvent());

		// Not yet implemented...
		// eventsMap.put( IExtensionManagerEvent.EM_NSFDBWRITEOBJECT , NSFDbWriteObjectEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFCLOSECOLLECTION , NIFCloseCollectionEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYKEY , NIFFindByKeyEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFFINDBYNAME , NIFFindByNameEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFOPENNOTE , NIFOpenNoteEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFREADENTRIES , NIFReadEntriesEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NIFUPDATECOLLECTION , NIFUpdateCollectionEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_SCHFREETIMESEARCH, SCHFreeTimeSearchEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_SCHRETRIEVE, SCHRetrieveEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_SCHSRVRETRIEVE, SCHSrvRetrieveEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEATTACHOLE2OBJECT, NSFNoteAttachOLE2ObjectEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEDELETEOLE2OBJECT, NSFNoteDeleteOLE2ObjectEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFNOTEEXTRACTOLE2OBJECT, NSFNoteExtractOLE2ObjectEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_FTSEARCHEXT, FTSearchExtEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP, NameLookupEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_AGENTRUN, AgentRunEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_AGENTCLOSE, AgentCloseEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NAMELOOKUP2, NameLookup2Event());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVECOPYNOTES, NSFArchiveCopyNotesEvent());
		// eventsMap.put( IExtensionManagerEvent.EM_NSFARCHIVEDELETENOTES, NSFArchiveDeleteNotesEvent());

	}

	private DotsEventFactory() {

	}

	private ConcurrentLinkedQueue<DotsEvent> recycleBin_ = new ConcurrentLinkedQueue<DotsEvent>();

	private void _recycleEvent(final DotsEvent event) {
		event.recycle();
		synchronized (recycleBin_) {
			recycleBin_.add(event);
		}
	}

	public static void recycleEvent(final DotsEvent event) {
		INSTANCE._recycleEvent(event);
	}

	private DotsEvent _getEvent() {
		DotsEvent result = null;
		synchronized (recycleBin_) {
			if (recycleBin_.isEmpty()) {
				recycleBin_.add(new DotsEvent(DotsEvent.TYPE.TRIGGERED));
			}
			result = recycleBin_.poll();
		}

		return result;
	}

	public static int getEventId(final String commandBuffer) {
		int iIndex = commandBuffer.indexOf(EM_EVENT_POSTFIX);
		if (iIndex < 0)
			return -1;
		int eventId = Integer.parseInt(commandBuffer.substring(EM_EVENT_PREFIX_LEN, iIndex).trim());
		return eventId;
	}

	public static String getEventParams(final String commandBuffer) {
		int iIndex = commandBuffer.indexOf(EM_EVENT_POSTFIX);
		String params = commandBuffer.substring(iIndex + EM_EVENT_POSTFIX.length());
		return params;
	}

	public static String runEventsForString(final String commandBuffer) {
		int eventid = getEventId(commandBuffer);
		if (eventid == -1)
			return commandBuffer;
		if (TaskletManager.INSTANCE.getTriggerTasks(eventid) != null) {
			// System.out.println("Trigger found for type: " + eventid);
			String params = getEventParams(commandBuffer);
			IExtensionManagerEvent event = eventsMap.get(eventid);
			Set<AbstractTasklet> tasks = TaskletManager.INSTANCE.getTriggerTasks(eventid);
			synchronized (tasks) {
				if (!tasks.isEmpty()) {
					for (AbstractTasklet task : tasks) {
						List<Method> methods = task.getTriggerMethods(eventid);
						for (Method crystal : methods) {
							DotsEvent de = INSTANCE._getEvent();
							de.loadMethod(crystal, task);
							de.loadEvent(event, params);
							// System.out.println("Setup DotsEvent for " + eventid + ": " + params + " (" + task.getId() + "."
							// + crystal.getName() + ")");
							TaskletManager.INSTANCE.execute(de);
						}
					}
				}
			}
		} else {
			// System.out.print("No triggers registered for event type: " + eventid);
		}
		return null;
	}

}
