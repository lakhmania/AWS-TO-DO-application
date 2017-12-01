#Team Information
* <Neha Lalwani>, <001268916>, <lalwani.n@husky.neu.edu>
* <Nirali Merchant>, <001268909>, <merchant.n@husky.neu.edu>
* <Chintan Koticha>, <001267049>, <koticha.c@husky.neu.edu>
* <Apoorva Lakhmani>, <001256312>, <lakhmani.a@husky.neu.edu>

#SSL Issuance
* Log-On to Namecheap and purchase a SSL certificate.
* Activation of SSL certificate through Domain Name DNV requires a CSR to be generated as follows:
* The openssl command to generate a private key is:

openssl genrsa 2048 > private-key.pem

* The CSR is generated based on the private key. The following command is used for the CSR creation:

openssl req -new -key private-key.pem -out csr.pem

* CSR is generated after filling out the relevant questions.
* Enter generated CSR in SSL activation , select Web-Server as Tomcat.
* Select DCV method as 'DNS-based' and for it to work create a CNAME record in AWS with details from namecheap.
* Once all details are confirmed, details are sent on registered email address.

#UPLOAD SERVER CERTIFICATE ON AWS IAM
* Upload certificate on AWS IAM using following command:

aws iam upload-server-certificate --server-certificate-name certificate_object_name --certificate-body file://certificate --private-key file://privatekey.pem --certificate-chain file://certificate_chain_file

* For chain file unzip certificate from Namecheap and use following command to concatenate certificate files into a single file to create a chain.

cat COMODORSADomainValidationSecureServerCA.crt COMODORSAAddTrustCA.crt AddTrustExternalCARoot.crt > ssl-bundle.crt

#ATTACHING SSL TO ELB
* Attach certificate ARN and sslPolicy in ELB's Listener resource to encrypt traffic between the load balancer and the clients that initiate HTTPS sessions, and traffic between the load balancer and your targets (EC2 instance in this case).

