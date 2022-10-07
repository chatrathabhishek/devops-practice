def installASM() {
    echo "Installing ASM..."
    sh '''
        # echo "Install kubectl"
        # curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
        # install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
        # kubectl version --client
        alias kubectl="/home/ac185391_ncr_com/google-cloud-sdk/bin/kubectl"
        kubectl version --client
        cd jenkins/ASM
        gcloud container clusters get-credentials ${params.cluster} --region ${params.region} --project ${params.projectKey}
        curl https://storage.googleapis.com/csm-artifacts/asm/asmcli_${params.asmMajor}.${params.asmMinor} > asmcli && chmod +x asmcli
        if [[ -z "asm" ]]
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
        --option multicluster \
        --option iap-operator \
        --custom_overlay kustomize/asm-base/asm-overlay-istio-svc.yaml \
        --custom_overlay kustomize/asm-base/asm-overlay-telemetry.yaml       
    '''
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
