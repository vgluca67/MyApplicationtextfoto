package com.example.myapplicationtextfoto;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import  android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.SeekBar;
//-----------------------------------
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
//----------------------------------------
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;









//-------------------------------------
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.os.Handler;
import android.text.style.ForegroundColorSpan;
import android.provider.DocumentsContract;
import android.net.Uri;
import java.io.BufferedReader;
import android.widget.ScrollView;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
//---------------------------------------

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
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import androidx.core.content.ContextCompat;

import android.widget.AdapterView;
import android.widget.Spinner;
import java.util.Locale;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.ImageView;
import android.content.Intent;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer.*;
import android.widget.ArrayAdapter;
import android.graphics.Color;

import androidx.annotation.Nullable;
public class MainActivity extends AppCompatActivity {
    Context context;
    private static final int REQUEST_CODE_TRACK1 = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int PICK_TXT_FILE = 1;
    private static final int REQUEST_IMAGE_CAPTURE2 = 1;
    private ImageView imageView;
    private String text;
    private Handler handler = new Handler();
    private int currentIndex = 0;
    String resultText, lenguage;
    private TextToSpeech tts;
    private SeekBar speedSeekBar,pitchSeekBar;
    private ScrollView scrollView;
    EditText editText;
    TextView textView;
    Button btncCpture, btnSpeack, btnstart, btnResetText, btnLoadText,btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // String textvision;
        btnResetText = findViewById(R.id.resettext);
        btnLoadText = findViewById(R.id.load_text);
        // imageView= findViewById(R.id.imageView);
     //   editText = findViewById(R.id.editTextname);
        textView = findViewById(R.id.textView);
        btnstart = findViewById(R.id.startButton);
        speedSeekBar = findViewById(R.id.speedseekBar);
        pitchSeekBar  = findViewById(R.id.pitchseekBar2);
        // String textToRead =" You can also select to disable deprecated APIs only up to a certain version of Qt.";
        //  highlightAndSpeak(editText,textToRead);
        btnStop = findViewById(R.id.btncapture);
        btnSpeack = findViewById(R.id.btnspeack);
        //text = editText.getText().toString();
        scrollView = findViewById(R.id.srollView);

        speedSeekBar.setProgress(100);
        pitchSeekBar.setProgress(100);
//---------------------------------------------------
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });
//---------------------------------------------------------
        // Activity result launcher per il file chooser
        ActivityResultLauncher<Intent> fileChooserLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            if (uri != null) {
                                leggiFileDaUri(uri);
                            }
                        }
                    }
                });
speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        float speed = 0.5f + (progress/100.0f);
        //textView.setText(text+speed);
        tts.setSpeechRate(speed);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
});
pitchSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress2, boolean b) {
        float pitch = 0.5f+(progress2/100.0f);
        //textView.setText(text+pitch);
        tts.setPitch(pitch);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
});
        btnResetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(" ");
              //  editText.setText(" ");
            }
        });
        btnLoadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openFilePicker(REQUEST_CODE_TRACK1);
                // Intent per aprire il file chooser
            //   Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            //    intent.addCategory(Intent.CATEGORY_OPENABLE);
             //   intent.setType("text/plain");
             //   intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
             //   fileChooserLauncher.launch(intent);
            //openFileChooser();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Text Files"), PICK_TXT_FILE);
               // fileChooserLauncher.launch(intent);
            }


        });
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startReading();
                String text = textView.getText().toString();
                highlightAndSpeak(text);

            }
        });
        Spinner spinner = findViewById(R.id.spinner);

        // Recupera l'array di stringhe dal resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_items, android.R.layout.simple_spinner_item);

        // Specifica il layout da utilizzare quando viene visualizzato l'elenco di scelte
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Applica l'adapter al spinner
        spinner.setAdapter(adapter);

        // Imposta un listener quando un elemento è selezionato
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Azione quando un elemento è selezionato
                String selectedItem = parentView.getItemAtPosition(position).toString();
                switch (selectedItem) {
                    case "English":
                        tts.setLanguage(Locale.ENGLISH);
                        break;
                    case "French":
                        tts.setLanguage(Locale.FRENCH);
                        break;
                    case "German":
                        tts.setLanguage(Locale.GERMAN);
                        break;
                    case "Italian":
                        tts.setLanguage(Locale.ITALIAN);
                        break;
                    default:
                        tts.setLanguage(Locale.ENGLISH);
                        break;
                }
                Toast.makeText(MainActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Azioni quando nulla è selezionato
            }
        });
