<%@page import="com.itsontran.constant.MessageEnum"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/templates/taglib.jsp" %>  
<script type="text/javascript">
document.getElementById('slide').classList.add('active');
</script> 
        <div class="page-head">
          <h2 class="page-head-title">Slide</h2>
          <nav aria-label="breadcrumb" role="navigation">
            <ol class="breadcrumb page-head-nav">
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin">Admin</a></li>
              <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/admin/slide">Slide</a></li>
              <li class="breadcrumb-item active">Edit</li>
            </ol>
          </nav>
        </div>
        <div class="main-content container-fluid">
        
      	  <c:if test="${not empty  eMsg}">
            <c:set var="alertClass" value="" />
            <c:set var="alertMessage" value="" />
            <!-- START với mỗi loại thông báo edit, add, delete thì có mỗi màu sắc cho alert riêng -->
      		<c:choose>
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
        
          <div class="row">
            <div class="col-md-12">
              <div class="card card-border-color card-border-color-success">
                <div class="card-header card-header-divider">Sửa slide<span class="card-subtitle">Sửa slide hiển thị ở trang chủ</span></div>
                <div class="card-body">
                  <form action="" method="post" enctype="multipart/form-data">
                  
                    <div class="form-group row">
                      <label class="col-12 col-sm-2 col-form-label text-sm-right" for="link">Link</label>
                      <div class="col-12 col-sm-10 col-lg-10">
                        <input class="form-control" id="link" name="link" value="${objSlide.link}" type="text" placeholder="Link">
                        <form:errors path="objSlide.link" cssClass="parsley-errors-list filled d-block mt-2" />
                      </div>
                    </div>
                    
                    <div class="form-group row">
                      <label class="col-12 col-sm-2 col-form-label text-sm-right" for="name">Name</label>
                      <div class="col-12 col-sm-10 col-lg-10">
                        <input class="form-control" id="name" name="name" value="${objSlide.name}" type="text" placeholder="Name">
                        <form:errors path="objSlide.name" cssClass="parsley-errors-list filled d-block mt-2" />
                      </div>
                    </div>
                    
                    <div class="form-group row">
                      <label class="col-12 col-sm-2 col-form-label text-sm-right" for="description">Description</label>
                      <div class="col-12 col-sm-10 col-lg-10">
                        <textarea class="form-control" id="description" name="description">${objSlide.description}</textarea>
                        <form:errors path="objSlide.description" cssClass="parsley-errors-list filled d-block mt-2" />
                      </div>
                    </div>
                    
                    <div class="form-group row">
                      <label class="col-12 col-sm-2 col-form-label text-sm-right" for="sort">Sort</label>
                      <div class="col-12 col-sm-10 col-lg-10">
                        <input class="form-control" id="sort" name="sort" value="${objSlide.sort}" type="number" min="1" placeholder="Sort">
                        <form:errors path="objSlide.sort" cssClass="parsley-errors-list filled d-block mt-2" />
                      </div>
                    </div>
                    
                    <div class="form-group row">
                      <label class="col-12 col-sm-2 col-form-label text-sm-right" for="picture">Image</label>
                      <div class="col-12 col-sm-10">
                        <input onchange="changeInput(this)" accept="image/gif, image/jpeg, image/png" class="inputfile" id="picture" 
                        type="file" name="picture" data-multiple-caption="{count} files selected" >
                        <label class="btn-secondary" for="picture"> <i class="mdi mdi-upload"></i><span>Browse image...</span></label>
                        <form:errors path="objSlide.image" cssClass="parsley-errors-list filled d-block mt-2" />
                      </div>
                    </div>
                    
                    <div class="form-group row" id="oldImage">
                      <div class="col-12 col-sm-2"></div>
                      <div class="col-12 col-sm-10">
                      	<div class="row">
                      	  <div class="col-12 col-sm-12" id="">
                            <div>Ảnh cũ.</div>
                            <img class="w-25 img-thumbnail rounded" style="height: 200px" src="${pageContext.request.contextPath}/files/${objSlide.image}" />
                      	  </div>
                      	</div>
                      </div>
                    </div>
                    
                    <div class="form-group row d-none" id="filePreview">
                      <div class="col-12 col-sm-2"></div>
                      <div class="col-12 col-sm-10">
                      	<div class="row">
                      	  <div class="col-12 col-sm-12" id="imagesPreview">
                      	  </div>
                      	</div>
                      </div>
                    </div>
                    <script type="text/javascript">
                    	function readURL(input) {
                   			if (input.files && input.files[0]) {
                   		    	var reader = new FileReader();
                   		    	reader.onload = function(e) {
                   		    		var img = $("<img class='w-25 img-thumbnail rounded' style='height: 200px' />");
                                    img.attr('src', event.target.result);
                                    img.appendTo($('#imagesPreview'));
                   		    	}
                   		    	reader.readAsDataURL(input.files[0]);
                   		  	}
                   		}
                    	function changeInput(input) {
                    		$('#filePreview').removeClass('d-none');
                    		$('#imagesPreview').html('<div>Ảnh mới.</div>');
                    		readURL(input);
                    	}
                    </script>
                    
                    <div class="row pt-3">
                      <div class="col-sm-12">
                        <p class="text-right">
                          <button class="btn btn-space btn-success" type="submit"><i class="icon icon-left mdi mdi-edit"></i> Edit</button>
                        </p>
                      </div>
                    </div>
                    
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
     