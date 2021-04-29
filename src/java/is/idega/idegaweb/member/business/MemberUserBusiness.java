/*
 * $Id: MemberUserBusiness.java,v 1.15 2007/04/24 13:35:01 eiki Exp $
 * Created on Jan 4, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import com.idega.business.IBOService;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 *
 *  Last modified: $Date: 2007/04/24 13:35:01 $ by $Author: eiki $
 *
 * @author <a href="mailto:palli@idega.com">palli</a>
 * @version $Revision: 1.15 $
 */
public interface MemberUserBusiness extends IBOService, UserBusiness {

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#moveUserBetweenDivisions
	 */
	public boolean moveUserBetweenDivisions(User user, Group fromDivisionGroup, Group toDivisionGroup,
			IWTimestamp term, IWTimestamp init, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getRegionalUnionGroupForClubGroup
	 */
	public Group getRegionalUnionGroupForClubGroup(Group club) throws NoRegionalUnionFoundException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getFederationGroupForClubGroup
	 */
	public Group getFederationGroupForClubGroup(Group club) throws NoFederationFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getLeagueGroupListForClubGroup
	 */
	public List<Group> getLeagueGroupListForClubGroup(Group club) throws NoLeagueFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#sendEmailFromIWMemberSystemAdministrator
	 */
	public boolean sendEmailFromIWMemberSystemAdministrator(String toEmailAddress, String CC, String BCC,
			String subject, String theMessageBody) throws MessagingException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllClubDivisionsForLeague
	 */
	public Collection<Group> getAllClubDivisionsForLeague(Group league) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getLeaguesListForUserFromTopNodes
	 */
	public List<Group> getLeaguesListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getFederationListForUserFromTopNodes
	 */
	public List<Group> getFederationListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getUnionListForUserFromTopNodes
	 */
	public List<Group> getUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubListForUserFromTopNodes
	 */
	public List<Group> getClubListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubListForUserFromTopNodes
	 */
	public List<Group> getDivisionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getRegionalUnionListForUserFromTopNodes
	 */
	public List<Group> getRegionalUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getGroupListForUserFromTopNodesAndGroupType
	 */
	public List<Group> getGroupListForUserFromTopNodesAndGroupType(User user, String groupType, IWUserContext iwuc)
			throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllRegionalUnionGroups
	 */
	public Collection<Group> getAllGroupsByGroupType(String groupType) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllRegionalUnionGroups
	 */
	public Collection<Group> getAllRegionalUnionGroups() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllLeagueGroups
	 */
	public Collection<Group> getAllLeagueGroups() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getAllClubGroups
	 */
	public Collection<Group> getAllClubGroups() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubGroupsForRegionUnionGroup
	 */
	public Collection<Group> getClubGroupsForRegionUnionGroup(Group regionalUnion) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubListForUser
	 */
	public List<Group> getClubListForUser(User user) throws NoClubFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getGroupTemporaryListForUser
	 */
	public List<Group> getGroupTemporaryListForUser(User user) throws NoClubFoundException, RemoteException;


/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getDivisionListForUser
	 */
	public List<Group> getDivisionListForUser(User user) throws NoDivisionFoundException,RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubForGroup
	 */
	public Group getClubForGroup(Group group) throws NoClubFoundException, RemoteException;
	public com.idega.user.data.bean.Group getClubForGroup(Integer id) throws NoClubFoundException;
	public com.idega.user.data.bean.Group getClubForGroup(Integer groupId, Boolean full) throws NoClubFoundException;

	public List<com.idega.user.data.bean.Group> getClubsForGroups(List<Integer> groupsIds);
	public List<Integer> getClubsIdsForGroups(List<Integer> groupsIds);

	public List<Integer> getClubDivisionsIdsForGroups(List<Integer> groupsIds);

	public List<com.idega.user.data.bean.Group> getLeaguesForGroups(List<Integer> groupsIds);
	public List<Integer> getLeaguesIdsForGroups(List<Integer> groupsIds);

	public List<com.idega.user.data.bean.Group> getRegionalUnionsForGroups(List<Integer> groupsIds);
	public List<Integer> getRegionalUnionsIdsForGroups(List<Integer> groupsIds);

	public List<Group> getActiveMembersGroupsForClubDivision(Group clubDivision);

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getDivisionForClub
	 */
	public Group getDivisionForClub(Group club) throws NoDivisionFoundException, RemoteException;

	public List<Group> getDivisionsForClub(Group club) throws NoDivisionFoundException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getDivisionForGroup
	 */
	public Group getDivisionForGroup(Group group) throws NoDivisionFoundException, RemoteException;
	public com.idega.user.data.bean.Group getDivisionForGroup(Integer id) throws NoDivisionFoundException;
	public com.idega.user.data.bean.Group getDivisionForGroup(Integer id, Boolean full) throws NoDivisionFoundException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubMemberNumberForUser
	 */
	public String getClubMemberNumberForUser(User user, Group club) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#isClubUsingTheMemberSystem
	 */
	public boolean isClubUsingTheMemberSystem(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#setClubMemberNumberForUser
	 */
	public boolean setClubMemberNumberForUser(String number, User user, Group club) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubCollectionGroupForLeague
	 * @param league
	 * @return
	 * @throws RemoteException
	 * @throws NoLeagueClubCollectionGroup
	 */
	public Group getClubCollectionGroupForLeague(Group league) throws RemoteException, NoLeagueClubCollectionGroup;

	/**
	 * @see is.idega.idegaweb.member.business.MemberUserBusinessBean#getClubNumberForGroup
	 * @param group
	 * @return
	 * @throws NoClubFoundException
	 * @throws RemoteException
	 */
	public String getClubNumberForGroup(Group group) throws NoClubFoundException, RemoteException;
	public String getClubNumberForGroup(com.idega.user.data.bean.Group group) throws NoClubFoundException, RemoteException;

	public com.idega.user.data.bean.Group getUnionForGroup(Integer groupId) throws NoUnionFoundException;

	public com.idega.user.data.bean.Group getUnionOrRegionalUnionForGroup(Integer groupId) throws NoUnionFoundException;

	public List<Group> getGroupClubPlayerListForUser(User user) throws RemoteException;

	public List<com.idega.user.data.bean.Group> getGroupsWithTypesForGroup(List<Integer> groupsIds, List<String> types);

	public Group getLeagueForDivision(Group division);
	public com.idega.user.data.bean.Group getLeagueForDivision(Integer divisionId);

	public boolean addGeneralMember(Integer groupId, User user, User addedBy, Date timestamp);

}