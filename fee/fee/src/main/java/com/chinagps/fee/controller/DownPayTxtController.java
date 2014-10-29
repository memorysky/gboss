package com.chinagps.fee.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinagps.fee.comm.SystemConst;
import com.chinagps.fee.entity.po.Paytxt;
import com.chinagps.fee.entity.po.Subco;
import com.chinagps.fee.service.PaytxtService;
import com.chinagps.fee.service.SubcoService;
import com.chinagps.fee.util.DateUtil;
import com.chinagps.fee.util.FormatUtil;
import com.chinagps.fee.util.LogOperation;
import com.chinagps.fee.util.StringUtils;
import com.chinagps.fee.util.Utils;
import com.chinagps.fee.util.Validation;

/**
 * @Package:com.chinagps.fee.controller
 * @ClassName:DownPayTxtController
 * @Description:下载银行托收文件
 * @author:zfy
 * @date:2014-5-21 下午4:20:34
 */
@Controller
@RequestMapping(value = "/pay")
public class DownPayTxtController extends BaseController{
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(DownPayTxtController.class);

	@Autowired
	@Qualifier("paytxtService")
	private PaytxtService paytxtService;
	
	@Autowired
	@Qualifier("subcoService")
	private SubcoService subcoService; 

	@RequestMapping(value = "downPaytxtFile")
	@LogOperation(description = "下载托收文件", op_type = SystemConst.OPERATELOG_ADD, model_id = 30000)
	public @ResponseBody
	HashMap<String, Object> downPaytxtFile(
			HttpServletRequest request,
			HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("下载托收文件 开始");
		}
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = true;
		String msg = SystemConst.OP_SUCCESS;
		PrintWriter out = null;
		try {
			String userId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_ID);
			
			Map<String,Object> map=parseReqParam2(request);
			//页面选择的下载日期
			String donwLoadDateStr = map.get("downLoadDate") == null ? DateUtil.format(new Date(),DateUtil.YMD):((String) map.get("downLoadDate")).replaceAll("-", "");
			Date donwLoadDate=DateUtil.parse(donwLoadDateStr, DateUtil.YMD);
			//页面选择的下载日期的下一天
			donwLoadDate.setDate(donwLoadDate.getDate()+1);
			String nextDateStr=DateUtil.format(donwLoadDate, DateUtil.YMD);
			// 类型：1：金融中心交易文件、2：金融中心总结文件、3：银盛交易文件、4：银盛总结文件、5：中国银联交易、总结文件
			Integer type = map.get("type") == null ? 1 : Integer.parseInt(map.get(
					"type").toString());
			int agency=2;//托收机构：1=银盛, 2=金融中心(默认),3=中国银联
			switch (type) {
			case 1:
				agency=2;
				break;
			case 2:
				agency=2;
				break;
			case 3:
				agency=1;
				break;
			case 4:
				agency=1;
				break;
			case 5:
				agency=3;
				break;
			default:
				agency=2;
				break;
			}
			map.put("agency", agency);
			String times = map.get("times") == null ? "1" : map.get("times").toString(); //下载的批次;
			
			//分公司ID
			String companyId = (String) request.getSession().getAttribute(
					SystemConst.ACCOUNT_COMPANYID);
			if(StringUtils.isNotBlank(companyId)){
				map.put("subcoNo", Long.valueOf(companyId));
			}
			
			//得到总结文件中需要的 代办银行号、收付单位代码、收付代码
			map.put("flag", 1);//有效的
			
			//托收返回标志
			map.put("payTag", SystemConst.PAY_TAG_DEFAULT);
			Subco subco=subcoService.getSubco(map);
			
			//是否是固定日期托收
			int ctIsFixt=subco.getCtIsfix();
			//固定托收日期
			int ctDay=subco.getCtDay();
			//实际托收日期=服务截止日期
			Date serviceEDate=donwLoadDate;
			if(ctIsFixt==1){//如果是固定日期托收,则服务截止日期为固定托收日期
				//获得某月的最后一天
				int lastDay=DateUtil.getActualMaximum(serviceEDate);
				if(ctDay>lastDay){
					ctDay=lastDay;
				}
				serviceEDate.setDate(ctDay);
			}
			response.setContentType("text/html; charset=utf-8");//定义输出类型
			// 修改输出文件内容
			out = response.getWriter();
			
			//需要托收的记录
			List<Paytxt> paytxts=null;
			
			//文件名
			String year = donwLoadDateStr.substring(0,4); //下载总结文件的年份
	        String month = donwLoadDateStr.substring(4,6); //下载总结文件的月份
	        String day = donwLoadDateStr.substring(6,8);//下载总结文件的日期
	        int recordNum=0;//总记录数
	        String fileName=null;
	        String money=null;
	        int pageNo=1;//页数
			int freq=0;//除数
			int remainder=0;//余数
			
