<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/templates/taglib.jsp" %>    
  <section id="slider">
    <!--slider-->
    <div class="container">
      <div class="row">
        <div class="col-sm-12">
          <div id="slider-carousel" class="carousel slide" data-ride="carousel">
            <ol class="carousel-indicators">
              <c:forEach items="${listTopSlides}" var="itemSlide" varStatus="count">
                <c:set var="cssClass" value="" />
                <c:if test="${count.index == 0}">
                  <c:set var="cssClass" value="active" />
                </c:if>
                  
                <li data-target="#slider-carousel" data-slide-to="${count.index}" class="${cssClass}"></li>
              </c:forEach>
            </ol>
            <div class="carousel-inner">
              <c:forEach items="${listTopSlides}" var="itemSlide" varStatus="count">
              
                <c:set var="cssClass" value="" />
                <c:if test="${count.index == 0}">
                  <c:set var="cssClass" value="active" />
                </c:if>
              
                <div class="item ${cssClass}">
                  <div class="col-sm-6">
                    <h1>S-SHOP</h1>
                    <h2>${itemSlide.name}</h2>
                    <p>${itemSlide.description}</p>
                    <a class="btn btn-default get" href="${itemSlide.link}">Get it now</a>
                  </div>
                  <div class="col-sm-6">
                    <img style="width: 484 px; height: 441px" src="${pageContext.request.contextPath}/files/${itemSlide.image}"
                     class="girl img-responsive" alt="" />
                  </div>
                </div>
              </c:forEach>
              
            </div>
            <a href="#slider-carousel" class="left control-carousel hidden-xs" data-slide="prev"><i class="fa fa-angle-left"></i></a>
            <a href="#slider-carousel" class="right control-carousel hidden-xs" data-slide="next"><i class="fa fa-angle-right"></i></a>
          </div>
        </div>
      </div>
    </div>
  </section>
  <!--/slider-->