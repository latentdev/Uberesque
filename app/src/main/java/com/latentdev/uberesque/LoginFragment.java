package com.latentdev.uberesque;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends BaseFormFragment implements IAccessResponse{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    EditText username;
    EditText password;
    Button btn_login;
    TextView link_signup;
    //private OnFragmentInteractionListener mListener;
    ProgressDialog progressDialog;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        username = (EditText) view.findViewById(R.id.input_username);
        password = (EditText) view.findViewById(R.id.input_password);
        btn_login = (AppCompatButton) view.findViewById(R.id.btn_login);
        link_signup = (TextView) view.findViewById(R.id.link_signup);
        progressDialog = new ProgressDialog(getActivity());

        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        link_signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment fragment = Fragment.instantiate(LoginFragment.super.getContext(), RegisterFragment.class.getName());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        });

        return view;
    }
/**
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Response response);
    }

    public void login()
    {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);


        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = "http://uberesque.azurewebsites.net/api/Account/Login?username="+username.getText().toString()+"&password="+password.getText().toString();
        AsyncConnection async = new AsyncConnection(this.getContext(),this.getActivity());
        async.delegate = this;
        async.execute(url);

        //progressDialog.hide();
        btn_login.setEnabled(true);

    }

    public boolean validate() {
        boolean valid = true;

        String temp_username = username.getText().toString();
        String temp_password = password.getText().toString();

        if (temp_username.isEmpty() || username.length() < 4 || username.length() > 10) {
            username.setError("enter a valid email address");
            valid = false;
        } else {
            username.setError(null);
        }

        if (temp_password.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
    public void onLoginFailed() {
        Toast.makeText(super.getContext(), "Login failed", Toast.LENGTH_LONG).show();
        progressDialog.hide();
        btn_login.setEnabled(true);
    }
    @Override
    public void postResult(Response asyncResult)
    {
            mListener.onFragmentInteraction(asyncResult);
            progressDialog.hide();
    }

    @Override
    public void Reset()
    {
        progressDialog.hide();
        btn_login.setEnabled(true);
    }


}
