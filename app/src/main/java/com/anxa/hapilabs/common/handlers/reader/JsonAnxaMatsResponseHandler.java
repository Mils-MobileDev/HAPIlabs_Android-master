package com.anxa.hapilabs.common.handlers.reader;

import org.json.JSONException;
import org.json.JSONObject;

import com.anxa.hapilabs.models.MessageObj;
import com.anxa.hapilabs.models.MessageObj.MESSAGE_TYPE;

import android.os.Handler;
import android.os.Message;


/****
* Notes:
* - Return =  UserProfileObj for successful request
* - MessageObj for failed request
*/
public class JsonAnxaMatsResponseHandler extends JsonDefaultResponseHandler {

	protected Handler handler;
	
	protected boolean isError = false;

	String OutputData = "";
    JSONObject jsonResponse;
    String  strJson;
    
    Message msg = new Message();
	
	

    /****** STEP1:
     * PLEASE ADD FORMAT OF THE FORGOT PASSOWRD RESPONSE JSON DATA
     * 
    
     * */
	public JsonAnxaMatsResponseHandler(Handler handler){
		super(handler);
		this.handler = handler;
	}
		
	
	
	
	@Override
	public void start(String strJson) {
		this.strJson = strJson;
		start();
	
	}
	@Override
	public void start(){

		/****** STEP3:
	     * START JSON PARSING HERE:
	     * 
	     
	     * 
	     * */
        try {
//        	{
//        		   error = "<null>";
//        		   "error_count" = 0;
//        		   message = Successful;
//        		   "message_detail" = "Logged successfully.";
//        	}

        	System.out.println("JsonAnxaMatsResponseHandler "+strJson);
    		
        	/****** inform handler that the jsn processing has started; this is optional *********/
        	msg.what = JsonDefaultResponseHandler.START;
        	handler.handleMessage(msg);
        	
             /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
            jsonResponse = new JSONObject(strJson);
             
             /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
             /*******  Returns null otherwise.  *******/
        	 
        	 JSONObject api_respose = jsonResponse.getJSONObject("api_response");
        	 
        	 String requestStatus = api_respose.optString("message");
        	 int errorCount = api_respose.optInt("error_count");
      		
        	 //for failed request
        	 if (requestStatus == null || requestStatus.equalsIgnoreCase("Failed")){

        		 //set message obj for the failed request
        		 MessageObj messageObj = JsonUtil.getMessageObj(MESSAGE_TYPE.FAILED,jsonResponse);
        		 
        		 //transfer the message obj to the response handler
        			 setResponseObj(messageObj);
        		 //set json response handler to completed
        		 msg.what = JsonDefaultResponseHandler.COMPLETED;
             	 
        		 //set the handler for the waiting class
        		 handler.handleMessage(msg);
        		 return;
        		 
        	 }else if (errorCount > 0){
        		 
        		 //set message obj for the failed request
        		 MessageObj messageObj = JsonUtil.getMessageObj(MESSAGE_TYPE.FAILED,jsonResponse);
        		 
        		 //transfer the message obj to the response handler
        			 setResponseObj(messageObj);
        		 //set json response handler to completed
        		 msg.what = JsonDefaultResponseHandler.COMPLETED;
             	 
        		 //set the handler for the waiting class
        		 handler.handleMessage(msg);
        		 return;
        	 }
        	        
        	 
        	 //possible successful request but empty result 
        		 
        		
        		
        	 //set message obj for the failed request
    		 MessageObj messageObj = JsonUtil.getMessageObj(MESSAGE_TYPE.SUCCESS,jsonResponse);
    		 
    		 //transfer the message obj to the response handler
    		setResponseObj(messageObj);
    		 //set json response handler to completed
    		 msg.what = JsonDefaultResponseHandler.COMPLETED;
         	 
    		 //set the handler for the waiting class
    		 handler.handleMessage(msg);
    		 return;
        		
        
         	  
         } catch (JSONException e) {
        	  	msg.what = JsonDefaultResponseHandler.ERROR;
             	handler.handleMessage(msg);
             
         }
	}




	@Override
	public void start(String strJson, String id) {
		// TODO Auto-generated method stub
		
	}
	
}
	