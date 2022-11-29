package com.isut.customer.apiclient;




import com.isut.customer.model.Booking.BookingModel;
import com.isut.customer.model.UpdateResponse;
import com.isut.customer.model.bookingNoti.NotiBookingResponse;
import com.isut.customer.model.cabd.TaxyModel;
import com.isut.customer.model.cabs.CabsModel;
import com.isut.customer.model.login.LoginModel;
import com.isut.customer.model.promo.PromoResponse;
import com.isut.customer.model.register.UserModel;
import com.isut.customer.model.vlaid.ValidResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {


    @POST("customer/add")
    Call<UserModel> signup(@Body RequestBody  jsonObject);
   @POST("customer/refer")
    Call<UpdateResponse> referFriend(@Header("Authorization") String token, @Query("email") String email);

   @POST("promoCode/isValid")
    Call<PromoResponse> applyCouponCode(@Header("Authorization") String token, @Query("promoCode") String promoCode);

    @POST("account/validator")
    Call<ValidResponse> validator(@Header("Authorization") String token);

    @POST("auth/login")
    Call<LoginModel> login(@Body RequestBody  jsonObject);
  @POST("rateUs/add")
    Call<LoginModel> rateus(@Header("Authorization") String token,@Body RequestBody  jsonObject);

    @POST("customer/update")
    Call<UpdateResponse> update(@Header("Authorization") String token, @Body RequestBody  jsonObject);
  @POST("tip/add")
    Call<UpdateResponse> tipAdd(@Header("Authorization") String token, @Body RequestBody  jsonObject);
 @POST("rating/add")
    Call<UpdateResponse> ratingAdd(@Header("Authorization") String token, @Body RequestBody  jsonObject);

    @POST("forgotPassword")
    Call<UpdateResponse> forgotpassword(@Header("Authorization") String token, @Body RequestBody  jsonObject);

  @POST("booking/add")
    Call<BookingModel> booking(@Header("Authorization") String token, @Body RequestBody  jsonObject);
  @POST("booking/schedule")
    Call<BookingModel> schedulebooking(@Header("Authorization") String token, @Body RequestBody  jsonObject);

  @POST("booking/update/desinationlocation")
    Call<BookingModel> updatebooking(@Header("Authorization") String token, @Body RequestBody  jsonObject);


@GET
Call<TaxyModel> getdriver(@Header("Authorization")
                                  String token,@Url String url);
@GET
Call<TaxyModel> getCabByType(@Header("Authorization")
                                  String token,@Url String url);


  /*  Call<TaxyModel> getdriver(@Header("Authorization")
                                      String token,
                              @Query("role") String role);
*/

    @GET
    Call<CabsModel> gercablist(@Header("Authorization") String token, @Url String url);
 @DELETE
    Call<UpdateResponse> deleteAccount(@Header("Authorization") String token, @Url String url);

    @GET
    Call<ResponseBody> geimgpath(@Header("Authorization")
                                         String token,
                                 @Url String url);
    @POST
    Call<NotiBookingResponse> cabstatus(@Header("Authorization") String token,
                                        @Url String url);
//remark accept reject
}

