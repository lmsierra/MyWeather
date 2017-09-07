package lmsierra.com.myweather.util;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class GsonRequest<T> extends JsonRequest<T> {

    private final Type type;
    private final Response.Listener<T> listener;

    public GsonRequest(int method, final String url, final String body, final Type type, final Response.Listener listener, Response.ErrorListener errorListener){

        super(method, url, body, listener, errorListener);

        this.type = type;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        try{

            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            if(json.length() == 0){
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }



            if(type == null){

                return (Response<T>) Response.success( json, HttpHeaderParser.parseCacheHeaders(response));

            }else{

                return (Response<T>) Response.success( new Gson().fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
            }


        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return null;


        }catch(JsonSyntaxException e){
            e.printStackTrace();
            return null;

        }
    }
}
