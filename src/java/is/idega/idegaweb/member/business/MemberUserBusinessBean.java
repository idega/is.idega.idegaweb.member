package is.idega.idegaweb.member.business;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.mail.MessagingException;

import com.idega.core.contact.data.Email;
import com.idega.data.MetaDataCapable;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserBusinessBean;
import com.idega.user.dao.GroupDAO;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupTypeConstants;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.DBUtil;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;

import is.idega.idegaweb.member.util.IWMemberConstants;

/**
 * Description:	Use this business class to handle member information
 * Copyright:    Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.1
 */
public class MemberUserBusinessBean extends UserBusinessBean implements MemberUserBusiness, UserBusiness {

	private static final long serialVersionUID = -3645515876345368597L;

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.business.MemberUserBusiness#moveUserBetweenDivisions(com.idega.user.data.User, com.idega.user.data.Group, com.idega.user.data.Group, com.idega.util.IWTimestamp, com.idega.util.IWTimestamp)
	 */
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	@Override
	public boolean moveUserBetweenDivisions(User user, Group fromDivisionGroup, Group toDivisionGroup, IWTimestamp term, IWTimestamp init, IWUserContext iwuc)  throws RemoteException{
		//this method get the parents of the user and finds out which is of the correct type and then uses that.
		//the division that the user is sent to must have a child group of type iwme_temporary

		try {
			GroupBusiness groupBiz = getGroupBusiness();
			Collection<Group> usersParentGroup = groupBiz.getParentGroups(user);
			Collection<Group> childrenOfToDivisonGroup = groupBiz.getChildGroups(toDivisionGroup);
			Collection<Group> parentsOfToDivisionGroup = groupBiz.getParentGroupsRecursive(toDivisionGroup);

			Group fromLeagueGroup = groupBiz.getGroupByGroupID(Integer.parseInt(fromDivisionGroup.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION)));
			Group toLeagueGroup = groupBiz.getGroupByGroupID(Integer.parseInt(toDivisionGroup.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION)));
			Group fromRegionalUnionGroup = null;
			Group toRegionalUnionGroup = null;

