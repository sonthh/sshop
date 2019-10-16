package com.itsontran.constant;

public enum MessageEnum {
	MSG_ADD_SUCCESS("Thực hiện thêm thành công"),
	MSG_ERROR("Có lỗi trong quá trình xử lý"),
	MSG_UPDATE_SUCCESS("Thực hiện sửa thành công"),
	MSG_DELETE_SUCCESS("Thực hiện xóa thành công"),
	MSG_DELETE_DENIED("Không xóa được tài khoản này");
	
	private final String status;
	
	MessageEnum(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
