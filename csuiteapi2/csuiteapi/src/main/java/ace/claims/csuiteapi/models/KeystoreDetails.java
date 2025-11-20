package ace.claims.csuiteapi.models;

import java.util.Date;

public class KeystoreDetails {
    private Date earliestCertExpiryDate;
    private String algorithmNames;

    public KeystoreDetails(Date earliestCertExpiryDate, String algorithmNames) {
        this.earliestCertExpiryDate = earliestCertExpiryDate;
        this.algorithmNames = algorithmNames;
    }

    public Date getEarliestCertExpiryDate() {
        return earliestCertExpiryDate;
    }

    public void setEarliestCertExpiryDate(Date earliestCertExpiryDate) {
        this.earliestCertExpiryDate = earliestCertExpiryDate;
    }

    public String getAlgorithmNames() {
        return algorithmNames;
    }

    public void setAlgorithmNames(String algorithmNames) {
        this.algorithmNames = algorithmNames;
    }
}
