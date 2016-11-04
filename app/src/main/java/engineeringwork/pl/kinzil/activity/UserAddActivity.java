package engineeringwork.pl.kinzil.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import engineeringwork.pl.kinzil.R;
import engineeringwork.pl.kinzil.containers.DatabaseHelper;
import engineeringwork.pl.kinzil.containers.User;

public class UserAddActivity extends AppCompatActivity {

    DatabaseHelper db;
    private Button loginButton;
    private Button createButton;
    private CheckBox AutorunCheckBox;
    private EditText loginEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);

        //autorun
        SharedPreferences shared = getSharedPreferences("data",MODE_PRIVATE);
        String actualLogin = shared.getString("login", null);
        String isAutorun = shared.getString("autorun", "false");
        if(actualLogin != null && isAutorun.equals("true"))
            login(actualLogin);

        db = DatabaseHelper.getInstance(this);

        loginButton = (Button) findViewById(R.id.LoginButton);
        createButton = (Button) findViewById(R.id.CreateButton);
        AutorunCheckBox = (CheckBox) findViewById(R.id.AutologCheckBox);
        loginEditText = (EditText) findViewById(R.id.LoginText);
        passwordEditText = (EditText) findViewById(R.id.PasswordText);

        onClickLogin();
        onClickRegister();
    }

    public void onClickLogin()
    {
        loginButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        hideKeyboard();
                        checkdata();
                    }
                }
        );
    }

    public void onClickRegister()
    {
        createButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        hideKeyboard();
                        showDialogRegister();
                    }
                }
        );
    }

    private void showDialogRegister()
    {
        final Dialog login = new Dialog(this, R.style.AlertDialogCustom);
        login.setContentView(R.layout.dialog_register);
        login.setTitle("Register");
        Button btnLogin = (Button) login.findViewById(R.id.btn_create);
        Button btnCancel = (Button) login.findViewById(R.id.btn_cancel);
        final EditText txtUsername = (EditText)login.findViewById(R.id.txt_name);
        final EditText txtPassword = (EditText)login.findViewById(R.id.txt_password);
        final EditText txtPasswordRepeat = (EditText)login.findViewById(R.id.txt_passwordRepeat);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsername.getText().toString().equals("")){
                    Toast.makeText(UserAddActivity.this, "Set login",Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtPassword.getText().toString().equals("")){
                    Toast.makeText(UserAddActivity.this, "Set password",Toast.LENGTH_LONG).show();
                    return;
                }
                if(txtPasswordRepeat.getText().toString().equals("")){
                    Toast.makeText(UserAddActivity.this, "Repeat password",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!txtPasswordRepeat.getText().toString().equals(txtPassword.getText().toString())){
                    Toast.makeText(UserAddActivity.this, "The repeat password is incorrect",Toast.LENGTH_LONG).show();
                    return;
                }
                User user = new User(txtUsername.getText().toString(), txtPassword.getText().toString());
                boolean isInserted = db.userInsert(user);
                if(isInserted) {
                    Toast.makeText(UserAddActivity.this, "Added user", Toast.LENGTH_LONG).show();
                    login.dismiss();
                }
                else
                    Toast.makeText(UserAddActivity.this, "User exist",Toast.LENGTH_LONG).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.dismiss();
            }
        });
        login.show();
    }

    private void checkdata()
    {
        if(loginEditText.getText().toString().equals("")){
            Toast.makeText(UserAddActivity.this, "Set login",Toast.LENGTH_LONG).show();
            return;
        }
        if(passwordEditText.getText().toString().equals("")){
            Toast.makeText(UserAddActivity.this, "Set password",Toast.LENGTH_LONG).show();
            return;
        }
        User user = new User(loginEditText.getText().toString(), passwordEditText.getText().toString());
        user = db.getUser(user);
        if(user == null) {
            Toast.makeText(UserAddActivity.this, "User not exist", Toast.LENGTH_LONG).show();
            return;
        }
        String password = passwordEditText.getText().toString();
        if(password.equals(user.getPassword())){
            autorun(AutorunCheckBox.isChecked());
            login(user.getLogin());
        } else {
            Toast.makeText(UserAddActivity.this, "Wrong password", Toast.LENGTH_LONG).show();
        }
    }

    private void login(String login)
    {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("login",login);
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void autorun(Boolean isAutorun)
    {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if(isAutorun)
            editor.putString("autorun","true");
        else
            editor.putString("autorun","false");
        editor.apply();
    }

    private void hideKeyboard()
    {
        if(getCurrentFocus() != null)
        {
            IBinder ibinder = getCurrentFocus().getWindowToken();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(ibinder, 0);
        }
    }
}
