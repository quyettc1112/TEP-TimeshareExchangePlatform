package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.Policy


import com.google.gson.annotations.SerializedName

/**
[
  {
    "policyId": 1,
    "type": "general",
    "title": "Chính sách Đặt phòng",
    "description": "Người dùng có thể đặt phòng tại các khu nghỉ dưỡng tham gia chương trình Timeshare Exchange thông qua nền tảng của chúng tôi. Mỗi lần đặt phòng sẽ được tính theo số điểm hoặc thời gian sở hữu của người dùng. Chính sách này yêu cầu người dùng phải hoàn tất thủ tục đặt phòng ít nhất 29 ngày trước khi đến. Các yêu cầu đặc biệt như phòng góc hay giường đôi sẽ phụ thuộc vào sự sẵn có tại thời điểm đặt phòng.",
    "createdDate": 1728799895319
  },
  {
    "policyId": 2,
    "type": "general",
    "title": "Chính sách Đảm bảo Hoàn tiền",
    "description": "Chúng tôi cam kết hoàn tiền cho người dùng nếu họ không hài lòng với trải nghiệm của mình tại khu nghỉ dưỡng. Để yêu cầu hoàn tiền, người dùng cần gửi yêu cầu trong vòng 24 giờ kể từ khi nhận phòng. Phải có lý do rõ ràng và chứng cứ về tình trạng phòng hoặc dịch vụ không đáp ứng yêu cầu của khách hàng.",
    "createdDate": 1733074765095
  },
  {
    "policyId": 3,
    "type": "general",
    "title": "Quyền lợi và Điều kiện Chuyển nhượng",
    "description": "Chính sách chuyển nhượng timeshare cho phép người dùng trao đổi hoặc bán quyền sử dụng timeshare của mình cho người khác. Quy trình này yêu cầu người chuyển nhượng phải thông báo cho nền tảng và cung cấp thông tin về người nhận. Tất cả các giao dịch chuyển nhượng phải được xác nhận và phê duyệt bởi nền tảng để đảm bảo tính hợp pháp và quyền lợi của các bên liên quan.",
    "createdDate": 1733074897226
  },
  {
    "policyId": 4,
    "type": "general",
    "title": "Quyền lợi Đặc biệt cho Thành viên",
    "description": "Thành viên của nền tảng Timeshare Exchange sẽ nhận được các quyền lợi đặc biệt như giảm giá khi đặt phòng, ưu tiên trong việc chọn phòng, và khả năng trao đổi thời gian nghỉ dưỡng dễ dàng hơn. Các quyền lợi này chỉ áp dụng cho những người dùng có ít nhất một năm thành viên và phải được duy trì để tiếp tục nhận ưu đãi.",
    "createdDate": 1733074942013
  },
  {
    "policyId": 5,
    "type": "general",
    "title": "Chính sách bảo mật thông tin",
    "description": "Mọi thông tin cá nhân của khách hàng được cam kết bảo mật tuyệt đối và chỉ sử dụng cho mục đích cung cấp dịch vụ. Chúng tôi không chia sẻ thông tin khách hàng với bất kỳ bên thứ ba nào mà không có sự đồng ý từ khách hàng.\n",
    "createdDate": 1733112321115
  },
  {
    "policyId": 6,
    "type": "general",
    "title": "Chính sách hủy đặt phòng",
    "description": "Khách hàng có thể hủy đặt phòng miễn phí trong vòng 24 giờ kể từ khi đặt. Sau thời gian này, phí hủy sẽ là 50% giá trị đặt phòng. Trường hợp hủy trong vòng 48 giờ trước thời gian nhận phòng, khách hàng không được hoàn tiền.",
    "createdDate": 1733112339391
  }
]
*/
class PolicyResponse : ArrayList<PolicyResponse.PolicyResponseItem>(){
    data class PolicyResponseItem(
        @SerializedName("policyId") val policyId: Int,
        @SerializedName("type") val type: String,
        @SerializedName("title") val title: String,
        @SerializedName("description") val description: String,
        @SerializedName("createdDate") val createdDate: Long
    )
}