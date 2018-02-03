package dhl.myrslnk.fscustom.com.division.ecommerce.phelixtechnology.simplepayumoneyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity : ";
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    /******** user information including product info **************/
    String product_name,userName,userEmail,userPhone,txnId;
    Double amount; //payable amount
    Button payNowButton; // paynow button
    EditText etAmount; //amount edittext
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        payNowButton = findViewById(R.id.pay_now);
        etAmount = findViewById(R.id.amount);

        /******* set user & product info here *******/

        product_name = "TestProductName";
        userName="TestUserName";
        userEmail="test@gmail.com";
        userPhone="1234567890";

        /******** onclick listener on paynow button ********/
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Double.parseDouble(etAmount.getText().toString());
                launchPayUMoneyFlow();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        payNowButton.setEnabled(true);
    }

    /**
     * This function sets the layout for activity
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);

            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            /************* check here for transaction response ***********/

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    Log.e(TAG, "SUCESS: " + transactionResponse.getTransactionStatus());
                } else {
                    Log.e(TAG, "FAILED: " + transactionResponse.getTransactionStatus());
                    //Failure Transaction
                }

                String payuResponse = transactionResponse.getPayuResponse(); //response from payumoney

                Log.e(TAG, "Transaction Response: " + transactionResponse);
                Log.e(TAG, "PayU Response: " + payuResponse);

                try {
                    JSONObject jObj = new JSONObject(payuResponse);
                    Log.e(TAG, "onActivityResult: " + jObj.getString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */

    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Activity Title");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        txnId = System.currentTimeMillis() + "";
        String phone = userPhone;
        String productName = product_name;
        String firstName = userName;
        String email = userEmail;
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl("https://phelixtech.com/community/opensource/payu_success.php")
                .setfUrl("https://phelixtech.com/community/opensource/payu_failure.php")
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(true)
                .setKey("rjQUPktU").setMerchantId("4934580");

        try {
            mPaymentParams = builder.build();

            generateHashFromServer(mPaymentParams);

        } catch (Exception e) {
            // some exception occurred
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            payNowButton.setEnabled(true);
        }
    }

    /**
     * This method generates hash from server.
     *
     * @param paymentParam payments params used for hash generation
     */
    public void generateHashFromServer(final PayUmoneySdkInitializer.PaymentParam paymentParam) {
        RequestQueue queue1 = Volley.newRequestQueue(this);
        String url = "https://phelixtech.com/community/opensource/PayUhashGeneration.php"; // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject;
                        String merchantHash = "";

                        try {
                            jsonObject = new JSONObject(response);
                            merchantHash = jsonObject.getString("payment_hash");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //setting up response values to the fragment
                        if (merchantHash.isEmpty() || merchantHash.equals("")) {
                            Toast.makeText(MainActivity.this, "Please Retry Payment", Toast.LENGTH_SHORT).show();
                        } else {
                            mPaymentParams.setMerchantHash(merchantHash);
                            //Toast.makeText(FinalCheckoutActivity.this, "m:"+mPaymentParams.getParams(), Toast.LENGTH_SHORT).show();
                            //Log.e(TAG, "onPostExecute: "+mPaymentParams.getParams() );
                            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, MainActivity.this, -1, false);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Error:" + error, Toast.LENGTH_LONG).show();
            }

        })
        /***************** POST PARAMETERS TO PHP SCRIPT FOR HASH GENERATION **************/
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(PayUmoneyConstants.KEY, "rjQUPktU");
                params.put(PayUmoneyConstants.AMOUNT, amount + "");
                params.put(PayUmoneyConstants.TXNID, txnId);
                params.put(PayUmoneyConstants.EMAIL, userEmail);
                params.put(PayUmoneyConstants.PRODUCT_INFO, product_name);
                params.put(PayUmoneyConstants.FIRSTNAME, userName);
                params.put(PayUmoneyConstants.UDF1, "");
                params.put(PayUmoneyConstants.UDF2, "");
                params.put(PayUmoneyConstants.UDF3, "");
                params.put(PayUmoneyConstants.UDF4, "");
                params.put(PayUmoneyConstants.UDF5, "");

                return params;
            }
        };
        queue1.add(stringRequest);
    }
}
