package com.pango.hsec.hsec.controller;

import android.support.annotation.RawRes;

import com.pango.hsec.hsec.model.GetGaleriaModel;
import com.pango.hsec.hsec.model.LoginModel;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by ICWB-01 on 04/04/2018.
 */

public interface WebServiceAPI {

    //@GET
   // Call<String> getToken(@Url String url);

    @POST("membership/authenticate")
    Call<String> getTokenAuth(@Body LoginModel UserLogin);


    @GET("media/GetMultimedia/{CodNoticia}")
    Call<GetGaleriaModel> getFiles(@Header("Authorization") String token, @Path("CodNoticia") String CodNoticia);

    @Multipart
    @POST("Media/Upload")
    Call<String> uploadImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("Media/UploadMultimedia")
    Call<String> uploadFile(@Header("Authorization") String token, @Part MultipartBody.Part file, @Part("NroDocReferencia") RequestBody Nrodoc, @Part("CodTabla") RequestBody tabla, @Part("GrupoPertenece") RequestBody Grupo);

    @Multipart
    @POST("Media/UploadAllFiles")
    Call<String> uploadAllFile(@Header("Authorization") String token, @Part("NroDocReferencia") RequestBody Nrodoc, @Part("CodTabla") RequestBody tabla, @Part("GrupoPertenece") RequestBody Grupo, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Observaciones/Insertar")
    Call<String> insertarObservacion(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody detalle, @Part("3") RequestBody planes,@Part("4") RequestBody Involucrados,@Part("5") RequestBody Subdetalle, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Observaciones/Actualizar")
    Call<String> actualizarObservacion(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody detalle, @Part("3") RequestBody planesDelete, @Part("4") RequestBody filesDelete, @Part("5") RequestBody codObservacion,@Part("6") RequestBody Involucrados, @Part("7") RequestBody subDetalle, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Verificacion/Insertar")
    Call<String> insertarVerificacion(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody planes, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Verificacion/Actualizar")
    Call<String> actualizarVerificacion(@Header("Authorization") String token, @Part("1") RequestBody cabecera,  @Part("2") RequestBody planesDelete, @Part("3") RequestBody filesDelete, @Part("4") RequestBody CodVerificacion, @Part List<MultipartBody.Part> files);


    @Multipart
    @POST("Inspecciones/InsertarObservacion")
    Call<String> insertarInspeccionObs(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody planes, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Inspecciones/ActualizarObservacion")
    Call<String> actualizarInspeccionObs(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody planesDelete, @Part("3") RequestBody filesDelete, @Part("4") RequestBody codInspeccion, @Part("5") RequestBody nroDetInspeccion, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Inspecciones/Insertar")
    Call<String> insertarInspeccion(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody responsables,@Part("3") RequestBody atendidos, @Part("4") RequestBody observaciones,@Part("5") RequestBody planes,  @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("Inspecciones/Actualizar")
    Call<String> actualizarInspeccion(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody responsables,@Part("3") RequestBody atendidos, @Part("4") RequestBody DeleteObs);

    @Multipart
    @POST("AccionMejora/PutPost")
    Call<String> PostAccionMejora(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody filesDelete, @Part("3") RequestBody CodAccion, @Part("4") RequestBody Correlativo, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("ObsFacilito/Post")
    Call<String> PostObsFacilito(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody DeleteObs,@Part("3") RequestBody CodObsFacilito, @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("ObsFacilito/PostAtencion")
    Call<String> PostObfAtencion(@Header("Authorization") String token, @Part("1") RequestBody cabecera, @Part("2") RequestBody DeleteObs,@Part("3") RequestBody CodObsFacilito,  @Part("4") RequestBody Correlativo,@Part List<MultipartBody.Part> files);


    @POST("usuario/updatetoken")
    Call<String> updateToken(@Header("Authorization") String token, @Body String token_device);

}
