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






package com.ibm.domino.commons.model;

import java.util.Date;
import java.util.List;

import lotus.domino.Session;

public interface IFreeRoomsProvider {

    public List<Room> getFreeRooms(Session session, String site, Date start, Date end, int capacity) throws ModelException;
}
