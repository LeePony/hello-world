<%@ tag pageEncoding="UTF-8" isELIgnored="false"
	import="com.ztesoft.zsmart.web.resource.Common"
	import="java.text.MessageFormat"%>
<%@ attribute name = "key" required="true" %>
<%@ attribute name = "param" %>
<%@ attribute name = "separator" %>
<%
	String value = Common.getResourceStringLogger(Common.getJScriptRes(key));
	if(param != null && !"".equals(param.trim()) ){
		if(separator !=null && !"".equals(separator.trim()) ){
			String [] arr = param.trim().split(separator.trim());
			value = MessageFormat.format(value, arr);
		}else{
			value = MessageFormat.format(value, param);
		}
	}
	out.print(value);
%>
