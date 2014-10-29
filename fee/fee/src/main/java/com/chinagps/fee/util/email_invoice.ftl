<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>赛格导航计费托收电子对账单</title>
</head>
<body style="font: 12px/1.5 tahoma, arial, \5b8b\4f53; color:#000000;">
<table width="100%" border="0" align="center" cellpadding="2" cellspacing="6" >
    <tr>
        <td width="100%" align="center">
        <strong>${companyName}</strong>
          </td>
        
    </tr>
    <tr>                     
         <td align="left" valign=top colspan=2>
         <hr align="left"  width="95%" size="1" noshade color="#cc0000" >
         </td>
    </tr>
    <tr>                     
      <td colspan=2>
      <span style='font-weight:bold;'>尊敬的 ${customerName} 用户 您好!</span>
    <br/>
    感谢您使用赛格导航终端！
   </td>
    </tr>
</table> 
          

           
<table width="95%" border="1" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
<tr>
<td colspan="3" style="background-color: grey;font-weight: bold">
本 期 交 易 汇 总 (人民币)
</td>
</tr>
<tr>
<td colspan="1">
银行账号:${acNo} 
</td>
<td >
扣费总额(元):${realAmount} 
</td>
</tr>

 <tr>                     
         <td align="left" valign=top colspan=3>
         <hr align="left"  width="100%" size="1" noshade color="#cc0000" >
         </td>
    </tr>
<td colspan="3" style="background-color: grey;font-weight: bold">
车 载 终 端 扣 费 明 细
</td>
</tr>
<#list unitList as unit>
<tr>
<td>
车载号码：${unit.callLetter} 
</td>
<td>
扣费周期：${unit.sdate} 至  ${unit.edate} 
</td>
<td>
扣费金额(元)：${unit.realAmount} 
</td>
</tr>
<tr>
<td colspan="1" height="100px" style="background-color: #eee;font-weight: bold">
项 目 明 细
</td>
<td colspan="2">
<div style="margin-top: -60px;">
  <ul style="height:20px;width:400px;list-style-type:none;">
    <li style="font-weight: bold;background-color: #eee;width:120px;float:left;border:1px solid #eee;">项目</li>
    <li style="font-weight: bold;background-color: #eee;width:120px;float:left;border:1px solid #eee;">金额</li>
    <li style="font-weight: bold;background-color: #eee;width:120px;float:left;border:1px solid #eee;">扣款日期</li>
    
    <#list unit.itemList as detailItem>
       <li style="width:120px;float:left;border:1px solid #eee;">${detailItem.itemName}</li>
       <li style="width:120px;float:left;border:1px solid #eee;">${detailItem.itemMoney}</li>
       <li style="width:120px;float:left;border:1px solid #eee;">${detailItem.payDate}</li>
   	</#list>
  </ul>
</div>
</td>
</tr>
</#list>
</table>

</body>
</html>