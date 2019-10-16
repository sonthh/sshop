<%@page import="com.itsontran.constant.MessageEnum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/templates/taglib.jsp" %>    
<script type="text/javascript">
document.getElementById('statistics').classList.add('active');
</script>  
        <div class="page-head">
          <h2 class="page-head-title">Statistics</h2>
          <nav aria-label="breadcrumb" role="navigation">
            <ol class="breadcrumb page-head-nav">
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin">Admin</a></li>
              <li class="breadcrumb-item active">Statistics</li>
            </ol>
          </nav>
        </div>
        <div class="main-content container-fluid">
		  <div class="row mb-1">
            <div class="col-sm-12">
              <form class="row" action="" method="post">
                <div class="col-4">
                  <div class="form-group row">
                    <label class="col-12 col-sm-3 col-form-label text-sm-right"> From </label>
                    <div class="col-12 col-sm-7 col-md-5 col-lg-4 col-xl-9">
                      <div class="input-group date datetimepicker" data-min-view="2" data-date-format="yyyy-mm-dd">
                        <input readonly="readonly" name="startDay" id="startDay" class="form-control form-control-sm" size="16" type="text" value="${startDay}">
                        <div class="input-group-append">
                          <button type="button" class="btn btn-primary"><i class="icon-th mdi mdi-calendar"></i></button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-4">
                  <div class="form-group row">
                    <label class="col-12 col-sm-3 col-form-label text-sm-right"> To </label>
                    <div class="col-12 col-sm-7 col-md-5 col-lg-4 col-xl-9">
                      <div class="input-group date datetimepicker" data-min-view="2" data-date-format="yyyy-mm-dd">
                        <input readonly="readonly" name="endDay" id="endDay" class="form-control form-control-sm" size="16" type="text" value="${endDay}">
                        <div class="input-group-append">
                          <button type="button" class="btn btn-primary"><i class="icon-th mdi mdi-calendar"></i></button>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col-2 form-group pt-3">
                  <button class="btn btn-space btn-primary btn-lg" type="submit"><span class="icon icon-left mdi mdi-book"></span>Cal</button>
                </div>
              </form>
            </div>
		  </div>	
      
          <div class="row">
            <div class="col-12 col-lg-6 col-xl-3">
              <div class="widget widget-tile">
                <i class="icon mdi mdi-shopping-cart text-primary" style="font-size: 40px"></i>
                <div class="data-info">
                  <div class="desc">Số lượng Order.</div>
                  <div class="value"><span class="number" data-toggle="counter" data-end="">${countOrder}</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-12 col-lg-6 col-xl-5">
              <div class="widget widget-tile">
                <i class="icon mdi mdi-shopping-basket text-danger" style="font-size: 40px"></i>
                <div class="data-info">
                  <div class="desc">Số loại sản phẩm bán ra.</div>
                  <div class="value"><span class="number" data-toggle="counter" data-end="">${countProductType}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="row">
            <div class="col-12 col-lg-6">
              <div class="card">
                <div class="card-header">Tính toán thu nhập.</div>
                <div class="card-body">
                  <ul class="user-timeline user-timeline-compact">
                    <li class="latest">
                      <div class="user-timeline-title">Tổng tiền.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countAllAmount}" /> VND.
                      </div>
                    </li>
                    <li class="latest">
                      <div class="user-timeline-title">Đã nhận.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countReceivedAmount}" /> VND.
                      </div>
                    </li>
                    <li>
                      <div class="user-timeline-title">Đã nhận từ payment.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countReceivedPaymentAmount}" /> VND.
                      </div>
                    </li>
                    <li>
                      <div class="user-timeline-title">Đã nhận từ giao hàng trực tiếp.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countReceivedAmount - countReceivedPaymentAmount}" /> VND.
                      </div>
                    </li>
                    <li class="latest">
                      <div class="user-timeline-title">Chưa nhận.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countAllAmount - countReceivedAmount}" /> VND.
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="col-12 col-lg-6">
              <div class="card">
                <div class="card-header">Thống kê đơn hàng.</div>
                <div class="card-body">
                  <ul class="user-timeline user-timeline-compact">
                    <li class="latest">
                      <div class="user-timeline-title">Hoàn tất.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countCompleteOrder}" /> đơn hàng.
                      </div>
                    </li>
                    <li class="latest">
                      <div class="user-timeline-title">Đang chờ.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countWaitOrder}" /> đơn hàng.
                      </div>
                    </li>
                    <li class="latest">
                      <div class="user-timeline-title">Đã được gởi đi.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countSendOrder}" /> đơn hàng.
                      </div>
                    </li>
                    <li class="latest">
                      <div class="user-timeline-title">Từ chối.</div>
                      <div class="user-timeline-description">
                        <fmt:formatNumber type="number" groupingUsed = "true" maxFractionDigits="3"
                        value="${countRefuseOrder}" /> đơn hàng.
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
      	
        </div>
