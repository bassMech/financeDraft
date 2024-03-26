package de.bassmech.findra.web.auth;

import java.time.Instant;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.bassmech.findra.web.service.AccountService;
import de.bassmech.findra.web.util.FacesMessageUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Named
@SessionScoped
public class SessionHandler {
	protected Logger logger = LoggerFactory.getLogger(SessionHandler.class);

	@Inject
	MemberRepository memberRepository;

	@Inject
	private HttpSession session;

	private String memberUuid;

	private String logoutMessage;

	@PostConstruct
	public void init() {

	}

	@Transactional
	public boolean create(String mail, String passwordPlain) throws LoginException {
		Member tempMember = memberRepository.findByMail(mail);
		if (tempMember == null) {
			throw new LoginException(LoginErrorCode.LOGIN_001, "Mail: %s", mail);
		}

		if (tempMember.getDeleted() != null || tempMember.getSuspended() != null) {
			throw new LoginException(LoginErrorCode.LOGIN_005, "Member: %s", tempMember.getUuid());
		}

		boolean loginCorrect = CryptUtil.areCredentialsCorrect(tempMember, passwordPlain);
		Instant now = Instant.now();

		memberUuid = tempMember.getUuid();
		tempMember.setLastLogin(now);
		tempMember.setLastUpdate(now);
		tempMember.setSession(session.getId());

		session.setAttribute("memberUuid", tempMember.getUuid());

		memberRepository.persistAndFlush(tempMember);
		// timeOut = now.plus(timeoutValue, ChronoUnit.MINUTES);

		logger.debug("Login successful");
		return true;
	}

	@Transactional
	public Member getLoggedInMemberWithSessionCheck() throws MemberFetchException {
		Member member = memberRepository.findByUuid(memberUuid);
		if (member == null) {
			throw new MemberFetchException(MemberFetchErrorCode.MEMBER_001, "uuid", memberUuid);
		}

		// TODO unify member check
		if (!member.getSession().equals(session.getId())) {
			throw new MemberFetchException(MemberFetchErrorCode.MEMBER_002, "uuid", memberUuid);
		}
		
		if (!member.getSession().equals(session.getId())) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, FacesMessageUtil.getFacesMessage(FacesMessage.SEVERITY_ERROR, "error",
					"error.member.not.found.or.wrong.password", facesContext.getViewRoot().getLocale()));
			destroy();
			return member;
		}

		return member;

	}

	public boolean isLoggedIn() throws MemberFetchException {
		if (memberUuid == null) {
			return false;
		}
		return getLoggedInMemberWithSessionCheck() != null;

//		if (timeOut.isAfter(Instant.now())) {
//			Member tempMember = memberRepository.findByUuid(memberUuid);
//			if (!tempMember.getSession().equals(session.getId())) {
////				refreshTimeout();
////			} else {
//				destroy();
//				return false;
//			}
//		}

	}

//	private void refreshTimeout() {
//		Instant now = Instant.now();
//		timeOut = now.plus(timeoutValue, ChronoUnit.MINUTES);
//		member.setLastUpdate(now);
//		memberRepository.persistAndFlush(member);
//	}

	@Transactional
	public void destroy() {
		if (memberUuid != null) {
			Member tempMember = memberRepository.findByUuid(memberUuid);
			tempMember.setLastUpdate(Instant.now());
			tempMember.setSession(null);
			// memberRepository.persistAndFlush(tempMember);
		}
		memberUuid = null;
		logoutMessage = null;
		session.invalidate();
		logger.debug("Session destroyed");
	}

	public String getAndClearLogoutMessage() {
		String temp = logoutMessage;
		if (temp == null) {
			temp = "";
		}

		logoutMessage = null;
		return temp;
	}

	public void setLogoutMessage(String logoutMessage) {
		this.logoutMessage = logoutMessage;
	}

	public String getMemberUuid() {
		return memberUuid;
	}

}
