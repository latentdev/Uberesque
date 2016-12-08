package com.latentdev.uberesque;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends BaseFormFragment implements IAccessResponse {
    EditText username;
    EditText password;
    EditText email;
    EditText firstname;
    EditText lastname;
    EditText color;
    EditText year;
    EditText make;
    EditText model;
    EditText plate;
    CheckBox driver;
    Button btn_login;
    LinearLayout car;
    TextView link_login;
    ProgressDialog progressDialog;
    public Response response;
    User user;
    Vehicle vehicle;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        username = (EditText) view.findViewById(R.id.input_name);
        password = (EditText) view.findViewById(R.id.input_password);
        email = (EditText) view.findViewById(R.id.input_email);
        firstname = (EditText) view.findViewById(R.id.input_firstname);
        lastname = (EditText) view.findViewById(R.id.input_lastname);
        driver = (CheckBox) view.findViewById(R.id.driver_check);
        color = (EditText) view.findViewById(R.id.input_color);
        year = (EditText) view.findViewById(R.id.input_year);
        make = (EditText) view.findViewById(R.id.input_make);
        model = (EditText) view.findViewById(R.id.input_model);
        plate = (EditText) view.findViewById(R.id.input_plate);
        car = (LinearLayout) view.findViewById(R.id.car);
        car.getLayoutParams().height = 0;
        driver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(driver.isChecked())
                {
                    car.getLayoutParams().height= LinearLayout.LayoutParams.WRAP_CONTENT;
                    car.invalidate();

                }
                else
                {
                    car.getLayoutParams().height = 0;
                    car.invalidate();
                }
            }
        });

        btn_login = (AppCompatButton) view.findViewById(R.id.btn_signup);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btn_login.setEnabled(false);
                register();
            }
        });
        link_login = (TextView) view.findViewById(R.id.link_login);
        progressDialog = new ProgressDialog(getActivity());


        link_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment fragment = Fragment.instantiate(RegisterFragment.super.getContext(), LoginFragment.class.getName());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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

    public void register()
    {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        btn_login.setEnabled(false);
        user=new User();
        user.UserName=username.getText().toString();
        user.Password = password.getText().toString();
        user.Email = email.getText().toString();
        user.FirstName = firstname.getText().toString();
        user.LastName = lastname.getText().toString();
        user.Driver = driver.isChecked();
        if(user.Driver==true)
        {
            vehicle=new Vehicle();
            vehicle.Color = color.getText().toString();
            vehicle.Year = Integer.parseInt(year.getText().toString());
            vehicle.Make = make.getText().toString();
            vehicle.Model = model.getText().toString();
        }
        response=new Response();
        response.user=user;
        response.vehicle=vehicle;
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String url = "http://uberesque.azurewebsites.net/api/Account/Register?email="+response.user.Email+"&username="+response.user.UserName+"&pass="+response.user.Password+"&firstname="+user.FirstName+"&lastname="+user.LastName+"&driver="+user.Driver.toString();
        if(response.user.Driver!=false)
        {
            url+="&color="+vehicle.Color+"&year="+vehicle.Year+"&make="+vehicle.Make+"&model="+vehicle.Model+"&plate="+vehicle.Plate;
        }
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
        String temp_email = password.getText().toString();
        String temp_firstname = firstname.getText().toString();
        String temp_lastname = lastname.getText().toString();

        if (temp_username.isEmpty() || username.length() < 4 || username.length() > 10) {
            username.setError("enter a valid  username");
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

        if (temp_email.isEmpty()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (temp_firstname.isEmpty()) {
            firstname.setError("enter a valid first name");
            valid = false;
        } else {
            firstname.setError(null);
        }
        if (temp_lastname.isEmpty()) {
            lastname.setError("enter a valid last name");
            valid = false;
        } else {
            lastname.setError(null);
        }

        if (driver.isChecked()) {
            String temp_color = username.getText().toString();
            String temp_year = password.getText().toString();
            String temp_make = password.getText().toString();
            String temp_model = firstname.getText().toString();
            if (temp_color.isEmpty()) {
                color.setError("enter a valid color");
                valid = false;
            } else {
                color.setError(null);
            }

            if (temp_year.isEmpty()) {
                year.setError("enter a valid year");
                valid = false;
            } else {
                year.setError(null);
            }

            if (temp_make.isEmpty()) {
                make.setError("enter a valid make");
                valid = false;
            } else {
                make.setError(null);
            }
            if (temp_model.isEmpty()) {
                model.setError("enter a valid model");
                valid = false;
            } else {
                model.setError(null);
            }
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(super.getContext(), "Login failed", Toast.LENGTH_LONG).show();
        progressDialog.hide();
        btn_login.setEnabled(true);
    }
    /*
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
    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Response response);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
