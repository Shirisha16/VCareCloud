package com.client.vcarecloud;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DownloadManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.SentMsgModel;
import com.client.vcarecloud.models.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ViewMessages extends AppCompatActivity implements LoadDetails {
    ImageView back;
    TextView date,sentTo,subject,attachment,description;

    ArrayList<SentMsgModel> sentMsgModelArrayList;
    String custId,message,error;
    UserDetails userDetails;
    SentMsgModel sentMsgModel;

    String id,sendDate,category,sentTo1,mailSubject,details,attachments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_messages);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        date=findViewById(R.id.date);
        sentTo=findViewById(R.id.sent);
        subject=findViewById(R.id.subject);
        attachment=findViewById(R.id.attach);
        description=findViewById(R.id.details);

        custId=getIntent().getStringExtra("custId");
        userDetails=new UserDetails(ViewMessages.this);
        sentMsgModel=new SentMsgModel();

        id=getIntent().getStringExtra("communicationId");
        custId=getIntent().getStringExtra("custId");
        sendDate=getIntent().getStringExtra("sendOn");
        category=getIntent().getStringExtra("sendToCategory");
        sentTo1=getIntent().getStringExtra("sendTo");
        mailSubject=getIntent().getStringExtra("messageSubject");
        details=getIntent().getStringExtra("messageDescription");
        attachments=getIntent().getStringExtra("attachments");

        if (attachments.equalsIgnoreCase("null")) {
            attachment.setText("No Attachment found");
        }
        else {
            attachment.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_attach_file_24, 0, 0, 0);
        }

        String[] parts = sendDate.split("T");
        String daystartDate = parts[0];
        if (sendDate!=null){
            date.setText(daystartDate);
        }

        sentTo.setText(sentTo1);
        subject.setText(mailSubject);

        if (details.equalsIgnoreCase("null")) {
            description.setText("N/A");
        }else if(details!=null){
            description.setText(Html.fromHtml(details).toString());
        } else {
            description.setText(details);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewMessages();
    }

    private void viewMessages() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        Call<String> call=api.view_mail(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                SentMsgModel model = new SentMsgModel();

                                model.setCommunicationId(jsonObject1.getString("communicationId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setSendOn(jsonObject1.getString("sendOn"));
                                model.setSendToCategory(jsonObject1.getString("sendToCategory"));
                                model.setSendTo(jsonObject1.getString("sendTo"));
                                model.setMessageSubject(jsonObject1.getString("messageSubject"));
                                model.setMessageDescription(jsonObject1.getString("messageDescription"));
                                model.setAttachments(jsonObject1.getString("attachments"));
                                sentMsgModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(ViewMessages.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onMethodCallback() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}