/*
tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
  //  @Override
  //  public void onInit(int i) {
  @Override

  public void onInit(int status) {
      if (status == TextToSpeech.SUCCESS) {
          int result = tts.setLanguage(Locale.ENGLISH); // Imposta la lingua italiana
          if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
              // Gestisci errore lingua
          }
      } else {
          // Gestisci errore TTS non riuscito a inizializzarsi
      }

    }
})
;
 */
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dispatchTakePictureIntent();
            if (tts !=null && tts.isSpeaking()){
                tts.stop();
            }
            }
        });
        btnSpeack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    speak();
               // String text = textView.getText().toString();
              //  if (!text.isEmpty()) {
               //     tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    //textView.setText("Sto pronunciando: " + text);
             //   Intent intent = new Intent();
              //  intent.setType("image/*");
              //  intent.setAction(Intent.ACTION_GET_CONTENT);
              //  startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);

                }



        });
    }

    /*
    private void recognizeTextFromImage(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    // resultText.getText();
//resultText=findViewById(R.id.visionmargin);
                    // Mostra il testo estratto nell'interfaccia utente
                    // Ricorda di aggiornare l'UI nel thread principale
                })
                .addOnFailureListener(e -> {
                    // Gestisci gli errori
                });
    }
    private void speak() {
        if (tts != null) {
            //  String text = editText.getText().toString();
            String text = editText.getText().toString();
            //String text2 = learnEnglishTextView.getText().toString();

            tts.setLanguage(Locale.ENGLISH);
            tts.setPitch(0.8f); // Regola la pronuncia fonetica
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            // tts.speak(text2, TextToSpeech.QUEUE_FLUSH, null, null);

        }
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //    Log.e("TTS", "lingua non supportata");
                Toast.makeText(this, "lingua inglese non supportata", Toast.LENGTH_SHORT).show();
            } else {
                // TextToSpeech.QUEUE_FLUSH,null,"";
                tts.setLanguage(Locale.ENGLISH);
        startReading();
            }
            // tts.setLanguage(Locale.FRANCE);
        }
    }
    //  tts.setLanguage(Locale.ENGLISH);

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            processImage(imageBitmap);
        }
    }
private void startReading(){
        String text ="hello good morning";
        editText.setText(text);
        highlightAndSpeak(text);
}
    private void processImage(Bitmap bitmap) {
        // Inizializza un TextRecognizer
      com.google.android.gms.vision.text.TextRecognizer recognizer = new  com.google.android.gms.vision.text.TextRecognizer.Builder(getApplicationContext()).build();//.build();
        if (!recognizer.isOperational()) {
         //   Log.e("MainActivity", "Text recognizer could not be set up on your device");
            return;
        }

        // Crea un Frame dalla bitmap
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        // Estrai il testo dall'immagine
        SparseArray<TextBlock> textBlocks = recognizer.detect(frame);
        StringBuilder recognizedText = new StringBuilder();
        for (int i = 0; i < textBlocks.size(); ++i) {
            TextBlock textBlock = textBlocks.valueAt(i);
            recognizedText.append(textBlock.getValue());
            recognizedText.append("\n");
        }

        // Utilizza il Text-to-Speech per riprodurre il testo
        tts.speak(recognizedText.toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

     */
        /*
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ENGLISH);
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        runOnUiThread(() -> highlightText(currentIndex));
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        runOnUiThread(() -> currentIndex = 0); // Reset dell'indice
                    }

                    @Override
                    public void onError(String utteranceId) {
                    }

                    @Override
                    public void onRangeStart(String utteranceId, int start, int end, int frame) {
                        super.onRangeStart(utteranceId, start, end, frame);
                        runOnUiThread(() -> highlightText(start));
                    }
                });
            }
        });

        // startButton.setOnClickListener(v -> startReading());
    }


         */
//--------------------------------------------------
/*
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

 */
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            // Permesso già concesso
            openSecondActivity();
        }
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
       // startActivityForResult(Intent.createChooser(intent, "Select Text Files"), PICK_TXT_FILE);
        //   fileChooserLauncher.launch(intent);
    }
    @Override
/*
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permesso concesso
        //        openSecondActivity();
            } else {
                // Permesso negato
                Toast.makeText(this, "Permesso negato", Toast.LENGTH_SHORT).show();
            }
        }
    }



 */

     //---------------------------------

   // @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_TXT_FILE && resultCode == RESULT_OK) {
         // openSecondActivity();
            StringBuilder allText = new StringBuilder();

            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri fileUri = data.getClipData().getItemAt(i).getUri();
                        String content = readTextFromUri(fileUri);
                        if (content != null) {
                            allText.append(content).append("\n");
                        }
                    }
                } else if (data.getData() != null) {
                    Uri fileUri = data.getData();
                    String content = readTextFromUri(fileUri);
                    if (content != null) {
                        allText.append(content).append("\n");
                    }
                }

                textView.setText(allText.toString());
            }
        }
    }

    private String readTextFromUri(Uri uri) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //--------------------------------

    private void openSecondActivity() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    //--------------------------------------
    private void startReading() {
        text = textView.getText().toString();
        textView.setText(text);

        currentIndex = 0;
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1");
    }
