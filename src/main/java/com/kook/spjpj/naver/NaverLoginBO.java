package com.kook.spjpj.naver;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

public class NaverLoginBO {
	// ���� ��û���� �����ϴ� �Ķ����
	// client_id: ���ø����̼� ��� �� �߱޹��� Ŭ���̾�Ʈ ���̵�
	// response_type: ���� ������ ���� ���а�. code�� ���� ������ �ִ�.
	// ���̹� �α��� ������ ����� ���޹��� �ݹ� URL(URL ���ڵ�) ���ø����̼��� ����� �� Callback URL�� ������ �����Դϴ�.
	// state: ���ø����̼��� ������ ���� ��ū (���̹������ڻ���Ʈ���� ��Ͻ� �־��� ��)
	
	private final static String CLIENT_ID = "H9zNwQQVbPK4owU8_9iM";
	private final static String CLIENT_SECRET = "3fWU8QJ8ht";
	private final static String REDIRECT_URI = "https://localhost:8443/spjpj/nredirect";
	private final static String SESSION_STATE = "oauth_state";
	// ������ ��ȸ API URL
	private final static String PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";
	// ���̹� ���̵�� ���� URL ���� Method
	public String getAuthorizationUrl(HttpSession session) {
		// ���� ��ȿ�� ������ ���Ͽ� ������ ����
		String state = generateRandomString();
		setSession(session, state);
		// Scribe���� �����ϴ� ���� URL ���� ����� �̿��Ͽ� ���̹� ���� URL ����
		OAuth20Service oauthService = new ServiceBuilder()
				.apiKey(CLIENT_ID)
				.apiSecret(CLIENT_SECRET)
				.callback(REDIRECT_URI)
				.state(state)
				.build(NaverLoginApi.instance());
		return oauthService.getAuthorizationUrl();
					
	}
	
	private String generateRandomString() {
		return UUID.randomUUID().toString(); // UUID�� ���ϰ��� �����ϴ� Ŭ����
	}
	
	// http session�� ������ ����
	private void setSession(HttpSession session, String state) {
		session.setAttribute(SESSION_STATE, state);
	}

	// ���̹� ���̵�� Callback ó�� �� AccessToken ȹ�� Method
	public OAuth2AccessToken getAccessToken(HttpSession session, String code, String state) throws IOException {
		// Callback���� ���޹��� ���ǰ����� �������� ���ǿ� ����Ǿ� �ִ� ���� ��ġ�ϴ��� Ȯ�� 
		String sessionState = getSession(session);
		if(StringUtils.pathEquals(sessionState, state)) {
			OAuth20Service oauthService = new ServiceBuilder()
					.apiKey(CLIENT_ID)
					.apiSecret(CLIENT_SECRET)
					.callback(REDIRECT_URI)
					.state(state)
					.build(NaverLoginApi.instance());
			// Scribe���� �����ϴ� AccessToken ȹ�� ������� ���̹� Access Token�� ȹ��
			OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
			return accessToken;
		}
		return null;
	}
	// http session�� ������ ��ȯ
	private String getSession(HttpSession session) {
		return (String)session.getAttribute(SESSION_STATE);
	}
	
	//Access Token�� �̿��Ͽ� ���̹� ����� ������ API�� ȣ��
		public String getUserProfile(OAuth2AccessToken oauthToken) throws IOException {
			OAuth20Service oauthService =new ServiceBuilder()
				.apiKey(CLIENT_ID)
		        .apiSecret(CLIENT_SECRET)
		        .callback(REDIRECT_URI)
		        .build(NaverLoginApi.instance());
			OAuthRequest request = new OAuthRequest(Verb.GET, PROFILE_API_URL, oauthService);
			oauthService.signRequest(oauthToken, request);
			Response response = request.send();
		    return response.getBody();
	}
}