def installArgoCD() {
    echo "Installing ArgoCD..."
    sh """
        gcloud container clusters get-credentials ${params.cluster} --region ${params.region} --project ${params.projectKey}
        kubectl create namespace argocd
        kubectl label namespace argocd istio-injection=enabled
        cd jenkins/ArgoCD
        kustomize build kustomize/ | kubectl apply -f -
        kustomize build kustomize/overlays | kubectl apply -f -
    """
}

def uninstallArgoCD() {
    echo "Uninstalling Argo..."
    sh """
        gcloud container clusters get-credentials ${params.cluster} --region ${params.region} --project ${params.projectKey}
        kustomize build kustomize/ | kubectl delete -f -
        kustomize build kustomize/overlays | kubectl delete -f -
    """
}

return this
