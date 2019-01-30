package com.safedoctor.safedoctor.Api;

import com.safedoctor.safedoctor.Model.Aid;
import com.safedoctor.safedoctor.Model.Booking;
import com.safedoctor.safedoctor.Model.Chat;
import com.safedoctor.safedoctor.Model.ChatSessionDataModel;
import com.safedoctor.safedoctor.Model.ConfirmedAppointmentContentModel;
import com.safedoctor.safedoctor.Model.ConsultationPayment;
import com.safedoctor.safedoctor.Model.ConsultationProfile;
import com.safedoctor.safedoctor.Model.HealthPost;
import com.safedoctor.safedoctor.Model.LoginDataModel;
import com.safedoctor.safedoctor.Model.LoginModel;
import com.safedoctor.safedoctor.Model.PatientChangeCredentials;
import com.safedoctor.safedoctor.Model.PatientModel;
import com.safedoctor.safedoctor.Model.PatientPhotoModel;
import com.safedoctor.safedoctor.Model.PatientblooddonordetailModel;
import com.safedoctor.safedoctor.Model.Peripheral;
import com.safedoctor.safedoctor.Model.Picture;
import com.safedoctor.safedoctor.Model.Referral;
import com.safedoctor.safedoctor.Model.RegistrationDataModel;
import com.safedoctor.safedoctor.Model.RegistrationModel;
import com.safedoctor.safedoctor.Model.ResendCodeModel;
import com.safedoctor.safedoctor.Model.ResendPhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.ServiceAvailabilityContentModel;
import com.safedoctor.safedoctor.Model.ServiceContentModel;
import com.safedoctor.safedoctor.Model.ServiceFeeDataModel;
import com.safedoctor.safedoctor.Model.StartRegistrationDataModel;
import com.safedoctor.safedoctor.Model.StartRegistrationModel;
import com.safedoctor.safedoctor.Model.SwagArrayResponseModel;
import com.safedoctor.safedoctor.Model.SwagResponseModel;
import com.safedoctor.safedoctor.Model.ValidatePhoneCodeDataModel;
import com.safedoctor.safedoctor.Model.ValidatePhoneCodeModel;
import com.safedoctor.safedoctor.Model.Vitalsignscapture;
import com.safedoctor.safedoctor.Model.requests.PatientMedicalHistoryIn;
import com.safedoctor.safedoctor.Model.responses.BasicObject;
import com.safedoctor.safedoctor.Model.responses.ChatRoomToken;
import com.safedoctor.safedoctor.Model.responses.DoctorOutObj;
import com.safedoctor.safedoctor.Model.responses.Facilityinfo;
import com.safedoctor.safedoctor.Model.responses.PatientProfileFormDataOut;
import com.safedoctor.safedoctor.Model.responses.Patientcontactperson;
import com.safedoctor.safedoctor.Model.responses.Patientprofilemedicalhistory;
import com.safedoctor.safedoctor.Model.responses.Patientprofilemedication;
import com.safedoctor.safedoctor.Model.responses.Review;
import com.safedoctor.safedoctor.Model.responses.ReviewQuestionsOut;
import com.safedoctor.safedoctor.Model.responses.ReviewStatistics;
import com.safedoctor.safedoctor.Model.responses.Reviewanswer;
import com.safedoctor.safedoctor.Model.responses.Reviewquestion;
import com.safedoctor.safedoctor.Model.responses.Reviewtype;
import com.safedoctor.safedoctor.Model.responses.TimeSlot;
import com.safedoctor.safedoctor.Model.responses.UserAccount;
import com.safedoctor.safedoctor.Model.responses.UserProfile;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface SafeDoctorService {

    @GET("api/library?size=1000&sort=createtime,desc")
    Call<HealthPost> getHealthLibrary();
   // Call<HealthPost> getHealthLibrary(@Header("Authorization") String authHeader);


    @GET("api/firstaid?size=1000&sort=createtime,desc")
    Call<Aid> getFirstAids();
   // Call<List<Aid>> getFirstAids(@Header("bearer-token") String authHeader);

    //to fetch list of services
    @GET("api/patients/{patientid}/services")
    Call<SwagResponseModel<List<ServiceContentModel>>> getServiceResponse(@Header("Authorization") String authHeader, @Path("patientid") int patientid);

    //to fetch list of clinical specialties
    @GET("api/setup/clinicalspecialties")
    Call<SwagResponseModel<List<BasicObject>>> getClinicalSpecialties(@Header("Authorization") String authHeader);

    //list available appointment slots
    @GET("api/appointments/serviceavailability/{serviceid}")
    Call<SwagResponseModel<List<ServiceAvailabilityContentModel>>> getAvailableServices(@Header("Authorization") String authHeader, @Path("serviceid") int serviceid, @QueryMap Map<String, String> query);

    //list available appointment slots based on speciality
    @GET("api/appointments/specialtyavailability/{specialtyid}?sort=starttime,asc")
    Call<SwagArrayResponseModel<List<TimeSlot>>> getSpecialtyAvailableServices(@Header("Authorization") String authHeader, @Path("specialtyid") int specialtyid, @QueryMap Map<String, String> query);


    //show servicefee
    @GET("api/appointments/servicefee/{serviceid}")
    Call<SwagArrayResponseModel<List<ServiceFeeDataModel>>> getServiceFee(@Header("Authorization") String authHeader, @Path("serviceid") int serviceid, @QueryMap Map<String, Integer> query);

    //login
    @POST("api/patients/login")
    Call<SwagArrayResponseModel<List<LoginDataModel>>> patientLogin(@Body LoginModel patient);


    //logout
    @GET("api//patients/{patientid}/logout")
    Call<SwagArrayResponseModel> patientLogout(@Header("Authorization") String authHeader, @Path("patientid") int patientid);

    //registration processes
    @POST("api/patients/startregistration")
    Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> startRegistration(@Body StartRegistrationModel phonenumber);

    @POST("api/patients/validatephonecode")
    Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> validatePhoneCode(@Body ValidatePhoneCodeModel phonenumber);

    @GET("api/patients/{phoneNumber}/getvalidation")
    Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> checkValidatePhoneCode(@Path("phoneNumber") String phoneNumber);

    @POST("api/patients/resendphonecode")
    Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>resendCode(@Body ResendCodeModel phonenumber);

    @POST("api/patients")
    Call<SwagArrayResponseModel<List<RegistrationDataModel>>> patientRegister(@Body RegistrationModel patient);

    @PUT("api/patients")
    Call<SwagArrayResponseModel<List<PatientModel>>> updatePatient(@Header("Authorization") String authHeader,@Body PatientModel patient);

    @POST("api/appointments/payandbook")
    Call<SwagArrayResponseModel<List<ConfirmedAppointmentContentModel>>> bookAndPay(@Header("Authorization") String authHeader, @Body Booking bookNPay);

    //to fetch list of confirmed appointments
    @GET("api/patients/{patientid}/appointments")//inprogress=7,completed=3,booked=8
    Call<SwagResponseModel<List<ConfirmedAppointmentContentModel>>> getConfirmedAppointments(@Header("Authorization") String authHeader, @Path("patientid") int patientid,@Query("statusid") int statusid);

    @GET("api/appointments/{bookingid}/getpatientchatsession")
    Call<SwagArrayResponseModel<List<ChatSessionDataModel>>> getChatSession (@Header("Authorization") String authHeader,@Path("bookingid") int bookingid);

    //show profile picture
    @GET("api/patients/{patientid}/profilephoto")
    Call<SwagArrayResponseModel<List<PatientPhotoModel>>> getProfilePicture(@Header("Authorization") String authHeader, @Path("patientid") int patientid);

    @POST("api/patients/{patientid}/profilephoto")
    Call<SwagArrayResponseModel> uploadProfilePicture(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body Picture picture);

    @PUT("api/patients/{patientid}/profilephoto")
    Call<SwagArrayResponseModel> updateProfilePicture(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body Picture picture);

    //Make Payments
    @GET("api/appointments/{bookingid}/paymentstatus")
    Call<SwagArrayResponseModel> getBookingStatus(@Header("Authorization") String authHeader, @Path("bookingid") int bookingid);

    //Resetting of password stuffs

    @POST("api/patients/resetpassword")
    Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> resetPassword(@Body StartRegistrationModel phonenumber);

    @POST("api/patients/validatresetcode")
    Call<SwagArrayResponseModel<List<ValidatePhoneCodeDataModel>>> validatResetcode(@Body ValidatePhoneCodeModel phonenumber);

    @GET("api/patients/{phoneNumber}/getresetdetails")
    Call<SwagArrayResponseModel<List<StartRegistrationDataModel>>> getResetdetails(@Path("phoneNumber") String phoneNumber);

    @POST("api/patients/resendresetcode")
    Call<SwagArrayResponseModel<List<ResendPhoneCodeDataModel>>>resendResetcode(@Body ResendCodeModel phonenumber);


    //Blood Donor Endpoints
    @GET("api/patients/{patientid}/blooddonorcertificates")
    Call<SwagResponseModel<List<PatientblooddonordetailModel>>> getBloodonordetails(@Header("Authorization") String authHeader, @Path("patientid") int patientid);

    @POST("api/patients/{patientid}/blooddonorcertificates")
    Call<SwagArrayResponseModel> uploadBloodonordetails(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<PatientblooddonordetailModel> details);

    @PUT("api/patients/{patientid}/blooddonorcertificates")
    Call<SwagArrayResponseModel> updateBloodonordetails(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<PatientblooddonordetailModel> details);

    ///patient form data
    @GET("api/patients/{patientid}/formdata")
    Call<SwagArrayResponseModel<List<PatientProfileFormDataOut>>> getFormData(@Header("Authorization") String authHeader, @Path("patientid") int patientid);


    ///Contact Persons
    @GET("api/patients/{patientid}/contactpersons")
    Call<SwagResponseModel<List<Patientcontactperson>>> getContactPersons(@Header("Authorization") String authHeader, @Path("patientid") int patientid);

    @POST("api/patients/{patientid}/contactpersons")
    Call<SwagArrayResponseModel<List<Patientcontactperson>>> saveContactPersons(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<Patientcontactperson> details);

    @PUT("api/patients/{patientid}/contactpersons")
    Call<SwagArrayResponseModel<List<Patientcontactperson>>> updateContactPersons(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<Patientcontactperson> details);

    @DELETE("api/patients/{patientid}/contactpersons/{id}")
    Call<SwagArrayResponseModel> deleteContactPerson(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Path("id") int id);


    ///Next of Kins
    @GET("api/patients/{patientid}/nextofkins")
    Call<SwagResponseModel<List<Patientcontactperson>>> getNextofKins(@Header("Authorization") String authHeader, @Path("patientid") int patientid);

    @POST("api/patients/{patientid}/nextofkins")
    Call<SwagArrayResponseModel<List<Patientcontactperson>>> saveNextofKins(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<Patientcontactperson> details);

    @PUT("api/patients/{patientid}/nextofkins")
    Call<SwagArrayResponseModel<List<Patientcontactperson>>> updateNextofKins(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<Patientcontactperson> details);

    @DELETE("api/patients/{patientid}/nextofkins/{id}")
    Call<SwagArrayResponseModel> deleteNextofKins(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Path("id") int id);

    ///Medications
    @GET("api/patients/{patientid}/profilemedications")
    Call<SwagResponseModel<List<Patientprofilemedication>>> getMedications(@Header("Authorization") String authHeader, @Path("patientid") int patientid);

    @POST("api/patients/{patientid}/profilemedications")
    Call<SwagArrayResponseModel<List<Patientprofilemedication>>> saveMedications(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<Patientprofilemedication> details);

    @PUT("api/patients/{patientid}/profilemedications")
    Call<SwagArrayResponseModel<List<Patientprofilemedication>>> updateMedications(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<Patientprofilemedication> details);

    @DELETE("api/patients/{patientid}/profilemedications/{id}")
    Call<SwagArrayResponseModel> deleteMedication(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Path("id") int id);


    //Medical History
    @GET("api/patients/{patientid}/profilemedicalhistories")
    Call<SwagResponseModel<List<Patientprofilemedicalhistory>>> getMedicalHistory(@Header("Authorization") String authHeader, @Path("patientid") int patientid);

    @POST("api/patients/{patientid}/profilemedicalhistories")
    Call<SwagArrayResponseModel<List<Patientprofilemedicalhistory>>> saveMedicalHistory(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<PatientMedicalHistoryIn> details);

    @PUT("api/patients/{patientid}/profilemedicalhistories")
    Call<SwagArrayResponseModel<List<Patientprofilemedicalhistory>>> updateMedicalHistory(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body List<PatientMedicalHistoryIn> details);

    @DELETE("api/patients/{patientid}/profilemedicalhistories/{id}")
    Call<SwagArrayResponseModel> deleteMedicalHistory(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Path("id") int id);

    //Doctors belonging to a specific clinical specialty
    @GET("api/setup/clinicalspecialties/{id}/doctors")
    Call<SwagArrayResponseModel<List<DoctorOutObj>>> getClinicalSpecialtyDoctors(@Header("Authorization") String authHeader, @Path("id") int id);

    @GET("api/setup/title")
    Call<SwagResponseModel<List<BasicObject>>> getAllTitle(@Header("Authorization") String authHeader);
    //Gets Doctors Profile Details
    @GET("api/users/{userid}/profile")
    Call<SwagArrayResponseModel<List<UserProfile>>> getDoctorProfile(@Header("Authorization") String authHeader, @Path("userid") String userid);

    @GET("api/reviews/statistics/{revieweeid}/details")
    Call<SwagArrayResponseModel<List<ReviewStatistics>>> getReviewStatsDetails(@Header("Authorization") String authHeader, @Path("revieweeid") String revieweeid, @QueryMap Map<String, String> query);


    /*******
     *
     * System Reviews
     */
    //Reviewanswers
    @GET("api/setup/reviewanswers")
    Call<SwagResponseModel<List<Reviewanswer>>> getReviewAnswers(@Header("Authorization") String authHeader);

    //review questions
    @GET("api/reviews/questions/{typeid}")
    Call<SwagArrayResponseModel<List<ReviewQuestionsOut<List<Reviewquestion>>>>> getReviewQuestions(@Path("typeid") int typeid, @Header("Authorization") String authHeader);

    //review question types
    @GET("api/setup/reviewtypes")
    Call<SwagResponseModel<List<Reviewtype>>> getReviewTypes(@Header("Authorization") String authHeader);

    @POST("api/reviews/reviews")
    Call<SwagArrayResponseModel<List<Review>>> saveReview(@Body Review review,@Header("Authorization") String authHeader);


    /*******
     *
     * Chat room
     */
    //review question types
    @GET("api/chatroom/generatechattoken")
    Call<SwagArrayResponseModel<List<ChatRoomToken>>> generateChatSession(@Header("Authorization") String authHeader);

    @PUT("api/patients/{patientid}/changemyphone")
    Call<SwagArrayResponseModel<ResendPhoneCodeDataModel>> updatePhoneNumber(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body ResendCodeModel regphone);


    @PUT("api/patients/{patientid}/changemypassword")
    Call<SwagArrayResponseModel> updatePassword(@Header("Authorization") String authHeader,@Path("patientid") int patientid, @Body PatientChangeCredentials changeCredentials);


    /**
     * Service Providers
     **/
    @GET("api/serviceproviders")
    Call<SwagResponseModel<List<Facilityinfo>>> getFacilityInfo();

    /**
     * Special list or doctor*/
    @GET("api/users")
    Call<SwagResponseModel<List<UserAccount>>> getUsersInfo(@Header("Authorization") String authHeader);

    @GET("api/consultations/patients/{patientid}/consultationReferralhistroy")
    Call<SwagArrayResponseModel<List<Referral>>> getReferral(@Header("Authorization") String authHeader, @Path("patientid") long patientid);

    @GET("api/consultations/patients/{patientid}/consultationhistroy?")//http://infotechhims.com:1350/api/consultations/patients/1/consultationhistroy?from=1-01-2018&to=1-02-2018
    Call<SwagArrayResponseModel<List<ConsultationProfile>>> getOnlineVisits(@Header("Authorization") String authHeader, @Path("patientid") long patientid, @Query("from") String from, @Query("to") String to);


    @POST("api/consultations/paydrug")
    Call<SwagArrayResponseModel<List<String>>> payDrug(@Header("Authorization") String authHeader,@Body ConsultationPayment consultationPayment);

    @POST("api/chatroom/chat")
    Call<SwagArrayResponseModel<List<Chat>>> saveChat(@Header("Authorization") String authHeader,@Body Chat chat);

    @GET("api/chatroom/consultation/{id}?sort=id,desc&size=1000")
    Call<SwagResponseModel<List<Chat>>> getChat(@Path("id") long consultationid,@Header("Authorization") String authHeader);


    //Peripheral Devices stusffs
    @GET("api/vitalsigns/peripherals?sort=id,asc&size=1000")
    Call<SwagResponseModel<List<Peripheral>>> getPeripherals(@Header("Authorization") String authHeader);

    @POST("api/vitalsigns/vitalsignscapture")
    Call<SwagArrayResponseModel<List<Vitalsignscapture>>> setVitalSign(@Header("Authorization") String authHeader, @Body Vitalsignscapture vitalsignscapture);

    @GET("api/vitalsigns/vitalsignscapture/patient/{patientid}")
    Call<SwagArrayResponseModel<List<Vitalsignscapture>>> getAllMyCapture(@Header("Authorization") String authHeader, @Path("patientid") long patientid);

    @GET("api/vitalsigns/vitalsignscapture/patient/{patientid}/{peripheralid}")
    Call<SwagArrayResponseModel<List<Vitalsignscapture>>> getAllMyCaptureForDevice(@Header("Authorization") String authHeader, @Path("patientid") long patientid,@Path("peripheralid") String peripheralid);


}