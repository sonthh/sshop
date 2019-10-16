<%@page import="com.itsontran.constant.MessageEnum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/templates/taglib.jsp" %>    
<script type="text/javascript">
document.getElementById('order').classList.add('active');
</script>  
        <div class="page-head">
          <h2 class="page-head-title">Order</h2>
          <nav aria-label="breadcrumb" role="navigation">
            <ol class="breadcrumb page-head-nav">
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin">Admin</a></li>
              <li class="breadcrumb-item active">Order</li>
            </ol>
          </nav>
        </div>
        <div class="main-content container-fluid">
      	  <c:if test="${not empty  eMsg}">
            <c:set var="alertClass" value="" />
            <c:set var="alertMessage" value="" />
            <!-- START với mỗi loại thông báo edit, add, delete thì có mỗi màu sắc cho alert riêng -->
      		<c:choose>
      	      <c:when test="${eMsg eq MessageEnum.MSG_ADD_SUCCESS}">
                <c:set var="alertClass" value="primary" />
                <c:set var="alertMessage" value="Success" />
      	      </c:when>
      	      <c:when test="${eMsg eq MessageEnum.MSG_UPDATE_SUCCESS}">
                <c:set var="alertClass" value="success" />
                <c:set var="alertMessage" value="Success" />
      	      </c:when>
      	      <c:when test="${eMsg eq MessageEnum.MSG_DELETE_SUCCESS}">
                <c:set var="alertClass" value="warning" />
                <c:set var="alertMessage" value="Success" />
      	      </c:when>
      	      <c:when test="${eMsg eq MessageEnum.MSG_ERROR}">
                <c:set var="alertClass" value="danger" />
                <c:set var="alertMessage" value="Error" />
      	      </c:when>
      		  <c:otherwise>
      		  </c:otherwise>
      		</c:choose>
            <!-- END với mỗi loại thông báo edit, add, delete thì có mỗi màu sắc cho alert riêng -->
            
            <!-- START alert message when add, edit, delete success -->          
  		    <div class="row">
  		      <div class="col-sm-12">
  		        <div class="alert alert-${alertClass} alert-icon alert-icon-border alert-dismissible" role="alert">
                  <div class="icon"><span class="mdi mdi-check"></span></div>
                  <div class="message">
                    <button class="close" type="button" data-dismiss="alert" aria-label="Close">
                      <span class="mdi mdi-close" aria-hidden="true"></span>
                    </button>
                    <strong>${alertMessage}! </strong>${eMsg.status } 
                  </div>
                </div>
  		      </div>	
  		    </div>
            <!-- END alert message when add, edit, delete success -->     
      	  </c:if>
		  <div class="row mb-1">
		    <div class="col-sm-6">
	       	  <button id="deleteSelected" class="btn btn-space btn-danger"><i class="icon icon-left mdi mdi-delete"></i>Delete</button>
            </div>
            <div class="col-sm-6">
                <form class="row" action="${pageContext.request.contextPath}/admin/order/search" method="get">
                  <div class="col-8">
                    <input class="form-control form-control-sm" style="height: 31px;" name="name" id="" type="text" placeholder="Search">
                  </div>
                  <div class="col-2">
                    <button class="btn btn-space btn-primary" type="submit"><span class="icon icon-left mdi mdi-search"></span>Search</button>
                  </div>
                </form>
            </div>
		  </div>		
          <div class="row">
            <div class="col-sm-12">
              <div class="card card-table">
                <div class="card-header">Order table
                </div>
                <div class="card-body">
                  <div class="table-responsive noSwipe">
                    <table class="table table-striped table-hover table-bordered" style="border: 1px solid #dee2e6;">
                      <thead>
                        <tr>
                          <th style="width:4%;">
                            <label class="custom-control custom-control-sm custom-checkbox">
                              <input id="checkAll" class="custom-control-input" type="checkbox"><span class="custom-control-label"></span>
                            </label>
                          </th>
                          <th style="width:10%;">Id</th>
                          <th style="width:10%;">Fullname</th>
                          <th style="width:10%;">Address</th>
                          <th style="width:10%;">Email</th>
                          <th style="width:10%;">Phone</th>
                          <th style="width:10%;">Message</th>
                          <th style="width:10%;">Amount</th>
                          <th style="width:10%;">Payment</th>
                          <th style="width:10%;">Datecreated</th>
                          <th style="width:10%;">Status</th>
                          <th style="width:10%;">Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:choose>
                          <c:when test="${not empty listOrders}">
                            <c:forEach items="${listOrders}" var="itemOrder">
                            
                              <!-- START Tô màu cho row mà vừa thêm hoặc vừa sửa -->
                              <c:set var="rowClass" value="" />
                              <c:choose>
                                <c:when test="${itemOrder.id == objOrder.id}">
                                  <c:set var="rowClass" value="table-warning" />
                                </c:when>
                                <c:otherwise>
                                  <c:set var="rowClass" value="" />
                                </c:otherwise>
                              </c:choose>
                              <!-- END Tô màu cho row mà vừa thêm hoặc vừa sửa -->
                              
                              <tr class="${rowClass}">
                                <td>
                                  <label class="custom-control custom-control-sm custom-checkbox">
                                    <input value="${itemOrder.id}" name="aRow" class="custom-control-input" type="checkbox"><span class="custom-control-label"></span>
                                  </label>
                                </td>
                                <td class="cell-detail"><span>${itemOrder.id}</span></td>
                                <td class="cell-detail"><span>${itemOrder.fullname}</span></td>
                                <td class="cell-detail"><span>${itemOrder.address}</span></td>
                                <td class="cell-detail"><span>${itemOrder.email}</span></td>
                                <td class="cell-detail"><span>${itemOrder.phone}</span></td>
                                <td class="cell-detail"><span>${itemOrder.message}</span></td>
                                <td class="cell-detail">
                                  <span>
                                    <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                                     value="${itemOrder.amount}" />
                                  </span>
                                </td>
                                <td class="cell-detail"><span>${itemOrder.payment}</span></td>
                                <td class="cell-detail"><span>${itemOrder.dateCreated}</span></td>
                                
                                <c:forEach items="${listOrderStatus}" var="itemOrderStatus">
                                    <c:if test="${itemOrderStatus.id == itemOrder.status}">
                                      <td class="cell-detail"><span>${itemOrderStatus.name}</span></td>
                                    </c:if>
                                </c:forEach>
                                
                                <td class="text-right">
                                  <a href="${pageContext.request.contextPath}/admin/order/orderitem/${itemOrder.id}-${page}"><button class="btn btn-space btn-success"><i class="icon icon-left mdi mdi-edit"></i>Items</button></a>
                                  <a href="${pageContext.request.contextPath}/admin/order/delete/${itemOrder.id}" onclick="return confirm('Bạn có muốn xóa order của ${itemOrder.fullname}')"><button class="btn btn-space btn-danger"><i class="icon icon-left mdi mdi-delete"></i>Delete</button></a>
                                </td>
                              </tr>
                            </c:forEach>
                          </c:when>
                          <c:otherwise>
                            <td class="cell-detail text-center" colspan="11"><span>Không order nào</span></td>
                          </c:otherwise>
                        </c:choose>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
		    <div class="col-sm-12 d-sm-flex justify-content-sm-center">
                <ul class="pagination">
                <c:if test="${not empty paginations}">
                
                  <c:choose>
                    <c:when test="${page == 1}">
                      <li class="page-item disabled"><a class="page-link" href="" aria-label="Previous"><span class="mdi mdi-chevron-left" aria-hidden="true"></span></a></li>
                    </c:when>
                    <c:otherwise>
                      <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/admin/order/page-${page - 1}" aria-label="Previous"><span class="mdi mdi-chevron-left" aria-hidden="true"></span></a></li>
                    </c:otherwise>
                  </c:choose>
                
                  <c:forEach items="${paginations }" var="itemPagination">
                    <c:choose>
                      <c:when test="${itemPagination == page }">
                        <li class="page-item active"><a class="page-link" href="${pageContext.request.contextPath}/admin/order/page-${itemPagination}">${itemPagination}</a></li>
                      </c:when>
                      <c:when test="${itemPagination == -1 }">
                        <li class="page-item disabled"><a class="page-link " href="">...</a></li>
                      </c:when>
                      <c:when test="${itemPagination == -2 }">
                        <li class="page-item disabled"><a class="page-link " href="javascript:void(0)">...</a></li>
                      </c:when>
                      <c:otherwise>
                        <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/admin/order/page-${itemPagination}">${itemPagination}</a></li>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                 
                  <c:choose>
                    <c:when test="${page == numberOfPages}">
                      <li class="page-item disabled"><a class="page-link" href="" aria-label="Next"><span class="mdi mdi-chevron-right" aria-hidden="true"></span></a></li>
                    </c:when>
                    <c:otherwise>
                      <li class="page-item"><a class="page-link" href="${pageContext.request.contextPath}/admin/order/page-${page + 1}" aria-label="Next"><span class="mdi mdi-chevron-right" aria-hidden="true"></span></a></li>
                    </c:otherwise>
                  </c:choose>
                  
                 </c:if>
                </ul>
            </div>
		  </div>
        </div>
