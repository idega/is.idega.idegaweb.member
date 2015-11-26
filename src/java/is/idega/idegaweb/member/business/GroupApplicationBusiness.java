package is.idega.idegaweb.member.business;

import java.util.Collection;

import com.idega.user.data.Group;
import com.idega.user.data.User;

import is.idega.idegaweb.member.data.GroupApplication;


public interface GroupApplicationBusiness extends com.idega.business.IBOService,com.idega.user.business.UserGroupPlugInBusiness
{
 public com.idega.user.business.GroupBusiness getGroupBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 @Override
public com.idega.presentation.PresentationObject instanciateViewer(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getDeniedStatusString()throws java.rmi.RemoteException, java.rmi.RemoteException;
 @Override
public com.idega.presentation.PresentationObject instanciateEditor(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 @Override
public void beforeUserRemove(com.idega.user.data.User p0, Group parentGroup)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 @Override
public void afterGroupCreateOrUpdate(com.idega.user.data.Group p0, Group parentGroup)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getApprovedStatusString()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean changeGroupApplicationStatus(is.idega.idegaweb.member.data.GroupApplication p0,java.lang.String p1) throws java.rmi.RemoteException;
 public java.util.Collection<GroupApplication> getGroupApplicationsByStatusAndUserOrderedByCreationDate(java.lang.String p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public com.idega.core.location.business.AddressBusiness getAddressBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 @Override
public void beforeGroupRemove(com.idega.user.data.Group p0, Group parentGroup)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 @Override
public java.util.List getUserPropertiesTabs(com.idega.user.data.User p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.data.GroupApplication createGroupApplication(com.idega.user.data.Group p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,java.lang.String p11, java.lang.String[] p12)throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.CreateException,com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 @Override
public void afterUserCreateOrUpdate(com.idega.user.data.User p0, Group parentGroup)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection<GroupApplication> getGroupApplicationsByStatusAndApplicationGroup(java.lang.String p0,com.idega.user.data.Group p1) throws java.rmi.RemoteException;
 public is.idega.idegaweb.member.data.GroupApplicationHome getGroupApplicationHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 @Override
public java.util.List getGroupPropertiesTabs(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getPendingStatusString()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean changeGroupApplicationStatus(int p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.user.business.UserBusiness getUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.member.data.GroupApplication createGroupApplication(com.idega.user.data.Group p0,com.idega.user.data.User p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.util.List p5)throws java.rmi.RemoteException,javax.ejb.CreateException,com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public boolean changeGroupApplicationAdminCommentAndGroups(is.idega.idegaweb.member.data.GroupApplication app, String adminComment, String[] groupIds) throws java.rmi.RemoteException;
 public Collection getGroupApplicationsByStatusAndUserAndApplicationGroupOrderedByCreationDate(String status, User user, Group applicationGroup);
 public Collection<GroupApplication> getGroupApplicationsByUserAndApplicationGroupOrderedByCreationDate(User user, Group applicationGroup);

}
