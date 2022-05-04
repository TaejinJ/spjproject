package com.kook.spjpj.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.kook.spjpj.command.BContentCommand;
import com.kook.spjpj.command.BDeleteCommand;
import com.kook.spjpj.command.BListCommand;
import com.kook.spjpj.command.BPageListCommand;
import com.kook.spjpj.command.FBModifyCommand;
import com.kook.spjpj.command.FBWriteCommand;
import com.kook.spjpj.command.FBoardReplyCommand;
import com.kook.spjpj.command.FBoardReplyViewCommand;
import com.kook.spjpj.command.ICommand;
import com.kook.spjpj.command.JoinCommand;
import com.kook.spjpj.command.QBWriteCommand;
import com.kook.spjpj.command.QBpageListCommand;
import com.kook.spjpj.command.QContentCommand;
import com.kook.spjpj.command.QDeleteCommand;
import com.kook.spjpj.command.QListCommand;
import com.kook.spjpj.command.QModifyCommand;
import com.kook.spjpj.command.RBContentCommand;
import com.kook.spjpj.command.RBPageListCommand;
import com.kook.spjpj.command.RBWriteCommand;
import com.kook.spjpj.command.RDeleteCommand;
import com.kook.spjpj.command.RListCommend;
import com.kook.spjpj.command.RecordCommand;
import com.kook.spjpj.command.WageBillCommand;
import com.kook.spjpj.dao.Dao;
import com.kook.spjpj.naver.NaverLoginBO;
import com.kook.spjpj.util.Constant;



@Controller
public class SPJController {

//	bean �����Ͽ� Constant�� ����
	private Dao dao;

	@Autowired
	public void setDao(Dao dao) {
		this.dao = dao;
		Constant.dao = dao;
	}

	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		Constant.passwordEncoder = passwordEncoder;
	}
	
	
	/* GoogleLogin */
	@Autowired
	private GoogleConnectionFactory googleConnectionFactory;
	
	@Autowired
	private OAuth2Parameters googleOAuth2Parameters;
	
	private ICommand com; // command�� �������̽� ��ü�� �����Ͽ� �������� ���� ���� Ŭ���� ��ü�� ���� ���

	   @RequestMapping("/plist")
	   public String plist(HttpServletRequest request,HttpServletResponse response,Model model) {
	      System.out.println("plist");
	      System.out.println(request.getParameter("pageNo"));
	      com = new BPageListCommand();
	      com.execute(model,  request);
	      return "plist";
	   }
	
	
	// NaverLoginBO
	private NaverLoginBO naverLoginBO;
	@Autowired 
	private void setNaverLoginBO (NaverLoginBO naverLoginBO) {
		this.naverLoginBO = naverLoginBO;
	}
	

// ===== ȸ������ ȭ�鿡 �Է��� ������ command�� ���ؼ� ó�� 
	@RequestMapping(value = "/join", produces = "application/text; charset=UTF8")
	@ResponseBody // jsp�� �ƴ� ���ڿ��̳� ��ü�� ������ ������ �ʿ�, ����: https://memostack.tistory.com/243
	public String join(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("join"); // consoleâ ���ؼ� ���࿩�� Ȯ�ο� �ڵ�

		com = new JoinCommand(); // join��û�� ���� commandŬ������ MiniCommand�������̽� ���� Ŭ����
		com.execute(model, request);
		String result = (String) request.getAttribute("result"); // JoinCommand���� ������ ������� String result�� �Է�
		System.out.println(result); // consoleâ ���ؼ� ���࿩�� Ȯ�ο� �ڵ�
		if (result.equals("success"))
			return "join-success";
		else
			return "join-failed";
	}

	
