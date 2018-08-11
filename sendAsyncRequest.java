package com.example.realestate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Handler;


public class sendAsyncRequest
 {
	 //==========================================
	 public static final int HTTP_TIMEOUT=60*1000;
	 //==========================================
	 private HttpClient mHttpClient;
	 private Handler mHandler;
	 public static int METHOD_GET=1;
	 public static int METHOD_POST=2;
	 //==========================================
	 private HttpClient getHttpClient()
	 {
		 if(mHttpClient==null)
		 {
			 mHttpClient=new DefaultHttpClient();
			 final HttpParams params=mHttpClient.getParams();
			 HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			 HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			 ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
			 
		 }
		 return mHttpClient;
	 }
	 //======================================================================================================================
	 public String sendSync(int method,String url,ArrayList<NameValuePair> postParameters)throws Exception
	 {
	
		 BufferedReader in=null;
		 try
		 {
			 HttpClient client=getHttpClient();
			 if(method==METHOD_GET)
			 {
				 HttpGet request=new HttpGet();
				
				 request.setURI(new URI(url));
				 HttpResponse responce=client.execute(request);
				 in=new BufferedReader(new InputStreamReader(responce.getEntity().getContent()));
				 
				 
			 }
			 else if(method==METHOD_POST)
			 {
				
				 HttpPost postrequest=new HttpPost(url);
				 for(int i=0;i<postParameters.size();i++)
				 {
					 postrequest.addHeader(postParameters.get(i).getName(),postParameters.get(i).getValue());
				 }
				// postrequest.setEntity(new UrlEncodedFormEntity(postParameters));
				 HttpResponse postResponse=client.execute(postrequest);
				 in=new BufferedReader(new InputStreamReader(postResponse.getEntity().getContent()));
				
				 
				 
			 }
			 else
			 {
				 return "INVALID METHOD";
			 }
			 StringBuffer sb = new StringBuffer("");
			  String line = "";
			  String NL = System.getProperty("line.separator");
			 while ((line = in.readLine()) != null) 
			 {
			
				  sb.append(line + NL);
	           
	                
			 }
			 in.close();
			 String result=sb.toString();
			 return result;
		 } catch(Exception e){
	            return e.toString();
	        }
	            finally 
	            {
	            if (in != null) 
	            {
	                try 
	                {
	                    in.close();
	                } catch (IOException e) 
	                {
	                   
	                }
	            }
	        
			 
	            }
	 }
	// ======================================================================================================================
	 public void sendAsync(final int method,final String urls,final ArrayList<NameValuePair> formParams,final requestCallBack callBack)
	 {
		 if(mHandler==null)
		 {
			 mHandler=new Handler();
		 }
		 
		 Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String restResponce;
				restResponce=requestposturl(method, urls, formParams);
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					callBack.completedWithResult(restResponce);	
						
					}
				});
			}
			 private String requestposturl(int method, String url, ArrayList<NameValuePair> formParam) 
			   {
		           try{
		        	   String response;
		        	   response  = sendSync(method,url, formParam);
		               return response;
		           }
		           catch(Exception e)
		           {
		        	   return "";
		        	   
		           }
			   }
		});
		 t.start();
	 }
	 
	  
  
}
	 
	 
	 
	 
	 
 