/*
    private void highlightText(int start) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new BackgroundColorSpan(0xFFFFFF00), start, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
        textView.invalidate();
        //editText.setText(spannable);
        //editText.invalidate();
    }


 */
    /*
        private void highlightAndSpeak(String text) {
          //  TextToSpeech.OnUtteranceCompletedListener
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                //@Override
                public void onStart(String utteranceId) {}

                @Override
                public void onDone(String utteranceId) {
                    runOnUiThread(() -> {
                        // Reset any highlighting after done speaking
                        Spannable spannable = (Spannable) textView.getText();
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.black)), 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    });
                }

                @Override
                public void onError(String utteranceId) {}

                @Override
                public void onRangeStart(String utteranceId, int start, int end, int frame) {
                    // Highlight the spoken text
                    runOnUiThread(() -> {
                        Spannable spannable = (Spannable) textView.getText();
                        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(android.R.color.holo_blue_dark)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    });
                }
            });

            // Build the parameters for speech
            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

            // Speak
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "utteranceId");
        }
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    /*
        public void highlightAndSpeak( String textToRead1) {
            String[] words = textToRead1.split(" ");
            Spannable spannable = new SpannableString(textToRead1);
          //  highlightAndSpeak(textToRead1);
            // Iterate over each word
            new Thread(() -> {
                for (int i = 0; i < words.length; i++) {
                    String word = words[i];

                    // Calculate start and end indices of word
                    int start = textToRead1.indexOf(word);
                    int end = start + word.length();

                    // Set highlight color
                    spannable.setSpan(new BackgroundColorSpan(Color.BLUE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //    highlightAndSpeak(editText,textToRead);
                    // Update UI on main thread
                    runOnUiThread(() -> editText.setText(new SpannableString(textToRead1)));

                    // Speak the word
                    tts.speak(word, tts.QUEUE_FLUSH, null, null);

                    // Wait for the word to be spoken, add a slight delay for clarity
                    while (tts.isSpeaking()) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    // Remove highlight after speaking
                    spannable.removeSpan(new BackgroundColorSpan(Color.YELLOW));
                }
            }).start();

        }


     */
    /*
    private void openFilePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("download/*");
        startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (requestCode == REQUEST_CODE_TRACK1) {
               // textView.getText(uri);
                //playTrack(mediaPlayer1, uri);
            }// else if (requestCode == REQUEST_CODE_TRACK2) {
               // playTrack(mediaPlayer2, uri);
           // }
        }
    }

     */
    // Metodo per leggere il file dal URI
    /*
private void highlightAndSpeak(String text) {
    SpannableString spannableString = new SpannableString(text);

    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
        ForegroundColorSpan highlightSpan;

        @Override
        public void onStart(String utteranceId) { }

        @Override
        public void onDone(String utteranceId) {
            // Reset text highlighting when done
            handler.post(() -> textView.setText(text));
        }

        @Override
        public void onError(String utteranceId) { }

        @Override
        public void onRangeStart(String utteranceId, int start, int end, int frame) {
            // Highlight the spoken text
            handler.post(() -> {
                if (highlightSpan != null) {
                    spannableString.removeSpan(highlightSpan);
                }
                highlightSpan = new ForegroundColorSpan(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_dark));
                spannableString.setSpan(highlightSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
            });
        }
    });

    // Build parameters for speech
    Bundle params = new Bundle();
    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");

    // Speak
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "utteranceId");
}

     */
private void highlightAndSpeak(String text) {
    SpannableString spannableString = new SpannableString(text);

    tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
        ForegroundColorSpan highlightSpan;

        @Override
        public void onStart(String utteranceId) { }

        @Override
        public void onDone(String utteranceId) {
            handler.post(() -> textView.setText(text)); // Reset text highlighting when done
        }

        @Override
        public void onError(String utteranceId) { }

        @Override
        public void onRangeStart(String utteranceId, int start, int end, int frame) {
            handler.post(() -> {
                // Remove previous highlight
                if (highlightSpan != null) {
                    spannableString.removeSpan(highlightSpan);
                }
                // Highlight the current range
                highlightSpan = new ForegroundColorSpan(ContextCompat.getColor(MainActivity.this, android.R.color.holo_blue_dark));
                spannableString.setSpan(highlightSpan, start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);

                // Automatically scroll to the highlighted part
                scrollView.post(() -> {
                    int line = textView.getLayout().getLineForOffset(start);
                    int y = textView.getLayout().getLineTop(line);
                    scrollView.smoothScrollTo(0, y);
                });
            });
        }
    });

    // Build parameters for speech
    Bundle params = new Bundle();
    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId");
String cleanedText = text.replace("\n"," ");
    // Start speaking
    tts.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, params, "utteranceId");
}

    private void leggiFileDaUri(Uri uri) {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            textView.setText(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            textView.setText("Errore nella lettura del file.");
        }
    }
}