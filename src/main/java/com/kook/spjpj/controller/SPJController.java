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

//	bean 주입하여 Constant에 설정
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
	
	private ICommand com; // command의 인터페이스 객체를 선언하여 다형성에 의해 구현 클래스 객체를 대입 사용

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
	

// ===== 회원가입 화면에 입력한 내용을 command를 통해서 처리 
	@RequestMapping(value = "/join", produces = "application/text; charset=UTF8")
	@ResponseBody // jsp가 아닌 문자열이나 객체을 보내기 때문에 필요, 참고: https://memostack.tistory.com/243
	public String join(HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("join"); // console창 통해서 실행여부 확인용 코드

		com = new JoinCommand(); // join요청에 따른 command클래스로 MiniCommand인터페이스 구현 클래스
		com.execute(model, request);
		String result = (String) request.getAttribute("result"); // JoinCommand에서 저장한 결과값을 String result에 입력
		System.out.println(result); // console창 통해서 실행여부 확인용 코드
		if (result.equals("success"))
			return "join-success";
		else
			return "join-failed";
	}

	
//	===== 로그인 화면 =====
	@RequestMapping("/spj_login")
	public String spj_login(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Model model) {
		System.out.println("login");
		//social login url구하기 메서드 호출
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
			// security form이 아닌 곳(a href="processLogin?log=1)에서 로그인 창 요청시
			model.addObject("log", "before login!");
		}
		if (error != null && error != "") { //로그인시 에러발생하면 security에서 요청(값은 1)
			model.addObject("error", "Invalid username or password!");
		} 
		if (logout != null && logout != "") { //로그아웃 성공시 security에서 요청(값은 1)
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
		//구글에서 요청하는 경로
		public String googleCallback(Model model,@RequestParam String code,
				HttpServletResponse response) throws IOException {
			System.out.println("여기는 googleCallback");
			OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
			AccessGrant accessGrant =   //access token처리객체
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
			//이 access_Token을 이용하여 kakao의 사용자 정보를 얻어냄
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
			// String 형식인 apiResult를 json 형태로 바꿈
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(apiResult);
			JSONObject jsonObj = (JSONObject) obj;
			JSONObject response_obj = (JSONObject)jsonObj.get("response");
			System.out.println("naver user 정보:"  + response_obj);
			// response의 nickname값 파싱
			String name = (String)response_obj.get("name");
			System.out.println("name:" +name);
			return new ModelAndView("spj_socialLogin", "result", apiResult);
			// addObject와 setViewName을 한번에 사용하는 방법으로 생성자 사용 첫번째 파라메터는 view jsp 이름,
			// 두번째는 속성명, 세번째는 속성값
		}
		
		//social메서드
		public void socialUrl(Model model, HttpSession session) {
			/* 구글code 발행 */
			OAuth2Operations oauthOperations = googleConnectionFactory.getOAuthOperations();
			//OAuth2를 처리케하는 객체
			String url = 
			oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, googleOAuth2Parameters);
			//GrantType은 Oauth2처리 방식 AUTHORIZATION_CODE는 서버사이드 인증,googleOAuth2Parameters는
			//빈에 설정된 scope와 redirect정보를 가진 객체
			System.out.println("구글:" + url);
			//model에 저장하여 리턴 login_view.jsp에 사용토록 함
			model.addAttribute("google_url", url);	
			
			
			/*kakao code  kakao developer페이지에 가서 등록*/
			//kakao https://tyrannocoding.tistory.com/61
			String kakao_url  = 
					"https://kauth.kakao.com/oauth/authorize"
					+ "?client_id=9f317d9f39587377b26054a0d805ba58"
					+ "&redirect_uri=https://localhost:8443/spjpj/kredirect"
					+ "&response_type=code";
			model.addAttribute("kakao_url", kakao_url );
			
			// naver social login 경로
			// 네이버아이디로 인증 URL을 생성하기 위하여 NaverLoginBO클래스의 getAuthorizationUrl메소드 호출
			String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
			System.out.println("네이버" +naverAuthUrl);
			model.addAttribute("naver_url", naverAuthUrl);	
		}
		
		//구글사용자정보 얻기 메서드
		public void getGoogleUserInfo(String access_Token,HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8"); 
			HashMap<String,Object> googleUserInfo = new HashMap<String,Object>();
			//1.7버젼으로 사용하라는 에러 나오면 change 하고 Facet가사 java 1.7로 수정
			//또는 생성자에도 지네릭 사용
			String reqURL = "https://www.googleapis.com/userinfo/v2/me?access_token="+access_Token;
			try {
				URL url = new URL(reqURL); 
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Authorization", "Bearer " + access_Token);
				int responseCode = conn.getResponseCode(); 
				System.out.println("responseCode : "+responseCode);
				if(responseCode == 200) { //200은 연결 성공
					BufferedReader br = 
						new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8")); 
					String line = ""; 
					String result = "";
					while ((line = br.readLine()) != null) {
						result += line;
					}
					JSONParser parser = new JSONParser(); //문자열을 json객체화하는 객체
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
		
		//kakao access-token 메서드
		public String getKakaoAccessToken (String authorize_code,HttpServletResponse response) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8"); 
			String access_Token = "";
			String refresh_Token = "";
			String reqURL = "https://kauth.kakao.com/oauth/token";
			try {
				URL url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				// URL연결은 입출력에 사용 될 수 있고, POST 혹은 PUT 요청을 하려면 setDoOutput을 true로 설정해야함.
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				//kakao로 응답해주는 값
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
				StringBuilder sb = new StringBuilder();
				sb.append("grant_type=authorization_code");
				sb.append("&client_id=9f317d9f39587377b26054a0d805ba58");  //본인이 발급받은 key
				sb.append("&redirect_uri=https://localhost:8443/spjpj/kredirect");
				// 본인이 설정해 놓은 경로
				sb.append("&code=" + authorize_code);
				bw.write(sb.toString());
				bw.flush();
				//결과 코드가 200이라면 성공
				int responseCode = conn.getResponseCode();
	            System.out.println("responseCode : " + responseCode);
	            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
	            BufferedReader br = 
	            	new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8")); 
	            String line = "";
	            String result = "";
	            while ((line = br.readLine()) != null) {
	            	result += line;
	            }
	            System.out.println("response body : " + result);
	            JSONParser parser = new JSONParser();
	            Object obj = parser.parse(result); //parse메서드는 Object반환
	            JSONObject jsonObj = (JSONObject) obj;
	            access_Token = (String)jsonObj.get("access_token");
				refresh_Token = (String)jsonObj.get("refresh_token");
				System.out.println("access_token : " + access_Token);
		        System.out.println("refresh_token : " + refresh_Token);
		        //io객체는 close
		        br.close();
	            bw.close();
			}
			catch(Exception e) {
				e.getMessage();
			}
			return access_Token;
		}
		
		//kakao access-token 으로 사용자 정보 얻기
		public HashMap<String,Object> getKakaoUserInfo (String access_Token) {
			HashMap<String, Object> userInfo = new HashMap<String, Object>();
			String reqURL = "https://kapi.kakao.com/v2/user/me";
			try {
				URL url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				//요청에 필요한 Header에 포함될 내용
				conn.setRequestProperty("Authorization", "Bearer " + access_Token);
				int responseCode = conn.getResponseCode(); //200이면 정상
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
		        JSONObject kakao_account = (JSONObject) jsonObj.get("kakao_account"); //검수후에 가능
		       
		        String accessToken = (String)properties.get("access_token");
		        String nickname = (String)properties.get("nickname");
		        String email = (String)kakao_account.get("email"); //검수후에 가능
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
		
		// 로그인 성공후 페이지이므로 id와 role정보를 얻어내는 방법
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Constant.username = userDetails.getUsername();
		System.out.println(userDetails.getUsername());//hsk5410@naver.com (로그인한 아이디)
		Collection<? extends GrantedAuthority>  authorities = authentication.getAuthorities();
		String auth = authorities.toString(); //role을 얻어서 문자열로 변환
		System.out.println(auth); //[ROLE_USER]형태
		return "spj_main";
	}
	
//	======= 자유게시판 화면 ======	
	@RequestMapping("/spj_FreeboardList")
	public String spj_FreeboardList(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("/spj_FreeboardList");	
		com = new BListCommand();
		com.execute(model, request);
		return "spj_FreeboardList";
	}
	
	@RequestMapping("/spj_freeboardWrite")  //ajax로 요청시 @ResponseBody없으면 jsp반환
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
	
	//게시글 보기
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
	
	//게시글 수정
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
	// 댓글달기
	@RequestMapping("/reply_view") //ajax
	public String reply_view(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("reply_view()");		
		com = new FBoardReplyViewCommand();
		com.execute(model,request); //선택한 게시판 목록에 대한 값들을 가져와서 댓글창에 사용
		request.setAttribute("user",Constant.username);
		//model에 두개의 속성 줄수없음으로  request에 줌
		return "reply_view";
	}
	
	
	@RequestMapping("/reply") 
	public String reply(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("reply()");		
		com = new FBoardReplyCommand();
		com.execute(model,request);		
		return "redirect:spj_FreeboardList";
	}
	

	
//	======== reviewboard 화면 =====
	@RequestMapping("/spj_reviewboardList")
	public String spj_reviewboardList(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("/spj_reviewboardList");
		com = new RListCommend();
		com.execute(model, request);
		return "spj_reviewboardList";
	}

	
	
	@RequestMapping("/spj_reviewboardWrite")  //ajax로 요청시 @ResponseBody없으면 jsp반환
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
	
	//게시글 보기
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

	//게시글 삭제
	@RequestMapping("/review_delete()")
	public String review_delete(HttpServletRequest request,HttpServletResponse response,Model model) {
		System.out.println("review_delete()");
		
		com = new RDeleteCommand();
		com.execute(model, request);
		
		return "redirect:spj_FreeboardList";
	}
	
	
	//======= 질문게시판 화면 ======	
			@RequestMapping("/spj_qnaboardList")
			public String spj_qnaboardList(HttpServletRequest request,HttpServletResponse response,Model model) {
				System.out.println("/spj_qnaboardList");	
				com = new QListCommand();
				com.execute(model, request);
				
				return "spj_qnaboardList";
			}
			
			@RequestMapping("/spj_QnAboardWrite")  //ajax로 요청시 @ResponseBody없으면 jsp반환
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
			//게시글 수정
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
			//리뷰게시판 페이징
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
	//리뷰게시판 페이징
		@RequestMapping("/rplist")
		public String review_plist(HttpServletRequest request,HttpServletResponse response,Model model) {
			System.out.println("rplist");
			System.out.println(request.getParameter("rpageNo"));
			com = new RBPageListCommand();
			com.execute(model, request);
			return "rplist";
		}
	
	
//	========== menu ================
//	===== main 화면 =====
	@RequestMapping("/spj_main")
	public String spj_main() {
		return "spj_main";
	}	
	
//	===== about 화면 =====
	@RequestMapping("/spj_about")
	public String spj_about() {
		return "spj_about";
	}	
		
//  ========= 알바 일지 작성 ========
	@RequestMapping("/spj_record")
	public String spj_record(Model model) {
		System.out.println("spj_record()");
		model.addAttribute("user", Constant.username);
		return "spj_record";
	}

	// ===== 회원가입 화면 =====
	@RequestMapping("/spj_join")
	public String spj_join() {
		return "spj_join";
	}

	// ===== 마이페이지 화면 =====
	@RequestMapping("/spj_myPage")
	public String spj_myPage() {
		return "spj_myPage";
	}
}
