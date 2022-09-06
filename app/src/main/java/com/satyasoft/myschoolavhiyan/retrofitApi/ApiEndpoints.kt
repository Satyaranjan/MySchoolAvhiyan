package com.satyasoft.myschoolavhiyan.retrofitApi

class ApiEndpoints {
   companion object{
       const val API_BASE_URL = "https://beta.trashless.com/"
       const val GET_ORDERS = "orders/getOrders?"
       const val LOGIN = "orders/getUserInfoById?"
       const val UPDATE_DELIVERY_STATUS = "orders/updateDeliveryStatus?"
       const val CONFIRM_DELIVERY_STATUS = "orders/confirmDeliveryStatus?"
       const val COUNT_DELIVERY_STATUS = "orders/getDeliveryCountByStatus?"
   }
}