			//find the player groups relations and set them to passive_pending
			if (usersParentGroup != null && !usersParentGroup.isEmpty() && childrenOfToDivisonGroup!=null && !childrenOfToDivisonGroup.isEmpty() ) {//user must have parents!
				Iterator<Group> iter = usersParentGroup.iterator();

				boolean existsInFromDivision = false;

				//handle from division
				while (iter.hasNext()) {
					Group parent = iter.next();
					String type = parent.getGroupType();
					if ( IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(type) ) {
						Collection<Group> par = groupBiz.getParentGroupsRecursive(parent);
						if (par.contains(fromDivisionGroup)) {

							//find the regional union
							Iterator<Group> parIter = par.iterator();
							while (parIter.hasNext()) {
								Group parentGroup = parIter.next();
								if( IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(parentGroup.getGroupType()) ){
									fromRegionalUnionGroup = parentGroup;
									break;
								}
							}

							//change all relation within that division to pending. To much maybe?
							existsInFromDivision = true;
							Collection<GroupRelation> col = groupBiz.getGroupRelationHome().findGroupsRelationshipsContainingBiDirectional( ((Integer)fromDivisionGroup.getPrimaryKey()).intValue(), ((Integer)parent.getPrimaryKey()).intValue() , "ST_ACTIVE" ); //Status liklega otharfi
							if(col!=null && !col.isEmpty()){
								Iterator<GroupRelation> iterator = col.iterator();
								while (iterator.hasNext()) {
									GroupRelation rel = iterator.next();
									rel.setPassivePending();
									rel.setTerminationDate(term.getTimestamp());
									rel.store();
									//set passive by?
								}
							}
						}
					}
				}

//			handle to division
				if(existsInFromDivision){

					//find the regional union
					Iterator<Group> parIter2 = parentsOfToDivisionGroup.iterator();
					while (parIter2.hasNext()) {
						Group parentGroup = parIter2.next();
						if( IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(parentGroup.getGroupType()) ){
							toRegionalUnionGroup = parentGroup;
							break;
						}
					}

					//set the users relations to the new divisions temporary group to active_pending
					Iterator<Group> iter2 = childrenOfToDivisonGroup.iterator();

					while (iter2.hasNext()) {
						Group child = iter2.next();
						String type = child.getGroupType();
						if (type.equals(IWMemberConstants.GROUP_TYPE_TEMPORARY)) {

							GroupRelation rel = groupBiz.getGroupRelationHome().create();
							rel.setRelatedGroup(user);
							rel.setGroup(child);
							rel.setRelationshipType(IWMemberConstants.GROUP_RELATION_TYPE_PARENT);
							rel.setActivePending();
							rel.setInitiationDate(init.getTimestamp());
							rel.store();
							break;//should only have one temp group!
						}
					}

					return sendEmailsForMembersTransfer(user,fromDivisionGroup,toDivisionGroup,fromLeagueGroup,toLeagueGroup, fromRegionalUnionGroup, toRegionalUnionGroup, term, init, iwuc );
				}

			}

		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}


		return true;
	}

	/**
	 * Sends out a report via email to all parties concerned
	 * @return boolean true if no errors occurred
	 */
	private boolean sendEmailsForMembersTransfer(User user, Group fromDivisionGroup, Group toDivisionGroup, Group fromLeagueGroup, Group toLeagueGroup, Group fromRegionalUnionGroup, Group toRegionalUnionGroup, IWTimestamp term, IWTimestamp init, IWUserContext iwuc)  throws RemoteException{

		if(!fromLeagueGroup.equals(toLeagueGroup)){
			System.err.println("MemberUserBusiness : Error transfering user because the leagues are not the same! from: "+fromLeagueGroup.getName()+" to: "+toLeagueGroup.getName());
			return false;
		}


		IWResourceBundle iwrb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwuc.getCurrentLocale());
		String toEmailAddress;

		String subject = iwrb.getLocalizedString("member_transfer.email_subject","IWMember transfer announcement");
		StringBuffer theMessageBody = new StringBuffer();
		try {
			theMessageBody.append(iwrb.getLocalizedString("member_transfer.email_body_automatic_message_text","This is an automatic confirmation message for a member transfer.")).append("\n\n")
			.append(iwrb.getLocalizedString("member_transfer.email_main_text","The member stated in this email will transfered from : "))
			.append(getGroupBusiness().getNameOfGroupWithParentName(fromDivisionGroup))
			.append(iwrb.getLocalizedString("member_transfer.email_main_to"," to : ")).append(getGroupBusiness().getNameOfGroupWithParentName(toDivisionGroup)).append(".\n");
		}
		catch (RemoteException e1) {

			e1.printStackTrace();
			return false;
		}

		theMessageBody.append(iwrb.getLocalizedString("member_transfer.email_main_text_user_name","User : ")).append(user.getName()).append("\n")
		.append(iwrb.getLocalizedString("member_transfer.email_main_text_user_pin","Social security number : ")).append(user.getPersonalID()).append("\n")
		.append(iwrb.getLocalizedString("member_transfer.email_main_text_date_from","Date from : ")).append(term.toString()).append("\n")
		.append(iwrb.getLocalizedString("member_transfer.email_main_text_date_to","Date to : ")).append(init.toString()).append("\n");

		String body = theMessageBody.toString();

		Collection<Email> userEmails = user.getEmails();
		if(userEmails!=null && !userEmails.isEmpty()){
			toEmailAddress = userEmails.iterator().next().getEmailAddress();
			try {
				sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}

		Collection<Email> leagueEmails = fromLeagueGroup.getEmails();//is same as to league
		if(leagueEmails!=null && !leagueEmails.isEmpty()){
			toEmailAddress = leagueEmails.iterator().next().getEmailAddress();
			try {
				sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
			}
			catch (MessagingException e) {
				e.printStackTrace();
			}
		}

		if(toRegionalUnionGroup!=null){
			Collection<Email> toRegionalEmails = toRegionalUnionGroup.getEmails();
			if(toRegionalEmails!=null && !toRegionalEmails.isEmpty()){
				toEmailAddress = toRegionalEmails.iterator().next().getEmailAddress();
				try {
					sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
				}
				catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}

		if(toRegionalUnionGroup!=null){
			Collection<Email> fromRegionalEmails = fromRegionalUnionGroup.getEmails();
			if(fromRegionalEmails!=null && !fromRegionalEmails.isEmpty()){
				toEmailAddress = fromRegionalEmails.iterator().next().getEmailAddress();
				try {
					sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
				}
				catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}


		return true;
	}


	/**
	 * Checks the groups parents for a regional union and returns the first it finds.
	 * @param club (Group)
	 * @return the regional union for that club if it exists, otherwise throws an exception
	 * @throws NoRegionalUnionFoundException
	 */
	@Override
	public Group getRegionalUnionGroupForClubGroup(Group club) throws NoRegionalUnionFoundException{
		List<Group> parents = club.getParentGroups();
		Iterator<Group> it = parents.iterator();
		Group regionalUnionGroup = null;

		if (it != null) {
			while (it.hasNext()) {
				Group parent = it.next();
				if (IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(parent.getGroupType())){
					regionalUnionGroup = parent;
				}
			}
		}

		if(regionalUnionGroup==null){
			throw new NoRegionalUnionFoundException(club.getName());
		}
		else{
			return regionalUnionGroup;
		}
	}

	/**
	 * TEMP IMPLEMENTATION : Gets all groups of the type iwme_federation if there are more than one than it only returns the first one!
	 * @param club (Group)
	 * @return the federation union for that club if it exists, otherwise throws an exception
	 * @throws NoFederationFoundException
	 */
	@Override
	public Group getFederationGroupForClubGroup(Group club) throws NoFederationFoundException, RemoteException{
		try {
			Collection<Group> federations = this.getGroupBusiness().getGroupHome().findGroupsByType(IWMemberConstants.GROUP_TYPE_FEDERATION);
			return federations.iterator().next();
		} catch (FinderException e) {
			e.printStackTrace();
			throw new NoFederationFoundException(club.getName());
		}

	}

	/**
	 * Checks the groups children for club divisons and then gets the division league connection and add to the list it returns
	 * @param club (Group)
	 * @return a list of leagues for that club if it has connections to any, otherwise throws an exception
	 * @throws NoLeagueFoundException
	 */
	@Override
	public List<Group> getLeagueGroupListForClubGroup(Group club) throws NoLeagueFoundException, RemoteException{
		String[] divisionType = {IWMemberConstants.GROUP_TYPE_CLUB_DIVISION};
		List<Group> children = club.getChildGroups(divisionType,true);
		List<Group> list = new ArrayList<Group>();

		if(children!=null && !children.isEmpty()){

			Iterator<Group> it = children.iterator();

			while (it.hasNext()) {
				Group div = it.next();
				Group league = getLeagueForDivision(div);
				if (league != null) {
					list.add(league);
				}
			}
		}


		if(list.isEmpty()){
			throw new NoLeagueFoundException(club.getName());
		}
		else{
			return list;
		}

	}

	@Override
	public com.idega.user.data.bean.Group getLeagueForDivision(Integer divisionId) {
		if (divisionId == null) {
			return null;
		}

		Group division = null;
		try {
			division = getGroupBusiness().getGroupByGroupID(divisionId);
		} catch (Exception e) {}

		Group league = getLeagueForDivision(division);
		if (league == null) {
			return null;
		}

		GroupDAO groupDAO = ELUtil.getInstance().getBean(GroupDAO.class);
		return groupDAO.findGroup(Integer.valueOf(league.getId()));
	}

	@Override
	public Group getLeagueForDivision(Group division) {
		return getLeagueForDivision(getLeagueId(division), Group.class.getName());
	}

	private String getLeagueId(MetaDataCapable division) {
		if (division == null) {
			return null;
		}

		return division.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
	}

	private <T extends Serializable> T getLeagueForDivision(String leagueId, String resultName) {
		if (StringUtil.isEmpty(leagueId) || StringUtil.isEmpty(resultName)) {
			return null;
		}

		if (StringHandler.isNumeric(leagueId)) {
			Integer id;
			try {
				id = Integer.parseInt(leagueId);

				if (id == -1) {
					return null;
				}

				if (resultName.equals(Group.class.getName())) {
					@SuppressWarnings("unchecked")
					T result = (T) this.getGroupBusiness().getGroupByGroupID(id);
					return result;
				} else if (resultName.equals(Integer.class.getName())) {
					@SuppressWarnings("unchecked")
					T result = (T) id;
					return result;
				} else if (resultName.equals(com.idega.user.data.bean.Group.class.getName())) {
					GroupDAO groupDAO = ELUtil.getInstance().getBean(GroupDAO.class);
					@SuppressWarnings("unchecked")
					T result = (T) groupDAO.findGroup(id);
					return result;
				} else {
					getLogger().warning("Unknown result type: " + resultName);
				}
			} catch (NumberFormatException e) {
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Error getting league for division. League ID: " + leagueId, e);
			}
		}

		return null;
	}

	@Override
	public boolean sendEmailFromIWMemberSystemAdministrator(String toEmailAddress, String CC, String BCC,String subject, String theMessageBody) throws MessagingException{
    	IWMainApplicationSettings settings = getIWMainApplication().getSettings();
		String systemEmailAddress = settings.getProperty(IWMemberConstants.APPLICATION_PARAMETER_ADMINISTRATOR_MAIN_EMAIL);
		String systemMailServer = settings.getProperty(IWMemberConstants.APPLICATION_PARAMETER_MAIL_SERVER);
		com.idega.util.SendMail.send(systemEmailAddress,toEmailAddress,CC,BCC,systemMailServer,subject,theMessageBody);
		return true;
	}

	//temp refactor this class to MemberBusiness or move this method to that class
	/**
	 * @return A collection of groups (of the type iwme_club_division)
	 */
	@Override
	public Collection<Group> getAllClubDivisionsForLeague(Group league) throws RemoteException{
		Collection<Group> groups = null;
		try {
			groups  = getGroupHome().findGroupsByMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION,league.getPrimaryKey().toString());
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return groups;
	}

	/*
	 * Return a list of League groups if the user has a league as a top node.
	 */
	@Override
	public List<Group> getLeaguesListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_LEAGUE,iwuc);
	}

	/*
	 * Return a list of Federation groups if the user has a Federation as a top node.
	 */
	@Override
	public List<Group> getFederationListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_FEDERATION,iwuc);
	}

	/*
	 * Return a list of Union groups if the user has a Union as a top node.
	 */
	@Override
	public List<Group> getUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_UNION,iwuc);
	}

	/*
	  * Return a list of Club groups if the user has a club as a top node.
	 */
	@Override
	public List<Group> getClubListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_CLUB,iwuc);
	}

	/*
	  * Return a list of Division groups if the user has a division as a top node.
	 */
	@Override
	public List<Group> getDivisionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_CLUB_DIVISION,iwuc);
	}

	/*
	 * Return a list of Regional Union groups if the user has a Regional Union as a top node.
	 */
	@Override
	public List<Group> getRegionalUnionListForUserFromTopNodes(User user, IWUserContext iwuc) throws RemoteException{
		return getGroupListForUserFromTopNodesAndGroupType(user,IWMemberConstants.GROUP_TYPE_REGIONAL_UNION,iwuc);
	}

	@Override
	public List<Group> getGroupListForUserFromTopNodesAndGroupType(User user, String groupType, IWUserContext iwuc) throws RemoteException{
		Collection<Group> tops = getUsersTopGroupNodesByViewAndOwnerPermissions(user,iwuc);
		List<Group> list = new ArrayList<Group>();
		if(tops!=null && !tops.isEmpty()){
			Iterator<Group> iter = tops.iterator();
			while (iter.hasNext()) {
				Group group = iter.next();
				if(groupType.equals(group.getGroupType())){
					list.add(group);
				}
			}

		}
		return list;
	}

	/**
	 * @return All groups with a certain type
	 * @throws RemoteException
	 */
	@Override
	public Collection<Group> getAllGroupsByGroupType(String groupType) throws RemoteException{
		try {
			return this.getGroupBusiness().getGroupHome().findGroupsByType(groupType);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * @return All groups with the type iwme_regional_union
	 * @throws RemoteException
	 */
	@Override
	public Collection<Group> getAllRegionalUnionGroups() throws RemoteException{
		try {
			return this.getGroupBusiness().getGroupHome().findGroupsByType(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * @return All groups with the type iwme_league
	 * @throws RemoteException
	 */
	@Override
	public Collection<Group> getAllLeagueGroups() throws RemoteException{
		try {
			return this.getGroupBusiness().getGroupHome().findGroupsByType(IWMemberConstants.GROUP_TYPE_LEAGUE);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * @return All groups with the type iwme_club
	 * @throws RemoteException
	 */
	@Override
	public Collection<Group> getAllClubGroups() throws RemoteException{
		try {
			return this.getGroupBusiness().getGroupHome().findGroupsByType(IWMemberConstants.GROUP_TYPE_CLUB);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * @return All groups with the type iwme_club that are children of the supplied regional union group
	 */
	@Override
	public Collection<Group> getClubGroupsForRegionUnionGroup(Group regionalUnion) throws RemoteException{
		String[] clubType = { IWMemberConstants.GROUP_TYPE_CLUB };
		return regionalUnion.getChildGroups(clubType,true);
	}

	/*
	 * Returns a list of all the clubs the user is a member of.
	 */
	@Override
	public List<Group> getClubListForUser(User user) throws NoClubFoundException,RemoteException{
		Collection<Group> parents = getGroupBusiness().getParentGroupsRecursive((Group) user);
		List<Group> list = new ArrayList<Group>();
		if (!ListUtil.isEmpty(parents)) {
			Iterator<Group> iter = parents.iterator();
			while (iter.hasNext()) {
				Group group = iter.next();
				if (IWMemberConstants.GROUP_TYPE_CLUB.equals(group.getGroupType())) {
					list.add(group);
				}
			}
		}

		if (list.isEmpty()) {
			//if no club is found we throw the exception
			throw new NoClubFoundException(user.getName());
		}
		else {
			return list;
		}
	}

	/*
	 * Returns a list of all the divisions the user is a member of.
	 */
	@Override
	public List<Group> getDivisionListForUser(User user) throws NoDivisionFoundException,RemoteException{
		Collection<Group> parents = getGroupBusiness().getParentGroupsRecursive((Group) user);
		List<Group> list = new ArrayList<Group>();
		if (parents!=null && !parents.isEmpty()) {
			Iterator<Group> iter = parents.iterator();
			while (iter.hasNext()) {
				Group group = iter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(group.getGroupType()) || IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_INNER.equals(group.getGroupType())){
					list.add(group);
				}
			}
		}

		if(list.isEmpty()){
			//if no division is found we throw the exception
			throw new NoDivisionFoundException(user.getName());
		}
		else {
			return list;
		}
	}

	/*
	 * Returns a list of all the groups of type "iwme_temporary" the user is a member of.
	 */
	@Override
	public List<Group> getGroupTemporaryListForUser(User user) throws RemoteException{
		Collection<Group> parents = getGroupBusiness().getParentGroupsRecursive((Group) user);
		List<Group> list = new ArrayList<Group>();
		if (parents!=null && !parents.isEmpty()) {
			Iterator<Group> iter = parents.iterator();
			while (iter.hasNext()) {
				Group group = iter.next();
				if(IWMemberConstants.GROUP_TYPE_TEMPORARY.equals(group.getGroupType())){
					list.add(group);
				}
			}
		}

		return list;
	}

	/*
	 * Returns a list of all the groups of type "iwme_club_player" the user is a member of.
	 */
	@Override
	public List<Group> getGroupClubPlayerListForUser(User user) throws RemoteException{
		Collection<Group> parents = getGroupBusiness().getParentGroupsRecursive((Group) user);
		List<Group> list = new ArrayList<Group>();
		if (parents!=null && !parents.isEmpty()) {
			Iterator<Group> iter = parents.iterator();
			while (iter.hasNext()) {
				Group group = iter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(group.getGroupType())){
					list.add(group);
				}
			}
		}

		return list;
	}

	/*
		* Returns the club that is a parent for this group.
	 */
	@Override
	public Group getClubForGroup(Group group) throws NoClubFoundException, RemoteException{
		Collection<Group> parents = getGroupBusiness().getParentGroupsRecursive(group);

		if(parents!=null && !parents.isEmpty()){
			Iterator<Group> iter = parents.iterator();
			while (iter.hasNext()) {
				Group parentGroup = iter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB.equals(parentGroup.getGroupType())){
					return parentGroup;//there should only be one
				}
			}
		}

		//if no club is found we throw the exception
		throw new NoClubFoundException(group.getName());
	}

	@Override
	public com.idega.user.data.bean.Group getUnionForGroup(Integer groupId) throws NoUnionFoundException {
		com.idega.user.data.bean.Group group = getGroupWithTypeForGroup(groupId, IWMemberConstants.GROUP_TYPE_UNION, true);
		if (group == null) {
			throw new NoUnionFoundException(String.valueOf(groupId));
		}

		return group;
	}

	@Override
	public com.idega.user.data.bean.Group getUnionOrRegionalUnionForGroup(Integer groupId) throws NoUnionFoundException {
		com.idega.user.data.bean.Group group = getGroupWithTypeForGroup(groupId, Arrays.asList(IWMemberConstants.GROUP_TYPE_UNION, IWMemberConstants.GROUP_TYPE_REGIONAL_UNION), true);
		if (group == null) {
			throw new NoUnionFoundException(String.valueOf(groupId));
		}

		return group;
	}

	@Override
	public com.idega.user.data.bean.Group getClubForGroup(Integer groupId) throws NoClubFoundException {
		return getClubForGroup(groupId, true);
	}

	@Override
	public com.idega.user.data.bean.Group getClubForGroup(Integer groupId, Boolean full) throws NoClubFoundException {
		com.idega.user.data.bean.Group group = getGroupWithTypeForGroup(groupId, IWMemberConstants.GROUP_TYPE_CLUB, full);
		if (group == null) {
			throw new NoClubFoundException(String.valueOf(groupId));
		}

		return group;
	}

	@Override
	public List<com.idega.user.data.bean.Group> getClubsForGroups(List<Integer> groupsIds) {
		return getGroupsWithTypesForGroup(groupsIds, Arrays.asList(IWMemberConstants.GROUP_TYPE_CLUB));
	}
	@Override
	public List<Integer> getClubsIdsForGroups(List<Integer> groupsIds) {
		return getGroupsWithTypesForGroup(groupsIds, Arrays.asList(IWMemberConstants.GROUP_TYPE_CLUB), true, Integer.class);
	}

	@Override
	public List<Integer> getClubDivisionsIdsForGroups(List<Integer> groupsIds) {
		return getGroupsWithTypesForGroup(groupsIds, Arrays.asList(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION, IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_INNER), true, Integer.class);
	}

	@Override
	public List<com.idega.user.data.bean.Group> getLeaguesForGroups(List<Integer> groupsIds) {
		return getLeagues(groupsIds, com.idega.user.data.bean.Group.class);
	}
	@Override
	public List<Integer> getLeaguesIdsForGroups(List<Integer> groupsIds) {
		return getLeagues(groupsIds, Integer.class);
	}
	private <T extends Serializable> List<T> getLeagues(List<Integer> groupsIds, Class<T> resultType) {
		List<String> types = Arrays.asList(IWMemberConstants.GROUP_TYPE_LEAGUE);

		List<T> results = getGroupsWithTypesForGroup(groupsIds, types, true, resultType);
		if (!ListUtil.isEmpty(results)) {
			return results;
		}

		List<com.idega.user.data.bean.Group> clubDivisions = getGroupsWithTypesForGroup(groupsIds, Arrays.asList(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION));
		if (ListUtil.isEmpty(clubDivisions)) {
			return null;
		}

		results = new ArrayList<>();
		for (com.idega.user.data.bean.Group division: clubDivisions) {
			if (division != null) {
				division = DBUtil.getInstance().lazyLoad(division);
			}
			T league = getLeagueForDivision(getLeagueId(division), resultType.getName());
			if (league != null) {
				results.add(league);
			}
		}
		return results;
	}

	@Override
	public List<com.idega.user.data.bean.Group> getRegionalUnionsForGroups(List<Integer> groupsIds) {
		return getGroupsWithTypesForGroup(groupsIds, Arrays.asList(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION));
	}
	@Override
	public List<Integer> getRegionalUnionsIdsForGroups(List<Integer> groupsIds) {
		return getGroupsWithTypesForGroup(groupsIds, Arrays.asList(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION), true, Integer.class);
	}

	private com.idega.user.data.bean.Group getGroupWithTypeForGroup(Integer groupId, String type, Boolean full) {
		if (StringUtil.isEmpty(type)) {
			return null;
		}

		return getGroupWithTypeForGroup(groupId, Arrays.asList(type), full);
	}

	private com.idega.user.data.bean.Group getInitializedGroup(com.idega.user.data.bean.Group group) {
		if (group == null) {
			return null;
		}

		group = DBUtil.getInstance().lazyLoad(group);
		return group;
	}
	private void doInitializeGroups(List<com.idega.user.data.bean.Group> groups) {
		if (ListUtil.isEmpty(groups)) {
			return;
		}

		DBUtil util = DBUtil.getInstance();
		for (com.idega.user.data.bean.Group group: groups) {
			util.lazyLoad(group);
		}
	}

	private com.idega.user.data.bean.Group getGroupWithTypeForGroup(Integer groupId, List<String> types, Boolean full) {
		List<com.idega.user.data.bean.Group> groups = getGroupsWithTypesForGroup(groupId, types, full);
		return ListUtil.isEmpty(groups) ? null : groups.iterator().next();
	}

	private List<com.idega.user.data.bean.Group> getGroupsWithTypesForGroup(Integer groupId, List<String> types, Boolean full) {
		if (groupId == null || ListUtil.isEmpty(types)) {
			return null;
		}

		GroupDAO groupDAO = ELUtil.getInstance().getBean(GroupDAO.class);
		com.idega.user.data.bean.Group group = groupDAO.findGroup(groupId);
		if (group == null) {
			return null;
		}
		String type = group.getType();
		if (StringUtil.isEmpty(type)) {
			return null;
		}

		if (types.contains(type)) {
			return Arrays.asList(getInitializedGroup(group));
		}

		//	Checking if need to get groups by type from children groups
		switch (type) {
		case GroupTypeConstants.GROUP_TYPE_CLUB:
			if (types.contains(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION) || types.contains(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_INNER)) {
				List<com.idega.user.data.bean.Group> groups = groupDAO.getChildGroups(Arrays.asList(groupId), types);
				doInitializeGroups(groups);
				return groups;
			}
			break;

		case IWMemberConstants.GROUP_TYPE_LEAGUE:
			if (types.contains(IWMemberConstants.GROUP_TYPE_CLUB) || types.contains(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION) || types.contains(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_INNER)) {
				List<com.idega.user.data.bean.Group> groups = groupDAO.getChildGroups(Arrays.asList(groupId), types);
				doInitializeGroups(groups);
				return groups;
			}

			break;

		case IWMemberConstants.GROUP_TYPE_CLUB_DIVISION:
			if (types.contains(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER)) {
				List<com.idega.user.data.bean.Group> groups = groupDAO.getChildGroups(Arrays.asList(groupId), types);
				doInitializeGroups(groups);
				return groups;
			}

		default:
			break;
		}

		//	Getting parent groups and checking types
		List<com.idega.user.data.bean.Group> groups = getGroupsWithTypesForGroup(Arrays.asList(groupId), types, full, com.idega.user.data.bean.Group.class);
		doInitializeGroups(groups);
		return groups;
	}

	@Override
	public List<com.idega.user.data.bean.Group> getGroupsWithTypesForGroup(List<Integer> groupsIds, List<String> types) {
		return getGroupsWithTypesForGroup(groupsIds, types, true, com.idega.user.data.bean.Group.class);
	}

	@Override
	public List<Group> getActiveMembersGroupsForClubDivision(Group clubDivision) {
		if (clubDivision == null) {
			return null;
		}

		try {
			List<com.idega.user.data.bean.Group> playersGroups = getGroupsWithTypesForGroup(
					Integer.valueOf(clubDivision.getId()),
					Arrays.asList(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER),
					false
			);
			if (ListUtil.isEmpty(playersGroups)) {
				return null;
			}

			List<Group> results = new ArrayList<>();
			for (com.idega.user.data.bean.Group playersGroup: playersGroups) {
				results.add(getGroupBusiness().getGroupByGroupID(playersGroup.getID()));
			}

			return results;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting groups with type " + IWMemberConstants.GROUP_TYPE_CLUB_PLAYER + " for " + clubDivision, e);
		}

		return null;
	}

	private <T extends Serializable> List<T> getGroupsWithTypesForGroup(List<Integer> groupsIds, List<String> types, Boolean full, Class<T> resultType) {
		long start = System.currentTimeMillis();

		try {
			if (ListUtil.isEmpty(groupsIds) || ListUtil.isEmpty(types)) {
				return null;
			}

			List<T> results = new ArrayList<>();
			boolean entities = resultType.getName().equals(com.idega.user.data.bean.Group.class.getName());

			GroupDAO groupDAO = ELUtil.getInstance().getBean(GroupDAO.class);
			List<Integer> ids = groupDAO.getParentGroupsIdsRecursive(groupsIds, types);

			if (ListUtil.isEmpty(ids)) {
				if (full != null && full.booleanValue()) {
					for (Integer id: groupsIds) {
						@SuppressWarnings("unchecked")
						List<T> tmp = entities ?
								(List<T>) groupDAO.getChildGroups(Arrays.asList(id), types):
								(List<T>) groupDAO.getChildGroupIds(Arrays.asList(id), types);

						if (!ListUtil.isEmpty(tmp)) {
							results.addAll(tmp);
						}
					}
				}
			} else {
				@SuppressWarnings("unchecked")
				List<T> tmp = entities ?
						(List<T>) groupDAO.findGroups(ids) :
						(List<T>) ids;
				results = tmp;
			}

			if (ListUtil.isEmpty(results)) {
				List<com.idega.user.data.bean.Group> groups = groupDAO.findGroups(groupsIds);
				if (!ListUtil.isEmpty(groups)) {
					for (com.idega.user.data.bean.Group group: groups) {
						try {
							if (group == null) {
								continue;
							}

							if (types.contains(group.getType())) {
								@SuppressWarnings("unchecked")
								T result = entities ? (T) group : (T) group.getID();
								results.add(result);
							}
						} catch (Exception e) {
							getLogger().log(Level.WARNING, "Error resovling if group (" + group + ") has one of a type from " + types, e);
						}
					}
				}
			}

			if (ListUtil.isEmpty(results)) {
				getLogger().info("Did not find any parent groups with types " + types + " for groups with IDs " + groupsIds);
				return null;
			}

			return results;
		} finally {
			CoreUtil.doDebug(start, System.currentTimeMillis(), getClass().getSimpleName() + ".getGroupsWithTypesForGroup: groups IDs: " +
					groupsIds + ", types: " + types + ", full: " + full + ", result type: " + resultType.getName());
		}
	}

	/**
	 * @param targetGroup
	 * @throws NoClubFoundException
	 * @throws RemoteException
	 */
	@Override
	public String getClubNumberForGroup(Group group) throws NoClubFoundException, RemoteException {
		Group club = getClubForGroup(group);
		return club.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
	}

	@Override
	public String getClubNumberForGroup(com.idega.user.data.bean.Group group) throws NoClubFoundException, RemoteException {
		if (group == null) {
			return null;
		}

		com.idega.user.data.bean.Group club = getClubForGroup(group.getID());
		return club == null ? null : club.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER);
	}

	/*
		* Returns the division that is a parent for this group.
	 */
	@Override
	public Group getDivisionForGroup(Group group) throws NoDivisionFoundException, RemoteException{
		Collection<Group> parents = getGroupBusiness().getParentGroupsRecursive(group);

		if(parents!=null && !parents.isEmpty()){
			Iterator<Group> iter = parents.iterator();
			while (iter.hasNext()) {
				Group parentGroup = iter.next();
				if(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(parentGroup.getGroupType()) || IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_INNER.equals(parentGroup.getGroupType())){
					return parentGroup;//there should only be one
				}
			}
		}

		//if no division is found we throw the exception
		throw new NoDivisionFoundException(group.getName());
	}

	@Override
	public com.idega.user.data.bean.Group getDivisionForGroup(Integer groupId) throws NoDivisionFoundException {
		return getDivisionForGroup(groupId, true);
	}

	@Override
	public com.idega.user.data.bean.Group getDivisionForGroup(Integer groupId, Boolean full) throws NoDivisionFoundException {
		com.idega.user.data.bean.Group group = getGroupWithTypeForGroup(groupId, IWMemberConstants.GROUP_TYPE_CLUB_DIVISION, full);
		if (group == null) {
			group = getGroupWithTypeForGroup(groupId, IWMemberConstants.GROUP_TYPE_CLUB_DIVISION_INNER, full);
		}
		if (group == null) {
			throw new NoDivisionFoundException(groupId == null ? CoreConstants.EMPTY : String.valueOf(groupId));
		}

		return group;
	}

	@Override
	public List<Group> getDivisionsForClub(Group club) throws NoDivisionFoundException, RemoteException {
		Collection<Group> children = club.getChildren();

		if (children != null && !children.isEmpty()) {
			List<Group> divisions = new ArrayList<>();

			Iterator<Group> it = children.iterator();
			while (it.hasNext()) {
				Group child = it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION)) {
					divisions.add(child);
				}
			}

			return divisions;
		}

		//if no club is found we throw the exception
		throw new NoDivisionFoundException(club.getName());
	}

	/**
	 * A method to find the first Division for a club.
	 * @param club
	 * @return
	 */
	@Override
	public Group getDivisionForClub(Group club) throws NoDivisionFoundException, RemoteException {
		List<Group> divisions = getDivisionsForClub(club);
		if (!ListUtil.isEmpty(divisions)) {
			return divisions.iterator().next();
		}

		//if no club is found we throw the exception
		throw new NoDivisionFoundException(club.getName());
	}

	/**
	 * A method to find the club collection group for a league
	 * @param league
	 * @return
	 * @throws NoLeagueClubCollectionGroup
	 */
	@Override
	public Group getClubCollectionGroupForLeague(Group league) throws RemoteException, NoLeagueClubCollectionGroup {
		Collection<Group> children = league.getChildren();

		if (children != null && !children.isEmpty()) {
			Iterator<Group> it = children.iterator();
			while (it.hasNext()) {
				Group child = it.next();
				if (child.getGroupType().equals(IWMemberConstants.GROUP_TYPE_LEAGUE_CLUB_COLLECTION)) {
					return child;
				}
			}
		}

		throw new NoLeagueClubCollectionGroup(league.getName());
	}


	@Override
	public String getClubMemberNumberForUser(User user, Group club) throws RemoteException{
		String id = user.getMetaData(IWMemberConstants.META_DATA_USER_CLUB_MEMBER_NUMBER_PREFIX+club.getPrimaryKey().toString());
		if(id!=null){
			return id;
		}else{
			return null;
		}
	}

	/**
	 * Checks the metadata (IWMemberConstants.META_DATA_CLUB_USING_SYSTEM) of a group to see if is using the member system
	 * @param group
	 * @return a boolean
	 */
	@Override
	public boolean isClubUsingTheMemberSystem(Group group) throws RemoteException{
		String using = group.getMetaData(IWMemberConstants.META_DATA_CLUB_USING_SYSTEM);
		return ( using != null && "TRUE".equals(using.toUpperCase()));
	}

	/**
	 * @return false if number is already taken, else true
	 */
	@Override
	public synchronized boolean setClubMemberNumberForUser(String number, User user, Group club) throws RemoteException{

		boolean setNumber = false;
		String clubId = club.getPrimaryKey().toString();

		try {
			Collection<User> users = getUserHome().findUsersByMetaData(IWMemberConstants.META_DATA_USER_CLUB_MEMBER_NUMBER_PREFIX+clubId,number);
			Iterator<User> iter = users.iterator();

			while (iter.hasNext()) {
				User thingy = iter.next();
				if(thingy.equals(user)){
					setNumber = true;//updating
				}
				break;//only one user should have this number
			}
		}
		catch (EJBException e) {
			e.printStackTrace();
			return false;
		}
		catch (FinderException e) {
			setNumber = true;
		}

		if(setNumber){
			user.setMetaData(IWMemberConstants.META_DATA_USER_CLUB_MEMBER_NUMBER_PREFIX+clubId,number);
			user.store();
		}

		return true;
	}

}