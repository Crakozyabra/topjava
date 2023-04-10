<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<script type="text/javascript">
  const i18n = [];
  <c:forTokens var="key" delims=", " items="common.deleted, common.saved, common.enabled, common.disabled, common.errorStatus, common.confirm">
    i18n["${key}"] = "<spring:message code="${key}"/>";
  </c:forTokens>
  i18n["addTitle"] = "<spring:message code="${param.source}.add"/>";
  i18n["editTitle"] = "<spring:message code="${param.source}.edit"/>";
</script>