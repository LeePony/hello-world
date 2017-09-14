<%@ tag pageEncoding="UTF-8" isELIgnored="false" 
 import="com.ztesoft.zsmart.core.utils.StringUtil"
%>
<%
	String webRoot = "";
	String scheme = request.getScheme();
	String serverName = request.getServerName();
	int port = request.getServerPort();
	String contextPath = request.getContextPath();

	
	String domain = "";
	if (StringUtil.isNotEmpty(domain)) {
        if (serverName.indexOf(domain) > -1) {
            webRoot = "https";
        }
        else {
            webRoot = scheme;
        }
    }
    else {
        webRoot = scheme;
    }
	
	webRoot += "://";
	webRoot += serverName;

	if (port != 80) {
		webRoot += ":" + port;
	}
	webRoot += contextPath + "/";
	out.print(webRoot);
%>