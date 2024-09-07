package com.example.myapplicationtextfoto;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import  android.widget.Button;
import androidx.core.app.ActivityCompat;

//-----------------------------------
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import android.os.Bundle;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//-------------------------------------
import android.util.Log;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import  android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.SeekBar;
//-------------------------------------
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.os.Handler;
import android.text.style.ForegroundColorSpan;
import android.provider.DocumentsContract;

import java.io.BufferedReader;
import android.widget.ScrollView;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileOutputStream;
//import java.io.File;
//---------------------------------------
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.Text;
//--------------------------------------
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import android.content.Context;
import android.util.SparseArray;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import androidx.core.content.ContextCompat;

import android.widget.AdapterView;
import android.widget.Spinner;
import java.util.Locale;
import android.graphics.Bitmap;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import android.text.TextUtils;

import android.content.Intent;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer.*;
import android.widget.ArrayAdapter;
import android.graphics.Color;

import androidx.annotation.Nullable;
public class MainActivity2 extends AppCompatActivity {
    private  ActivityResultLauncher<Intent>createFileLauncher;
Button btnSaveTxt,btnLoadImage;
TextView textView2;
EditText editTextFileName;

ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int  REQUEST_WRITE_STORAGE = 112;
    private static final int  REQUEST_WRITE_STORAGE2 = 118;
    private static final int CREATE_FILE_REQUEST_CODE = 100;
    private static final int ANOTHER_ACTIVITY_REQUEST_CODE = 200;
    private static final int CREATE_FILE_REQUEST_CODE2 = 150;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
createFileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        result-> {
    if (result.getResultCode() == RESULT_OK){
        Intent data= result.getData();
        if(data !=null){
            Uri uri= data.getData();
            //writeToFile(uri,)
            saveTextToFile2(uri,textView2.getText().toString());
        }
    }
});
        checkStoragePermissions();

        btnSaveTxt=findViewById(R.id.button);
        btnLoadImage = findViewById(R.id.button2);
        imageView = findViewById(R.id.imageView);
textView2 = findViewById(R.id.textView2);
editTextFileName = findViewById(R.id.editTextName);


        // Verifica i permessi
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }


        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        btnSaveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // saveTextToFile(textView2.getText().toString());
                createFile();
            }
        });
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // Passa la bitmap al rilevatore di testo
                processImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


     */
    private void processImage(Bitmap bitmap) {
        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        textRecognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text result) {
                        // Testo riconosciuto
                        String detectedText = result.getText();
                       // Log.d("OCR", "Recognized text: " + detectedText);
                        textView2.setText(detectedText);
                                  //saveTextToPreferences(detectedText);
                        // Puoi aggiornarlo su un TextView se necessario
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    //@Override
                    public void onFailure(@NonNull Exception e) {
                     //   Log.e("OCR", "Text recognition failed.", e);
                        textView2.setText("riconoscimento fallito!");
                    }
                });
    }
/*
    private void saveTextToPreferences(String text) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("recognizedText", text);
        editor.apply(); // Non dimenticare di applicare le modifiche
    }
/*
    // All'interno del listener di successo
   .addOnSuccessListener(new OnSuccessListener<Text>() {
        @Override
        public void onSuccess(Text result) {
            String detectedText = result.getText();
            // Salva il testo in SharedPreferences
            saveTextToPreferences(detectedText);
        }
    })

*/
    /*

    private void saveTextToFile(String text) {
        // Crea un file nella directory "Documents" della memoria esterna
        File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File textFile = new File(documentsDir, "outfile1.txt");

        // Scrive il testo nel file
        try (FileOutputStream fos = new FileOutputStream(textFile)) {
            fos.write(text.getBytes());
            Toast.makeText(this, "File salvato: " + textFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Errore nel salvataggio del file", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permesso ottenuto", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permesso negato", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/
    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE,editTextFileName.getText() + ".txt");
        createFileLauncher.launch(intent);

    }
/*
    protected void ontP(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                saveTextToFile(uri, textView2.getText().toString());
            }
        }
    }


*/
    private void saveTextToFile2(Uri uri, String text) {
        try (FileOutputStream fos = (FileOutputStream) getContentResolver().openOutputStream(uri)) {
            if (fos != null) {
                fos.write(text.getBytes());
                Toast.makeText(this, "File salvato: " + uri.getPath(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Errore nel salvataggio del file", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Errore nel salvataggio del file", Toast.LENGTH_SHORT).show();
        }
    }

    /*


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permesso ottenuto", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permesso negato", Toast.LENGTH_SHORT).show();
            }
        }
    }

      */
    //--------------------
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        Uri imageUri = data.getData();

                // Codice per salvare il file


                //Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    // Passa la bitmap al rilevatore di testo
                    processImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
/*
        } else if (requestCode == ANOTHER_ACTIVITY_REQUEST_CODE) {
            // Gestisci il risultato da un'altra attività
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    saveTextToFile(textView2.getText().toString());
                }
            }
            // Fai qualcosa con il risultato
        }
    }

 */
    }
    //-------------------
private void saveTextToFile(String text) {
    // Crea un file nella directory "Documents" della memoria esterna
    File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    File textFile = new File(documentsDir, "output1.txt");
 //   if (isExternalStorageWritable()) {
        // Scrive il testo nel file
        try (FileOutputStream fos = new FileOutputStream(textFile)) {
            fos.write(text.getBytes());
            Toast.makeText(this, "File salvato: " + textFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Errore nel salvataggio del file", Toast.LENGTH_SHORT).show();
        }
    //}
}
/*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permesso ottenuto", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permesso negato", Toast.LENGTH_SHORT).show();
            }
        }
    }

 */
    /*
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

     */
    private void checkStoragePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        } else {
            // I permessi sono già stati concessi, inizia a scrivere sulla scheda SD
            startWritingProcess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permessi concessi, avvia il processo di scrittura
                startWritingProcess();
            } else {
                Toast.makeText(this, "Permessi negati. Impossibile scrivere nella scheda SD.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startWritingProcess() {
        // Qui puoi iniziare il tuo processo di scrittura su SD
        Toast.makeText(this, "Permessi concessi. Inizio scrittura sulla scheda SD.", Toast.LENGTH_SHORT).show();
    }
}



