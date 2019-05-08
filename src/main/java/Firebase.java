import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.api.core.ApiFuture;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Firebase {
    public static Firestore db;

    public Firebase() {
        // Instantiate Firebase
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/Firebase/missmeyet-551c5-firebase-adminsdk.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://missmeyet-551c5.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();

        } catch (Exception FileNotFoundException) {
            // nothing
        }
    }

    public static ArrayList<String> getList(String s) {
        ArrayList<String> toReturn = new ArrayList<>();

        try {
            //asynchronously retrieve all documents
            ApiFuture<QuerySnapshot> future = db.collection(s).get();
            // future.get() blocks on response
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                toReturn.add(document.getId());
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    public static void addImage(String s, String f_id) {
        db.collection(s).document(f_id);

        // Create a Map to store the data we want to set
        Map<String, Object> docData = new HashMap<>();
        ApiFuture<WriteResult> future = db.collection(s).document(f_id).set(docData);
    }
}
