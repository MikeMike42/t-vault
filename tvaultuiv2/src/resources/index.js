const Resources = {
  csuiteDescription:
    'C-Suite is a simplified and centralized asset management solution for use on Claims Product Team. Create .jks certificates, manage S3 buckets, update credentials, and much more with our elegant, easy to understand interface. Let\'s get started!',
  loginNotes:
    ' This instance of C-Suite is for the Claims Product Team for internal use only. This app is not to be distributed to other teams without consent from CP Team. Do not use it for Tier1 application integrations.',
  createDescription:
    'Create .jks certificates with the press of a button so you can get to developing in your local environment faster. No more running a whole set of commands every time your cert expires!',
  accessDescription:
    'Access S3 buckets in a simple and straightforward way. Upload or browse to prod or nonprod environments with our simplified tool.',
  credentialsDescription:
    'Update credentials for something filler finish this text later inb4 jk wow that\'s fresh.',
  serviceAccount:
    "Service Account Activation. By default passwords are not set to autorotate <br/> <p>Note: When 'Enable Password Rotation' is turned off, the password for this service account will not be autorotated by C-Suite.</p>",
  offBoardConfirmation:
    'Are you sure you want to offboard this Service Account? This will not delete the Service Account from AD server.',
  offBoardDecommissionedConfirmation:
    "The Service account does not exist in Active Directory and can't be managed from C-Suite.",
  offBoardSuccessfull:
    'Offboarding of Service Account has been completed successfully.',
  svcNotEnableUpdateMsg:
    'days and will not be enabled for auto rotation by C-Suite even after it is expired. You need to make sure the password for this service account is getting rotated appropriately.',
  svcNotEnableOnboardMsg:
    'days and will not be enabled for auto rotation by C-Suite. You need to make sure the password for this service account is getting rotated appropriately.',
  svcPwdEnableNoValueMsg:
    'days and enabled for auto rotation by C-Suite. When you request for the password after this time, C-Suite will generate new password and make it available.',
  svcPwdEnableWithValueMsg:
    'and enabled for auto rotation by C-Suite. When you request for the password after this time, C-Suite will generate new password and make it available.',
  noSafeSecretFound:
    'Add a <strong>Folder</strong> and then you will be able to add <strong>Secrets</strong> to view them all here.',
  noSafeSecretFoundReadPerm:
    'No <strong>Folder</strong> or <strong>Secrets</strong> found here.',
  noUsersPermissionFound:
    'No <strong>Users</strong> are given permission to access this safe, add users to access the safe.',
  noGroupsPermissionFound:
    'No <strong>Groups</strong> are given permission to access this safe, add groups to access the safe.',
  noAwsPermissionFound:
    'No <strong>Applications</strong> are given permission to access this safe, add applications to access the safe.',
  noAwsPermissionFoundIam:
    'No <strong>Applications</strong> are given permission to access this IAM service account, add applications to access the account.',
  noAwsPermissionFoundAzure:
    'No <strong>Applications</strong> are given permission to access this Azure service account, add applications to access the account.',
  noAwsPermissionFoundCertificate:
    'No <strong>Applications</strong> are given permission to access this certificate, add applications to access the certificate.',
  noAwsPermissionFoundSvcAcc:
    'No <strong>Applications</strong> are given permission to access this service account, add applications to access the account.',
  noAppRolePermissionFound:
    'No <strong>App roles</strong> are given permission to access this safe, add approles to access the safe.',
  noAppRolePermissionFoundIam:
    'No <strong>App roles</strong> are given permission to access this IAM service account, add approles to access the account.',
  noAppRolePermissionFoundAzure:
    'No <strong>App roles</strong> are given permission to access this Azure service account, add approles to access the account.',
  noAppRolePermissionFoundCertificate:
    'No <strong>App roles</strong> are given permission to access this certificate, add approles to access the certificate.',
  noAppRolePermissionFoundSvcAcc:
    'No <strong>App roles</strong> are given permission to access this service account, add approles to access the account.',
  transferConfirmation:
    'Are you sure you want to transfer service account owner?',
  noCertificatesFound:
    'Once you add a <strong>Certificate</strong> you’ll see the  Corresponding <strong>Details</strong> here!',
  appRoles:
    'AppRoles operate a lot like safes, but they put the application as the logical unit for sharing. Additional Accessor ID and Secret ID pairs can easily be created through C-Suite, Secret IDs can only be accessed when downloaded.',
  certificateDesc: 'Create internal keystore.',
  noTransferOwnerAvailable:
    'Certificate may not be approved or rejected.Please follow the instructions mentioned in email',
  certificateGuide1:
    'Check whether the keystore you need is already in the list on the left. If one has already been created by a team member you can simply download the existing keystore. If you need the password to download a keystore please reach out to a team member.',
  certificateGuide2:
    'If the keystore you are looking for has not yet been created, use the below form to create a new internal keystore, which will then be available for download.',
  certificateGuide3:
    'Currently only the standard keystore template is used to create keystores, this includes: </br>Signature algorithm: SHA256-RSA.</br>Key usage : digitalSignature, keyEncipherment.</br>Only internal certificates are supported at this time.',
  certificateGuide4:
    'Suggestions for improvements and features are welcome, please reach out to Livingston.Mike@ace.aaa.com if you have any to share. For more information on how to manage certificates please go <a href="https://www.google.com" target="_blank">here</a>.',
  iamServiceAccountDesc:
    'IAM Service Accounts can only be modified in AWS/IAM. You can only view the details of this IAM Service Account and rotate the associated secret within C-Suite.',
  noAppRolesAvailable:
    'Once you create a <strong>New Approle</strong> you’ll be able to add <strong>Secret</strong> to view them all here!',
  azurePrincipal:
    'C-Suite can be used to manage secrets of service principals in Azure Active Directory. In order to self-service your service principals in Azure Active Directory there is a three-step process to onboard the account into C-Suite.',
  azureGuide1:
    '<strong>On-Boarding:</strong>This step brings the AAD service principals into C-Suite so that the secrets can be read or rotated through C-Suite. This is a one-time operation.',
  azureGuide2:
    '<strong>Service Principal Activation:</strong>The AAD service principal owner will Activate (rotate) the account password once after on-boarding the account into C-Suite. This process ensures that the secrets in C-Suite and Azure Active Directory are in sync.',
  azureGuide3:
    '<strong>Granting Permissions:</strong>When an AAD service principal is activated in C-Suite, the service principal owner can grant specific permissions to other users and groups allowing others to read and/or rotate the secrets for the AAD service principal as well through C-Suite.',
  azureActivateConfirmation:
    'During the activation, the password of the Azure service principal will be rotated to ensure Azure and C-Suite are in sync. If you want to continue with activation now please click the "ACTIVATE" button below and make sure to update any services depending on the service principal with its new password.',
};

export default { Resources };
