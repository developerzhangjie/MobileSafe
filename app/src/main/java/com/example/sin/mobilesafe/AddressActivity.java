package com.example.sin.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import bean.AddressInfo;

/**
 * Created by Sin on 2016/9/26.
 * Description:
 */

public class AddressActivity extends Activity {
    private EditText et_address_phone;
    private TextView provinceCity;
    private TextView operator;
    private TextView cityCode;
    private TextView zipCode;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
    }

    private void initView() {
        Button bn_address_query = (Button) findViewById(R.id.bn_address_query);
        et_address_phone = (EditText) findViewById(R.id.et_address_phone);
        zipCode = (TextView) findViewById(R.id.zipCode);
        cityCode = (TextView) findViewById(R.id.cityCode);
        operator = (TextView) findViewById(R.id.operator);
        provinceCity = (TextView) findViewById(R.id.provinceCity);
        final RequestQueue requestQueue = Volley.newRequestQueue(AddressActivity.this);
        bn_address_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = et_address_phone.getText().toString().trim();
                if (!TextUtils.isEmpty(number)) {
                    url = "http://apicloud.mob.com/v1/mobile/address/query?key=19c88142861e7&phone=" + number;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //获取到了返回的json数据
                                    Log.d("TAG", response.toString());
                                    //进行解析
                                    Gson gson = new Gson();
                                    AddressInfo addressInfo = gson.fromJson(response.toString(), AddressInfo.class);
                                    String retCode = addressInfo.getRetCode();
                                    if (Integer.parseInt(retCode) == 200) {
                                        String city1 = addressInfo.getResult().getCity();
                                        String cityCode1 = addressInfo.getResult().getCityCode();
                                        String province1 = addressInfo.getResult().getProvince();
                                        String operator1 = addressInfo.getResult().getOperator();
                                        String zipCode1 = addressInfo.getResult().getZipCode();
                                        provinceCity.setText("地区：" + province1 + city1);
                                        operator.setText("运营商：" + operator1);
                                        cityCode.setText("城市区号：" + cityCode1);
                                        zipCode.setText("邮编：" + zipCode1);
                                    } else {
                                        Toast.makeText(AddressActivity.this, "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(AddressActivity.this, "请连接互联网", Toast.LENGTH_SHORT).show();
                                }
                            });
                            requestQueue.add(jsonObjectRequest);
                        }
                    }).start();
                } else {
                    Toast.makeText(AddressActivity.this, "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
                    //执行抖动动画
                    Animation shake = AnimationUtils.loadAnimation(AddressActivity.this, R.anim.shake);
                    et_address_phone.startAnimation(shake);
                    //振动
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    //设置振动的时间
                    //milliseconds : 振动的时间
                    vibrator.vibrate(100);//国产定制手机,执行默认的振动的时间  比如小米,单位毫秒
                }
            }
        });
    }
}