//	===== �α��� ȭ�� =====
	@RequestMapping("/spj_login")
	public String spj_login(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Model model) {
		System.out.println("login");
		//social login url���ϱ� �޼��� ȣ��
		socialUrl(model, session);
		return "spj_login";
	}

	@RequestMapping(value = "/processLogin", method = RequestMethod.GET)
	public ModelAndView processLogin(
			@RequestParam(value = "log", required = false) String log,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			HttpSession session, Model pmodel
			) {
		System.out.println("processLogin");
		ModelAndView model = new ModelAndView();		
		if(log != null && log !="")  { 
			// security form�� �ƴ� ��(a href="processLogin?log=1)���� �α��� â ��û��
			model.addObject("log", "before login!");
		}
		if (error != null && error != "") { //�α��ν� �����߻��ϸ� security���� ��û(���� 1)
			model.addObject("error", "Invalid username or password!");
		} 
		if (logout != null && logout != "") { //�α׾ƿ� ������ security���� ��û(���� 1)
			model.addObject("logout", "You've been logged out successfully.");
		}
		socialUrl(pmodel, session);
		model.setViewName("spj_login");	
		return model;
	}
	
	@RequestMapping("/Login")
	public String Login(HttpServletRequest request, HttpServletResponse response, Model model, HttpSession session){
		System.out.println("Login");
		socialUrl(model, session);
		return "spj_login";
	}	
	
	//https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=sonmit002&logNo=221344583488
		@RequestMapping(value="/redirect",produces = "application/text; charset=UTF8")
		//���ۿ��� ��û�ϴ� ���
		public String googleCallback(Model model,@RequestParam String code,
				HttpServletResponse response) throws IOException {
			System.out.println("����� googleCallback");
			OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
			AccessGrant accessGrant =   //access tokenó����ü
			oauthOperations.exchangeForAccess(code, googleOAuth2Parameters.getRedirectUri(), null);
			String accessToken = accessGrant.getAccessToken();
			getGoogleUserInfo(accessToken,response);
			return "spj_socialLogin";
		}
		
		@RequestMapping(value="/kredirect",produces = "application/json; charset=UTF8")
		public String kredirect(@RequestParam String code, HttpServletResponse response, 
				Model model) throws Exception {
			System.out.println("#########" + code);
			String access_Token = getKakaoAccessToken(code,response);
			System.out.println("###access_Token#### : " + access_Token);
			//�� access_Token�� �̿��Ͽ� kakao�� ����� ������ ��
			HashMap<String, Object> userInfo = getKakaoUserInfo(access_Token);
			return "spj_socialLogin";
		}
		
		@RequestMapping("/nredirect")
		public ModelAndView callback(@RequestParam String code, @RequestParam String state,
				HttpSession session) throws Exception {
			System.out.println("state: " +state);
			OAuth2AccessToken oauthToken = naverLoginBO.getAccessToken(session, code, state);
			String apiResult = naverLoginBO.getUserProfile(oauthToken);
			System.out.println(apiResult);
			// String ������ apiResult�� json ���·� �ٲ�
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(apiResult);
			JSONObject jsonObj = (JSONObject) obj;
			JSONObject response_obj = (JSONObject)jsonObj.get("response");
			System.out.println("naver user ����:"  + response_obj);
			// response�� nickname�� �Ľ�
			String name = (String)response_obj.get("name");
			System.out.println("name:" +name);
			return new ModelAndView("spj_socialLogin", "result", apiResult);
			// addObject�� setViewName�� �ѹ��� ����ϴ� ������� ������ ��� ù��° �Ķ���ʹ� view jsp �̸�,
			// �ι�°�� �Ӽ���, ����°�� �Ӽ���
		}
		
		//social�޼���
		public void socialUrl(Model model, HttpSession session) {
			/* ����code ���� */
			OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
			//OAuth2�� ó�����ϴ� ��ü
			String url = 
			oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
			//GrantType�� Oauth2ó�� ��� AUTHORIZATION_CODE�� �������̵� ����,googleOAuth2Parameters��
			//�� ������ scope�� redirect������ ���� ��ü
			System.out.println("����:" + url);
			//model�� �����Ͽ� ���� login_view.jsp�� ������ ��
			model.addAttribute("google_url", url);	
			
			
			/*kakao code  kakao developer�������� ���� ���*/
			//kakao https://tyrannocoding.tistory.com/61
			String kakao_url  = 
					"https://kauth.kakao.com/oauth/authorize"
					+ "?client_id=9f317d9f39587377b26054a0d805ba58"
					+ "&redirect_uri=https://localhost:8443/spjpj/kredirect"
					+ "&response_type=code";
			model.addAttribute("kakao_url", kakao_url );
			
			// naver social login ���
			// ���̹����̵�� ���� URL�� �����ϱ� ���Ͽ� NaverLoginBOŬ������ getAuthorizationUrl�޼ҵ� ȣ��
			String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
			System.out.println("���̹�" +naverAuthUrl);
			model.addAttribute("naver_url", naverAuthUrl);	
		}
		
		//���ۻ�������� ��� �޼���
		public void getGoogleUserInfo(String access_Token,HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8"); 
			HashMap<String,Object> googleUserInfo = new HashMap<String,Object>();
			//1.7�������� ����϶�� ���� ������ change �ϰ� Facet���� java 1.7�� ����
			//�Ǵ� �����ڿ��� ���׸� ���
			String reqURL = "https://www.googleapis.com/userinfo/v2/me?access_token="+access_Token;
			try {
				URL url = new URL(reqURL); 
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Authorization", "Bearer " + access_Token);
				int responseCode = conn.getResponseCode(); 
				System.out.println("responseCode : "+responseCode);
				if(responseCode == 200) { //200�� ���� ����
					BufferedReader br = 
						new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8")); 
					String line = ""; 
					String result = "";
					while ((line = br.readLine()) != null) {
						result += line;
					}
					JSONParser parser = new JSONParser(); //���ڿ��� json��üȭ�ϴ� ��ü
					Object obj = parser.parse(result);
					JSONObject jsonObj = (JSONObject) obj;
					String name_obj = (String)jsonObj.get("name");
					String email_obj = (String)jsonObj.get("email");
					String id_obj = "GOOGLE_" + (String)jsonObj.get("id");
					
					googleUserInfo.put("name", name_obj); 
					googleUserInfo.put("email", email_obj); 
					googleUserInfo.put("id", id_obj);
					
					System.out.println("googleUserInfo : " + googleUserInfo);	
					
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		//kakao access-token �޼���
		public String getKakaoAccessToken (String authorize_code,HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8"); 
			String access_Token = "";
			String refresh_Token = "";
			String reqURL = "https://kauth.kakao.com/oauth/token";
			try {
				URL url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				// URL������ ����¿� ��� �� �� �ְ�, POST Ȥ�� PUT ��û�� �Ϸ��� setDoOutput�� true�� �����ؾ���.
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				//kakao�� �������ִ� ��
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
				StringBuilder sb = new StringBuilder();
				sb.append("grant_type=authorization_code");
				sb.append("&client_id=9f317d9f39587377b26054a0d805ba58");  //������ �߱޹��� key
				sb.append("&redirect_uri=https://localhost:8443/spjpj/kredirect");
				// ������ ������ ���� ���
				sb.append("&code=" + authorize_code);
				bw.write(sb.toString());
				bw.flush();
				//��� �ڵ尡 200�̶�� ����
				int responseCode = conn.getResponseCode();
	            System.out.println("responseCode : " + responseCode);
	            // ��û�� ���� ���� JSONŸ���� Response �޼��� �о����
	            BufferedReader br = 
	            	new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8")); 
	            String line = "";
	            String result = "";
	            while ((line = br.readLine()) != null) {
	            	result += line;
	            }
	            System.out.println("response body : " + result);
	            JSONParser parser = new JSONParser();
	            Object obj = parser.parse(result); //parse�޼���� Object��ȯ
	            JSONObject jsonObj = (JSONObject) obj;
	            access_Token = (String)jsonObj.get("access_token");
				refresh_Token = (String)jsonObj.get("refresh_token");
				System.out.println("access_token : " + access_Token);
		        System.out.println("refresh_token : " + refresh_Token);
		        //io��ü�� close
		        br.close();
	            bw.close();
			}
			catch(Exception e) {
				e.getMessage();
			}
			return access_Token;
		}
		
		//kakao access-token ���� ����� ���� ���
		public HashMap<String,Object> getKakaoUserInfo (String access_Token) {
			HashMap<String, Object> userInfo = new HashMap<String, Object>();
			String reqURL = "https://kapi.kakao.com/v2/user/me";
			try {
				URL url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				//��û�� �ʿ��� Header�� ���Ե� ����
				conn.setRequestProperty("Authorization", "Bearer " + access_Token);
				int responseCode = conn.getResponseCode(); //200�̸� ����
				System.out.println("responseCode : " + responseCode);
				BufferedReader br = 
						new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				String line = "";
		        String result = "";
		        while ((line = br.readLine()) != null) {
	                result += line;
	            }
		        System.out.println("response body : " + result);
		        
		        JSONParser parser = new JSONParser();
		        Object obj = parser.parse(result);
		        JSONObject jsonObj = (JSONObject) obj;	       
		        JSONObject properties = (JSONObject) jsonObj.get("properties");
		        JSONObject kakao_account = (JSONObject) jsonObj.get("kakao_account"); //�˼��Ŀ� ����
		       
		        String accessToken = (String)properties.get("access_token");
		        String nickname = (String)properties.get("nickname");
		        String email = (String)kakao_account.get("email"); //�˼��Ŀ� ����
		        userInfo.put("accessToken", access_Token);
	            userInfo.put("nickname", nickname);
	            userInfo.put("email", email);
	            System.out.println("=============");
	            System.out.println("nickname  " + nickname);
	            System.out.println("email  " + email);
	            System.out.println("=============");
			}
			catch(Exception e) {
				e.getMessage();
			}
			return userInfo;
		}
		
	//=======logout========
	@RequestMapping("/spj_logout") 
	public String spj_logout() {
		System.out.println("logout");
		return "spj_logout";
	}
	
	@RequestMapping("/main") 
	public String main(HttpServletRequest request,HttpServletResponse response,
			Model model,Authentication authentication) {
		System.out.println("main");
		
		// �α��� ������ �������̹Ƿ� id�� role������ ���� ���
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Constant.username = userDetails.getUsername();
		System.out.println(userDetails.getUsername());//hsk5410@naver.com (�α����� ���̵�)
		Collection<? extends GrantedAuthority>  authorities = authentication.getAuthorities();
		String auth = authorities.toString(); //role�� �� ���ڿ��� ��ȯ
		System.out.println(auth); //[ROLE_USER]����
		return "spj_main";
	}
	
//	======= �����Խ��� ȭ�� ======	
	@RequestMapping("/spj_FreeboardList")
	public String spj_FreeboardList(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("/spj_FreeboardList");	
		com = new BListCommand();
		com.execute(model, request);
		return "spj_FreeboardList";
	}
	
	@RequestMapping("/spj_freeboardWrite")  //ajax�� ��û�� @ResponseBody������ jsp��ȯ
	public String spj_freeboardWrite(Model model) {
		System.out.println("spj_freeboardWrite()");
		model.addAttribute("bNic",Constant.username);	//bnic? bname?
		return "spj_freeboardWrite";
	}
	
	@RequestMapping("/write")
	public String write(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("write");
		com = new FBWriteCommand();
		com.execute(model, request);
		return "redirect:spj_FreeboardList";
	}
	
	//�Խñ� ����
	@RequestMapping("/spj_freeboardView")
	public String content_view(HttpServletRequest request, HttpServletResponse response, Model model) {
		com = new BContentCommand();
		com.execute(model, request);
		model.addAttribute("user",Constant.username);
		if(model.containsAttribute("content_view")) {
			String result = "success";
			System.out.println(result);
		}
		return "spj_freeboardView"; 
	}
	
	//�Խñ� ����
	@RequestMapping("/spj_freeboardModify")
	public String modify_view(HttpServletRequest request, HttpServletResponse response, Model model) {
		com = new BContentCommand();
		com.execute(model, request);
		model.addAttribute("user",Constant.username);
		if(model.containsAttribute("content_view")) {
			String result = "success";
			System.out.println(result);
		}
		return "spj_freeboardModify"; 
	}
	
	@RequestMapping("/modify")
	public String modify(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("modify()");
		com = new FBModifyCommand();
		com.execute(model,request);
		return "redirect:spj_FreeboardList";
	}
	
	@RequestMapping("/delete")
	public String delete(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("delete()");
		com = new BDeleteCommand();
		com.execute(model, request);
		
		return "redirect:spj_FreeboardList";
	}
	// ��۴ޱ�
	@RequestMapping("/reply_view") //ajax
	public String reply_view(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("reply_view()");		
		com = new FBoardReplyViewCommand();
		com.execute(model,request); //������ �Խ��� ��Ͽ� ���� ������ �����ͼ� ���â�� ���
		request.setAttribute("user",Constant.username);
		//model�� �ΰ��� �Ӽ� �ټ���������  request�� ��
		return "reply_view";
	}
	
	
	@RequestMapping("/reply") 
	public String reply(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("reply()");		
		com = new FBoardReplyCommand();
		com.execute(model,request);		
		return "redirect:spj_FreeboardList";
	}
	

	
//	======== reviewboard ȭ�� =====
	@RequestMapping("/spj_reviewboardList")
	public String spj_reviewboardList(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("/spj_reviewboardList");
		com = new RListCommend();
		com.execute(model, request);
		return "spj_reviewboardList";
	}

	
	
	@RequestMapping("/spj_reviewboardWrite")  //ajax�� ��û�� @ResponseBody������ jsp��ȯ
	public String spj_reviewboardWrite(Model model) {
		System.out.println("spj_reviewboardWrite()");
		model.addAttribute("bnic",Constant.username);	//bnic? bname?
		return "spj_reviewboardWrite";
	}
	
	@RequestMapping("/review_write")
	public String review_write(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("review_write");
		com = new RBWriteCommand();
		com.execute(model, request);
		model.addAttribute("bNic",Constant.username);
		return "redirect:spj_reviewboardList";
	}
	
	//�Խñ� ����
	@RequestMapping("/spj_reviewboardView")
	public String review_content_view(HttpServletRequest request, HttpServletResponse response, Model model) {
		com = new RBContentCommand();
		com.execute(model, request);
		model.addAttribute("user",Constant.username);
		if(model.containsAttribute("review_content_view")) {
			String result = "success";
			System.out.println(result);
		}
		return "spj_reviewboardView"; 
	}

	//�Խñ� ����
	@RequestMapping("/review_delete()")
	public String review_delete(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("review_delete()");
		
		com = new RDeleteCommand();
		com.execute(model, request);
		
		return "redirect:spj_FreeboardList";
	}
	
	
	//======= �����Խ��� ȭ�� ======	
			@RequestMapping("/spj_qnaboardList")
			public String spj_qnaboardList(HttpServletRequest request,HttpServletResponse response,Model model) {
				System.out.println("/spj_qnaboardList");	
				com = new QListCommand();
				com.execute(model, request);
				
				return "spj_qnaboardList";
			}
			
			@RequestMapping("/spj_QnAboardWrite")  //ajax�� ��û�� @ResponseBody������ jsp��ȯ
			public String spj_qnaboardWrite(Model model) {
				System.out.println("spj_qnaboardWrite()");
				model.addAttribute("qNic",Constant.username);	//bnic? bname?
				return "spj_QnAboardWrite";
			}
			
			@RequestMapping("/qwrite")
			public String qwrite(HttpServletRequest request,HttpServletResponse response,Model model) {
				System.out.println("qwrite");
				com = new QBWriteCommand();
				com.execute(model, request);
				return "redirect:spj_qnaboardList";
			}
			
			
			
			@RequestMapping("/qmodify")
			public String qmodify(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
				System.out.println("qmodify()");
				com = new QModifyCommand();
				com.execute(model,request);
				return "redirect:spj_qnaboardList";
			}
			//�Խñ� ����
			@RequestMapping("/spj_qnaboardModify")
			public String qmodify_view(HttpServletRequest request, HttpServletResponse response, Model model) {
				com = new QContentCommand();
				com.execute(model, request);
				model.addAttribute("user",Constant.username);
				if(model.containsAttribute("qcontent_view")) {
					String result = "success";
					System.out.println(result);
				}
				return "spj_qnaboardModify"; 
			}
			
			@RequestMapping("/spj_QnaboardView")
			public String qcontent_view(HttpServletRequest request, HttpServletResponse response, Model model) {
				com = new QContentCommand();
				com.execute(model, request);
				model.addAttribute("user",Constant.username);
				if(model.containsAttribute("qcontent_view")) {
					String result = "success";
					System.out.println(result);
				}
				return "spj_QnaboardView"; 
			}
			@RequestMapping("/qdelete")
			public String qdelete(HttpServletRequest request,HttpServletResponse response,Model model) {
				System.out.println("qdelete()");
				
				com = new QDeleteCommand();
				com.execute(model, request);
				
				return "redirect:spj_qnaboardList";
			}
			//����Խ��� ����¡
			public String review_qlist(HttpServletRequest request,HttpServletResponse response,Model model) {
				System.out.println("qplist");
				System.out.println(request.getParameter("qpageNo"));
				com = new QBpageListCommand();
				com.execute(model, request);
				return "qplist";
			}


//	======== record ===========
	@RequestMapping("/record")
	public String record(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("record");
		com = new RecordCommand();
		com.execute(model, request);
		String nic = request.getParameter("rnic");
		model.addAttribute("nic", nic);
		return "redirect:spj_wageBill"; 
	}
	
	@RequestMapping("/spj_wageBill")
	public String spj_wageBill(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("spj_wageBill");
		model.addAttribute("user", Constant.username);
		com = new WageBillCommand();
		com.execute(model, request);
		return "spj_wageBill";
	}
	
	@RequestMapping("/myWageBill")
	public String myWageBill(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("myWageBill");
		model.addAttribute("user", Constant.username);
		com = new WageBillCommand();
		com.execute(model, request);
		String nic = request.getParameter("rnic");			
		return "redirect:spj_wageBill"; 
	}
	//����Խ��� ����¡
		@RequestMapping("/rplist")
		public String review_plist(HttpServletRequest request,HttpServletResponse response,Model model) {
			System.out.println("rplist");
			System.out.println(request.getParameter("rpageNo"));
			com = new RBPageListCommand();
			com.execute(model, request);
			return "rplist";
		}
	
	
//	========== menu ================
//	===== main ȭ�� =====
	@RequestMapping("/spj_main")
	public String spj_main() {
		return "spj_main";
	}	
	
//	===== about ȭ�� =====
	@RequestMapping("/spj_about")
	public String spj_about() {
		return "spj_about";
	}	
		
//  ========= �˹� ���� �ۼ� ========
	@RequestMapping("/spj_record")
	public String spj_record(Model model) {
		System.out.println("spj_record()");
		model.addAttribute("user", Constant.username);
		return "spj_record";
	}

	// ===== ȸ������ ȭ�� =====
	@RequestMapping("/spj_join")
	public String spj_join() {
		return "spj_join";
	}

	// ===== ���������� ȭ�� =====
	@RequestMapping("/spj_myPage")
	public String spj_myPage() {
		return "spj_myPage";
	}
}
