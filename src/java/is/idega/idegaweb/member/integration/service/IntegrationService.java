package is.idega.idegaweb.member.integration.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import is.idega.idegaweb.member.integration.bean.MemberChangeRequest;

public interface IntegrationService {

	public static final String	PATH = "/api/integration",
								MEMBER_SYNC = "/member";

	public Response doSyncMember(String apiKey, HttpServletRequest request, MemberChangeRequest member);

}