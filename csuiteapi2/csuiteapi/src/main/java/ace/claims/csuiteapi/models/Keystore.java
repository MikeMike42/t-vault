package ace.claims.csuiteapi.models;

public class Keystore {
    private String name;
    private String type;
    private String containerName;
    private String certOwnerEmailId;
    private String applicationTag;
    private String createDate;
    private String expiryDate;
    private String[] dnsNames;
    private String algorithmNames;

    public Keystore(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Keystore(String name, String[] dnsNames, String expiryDate, String createDate, String applicationTag,
                    String certOwnerEmailId, String containerName, String type, String algorithmNames) {
        this.name = name;
        this.dnsNames = dnsNames;
        this.expiryDate = expiryDate;
        this.createDate = createDate;
        this.applicationTag = applicationTag;
        this.certOwnerEmailId = certOwnerEmailId;
        this.containerName = containerName;
        this.type = type;
        this.algorithmNames = algorithmNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCertOwnerEmailId() {
        return certOwnerEmailId;
    }

    public void setCertOwnerEmailId(String certOwnerEmailId) {
        this.certOwnerEmailId = certOwnerEmailId;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getApplicationTag() {
        return applicationTag;
    }

    public void setApplicationTag(String applicationTag) {
        this.applicationTag = applicationTag;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String[] getDnsNames() {
        return dnsNames;
    }

    public void setDnsNames(String[] dnsNames) {
        this.dnsNames = dnsNames;
    }

    public String getAlgorithmNames() {
        return algorithmNames;
    }

    public void setAlgorithmNames(String algorithmNames) {
        this.algorithmNames = algorithmNames;
    }
}
