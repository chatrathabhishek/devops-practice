def generate_ingress_cert() {
    echo "Installing certs..."
    sh """
        ls
        chmod +x jenkins/ASM/generate_ingress_cert.sh
        ./jenkins/ASM/generate_ingress_cert.sh ${params.cluster} ${params.region} ${params.projectKey}
        echo "Finished installing certs!"
    """
}

def install_kustomize() {
    echo "Installing Kustomize...."
    sh """
        curl -s "https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh" > install.sh
        chmod +x install.sh
        ./install.sh
        kustomize version
    """
}

def installASM() {
    echo "Installing ASM..."
    sh """
        #!/bin/bash
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
        # Note: need to create mesh before upgrading/installing a new cluster to project's mesh
        # echo "Enabling mesh"
        # gcloud alpha container hub mesh enable --project=${params.projectKey}
        # echo "Creating mesh"
        # ./asmcli create-mesh ${params.projectKey} ${params.projectKey}/${params.region}1/gke-1 ${params.projectKey}/${params.region}/gke-2
        echo "Installing ASM"
        MAJOR=${params.asmMajor} MINOR=${params.asmMinor} POINT=${params.asmPoint} REV=${params.asmRev} CONFIG_VER=2 ./asmcli install \
        --verbose \
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
        --custom_overlay asm-base/asm-overlay-istio-svc.yaml \
        --custom_overlay asm-base/asm-overlay-telemetry.yaml       
    """
    kustomize = "kustomize/Overlay/"
    sh """
        echo "Installing cluster overlays!"
        pwd
        cd jenkins/ASM
        kustomize build ${kustomize} | kubectl apply -f -
        cd /var/lib/jenkins/workspace/ASM/
    """
}

return this
