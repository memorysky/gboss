package com.chinagps.fee.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ldap.objectClasses.CommonCompany;
import ldap.oper.OpenLdap;
import ldap.oper.OpenLdapManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Collection;
import com.chinagps.fee.entity.po.Reprintinvoice;
import com.chinagps.fee.entity.po.Serviceitem;
import com.chinagps.fee.entity.vo.ItemVO;
import com.chinagps.fee.service.PaymentService;
import com.chinagps.fee.service.ReprintinvoiceService;
import com.chinagps.fee.util.CreateExcel_PDF_CSV;
import com.chinagps.fee.util.FilesToZip;
import com.chinagps.fee.util.LogOperation;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.WordPrint;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.chinagps.fee.controller
 * @ClassName:ReprintinvoiceController
 * @Description:补打发票控制器
 * @author:zfy
 * @date:2014-6-9 下午3:29:46
 */
@Controller
@RequestMapping(value = "/invoice")
public class ReprintinvoiceController extends BaseController{
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(ReprintinvoiceController.class);

	@Autowired
	@Qualifier("reprintinvoiceService")
	private ReprintinvoiceService reprintinvoiceService;
	
	@Autowired
	@Qualifier("paymentService")
	private PaymentService paymentService;
	
	/**
	 * @Title:findReprintPage
	 * @Description:查询补打设置信息
	 * @param pageSelect
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "findReprintPage")
	public @ResponseBody
	Page<Map<String, Object>> findReprintPage(@RequestBody PageSelect<Map<String, Object>> pageSelect,
			HttpServletRequest request) throws SystemException {
		Page<Map<String, Object>> results = null;
		try {
			if(pageSelect==null){
				pageSelect=new PageSelect<Map<String,Object>>();
			}
			if (pageSelect != null) {
				if (pageSelect.getFilter() == null) {
					pageSelect.setFilter(new HashMap());
				}
			}
				String userId = (String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_ID);
				String companyId = (String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_COMPANYID);
				String orgId=(String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_ORGID);
				if (StringUtils.isNotBlank(userId)) {
					OpenLdap openLdap = OpenLdapManager.getInstance();
					//判断是否是分公司
					int level=openLdap.getUserCompanyLevel(orgId);
					if(level==0){//总部
						
					}else if(level==1){//分公司
						pageSelect.getFilter().put("subcoNo", Long.valueOf(companyId));
					}else if(level==2){//营业处
						pageSelect.getFilter().put("subcoNo", Long.valueOf(companyId));
					}
				}
				
			results = reprintinvoiceService.findReprintPage(pageSelect);
			/*//将本次查到的unitId全部保存在session中
			List<Map<String,Object>> dataList=reprintinvoiceService.findReprints(pageSelect.getFilter());
			List<Long> unitIds=new ArrayList<Long>();
			if(Utils.isNotNullOrEmpty(dataList)){
				for (Map<String, Object> map : dataList) {
					unitIds.add(Long.valueOf(map.get("unitId").toString()));
				}
			}*/
			HttpSession session = request.getSession();	
			session.setAttribute("reprintFilterTmp", pageSelect.getFilter());

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印日志
			e.printStackTrace();
			// 同时把异常抛到前台
			//throw new SystemException();
		}
		return results;
	}
	
	@RequestMapping(value = "addReprintinvoice")
	@LogOperation(description = "添加发票补打信息", op_type = SystemConst.OPERATELOG_ADD, model_id = 30012)
	public @ResponseBody
	 HashMap<String, Object> addServiceitem(
			@RequestBody  Reprintinvoice reprintinvoice,
			 HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
		
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			reprintinvoice.setSubcoNo(Long.valueOf(companyId));
			reprintinvoice.setIsPrinted(0);
			reprintinvoice.setIsDel(0);
			reprintinvoiceService.save(reprintinvoice);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			flag = false;
			msg = SystemConst.OP_FAILURE;
			e.printStackTrace();
		}
		resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);
		return resultMap;
	}
	
	@RequestMapping(value = "updateReprint")
	@LogOperation(description = "修改补打发票设置", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30012)
	public @ResponseBody
	 HashMap<String, Object> updateReprint(
			@RequestBody Reprintinvoice reprintinvoice,
			 BindingResult bindingResult,  HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			reprintinvoice.setSubcoNo(Long.valueOf(companyId));
			int result = reprintinvoiceService.updateReprint(reprintinvoice);
			if (result == -1) {
				flag = false;
				msg = "参数不合法";
			} else if (result == 0) {
				flag = false;
				msg = "要操作的对象不存在";
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			flag = false;
			msg = SystemConst.OP_FAILURE;
			e.printStackTrace();
		}
		resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);
		return resultMap;
	}
	@RequestMapping(value = "deleteReprint")
	@LogOperation(description = "删除补打发票设置", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30012)
	public @ResponseBody
	 HashMap<String, Object> deleteReprint(
			@RequestBody List<Long> reprintIds,
			 BindingResult bindingResult,  HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			int result = reprintinvoiceService.updateReprint2(reprintIds);
			if (result == -1) {
				flag = false;
				msg = "参数不合法";
			} else if (result == 0) {
				flag = false;
				msg = "要操作的对象不存在";
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			flag = false;
			msg = SystemConst.OP_FAILURE;
			e.printStackTrace();
		}
		resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);
		return resultMap;
	}
	
	/**
	 * @Title:addRePrint
	 * @Description:补打发票打印处理 查询“打印本页”、“打印本页选中记录”等数据,并打印
	 * @param ids
	 * @param printType
	 * @param printFun 打印方法，1：word、2：网页
	 * @param request
	 * @return
	 * @throws SystemException
	 */
	@RequestMapping(value = "addRePrint")
	@LogOperation(description = "发票补打", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30012)
	public @ResponseBody List<Map<String, Object>> addRePrint(@RequestBody Map<String, Object> map,
			HttpServletRequest request,HttpServletResponse response) throws SystemException {
		List<Map<String, Object>> results = null;
		try {
				String userId = (String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_ID);
				String companyId = (String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_COMPANYID);
				String orgId=(String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_ORGID);
				String reprintIds=null;
				String unitIds=null;
				Integer printType=1;
				Integer printFun=2;
				if(map!=null){
					printType=map.get("printType")==null?1:(Integer)map.get("printType");
					printFun=map.get("printFun")==null?1:(Integer)map.get("printFun");
					reprintIds=map.get("reprintIds")==null?null:(String)map.get("reprintIds");
					unitIds=map.get("unitIds")==null?null:(String)map.get("unitIds");
				}
				if (StringUtils.isNotBlank(userId)) {
					OpenLdap openLdap = OpenLdapManager.getInstance();
					//判断是否是分公司
					int level=openLdap.getUserCompanyLevel(orgId);
					if(level==0){//总部
						
					}else if(level==1){//分公司
						map.put("subcoNo", Long.valueOf(companyId));
					}else if(level==2){//营业处
						map.put("subcoNo", Long.valueOf(companyId));
					}
				}
				//map.put("unitIds", unitIds);
				
				map.put("isRePrint", 1);//表示是补打，关联补打发票记录表
				HttpSession session = request.getSession();	
				if(printType==1 || printType==2){//打印本页记录 1、打印本页选中的记录 2
					results = paymentService.findRePrints(map,true);
				}else if(printType==3){//删除选中记录 3
					reprintinvoiceService.updateReprint2(reprintIds);
				}else if(printType==4){//打印本次查询到的所有记录 4
					map=(Map<String, Object>)  session.getAttribute("reprintFilterTmp");
					map.put("isRePrint", 1);//表示是补打，关联补打发票记录表
					results = paymentService.findRePrints(map,true);
				}else if(printType==5){//导出Excel 5
				}
				

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印日志
			e.printStackTrace();
			// 同时把异常抛到前台
			//throw new SystemException();
		}
		return results;
	}
	
	@RequestMapping(value = "exportAndRePrint")
	@LogOperation(description = "发票补打导出", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30010)
	public @ResponseBody void exportAndRePrint(
			HttpServletRequest request,HttpServletResponse response) throws SystemException {
		List<Map<String, Object>> results = null;
		try {
				String userId = (String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_ID);
				String companyId = (String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_COMPANYID);
				String orgId=(String) request.getSession().getAttribute(
						SystemConst.ACCOUNT_ORGID);
				HttpSession session = request.getSession();	
				Map map=parseReqParam2(request);
				if (StringUtils.isNotBlank(userId)) {
					OpenLdap openLdap = OpenLdapManager.getInstance();
					//判断是否是分公司
					int level=openLdap.getUserCompanyLevel(orgId);
					if(level==0){//总部
						
					}else if(level==1){//分公司
						map.put("subcoNo", Long.valueOf(companyId));
					}else if(level==2){//营业处
						map.put("subcoNo", Long.valueOf(companyId));
					}
				}
				map.put("isRePrint", 1);//表示是补打，关联补打发票记录表
				Integer printType=1;
				Integer printFun=2;
				if(map!=null){
					printType=map.get("printType")==null?1:Integer.valueOf(map.get("printType").toString());
					printFun=map.get("printFun")==null?1:Integer.valueOf(map.get("printFun").toString());
				}
				if(printType==1 || printType==2){//打印本页记录 1、打印本页选中的记录 2
					results = paymentService.findRePrints(map,true);
				}/*else if(printType==3){//清除选中记录打印标记 3
					reprintinvoiceService.updateReprint2(reprintIds);
				}*/else if(printType==4){//打印本次查询到的所有记录 4
					map=(Map<String, Object>) session.getAttribute("reprintFilterTmp");
					map.put("isRePrint", 1);//表示是补打，关联补打发票记录表
					results = paymentService.findRePrints(map,true);
				}else if(printType==5){//导出Excel
					map=(Map<String, Object>) session.getAttribute("reprintFilterTmp");
					map.put("isRePrint", 1);//表示是补打，关联补打发票记录表
					results = paymentService.findRePrints(map,false);
					try {
					String[] title = {"邮编", "地址", "收件人", "车载电话1", "用户名称",
	        				"银行名称", "扣款银行账号", "流水号", "打印日期", "项目",
	        				"金额", "扣款日期", "合计人民币(大写)", "(小写)", "计费时段始","计费时段终",
	        				"用户名", "车载电话2"};
	        		List<String[]> showList = new ArrayList<String[]>();
	        		int index;
	        		List<ItemVO> itemVOs;
	        		float money=0f;
	        		String[] row = new String[title.length];
	        		for(Map<String, Object> info: results){ 
	        			index = 0;
	        			money=0f;
	        			row[index++] = Utils.clearNull(info.get("postCode"));
	        			row[index++] = Utils.clearNull(info.get("address"));
	        			row[index++] =  Utils.clearNull(info.get("addressee"));
	        			row[index++] =  Utils.clearNull(info.get("callLetter1"));
	        			row[index++] =  Utils.clearNull(info.get("customerName"));
	        			row[index++] =  Utils.clearNull(info.get("bank"));       			
	        			row[index++] =  Utils.clearNull(info.get("acNo")+" ");       			
	        			row[index++] =  Utils.clearNull(info.get("bwNo"));
	        			row[index++] =  Utils.clearNull(info.get("printDate"));
	        			row[index++] ="GPS信息费" ;
    					row[index++] =Utils.clearNull(info.get("realAmount"));    
    					row[index++] =Utils.clearNull(info.get("payTime"));
	        			row[index++] =  Utils.clearNull(info.get("realAmountRMB"));
	        			row[index++] =  Utils.clearNull(info.get("realAmount"));    			
	        			row[index++] =  Utils.clearNull(info.get("sdate"));
	        			row[index++] =  Utils.clearNull(info.get("edate"));
	        			row[index++] =  Utils.clearNull(info.get("customerName"));
	        			row[index++] =  Utils.clearNull(info.get("callLetter")+" ");
	        			
	        			showList.add(row);
	        			row=row.clone();
	        		}
	        		
	        		CreateExcel_PDF_CSV.createExcel(showList, response, "发票补打", title);
	        		showList.clear();
	        		showList=null;
	        		//容器不能同时用out字符流输出，outStream字节流输出，所以用字符流下载压缩文件时，return null 阻止字节流的输出
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
				
				//如果需要打印word
				if(printFun==1 && (printType!=3) && (printType!=5)){
					//生成临时文件目录
					String tempDirectory=request.getSession().getServletContext().getRealPath("/");
					//E:\J2EE\workspace\fee\src\main\webapp
					tempDirectory=tempDirectory+"/tmp";
					OpenLdap openLdap = OpenLdapManager.getInstance();
					if(companyId!=null){
						CommonCompany commonCompany=openLdap.getCompanyById(companyId.toString());
						String companyNo=commonCompany.getCompanycode()==null?"":commonCompany.getCompanycode();
						if(StringUtils.isBlank(companyNo)){
							companyNo="0";
						}else {
							int length=companyNo.length();
							if(length>=2){
							   companyNo=companyNo.substring(length-2, length);
							}
						}
						tempDirectory=tempDirectory+"/tmp"+companyNo;
					}
					String wordFileName="feePrint";//word文件名
					File tempDirectoryFile = new File(tempDirectory);
					if(!tempDirectoryFile.exists()){
						tempDirectoryFile.mkdirs();
					}else{
						//删除目录下的所有文件
						FilesToZip.deleteDir(tempDirectoryFile);
						if(!tempDirectoryFile.exists()){
							tempDirectoryFile.mkdirs();
						}
					}
					WordPrint test = new WordPrint();
					if(Utils.isNotNullOrEmpty(results)){
						//一个word放置1000条需要打印的数据
						List<Map<String, Object>> dataList=null;
						int size=results.size();
						int maxSize=SystemConst.WORD_MAX_PAGE;
						if(size>maxSize){
							int freq=size/maxSize;
							int remainder=size%maxSize;//余数
							int i=0;
							for (i = 0; i < freq; i++) {
								dataList=results.subList(i*maxSize,((i*maxSize)+(maxSize)));
								//生成临时文件
								test.createWord4fee(tempDirectory, wordFileName+i, dataList);
							}
							if(remainder>0){
								dataList=results.subList(i*maxSize,maxSize*i+remainder);
								//生成临时文件
								test.createWord4fee(tempDirectory, wordFileName+i, dataList);
							}
						}else{
							dataList= results;
							//生成临时文件
							test.createWord4fee(tempDirectory, wordFileName, dataList);
						}
					}
					//将临时目录下的临时文件打成zip包，供客户端下载
					File[] fs = tempDirectoryFile.listFiles();
					FilesToZip.downLoadFiles(tempDirectory+"/fee.zip", fs, request, response);
					//容器不能同时用out字符流输出，outStream字节流输出，所以用字符流下载压缩文件时，return null 阻止字节流的输出
					//下载完后删除临时文件夹
					/*if(tempDirectoryFile.exists()){
						FilesToZip.deleteDir(tempDirectoryFile);
					}*/
				}
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

