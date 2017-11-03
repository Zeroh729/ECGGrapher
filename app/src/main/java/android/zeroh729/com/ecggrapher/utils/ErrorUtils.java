package android.zeroh729.com.ecggrapher.utils;


import android.zeroh729.com.ecggrapher.data.model.APIError;
import android.zeroh729.com.ecggrapher.data.remote.BaseService;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {
    public static APIError parseError(Response<?> response){
        APIError error;

        Converter<ResponseBody, APIError> converter = BaseService.retrofit().responseBodyConverter(APIError.class, new Annotation[0]);

        try{
            error = converter.convert(response.errorBody());
        }catch (IOException e){
            return new APIError();
        }

        return error;
    }
}
