<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeProveedorDetalle.lista.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeProveedor" />
        </jsp:include>

        <h1><s:message code="informeProveedorDetalle.lista.label" /></h1>
        <hr/>

        <form name="filtraLista" class="form-search" method="post" action="<c:url value='/factura/informeProveedorDetalle' />">
            <input type="hidden" name="pagina" id="pagina" value="${pagina}" />
            <input type="hidden" name="tipo" id="tipo" value="" />
            <input type="hidden" name="correo" id="correo" value="" />
            <input type="hidden" name="order" id="order" value="${param.order}" />
            <input type="hidden" name="sort" id="sort" value="${param.sort}" />
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/factura/informeProveedorDetalle/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='informeProveedorDetalle.nuevo.label' /></a>
                <input name="filtro" type="text" class="input-medium search-query" value="${param.filtro}">

                <button type="submit" class="btn"><i class="icon-search"></i> <s:message code="buscar.label" /></button>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                            <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>
            <c:if test="${informesProveedorDetalle != null}">
                <s:bind path="informesProveedorDetalle.*">
                    <c:if test="${not empty status.errorMessages}">
                        <div class="alert alert-block alert-error fade in" role="status">
                            <a class="close" data-dismiss="alert">×</a>
                            <c:forEach var="error" items="${status.errorMessages}">
                                <c:out value="${error}" escapeXml="false"/><br />
                            </c:forEach>
                        </div>
                    </c:if>
                </s:bind>
            </c:if>

            <c:url value="/factura/informeProveedor/finaliza" var="linky">
                <c:param name="id" value="${informeId.id}"/>
            </c:url>
            <a class="btn btn-primary" href="<c:out value='${linky}'/>"><i class="icon-user icon-white"></i> <s:message code='finaliza.label' /></a>
            <%--
            <a  href='<c:out value="${linky}"/>'>x</a>
            --%>


            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="folio" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="proveedor" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="rfc" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="iva" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="subtotal" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="total" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="dctoProntoPago" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="status" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="fechaFactura" />
                        </jsp:include>
                        <jsp:include page="/WEB-INF/jsp/columnaOrdenada.jsp" >
                            <jsp:param name="columna" value="informeProveedor" />
                        </jsp:include>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${informesProveedorDetalle}" var="informeProveedorDetalle" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/factura/informeProveedorDetalle/ver/${informeProveedorDetalle.id}' />">${informeProveedorDetalle.folioFactura}</a></td>                            
                            <td>${informeProveedorDetalle.nombreProveedor}</td>
                            <td>${informeProveedorDetalle.RFCProveedor}</td>
                            <td>${informeProveedorDetalle.IVA}</td>
                            <td>${informeProveedorDetalle.subtotal}</td>
                            <td>${informeProveedorDetalle.total}</td>
                            <td>${informeProveedorDetalle.dctoProntoPago}</td>
                            <td>${informeProveedorDetalle.statusTexto}</td>
                            <td>${informeProveedorDetalle.fechaFactura}</td>
                            <td>${informeProveedorDetalle.informeProveedor.id}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <jsp:include page="/WEB-INF/jsp/paginacion.jsp" />
        </form>        
    <content>
        <script src="<c:url value='/js/lista.js' />"></script>
    </content>
</body>
</html>
