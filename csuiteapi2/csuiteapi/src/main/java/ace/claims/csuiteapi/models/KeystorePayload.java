package ace.claims.csuiteapi.models;

import java.util.List;

public class KeystorePayload {
    List<KeystoreUpload> uploads;
    String keystoreName;

    public KeystorePayload(List<KeystoreUpload> uploads, String keystoreName) {
        this.uploads = uploads;
        this.keystoreName = keystoreName;
    }

    public List<KeystoreUpload> getUploads() {
        return uploads;
    }

    public void setUploads(List<KeystoreUpload> uploads) {
        this.uploads = uploads;
    }

    public String getKeystoreName() {
        return keystoreName;
    }

    public void setKeystoreName(String keystoreName) {
        this.keystoreName = keystoreName;
    }
}
