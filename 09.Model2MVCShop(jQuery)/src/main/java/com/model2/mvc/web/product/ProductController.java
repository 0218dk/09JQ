package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserService;


//==> 회원관리 Controller
@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping(value="addProduct", method=RequestMethod.GET)
	public String addProduct() throws Exception {

		System.out.println("/addProduct : GET");
		
		
		//return "redirect:/product/addProductResult.jsp";
		return "redirect:/product/addProduct.jsp";
	}
	
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct( @ModelAttribute("product") Product product ) throws Exception {

		System.out.println("/addProduct : POST");
		//Business Logic
		productService.addProduct(product);
		
		/*정보 한개 넘겨서 추가등록 버튼*/
		//return "redirect:/product/addProductView.jsp";
		
		product.setProTranCode("add");
		return "forward:/product/updateProductView.jsp";
	}
	
	
	@RequestMapping(value="getProduct", method=RequestMethod.GET)
	public String getProduct( @RequestParam("prodNo") int prodNo , 
							  @RequestParam("menu")String menu,
							   Model model ) throws Exception {
		
		System.out.println("/getProduct.do");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		if ((menu).equals("manage")) {
			return "forward:/product/updateProduct";
		} else  {
			return "forward:/product/getProduct.jsp";
		}
	}
	
	
	@RequestMapping(value="listProduct")
	public String listProduct( @ModelAttribute("search") Search search ,
							 	Model model) throws Exception{
		
		System.out.println("서치? : "+search);
		
		System.out.println(search);
		
		
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		// Business logic 수행
		
		System.out.println("맵에 세팅 전 서치 : "+search);
		Map<String , Object> map=productService.getProductList(search);
		
		System.out.println("컨트롤러의 맵 : "+map);
		
		
		System.out.println("=============================================================");
		Page resultPage = new Page( search.getCurrentPage(),
				((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("리절트페이지 : "+resultPage);
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		System.out.println("모델에 뭐가 들어감? : "+model);
		return "forward:/product/listProduct.jsp";
	}
	
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public String updateProduct( @RequestParam("prodNo") int prodNo , 
									  @RequestParam("menu")String menu,
									   Model model ) throws Exception {

		System.out.println("GET이다===");
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct( @ModelAttribute("product") Product product , Model model , HttpSession session) throws Exception{

		System.out.println("POST다다다다다===");
		//Business Logic
		productService.updateProduct(product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	
	
	
	
	
	
	
	
}