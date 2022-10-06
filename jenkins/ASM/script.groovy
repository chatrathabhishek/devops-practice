def installASM() {
    echo "Installing ASM..."
    sh """
        gcloud container clusters get-credentials ac-cluster-1 --zone us-central1-c --project sws-globalsre-cug01-qa
        curl https://storage.googleapis.com/csm-artifacts/asm/asmcli_${asmMajor}.${asmMinor} > asmcli && chmod +x asmcli
        mkdir asm
        MAJOR=${asmMajor} MINOR=${asmMinor} POINT=${asmPoint} REV=${asmRev} CONFIG_VER=2 ./asmcli install \
        --project_id ${projectKey} \
        --cluster_name ${cluster} \
        --cluster_location ${region} \
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
    kustomize = "kustomize/projects/${projectKey}/${cluster}/"
    sh """
        echo "Installing cluster overlays!"
        kustomize build ${kustomize} | kubectl apply -f -
        echo "Installing certs!"
        ./gsreasmctl -cert ${projectKey} ${cluster} ${region} || CERT_RESULT=\$?
        echo "Finished installing certs!"
    """
}

return this
