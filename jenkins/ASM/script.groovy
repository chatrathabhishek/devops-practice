def installASM() {
    echo "Installing ASM..."
    sh """
        gcloud container clusters get-credentials ac-gke --region us-central1 --project sws-globalsre-cug01-qa
        curl https://storage.googleapis.com/csm-artifacts/asm/asmcli_${params.asmMajor}.${params.asmMinor} > asmcli && chmod +x asmcli
        mkdir asm
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
    """
    kustomize = "kustomize/Overlay/"
    sh """
        echo "Installing cluster overlays!"
        kustomize build ${kustomize} | kubectl apply -f -
        echo "Installing certs!"
        ./gsreasmctl -cert ${params.projectKey} ${params.cluster} ${params.region} || CERT_RESULT=\$?
        echo "Finished installing certs!"
    """
}

return this