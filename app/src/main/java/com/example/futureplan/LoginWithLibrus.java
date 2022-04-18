package com.example.futureplan;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginWithLibrus#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginWithLibrus extends Fragment {
    private EditText editTextUsername;
    private EditText editTextPassword;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginWithLibrus() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginWithLibrus.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginWithLibrus newInstance(String param1, String param2) {
        LoginWithLibrus fragment = new LoginWithLibrus();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_with_librus, container, false);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);

        Button btnLog = view.findViewById(R.id.btnLogLibrus);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description dw = new description();


                dw.execute();
            }
        });


        return  view;
    }

    public class description extends AsyncTask<Void, Void, Void> {

        private  String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";


        @Override
        protected Void doInBackground(Void... voids) {

/*

            try {
                Connection.Response loginFormResponse = Jsoup.connect("https://portal.librus.pl/rodzina/synergia/loguj")
                        .method(Connection.Method.GET)
                        .userAgent(USER_AGENT)
                        .execute();

                Document doc = Jsoup.connect("https://portal.librus.pl/rodzina/synergia/loguj").get();
               System.out.println(doc.select("widget-content").text());
            }catch (IOException e){
                System.out.println(e.getMessage());
            }


                Connection.Response res = doConnect(LOGIN_PAGE).execute();
                sessionCookie = res.cookie(SESSION_COOKIE);
            } catch (IOException e) {
                e.printStackTrace();
            }
/*
                FormElement loginForm = (FormElement)loginFormResponse.parse()
                        .select("div#LoginBox >  form").first();

                checkElement("Login Form", loginForm);

                Element loginField = loginForm.select("#Login").first();
                checkElement("Login Field", loginField);
                loginField.val(editTextUsername.getText().toString());

                Element passwordField = loginForm.select("#Pass").first();
                checkElement("Password Field", passwordField);
                passwordField.val(editTextPassword.getText().toString());

                // Now send the form for login
                Connection.Response loginActionResponse = loginForm.submit()
                        .cookies(loginFormResponse.cookies())
                        .userAgent(USER_AGENT)
                        .execute();

                System.out.println(loginActionResponse.parse().html());
*/

            try {
                Librus librus = new Librus();
                librus.login(editTextUsername.getText().toString(), editTextPassword.getText().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        private void checkElement(String name, Element elem) throws IOException {
            if(elem == null){
                throw new IOException("Unable to find " + name);
            }
        }

        private IOException librusError() {
            return new IOException("Something's wrong with librus page.");
        }



    }
}