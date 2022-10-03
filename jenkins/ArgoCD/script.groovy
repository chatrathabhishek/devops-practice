def installArgoCD() {
    echo "Installing ArgoCD..."
    sh """
        gcloud container clusters get-credentials ac-cluster-1 --zone us-central1-c --project sws-globalsre-cug01-qa
        kubectl create namespace argocd
        kubectl label namespace argocd istio-injection=enabled
        kustomize build kustomize/ | kubectl apply -f -
        kustomize build kustomize/overlays | kubectl apply -f -
    """
}

return this
