package android.whitewidget.com.boilerplateandroid.data.model;

public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    //How to use this
    //  in onResponse(Call<Model> call, Response<Model> response)()
        //  if(response.isSuccessful()){
        //      APIError error = ErrorUtils.parseError(response);
        //  }

}