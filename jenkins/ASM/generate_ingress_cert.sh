#!/bin/bash

set -e

#CLUSTER = $1
#REGION = $2
#PROJECT = $3
echo $1
echo $2
echo $3
CERT_DOMAIN=${CERT_DOMAIN:-"svc.id.goog"}
CA_CERT=${CA_CERT:-"istio-ca-secret"}
SECRET_NAME=${SECRET_NAME:-"ingress-gateway-cert"}
CERT_HOST=${CERT_HOST:-$1}
gcloud container clusters get-credentials $1 --region $2 --project $3
kubectl get secret ${CA_CERT} -n istio-system -o json | jq -r .data.\"ca-cert.pem\" | base64 --decode > ca-cert.pem
kubectl get secret istio-ca-secret -n istio-system -o json | jq -r .data.\"ca-key.pem\" | base64 --decode > ca-key.pem
echo "openssl req -out cert.csr -newkey rsa:2048 -nodes -keyout cert.key -subj \"/CN=${CERT_HOST}.${CERT_DOMAIN}/O=ac-test\""
openssl req -out cert.csr -newkey rsa:2048 -nodes -keyout cert.key -subj "/CN=${CERT_HOST}.${CERT_DOMAIN}/O=ac-test"
cat cert.csr
echo "\n"
cat cert.csr
openssl x509 -req -days 1825 -CA ca-cert.pem -CAkey ca-key.pem -set_serial 0 -in cert.csr -out cert.crt
kubectl create -n istio-system secret tls ${SECRET_NAME} --key=cert.key --cert=cert.crt || KUBECTL_ERROR=$?
#delete the files when finished
rm *.pem *.crt *.csr *.key
