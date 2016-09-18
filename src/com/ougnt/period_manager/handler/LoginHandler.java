package com.ougnt.period_manager.handler;

import android.content.Context;
import android.content.SharedPreferences;

import com.ougnt.period_manager.activity.InitialActivity;
import com.ougnt.period_manager.data.RsaEncoder;
import com.ougnt.period_manager.repository.RsaRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

public class LoginHandler {

    public static final String PRsaId = "period_manager_preference_rsa_id";
    public static final String PRsaE = "period_manager_preference_rsa_e";
    public static final String PRsaN = "period_manager_preference_rsa_n";
    public static final String PUserToken = "period_manager_preference_user_token";
    public static final String NotExist = "not_exist";

    public void login(Context context, String email, String password) {
        RsaRepository repo = handshake(context);

        RsaEncoder encoder = new RsaEncoder(repo.e, repo.n);
        String encryptLoginRequest = encoder.encrypt(String.format("{\"userName\":\"%s\",\"password\":\"%s\"}", email, password));
        String loginResp = HttpHelper.post("http://" + InitialActivity.StatServer + "/login?id=" + repo.id, encryptLoginRequest);
        saveUserToken(context, loginResp);
    }

    private RsaRepository handshake(Context context){

        SharedPreferences sp = context.getSharedPreferences(InitialActivity.PName, Context.MODE_PRIVATE);
        RsaRepository rsaRepo = new RsaRepository();

        if(sp.getString(PRsaId, NotExist).equals(NotExist) || sp.getString(PRsaE, NotExist).equals(NotExist) || sp.getString(PRsaN, NotExist).equals(NotExist)) {

            String response = HttpHelper.get("http://" + InitialActivity.StatServer + "/handshake");

            try {
                JSONObject json = new JSONObject(response);
                rsaRepo.id = json.getString("id");
                rsaRepo.e = new BigInteger(json.getString("e"), 36);
                rsaRepo.n = new BigInteger(json.getString("n"), 36);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(PRsaId, rsaRepo.id);
                edit.putString(PRsaE, rsaRepo.e.toString(36));
                edit.putString(PRsaN, rsaRepo.n.toString(36));
                edit.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rsaRepo;
        }

        rsaRepo.id = sp.getString(PRsaId, NotExist);
        rsaRepo.e = new BigInteger(sp.getString(PRsaE, "0"), 36);
        rsaRepo.n = new BigInteger(sp.getString(PRsaN, "0"), 36);

        return rsaRepo;
    }

    private void saveUserToken(Context context, String userToken) {
        SharedPreferences sp = context.getSharedPreferences(InitialActivity.PName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(PUserToken, userToken);
        edit.apply();
    }

//    public void logout(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(InitialActivity.PName, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sp.edit();
//        edit.putString(PUserToken, "");
//        edit.apply();
//    }
}
