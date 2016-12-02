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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
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

    //private OnFragmentInteractionListener mListener;

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
        username = (EditText) view.findViewById(R.id.input_username);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            //mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
