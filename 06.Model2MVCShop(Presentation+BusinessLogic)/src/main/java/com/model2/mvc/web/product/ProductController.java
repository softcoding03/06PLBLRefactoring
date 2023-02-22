package com.model2.mvc.web.product;


import java.sql.Date;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserService;


//==> ȸ������ Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method ���� ����
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	//@Value("#{commonProperties['pageUnit']}")
	@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	//@Value("#{commonProperties['pageSize']}")
	@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception {

		System.out.println("/addProductView.do");
		
		return "forward:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct(@ModelAttribute("product") Product product, Model model) throws Exception {

		System.out.println("/addProduct.do");
		//Business Logic
		productService.addProduct(product);
		model.addAttribute("product", product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int prodNo, Model model ) throws Exception {
		
		System.out.println("/getProduct.do");
		System.out.println("prodNo int�� �� ����Ǿ����� ?  --> "+prodNo);
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
		
		// Model �� View ����
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	@RequestMapping("/listProduct.do")
	public String listUser( @ModelAttribute("search") Search search,
							@RequestParam("menu") String menu,
							Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listProduct.do");
		System.out.println("menu �� �Ѿ� �Գ��� ?? --->"+menu);
		
		if(search.getCurrentPage()==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model �� View ����
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		System.out.println("resultpage �� ���õǾ����� ??? --->"+resultPage);
		System.out.println("menu �� �Ѿ� �Գ��� 222?? --->"+menu);
		System.out.println("list �� �Ѿ� �Գ��� ?? --->"+map.get("list"));

		if (menu.equals("manage")) {
			return "forward:/product/listProductManage.jsp";
		}else {
			return "forward:/product/listProductSearch.jsp";
		}
	}
		
	@RequestMapping("/updateProductView.do")
	public String updateProductView(@RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		// Model �� View ����
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}

	
	@RequestMapping("/updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product,
								Model model , HttpSession session) throws Exception{
		
		System.out.println("product ??? -> "+ product);
		System.out.println("/updateProduct.do");
		//Business Logic
		productService.updateProduct(product);
		
		Product product2 = productService.getProduct(product.getProdNo());
		model.addAttribute("product", product2);
		return "forward:/product/updateProduct.jsp";
	}
}