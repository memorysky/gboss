package com.chinagps.fee.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import ldap.oper.OpenLdap;
import ldap.oper.OpenLdapManager;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.comm.SystemException;
import com.chinagps.fee.entity.po.Servicepack;
import com.chinagps.fee.service.ServicePackService;
import com.chinagps.fee.util.LogOperation;
import com.chinagps.fee.util.PageSelect;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.page.Page;

/**
 * @Package:com.chinagps.fee.controller.sel
 * @ClassName:ServicePackController
 * @Description:套餐管理控制层
 * @author:zfy
 * @date:2013-8-9 下午2:39:42
 */
@Controller
@RequestMapping(value="/service")
public class ServicePackController extends BaseController {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(ServicePackController.class);

	@Autowired
	@Qualifier("servicePackService")
	private ServicePackService servicePackService;

	private ObjectMapper objectMapper = new ObjectMapper();
	private JsonGenerator jsonGenerator = null;
     
	/**
	 * @Title:addServicePackItemPrice
	 * @Description:添加套餐、套餐服务项、套餐价格
	 * @param servicepack
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "addServicePackItemPrice")
	@LogOperation(description = "添加套餐", op_type = SystemConst.OPERATELOG_ADD, model_id = 30032)
	public @ResponseBody
	HashMap<String, Object> addServicePackItemPrice(
			@RequestBody Servicepack servicepack, HttpServletRequest request) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("添加套餐 开始");
		}
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			if (LOGGER.isDebugEnabled()) {
				jsonGenerator = objectMapper.getJsonFactory()
						.createJsonGenerator(System.out, JsonEncoding.UTF8);
				System.out.println("参数:");
				jsonGenerator.writeObject(servicepack);
			}
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			String orgId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ORGID);
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			if (servicepack != null) {
				if (StringUtils.isNotBlank(userId)) {
					servicepack.setOpId(Long.valueOf(userId));
				}
				servicepack.setOrgId(Long.valueOf(orgId));
				
				OpenLdap openLdap = OpenLdapManager.getInstance();
				//判断是否是分公司
				int level=openLdap.getUserCompanyLevel(orgId);
				if(level==0){//总部
					servicepack.setSubcoNo(0l);
				}else{//分公司
					servicepack.setSubcoNo(Long.valueOf(companyId));
				}
			}
			int result = servicePackService
					.addServicePackItemPrice(servicepack);
			if (result == -1) {
				flag = false;
				msg = "参数不合法";
			} else if (result == 2) {
				flag = false;
				msg = "套餐编号为[" + servicepack.getPackCode() + "]的已存在";
			} else if (result == 3) {
				flag = false;
				msg = "套餐名称为[" + servicepack.getName() + "]的已存在";
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			flag = false;
			msg = SystemConst.OP_FAILURE;
			e.printStackTrace();
		}
		resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("添加套餐 结束");
		}
		return resultMap;
	}

	/**
	 * @Title:updateServicePackItemPrice
	 * @Description:添加套餐、套餐服务项、套餐价格
	 * @param servicepack
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "updateServicePackItemPrice")
	@LogOperation(description = "修改套餐", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30032)
	public @ResponseBody
	HashMap<String, Object> updateServicePackItemPrice(
			@RequestBody Servicepack servicepack, HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			if (LOGGER.isDebugEnabled()) {
				jsonGenerator = objectMapper.getJsonFactory()
						.createJsonGenerator(System.out, JsonEncoding.UTF8);
				System.out.println("参数:");
				jsonGenerator.writeObject(servicepack);
			}
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			String orgId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ORGID);
			if (servicepack != null) {
				if (StringUtils.isNotBlank(userId)) {
					servicepack.setOpId(Long.valueOf(userId));
				}
				if (StringUtils.isNotBlank(userId)) {
					servicepack.setOrgId(Long.valueOf(orgId));
				}
			}
			int result = servicePackService
					.updateServicePackItemPrice(servicepack);
			if (result == -1) {
				flag = false;
				msg = "参数不合法";
			} else if (result == 0) {
				flag = false;
				msg = "要操作的对象不存在";
			} else if (result == 2) {
				flag = false;
				msg = "套餐编号为[" + servicepack.getPackCode() + "]的已存在";
			} else if (result == 3) {
				flag = false;
				msg = "套餐名称为[" + servicepack.getName() + "]的已存在";
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
	 * @Title:deleteServicePackItemPrice
	 * @Description:删除套餐
	 * @param servicepack
	 *            参数例子:{id:"1"}
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "deleteServicePackItemPrice", method = RequestMethod.POST)
	@LogOperation(description = "删除套餐", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30032)
	public @ResponseBody
	HashMap<String, Object> deleteServicePackItemPrice(
			@RequestBody Servicepack servicepack, BindingResult bindingResult,
			HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			if (servicepack == null || servicepack.getPackId() == null) {
				flag = false;
				msg = "参数不合法";
			} else {
				int result = servicePackService
						.deleteServicePackItemPrice(servicepack.getPackId());
				if (result == 0) {
					flag = false;
					msg = "要操作的套餐不存在";
				}
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
	 * @Title:findServicePackItemPrice
	 * @Description:分页查询套餐(未审核、已审核共用一个方法)
	 * @param pageSelect
	 *            参数例子{isVerified:0}
	 * @param bindingResult
	 * @param request
	 * @return
	 * @throws SystemException
	 * @throws
	 */
	@RequestMapping(value = "findServicePackItemPrice")
	public @ResponseBody
	Page<Map<Long, Object>> findServicePackItemPrice(
			@RequestBody PageSelect<Servicepack> pageSelect,
			BindingResult bindingResult, HttpServletRequest request)
			throws SystemException {
		Page<Map<Long, Object>> results = null;
		try {
			String orgId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ORGID);
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			if (pageSelect != null) {
				Map conditionMap = pageSelect.getFilter();
				if (conditionMap == null) {
					conditionMap = new HashMap();
				}
				conditionMap.put("companyId", companyId);
				conditionMap.put("orgId", orgId);
			}
			results = servicePackService.findServicePackItemPrice(pageSelect);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			// 打印日志
			e.printStackTrace();
			// 同时把异常抛到前台
			throw new SystemException();
		}
		return results;
	}

	/**
	 * @Title:updateIsVerifiedByIds
	 * @Description:批量审核套餐
	 * @param ids
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "updateIsVerifiedByIds")
	@LogOperation(description = "批量审核套餐", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30032)
	public @ResponseBody
	HashMap<String, Object> updateIsVerifiedByIds(
			@RequestBody List<Long> list, HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);

			servicePackService.updateIsVerifiedByIds(list, userId==null?null:Long.valueOf(userId));
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
	 * @Title:updateIsValidByIds
	 * @Description:批量设置套餐是否对新客户有效
	 * @param ids
	 * @param isvalid
	 *            0无效、1有效
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "updateIsValidByIds")
	@LogOperation(description = "批量设置套餐是否对新客户有效", op_type = SystemConst.OPERATELOG_UPDATE, model_id = 30032)
	public @ResponseBody
	HashMap<String, Object> updateIsValidByIds(@RequestParam List<Long> ids,
			@RequestParam Integer isvalid, HttpServletRequest request) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		try {
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			servicePackService.updateIsValidByIds(ids, isvalid, userId==null?null:Long.valueOf(userId));
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
	 * @Title:getServicePackItemsPrices
	 * @Description:根据ID查询套餐，以及套餐与服务项的关系、套餐与不同用户类型的价格关系
	 * @param id
	 * @param request
	 * @return
	 * @throws
	 */
	@RequestMapping(value = "getServicePackItemsPrices")
	public @ResponseBody
	HashMap<String, Object> getServicePackItemsPrices(@RequestParam Long id,
			HttpServletRequest request) throws SystemException {
		HashMap<String, Object> resultMap = null;
		try {
			resultMap = servicePackService.getServicePackItemsPrices(id);
		} catch (SystemException e) {
			// 打印日志
			e.printStackTrace();
			// 同时把异常抛到前台
			throw new SystemException();
		}
		return resultMap;
	}
	
	@RequestMapping(value = "exportExcel4Pack")
	public void exportExcel4Pack(HttpServletRequest request,HttpServletResponse response) throws SystemException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("导出套餐 开始");
		}
		OutputStream os = null;
		WritableWorkbook wb = null;
		try {
			WritableFont wf_key = new jxl.write.WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.BOLD); 
	        WritableFont wf_value = new jxl.write.WritableFont(WritableFont.createFont("宋体"), 10,WritableFont.NO_BOLD); 
            
	        WritableCellFormat wcf_value = new WritableCellFormat(wf_value); 
            wcf_value.setAlignment(jxl.format.Alignment.CENTRE); 
            wcf_value.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
            wcf_value.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); 
             
            WritableCellFormat wcf_name_center = new WritableCellFormat(wf_key); 
            wcf_name_center.setAlignment(Alignment.CENTRE); 
            wcf_name_center.setVerticalAlignment(VerticalAlignment.CENTRE); 
            wcf_name_center.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); 
             
            WritableFont wf_title = new jxl.write.WritableFont(WritableFont.createFont("宋体"), 14,WritableFont.BOLD); 
            WritableCellFormat  wcf_title = new WritableCellFormat(wf_title); 
            wcf_title.setAlignment(Alignment.CENTRE); 
            wcf_title.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); 
            
			String orgId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ORGID);
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("companyId", companyId);
			map.put("orgId", orgId);
			map.put("isVerified", request.getParameter("isVerified"));
			map.put("code", request.getParameter("code"));
			map.put("name", request.getParameter("name"));
			List<Map<Long, Object>> results = servicePackService.findServicePackItemPrice(map);
		
			String fileName="服务套餐报表";
			
			os = response.getOutputStream();
			response.setHeader("Content-disposition", "attachment; filename="+ java.net.URLEncoder.encode(fileName,"UTF-8")+ ".xls"); //设定输出文件头
			response.setContentType("application/excel"); //定义输出类型
			wb = Workbook.createWorkbook(os);
	        WritableSheet ws = wb.createSheet("服务套餐报表",0); 
	         
	        int startRowNum=0;//起始行 
	        int startColNum=0;//起始列 
	         
	        //设置列宽 
	        ws.setColumnView(0, 10); 
	        ws.setColumnView(1, 14);
	        ws.setColumnView(2, 8);
	        ws.setColumnView(3, 8);
	        ws.setColumnView(4, 8);
	        ws.setColumnView(5, 8);
	        ws.setColumnView(6, 8);
	        ws.setColumnView(7, 8);
	        ws.setColumnView(8, 8);

	        ws.setColumnView(9, 14); 
	        ws.setColumnView(10, 10); 
	        ws.setColumnView(11, 12); 
	        //第一行
	        ws.addCell(new Label(startColNum,startRowNum,"序号",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum,startRowNum+1); 
	       
	        startColNum=1; 
	        ws.addCell(new Label(startColNum,startRowNum,"编号",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum,startRowNum+1); 
	       
	        startColNum=2; 
	        ws.addCell(new Label(startColNum,startRowNum,"套餐名称",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum,startRowNum+1); 
	       
	        startColNum=3; 
	        ws.addCell(new Label(startColNum,startRowNum,"金卡",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum+1,startRowNum); 
	        ws.addCell(new Label(startColNum,startRowNum+1,"月",wcf_name_center)); 
	        startColNum=4; 
	        ws.addCell(new Label(startColNum,startRowNum+1,"年",wcf_name_center)); 
		    
	        startColNum=5; 
	        ws.addCell(new Label(startColNum,startRowNum,"银卡",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum+1,startRowNum); 
	        ws.addCell(new Label(startColNum,startRowNum+1,"月",wcf_name_center)); 
	        startColNum=6; 
	        ws.addCell(new Label(startColNum,startRowNum+1,"年",wcf_name_center)); 
		
	        startColNum=7; 
	        ws.addCell(new Label(startColNum,startRowNum,"普通卡",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum+1,startRowNum); 
	        ws.addCell(new Label(startColNum,startRowNum+1,"月",wcf_name_center)); 
	        startColNum=8; 
	        ws.addCell(new Label(startColNum,startRowNum+1,"年",wcf_name_center)); 
	        
	        startColNum=9; 
	        ws.addCell(new Label(startColNum,startRowNum,"备注",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum,startRowNum+1); 
	       
	        startColNum=10; 
	        ws.addCell(new Label(startColNum,startRowNum,"新用户是否生效",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum,startRowNum+1); 
	       
	        startColNum=11; 
	        ws.addCell(new Label(startColNum,startRowNum,"生效时间",wcf_name_center)); 
	        ws.mergeCells(startColNum,startRowNum, startColNum,startRowNum+1); 

	        startRowNum=2;       
	        startColNum=0; 
	         
        	if (results != null && !results.isEmpty()) {
        		Map<Long, Object> resultMap=null;
        		int rowIndex=0;
        		int listLenth=results.size();
        		for (int j = 0; j < listLenth; j++) {
        			resultMap = results.get(j);
					rowIndex = startRowNum+j; 
					ws.addCell(new Label(startColNum++,rowIndex,rowIndex+"",wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("code")),wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("name")),wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("type2MonthPrice")),wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("type2YearPrice")),wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("type1MonthPrice")),wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("type1YearPrice")),wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("type0MonthPrice")),wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("type0YearPrice")),wcf_value)); 
					ws.addCell(new Label(startColNum++,rowIndex,Utils.clearNull(resultMap.get("remark")),wcf_value)); 
					if((resultMap.get("isvalid") == null?0:(Integer)resultMap.get("isvalid"))==1){
						ws.addCell(new Label(startColNum++,rowIndex,"是",wcf_value)); 
					}else{
						ws.addCell(new Label(startColNum++,rowIndex,"否",wcf_value));
					}
					ws.addCell(new Label(startColNum++,rowIndex,resultMap.get("verifiedTime") == null?"":resultMap.get("verifiedTime")+"",wcf_value)); 
					
					startColNum=0;
				}
        	}
	        wb.write();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		}  finally {
			try {
				if(wb!=null){
					wb.close();
				}
				if(os!=null){
					os.flush();    
					os.close();
				}
			} catch (WriteException e) {
				LOGGER.error(e.getMessage(), e);
				e.printStackTrace();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
				e.printStackTrace();
			}
		} 
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("导出套餐 结束");
		}
	}
}
