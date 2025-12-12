package ace.claims.csuiteapi.models;

public class KeystoreUpload {
    String fileName;
    String content;
    String keystoreName;

    public KeystoreUpload(String fileName, String content, String keystoreName) {
        this.fileName = fileName;
        this.content = content;
        this.keystoreName = keystoreName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeystoreName() {
        return keystoreName;
    }

    public void setKeystoreName(String keystoreName) {
        this.keystoreName = keystoreName;
    }
}
