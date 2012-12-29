/**
 * 
 */
package com.ibm.dots.tasklet.events;

import java.util.EnumMap;

/**
 * @author nfreeman
 * 
 */
public enum DotsEventParams implements IDotsEventParam {
	SourceDbpath, DestDbpath, DataDbpath, Noteid, Flag(Long.class), Username, Dbclass(Integer.class), Forcecreate(Boolean.class), RequestNoteid, ResponseNoteid, Unid, Stopfile, DocsAdded(
			Long.class), DocsUpdated(Long.class), DocsDelete(Long.class), BytesIndexed(Long.class), Query, Options(Long.class), Limit(
			Long.class), DocsReturned(Long.class), SizeBefore(Long.class), SizeAfter(Long.class), DocsCount(Integer.class), FromName, ToName, SinceSeqNum(
			Long.class), Filename, OriginalFilename, EncodingType(Integer.class), Itemname, From, Insert(Boolean.class), Update(
			Boolean.class), Delete(Boolean.class), FolderNoteid, AddOperation(Boolean.class), SinceTimeDate, NoteClass(Integer.class), Manager, DefaultAccess(
			Integer.class), RemoteName, NetProtocol(Integer.class), NetAddress, WEvent(Integer.class), SessionId(Long.class), Command, MaxCommandLen(
			Long.class), SMTPReply, SMTPReplyLength(Long.class), RemoteIP, RemoteHost, PossibileRelay(Boolean.class), SMTPGreeting, SMTPMaxGreetingLen(
			Long.class);

	final Class<?> type_;

	private DotsEventParams() {
		type_ = String.class;
	}

	private DotsEventParams(Class<?> c) {
		type_ = c;
	}

	@Override
	public Class<?> getType() {
		return type_;
	}

	public static void populateParamMap(EnumMap<DotsEventParams, Object> map, DotsEventParams[] params, String buffer) {
		String[] values = null;
		if (buffer == null || buffer.length() == 0) {
			values = new String[0];
		} else {
			if (buffer.endsWith(",")) {
				buffer += " ";
			}
			values = buffer.split(",");
		}
		for (int i = 0; i < values.length; i++) {
			if (params.length > i) {
				DotsEventParams param = params[i];
				if (param.equals(DotsEventParams.Noteid)) {
					int nid = Integer.parseInt(values[i], 10);
					map.put(param, Integer.toHexString(nid));
				} else if (String.class.equals(param.getType())) {
					map.put(param, values[i]);
				} else if (Integer.class.equals(param.getType())) {
					map.put(param, Integer.parseInt(values[i], 16));
				} else if (Boolean.class.equals(param.getType())) {
					map.put(param, ("0".equals(values[i]) || "false".equals(values[i])));
				} else if (Long.class.equals(param.getType())) {
					map.put(param, Long.parseLong(values[i], 16));
				}
			} else {
				if (!map.containsKey(DotsEventParams.Username)) {
					map.put(DotsEventParams.Username, values[i]);
				}
			}
		}

	}

}
