package com.chinagps.fee.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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

/**
 * @Package:com.chinagps.fee.controller
 * @ClassName:UploadPayTxtController
 * @Description:上传银行托收文件
 * @author:zfy
 * @date:2014-6-3 上午9:40:56
 */
@Controller
@RequestMapping(value = "/pay")
public class UploadPayTxtController extends BaseController{
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(UploadPayTxtController.class);

	@Autowired
	@Qualifier("paytxtService")
	private PaytxtService paytxtService;
	
	@Autowired
	@Qualifier("subcoService")
	private SubcoService subcoService; 

	@RequestMapping(value = "uploadPaytxtFile1")
	@LogOperation(description = "上传银盛、金融中心托收文件", op_type = SystemConst.OPERATELOG_ADD, model_id = 30001)
	public @ResponseBody
	void uploadPaytxtFile1(
			@RequestParam("paytxtFile") List<MultipartFile> paytxtFiles,
			@RequestParam("agency") int agency,
			HttpServletRequest request,
			HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("上传金融中心、银盛托收文件 开始");
		}
		//HashMap<String, Object> resultMap = new HashMap<String, Object>();
		try {
			paytxtService.uploadPaytxt1(paytxtFiles, agency, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			OutputStream out = null;
			try {
				 out = response.getOutputStream();
				String str = "<script>parent.callback('"+e.getMessage()+"','false');</script>";
				out.write(str.getBytes("UTF-8"));
				out.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				 if(out != null){
		                try{out.close();}catch(Exception e1){
		                	e1.printStackTrace();
		                }
		             }
			}
		}
		/*resultMap.put(SystemConst.SUCCESS, flag);
		resultMap.put(SystemConst.MSG, msg);*/
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("上传金融中心、银盛托收文件 结束");
		}
		//return resultMap;
	}
	
	@RequestMapping(value = "uploadPaytxtFile2")
	@LogOperation(description = "上传中国银联托收文件", op_type = SystemConst.OPERATELOG_ADD, model_id = 30003)
	public @ResponseBody
	void uploadPaytxtFile2(
			@RequestParam("paytxtFile") List<MultipartFile> paytxtFiles,
			HttpServletRequest request,
			HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("上传中国银联托收文件 开始");
		}
		try {
			paytxtService.uploadPaytxt2(paytxtFiles, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			OutputStream out = null;
			try {
				 out = response.getOutputStream();
				String str = "<script>parent.callback('"+e.getMessage()+"','false');</script>";
				out.write(str.getBytes("UTF-8"));
				out.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally{
				 if(out != null){
		                try{out.close();}catch(Exception e1){
		                	e1.printStackTrace();
		                }
		             }
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("上传中国银联托收文件 结束");
		}
		//return resultMap;
	}
}
