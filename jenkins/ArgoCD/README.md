#Argo CD with istio

This part of the repo is used to install ArgoCD with istio

###Prereq

1. Create a GKE cluster
2. Install ASM on GKE for istio. You can refer https://github.com/chatrathabhishek/devops-practice/tree/main/jenkins/ASM
3. We are importing secrets from the secret manager, For that we need external secret controller. Install external secret controller as follows
    - Create a service accont with Secret Accessor role in IAM
    - Enable workload identity
    ```
    kubeclt create namespace external-secrets
    kubectl create serviceaccount kube-external-secrets --namespace external-secrets
    gcloud iam service-accounts add-iam-policy-binding external-secret@<GCP_PROJECT>.iam.gserviceaccount.com --role roles/iam.workloadIdentityUser --member "serviceAccount:sws-globalsre-cug01-qa.svc.id.goog[external-secrets/kube-external-secrets]"
    ```
    - Install external secret using helm
    ```
    helm repo add external-secrets https://charts.external-secrets.io
    helm install external-secrets external-secrets/external-secrets --set serviceAccount.annotations."iam\.gke\.io\/gcp-service-account"="external-secretst\@<GCP_PROJECT>.iam.gserviceaccount.com"
    ```
    - Create Secret store
    
    ```
    Edit the secret-store.yaml
    kubectl apply secret-store.yaml

4. Add the values for argocd-github-client-id, argocd-github-client-secret, argocd-github-username, argocd-github-token and github-argocd-webhook in secret manager
5. Run the jenkins pipeline
