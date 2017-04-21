package is.idega.idegaweb.member.integration.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MemberChangeResponse implements Serializable {

	private static final long serialVersionUID = -5951786608453530098L;

	private Integer userId;

	private Boolean success;

	private String message;

	public MemberChangeResponse() {
		super();
	}

	public MemberChangeResponse(Boolean success, String message) {
		this();

		this.success = success;
		this.message = message;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}