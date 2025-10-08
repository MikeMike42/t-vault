package ace.claims.csuiteapi.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.springframework.core.io.InputStreamResource;

public class SerializableDataContainer implements Serializable {
    private byte[] data;

    public SerializableDataContainer(InputStreamResource resource) throws IOException {
        try (InputStream is = resource.getInputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            this.data = bos.toByteArray();
        }
    }

    public InputStreamResource toInputStreamResource() {
        return new InputStreamResource(new ByteArrayInputStream(this.data));
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}