    package com.example.nitin.facebook;

    import android.content.Intent;
    import android.graphics.Color;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.TextView;

    import com.facebook.AccessToken;
    import com.facebook.AccessTokenTracker;
    import com.facebook.CallbackManager;
    import com.facebook.FacebookCallback;
    import com.facebook.FacebookException;
    import com.facebook.FacebookSdk;
    import com.facebook.GraphRequest;
    import com.facebook.GraphResponse;
    import com.facebook.HttpMethod;
    import com.facebook.ProfileTracker;
    import com.facebook.login.LoginResult;
    import com.facebook.login.widget.LoginButton;
    import com.facebook.login.widget.ProfilePictureView;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.net.MalformedURLException;
    import java.net.URL;

    public class MainActivity extends AppCompatActivity {
        ProfilePictureView profile;
        private CallbackManager callbackManager;
        private AccessTokenTracker accessTokenTracker;
        private LoginButton loginButton;
        TextView info;

        @Override
        protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
            super.onActivityResult(requestCode, responseCode, intent);
            callbackManager.onActivityResult(requestCode,responseCode,intent);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            callbackManager=CallbackManager.Factory.create();
            info=(TextView)findViewById(R.id.res);

            loginButton=(LoginButton)findViewById(R.id.login_button);
            loginButton.setHeight(100);
            loginButton.setTextColor(Color.WHITE);
            loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            loginButton.setCompoundDrawablePadding(0);

            FacebookCallback<LoginResult>callback=new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.i("LoginActivity", response.toString());
                            // Get facebook data from login
                            Bundle bFacebookData = getFacebookData(object);
                            info.setText(bFacebookData.toString());
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name,gender, birthday, location");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                @Override
                public void onCancel() {
                    info.setText("nopee");

                }
                @Override
                public void onError(FacebookException e) {
                    e.printStackTrace();
                }
            };

            loginButton.setReadPermissions("email", "user_birthday","user_posts");
            loginButton.registerCallback(callbackManager, callback);


        }
        private Bundle getFacebookData(JSONObject object) {

                Bundle bundle = new Bundle();
            String id = null;
            try {
                id = object.getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            bundle.putString("idFacebook", id);
                if (object.has("first_name"))
                    try {
                        bundle.putString("first_name", object.getString("first_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                if (object.has("last_name"))
                    try {
                        bundle.putString("last_name", object.getString("last_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                if (object.has("email"))
                    try {
                        bundle.putString("email", object.getString("email"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                if (object.has("gender"))
                    try {
                        bundle.putString("gender", object.getString("gender"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                if (object.has("birthday"))
                    try {
                        bundle.putString("birthday", object.getString("birthday"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                if (object.has("location"))
                    try {
                        bundle.putString("location", object.getJSONObject("location").getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                return bundle;
            }

        }