			int paytxtsSize=0;//paytxts的长度
			Paytxt paytxt=null;
	        if(subco!=null){
	        	if (type == 1) {// 金融中心交易文件
					fileName=subco.getAccountCode()+ SystemConst.ZERO2 + month + times;
					//判断该批次是否已生成
					map.put("txtName", fileName);
					Map<String,Object> mapZj=paytxtService.getZjPaytxts(map);
					if(Utils.isNotNullOrEmpty(mapZj.get("feeSum"))){
						flag = false;
						msg = "该批次的文件已生成!";
						recordNum=mapZj.get("recordNum")==null?0:((Long)mapZj.get("recordNum")).intValue();
					}else{
						//用存过生成托收记录 
						java.sql.Date endDate=new java.sql.Date(serviceEDate.getTime());
						long result=paytxtService.addPaytxts(subco.getSubcoNo(), agency, fileName, Long.valueOf(userId), endDate);
						mapZj=paytxtService.getZjPaytxts(map);
						recordNum=mapZj.get("recordNum")==null?0:((Long)mapZj.get("recordNum")).intValue();
					}	
					response.setHeader("Content-disposition", "attachment; filename="
							+ java.net.URLEncoder.encode(fileName, "UTF-8") + SystemConst.PAY_TXT_TYPE.get(type)); // 设定输出文件头
					
					freq=recordNum/SystemConst.COLL_MAX_MAX;
					remainder=recordNum%SystemConst.COLL_MAX_MAX;//余数
					if(remainder>0 || recordNum<SystemConst.COLL_MAX_MAX){
						freq++;
					}
					//分页查询需要托收的记录
					for (pageNo = 1; pageNo <= freq; pageNo++) {
						paytxts=paytxtService.findPaytxts(map,pageNo,SystemConst.COLL_MAX_MAX);
						paytxtService.clear();
						if(Utils.isNotNullOrEmpty(paytxts)){
							paytxtsSize=paytxts.size();
							for (int i = 0; i < paytxts.size(); i++) {
								paytxt=paytxts.get(i);
								if(i==paytxtsSize-1 && pageNo==freq){
									out.print(paytxt.gainFileLine(true));
								}else{
									out.println(paytxt.gainFileLine(false));
								}
							}
						}
					}
				} else if (type == 2) {// 金融中心总结文件
					fileName=subco.getAccountCode()+  SystemConst.ZERO2 + month + times;
					map.put("txtName", fileName);
					 Map<String,Object> mapZj=paytxtService.getZjPaytxts(map);
					response.setHeader("Content-disposition", "attachment; filename="
							+ java.net.URLEncoder.encode(fileName, "UTF-8") + SystemConst.PAY_TXT_TYPE.get(type)); // 设定输出文件头
					if(Utils.isNotNullOrEmpty(mapZj) && Utils.isNotNullOrEmpty(mapZj.get("recordNum")) && Utils.isNotNullOrEmpty(mapZj.get("feeSum"))){
						StringBuffer sb=new StringBuffer();
						//代办银行号
						sb.append(FormatUtil.rightAppendBlank(subco.getBankCode(), 5));
						//收付单位代码
						sb.append(FormatUtil.leftAppendBlank(subco.getAccountCode(), 5));
						//币种 0-人民币；1-港币；
						sb.append(SystemConst.RMB0);
						//收付标志 1-收；0-付
						sb.append(SystemConst.PAY_TYPE1);
						//收付代码
						sb.append(FormatUtil.rightAppendBlank(subco.getAccountNo(), 3));
						//交易文件名
						sb.append(FormatUtil.rightAppendBlank(fileName, 8));
						//总户数 不足左补0；必须与交易明细文件中记录数一致。
						sb.append(FormatUtil.leftAppendZero(mapZj.get("recordNum").toString(), 8));
						//总金额 99999999999.99；等于交易明细文件中的金额总和。不足左补0
						sb.append(FormatUtil.leftAppendZero(FormatUtil.doubleToStr2Deci(Double.valueOf(mapZj.get("feeSum").toString())), 14));
						//已划款户数 8位 不填
						sb.append(FormatUtil.rightAppendBlank(SystemConst.BLANK, 8));
						//已划款金额 14位 不填
						sb.append(FormatUtil.rightAppendBlank(SystemConst.BLANK, 14));
						//送银行日期
						sb.append(FormatUtil.rightAppendBlank(donwLoadDateStr, 8));
						//应划款日期
						sb.append(FormatUtil.rightAppendBlank(nextDateStr, 8));
						//划款日期
						sb.append(FormatUtil.rightAppendBlank("00000000", 8));
						out.print(sb.toString());
					}
				} else if (type == 3) {// 银盛交易文件
					fileName=subco.getAccountCode()+ SystemConst.ZERO2 + year + month + times;
					
					//判断该批次是否已生成
					map.put("txtName", fileName);
					Map<String,Object> mapZj=paytxtService.getZjPaytxts(map);
					if(Utils.isNotNullOrEmpty(mapZj.get("feeSum"))){
						flag = false;
						msg = "该批次的文件已生成!";
						recordNum=mapZj.get("recordNum")==null?0:((Long)mapZj.get("recordNum")).intValue();
					}else{
						//用存过生成托收记录 
						java.sql.Date endDate=new java.sql.Date(serviceEDate.getTime());
						long result=paytxtService.addPaytxts(subco.getSubcoNo(), agency, fileName, Long.valueOf(userId), endDate);
						mapZj=paytxtService.getZjPaytxts(map);
						recordNum=mapZj.get("recordNum")==null?0:((Long)mapZj.get("recordNum")).intValue();
					}	
					response.setHeader("Content-disposition", "attachment; filename="
							+ java.net.URLEncoder.encode(fileName, "UTF-8") + SystemConst.PAY_TXT_TYPE.get(type)); // 设定输出文件头
					//分页查询需要托收的记录
					
					//查询需要托收的记录
					freq=recordNum/SystemConst.COLL_MAX_MAX;
					remainder=recordNum%SystemConst.COLL_MAX_MAX;//余数
					if(remainder>0 || recordNum<SystemConst.COLL_MAX_MAX){
						freq++;
					}
					
					for (pageNo = 1; pageNo <= freq; pageNo++) {
						    paytxts=paytxtService.findPaytxts(map,pageNo,SystemConst.COLL_MAX_MAX);
						    paytxtService.clear();
						    if(Utils.isNotNullOrEmpty(paytxts)){
							    paytxtsSize=paytxts.size();
								for (int i = 0; i < paytxts.size(); i++) {
									paytxt=paytxts.get(i);
									if(i==paytxtsSize-1 && pageNo==freq){
										out.print(paytxt.gainFileLine(true));
									}else{
										out.println(paytxt.gainFileLine(false));
									}
								}
						    }
					}
					
				} else if (type == 4) {// 银盛总结文件
					fileName=subco.getAccountCode()+ SystemConst.ZERO2 + year + month + times;
					
					map.put("txtName", fileName);
					 Map<String,Object> mapZj=paytxtService.getZjPaytxts(map);
					response.setHeader("Content-disposition", "attachment; filename="
							+ java.net.URLEncoder.encode(fileName, "UTF-8") + SystemConst.PAY_TXT_TYPE.get(type)); // 设定输出文件头
					if(Utils.isNotNullOrEmpty(mapZj) && Utils.isNotNullOrEmpty(mapZj.get("recordNum")) && Utils.isNotNullOrEmpty(mapZj.get("feeSum"))){
						StringBuffer sb=new StringBuffer();
						//代办银行号
						sb.append(FormatUtil.rightAppendBlank(subco.getBankCode(), 5));
						//收付单位代码
						sb.append(FormatUtil.leftAppendBlank(subco.getAccountCode(), 5));
						//币种 0-人民币；1-港币；
						sb.append(SystemConst.RMB0);
						//收付标志 1-收；0-付
						sb.append(SystemConst.PAY_TYPE1);
						//收付代码
						sb.append(FormatUtil.rightAppendBlank(subco.getAccountNo(), 3));
						//交易文件名
						sb.append(FormatUtil.rightAppendBlank(fileName, 12));
						//总户数 不足左补0；必须与交易明细文件中记录数一致。
						sb.append(FormatUtil.leftAppendZero(mapZj.get("recordNum").toString(), 8));
						//总金额 99999999999.99；等于交易明细文件中的金额总和。不足左补0
						sb.append(FormatUtil.leftAppendZero(FormatUtil.doubleToStr2Deci(Double.valueOf(mapZj.get("feeSum").toString())), 14));
						//已划款户数 8位 不填
						sb.append(FormatUtil.rightAppendBlank(SystemConst.BLANK, 8));
						//已划款金额 14位 不填
						sb.append(FormatUtil.rightAppendBlank(SystemConst.BLANK, 14));
						//送银行日期
						sb.append(FormatUtil.rightAppendBlank(donwLoadDateStr, 8));
						//应划款日期
						sb.append(FormatUtil.rightAppendBlank(nextDateStr, 8));
						//划款日期
						sb.append(FormatUtil.rightAppendBlank("00000000", 8));
						out.print(sb.toString());
					}
				} else if (type == 5) {// 中国银联交易、总结文件
					if(StringUtils.isNotBlank(year) && year.length()>2){
						year=year.substring(2,year.length());
					}
				   /*//获得当天的文件批次号，按顺序递增，从001-999
					map.put("today", 1);
					String fileMaxtimessStr=paytxtService.getMaxFiletimessOfToday(map);
					Long fileMaxtimess=0l;
					if(fileMaxtimessStr!=null){
						fileMaxtimess=Long.valueOf(fileMaxtimess);
					}
					fileMaxtimess++;*/
					fileName=FormatUtil.leftAppendZero(subco.getAccountCode(),15)+ SystemConst.PAY_Q + year + month +day +SystemConst.PAY__+FormatUtil.leftAppendZero(times,3);
					
					response.setHeader("Content-disposition", "attachment; filename="
							+ java.net.URLEncoder.encode(fileName, "UTF-8") + SystemConst.PAY_TXT_TYPE.get(type)); // 设定输出文件头
					//判断该批次是否已生成
					map.put("txtName", fileName);
					Map<String,Object> mapZj=paytxtService.getZjPaytxts(map);
					if(Utils.isNotNullOrEmpty(mapZj.get("feeSum"))){
						flag = false;
						msg = "该批次的文件已生成!";
						recordNum=mapZj.get("recordNum")==null?0:((Long)mapZj.get("recordNum")).intValue();
					}else{
						//用存过生成托收记录 
						java.sql.Date endDate=new java.sql.Date(serviceEDate.getTime());
						long result=paytxtService.addPaytxts(subco.getSubcoNo(), agency, fileName, Long.valueOf(userId), endDate);
						mapZj=paytxtService.getZjPaytxts(map);
						recordNum=mapZj.get("recordNum")==null?0:((Long)mapZj.get("recordNum")).intValue();
					}
					//头记录
					out.println(FormatUtil.rightAppendBlank(SystemConst.SERVICE_FEE, 20,FormatUtil.GBK));
					
					//查询需要托收的记录
					map.put("txtName", fileName);
					
					freq=recordNum/SystemConst.COLL_MAX_MAX;
					remainder=recordNum%SystemConst.COLL_MAX_MAX;//余数
					if(remainder>0 || recordNum<SystemConst.COLL_MAX_MAX){
						freq++;
					}
					//分页查询需要托收的记录
					for (pageNo = 1; pageNo <= freq; pageNo++) {
						paytxts=paytxtService.findPaytxts(map,pageNo,SystemConst.COLL_MAX_MAX);
						paytxtService.clear();
						if(Utils.isNotNullOrEmpty(paytxts)){
							paytxtsSize=paytxts.size();
							for (int i = 0; i < paytxts.size(); i++) {
								paytxt=paytxts.get(i);
								paytxt.setRecordIndex(i+1);
								out.println(paytxt.gainFileLine2());
							}
						}
					}
					
					//尾记录
					if(Utils.isNullOrEmpty(mapZj.get("feeSum"))){
						mapZj=paytxtService.getZjPaytxts(map);
					}
					if(Utils.isNotNullOrEmpty(mapZj) && Utils.isNotNullOrEmpty(mapZj.get("recordNum")) && Utils.isNotNullOrEmpty(mapZj.get("feeSum"))){
						StringBuffer sb=new StringBuffer();
						//总金额 9999999999999；等于交易明细文件中的金额总和。不足左补0
						money=mapZj.get("feeSum").toString();
						if(Validation.isNumericalString(money)){
							money = FormatUtil.getAppointedCurrencyFormat(money,10,2).replaceAll("\\.","");
				        }else{
				    	    money = FormatUtil.getAppointedCurrencyFormat("0",10,2).replaceAll("\\.","");
				        }
						sb.append(money);
						//总户数 不足左补0；必须与交易明细文件中记录数一致。
						sb.append(FormatUtil.leftAppendZero(mapZj.get("recordNum").toString(), 12));
						//成功扣款金额  所有成功扣款记录的扣款金额的和，在请求文件中为全0
						sb.append(FormatUtil.unFormatDec(FormatUtil.getAppointedNumberFormat(0,12,0)));
						//成功扣款笔数  所有成功扣款记录的总数，在请求文件中为全0
						sb.append(FormatUtil.unFormatDec(FormatUtil.getAppointedNumberFormat(0,12,0)));
						out.print(sb.toString());
					}
				}
	        }else{
	        	out.print("下载失败，分公司还未设置银行账号信息!");
	        }
			
			out.flush();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			flag = false;
			msg = SystemConst.OP_FAILURE;
			e.printStackTrace();
		} finally {
			try {
				if(out!=null){
					out.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("下载托收文件 结束");
		}
		return resultMap;
	}
}
