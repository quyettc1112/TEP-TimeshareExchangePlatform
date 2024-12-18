package com.example.tep_timeshareexchangeplatform.BaseModel.Respone.FAQ


import com.google.gson.annotations.SerializedName

/**
[
  {
    "faqId": 4,
    "type": "general",
    "title": "What is the cost of membership and postings?",
    "description": "Membership is currently $18.99 for 12 months and is required to add a rental or resale, or to contact an owner about a posting. Rental postings start at $39.99 for 6 months, and resale postings start at $60 for 12 months. A full list of pricing and benefits can be found on our pricing and details page.",
    "createdDate": 1728504421000
  },
  {
    "faqId": 5,
    "type": "general",
    "title": "What is RedWeek Verified or Verified & Prote",
    "description": "The RedWeek Verified flag means we have independently confirmed key details of the posting so you can feel confident in your transaction. Everything you see in the green box on a posting page has been professionally verified. Verified & Protected means that the posting has been verified, and it also offers secure online booking.",
    "createdDate": 1728480067000
  },
  {
    "faqId": 6,
    "type": "general",
    "title": "What to do if I've forgotten my password????",
    "description": "Membership is currently $19 for 12 months and is required to add a rental or resale, or to contact an owner about a posting. Rental postings start at $39.99 for 6 months, and resale postings start at $59.99 for 12 months. A full list of pricing and benefits can be found on our pricing and details page.",
    "createdDate": 1728506595000
  },
  {
    "faqId": 7,
    "type": "general",
    "title": "Are nightly rentals available?",
    "description": "The majority of the rentals on RedWeek.com are posted directly by timeshare owners who have purchased a weekly interval.",
    "createdDate": 1733052481000
  },
  {
    "faqId": 8,
    "type": "general",
    "title": "What currency is used for posted prices?",
    "description": "We ask our members to post all prices in $USD, but you should always double check when making an inquiry.",
    "createdDate": 1733052483000
  },
  {
    "faqId": 9,
    "type": "general",
    "title": "How do I inquire about a posting?",
    "description": "You will need to contact the owner of the posting directly. You can send an email inquiry by choosing the 'Ask a question' option on any posting page.",
    "createdDate": 1733052784000
  },
  {
    "faqId": 10,
    "type": "general",
    "title": "How do I inquire about a posting?",
    "description": "You will need to contact the owner of the posting directly. You can send an email inquiry by choosing the Ask a question option on any posting page.",
    "createdDate": 1733052838000
  },
  {
    "faqId": 11,
    "type": "general",
    "title": "Do you have a rental agreement?",
    "description": "Yes! All online bookings come with our custom-built rental agreement with protections for both parties. Renters will agree to these terms hen submitting their booking request. If you choose not to use our online booking, we highly recommend having some kind of written and signed rental agreement.",
    "createdDate": 1733113043000
  },
  {
    "faqId": 12,
    "type": "general",
    "title": "What to do if I've forgotten my password?",
    "description": "If you've lost or forgotten your RedWeek password, simply go to the sign in page and click on the \"Forgot Password?\" link next to the password field. By entering the email address associated with your RedWeek account, you will be sent an email with instructions on how to reset your password.\n\nIf you wish to change your password to something easier to remember, please sign in to your account, click \"My Account\" from the top of any page, and choose \"Profile & Password\" under the \"Account Details\" section to enter a new password of your choice.",
    "createdDate": 1733113085000
  }
]
*/
class FAQResponse : ArrayList<FAQResponse.FAQResponseItem>(){
    data class FAQResponseItem(
      @SerializedName("faqId") val faqId: Int,
      @SerializedName("type") val type: String,
      @SerializedName("title") val title: String,
      @SerializedName("description") val description: String,
      @SerializedName("createdDate") val createdDate: Long,
      var isExpandable: Boolean = false
    )
}