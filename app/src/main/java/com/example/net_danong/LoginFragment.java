package com.example.net_danong;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {
    //이메일 비밀번호 로그인 모듈 변수
    private FirebaseAuth mAuth;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;
    //내부 전환 newInstance 필수
    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        //이메일 비밀번호 로그인 모듈 변수
        mAuth = FirebaseAuth.getInstance();

        //변수들
        final EditText userEmail = (EditText) view.findViewById(R.id.et_eamil);
        final EditText userPw = (EditText) view.findViewById(R.id.et_password);
        Button btnSignUp = (Button) view.findViewById(R.id.btn_signUp);
        Button btnSignIn = (Button) view.findViewById(R.id.btn_logIn);
        Button btnFind = (Button) view.findViewById(R.id.btn_infoForgot);

        //로그인 버튼 눌렀을 때
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString();
                String password = userPw.getText().toString();
                //로그인 성공
                loginStart(email, password);
                userEmail.setText(null);
                userPw.setText(null);
                //닫아라


            }
        });

        //회원가입 버튼 눌렀을 때
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //가입하러 가기 버튼 눌렀을 때, 회원가입 페이지로 이동
                ((MainActivity)getActivity()).replaceJoinFrag(JoinFragment.newInstance());
            }
        });

        //아이디+비밀번호 찾기 버튼 눌렀을 때
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //관련이벤트 처리
            }
        });

        return view;
    }

    //@Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(),"연결이 해제되었습니다!",Toast.LENGTH_SHORT).show();
    }

    //public Boolean check; (로그인 처리 함수)
    public void loginStart(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        Toast.makeText(getActivity(), "존재하지 않는 id 입니다.", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(getActivity(), "비밀번호가 옳지 않습니다.", Toast.LENGTH_SHORT).show();
                    } catch (FirebaseNetworkException e) {
                        Toast.makeText(getActivity(), "Firebase NetworkException", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Exception", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    currentUser = mAuth.getCurrentUser();
                    Toast.makeText(getActivity(), "어서오세요, " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("Token", "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    // Get new Instance ID token
                                    String token = task.getResult().getToken();
                                    String uid = currentUser.getUid();

                                    Map<String,Object> map = new HashMap<>();
                                    map.put("pushToken",token);
                                    FirebaseDatabase.getInstance().getReference().child("chatUsers").child(uid).updateChildren(map);
                                }
                            });
                    ((MainActivity)getActivity()).removeLogFrag(LoginFragment.newInstance());


/*                    Fragment newFragment = new LoginFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();*/


                    //데이터 넘기기?
                    // Intent intent = new Intent(getActivity(), MainActivity.class);
                    // startActivity(intent);
                    //finish();
                }

            }
        });
    }


    //로그아웃 안했으면, 즉 로그인 되어있으면 자동으로 메인페이지로 이동시키기
//   @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            //currentUSer == null 인 경우가 로그인 안 된 상태,, null아니면 로그인 되어있다는 뜻..
//            //Intent intent = new Intent(getActivity(), MainActivity.class);
//            //startActivity(intent);
//            //finish();
//        }
//    }


//Class End
}

