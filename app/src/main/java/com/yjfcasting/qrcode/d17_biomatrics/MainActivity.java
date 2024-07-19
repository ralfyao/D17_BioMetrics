package com.yjfcasting.qrcode.d17_biomatrics;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private BiometricManager manager;
    private BiometricPrompt.PromptInfo prompt;
    private BiometricPrompt biometricPrompt;
    private Button btn_biometric;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        Executor executor = Executors.newSingleThreadExecutor();
        btn_biometric = findViewById(R.id.btn_biometrics);
        manager = BiometricManager.from(this);
        prompt = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("指紋辨識")
                .setNegativeButtonText("TEST")// 要再增加呼叫設定NegativeButtonText，否則會有java.lang.IllegalArgumentException: Negative text must be set and non-empty.的錯誤
                .setSubtitle("請使用掃描器進行認證")
                .build();

        biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this)
                , new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication Error:" + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "驗證成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication Failed:", Toast.LENGTH_SHORT).show();
            }
        });

        btn_biometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(prompt);
            }
        });
        if(manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS){
            Toast.makeText(this, "BIOMETRIC_STRONG", Toast.LENGTH_SHORT).show();
        } else if (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS){
            Toast.makeText(this, "BIOMETRIC_WEAK", Toast.LENGTH_SHORT).show();
        } else if (manager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS){
            Toast.makeText(this, "DEVICE_CREDENTIAL", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "此裝置無法進行生物辨識", Toast.LENGTH_SHORT).show();
        }
    }
}