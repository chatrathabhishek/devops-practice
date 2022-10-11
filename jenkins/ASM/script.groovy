def generate_ingress_cert() {
    echo "Generating certificate..."
    sh """
        export CERT_DOMAIN=${CERT_DOMAIN:-"svc.id.goog"}
        export CA_CERT=${CA_CERT:-"istio-ca-secret"}
        export SECRET_NAME=${SECRET_NAME:-"ingress-gateway-cert"}
        export CERT_HOST=${CERT_HOST:-"${param.CLUSTER}"}
        gcloud container clusters get-credentials ${params.cluster} --region ${params.region} --project ${params.projectKey}
        kubectl get secret ${CA_CERT} -n istio-system -o json | jq -r .data.\"ca-cert.pem\" | base64 --decode > ca-cert.pem
        kubectl get secret istio-ca-secret -n istio-system -o json | jq -r .data.\"ca-key.pem\" | base64 --decode > ca-key.pem
        openssl req -out cert.csr -newkey rsa:2048 -nodes -keyout cert.key -subj "/CN=${CERT_HOST}.${CERT_DOMAIN}/O=ac-test"
        cat cert.csr
        openssl x509 -req -days 1825 -CA ca-cert.pem -CAkey ca-key.pem -set_serial 0 -in cert.csr -out cert.crt
        kubectl create -n istio-system secret tls ${SECRET_NAME} --key=cert.key --cert=cert.crt || KUBECTL_ERROR=$?
        #delete the files when finished
        rm *.pem *.crt *.csr *.key
    """
}

def installASM() {
    echo "Installing ASM..."
    sh """
        alias kubectl=/home/${params.hostname}/google-cloud-sdk/bin/kubectl
        kubectl version --client
        cd jenkins/ASM
        gcloud container clusters get-credentials ${params.cluster} --region ${params.region} --project ${params.projectKey}
        curl https://storage.googleapis.com/csm-artifacts/asm/asmcli_${params.asmMajor}.${params.asmMinor} > asmcli && chmod +x asmcli
        if [[ ! -d "asm" ]]
        then
          mkdir asm
        fi
        pwd
        ls -al
        MAJOR=${params.asmMajor} MINOR=${params.asmMinor} POINT=${params.asmPoint} REV=${params.asmRev} CONFIG_VER=2 ./asmcli install \
        --project_id ${params.projectKey} \
        --cluster_name ${params.cluster} \
        --cluster_location ${params.region} \
        --enable_all \
        --enable-registration \
        --output_dir asm \
        --option cloud-tracing \
        --option legacy-default-egressgateway \
        --option envoy-access-log \
        --option iap-operator \
        --custom_overlay kustomize/asm-base/asm-overlay-istio-svc.yaml \
        --custom_overlay kustomize/asm-base/asm-overlay-telemetry.yaml       
    """
    kustomize = "kustomize/Overlay/"
    sh '''
        echo "Installing cluster overlays!"
        kustomize build ${kustomize} | kubectl apply -f -
        echo "Installing certs!"
        ./gsreasmctl -cert ${params.projectKey} ${params.cluster} ${params.region} || CERT_RESULT=\$?
        echo "Finished installing certs!"
    '''
}

return this
